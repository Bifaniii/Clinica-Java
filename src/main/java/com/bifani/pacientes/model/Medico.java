package com.bifani.pacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "usuario_id")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Medico extends Usuario {

    @NotBlank
    @Column(name = "speciality", nullable = false)
    private String speciality;

    @NotBlank
    @Column(name = "crm", nullable = false, unique = true)
    private String crm;

    @OneToMany(mappedBy = "doctor")
    private List<Consulta> consultas;
}
