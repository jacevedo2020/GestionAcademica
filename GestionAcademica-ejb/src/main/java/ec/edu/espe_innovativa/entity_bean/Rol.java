package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;


@Entity
@Table(name = "rol")
@XmlRootElement
public class Rol implements Serializable {

    public final static int ROL_ADMINISTRADOR = 1;
    public final static int ROL_ADMINISTRADOR_CENTRO = 2;
    public final static int ROL_INSTRUCTOR = 3;
    public final static int ROL_ESTUDIANTE = 4;
    public final static int ROL_ANALISTA = 5;
    public final static int ROL_ASISTENTE_FACTURACION = 6;
    public final static int ROL_FINANCIERO = 7;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "nombre")
    private String nombre;
    @Size(min = 1, max = 75)
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "rol")
    private List<UsuarioRol> usuarioRolList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "rol")
    private List<RolOpcion> rolOpcionList;

    @Transient
    private boolean seleccionado;

    public Rol() {
    }

    public Rol(Integer id) {
        this.id = id;
    }

    public Rol(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    @JsonIgnore
    public List<UsuarioRol> getUsuarioRolList() {
        return usuarioRolList;
    }

    public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
        this.usuarioRolList = usuarioRolList;
    }

    @XmlTransient
    @JsonIgnore
    public List<RolOpcion> getRolOpcionList() {
        return rolOpcionList;
    }

    public void setRolOpcionList(List<RolOpcion> rolOpcionList) {
        this.rolOpcionList = rolOpcionList;
    }

    public List<Usuario> getUsuarioList() {
        if (usuarioRolList == null) {
            return new ArrayList<>();
        }
        return usuarioRolList.stream().map(ur -> ur.getUsuario()).collect(Collectors.toList());
    }

    public List<Opcion> getOpcionList() {
        if (rolOpcionList == null) {
            return new ArrayList<>();
        }
        return rolOpcionList.stream().map(ro -> ro.getOpcion()).collect(Collectors.toList());
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Rol[ id=" + id + " ]";
    }

    private void agregarOpcion(Opcion op) {
        RolOpcion rolOpcion = new RolOpcion();
        rolOpcion.setOpcion(op);
        rolOpcion.setRol(this);
        rolOpcionList.add(rolOpcion);
    }

    public void agregarOpciones(List<Opcion> opciones) {
        if (rolOpcionList == null) {
            rolOpcionList = new ArrayList<>();
        }
        opciones.forEach(o -> agregarOpcion(o));
    }

    public void eliminarRolOpcion(RolOpcion rolOpcion) {
        rolOpcionList.remove(rolOpcion);
    }

    public boolean verificarOpcionAsignada(Opcion op) {
        if (rolOpcionList == null) {
            return false;
        }
        return rolOpcionList.stream().filter(ro -> ro.getOpcion().equals(op)).count() > 0;
    }

    private void agregarUsuario(Usuario u) {
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setRol(this);
        usuarioRol.setUsuario(u);
        usuarioRolList.add(usuarioRol);
    }

    public void agregarUsuarios(List<Usuario> usuarios) {
        if (usuarioRolList == null) {
            usuarioRolList = new ArrayList<>();
        }
        usuarios.forEach(u -> agregarUsuario(u));
    }

    public void eliminarUsuarioRol(UsuarioRol usuarioRol) {
        usuarioRolList.remove(usuarioRol);
    }

    public boolean verificarUsuarioAsignado(Usuario u) {
        if (usuarioRolList == null) {
            return false;
        }
        return usuarioRolList.stream().filter(o -> o.getUsuario().equals(u)).count() > 0;
    }

}
