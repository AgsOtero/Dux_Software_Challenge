package agsotero.dux_softaware_prueba_tecnica.service;

import agsotero.dux_softaware_prueba_tecnica.model.Equipo;

import java.util.List;

public interface EquipoService {

    List<Equipo> getAllEquipos();

    Equipo getEquipoById(int id);

    List<Equipo> getEquiposByNombre(String nombre);

    Equipo createEquipo(Equipo equipo);

    void updateEquipo(Equipo equipo);

    void deleteEquipo(int id);

}
