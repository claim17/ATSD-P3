package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import todolist.authentication.ManagerUserSession;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.service.EquipoService;

import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/equipos")
    public String listadoEquipos(Model model) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        List<EquipoData> equipos = equipoService.findAllOrdenadosPorNombre();
        model.addAttribute("equipos", equipos);
        
        return "equipos";
    }

    @GetMapping("/equipos/{id}")
    public String detallesEquipo(@PathVariable(value = "id") Long idEquipo, Model model) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        EquipoData equipo = equipoService.recuperarEquipo(idEquipo);
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(idEquipo);
        
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        
        return "equipoDetalle";
    }
}