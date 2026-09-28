package com.bifani.pacientes.controller;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.exception.GlobalExceptionHandler;
import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.service.ConsultaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    @Mock
    private ConsultaService service;

    @InjectMocks
    private ConsultaController controller;

    private MockMvc mockMvc;

    private final UUID pacienteId = UUID.randomUUID();
    private final UUID medicoId = UUID.randomUUID();
    private final String data = LocalDateTime.now().plusDays(1).withNano(0).toString();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private Consulta consulta() {
        Paciente paciente = new Paciente();
        paciente.setId(pacienteId);
        Medico medico = new Medico();
        medico.setId(medicoId);
        return Consulta.builder()
                .id(1L)
                .paciente(paciente)
                .doctor(medico)
                .date(LocalDateTime.parse(data))
                .description("Retorno cardiologia")
                .build();
    }

    private String json(String date, String description) {
        return """
                {"pacienteId": "%s", "medicoId": "%s", "date": "%s", "description": "%s"}
                """.formatted(pacienteId, medicoId, date, description);
    }

    @Test
    void criarConsulta_deveRetornar201ComIdsDoPacienteEDoMedico() throws Exception {
        when(service.criarConsulta(any(CriarConsultaRequest.class))).thenReturn(consulta());

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(data, "Retorno cardiologia")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pacienteId").value(pacienteId.toString()))
                .andExpect(jsonPath("$.medicoId").value(medicoId.toString()));
    }

    @Test
    void criarConsulta_deveRetornar400QuandoDataNoPassado() throws Exception {
        String ontem = LocalDateTime.now().minusDays(1).withNano(0).toString();

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(ontem, "Retorno cardiologia")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(service);
    }

    @Test
    void criarConsulta_deveRetornar400QuandoDescricaoEmBranco() throws Exception {
        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(data, "")))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void criarConsulta_deveRetornar404QuandoMedicoNaoExiste() throws Exception {
        when(service.criarConsulta(any(CriarConsultaRequest.class)))
                .thenThrow(new RecursoNaoEncontradoException("Médico não encontrado!"));

        mockMvc.perform(post("/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(data, "Retorno cardiologia")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Médico não encontrado!"));
    }

    @Test
    void atualizarConsulta_deveRetornar200() throws Exception {
        when(service.atualizar(eq(1L), any(CriarConsultaRequest.class))).thenReturn(consulta());

        mockMvc.perform(put("/consultas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(data, "Retorno cardiologia")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Retorno cardiologia"));
    }

    @Test
    void deletarConsulta_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/consultas/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
