package com.qk.mscuenta.repository;

import com.qk.mscuenta.entity.Movimiento;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<@NonNull Movimiento, @NonNull Long> {
    @Query("SELECT m FROM Movimiento m WHERE m.numeroCuenta IN :numeroCuentas and m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<Movimiento> findAllByNumeroCuentaIn(
            Collection<String> numeroCuentas,
            @Nonnull LocalDateTime fechaInicio,
            @Nonnull LocalDateTime fechaFin);
}
