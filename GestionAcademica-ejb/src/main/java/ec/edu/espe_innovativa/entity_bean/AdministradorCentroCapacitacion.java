package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "administrador_centro_capacitacion")
@XmlRootElement
public class AdministradorCentroCapacitacion implements Serializable, Comparable<AdministradorCentroCapacitacion> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_asignacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacion;
    @JoinColumn(name = "id_centro_capacitacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CentroCapacitacion centroCapacitacion;
    @JoinColumn(name = "id_usuario_asigno", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario creadoPor;
    @JoinColumn(name = "id_usuario_administrador", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario administrador;

    public AdministradorCentroCapacitacion() {
        fechaAsignacion = new Date();
    }

    public AdministradorCentroCapacitacion(Integer id) {
        this.id = id;
    }

    public AdministradorCentroCapacitacion(Integer id, Date fechaAsignacion) {
        this.id = id;
        this.fechaAsignacion = fechaAsignacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }
    public Date getFechaFin() {
        if (isAdministradorActual())
            return null;
        else{
            return centroCapacitacion.getAdministradorList().get(centroCapacitacion.getAdministradorList().indexOf(this) -1).getFechaAsignacion();
        }
    }
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public CentroCapacitacion getCentroCapacitacion() {
        return centroCapacitacion;
    }

    public void setCentroCapacitacion(CentroCapacitacion centroCapacitacion) {
        this.centroCapacitacion = centroCapacitacion;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
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
        if (!(object instanceof AdministradorCentroCapacitacion)) {
            return false;
        }
        AdministradorCentroCapacitacion other = (AdministradorCentroCapacitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.AdministradorCentroCapacitacion[ id=" + id + " ]";
    }

    @Override
    public int compareTo(AdministradorCentroCapacitacion o) {
        return o.fechaAsignacion.compareTo(this.fechaAsignacion);
    }

    public boolean isAdministradorActual() {
        return this.equals(centroCapacitacion.getAdministradorActual());
    }
}
