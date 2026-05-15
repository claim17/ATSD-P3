package todolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import todolist.service.EquipoService;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private EquipoService equipoService;

    @Override
    public void run(String... args) throws Exception {
        if (equipoService.findAllOrdenadosPorNombre().isEmpty()) {
            equipoService.crearEquipo("Equipo Alfa");
            equipoService.crearEquipo("Equipo Beta");
            equipoService.crearEquipo("Equipo Gamma");
        }
    }
}