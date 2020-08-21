package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
@Table(name = "centro_capacitacion")
@XmlRootElement
public class CentroCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "id_canton", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Canton canton;
    @Size(max = 300)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 9)
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "centroCapacitacion")
    private List<AdministradorCentroCapacitacion> administradorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "centroCapacitacion")
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionList;

    @Transient
    private boolean seleccionado;

    
    public CentroCapacitacion() {
    }

    public CentroCapacitacion(Integer id) {
        this.id = id;
    }

    public CentroCapacitacion(Integer id, String nombre) {
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
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
        if (!(object instanceof CentroCapacitacion)) {
            return false;
        }
        CentroCapacitacion other = (CentroCapacitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.CentroCapacitacion[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<AdministradorCentroCapacitacion> getAdministradorList() {
        if (administradorList != null) {
            return administradorList.stream().sorted().collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void setAdministradorList(List<AdministradorCentroCapacitacion> administradorList) {
        this.administradorList = administradorList;
    }

    public AdministradorCentroCapacitacion getAdministradorActual() {
        if (getAdministradorList().isEmpty()) {
            return null;
        } else {
            return getAdministradorList().get(0);
        }
    }

    public boolean verificarAdministradorAsignado(Usuario usuario) {
        if (getAdministradorActual()==null)
            return false;
        else
            return getAdministradorActual().getAdministrador().equals(usuario);
    }

    public void agregarAdministrador(AdministradorCentroCapacitacion administradorCentroCapacitacion){
        administradorCentroCapacitacion.setCentroCapacitacion(this);
        if (administradorList ==null)
            administradorList = new ArrayList<>();
        administradorList.add(administradorCentroCapacitacion);
    }

    @XmlTransient
    @JsonIgnore
    public List<CursoCentroCapacitacion> getCursoCentroCapacitacionList() {
        return cursoCentroCapacitacionList;
    }
    /*public List<CursoCentroCapacitacion> getCursoCentroCapacitacionNotasIngresadasList() {
        if (cursoCentroCapacitacionList==null)
            return new ArrayList<>();
        return cursoCentroCapacitacionList.stream().filter(c -> Objects.equals(c.getEstado(), CursoCentroCapacitacion.ESTADO_REGISTRADO_NOTAS)).collect(Collectors.toList());
    }*/


    public void setCursoCentroCapacitacionList(List<CursoCentroCapacitacion> cursoCentroCapacitacionList) {
        this.cursoCentroCapacitacionList = cursoCentroCapacitacionList;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    
}
