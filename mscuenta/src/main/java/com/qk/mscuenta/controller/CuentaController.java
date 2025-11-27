package com.qk.mscuenta.controller;

import com.qk.mscuenta.dto.CuentaDto;
import com.qk.mscuenta.dto.SuccessResponse;
import com.qk.mscuenta.service.CuentaService;
import com.qk.mscuenta.util.MetodosGlobales;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<@NonNull SuccessResponse> crearCuenta(
            @RequestBody CuentaDto cuentaDto
    ) {
        return MetodosGlobales.createdWithLocation(
                "", cuentaService.crearCuenta(cuentaDto));
    }

    @PutMapping
    public ResponseEntity<@NonNull SuccessResponse> cambiarEstadoDeLaCuenta(
            @RequestParam String numeroCuenta,
            @RequestParam Boolean nuevoEstado) {
        return ResponseEntity.ok(cuentaService.cambiarEstadoDeLaCuenta(numeroCuenta, nuevoEstado));
    }

    @GetMapping
    public ResponseEntity<@NonNull SuccessResponse> obtenerCuentaByNumeroCuenta(
            @RequestParam String numeroCuenta) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaByNumeroCuenta(numeroCuenta));
    }

    @DeleteMapping("/desactivar-por-cliente")
    public ResponseEntity<@NonNull SuccessResponse> desactivarCuentasPorClienteId(
            @RequestParam Long clienteId) {
        return ResponseEntity.ok(cuentaService.desactivarCuentasPorClienteId(clienteId));
    }

    @DeleteMapping
    public ResponseEntity<@NonNull SuccessResponse> eliminarCuentaByNumeroCuenta(
            @RequestParam String numeroCuenta) {
        return ResponseEntity.ok(cuentaService.eliminarCuenta(numeroCuenta));
    }
}
