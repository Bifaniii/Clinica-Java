package com.bifani.pacientes.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CriarConsultaRequest {
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime date;
    private String description;
}
