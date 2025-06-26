package com.example.docentesapi.service;

import com.example.docentesapi.entity.Docente;
import com.example.docentesapi.exception.DocenteNotFoundException;
import com.example.docentesapi.exception.EmailAlreadyExistsException;
import com.example.docentesapi.exception.InvalidDateException;
import com.example.docentesapi.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
public class DocenteService {

    private final DocenteRepository docenteRepository;


    @Autowired
    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }


    @Transactional(readOnly = true)
    public Page<Docente> obtenerTodosLosDocentes(Pageable pageable) {
        return docenteRepository.findAllOrderByNombre(pageable);
    }


    @Transactional(readOnly = true)
    public List<Docente> obtenerTodosLosDocentes() {
        return docenteRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Docente obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new DocenteNotFoundException("Docente no encontrado con ID: " + id));
    }


    public Docente crearDocente(Docente docente) {
        if (docenteRepository.existsByEmailDocente(docente.getEmailDocente())) {
            throw new EmailAlreadyExistsException("Ya existe un docente con el email: " + docente.getEmailDocente());
        }
        validarFechaNacimiento(docente.getFecNacimiento());

        // Validar tiempo de servicio
        validarTiempoServicio(docente.getTiempoServicio(), docente.getFecNacimiento());
        return docenteRepository.save(docente);
    }


    public Docente actualizarDocente(Long id, Docente docenteActualizado) {
        Docente docenteExistente = obtenerDocentePorId(id);

        if (docenteRepository.existsByEmailDocenteAndIdDocenteNot(docenteActualizado.getEmailDocente(), id)) {
            throw new EmailAlreadyExistsException("Ya existe otro docente con el email: " + docenteActualizado.getEmailDocente());
        }
        // Validar fecha de nacimiento
        validarFechaNacimiento(docenteActualizado.getFecNacimiento());

        // Validar tiempo de servicio
        validarTiempoServicio(docenteActualizado.getTiempoServicio(), docenteActualizado.getFecNacimiento());
        docenteExistente.setNomDocente(docenteActualizado.getNomDocente());
        docenteExistente.setDirDocente(docenteActualizado.getDirDocente());
        docenteExistente.setCiuDocente(docenteActualizado.getCiuDocente());
        docenteExistente.setEmailDocente(docenteActualizado.getEmailDocente());
        docenteExistente.setFecNacimiento(docenteActualizado.getFecNacimiento());
        docenteExistente.setTiempoServicio(docenteActualizado.getTiempoServicio());

        return docenteRepository.save(docenteExistente);
    }


    public void eliminarDocente(Long id) {
        if (!docenteRepository.existsById(id)) {
            throw new DocenteNotFoundException("No se puede eliminar. Docente no encontrado con ID: " + id);
        }

        docenteRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<Docente> obtenerDocentesPorCiudad(String ciudad) {
        List<Docente> docentes = docenteRepository.findByCiuDocente(ciudad);

        if (docentes.isEmpty()) {
            throw new DocenteNotFoundException("No se encontraron docentes en la ciudad: " + ciudad);
        }

        return docentes;
    }


    @Transactional(readOnly = true)
    public List<Docente> obtenerDocentesPorExperiencia(Integer anosMinimos) {
        if (anosMinimos < 0) {
            throw new IllegalArgumentException("Los años de experiencia no pueden ser negativos");
        }

        List<Docente> docentes = docenteRepository.findByExperienciaMinima(anosMinimos);

        if (docentes.isEmpty()) {
            throw new DocenteNotFoundException("No se encontraron docentes con al menos " + anosMinimos + " años de experiencia");
        }

        return docentes;
    }


    @Transactional(readOnly = true)
    public Double calcularEdadPromedio() {
        long totalDocentes = docenteRepository.count();

        if (totalDocentes == 0) {
            throw new DocenteNotFoundException("No hay docentes registrados para calcular la edad promedio");
        }

        Double edadPromedio = docenteRepository.calcularEdadPromedio();

        return edadPromedio != null ? Math.round(edadPromedio * 100.0) / 100.0 : 0.0;
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {

        LocalDate fechaActual = LocalDate.now();
        int edad = Period.between(fechaNacimiento, fechaActual).getYears();
        if (edad > 100) {
            throw new InvalidDateException("fecNacimiento", fechaNacimiento,
                    "La fecha de nacimiento indica una edad no válida (mayor a 100 años)");
        }
    }

    private void validarTiempoServicio(Integer tiempoServicio, LocalDate fechaNacimiento) {

        if (fechaNacimiento != null) {
            int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
            int edadMinimaParaTrabajo =30;
            int maximoTiempoServicioPosible = edad - edadMinimaParaTrabajo;

            if (tiempoServicio > maximoTiempoServicioPosible) {
                throw new InvalidDateException("tiempoServicio", tiempoServicio,
                        String.format("El tiempo de servicio (%d años) no es coherente con la edad del docente (%d años)",
                                tiempoServicio, edad));
            }
        }
    }
}