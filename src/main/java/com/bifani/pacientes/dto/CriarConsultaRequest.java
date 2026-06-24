package com.bifani.pacientes.dto;


import java.time.LocalDateTime;

public record CriarConsultaRequest (
    Long pacienteId,
    Long medicoId,
    LocalDateTime date,
    String description
){}
