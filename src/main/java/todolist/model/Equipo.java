package todolist.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name= "equipos")
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    // Declaramos el tipo de recuperacion como LAZY
    // No haría falta porque es el tipo por defecto en una relacion muchos a muchos.
    // Al recuperar un equipo NO SE RECUPERA AUTOMATICAMENTE la lista de usuarios.
    // Sólo recupera cuando se accesde al atributo 'usuarios'; entonces se genera una query en la
    // BD que devuelve todos los usuarios del equipo y rellena el atributo.

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "equipo_usuario",
               joinColumns = {@JoinColumn(name = "fk_quipo")},
               inverseJoinColumns = {@JoinColumn(name = "fk_usuario")})
    Set<Usuario> usuarios = new HashSet<>();

    //Constructor vacio necesario para hibernate
    //No usarse desde la applicación
    public Equipo(){}

    public  Equipo(String nombre)
    {
        this.nombre = nombre;
    }

    public Long getId() {return id;}

    public void setId(Long id){this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Usuario> getUsuarios(){return usuarios;}

    public void addUsuario(Usuario usuario){
        // Hay que actualizar ambas colecciones, porque JPA/Hibernate
        // no lo hace automáticamente.
        this.getUsuarios().add(usuario);
        usuario.getEquipos().add(this);
    }

    public void removeUsuario(Usuario usuario){
        // Hay que actualizar ambas colecciones, porque JPA/Hibernate
        // no lo hace automáticamente.
        this.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        if (id != null && equipo.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, equipo.id);
        // si no comparamos por campos obligatorios
        return nombre.equals(equipo.nombre);
    }

    @Override
    public int hashCode() {return Objects.hash(id, nombre);}
}
