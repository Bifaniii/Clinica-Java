package com.bifani.pacientes.service;

import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {
    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public List<Paciente> listarTodos() {
        return repository.findAll();
    }

    public Paciente salvar(Paciente paciente) {
        if(paciente.getAge() < 0){
            throw new RuntimeException("Idade inválida");
        } else if (paciente.getCity().isBlank()){
            throw new RuntimeException("Cidade não informada!");
        }
        return repository.save(paciente);
    }

    public Paciente buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

}
