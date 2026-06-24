package com.bifani.pacientes.service;

import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.repository.ConsultaRepository;
import com.bifani.pacientes.repository.MedicoRepository;
import com.bifani.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ConsultaService(ConsultaRepository consultaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Consulta> listarTodasConsultas() {
        return consultaRepository.findAll();
    }

    public Consulta criarConsulta(Long pacienteId, Long medicoId, LocalDateTime dateTime) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado!"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Médico não encontrado!"));

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setDoctor(medico);
        consulta.setDate(dateTime);

        return consultaRepository.save(consulta);
    }

    public Consulta salvar(Consulta consulta){
        return consultaRepository.save(consulta);
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));
    }

    public void delete(Long id) {
        consultaRepository.deleteById(id);
    }
}
