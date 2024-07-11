package agsotero.dux_softaware_prueba_tecnica.service;

import agsotero.dux_softaware_prueba_tecnica.exception.EquipoNotFoundException;
import agsotero.dux_softaware_prueba_tecnica.model.Equipo;
import agsotero.dux_softaware_prueba_tecnica.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;

    @Autowired
    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    @Override
    public List<Equipo> getAllEquipos() {
        return equipoRepository.findAll();
    }

    @Override
    public Equipo getEquipoById(int id) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
        if (optionalEquipo.isPresent()) {
            return optionalEquipo.get();
        } else {
            throw new EquipoNotFoundException("Equipo no encontrado con id: " + id);
        }
    }

    @Override
    public List<Equipo> getEquiposByNombre(String nombre) {
        return equipoRepository.findByNombre(nombre);
    }

    @Override
    public Equipo createEquipo(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

    @Override
    public void updateEquipo(Equipo equipo) {
        if (equipoRepository.existsById(equipo.getId())) {
            equipoRepository.save(equipo);
        } else {
            throw new EquipoNotFoundException("Equipo no encontrado con id: " + equipo.getId());
        }
    }

    @Override
    public void deleteEquipo(int id) {
        if (equipoRepository.existsById(id)) {
            equipoRepository.deleteById(id);
        } else {
            throw new EquipoNotFoundException("Equipo no encontrado con id: " + id);
        }
    }
}
