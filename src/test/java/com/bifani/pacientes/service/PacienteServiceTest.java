package com.bifani.pacientes.service;

import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.exception.RegraDeNegocioException;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.model.Role;
import com.bifani.pacientes.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PacienteService service;

    private Paciente paciente(int idade, String cidade) {
        Paciente paciente = new Paciente();
        paciente.setName("Maria");
        paciente.setEmail("maria@email.com");
        paciente.setPassword("123456");
        paciente.setAge(idade);
        paciente.setCity(cidade);
        return paciente;
    }

    @Test
    void salvar_deveCriptografarSenhaEDefinirRolePaciente() {
        when(passwordEncoder.encode("123456")).thenReturn("hash-bcrypt");
        when(repository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        Paciente salvo = service.salvar(paciente(30, "São Paulo"));

        assertThat(salvo.getPassword()).isEqualTo("hash-bcrypt");
        assertThat(salvo.getRole()).isEqualTo(Role.PACIENTE);
    }

    @Test
    void salvar_deveRejeitarIdadeNegativa() {
        assertThatThrownBy(() -> service.salvar(paciente(-1, "São Paulo")))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessage("Idade inválida");
        verify(repository, never()).save(any());
    }

    @Test
    void salvar_deveRejeitarCidadeNulaOuEmBranco() {
        assertThatThrownBy(() -> service.salvar(paciente(30, null))).isInstanceOf(RegraDeNegocioException.class);
        assertThatThrownBy(() -> service.salvar(paciente(30, "  "))).isInstanceOf(RegraDeNegocioException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void atualizar_deveFalharQuandoPacienteNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(id, paciente(30, "São Paulo")))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void atualizar_deveManterOIdDoPath() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(new Paciente()));
        when(repository.save(any(Paciente.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.atualizar(id, paciente(30, "São Paulo")).getId()).isEqualTo(id);
    }

    @Test
    void deletar_deveRemoverPacienteExistente() {
        UUID id = UUID.randomUUID();
        Paciente existente = new Paciente();
        when(repository.findById(id)).thenReturn(Optional.of(existente));

        service.deletar(id);

        verify(repository).delete(existente);
    }
}
