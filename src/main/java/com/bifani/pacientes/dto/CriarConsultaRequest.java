package com.bifani.pacientes.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CriarConsultaRequest (
    @NotNull(message = "Paciente é obrigatório")
    UUID pacienteId,
    @NotNull(message = "Médico é obrigatório")
    UUID medicoId,
    @NotNull(message = "Data é obrigatória")
    @Future(message = "A consulta deve ser marcada para uma data futura")
    LocalDateTime date,
    @NotBlank(message = "Descrição é obrigatória")
    String description
){}
