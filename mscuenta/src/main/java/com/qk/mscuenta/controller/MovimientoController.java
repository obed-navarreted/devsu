package com.qk.mscuenta.controller;

import com.qk.mscuenta.dto.MovimientoDto;
import com.qk.mscuenta.dto.SuccessResponse;
import com.qk.mscuenta.service.MovimientoService;
import com.qk.mscuenta.util.MetodosGlobales;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<@NonNull SuccessResponse> registrarMovimiento(@RequestBody MovimientoDto movimientoDTO) {
        return MetodosGlobales.createdWithLocation(
                "", movimientoService.registrarMovimiento(movimientoDTO));
    }

    @PostMapping("/reporte")
    public ResponseEntity<@NonNull SuccessResponse> generarReporte(@RequestBody Map<String, Object> request) {
        Long clienteId = Long.valueOf(Objects.toString(request.get("clienteId")));
        LocalDateTime fechaInicio = LocalDateTime.parse(request.get("fechaInicio").toString());
        LocalDateTime fechaFin = LocalDateTime.parse(request.get("fechaFin").toString());
        return ResponseEntity.ok(
                movimientoService.generarReporteDeMovimientos(clienteId, fechaInicio, fechaFin));
    }
}
