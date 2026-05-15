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
        Equipo equipo;

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

    @Test
    public void comprobarIgualdadEquipos(){
        //GIVEN
        //Creamos 3 equipos sin ID solo con el nombre
        Equipo equipo1 = new Equipo("Project 1");
        Equipo equipo2 = new Equipo("Project 2");
        Equipo equipo3 = new Equipo("Project 2");

        //THEN
        // Comprobamos igualdad basada en el atributo nombre y que el
        // hashCode es el mismo para dos equipos con igual nombre
        assertThat(equipo1).isNotEqualTo(equipo2);
        assertThat(equipo2).isEqualTo(equipo3);
        assertThat(equipo2.hashCode()).isEqualTo(equipo3.hashCode());

        //WHEN
        //Añadimos identificadores y comprobamos igualdad por identificadores
        equipo1.setId(1L);
        equipo2.setId(1L);
        equipo3.setId(3L);

        //WHEN
        //Comprobamos igualdad basada en el atributo nombre
        assertThat(equipo1).isEqualTo(equipo2);
        assertThat(equipo2).isNotEqualTo(equipo3);
    }
}
