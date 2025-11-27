package com.qk.mscuenta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoDto(
        Long id,
        LocalDateTime fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        String numeroCuenta
) {
}
