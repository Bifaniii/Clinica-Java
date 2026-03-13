package com.bifani.pacientes.repository;

import com.bifani.pacientes.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
