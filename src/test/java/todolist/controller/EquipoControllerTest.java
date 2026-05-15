package todolist.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import todolist.authentication.ManagerUserSession;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipoController.class)
public class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void listadoEquiposEstandoLogeadoDevuelveVistaYAtributos() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        EquipoData equipo = new EquipoData();
        equipo.setId(1L);
        equipo.setNombre("Equipo de Desarrollo");

        when(equipoService.findAllOrdenadosPorNombre()).thenReturn(Arrays.asList(equipo));

        // WHEN & THEN
        mockMvc.perform(get("/equipos"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipos"))
                .andExpect(model().attributeExists("equipos"));
    }

    @Test
    public void listadoEquiposSinLogearRedirigeALogin() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/equipos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void detallesEquipoEstandoLogeadoDevuelveVistaYAtributos() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        EquipoData equipo = new EquipoData();
        equipo.setId(1L);
        equipo.setNombre("Equipo de Desarrollo");

        when(equipoService.recuperarEquipo(1L)).thenReturn(equipo);
        when(equipoService.usuariosEquipo(1L)).thenReturn(Arrays.asList(new UsuarioData()));

        // WHEN & THEN
        mockMvc.perform(get("/equipos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("equipoDetalle"))
                .andExpect(model().attributeExists("equipo"))
                .andExpect(model().attributeExists("usuarios"));
    }

    @Test
    public void detallesEquipoSinLogearRedirigeALogin() throws Exception {
        // GIVEN
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        // WHEN & THEN
        mockMvc.perform(get("/equipos/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}