package com.bifani.pacientes.controller;

import com.bifani.pacientes.dto.CriarConsultaRequest;
import com.bifani.pacientes.model.Consulta;
import com.bifani.pacientes.repository.ConsultaRepository;
import com.bifani.pacientes.service.ConsultaService;
import com.bifani.pacientes.service.MedicoService;
import com.bifani.pacientes.service.PacienteService;
import jakarta.validation.Valid;
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
    public List<Consulta> listarConsultas() {
        return consultaService.listarTodasConsultas();
    }

    @GetMapping("/{id}")
    public Consulta buscarConsultaPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }


    @PostMapping
    public Consulta criarConsulta(@Valid @RequestBody CriarConsultaRequest request) {
        Consulta consulta = new Consulta();
        consulta.setDate(request.getDate());
        consulta.setDescription(request.getDescription());

        return consultaService.criarConsulta(
                request.getPacienteId(),
                request.getMedicoId(),
                consulta);
    }

    @PutMapping("/{id}")
    public Consulta atualizarConsulta(@PathVariable Long id, @Valid @RequestBody Consulta consulta) {
        consulta.setId(id);
        return consultaService.salvar(consulta);
    }

    @DeleteMapping("/{id}")
    public void deletarConsulta(@PathVariable Long id) {
        consultaService.delete(id);
    }

}
