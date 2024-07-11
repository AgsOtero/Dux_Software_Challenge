package agsotero.dux_softaware_prueba_tecnica.controler;

import agsotero.dux_softaware_prueba_tecnica.model.Equipo;
import agsotero.dux_softaware_prueba_tecnica.repository.EquipoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EquipoRepository equipoRepository;

    private Equipo equipo;

    @BeforeEach
    void setUp() {
        equipo = new Equipo();
        equipo.setNombre("River Plate");
        equipo.setLiga("Primera División de Argentina");
        equipo.setPais("Argentina");
        equipoRepository.save(equipo);
    }

    @Test
    void testGetAllEquipos() throws Exception {
        mockMvc.perform(get("/equipos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void testGetEquipoById() throws Exception {
        mockMvc.perform(get("/equipos/{id}", equipo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(equipo.getNombre())));
    }

    @Test
    void testGetEquipoByIdNotFound() throws Exception {
        mockMvc.perform(get("/equipos/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetEquiposByNombre() throws Exception {
        mockMvc.perform(get("/equipos/buscar")
                        .param("nombre", "River Plate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(equipo.getNombre())));
    }

    @Test
    void testCreateEquipo() throws Exception {
        mockMvc.perform(post("/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"Barcelona FC\", \"liga\": \"La Liga\", \"pais\": \"España\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Barcelona FC")));
    }

    @Test
    void testCreateEquipoInvalid() throws Exception {
        mockMvc.perform(post("/equipos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"\", \"liga\": \"La Liga\", \"pais\": \"España\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", is("El nombre no debe estar en blanco")));
    }

    @Test
    void testUpdateEquipo() throws Exception {
        equipo.setNombre("Updated Name");

        mockMvc.perform(put("/equipos/{id}", equipo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"Updated Name\", \"liga\": \"La Liga\", \"pais\": \"España\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Updated Name")));
    }

    @Test
    void testUpdateEquipoNotFound() throws Exception {
        mockMvc.perform(put("/equipos/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"Updated Name\", \"liga\": \"La Liga\", \"pais\": \"España\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEquipoInvalid() throws Exception {
        mockMvc.perform(put("/equipos/{id}", equipo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"nombre\": \"\", \"liga\": \"La Liga\", \"pais\": \"España\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre", is("El nombre no debe estar en blanco")));
    }

    @Test
    void testDeleteEquipo() throws Exception {
        mockMvc.perform(delete("/equipos/{id}", equipo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteEquipoNotFound() throws Exception {
        mockMvc.perform(delete("/equipos/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
