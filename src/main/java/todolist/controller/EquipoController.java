package todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(Model model) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        model.addAttribute("equipoData", new EquipoData());
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos/nuevo")
    public String crearEquipo(@ModelAttribute EquipoData equipoData, RedirectAttributes flash) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        EquipoData equipoCreado = equipoService.crearEquipo(equipoData.getNombre());
        equipoService.addUsuarioAEquipo(equipoCreado.getId(), usuarioLogeadoId);
        flash.addFlashAttribute("mensaje", "Equipo creado correctamente");
        return "redirect:/equipos";
    }

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
        boolean usuarioEnEquipo = equipoService.usuarioEnEquipo(idEquipo, usuarioLogeadoId);
        
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuarioEnEquipo", usuarioEnEquipo);
        model.addAttribute("usuarioLogeadoId", usuarioLogeadoId);
        
        return "equipoDetalle";
    }

    @GetMapping("/misequipos")
    public String misEquipos(Model model) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        List<EquipoData> equipos = equipoService.equiposUsurio(usuarioLogeadoId);
        model.addAttribute("equipos", equipos);
        
        return "misequipos";
    }

    @PostMapping("/equipos/{id}/unirse")
    public String unirsAEquipo(@PathVariable(value = "id") Long idEquipo, RedirectAttributes flash) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        equipoService.addUsuarioAEquipo(idEquipo, usuarioLogeadoId);
        flash.addFlashAttribute("mensaje", "Te has unido al equipo correctamente");
        return "redirect:/equipos/" + idEquipo;
    }

    @PostMapping("/equipos/{id}/salir")
    public String salirDelEquipo(@PathVariable(value = "id") Long idEquipo, RedirectAttributes flash) {
        Long usuarioLogeadoId = managerUserSession.usuarioLogeado();
        if (usuarioLogeadoId == null) {
            return "redirect:/login";
        }

        equipoService.removeUsuarioDeEquipo(idEquipo, usuarioLogeadoId);
        flash.addFlashAttribute("mensaje", "Has salido del equipo correctamente");
        return "redirect:/equipos";
    }
}