package com.bifani.pacientes.dto;

import com.bifani.pacientes.model.Consulta;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarConsultaResponse(
        Long id,
        UUID pacienteId,
        UUID medicoId,
        LocalDateTime date,
        String description
) {
    public CriarConsultaResponse(Consulta consulta) {
        this(consulta.getId(), consulta.getPaciente().getId(), consulta.getDoctor().getId(),
                consulta.getDate(), consulta.getDescription());
    }
}
