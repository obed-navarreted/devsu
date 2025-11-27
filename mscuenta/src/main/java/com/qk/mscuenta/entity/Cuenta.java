package com.qk.mscuenta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Cuenta {
    @Id
    private String numeroCuenta;

    private String tipoCuenta;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private Long clienteId;

    public Cuenta() {
    }

    public Cuenta(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            Boolean estado,
            Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public String numeroCuenta() {
        return numeroCuenta;
    }

    public Cuenta setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        return this;
    }

    public String tipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public BigDecimal saldoInicial() {
        return saldoInicial;
    }

    public Cuenta setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
        return this;
    }

    public Boolean estado() {
        return estado;
    }

    public Cuenta setEstado(Boolean estado) {
        this.estado = estado;
        return this;
    }

    public Long clienteId() {
        return clienteId;
    }

    public Cuenta setClienteId(Long clienteId) {
        this.clienteId = clienteId;
        return this;
    }
}
