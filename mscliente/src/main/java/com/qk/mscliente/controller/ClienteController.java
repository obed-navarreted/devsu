package com.qk.mscliente.controller;

import com.qk.mscliente.dto.ClienteDto;
import com.qk.mscliente.service.ClienteService;
import com.qk.mscliente.dto.SuccessResponse;
import com.qk.mscliente.util.GeneralMehods;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/clientes")
@RestController
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<@NonNull SuccessResponse> guardarCliente(@Validated @RequestBody ClienteDto clienteDto) {
        return GeneralMehods.createdWithLocation("", clienteService.guardarCliente(clienteDto));
    }

    @GetMapping
    public ResponseEntity<@NonNull SuccessResponse> obtenerTodosClientes() {
        return ResponseEntity.ok(clienteService.obtenerTodosClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull SuccessResponse> obtenerClientePorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull SuccessResponse> actualizarCliente(@PathVariable Long id, @Validated @RequestBody ClienteDto clienteDto) {
        return ResponseEntity.ok(clienteService.actualizarCliente(id, clienteDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull SuccessResponse> eliminarCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.eliminarCliente(id));
    }
}
