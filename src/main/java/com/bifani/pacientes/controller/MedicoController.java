package com.bifani.pacientes.controller;

import com.bifani.pacientes.model.Medico;
import com.bifani.pacientes.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Medico buscarMedicoPorId(@PathVariable Long id) {
        return service.buscarMedicoPorId(id);
    }

    @PostMapping
    public Medico criar(@Valid @RequestBody Medico medico) {
        return service.salvar(medico);
    }

    @PutMapping("/{id}")
    public Medico atualizar(@Valid @PathVariable Long id, @RequestBody Medico medico) {
        medico.setId(id);
        return service.salvar(medico);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
