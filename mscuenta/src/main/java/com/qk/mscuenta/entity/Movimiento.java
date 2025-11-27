package com.qk.mscuenta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@SequenceGenerator(name = "movimientos_id_seq", sequenceName = "movimientos_id_seq", allocationSize = 1)
public class Movimiento {
    @Id
    @GeneratedValue(generator = "movimientos_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime fecha;
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;

    private String numeroCuenta;

    public Movimiento() {
    }

    public Movimiento(
            Long id,
            LocalDateTime fecha,
            String tipoMovimiento,
            BigDecimal valor,
            BigDecimal saldo,
            String numeroCuenta) {
        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.valor = valor;
        this.saldo = saldo;
        this.numeroCuenta = numeroCuenta;
    }

    public Long id() {
        return id;
    }

    public Movimiento setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDateTime fecha() {
        return fecha;
    }

    public Movimiento setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public String tipoMovimiento() {
        return tipoMovimiento;
    }

    public Movimiento setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        return this;
    }

    public BigDecimal valor() {
        return valor;
    }

    public Movimiento setValor(BigDecimal valor) {
        this.valor = valor;
        return this;
    }

    public BigDecimal saldo() {
        return saldo;
    }

    public Movimiento setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
        return this;
    }

    public String numeroCuenta() {
        return numeroCuenta;
    }

    public Movimiento setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        return this;
    }
}
