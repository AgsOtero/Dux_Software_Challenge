package agsotero.dux_softaware_prueba_tecnica.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Equipo {

    @Id
    @GeneratedValue
    private int id;

    private String nombre;
    private String liga;
    private String pais;

}
