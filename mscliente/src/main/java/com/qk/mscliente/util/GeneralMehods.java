package com.qk.mscliente.util;

import com.qk.mscliente.dto.ClienteDto;
import com.qk.mscliente.entity.Cliente;
import tools.jackson.databind.ObjectMapper;

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
}
