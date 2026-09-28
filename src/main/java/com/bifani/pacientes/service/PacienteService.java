package com.bifani.pacientes.service;

import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.exception.RegraDeNegocioException;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.model.Role;
import com.bifani.pacientes.repository.PacienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PacienteService {
    private final PacienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PacienteService(PacienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Paciente> listarTodos() {
        return repository.findAll();
    }

    public Paciente salvar(Paciente paciente) {
        if (paciente.getAge() < 0) {
            throw new RegraDeNegocioException("Idade inválida");
        } else if (paciente.getCity() == null || paciente.getCity().isBlank()) {
            throw new RegraDeNegocioException("Cidade não informada!");
        }
        paciente.setRole(Role.PACIENTE);
        paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
        return repository.save(paciente);
    }

    public Paciente atualizar(UUID id, Paciente paciente) {
        buscarPorId(id);
        paciente.setId(id);
        return salvar(paciente);
    }

    public Paciente buscarPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado!"));
    }

    public void deletar(UUID id) {
        repository.delete(buscarPorId(id));
    }
}
