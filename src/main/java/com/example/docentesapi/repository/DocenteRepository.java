package com.example.docentesapi.repository;

import com.example.docentesapi.entity.Docente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    //    @Query("SELECT d FROM Docente d WHERE LOWER(d.ciuDocente) = LOWER(:ciudad)")
//    List<Docente> findByCiudad(@Param("ciudad") String ciudad);
    List<Docente> findByCiuDocente(String ciudad);

    @Query("SELECT d FROM Docente d WHERE d.tiempoServicio >= :anosMinimos ORDER BY d.tiempoServicio DESC")
    List<Docente> findByExperienciaMinima(@Param("anosMinimos") Integer anosMinimos);

    @Query("SELECT AVG(CAST((YEAR(CURRENT_DATE) - YEAR(d.fecNacimiento)) AS double)) FROM Docente d")
    Double calcularEdadPromedio();

    boolean existsByEmailDocente(String email);

    @Query("SELECT COUNT(d) > 0 FROM Docente d WHERE d.emailDocente = :email AND d.idDocente != :idDocente")
    boolean existsByEmailDocenteAndIdDocenteNot(@Param("email") String email, @Param("idDocente") Long idDocente);

    @Query("SELECT d FROM Docente d ORDER BY d.nomDocente ASC")
    Page<Docente> findAllOrderByNombre(Pageable pageable);


}