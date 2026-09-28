package com.bifani.pacientes.service;

import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Role;
import com.bifani.pacientes.repository.MedicoRepository;
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
class MedicoServiceTest {

    @Mock
    private MedicoRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MedicoService service;

    @Test
    void salvar_deveCriptografarSenhaEDefinirRoleMedico() {
        Medico medico = new Medico();
        medico.setPassword("123456");
        medico.setCrm("123456-SP");
        when(passwordEncoder.encode("123456")).thenReturn("hash-bcrypt");
        when(repository.save(medico)).thenReturn(medico);

        Medico salvo = service.salvar(medico);

        assertThat(salvo.getRole()).isEqualTo(Role.MEDICO);
        assertThat(salvo.getPassword()).isEqualTo("hash-bcrypt");
    }

    @Test
    void buscarMedicoPorId_deveLancarExcecaoQuandoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarMedicoPorId(id))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Médico não encontrado!");
    }

    @Test
    void deletar_naoDeveRemoverQuandoMedicoNaoExiste() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deletar(id)).isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).delete(any());
    }
}
