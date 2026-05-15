package todolist.dto;

import java.io.Serializable;
import java.util.Objects;

public class EquipoData {

    private Long id;
    private String nombre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){return true;}
        if (!(o instanceof EquipoData)) return false;
        EquipoData that = (EquipoData) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {return Objects.hash(getId());}
}
