package com.bifani.pacientes.service;

import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Role;
import com.bifani.pacientes.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioDetailsService service;

    @Test
    void loadUserByUsername_deveBuscarPeloEmailERetornarRoleComoAuthority() {
        Medico medico = new Medico();
        medico.setEmail("dr.joao@clinica.com");
        medico.setRole(Role.MEDICO);
        when(repository.findByEmail("dr.joao@clinica.com")).thenReturn(Optional.of(medico));

        UserDetails details = service.loadUserByUsername("dr.joao@clinica.com");

        assertThat(details.getUsername()).isEqualTo("dr.joao@clinica.com");
        assertThat(details.getAuthorities()).extracting("authority").containsExactly("ROLE_MEDICO");
    }

    @Test
    void loadUserByUsername_deveLancarExcecaoQuandoEmailNaoCadastrado() {
        when(repository.findByEmail("x@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("x@email.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
