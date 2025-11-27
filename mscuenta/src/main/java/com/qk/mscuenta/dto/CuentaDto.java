package com.qk.mscuenta.dto;

import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CuentaDto(
        String numeroCuenta,
        @Pattern(regexp = "AHORRO|CORRIENTE", message = "El tipo de cuenta debe ser 'AHORRO' o 'CORRIENTE'")
        String tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        Long clienteId) {
}
