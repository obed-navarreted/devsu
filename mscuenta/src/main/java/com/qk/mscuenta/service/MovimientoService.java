package com.qk.mscuenta.service;

import com.qk.mscuenta.dto.CuentaDto;
import com.qk.mscuenta.dto.MovimientoDto;
import com.qk.mscuenta.dto.SuccessResponse;
import com.qk.mscuenta.entity.Cuenta;
import com.qk.mscuenta.entity.Movimiento;
import com.qk.mscuenta.exception.ResourceNotFoundException;
import com.qk.mscuenta.exception.SaldoNoDisponibleException;
import com.qk.mscuenta.repository.CuentaRepository;
import com.qk.mscuenta.repository.MovimientoRepository;
import com.qk.mscuenta.util.MetodosGlobales;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.qk.mscuenta.util.MetodosGlobales.movimientoEntityToDTO;

@Service
@Transactional(readOnly = true)
public class MovimientoService {
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public MovimientoService(CuentaRepository cuentaRepository,
                             MovimientoRepository movimientoRepository,
                             RedisTemplate<String, Object> redisTemplate) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public SuccessResponse registrarMovimiento(MovimientoDto movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findCuentaByNumeroCuenta(movimientoDTO.numeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        if (Boolean.FALSE.equals(cuenta.estado())) {
            throw new ResourceNotFoundException("La cuenta está inactiva");
        }

        boolean existeElCliente = redisTemplate.opsForHash()
                .get("clientes", String.valueOf(cuenta.clienteId())) != null;
        if (!existeElCliente) {
            throw new ResourceNotFoundException("El cliente asociado a la cuenta es inválido o no existe");
        }

        BigDecimal saldoActual = cuenta.saldoInicial();
        BigDecimal valorMovimiento = movimientoDTO.valor();

        if (cuenta.tipoCuenta().equalsIgnoreCase("AHORRO") &&
                saldoActual.add(valorMovimiento).compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible");
        }

        BigDecimal nuevoSaldo = saldoActual.add(valorMovimiento);

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(valorMovimiento.compareTo(BigDecimal.ZERO) > 0 ? "DEPOSITO" : "RETIRO");
        movimiento.setValor(valorMovimiento);
        movimiento.setSaldo(nuevoSaldo);
        movimiento.setNumeroCuenta(movimientoDTO.numeroCuenta());

        Movimiento guardado = movimientoRepository.save(movimiento);
        return new SuccessResponse(movimientoEntityToDTO(guardado));
    }

    public SuccessResponse generarReporteDeMovimientos(
            Long clienteId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        String clienteRaw = (String) redisTemplate.opsForHash()
                .get("clientes", String.valueOf(clienteId));
        if (clienteRaw == null) {
            throw new ResourceNotFoundException("El cliente no existe");
        }

        Map<String, Object> clienteMap = MetodosGlobales.convert(clienteRaw);
        List<Cuenta> cuentas = cuentaRepository.findAllByClienteId(clienteId);
        List<Movimiento> movimientos = movimientoRepository.findAllByNumeroCuentaIn(
                cuentas.stream().map(Cuenta::numeroCuenta).toList(), fechaInicio, fechaFin
        );

        List<CuentaDto> cuentasDto = cuentas
                .stream()
                .map(MetodosGlobales::cuentaEntityToDto)
                .toList();
        List<MovimientoDto> movimientoDtos = movimientos
                .stream()
                .map(MetodosGlobales::movimientoEntityToDTO)
                .toList();

        Map<String, Object> result = Map.of(
                "cliente", clienteMap,
                "cuentas", cuentasDto,
                "movimientos", movimientoDtos
        );
        return new SuccessResponse(result);
    }
}
