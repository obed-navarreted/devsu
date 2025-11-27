package com.qk.mscuenta.repository;

import com.qk.mscuenta.entity.Cuenta;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<@NonNull Cuenta, @NonNull String> {
    boolean existsByClienteId(@NonNull Long clienteId);
    Optional<Cuenta> findCuentaByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findAllByClienteId(@NonNull Long clienteId);
}
