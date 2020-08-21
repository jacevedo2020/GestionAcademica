package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "evaluacion")
@XmlRootElement
public class Evaluacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "tipo_valor")
    private Character tipoValor;
    public final static Character TIPO_VALOR_PUNTAJE = 'P';
    public final static Character TIPO_VALOR_ASISTENCIA = 'A';

    @Size(max = 100)
    @Column(name = "documento_justificacion_nombre")
    private String documentoJustificacionNombre;
    @Size(max = 500)
    @Column(name = "documento_justificacion_url")
    private String documentoJustificacionUrl;

    @JoinColumn(name = "id_inscripcion", referencedColumnName = "id")
    @ManyToOne
    private Inscripcion inscripcion;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario creadoPor;

    @JoinColumn(name = "solicitud_modificacion_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario solicitudModificacionPor;

    @Column(name = "solicitud_modificacion_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date solicitudModificacionFecha;

    @Size(max = 250)
    @Column(name = "modificacion_observacion")
    private String modificacionObservacion;
    
    
    public Evaluacion() {
        fecha = new Date();
    }

    public Evaluacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Character getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(Character tipoValor) {
        this.tipoValor = tipoValor;
    }

    public Usuario getSolicitudModificacionPor() {
        return solicitudModificacionPor;
    }

    public void setSolicitudModificacionPor(Usuario solicitudModificacionPor) {
        this.solicitudModificacionPor = solicitudModificacionPor;
    }

    public Date getSolicitudModificacionFecha() {
        return solicitudModificacionFecha;
    }

    public void setSolicitudModificacionFecha(Date solicitudModificacionFecha) {
        this.solicitudModificacionFecha = solicitudModificacionFecha;
    }

    public String getModificacionObservacion() {
        return modificacionObservacion;
    }

    public void setModificacionObservacion(String modificacionObservacion) {
        this.modificacionObservacion = modificacionObservacion;
    }

    
    public String getDocumentoJustificacionNombre() {
        return documentoJustificacionNombre;
    }

    public void setDocumentoJustificacionNombre(String documentoJustificacionNombre) {
        this.documentoJustificacionNombre = documentoJustificacionNombre;
    }

    public String getDocumentoJustificacionUrl() {
        return documentoJustificacionUrl;
    }

    public void setDocumentoJustificacionUrl(String documentoJustificacionUrl) {
        this.documentoJustificacionUrl = documentoJustificacionUrl;
    }

    public String getDocumentoJustificacionUrlCompleto() {
        try {
            return this.documentoJustificacionUrl + "/" + this.documentoJustificacionNombre;
        } catch (Exception e) {
            return null;
        }
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
        if (!(object instanceof Evaluacion)) {
            return false;
        }
        Evaluacion other = (Evaluacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Evaluacion[ id=" + id + " ]";
    }

    public void actualizarValor(Usuario usuario) {
        BigDecimal valorNuevo;
        if (Objects.equals(tipoValor, TIPO_VALOR_PUNTAJE)) {
            valorNuevo = inscripcion.getPuntaje();
        } else {
            valorNuevo = inscripcion.getAsistencia();
        }
        //if (valor ==null || !valor.equals(valorNuevo)) {
        if (!Objects.equals(valor, valorNuevo)) {
            valor = valorNuevo;
            fecha = new Date();
            creadoPor=usuario;
        }
    }
    
    

}
