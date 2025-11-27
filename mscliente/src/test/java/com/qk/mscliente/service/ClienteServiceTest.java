package com.qk.mscliente.service;

import com.qk.mscliente.dto.ClienteDto;
import com.qk.mscliente.dto.SuccessResponse;
import com.qk.mscliente.entity.Cliente;
import com.qk.mscliente.exception.ResourceNotFoundException;
import com.qk.mscliente.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    // MOCK CLAVE: Necesario para simular redisTemplate.opsForHash()
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("actualizarCliente: Debe actualizar DB y Redis cuando el cliente existe")
    void actualizarCliente_Exito() {
        Long clienteId = 1L;

        ClienteDto dtoActualizar = new ClienteDto(
                clienteId, "Obed Nuevo", "M", 28, "ID-123", "Managua", "88888888", "pass123", true
        );

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(clienteId);
        clienteExistente.setNombre("Obed Viejo");

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        SuccessResponse response = clienteService.actualizarCliente(clienteId, dtoActualizar);

        verify(clienteRepository).findById(clienteId);

        assertEquals("Obed Nuevo", clienteExistente.getNombre());
        assertEquals(28, clienteExistente.getEdad());

        verify(clienteRepository).save(clienteExistente);

        verify(hashOperations).put(eq("clientes"), eq(String.valueOf(clienteId)), anyString());

        assertEquals("Cliente actualizado exitosamente", response.data());
    }

    @Test
    @DisplayName("actualizarCliente: Debe lanzar excepción si el ID no existe")
    void actualizarCliente_NoEncontrado() {
        Long idInexistente = 99L;
        ClienteDto dto = new ClienteDto(idInexistente, "Test", "M", 20, "ID", "Dir", "Tel", "Pass", false);

        when(clienteRepository.findById(idInexistente)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.actualizarCliente(idInexistente, dto);
        });

        assertEquals("(actualizarCliente) Cliente no encontrado con id: " + idInexistente, exception.getMessage());

        verify(clienteRepository, times(0)).save(any());
        verify(redisTemplate, times(0)).opsForHash();
    }
}