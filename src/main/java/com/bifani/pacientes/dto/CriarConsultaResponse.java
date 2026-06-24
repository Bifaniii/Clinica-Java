package com.bifani.pacientes.dto;

import java.time.LocalDateTime;

public record CriarConsultaResponse(
        Long pacienteId,
        Long medicoId,
        LocalDateTime date,
        String description
) {
}
