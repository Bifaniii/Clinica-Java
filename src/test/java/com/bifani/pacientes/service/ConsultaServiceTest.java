package com.bifani.pacientes.service;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.repository.ConsultaRepository;
import com.bifani.pacientes.repository.MedicoRepository;
import com.bifani.pacientes.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ConsultaService service;

    private Paciente paciente;
    private Medico medico;
    private final LocalDateTime amanha = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(UUID.randomUUID());
        medico = new Medico();
        medico.setId(UUID.randomUUID());
    }

    private CriarConsultaRequest request() {
        return new CriarConsultaRequest(paciente.getId(), medico.getId(), amanha, "Retorno cardiologia");
    }

    @Test
    void criarConsulta_deveVincularPacienteEMedicoCorretos() {
        when(pacienteRepository.findById(paciente.getId())).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(medico.getId())).thenReturn(Optional.of(medico));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(inv -> inv.getArgument(0));

        Consulta consulta = service.criarConsulta(request());

        assertThat(consulta.getPaciente()).isEqualTo(paciente);
        assertThat(consulta.getDoctor()).isEqualTo(medico);
        assertThat(consulta.getDate()).isEqualTo(amanha);
        assertThat(consulta.getDescription()).isEqualTo("Retorno cardiologia");
    }

    @Test
    void criarConsulta_deveFalharQuandoMedicoNaoExiste() {
        when(pacienteRepository.findById(paciente.getId())).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(medico.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criarConsulta(request()))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Médico não encontrado!");
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void criarConsulta_deveFalharQuandoPacienteNaoExiste() {
        when(pacienteRepository.findById(paciente.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.criarConsulta(request()))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Paciente não encontrado!");
        verifyNoInteractions(medicoRepository);
    }

    @Test
    void atualizar_deveAlterarAConsultaExistenteBuscandoPacientePeloPacienteId() {
        Consulta existente = Consulta.builder().id(1L).description("Antiga").build();
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(pacienteRepository.findById(paciente.getId())).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(medico.getId())).thenReturn(Optional.of(medico));
        when(consultaRepository.save(existente)).thenReturn(existente);

        service.atualizar(1L, request());

        ArgumentCaptor<Consulta> captor = ArgumentCaptor.forClass(Consulta.class);
        verify(consultaRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(1L);
        assertThat(captor.getValue().getPaciente()).isEqualTo(paciente);
        assertThat(captor.getValue().getDescription()).isEqualTo("Retorno cardiologia");
    }

    @Test
    void atualizar_deveFalharQuandoConsultaNaoExiste() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(99L, request()))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void delete_deveRemoverConsultaExistente() {
        Consulta existente = Consulta.builder().id(1L).build();
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(existente));

        service.delete(1L);

        verify(consultaRepository).delete(existente);
    }
}
