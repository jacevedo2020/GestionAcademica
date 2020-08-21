package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "plantilla_certificado")
@XmlRootElement
public class PlantillaCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 250)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Character estado;
    @Size(max = 100)
    @Column(name = "documento_plantilla_nombre")
    private String documentoPlantillaNombre;
    @Size(max = 500)
    @Column(name = "documento_plantilla_url")
    private String documentoPlantillaUrl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    public PlantillaCertificado() {
    }

    public PlantillaCertificado(Integer id) {
        this.id = id;
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

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getDocumentoPlantillaNombre() {
        return documentoPlantillaNombre;
    }

    public void setDocumentoPlantillaNombre(String documentoPlantillaNombre) {
        this.documentoPlantillaNombre = documentoPlantillaNombre;
    }

    public String getDocumentoPlantillaUrl() {
        return documentoPlantillaUrl;
    }

    public void setDocumentoPlantillaUrl(String documentoPlantillaUrl) {
        this.documentoPlantillaUrl = documentoPlantillaUrl;
    }
    
    public String getDocumentoPlantillaUrlCompleto() {
        try {
            return this.documentoPlantillaUrl + "/" + this.documentoPlantillaNombre;
        } catch (Exception e) {
            return null;
        }
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
        if (!(object instanceof PlantillaCertificado)) {
            return false;
        }
        PlantillaCertificado other = (PlantillaCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.PlantillaCertificado[ id=" + id + " ]";
    }
    
}
