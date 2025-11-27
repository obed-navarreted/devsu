package com.qk.mscuenta.util;

import com.qk.mscuenta.dto.CuentaDto;
import com.qk.mscuenta.dto.MovimientoDto;
import com.qk.mscuenta.dto.SuccessResponse;
import com.qk.mscuenta.entity.Cuenta;
import com.qk.mscuenta.entity.Movimiento;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Map;

public class MetodosGlobales {
    private static final ObjectMapper mapper = new ObjectMapper();

    private MetodosGlobales() {
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

    public static String generarNumeroDeCuenta() {
        StringBuilder numeroCuenta = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int digito = (int) (Math.random() * 10);
            numeroCuenta.append(digito);
        }
        return numeroCuenta.toString();
    }

    public static ResponseEntity<@NonNull SuccessResponse> createdWithLocation(Object resourceId, SuccessResponse successResponse) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
        return ResponseEntity.created(location).body(successResponse);
    }

    public static Map<String, Object> convert(String json) {
        return mapper.readValue(json, new TypeReference<>() {
        });
    }

    public static MovimientoDto movimientoEntityToDTO(Movimiento movimiento) {
        return new MovimientoDto(
                movimiento.id(),
                movimiento.fecha(),
                movimiento.tipoMovimiento(),
                movimiento.valor(),
                movimiento.saldo(),
                movimiento.numeroCuenta()
        );
    }

    public static CuentaDto cuentaEntityToDto(Cuenta cuenta) {
        return new CuentaDto(
                cuenta.numeroCuenta(),
                cuenta.tipoCuenta(),
                cuenta.saldoInicial(),
                cuenta.estado(),
                cuenta.clienteId()
        );
    }

    public static Cuenta cuentaDtoToEntity(CuentaDto cuentaDto) {
        return new Cuenta(
                cuentaDto.numeroCuenta(),
                cuentaDto.tipoCuenta(),
                cuentaDto.saldoInicial(),
                cuentaDto.estado(),
                cuentaDto.clienteId()
        );
    }
}
