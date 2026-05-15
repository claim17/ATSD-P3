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
        if (id != null && that.id != null)
            return Objects.equals(id, that.id);
        //Si no comparamos campos obligatorios.
        return nombre.equals(that.nombre);
    }

    @Override
    public int hashCode() {
        // hash basado en campos obligatorios.
        return Objects.hash(nombre);
    }
}
