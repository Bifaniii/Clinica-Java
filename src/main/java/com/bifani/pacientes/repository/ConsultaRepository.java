package com.bifani.pacientes.repository;

import com.bifani.pacientes.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
