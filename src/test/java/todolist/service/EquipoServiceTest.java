package todolist.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import todolist.dto.EquipoData;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoServiceTest {
    @Autowired
    EquipoService equipoService;

    @Test
    public void crearRecuperarEquipo(){
        EquipoData equipo = equipoService.crearEquipo("Project 1");
        assertThat(equipo.getId()).isNotNull();

        EquipoData equipoDataDb = equipoService.recuperarEquipo(equipo.getId());
        assertThat(equipoDataDb).isNotNull();
        assertThat(equipoDataDb.getNombre()).isEqualTo("Project 1");
    }
}
