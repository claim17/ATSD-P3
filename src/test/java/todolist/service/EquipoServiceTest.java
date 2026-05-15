package todolist.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.model.Equipo;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoServiceTest {
    @Autowired
    EquipoService equipoService;
    @Autowired
    UsuarioService usuarioService;

    @Test
    public void crearRecuperarEquipoTest(){
        EquipoData equipo = equipoService.crearEquipo("Project 1");
        assertThat(equipo.getId()).isNotNull();

        EquipoData equipoDataDb = equipoService.recuperarEquipo(equipo.getId());
        assertThat(equipoDataDb).isNotNull();
        assertThat(equipoDataDb.getNombre()).isEqualTo("Project 1");
    }

    @Test
    public void listadoEquiposOrdenAlfabeticoTest(){
        // GIVEN
        // Dos equipos en la base de datos.
        equipoService.crearEquipo("Project BBB");
        equipoService.crearEquipo("Project AAA");

        // WHEN
        // Recuperamos los equipos.
        List<EquipoData> equipos = equipoService.findAllOrdenadosPorNombre();

        // THEN
        // Los equipos están ordenados por nombre
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Project AAA");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Project BBB");
    }

    @Test
    public void addUsuarioAEquipoTest(){
        // GIVEN
        // Un usuario y un equipo en la base de datos.
        UsuarioData usuario = new UsuarioData();

        usuario.setEmail("user@umh");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        EquipoData equipo = equipoService.crearEquipo("Project 1");

        // WHEN
        // Añadimos el usuario al equipo
        equipoService.addUsuarioAEquipo(equipo.getId(), usuario.getId());

        // THEN
        // El usuario pertenece al equipo
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(equipo.getId());
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).getEmail()).isEqualTo("user@umh");
    }

    @Test
    public void recuperarEquiposDeUsuarioTest(){
        // GIVEN
        // Un usuario y dos equipos en la base de datos.
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@umh");
        usuario.setPassword("1234");
        usuario = usuarioService.registrar(usuario);
        EquipoData equipo1 = equipoService.crearEquipo("Project 1");
        EquipoData equipo2 = equipoService.crearEquipo("Project 2");
        equipoService.addUsuarioAEquipo(equipo1.getId(), usuario.getId());
        equipoService.addUsuarioAEquipo(equipo2.getId(), usuario.getId());

        // WHEN
        // Recuperamos los equipos del usuario.
        List<EquipoData> equipos = equipoService.equiposUsurio(usuario.getId());

        // THEN
        // El usuario pertenece a los dos equipos.
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Project 1");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Project 2");
    }

    @Test
    public void comprobarExcepcionesTest(){
        // Comprobamos las excepciones lanzadas por metodos.
        // recuperarEquipo, addUsuarioAEquipo, usuariosEquipo, equiposUsuario.
        assertThatThrownBy(() -> equipoService.recuperarEquipo(1L)).isInstanceOf(EquipoServiceException.class);
        assertThatThrownBy(() -> equipoService.addUsuarioAEquipo(1L, 1L)).isInstanceOf(EquipoServiceException.class);
        assertThatThrownBy(() -> equipoService.usuariosEquipo(1L)).isInstanceOf(EquipoServiceException.class);
        assertThatThrownBy(() -> equipoService.equiposUsurio(1L)).isInstanceOf(EquipoServiceException.class);

        // Creamos un equipo pero no un usuario y comprobamos que tambien se lanza un excepcion.
        EquipoData equipo = equipoService.crearEquipo("Project 1");
        assertThatThrownBy(() -> equipoService.addUsuarioAEquipo(equipo.getId(), 1L)).isInstanceOf(EquipoServiceException.class);
    }
}
