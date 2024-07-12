package agsotero.dux_softaware_prueba_tecnica.web.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "equipo")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotBlank(message = "El nombre no debe estar en blanco")
    @Column(name = "nombre")
    private String nombre;

    @NotBlank(message = "La liga no debe estar en blanco")
    @Column(name = "liga")
    private String liga;

    @NotBlank(message = "El país no debe estar en blanco")
    @Column(name = "pais")
    private String pais;

    // Getters y setters
}
