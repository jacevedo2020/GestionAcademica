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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "usuario")
@XmlRootElement
public class Usuario implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "clave")
    private String clave;
    @Column(name = "estado")
    private Character estado;
    @Column(name = "clave_expirada")
    private Character claveExpirada;
    @JoinColumn(name = "id_persona", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Persona persona;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "usuario")
    private List<UsuarioRol> usuarioRolList;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "administrador")
    private List<AdministradorCentroCapacitacion> centroCapacitacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<Inscripcion> inscripcionList;
    @OneToMany(mappedBy = "instructor")
    private List<CursoCentroCapacitacion> cursosDictados;

    @Transient
    private boolean seleccionado;

    public Usuario() {
        persona = new Persona();
        estado = 'A';
        claveExpirada = 'N';
    }
    public Usuario(Persona persona) {
        this();
        this.persona= persona;
    }
    public Usuario(Integer id) {
        this.id = id;
    }

    public Usuario(Integer id, String nombre, String clave) {
        this.id = id;
        this.clave = clave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Character getClaveExpirada() {
        return claveExpirada;
    }

    public void setClaveExpirada(Character claveExpirada) {
        this.claveExpirada = claveExpirada;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    @XmlTransient
    @JsonIgnore
    public List<UsuarioRol> getUsuarioRolList() {
        return usuarioRolList;
    }

    public void setUsuarioRolList(List<UsuarioRol> usuarioRolList) {
        this.usuarioRolList = usuarioRolList;
    }

    public List<Rol> getRolesList() {
        return usuarioRolList.stream().map(ur -> ur.getRol()).collect(Collectors.toList());
    }

    public void eliminarUsuarioRol(UsuarioRol usuarioRol) {
        usuarioRolList.remove(usuarioRol);
    }

    public void agregarRol(Rol r) {
        if (usuarioRolList == null) {
            usuarioRolList = new ArrayList<>();
        }
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setRol(r);
        usuarioRol.setUsuario(this);
        usuarioRolList.add(usuarioRol);
    }

    public void agregarRoles(List<Rol> roles) {

        roles.forEach(r -> agregarRol(r));
    }

    public boolean verificarRolAsignado(Integer idRol) {
        if (usuarioRolList == null) {
            return false;
        }
        //return usuarioRolList.stream().filter(o -> o.getRol().equals(rol)).count() > 0;
        return usuarioRolList.stream().map(ur -> ur.getRol()).anyMatch(r -> r.getId().equals(idRol));

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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Usuario[ id=" + id + " ]";
    }

    public boolean verificarRolInstructor() {
        return verificarRolAsignado(Rol.ROL_INSTRUCTOR);
    }

    public boolean verificarRolEstudiante() {
        return verificarRolAsignado(Rol.ROL_ESTUDIANTE);
    }

    public boolean verificarRolAdministrador() {
        return verificarRolAsignado(Rol.ROL_ADMINISTRADOR);
    }

    public boolean verificarRolAdministradorCentro() {
        return verificarRolAsignado(Rol.ROL_ADMINISTRADOR_CENTRO);
    }

    public boolean verificarRolAnalista() {
        return verificarRolAsignado(Rol.ROL_ANALISTA);    
    }

    public boolean verificarRolAsistenteFacturacion() {
        return verificarRolAsignado(Rol.ROL_ASISTENTE_FACTURACION);    
    }
    public boolean verificarRolFinanciero() {
        return verificarRolAsignado(Rol.ROL_FINANCIERO);    
    }
    public List<AdministradorCentroCapacitacion> getCentroCapacitacionList() {
        if (centroCapacitacionList != null) {
            return centroCapacitacionList.stream().sorted().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void setCentroCapacitacionList(List<AdministradorCentroCapacitacion> centroCapacitacionList) {
        this.centroCapacitacionList = centroCapacitacionList;
    }

    public AdministradorCentroCapacitacion getCentroCapacitacionAdministracionActual() {
        if (getCentroCapacitacionList().isEmpty()) {
            return null;
        } else {
            return getCentroCapacitacionList().get(0);
        }
    }

    @XmlTransient
    @JsonIgnore
    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    @XmlTransient
    @JsonIgnore
    public List<CursoCentroCapacitacion> getCursosDictados() {
        return cursosDictados;
    }

    /*public List<CursoCentroCapacitacion> getCursosDictadosPendientesEvaluar() {
        if (cursosDictados == null) {
            return new ArrayList<>();
        }
        return cursosDictados.stream().filter(c -> c.pendienteEvaluacion()).collect(Collectors.toList());
    }*/

    public void setCursosDictados(List<CursoCentroCapacitacion> cursosDictados) {
        this.cursosDictados = cursosDictados;
    }

    public boolean verificarInscripcion(CursoCentroCapacitacion curso) {
        if (inscripcionList == null || inscripcionList.isEmpty()) {
            return false;
        }
        return inscripcionList.stream().anyMatch(i -> !i.isEstadoMatriculaAnulada() && i.getCursoCentroCapacitacion().equals(curso));
    }

    public Inscripcion getUltimaInscripcion() {
        if (inscripcionList == null || inscripcionList.isEmpty()) {
            return null;
        }
        return inscripcionList.stream().max((i1, i2) -> i1.getId().compareTo(i2.getId())).get();
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        if (inscripcionList == null) {
            inscripcionList = new ArrayList<>();
        }
        inscripcionList.add(inscripcion);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Usuario usuarioClonado = (Usuario) super.clone();
        Persona personaClonada = (Persona) persona.clone();
        usuarioClonado.setPersona(personaClonada);
        return usuarioClonado; 
    }

    
    
}
