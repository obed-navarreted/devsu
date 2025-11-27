package com.qk.mscliente.util;

import com.qk.mscliente.dto.ClienteDto;
import com.qk.mscliente.dto.SuccessResponse;
import com.qk.mscliente.entity.Cliente;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;

public class GeneralMehods {
    private static final ObjectMapper mapper = new ObjectMapper();

    private GeneralMehods() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }

    public static String toJsonString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON string", e);
        }
    }

    public static ClienteDto clienteToDto(Cliente cliente) {
        return new ClienteDto(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getContrasena(),
                cliente.getEstado()
        );
    }

    public static Cliente dtoToCliente(ClienteDto clienteDto) {
        return new Cliente(
                clienteDto.id(),
                clienteDto.nombre(),
                clienteDto.genero(),
                clienteDto.edad(),
                clienteDto.identificacion(),
                clienteDto.direccion(),
                clienteDto.telefono(),
                clienteDto.contrasena(),
                clienteDto.estado()
        );
    }

    public static ResponseEntity<@NonNull SuccessResponse> createdWithLocation(Object resourceId, SuccessResponse successResponse) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location).body(successResponse);
    }
}
