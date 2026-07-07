package com.bifani.pacientes.controller;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.dto.CriarConsultaResponse;
import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.repository.ConsultaRepository;
import com.bifani.pacientes.service.ConsultaService;
import com.bifani.pacientes.service.MedicoService;
import com.bifani.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;
    private final MedicoService medicoService;
    private final PacienteService pacienteService;

    public ConsultaController(ConsultaService consultaService, MedicoService medicoService, PacienteService pacienteService) {
        this.consultaService = consultaService;
        this.medicoService = medicoService;
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<Consulta> listarConsultas() {
        return consultaService.listarTodasConsultas();
    }

    @GetMapping("/{id}")
    public Consulta buscarConsultaPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<CriarConsultaResponse> criarConsulta(@Valid @RequestBody CriarConsultaRequest request) {
        consultaService.criarConsulta(request.pacienteId(), request.medicoId(), request.date());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriarConsultaResponse> atualizarConsulta(@PathVariable Long id, @Valid @RequestBody CriarConsultaRequest request) {
        consultaService.salvar(id, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
