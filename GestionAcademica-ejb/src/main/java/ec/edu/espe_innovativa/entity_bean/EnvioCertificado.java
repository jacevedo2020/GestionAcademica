package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "envio_certificado")
@XmlRootElement
public class EnvioCertificado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 25)
    @Column(name = "guia")
    private String guia;
    @Column(name = "tipo_registro")
    private Character tipoRegistro;

    public final static Character TIPO_REGISTRO_ENTREGA_PRESENCIAL = 'E';
    public final static Character TIPO_REGISTRO_ENVIO = 'V';
    public final static Character TIPO_REGISTRO_DEVOLUCION = 'D';
    public final static Character TIPO_REGISTRO_REENVIO = 'R';

    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Size(max = 60)
    @Column(name = "destinatario_nombres")
    private String destinatarioNombres;
    @Size(max = 30)
    @Column(name = "destinatario_apellidos")
    private String destinatarioApellidos;
    @JoinColumn(name = "destinatario_parroquia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Parroquia destinatarioParroquia;
    @Size(max = 300)
    @Column(name = "destinatario_direccion")
    private String destinatarioDireccion;
    @Size(max = 250)
    @Column(name = "destinatario_referencia")
    private String destinatarioReferencia;
    @JoinColumn(name = "id_certificado", referencedColumnName = "id")
    @ManyToOne
    private Certificado certificado;
    @JoinColumn(name = "enviado_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario enviadoPor;
    
    @Column(name = "entregado_a_tercero")
    private Character entregadoATercero;
    public final static Character ENTREGADO_A_TERCERO_SI = 'S';
    public final static Character ENTREGADO_A_TERCERO_NO = 'N';

    public EnvioCertificado() {
        fechaRegistro = new Date();
    }

    public EnvioCertificado(Integer id) {
        this.id = id;
    }

    public EnvioCertificado(Integer id, Date fechaRegistro) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getGuia() {
        return guia;
    }

    public void setGuia(String guia) {
        this.guia = guia;
    }

    public Character getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Character tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDestinatarioNombres() {
        return destinatarioNombres;
    }

    public void setDestinatarioNombres(String destinatarioNombres) {
        this.destinatarioNombres = destinatarioNombres;
    }

    public String getDestinatarioApellidos() {
        return destinatarioApellidos;
    }

    public void setDestinatarioApellidos(String destinatarioApellidos) {
        this.destinatarioApellidos = destinatarioApellidos;
    }

    public Parroquia getDestinatarioParroquia() {
        return destinatarioParroquia;
    }

    public void setDestinatarioParroquia(Parroquia destinatarioParroquia) {
        this.destinatarioParroquia = destinatarioParroquia;
    }


    public String getDestinatarioDireccion() {
        return destinatarioDireccion;
    }

    public void setDestinatarioDireccion(String destinatarioDireccion) {
        this.destinatarioDireccion = destinatarioDireccion;
    }

    public String getDestinatarioReferencia() {
        return destinatarioReferencia;
    }

    public void setDestinatarioReferencia(String destinatarioReferencia) {
        this.destinatarioReferencia = destinatarioReferencia;
    }

    public Certificado getCertificado() {
        return certificado;
    }

    public void setCertificado(Certificado certificado) {
        this.certificado = certificado;
    }

    public Usuario getEnviadoPor() {
        return enviadoPor;
    }

    public void setEnviadoPor(Usuario enviadoPor) {
        this.enviadoPor = enviadoPor;
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
        if (!(object instanceof EnvioCertificado)) {
            return false;
        }
        EnvioCertificado other = (EnvioCertificado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.EnvioCertificado[ id=" + id + " ]";
    }

    public boolean isTipoRegistroEntregaPresencial(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_ENTREGA_PRESENCIAL);
    }
    public boolean isTipoRegistroEnvio(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_ENVIO);
    }
    public boolean isTipoRegistroDevolucion(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_DEVOLUCION);
    }
    public boolean isEnvioOk(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_ENVIO) 
                || Objects.equals(tipoRegistro, TIPO_REGISTRO_REENVIO); 
    }
    public boolean isEntregaOk(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_ENTREGA_PRESENCIAL);
    }
    public boolean isEnvioDevuelto(){
        return Objects.equals(tipoRegistro, TIPO_REGISTRO_DEVOLUCION); 
    }

    public Character getEntregadoATercero() {
        return entregadoATercero;
    }

    public void setEntregadoATercero(Character entregadoATercero) {
        this.entregadoATercero = entregadoATercero;
    }

     
}
