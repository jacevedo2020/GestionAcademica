package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "curso")
@XmlRootElement
public class Curso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nro_horas")
    private Integer nroHoras;
    @Column(name = "modalidad")
    private Character modalidad;
    @Column(name = "tipo_certificado")
    private Character tipoCertificado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio")
    private BigDecimal precio;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Proyecto proyecto;
    @JoinColumn(name = "creado_por", referencedColumnName = "id")
    @ManyToOne
    private Usuario creadoPor;
    @Column(name = "estado")
    private Character estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", orphanRemoval = true)
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionList;
    @JoinColumn(name = "id_programa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Programa programa;
    @Column(name = "tipo")
    private Character tipo;
    @Column(name = "nro_minimo_participantes")
    private Integer nroMinimoParticipantes;
    @JoinColumn(name = "id_canton", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Canton canton;

    public final static Character MODALIDAD_PRESENCIAL = 'P';
    public final static Character MODALIDAD_SEMIPRESENCIAL = 'S';
    public final static Character MODALIDAD_VIRTUAL = 'V';
    public final static Character MODALIDAD_ONLINE = 'O';

    public final static Character TIPO_CERTIFICADO_ASISTENCIA = 'A';
    public final static Character TIPO_CERTIFICADO_APROBACION = 'P';

    public final static Character TIPO_CONTINUO = 'C';
    public final static Character TIPO_CORPORATIVO = 'O';

    public final static Character ESTADO_ACTIVO = 'A';
    public final static Character ESTADO_INACTICO = 'I';

    
    @Transient
    private boolean seleccionado;
    
    public Curso() {
        estado = ESTADO_ACTIVO;
        tipo = TIPO_CONTINUO;
    }

    public Curso(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNroHoras() {
        return nroHoras;
    }

    public void setNroHoras(Integer nroHoras) {
        this.nroHoras = nroHoras;
    }

    public Character getModalidad() {
        return modalidad;
    }

    public void setModalidad(Character modalidad) {
        this.modalidad = modalidad;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getNroMinimoParticipantes() {
        return nroMinimoParticipantes;
    }

    public void setNroMinimoParticipantes(Integer nroMinimoParticipantes) {
        this.nroMinimoParticipantes = nroMinimoParticipantes;
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
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Curso[ id=" + id + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<CursoCentroCapacitacion> getCursoCentroCapacitacionList() {
        return cursoCentroCapacitacionList;
    }

    public void setCursoCentroCapacitacionList(List<CursoCentroCapacitacion> cursoCentroCapacitacionList) {
        this.cursoCentroCapacitacionList = cursoCentroCapacitacionList;
    }

    public void agregarCursoCentroCapacitacion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        if (cursoCentroCapacitacionList == null) {
            cursoCentroCapacitacionList = new ArrayList<>();
        }
        cursoCentroCapacitacion.setCurso(this);
        cursoCentroCapacitacionList.add(cursoCentroCapacitacion);
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public String getModalidadDescripcion() {
        if (modalidad.equals(MODALIDAD_PRESENCIAL)) {
            return "Presencial";
        } else if (modalidad.equals(MODALIDAD_SEMIPRESENCIAL)) {
            return "Semipresencial";
        } else if (modalidad.equals(MODALIDAD_VIRTUAL)) {
            return "Virtual";
        } else if (modalidad.equals(MODALIDAD_ONLINE)) {
            return "Online";
        } else {
            return "";
        }
    }

    public Character getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Character tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getTipoCertificacionDescripcion() {
        if (tipoCertificado.equals(TIPO_CERTIFICADO_ASISTENCIA)) {
            return "Asistencia";
        } else if (tipoCertificado.equals(TIPO_CERTIFICADO_APROBACION)) {
            return "Aprobación";
        } else {
            return "";
        }
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public String getTipoDescripcion() {
        if (tipo.equals(TIPO_CONTINUO)) {
            return "Continuo";
        } else if (tipo.equals(TIPO_CORPORATIVO)) {
            return "Corporativo";
        } else {
            return "";
        }
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

}
