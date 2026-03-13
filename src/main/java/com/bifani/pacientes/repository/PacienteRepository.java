package com.bifani.pacientes.repository;

import com.bifani.pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}
