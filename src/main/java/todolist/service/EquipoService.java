package todolist.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todolist.dto.EquipoData;
import todolist.model.Equipo;
import todolist.repository.EquipoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EquipoService {
    Logger logger = LoggerFactory.getLogger(EquipoService.class);

    @Autowired
    EquipoRepository equipoRepository;

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
            return null;
        }
        else return modelMapper.map(equipo, EquipoData.class);
    }

}
