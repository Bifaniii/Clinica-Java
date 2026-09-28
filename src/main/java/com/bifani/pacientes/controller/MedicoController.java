package com.bifani.pacientes.controller;

import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Medico> listarMedicos() {
        return service.listarTodosMedicos();
    }

    @GetMapping("/{id}")
    public Medico buscarMedicoPorId(@PathVariable UUID id) {
        return service.buscarMedicoPorId(id);
    }

    @PostMapping
    public ResponseEntity<Medico> criar(@Valid @RequestBody Medico medico) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(medico));
    }

    @PutMapping("/{id}")
    public Medico atualizar(@PathVariable UUID id, @Valid @RequestBody Medico medico) {
        return service.atualizar(id, medico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
