package com.bifani.pacientes.controller;

import com.bifani.pacientes.exception.GlobalExceptionHandler;
import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PacienteControllerTest {

    @Mock
    private PacienteService service;

    @InjectMocks
    private PacienteController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void criar_deveRetornar201SemExporASenha() throws Exception {
        when(service.salvar(any(Paciente.class))).thenAnswer(inv -> {
            Paciente p = inv.getArgument(0);
            p.setPassword("hash-bcrypt");
            return p;
        });

        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Maria", "email": "maria@email.com", "password": "123456",
                                 "age": 30, "city": "São Paulo"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("maria@email.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void criar_deveRetornar400QuandoIdadeAcimaDoLimite() throws Exception {
        mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "Maria", "email": "maria@email.com", "password": "123456",
                                 "age": 150, "city": "São Paulo"}
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void buscar_deveRetornar404QuandoPacienteNaoExiste() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.buscarPorId(id)).thenThrow(new RecursoNaoEncontradoException("Paciente não encontrado!"));

        mockMvc.perform(get("/pacientes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Paciente não encontrado!"));
    }
}
