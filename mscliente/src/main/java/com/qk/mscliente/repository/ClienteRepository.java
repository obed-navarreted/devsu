package com.qk.mscliente.repository;

import com.qk.mscliente.entity.Cliente;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClienteRepository extends JpaRepository<@NonNull Cliente, @NonNull Long> {
    @Query("SELECT distinct(c.id) FROM Cliente c")
    Set<Integer> getAllClientIds();

    @Query("SELECT c FROM Cliente c")
    List<Cliente> findAllClientes();
}
