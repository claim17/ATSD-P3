package todolist.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todolist.dto.EquipoData;
import todolist.dto.UsuarioData;
import todolist.model.Equipo;
import todolist.model.Usuario;
import todolist.repository.EquipoRepository;
import org.springframework.transaction.annotation.Transactional;
import todolist.repository.UsuarioRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    EquipoRepository equipoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public EquipoData crearEquipo(String nombre){
        logger.debug("Creando equipo " + nombre + " en el repositorio");
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public EquipoData recuperarEquipo(Long id){
        logger.debug("Recuperando el equipo con id = " + id + " de la base de datos.");
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if(equipo == null) {
            logger.debug("El equipo con id = " + id + " no existe.");
            throw new EquipoServiceException("No se ha podido encontrar el equipo con id: "+ id + ". ID incorrecto o el equipo no existe.");
        }
        else return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public List<EquipoData> findAllOrdenadosPorNombre() {
        logger.debug("Recuperando todos los equipos de las base de datos");
        List<Equipo> equipos = (List<Equipo>) equipoRepository.findAll();
        List<EquipoData> equiposData = equipos.stream()
                .map(e -> modelMapper.map(e, EquipoData.class))
                .collect(Collectors.toList());
        equiposData.sort(Comparator.comparing(EquipoData::getNombre));

        return equiposData;
    }

    @Transactional
    public void addUsuarioAEquipo(Long teamId, Long userId){
        logger.debug("Agregando usuario " + userId + " al equipo " + teamId);
        Equipo equipo = equipoRepository.findById(teamId).orElse(null);
        if(equipo == null){
            logger.error("No existe equipo con id: " + teamId);
            throw new EquipoServiceException("Equipo con id = " + teamId + " no existe.");
        }
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if(usuario == null){
            logger.error("Usuario con id: " + userId + " inexistente al agregarlo al equipo " + teamId);
            throw new EquipoServiceException("Usuario con id = " + userId + " no existe.");
        }
        equipo.addUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> usuariosEquipo(Long id){
        logger.debug("Devolviendo todos los usuarios del equipo "+ id);
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if(equipo == null){
            logger.error("Error en equipo service - No encontrado el equipo con id: " + id);
            throw new EquipoServiceException("No existe el equipo con id: " + id);
        }
        //Hacemos uso de Java Stream API para mapear la lista de entidades a DTO's.
        return equipo.getUsuarios().stream()
                .map(u->modelMapper.map(u, UsuarioData.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EquipoData> equiposUsurio(Long userId){
        logger.debug("Recuperando los equipos del usuario con id: " + userId);
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario == null){
            logger.error("El usuario con id: " + userId + " no existe.");
            throw new EquipoServiceException("No se ha podido encontrar al usuario " + userId + " , no se buscar los equipos a los que pertenece este usuario.");
        }
        return usuario.getEquipos().stream()
                .map(e->modelMapper.map(e, EquipoData.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeUsuarioDeEquipo(Long teamId, Long userId){
        logger.debug("Removiendo usuario " + userId + " del equipo " + teamId);
        Equipo equipo = equipoRepository.findById(teamId).orElse(null);
        if(equipo == null){
            logger.error("No existe equipo con id: " + teamId);
            throw new EquipoServiceException("Equipo con id = " + teamId + " no existe.");
        }
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if(usuario == null){
            logger.error("Usuario con id: " + userId + " inexistente al removerse del equipo " + teamId);
            throw new EquipoServiceException("Usuario con id = " + userId + " no existe.");
        }
        equipo.removeUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public boolean usuarioEnEquipo(Long teamId, Long userId){
        logger.debug("Verificando si usuario " + userId + " pertenece al equipo " + teamId);
        Equipo equipo = equipoRepository.findById(teamId).orElse(null);
        if(equipo == null){
            logger.error("No existe equipo con id: " + teamId);
            throw new EquipoServiceException("Equipo con id = " + teamId + " no existe.");
        }
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if(usuario == null){
            logger.error("Usuario con id: " + userId + " no existe.");
            throw new EquipoServiceException("Usuario con id = " + userId + " no existe.");
        }
        return equipo.getUsuarios().contains(usuario);
    }

}
