package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "estado")
@XmlRootElement
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;

    public final static Integer ID_ESTADO_MATRICULA_REGISTRADA = 1;
    public final static Integer ID_ESTADO_MATRICULA_APROBADA = 2;
    public final static Integer ID_ESTADO_FACTURA_REGISTRADA = 3;
    public final static Integer ID_ESTADO_FACTURA_LIQUIDADA = 4;
    public final static Integer ID_ESTADO_MATRICULA_ANULADA = 5;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estado")
    private List<EstadoInscripcion> estadoInscripcionList;

    public Estado() {
    }

    public Estado(Integer id) {
        this.id = id;
    }

    public Estado(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    @JsonIgnore
    public List<EstadoInscripcion> getEstadoInscripcionList() {
        return estadoInscripcionList;
    }

    public void setEstadoInscripcionList(List<EstadoInscripcion> estadoInscripcionList) {
        this.estadoInscripcionList = estadoInscripcionList;
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
        if (!(object instanceof Estado)) {
            return false;
        }
        Estado other = (Estado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Estado[ id=" + id + " ]";
    }

    public boolean isEstadoMatriculaRegistrada() {
        return id == ID_ESTADO_MATRICULA_REGISTRADA;
    }

    public boolean isEstadoMatriculaAprobada() {
        return id == ID_ESTADO_MATRICULA_APROBADA;
    }

    public boolean isEstadoMatriculaAnulada() {
        return id == ID_ESTADO_MATRICULA_ANULADA;
    }

    public boolean isEstadoFacturaRegistrada() {
        return id == ID_ESTADO_FACTURA_REGISTRADA;
    }

    public boolean isEstadoFacturaLiquidada() {
        return id == ID_ESTADO_FACTURA_LIQUIDADA;
    }
}
