package com.bifani.pacientes.controller;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.dto.CriarConsultaResponse;
import com.bifani.pacientes.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<CriarConsultaResponse> listarConsultas() {
        return consultaService.listarTodasConsultas().stream()
                .map(CriarConsultaResponse::new)
                .toList();
    }

    @GetMapping("/{id}")
    public CriarConsultaResponse buscarConsultaPorId(@PathVariable Long id) {
        return new CriarConsultaResponse(consultaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CriarConsultaResponse> criarConsulta(@Valid @RequestBody CriarConsultaRequest request) {
        var consulta = consultaService.criarConsulta(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CriarConsultaResponse(consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CriarConsultaResponse> atualizarConsulta(@PathVariable Long id, @Valid @RequestBody CriarConsultaRequest request) {
        var consulta = consultaService.atualizar(id, request);

        return ResponseEntity.ok(new CriarConsultaResponse(consulta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        consultaService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
