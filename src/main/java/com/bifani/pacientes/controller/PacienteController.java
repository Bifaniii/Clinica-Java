package com.bifani.pacientes.controller;

import com.bifani.pacientes.model.Paciente;
import com.bifani.pacientes.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Paciente> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Paciente buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Paciente atualizar(@Valid @PathVariable Long id, @RequestBody Paciente paciente) {
        paciente.setId(id);
        return service.salvar(paciente);
    }

    @PostMapping
    public Paciente criar(@Valid @RequestBody Paciente paciente) {
        return service.salvar(paciente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
