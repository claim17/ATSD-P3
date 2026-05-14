package todolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name= "equipos")
public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String nombre;

    //Constructor vacio necesario para hibernate
    //No usarse desde la applicación
    public Equipo(){}

    public  Equipo(String nombre)
    {
        this.nombre = nombre;
    }

    public long getId() {return id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
