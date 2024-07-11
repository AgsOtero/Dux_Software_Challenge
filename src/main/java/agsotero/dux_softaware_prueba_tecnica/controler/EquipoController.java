package agsotero.dux_softaware_prueba_tecnica.controler;

import agsotero.dux_softaware_prueba_tecnica.exception.EquipoNotFoundException;
import agsotero.dux_softaware_prueba_tecnica.model.Equipo;
import agsotero.dux_softaware_prueba_tecnica.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipos")
@Validated
public class EquipoController {

    private final EquipoService equipoService;

    @Autowired
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @Operation(summary = "Obtiene todos los equipos")
    @GetMapping
    public List<Equipo> getAllEquipos() {
        return equipoService.getAllEquipos();
    }

    @Operation(summary = "Obtiene un equipo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class))}),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipo> getEquipoById(
            @Parameter(description = "ID del equipo") @PathVariable @Min(1) int id) {
        try {
            Equipo equipo = equipoService.getEquipoById(id);
            return new ResponseEntity<>(equipo, HttpStatus.OK);
        } catch (EquipoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Busca equipos por nombre")
    @GetMapping("/buscar")
    public List<Equipo> getEquiposByNombre(
            @Parameter(description = "Nombre del equipo") @RequestParam @NotBlank @Size(min = 3) String nombre) {
        return equipoService.getEquiposByNombre(nombre);
    }

    @Operation(summary = "Crea un nuevo equipo")
    @ApiResponse(responseCode = "201", description = "Equipo creado")
    @PostMapping
    public ResponseEntity<Equipo> createEquipo(@RequestBody @Valid Equipo equipo) {
        Equipo createdEquipo = equipoService.createEquipo(equipo);
        return new ResponseEntity<>(createdEquipo, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualiza un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class))}),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Equipo> updateEquipo(
            @Parameter(description = "ID del equipo") @PathVariable @Min(1) int id,
            @RequestBody @Valid Equipo equipo) {
        try {
            equipo.setId(id);
            equipoService.updateEquipo(equipo);
            return new ResponseEntity<>(equipo, HttpStatus.OK);
        } catch (EquipoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Elimina un equipo")
    @ApiResponse(responseCode = "204", description = "Equipo eliminado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipo(
            @Parameter(description = "ID del equipo") @PathVariable @Min(1) int id) {
        try {
            equipoService.deleteEquipo(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EquipoNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}