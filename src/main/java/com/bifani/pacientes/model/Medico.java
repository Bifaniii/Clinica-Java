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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "speciality", nullable = false)
    private String speciality;

    @NotBlank
    @Column(name = "crm", nullable = false, unique = true)
    private String crm;

    @OneToMany(mappedBy = "doctor")
    private List<Consulta> consultas;
}
