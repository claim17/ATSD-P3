package todolist.repository;

import org.springframework.beans.factory.annotation.Autowired;
import todolist.model.Equipo; // This model doesn’t exist yet 
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoTest {

    @Autowired
    private EquipoRepository equipoRepository;

    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Project P1");
        assertThat(equipo.getNombre()).isEqualTo("Project P1");
    }

    @Test
    @Transactional
    public void grabarYBuscarEquipo(){
        //Given
        //Un quipo nuevo
        Equipo equipo = new Equipo("Project P1");

        //Probamos el producto vacio, necesario para que funcione JPA/hibernte
        Equipo equipo1 = new Equipo();

        //Creamos ya el equipo
        equipo = new Equipo("Project P1");
        //WHEN
        // Salvamos el equipo en la base de datos.
        equipoRepository.save(equipo);

        //THEN
        // Su identificador se ha actualizado y lo podemos usar para recuperarlo de la base de datos.
        Long equipoId = equipo.getId();
        assertThat(equipoId).isNotNull();
        Equipo equipoDB = equipoRepository.findById(equipoId).orElse(null);
        assertThat(equipoDB).isNotNull();
        assertThat(equipoDB.getNombre()).isEqualTo("Project P1");
    }
}
