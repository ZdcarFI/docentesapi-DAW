package com.example.docentesapi.controller;

import com.example.docentesapi.entity.Docente;
import com.example.docentesapi.service.DocenteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/docentes")
@Tag(name = "Docentes", description = "API para la gestión de docentes universitarios")
public class DocenteController {

    private final DocenteService docenteService;


    @Autowired
    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    @Operation(summary = "Listar docentes",
            description = "Obtiene una lista paginada de todos los docentes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    public ResponseEntity<Map<String, Object>> listarDocentes(
            @Parameter(description = "Número de página (inicia en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de elementos por página", example = "10")
            @RequestParam(defaultValue = "10") int size

           ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Docente> pageDocentes = docenteService.obtenerTodosLosDocentes(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("docentes", pageDocentes.getContent());
        response.put("currentPage", pageDocentes.getNumber());
        response.put("totalItems", pageDocentes.getTotalElements());
        response.put("totalPages", pageDocentes.getTotalPages());
        response.put("pageSize", pageDocentes.getSize());
        response.put("hasNext", pageDocentes.hasNext());
        response.put("hasPrevious", pageDocentes.hasPrevious());
        response.put("isFirst", pageDocentes.isFirst());
        response.put("isLast", pageDocentes.isLast());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener docente por ID",
            description = "Busca y retorna un docente específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente encontrado"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    public ResponseEntity<Docente> obtenerDocentePorId(
            @Parameter(description = "ID único del docente", example = "1")
            @PathVariable Long id) {

        Docente docente = docenteService.obtenerDocentePorId(id);
        return ResponseEntity.ok(docente);
    }


    @PostMapping
    @Operation(summary = "Crear nuevo docente",
            description = "Registra un nuevo docente en el sistema con validaciones completas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Docente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado")
    })
    public ResponseEntity<Docente> crearDocente(
            @Parameter(description = "Datos del nuevo docente")
            @Valid @RequestBody Docente docente) {

        Docente nuevoDocente = docenteService.crearDocente(docente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar docente",
            description = "Actualiza todos los datos de un docente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado"),
            @ApiResponse(responseCode = "409", description = "Email ya registrado por otro docente")
    })
    public ResponseEntity<Docente> actualizarDocente(
            @Parameter(description = "ID del docente a actualizar", example = "1")
            @PathVariable Long id,

            @Parameter(description = "Nuevos datos del docente")
            @Valid @RequestBody Docente docente) {

        Docente docenteActualizado = docenteService.actualizarDocente(id, docente);
        return ResponseEntity.ok(docenteActualizado);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar docente",
            description = "Elimina permanentemente un docente del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Docente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    public ResponseEntity<Void> eliminarDocente(
            @Parameter(description = "ID del docente a eliminar", example = "1")
            @PathVariable Long id) {

        docenteService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Listar docentes por ciudad",
            description = "Obtiene todos los docentes que residen en una ciudad específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron docentes en la ciudad especificada")
    })
    public ResponseEntity<Map<String, Object>> obtenerDocentesPorCiudad(
            @Parameter(description = "Nombre de la ciudad", example = "Cusco")
            @PathVariable String ciudad) {

        List<Docente> docentes = docenteService.obtenerDocentesPorCiudad(ciudad);

        Map<String, Object> response = new HashMap<>();
        response.put("ciudad", ciudad);
        response.put("totalDocentes", docentes.size());
        response.put("docentes", docentes);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/experiencia/{anos}")
    @Operation(summary = "Listar docentes por experiencia mínima",
            description = "Obtiene docentes con al menos la cantidad especificada de años de servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Años de experiencia inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontraron docentes con la experiencia especificada")
    })
    public ResponseEntity<Map<String, Object>> obtenerDocentesPorExperiencia(
            @Parameter(description = "Años mínimos de experiencia", example = "5")
            @PathVariable Integer anos) {

        List<Docente> docentes = docenteService.obtenerDocentesPorExperiencia(anos);

        Map<String, Object> response = new HashMap<>();
        response.put("experienciaMinima", anos);
        response.put("totalDocentes", docentes.size());
        response.put("docentes", docentes);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/edad-promedio")
    @Operation(summary = "Calcular edad promedio",
            description = "Calcula y retorna la edad promedio de todos los docentes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cálculo realizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No hay docentes registrados")
    })
    public ResponseEntity<Map<String, Object>> calcularEdadPromedio() {

        Double edadPromedio = docenteService.calcularEdadPromedio();
        Long totalDocentes = (long) docenteService.obtenerTodosLosDocentes().size();

        Map<String, Object> response = new HashMap<>();
        response.put("edadPromedio", edadPromedio);
        response.put("totalDocentes", totalDocentes);
        response.put("mensaje", "Edad promedio calculada basada en " + totalDocentes + " docentes");

        return ResponseEntity.ok(response);
    }


}