package agsotero.dux_softaware_prueba_tecnica.web.repository;

import agsotero.dux_softaware_prueba_tecnica.web.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    List<Equipo> findByNombre(String nombre);
}
