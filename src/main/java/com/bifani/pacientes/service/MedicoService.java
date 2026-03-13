package com.bifani.pacientes.service;

import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoService {
    public final MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public List<Medico> listarTodosMedicos() {
        return repository.findAll();
    }

    public Medico buscarMedicoPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Médico não encontrado!"));
    }

    public Medico salvar(Medico medico) {
        return repository.save(medico);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
