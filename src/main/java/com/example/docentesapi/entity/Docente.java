package com.example.docentesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.Period;


@Entity
@Table(name = "docentes")
@Schema(description = "Entidad que representa un docente universitario")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_docente")
    @Schema(description = "ID único del docente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long idDocente;

    @NotBlank(message = "El nombre del docente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(name = "nom_docente", nullable = false, length = 100)
    @Schema(description = "Nombre completo del docente", example = "Dr. Juan Carlos Pérez López")
    private String nomDocente;


    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(name = "dir_docente", nullable = false, length = 200)
    @Schema(description = "Dirección de residencia del docente", example = "Av. Los Incas 123, San Blas")
    private String dirDocente;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50, message = "La ciudad no puede exceder 50 caracteres")
    @Column(name = "ciu_docente", nullable = false, length = 50)
    @Schema(description = "Ciudad de residencia del docente", example = "Cusco")
    private String ciuDocente;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Column(name = "email_docente", nullable = false, unique = true, length = 100)
    @Schema(description = "Correo electrónico único del docente", example = "juan.perez@universidad.edu.pe")
    private String emailDocente;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @Column(name = "fec_nacimiento", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Fecha de nacimiento del docente", example = "1975-03-15")
    private LocalDate fecNacimiento;

    @NotNull(message = "El tiempo de servicio es obligatorio")
    @Min(value = 0, message = "El tiempo de servicio no puede ser negativo")
    @Max(value = 50, message = "El tiempo de servicio no puede exceder 50 años")
    @Column(name = "tiempo_servicio", nullable = false)
    @Schema(description = "Años de servicio del docente", example = "15")
    private Integer tiempoServicio;

    // Constructores

    public Docente() {}


    public Docente(String nomDocente, String dirDocente, String ciuDocente,
                   String emailDocente, LocalDate fecNacimiento, Integer tiempoServicio) {
        this.nomDocente = nomDocente;
        this.dirDocente = dirDocente;
        this.ciuDocente = ciuDocente;
        this.emailDocente = emailDocente;
        this.fecNacimiento = fecNacimiento;
        this.tiempoServicio = tiempoServicio;
    }




    public Long getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Long idDocente) {
        this.idDocente = idDocente;
    }

    public String getNomDocente() {
        return nomDocente;
    }

    public void setNomDocente(String nomDocente) {
        this.nomDocente = nomDocente;
    }

    public String getDirDocente() {
        return dirDocente;
    }

    public void setDirDocente(String dirDocente) {
        this.dirDocente = dirDocente;
    }

    public String getCiuDocente() {
        return ciuDocente;
    }

    public void setCiuDocente(String ciuDocente) {
        this.ciuDocente = ciuDocente;
    }

    public String getEmailDocente() {
        return emailDocente;
    }

    public void setEmailDocente(String emailDocente) {
        this.emailDocente = emailDocente;
    }

    public LocalDate getFecNacimiento() {
        return fecNacimiento;
    }

    public void setFecNacimiento(LocalDate fecNacimiento) {
        this.fecNacimiento = fecNacimiento;
    }

    public Integer getTiempoServicio() {
        return tiempoServicio;
    }

    public void setTiempoServicio(Integer tiempoServicio) {
        this.tiempoServicio = tiempoServicio;
    }

}