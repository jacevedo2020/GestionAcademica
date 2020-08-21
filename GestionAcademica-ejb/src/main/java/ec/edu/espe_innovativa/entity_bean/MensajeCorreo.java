package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "mensaje_correo")
@XmlRootElement
public class MensajeCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    
    public final static Integer ID_CREACION_USUARIO=1;
    public final static Integer ID_RECUPERACION_CLAVE=2;
    public final static Integer ID_MATRICULA_NO_APROBADA=3;
    public final static Integer ID_MATRICULA_APROBADA=4;
    public final static Integer ID_ENVIO_CERTIFICADO=5;
    public final static Integer ID_CERTIFICADO_POR_RETIRAR=6;
    public final static Integer ID_MATRICULA_PENDIENTE_APROBAR=7;
    
    
    @Size(max = 30)
    @Column(name = "tipo")
    private String tipo;
    @Size(max = 50)
    @Column(name = "asunto")
    private String asunto;
    @Size(max = 500)
    @Column(name = "mensaje")
    private String mensaje;

    
    
    public MensajeCorreo() {
    }

    public MensajeCorreo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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
        if (!(object instanceof MensajeCorreo)) {
            return false;
        }
        MensajeCorreo other = (MensajeCorreo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.MensajeCorreo[ id=" + id + " ]";
    }
    
}
