package com.bifani.pacientes.controller;

import com.bifani.pacientes.model.Usuario;
import com.bifani.pacientes.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repository,
                             PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping
    public Usuario criar(@Valid @RequestBody Usuario usuario){

        usuario.setPassword(encoder.encode(usuario.getPassword()));

        return repository.save(usuario);
    }
}