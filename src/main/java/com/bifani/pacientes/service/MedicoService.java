package com.bifani.pacientes.service;

import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Role;
import com.bifani.pacientes.repository.MedicoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicoService {
    private final MedicoRepository repository;
    private final PasswordEncoder passwordEncoder;

    public MedicoService(MedicoRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Medico> listarTodosMedicos() {
        return repository.findAll();
    }

    public Medico buscarMedicoPorId(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado!"));
    }

    public Medico salvar(Medico medico) {
        medico.setRole(Role.MEDICO);
        medico.setPassword(passwordEncoder.encode(medico.getPassword()));
        return repository.save(medico);
    }

    public Medico atualizar(UUID id, Medico medico) {
        buscarMedicoPorId(id);
        medico.setId(id);
        return salvar(medico);
    }

    public void deletar(UUID id) {
        repository.delete(buscarMedicoPorId(id));
    }
}
