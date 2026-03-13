package com.bifani.pacientes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "pacientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Min(0)
    @Max(120)
    @Column(name = "age", nullable = false)
    private int age;

    @NotBlank
    @Column(name = "city", nullable = false)
    private String city;

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;
}
