package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "certificado")
@XmlRootElement
public class Certificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_para_impresion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaParaImpresion;
    @Column(name = "tipo")
    private Character tipo;
    @Size(max = 100)
    @Column(name = "documento_nombre")
    private String documentoNombre;
    @Size(max = 500)
    @Column(name = "documento_url")
    private String documentoUrl;
    @Column(name = "secuencial")
    private Integer secuencial;
    @Column(name = "entregado")
    private Character entregado;

    public final static Character ENTREGADO_SI = 'S';
    public final static Character ENTREGADO_NO = 'N';

    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;
    @Size(max = 10)
    @Column(name = "cedula_recibe")
    private String cedulaRecibe;
    @Size(max = 50)
    @Column(name = "nombres_recibe")
    private String nombresRecibe;
    @Size(max = 50)
    @Column(name = "apellidos_recibe")
    private String apellidosRecibe;
    @JoinColumn(name = "id_inscripcion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inscripcion inscripcion;
    @JoinColumn(name = "creado_por", referencedColumnName = "id")
    @ManyToOne
    private Usuario creadoPor;
    @JoinColumn(name = "entregado_por", referencedColumnName = "id")
    @ManyToOne
    private Usuario entregadoPor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "certificado", orphanRemoval = true)
    private List<ImpresionCertificado> impresionCertificadoList;
    @OneToMany(mappedBy = "certificado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnvioCertificado> envioCertificadoList;

    public Certificado() {
        entregado = ENTREGADO_NO;
        fechaCreacion = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public String getDocumentoNombre() {
        return documentoNombre;
    }

    public void setDocumentoNombre(String documentoNombre) {
        this.documentoNombre = documentoNombre;
    }

    public String getDocumentoUrl() {
        return documentoUrl;
    }

    public void setDocumentoUrl(String documentoUrl) {
        this.documentoUrl = documentoUrl;
    }

    public Integer getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Integer secuencial) {
        this.secuencial = secuencial;
    }

    public Character getEntregado() {
        return entregado;
    }

    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getCedulaRecibe() {
        return cedulaRecibe;
    }

    public void setCedulaRecibe(String cedulaRecibe) {
        this.cedulaRecibe = cedulaRecibe;
    }

    public String getNombresRecibe() {
        return nombresRecibe;
    }

    public void setNombresRecibe(String nombresRecibe) {
        this.nombresRecibe = nombresRecibe;
    }

    public String getApellidosRecibe() {
        return apellidosRecibe;
    }

    public void setApellidosRecibe(String apellidosRecibe) {
        this.apellidosRecibe = apellidosRecibe;
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

    public Usuario getEntregadoPor() {
        return entregadoPor;
    }

    public void setEntregadoPor(Usuario entregadoPor) {
        this.entregadoPor = entregadoPor;
    }

    public String getDocumentoUrlCompleto() {
        try {
            return this.documentoUrl + "/" + this.documentoNombre;
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
        if (!(object instanceof Certificado)) {
            return false;
        }
        Certificado other = (Certificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Certificado[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<ImpresionCertificado> getImpresionCertificadoList() {
        return impresionCertificadoList;
    }

    public void setImpresionCertificadoList(List<ImpresionCertificado> impresionCertificadoList) {
        this.impresionCertificadoList = impresionCertificadoList;
    }

    public void registrarImpresion(Usuario usuario) {
        ImpresionCertificado impresionCertificado = new ImpresionCertificado();
        impresionCertificado.setImpresoPor(usuario);
        impresionCertificado.setCertificado(this);
        if (impresionCertificadoList == null) {
            impresionCertificadoList = new ArrayList<>();
        }
        impresionCertificadoList.add(impresionCertificado);
    }

    public boolean isImpreso() {
        if (impresionCertificadoList == null) {
            return false;
        }
        return !impresionCertificadoList.isEmpty();
    }

    /**
     *
     * @return True, si el certificado tiene el estado de ENVIADO o REENVIADO
     * por correos del ecuador
     */
    public boolean isEnvioOk() {
        if (envioCertificadoList == null || envioCertificadoList.isEmpty()) {
            return false;
        }
        return getEnvioCertificado().isEnvioOk();
    }

    /**
     *
     * @return True, si el certificado tiene el estado de ENTREGADO
     * personalmente
     */
    public boolean isEntregaOk() {
        if (envioCertificadoList == null || envioCertificadoList.isEmpty()) {
            return false;
        }
        return getEnvioCertificado().isEntregaOk();
    }

    /**
     *
     * @return True, si el certificado si ha sido ENTREGADO (personalmente) o si
     * fue ENVIADo o REENVIADO (por correos del Ecuador),
     */
    public boolean isEntregaEnvioOk() {
        if (envioCertificadoList == null || envioCertificadoList.isEmpty()) {
            return false;
        }
        return !getEnvioCertificado().isEnvioDevuelto();
    }

    /**
     *
     * @return El ultimo registro de envio de certificado
     */
    public EnvioCertificado getEnvioCertificado() {
        if (envioCertificadoList == null) {
            return null;
        }
        return envioCertificadoList.stream()
                .max((e1, e2) -> e1.getId().compareTo(e2.getId()))
                .orElse(null);
    }

    @XmlTransient
    @JsonIgnore
    public List<EnvioCertificado> getEnvioCertificadoList() {
        return envioCertificadoList;
    }

    public void setEnvioCertificadoList(List<EnvioCertificado> envioCertificadoList) {
        this.envioCertificadoList = envioCertificadoList;
    }

    public void agregarEnvio(EnvioCertificado envioCertificado) {
        if (envioCertificadoList == null) {
            envioCertificadoList = new ArrayList<>();
        }
        envioCertificado.setCertificado(this);
        envioCertificadoList.add(envioCertificado);
    }

    public Date getFechaParaImpresion() {
        return fechaParaImpresion;
    }

    public void setFechaParaImpresion(Date fechaParaImpresion) {
        this.fechaParaImpresion = fechaParaImpresion;
    }
    
    

}
