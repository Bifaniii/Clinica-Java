package com.bifani.pacientes.service;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.exception.RecursoNaoEncontradoException;
import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.repository.ConsultaRepository;
import com.bifani.pacientes.repository.MedicoRepository;
import com.bifani.pacientes.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada!"));
    }

    @Transactional
    public Consulta criarConsulta(CriarConsultaRequest request) {
        Consulta consulta = Consulta.builder()
                .paciente(buscarPaciente(request.pacienteId()))
                .doctor(buscarMedico(request.medicoId()))
                .date(request.date())
                .description(request.description())
                .build();

        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta atualizar(Long consultaId, CriarConsultaRequest request) {
        Consulta consulta = buscarPorId(consultaId);

        consulta.setPaciente(buscarPaciente(request.pacienteId()));
        consulta.setDoctor(buscarMedico(request.medicoId()));
        consulta.setDate(request.date());
        consulta.setDescription(request.description());

        return consultaRepository.save(consulta);
    }

    @Transactional
    public void delete(Long id) {
        consultaRepository.delete(buscarPorId(id));
    }

    private Paciente buscarPaciente(UUID id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado!"));
    }

    private Medico buscarMedico(UUID id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado!"));
    }
}
