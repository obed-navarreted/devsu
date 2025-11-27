package com.qk.mscuenta.service;

import com.qk.mscuenta.dto.CuentaDto;
import com.qk.mscuenta.dto.SuccessResponse;
import com.qk.mscuenta.entity.Cuenta;
import com.qk.mscuenta.exception.ResourceNotFoundException;
import com.qk.mscuenta.repository.CuentaRepository;
import com.qk.mscuenta.util.MetodosGlobales;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.qk.mscuenta.util.MetodosGlobales.cuentaEntityToDto;

@Service
@Transactional(readOnly = true)
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public CuentaService(CuentaRepository cuentaRepository, RedisTemplate<String, Object> redisTemplate) {
        this.cuentaRepository = cuentaRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public SuccessResponse crearCuenta(CuentaDto cuentaDto) {
        Long clienteId = cuentaDto.clienteId();
        String numeroCuenta = MetodosGlobales.generarNumeroDeCuenta();
        boolean existeElCliente = redisTemplate.opsForHash().get("clientes", String.valueOf(clienteId)) != null;
        if (!existeElCliente) {
            throw new ResourceNotFoundException("El cliente no existe");
        }

        Cuenta cuenta = MetodosGlobales.cuentaDtoToEntity(cuentaDto);
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setEstado(true);
        cuenta.setSaldoInicial(BigDecimal.ZERO);
        Cuenta nueva = cuentaRepository.save(cuenta);
        return new SuccessResponse(cuentaEntityToDto(nueva));
    }

    @Transactional
    public SuccessResponse cambiarEstadoDeLaCuenta(String numeroCuenta, Boolean estado) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setEstado(estado);
        Cuenta cuentaDesactivada = cuentaRepository.save(cuenta);
        return new SuccessResponse(cuentaEntityToDto(cuentaDesactivada));
    }

    @Transactional
    public SuccessResponse desactivarCuentasPorClienteId(Long clienteId) {
        boolean existeCuenta = cuentaRepository.existsByClienteId(clienteId);
        if (!existeCuenta) {
            throw new ResourceNotFoundException("No existen cuentas para el cliente con ID: " + clienteId);
        }
        var cuentas = cuentaRepository.findAllByClienteId(clienteId);
        for (Cuenta cuenta : cuentas) {
            cuenta.setEstado(false);
            cuentaRepository.save(cuenta);
        }

        return new SuccessResponse("Cuentas desactivadas correctamente para el cliente con ID: " + clienteId);
    }

    @Transactional
    public SuccessResponse eliminarCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setEstado(false);
        cuentaRepository.delete(cuenta);
        return new SuccessResponse("Cuenta eliminada correctamente");
    }

    public SuccessResponse obtenerCuentaByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return new SuccessResponse(cuentaEntityToDto(cuenta));
    }
}
