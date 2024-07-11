package agsotero.dux_softaware_prueba_tecnica.service;

import agsotero.dux_softaware_prueba_tecnica.exception.EquipoNotFoundException;
import agsotero.dux_softaware_prueba_tecnica.model.Equipo;
import agsotero.dux_softaware_prueba_tecnica.repository.EquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceImplTest {

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private EquipoServiceImpl equipoService;

    private Equipo equipo;

    @BeforeEach
    void setUp() {
        equipo = new Equipo();
        equipo.setId(1);
        equipo.setNombre("Real Madrid");
        equipo.setLiga("La Liga");
        equipo.setPais("España");
    }

    @Test
    void testGetAllEquipos() {
        List<Equipo> equipos = Arrays.asList(equipo);
        when(equipoRepository.findAll()).thenReturn(equipos);

        List<Equipo> result = equipoService.getAllEquipos();

        assertEquals(1, result.size());
        assertEquals("Real Madrid", result.get(0).getNombre());
        verify(equipoRepository, times(1)).findAll();
    }

    @Test
    void testGetEquipoById() {
        when(equipoRepository.findById(1)).thenReturn(Optional.of(equipo));

        Equipo result = equipoService.getEquipoById(1);

        assertNotNull(result);
        assertEquals("Real Madrid", result.getNombre());
        verify(equipoRepository, times(1)).findById(1);
    }

    @Test
    void testGetEquipoByIdNotFound() {
        when(equipoRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EquipoNotFoundException.class, () -> {
            equipoService.getEquipoById(1);
        });

        assertEquals("Equipo no encontrado con id: 1", exception.getMessage());
        verify(equipoRepository, times(1)).findById(1);
    }

    @Test
    void testGetEquiposByNombre() {
        List<Equipo> equipos = Arrays.asList(equipo);
        when(equipoRepository.findByNombre("Real Madrid")).thenReturn(equipos);

        List<Equipo> result = equipoService.getEquiposByNombre("Real Madrid");

        assertEquals(1, result.size());
        assertEquals("Real Madrid", result.get(0).getNombre());
        verify(equipoRepository, times(1)).findByNombre("Real Madrid");
    }

    @Test
    void testCreateEquipo() {
        when(equipoRepository.save(equipo)).thenReturn(equipo);

        Equipo result = equipoService.createEquipo(equipo);

        assertNotNull(result);
        assertEquals("Real Madrid", result.getNombre());
        verify(equipoRepository, times(1)).save(equipo);
    }

    @Test
    void testUpdateEquipo() {
        when(equipoRepository.existsById(equipo.getId())).thenReturn(true);
        when(equipoRepository.save(equipo)).thenReturn(equipo);

        equipoService.updateEquipo(equipo);

        verify(equipoRepository, times(1)).existsById(equipo.getId());
        verify(equipoRepository, times(1)).save(equipo);
    }

    @Test
    void testUpdateEquipoNotFound() {
        when(equipoRepository.existsById(equipo.getId())).thenReturn(false);

        Exception exception = assertThrows(EquipoNotFoundException.class, () -> {
            equipoService.updateEquipo(equipo);
        });

        assertEquals("Equipo no encontrado con id: " + equipo.getId(), exception.getMessage());
        verify(equipoRepository, times(1)).existsById(equipo.getId());
        verify(equipoRepository, times(0)).save(equipo);
    }

    @Test
    void testDeleteEquipo() {
        when(equipoRepository.existsById(equipo.getId())).thenReturn(true);
        doNothing().when(equipoRepository).deleteById(equipo.getId());

        equipoService.deleteEquipo(equipo.getId());

        verify(equipoRepository, times(1)).existsById(equipo.getId());
        verify(equipoRepository, times(1)).deleteById(equipo.getId());
    }

    @Test
    void testDeleteEquipoNotFound() {
        when(equipoRepository.existsById(equipo.getId())).thenReturn(false);

        Exception exception = assertThrows(EquipoNotFoundException.class, () -> {
            equipoService.deleteEquipo(equipo.getId());
        });

        assertEquals("Equipo no encontrado con id: " + equipo.getId(), exception.getMessage());
        verify(equipoRepository, times(1)).existsById(equipo.getId());
        verify(equipoRepository, times(0)).deleteById(equipo.getId());
    }
}
