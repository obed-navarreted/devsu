package com.qk.mscliente.service;

import com.qk.mscliente.dto.ClienteDto;
import com.qk.mscliente.dto.SuccessResponse;
import com.qk.mscliente.entity.Cliente;
import com.qk.mscliente.exception.ResourceNotFoundException;
import com.qk.mscliente.repository.ClienteRepository;
import com.qk.mscliente.util.GeneralMehods;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.qk.mscliente.util.GeneralMehods.clienteToDto;
import static com.qk.mscliente.util.GeneralMehods.dtoToCliente;

@Component
@Transactional(readOnly = true)
public class ClienteService {
    private static final String REDIS_CLIENTE_HASH = "clientes";
    private final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ClienteService(ClienteRepository clienteRepository, RedisTemplate<String, Object> redisTemplate) {
        this.clienteRepository = clienteRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        List<Cliente> clientes = clienteRepository.findAllClientes();
        for (Cliente cliente : clientes) {
            ClienteDto clienteDto = clienteToDto(cliente);
            redisTemplate.opsForHash().put(REDIS_CLIENTE_HASH, String.valueOf(cliente.getId()),
                    GeneralMehods.toJsonString(clienteDto));
        }
    }

    @Transactional
    public SuccessResponse guardarCliente(ClienteDto clienteDto) {
        Cliente cliente = dtoToCliente(clienteDto);
        Cliente newCliente = clienteRepository.save(cliente);
        log.info("Cliente guardado exitosamente");
        ClienteDto event = clienteToDto(cliente);
        redisTemplate.opsForHash().put(REDIS_CLIENTE_HASH, String.valueOf(newCliente.getId()),
                        GeneralMehods.toJsonString(event));
        return new SuccessResponse(event);
    }

    public SuccessResponse obtenerTodosClientes() {
        log.info("Obteniendo todos los clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDto> clienteDtos = clientes.stream()
                .map(GeneralMehods::clienteToDto)
                .toList();
        return new SuccessResponse(clienteDtos);
    }

    public SuccessResponse obtenerClientePorId(Long id) {
        log.info("Obteniendo cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("(obtenerClientePorId) Cliente no encontrado con id: " + id));
        ClienteDto clienteDto = clienteToDto(cliente);
        return new SuccessResponse(clienteDto);
    }

    @Transactional
    public SuccessResponse actualizarCliente(Long id, ClienteDto clienteDto) {
        log.info("Actualizando cliente con id: {}", id);
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("(actualizarCliente) Cliente no encontrado con id: " + id));

        clienteExistente.setNombre(clienteDto.nombre());
        clienteExistente.setGenero(clienteDto.genero());
        clienteExistente.setEdad(clienteDto.edad());
        clienteExistente.setIdentificacion(clienteDto.identificacion());
        clienteExistente.setDireccion(clienteDto.direccion());
        clienteExistente.setTelefono(clienteDto.telefono());
        clienteExistente.setContrasena(clienteDto.contrasena());
        clienteExistente.setEstado(clienteDto.estado());

        clienteRepository.save(clienteExistente);

        redisTemplate.opsForHash().put(REDIS_CLIENTE_HASH, String.valueOf(id),
                GeneralMehods.toJsonString(clienteDto));
        log.info("Cliente actualizado exitosamente");
        return new SuccessResponse("Cliente actualizado exitosamente");
    }

    @Transactional
    public SuccessResponse eliminarCliente(Long id) {
        log.info("Eliminando cliente con id: {}", id);
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("(eliminarCliente) Cliente no encontrado con id: " + id));

        clienteRepository.delete(clienteExistente);
        redisTemplate.opsForHash().delete(REDIS_CLIENTE_HASH, String.valueOf(id));
        log.info("Cliente eliminado exitosamente");
        return new SuccessResponse("Cliente eliminado exitosamente");
    }
}
