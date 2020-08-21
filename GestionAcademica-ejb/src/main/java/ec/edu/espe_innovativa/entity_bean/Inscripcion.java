package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "inscripcion")
@XmlRootElement
public class Inscripcion implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "id_curso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CursoCentroCapacitacion cursoCentroCapacitacion;
    @JoinColumn(name = "creado_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario creadoPor;
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario estudiante;
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @JoinColumn(name = "aprobado_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario aprobadoPor;
    @Size(max = 250)
    @Column(name = "observacion_aprobacion")
    private String observacionAprobacion;

    @Column(name = "puntaje")
    private BigDecimal puntaje;

    @Column(name = "asistencia")
    private BigDecimal asistencia;

    @Column(name = "estado_evaluacion")
    private Character estadoEvaluacion;
    public final static Character ESTADO_EVALUACION_PENDIENTE_INGRESO = 'P';
    public final static Character ESTADO_EVALUACION_PENDIENTE_APROBACION = 'E';
    public final static Character ESTADO_EVALUACION_SOLICITADO_RECALIFICACION = 'R';
    public final static Character ESTADO_EVALUACION_APROBADO = 'A';

    @JoinColumn(name = "aprobacion_evaluacion_por", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario aprobacionEvaluacionPor;

    @Basic(optional = false)
    @NotNull
    @Column(name = "aprobacion_evaluacion_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date aprobacionEvaluacionFecha;

    @Column(name = "credito")
    private Character credito;
    public final static Character CREDITO_SI = 'S';
    public final static Character CREDITO_NO = 'N';

    @Column(name = "abono")
    private Character abono;
    public final static Character ABONO_SI = 'S';
    public final static Character ABONO_NO = 'N';

    @Column(name = "paga_empresa")
    private Character pagaEmpresa;
    public final static Character PAGA_EMPRESA_SI = 'S';
    public final static Character PAGA_EMPRESA_NO = 'N';

    @Column(name = "forma_pago")
    private Character formaPago;

    public final static Character FORMA_PAGO_EFECTIVO = 'E';
    public final static Character FORMA_PAGO_CHEQUE = 'C';
    public final static Character FORMA_PAGO_DEPOSITO = 'D';
    public final static Character FORMA_PAGO_TARJETA_CREDITO = 'T';
    public final static Character FORMA_PAGO_PAGO_LINEA = 'P';
    public final static Character FORMA_PAGO_TRANSFERENCIA_BANCARIA = 'B';

    @Column(name = "medio_entero_curso")
    private Character medioEnteroCurso;

    public final static Character MEDIO_REDES_SOCIALES = 'S';
    public final static Character MEDIO_PAGINA_WEB = 'P';
    public final static Character MEDIO_CORREO_ELECTRONICO = 'C';
    public final static Character MEDIO_FLYERS = 'Y';
    public final static Character MEDIO_PRENSA_ESCRITA = 'E';
    public final static Character MEDIO_RADIO = 'R';
    public final static Character MEDIO_REFERIDO = 'F';

    @JoinColumn(name = "factura_id_tipo_identificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoIdentificacion facturaTipoIdentificacion;
    @Size(max = 300)
    @Column(name = "factura_razon_social")
    private String facturaRazonSocial;
    @Size(max = 300)
    @Column(name = "factura_direccion")
    private String facturaDireccion;
    @Size(max = 13)
    @Column(name = "factura_ruc")
    private String facturaRuc;
    @Size(max = 10)
    @Column(name = "factura_telefono")
    private String facturaTelefono;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Error: Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "factura_email")
    private String facturaEmail;
    @Size(max = 20)
    @Column(name = "factura_numero")
    private String facturaNumero;
    @Column(name = "trabaja_actualmente")
    private Character trabajaActualmente;
    @Size(max = 50)
    @Column(name = "empresa_trabajo")
    private String empresaTrabajo;
    @Size(max = 50)
    @Column(name = "direccion_trabajo")
    private String direccionTrabajo;
    @Size(max = 10)
    @Column(name = "telefono_trabajo")
    private String telefonoTrabajo;
    @Size(max = 100)
    @Column(name = "documento_pago_nombre")
    private String documentoPagoNombre;
    @Size(max = 500)
    @Column(name = "documento_pago_url")
    private String documentoPagoUrl;
    @JoinColumn(name = "id_tipo_estudiante", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoEstudiante tipoEstudiante;
    @Column(name = "tipo")
    private Character tipo;
    public final static Character TIPO_EMPRESA = 'E'; //cuando se trata de curso corporativo, la primera inscripcion es de TIPO_EMPRESA
    public final static Character TIPO_ALUMNO = 'A';  //inscripcion individual alumno a un curso continuo
    public final static Character TIPO_ALUMNO_GRUPO = 'G';  //inscripcion grupal (cabecera) a un curso continuo 

    @JoinColumn(name = "id_inscripcion_padre", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inscripcion inscripcionPadre; //cuando se trata de una inscripcion grupal q paga la empresa, aqui se encuentra la inscriipcion de la empresa

    @Column(name = "tiene_discapacidad")
    private Character tieneDiscapacidad;
    @Size(max = 100)
    @Column(name = "tipo_discapacidad")
    private String tipoDiscapacidad;

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluacion> evaluacionList;

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificado> certificadoList;

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstadoInscripcion> estadoInscripcionList;

    @Column(name = "pagado")
    private Character pagado;
    public final static Character PAGADO_SI = 'S';
    public final static Character PAGADO_NO = 'N';

    @Transient
    private boolean seleccionado;
    @Transient
    private boolean seleccionado2;

    public Inscripcion() {
        fecha = new Date();
        estadoEvaluacion = ESTADO_EVALUACION_PENDIENTE_INGRESO;
        tipo = TIPO_ALUMNO;
        credito = CREDITO_NO;
        abono = ABONO_SI;
        pagaEmpresa = 'N';
        facturaTipoIdentificacion = new TipoIdentificacion(TipoIdentificacion.ID_TIPO_CEDULA);
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

    public CursoCentroCapacitacion getCursoCentroCapacitacion() {
        return cursoCentroCapacitacion;
    }

    public void setCursoCentroCapacitacion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        this.cursoCentroCapacitacion = cursoCentroCapacitacion;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Usuario getAprobadoPor() {
        return aprobadoPor;
    }

    public void setAprobadoPor(Usuario aprobadoPor) {
        this.aprobadoPor = aprobadoPor;
    }

    public String getObservacionAprobacion() {
        return observacionAprobacion;
    }

    public void setObservacionAprobacion(String observacionAprobacion) {
        this.observacionAprobacion = observacionAprobacion;
    }

    public Character getEstadoEvaluacion() {
        return estadoEvaluacion;
    }

    public void setEstadoEvaluacion(Character estadoEvaluacion) {
        this.estadoEvaluacion = estadoEvaluacion;
    }

    public void setFormaPago(Character formaPago) {
        this.formaPago = formaPago;
    }

    public Character getFormaPago() {
        return formaPago;
    }

    public Character getMedioEnteroCurso() {
        return medioEnteroCurso;
    }

    public void setMedioEnteroCurso(Character medioEnteroCurso) {
        this.medioEnteroCurso = medioEnteroCurso;
    }

    public String getFacturaRazonSocial() {
        return facturaRazonSocial;
    }

    public void setFacturaRazonSocial(String facturaRazonSocial) {
        this.facturaRazonSocial = facturaRazonSocial;
    }

    public String getFacturaDireccion() {
        return facturaDireccion;
    }

    public void setFacturaDireccion(String facturaDireccion) {
        this.facturaDireccion = facturaDireccion;
    }

    public String getFacturaRuc() {
        return facturaRuc;
    }

    public void setFacturaRuc(String facturaRuc) {
        this.facturaRuc = facturaRuc;
    }

    public String getFacturaTelefono() {
        return facturaTelefono;
    }

    public void setFacturaTelefono(String facturaTelefono) {
        this.facturaTelefono = facturaTelefono;
    }

    public String getFacturaEmail() {
        return facturaEmail;
    }

    public void setFacturaEmail(String facturaEmail) {
        this.facturaEmail = facturaEmail;
    }

    public String getFacturaNumero() {
        return facturaNumero;
    }

    public void setFacturaNumero(String facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    public Character getTrabajaActualmente() {
        return trabajaActualmente;
    }

    public void setTrabajaActualmente(Character trabajaActualmente) {
        this.trabajaActualmente = trabajaActualmente;
    }

    public String getEmpresaTrabajo() {
        return empresaTrabajo;
    }

    public void setEmpresaTrabajo(String empresaTrabajo) {
        this.empresaTrabajo = empresaTrabajo;
    }

    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }

    public void setDireccionTrabajo(String direccionTrabajo) {
        this.direccionTrabajo = direccionTrabajo;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public TipoIdentificacion getFacturaTipoIdentificacion() {
        return facturaTipoIdentificacion;
    }

    public void setFacturaTipoIdentificacion(TipoIdentificacion facturaTipoIdentificacion) {
        this.facturaTipoIdentificacion = facturaTipoIdentificacion;
    }

    public String getDocumentoPagoNombre() {
        return documentoPagoNombre;
    }

    public void setDocumentoPagoNombre(String documentoPagoNombre) {
        this.documentoPagoNombre = documentoPagoNombre;
    }

    public String getDocumentoPagoUrl() {
        return documentoPagoUrl;
    }

    public void setDocumentoPagoUrl(String documentoPagoUrl) {
        this.documentoPagoUrl = documentoPagoUrl;
    }

    public TipoEstudiante getTipoEstudiante() {
        return tipoEstudiante;
    }

    public void setTipoEstudiante(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public Character getCredito() {
        return credito;
    }

    public void setCredito(Character credito) {
        this.credito = credito;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public Character getTieneDiscapacidad() {
        return tieneDiscapacidad;
    }

    public void setTieneDiscapacidad(Character tieneDiscapacidad) {
        this.tieneDiscapacidad = tieneDiscapacidad;
    }

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    @XmlTransient
    @JsonIgnore
    public List<Evaluacion> getEvaluacionList() {
        return evaluacionList;
    }

    public void setEvaluacionList(List<Evaluacion> evaluacionList) {
        this.evaluacionList = evaluacionList;
    }

    public BigDecimal getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(BigDecimal puntaje) {
        this.puntaje = puntaje;
    }

    public BigDecimal getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(BigDecimal asistencia) {
        this.asistencia = asistencia;
    }

    public Usuario getAprobacionEvaluacionPor() {
        return aprobacionEvaluacionPor;
    }

    public void setAprobacionEvaluacionPor(Usuario aprobacionEvaluacionPor) {
        this.aprobacionEvaluacionPor = aprobacionEvaluacionPor;
    }

    public Date getAprobacionEvaluacionFecha() {
        return aprobacionEvaluacionFecha;
    }

    public void setAprobacionEvaluacionFecha(Date aprobacionEvaluacionFecha) {
        this.aprobacionEvaluacionFecha = aprobacionEvaluacionFecha;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public boolean isSeleccionado2() {
        return seleccionado2;
    }

    public void setSeleccionado2(boolean seleccionado2) {
        this.seleccionado2 = seleccionado2;
    }

    public List<EstadoInscripcion> getEstadoInscripcionList() {
        return estadoInscripcionList;
    }

    public void setEstadoInscripcionList(List<EstadoInscripcion> estadoInscripcionList) {
        this.estadoInscripcionList = estadoInscripcionList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Certificado> getCertificadoList() {
        return certificadoList;
    }

    public void setCertificadoList(List<Certificado> certificadoList) {
        this.certificadoList = certificadoList;
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
        if (!(object instanceof Inscripcion)) {
            return false;
        }
        Inscripcion other = (Inscripcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Inscripcion[ id=" + id + " ]";
    }

    public String getFormaPagoDesc() {
        if (Objects.equals(formaPago, FORMA_PAGO_EFECTIVO)) {
            return "Efectivo";
        }
        if (Objects.equals(formaPago, FORMA_PAGO_DEPOSITO)) {
            return "Depósito";
        }
        if (Objects.equals(formaPago, FORMA_PAGO_CHEQUE)) {
            return "Cheque";
        }
        if (Objects.equals(formaPago, FORMA_PAGO_PAGO_LINEA)) {
            return "Pago en Línea";
        }
        if (Objects.equals(formaPago, FORMA_PAGO_TARJETA_CREDITO)) {
            return "Tarjeta de Crédito";
        }
        if (Objects.equals(formaPago, FORMA_PAGO_TRANSFERENCIA_BANCARIA)) {
            return "Transferencia Bancaria";
        }
        return "";
    }

    public String getMedioEnteroCursoDesc() {
        if (Objects.equals(medioEnteroCurso, MEDIO_CORREO_ELECTRONICO)) {
            return "Correo Electrónico";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_FLYERS)) {
            return "Flyers";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_PAGINA_WEB)) {
            return "Página Web";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_PRENSA_ESCRITA)) {
            return "Prensa Escrita";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_RADIO)) {
            return "Radio";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_REDES_SOCIALES)) {
            return "Redes Sociales";
        }
        if (Objects.equals(medioEnteroCurso, MEDIO_REFERIDO)) {
            return "Referido";
        }
        return "";
    }

    public String getDocumentoPagoUrlCompleto() {
        try {
            return this.documentoPagoUrl + "/" + this.documentoPagoNombre;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @return si el alumno aprobo o no el curso, o si esta pendiente de
     * registrar la evaluacion. Para visualizar en pantalla de alumno
     */
    public String getEstadoAprobacionCurso() {
        if (!Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_APROBADO)) {
            return "Pendiente de registrar";
        }
        if (isAlumnoApruebaCurso()) {
            return "Aprobado";
        } else {
            return "Reprobado";
        }
    }


    /**
     *
     * @return true si con las notas de puntaje y asistencia, el alumno aprueba
     * el curso 
     */
    public boolean isAlumnoApruebaCurso() {
        if (cursoCentroCapacitacion.getTipoCertificado().equals(Curso.TIPO_CERTIFICADO_APROBACION)) {
            if (puntaje == null || asistencia == null) {
                return false;
            }
            return puntaje.compareTo(new BigDecimal(7)) >= 0 && asistencia.compareTo(new BigDecimal(80)) >= 0;
        } else {
            if (asistencia == null) {
                return false;
            }
            return asistencia.compareTo(new BigDecimal(80)) >= 0;
        }
    }

    /**
     *
     * @return true si con las notas de puntaje y asistencia, el alumno reprueba
     * el curso 
     */
    public boolean isAlumnoRepruebaCurso() {
        if (cursoCentroCapacitacion.getTipoCertificado().equals(Curso.TIPO_CERTIFICADO_APROBACION)) {
            if (puntaje == null || asistencia == null) {
                return false;
            }
            return puntaje.compareTo(new BigDecimal(7)) < 0 && asistencia.compareTo(new BigDecimal(80)) < 0;
        } else {
            if (asistencia == null) {
                return false;
            }
            return asistencia.compareTo(new BigDecimal(80)) < 0;
        }
    }
    
    
    /**
     *
     * @return true si la evaluacion ingresada por el instructor ha sido
     * aprobada por el analista y si con esas notas (puntaje y asistencia) el
     * estudiante aprueba el curso
     */
    public boolean isAlumnoApruebaCursoConfirmado() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_APROBADO) && isAlumnoApruebaCurso();
    }

    /**
     *
     * @return true si la evaluacion ingresada por el instructor ha sido
     * aprobada por el analista y si con esas notas (puntaje y asistencia) el
     * estudiante reprueba el curso
     */
    public boolean isAlumnoRepruebaCursoConfirmado() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_APROBADO) && isAlumnoRepruebaCurso();
    }
    public void agregarEvaluacion(Character tipoValor, Usuario usuario) {
        if (evaluacionList == null) {
            evaluacionList = new ArrayList<>();
        }
        Evaluacion e = new Evaluacion();
        e.setTipoValor(tipoValor);
        e.setCreadoPor(usuario);
        if (Objects.equals(tipoValor, Evaluacion.TIPO_VALOR_PUNTAJE)) {
            e.setValor(puntaje);
        } else {
            e.setValor(asistencia);
        }
        e.setInscripcion(this);
        evaluacionList.add(e);
    }

    public Evaluacion getEvaluacionAsistenciaInicial() {
        return getEvaluacionInicial(Evaluacion.TIPO_VALOR_ASISTENCIA);
    }

    public Evaluacion getEvaluacionPuntajeInicial() {
        return getEvaluacionInicial(Evaluacion.TIPO_VALOR_PUNTAJE);
    }

    public Evaluacion getEvaluacionAsistenciaModificada() {
        return getEvaluacionModificada(Evaluacion.TIPO_VALOR_ASISTENCIA);
    }

    public Evaluacion getEvaluacionPuntajeModificado() {
        return getEvaluacionModificada(Evaluacion.TIPO_VALOR_PUNTAJE);
    }

    private Evaluacion getEvaluacionInicial(Character tipoValor) {
        if (evaluacionList == null) {
            return null;
        }
        List<Evaluacion> evaluacionListTemp = evaluacionList.stream()
                .filter(e -> e.getId() != null)
                .sorted((e1, e2) -> e1.getId().compareTo(e2.getId()))
                .collect(Collectors.toList());
        for (Evaluacion e : evaluacionListTemp) {
            if (Objects.equals(e.getTipoValor(), tipoValor)) {
                return e;
            }
        }
        return null;
    }

    private Evaluacion getEvaluacionModificada(Character tipoValor) {
        if (evaluacionList == null) {
            return null;
        }
        List<Evaluacion> evaluacionListTemp = evaluacionList.stream()
                .filter(e -> e.getId() != null)
                .sorted((e1, e2) -> e1.getId().compareTo(e2.getId()))
                .collect(Collectors.toList());
        int contador = 0;
        for (Evaluacion e : evaluacionListTemp) {
            if (Objects.equals(e.getTipoValor(), tipoValor)) {
                contador++;
                if (contador == 2) {
                    return e;
                }
            }
        }
        return null;
    }

    public void actualizarValoresEvaluacion(Usuario usuario) {
        Evaluacion e;
        if (Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_PENDIENTE_INGRESO)) {
            e = getEvaluacionInicial(Evaluacion.TIPO_VALOR_PUNTAJE);
            actualizarEvaluacion(e, usuario, Evaluacion.TIPO_VALOR_PUNTAJE);

            e = getEvaluacionInicial(Evaluacion.TIPO_VALOR_ASISTENCIA);
            actualizarEvaluacion(e, usuario, Evaluacion.TIPO_VALOR_ASISTENCIA);
        } else if (Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_PENDIENTE_APROBACION)) {
            e = getEvaluacionModificada(Evaluacion.TIPO_VALOR_ASISTENCIA);
            actualizarEvaluacion(e, usuario, Evaluacion.TIPO_VALOR_ASISTENCIA);
        } else if (Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_SOLICITADO_RECALIFICACION)) {
            e = getEvaluacionModificada(Evaluacion.TIPO_VALOR_PUNTAJE);
            actualizarEvaluacion(e, usuario, Evaluacion.TIPO_VALOR_PUNTAJE);
        }

    }

    private void actualizarEvaluacion(Evaluacion evaluacion, Usuario usuario, Character tipoValor) {
        if (evaluacion == null) {
            if (Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_PENDIENTE_INGRESO)) {
                if ((Objects.equals(tipoValor, Evaluacion.TIPO_VALOR_PUNTAJE) && puntaje != null)
                        || (Objects.equals(tipoValor, Evaluacion.TIPO_VALOR_ASISTENCIA) && asistencia != null)) {
                    agregarEvaluacion(tipoValor, usuario);
                }
            } else { //solicitado recalificacion
                if ((puntaje == null && getEvaluacionPuntajeInicial().getValor() != null)
                        || (puntaje != null && getEvaluacionPuntajeInicial().getValor() == null)
                        || puntaje.compareTo(getEvaluacionPuntajeInicial().getValor()) != 0) {
                    agregarEvaluacion(tipoValor, usuario);
                }
            }
        } else {
            evaluacion.actualizarValor(usuario);
        }
    }

    public void aprobarEvaluacion(Usuario usuario) {
        estadoEvaluacion = ESTADO_EVALUACION_APROBADO;
        aprobacionEvaluacionPor = usuario;
        aprobacionEvaluacionFecha = new Date();
    }

    public void agregarCertificado(Certificado certificado) {
        if (certificadoList == null) {
            certificadoList = new ArrayList<>();
        }
        certificado.setInscripcion(this);
        certificadoList.add(certificado);
    }

    /**
     *
     * @return True si el estado de evaluacion esta en estado PENDIENTE INGRESO
     * sin importar el estado de la inscripcion (Aprobada o No)
     */
    public boolean isEvaluacionEstadoPendienteIngreso() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_PENDIENTE_INGRESO);
    }

    public boolean isEvaluacionEstadoPendienteAprobacion() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_PENDIENTE_APROBACION);
    }

    public boolean isEvaluacionEstadoSolicitadoRecalificacion() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_SOLICITADO_RECALIFICACION);
    }

    public boolean isEvaluacionEstadoAprobado() {
        return Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_APROBADO);
    }

    /**
     *
     * @return True, si la inscripcion esta Lista para ingresar evaluacion La
     * inscripcion ha sido aprobada, y el instructor aun no finaliza el ingreso
     * O el analista ha solicitado la recalificacion,
     */
    public boolean isEvaluacionPorIngresar() {
        return (isEstadoHabilitadoParaRegistroNotas() && isEvaluacionEstadoPendienteIngreso())
                || Objects.equals(estadoEvaluacion, ESTADO_EVALUACION_SOLICITADO_RECALIFICACION);
    }

    /**
     *
     * @return True si el instructor ya ha finalizado el ingreso de la
     * evaluacion ya sea que el analista ha aprobado la evaluacion o aun no lo
     * aprueba
     */
    public boolean isEvaluacionIngresada() {
        return (isEvaluacionEstadoPendienteAprobacion() || isEvaluacionEstadoAprobado());
    }

    public boolean isEvaluacionPendienteDigitar() {
        if (cursoCentroCapacitacion.isTipoCertificadoAprobacion()) {
            if (puntaje == null) {
                return true;
            }
        }
        return (asistencia == null);
    }

    /**
     *
     * @return true si el estudiante ha aprobado el curso (notas confirmadas) y
     * que aun no se ha generado el certificado
     */
    public boolean isCertificadoGeneracionPendiente() {
        return isAlumnoApruebaCursoConfirmado() && (certificadoList == null || certificadoList.isEmpty());
    }

    public boolean isCertificadoGeneracionOk() {
        if (certificadoList == null) {
            return false;
        }
        return certificadoList.size() > 0;
    }

    public boolean isCertificadoImpresionPendiente() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return false;
        }
        return !getUltimoCertificado().isImpreso();
    }

    public boolean isCertificadoImpresionOk() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return false;
        }
        return getUltimoCertificado().isImpreso();
    }

    public boolean isCertificadoEntregaEnvioPendiente() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return false;
        }
        return getUltimoCertificado().isImpreso() && !getUltimoCertificado().isEntregaEnvioOk();
    }

    public boolean isCertificadoEntregaEnvioOk() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return false;
        }
        return getUltimoCertificado().isEntregaEnvioOk();
    }

    public boolean isCertificadoEnvioOk() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return false;
        }
        return getUltimoCertificado().isEnvioOk();
    }

    public boolean isInscripcionEstudiante() {
        return Objects.equals(tipo, TIPO_ALUMNO);
    }

    public boolean isInscripcionEstudianteGrupo() {
        return Objects.equals(tipo, TIPO_ALUMNO_GRUPO);
    }

    public boolean isInscripcionEmpresa() {
        return Objects.equals(tipo, TIPO_EMPRESA);
    }

    public boolean isEstadoMatriculaAprobada() {
        return getUltimoEstadoInscripcion().getEstado().isEstadoMatriculaAprobada();
    }

    public boolean isEstadoMatriculaAnulada() {
        return getUltimoEstadoInscripcion().getEstado().isEstadoMatriculaAnulada();
    }

    public boolean isEstadoMatriculaRegistrada() {
        return getUltimoEstadoInscripcion().getEstado().isEstadoMatriculaRegistrada();
    }

    public boolean isEstadoHabilitadoParaRegistroNotas() {
        return !isEstadoMatriculaRegistrada() && !isEstadoMatriculaAnulada();
    }

    public boolean isEstadoHabilitadoParaGenerarCertificado() {
        return !isEstadoMatriculaRegistrada() && !isEstadoMatriculaAnulada();
    }

    public Certificado getUltimoCertificado() {
        if (certificadoList == null || certificadoList.isEmpty()) {
            return null;
        }
        return certificadoList.stream().max((c1, c2) -> c1.getId().compareTo(c2.getId())).get();
    }

    public Character getAbono() {
        return abono;
    }

    public void setAbono(Character abono) {
        this.abono = abono;
    }

    public Character getPagaEmpresa() {
        return pagaEmpresa;
    }

    public void setPagaEmpresa(Character pagaEmpresa) {
        this.pagaEmpresa = pagaEmpresa;
    }

    public boolean isCreditoSi() {
        return Objects.equals(credito, CREDITO_SI);
    }

    public boolean isAbonoNo() {
        return Objects.equals(abono, ABONO_NO);
    }

    public boolean isAbonoSi() {
        return Objects.equals(abono, ABONO_SI);
    }

    public boolean isPagaEmpresaNo() {
        return Objects.equals(pagaEmpresa, PAGA_EMPRESA_NO);
    }

    public boolean isPagaEmpresaSi() {
        return Objects.equals(pagaEmpresa, PAGA_EMPRESA_SI);
    }

    public EstadoInscripcion getUltimoEstadoInscripcion() {
        if (estadoInscripcionList == null || estadoInscripcionList.isEmpty()) {
            return null;
        }
        return estadoInscripcionList.stream().max((c1, c2) -> c1.getId().compareTo(c2.getId())).get();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Inscripcion inscripcionClonado = (Inscripcion) super.clone();
        Usuario estudianteClonado = (Usuario) estudiante.clone();
        inscripcionClonado.setEstudiante(estudianteClonado);
        return inscripcionClonado;
    }

    public void agregarEstado(EstadoInscripcion estadoInscripcion) {
        if (estadoInscripcionList == null) {
            estadoInscripcionList = new ArrayList<>();
        }
        estadoInscripcion.setInscripcion(this);
        estadoInscripcionList.add(estadoInscripcion);
    }

    public boolean isFormaPagoEfectivo() {
        return formaPago == FORMA_PAGO_EFECTIVO;
    }

    public Inscripcion getInscripcionPadre() {
        return inscripcionPadre;
    }

    public void setInscripcionPadre(Inscripcion inscripcionPadre) {
        this.inscripcionPadre = inscripcionPadre;
    }

    public Character getPagado() {
        return pagado;
    }

    public void setPagado(Character pagado) {
        this.pagado = pagado;
    }

    public boolean filtrarPorAnalista(Usuario usuario) {
        if (cursoCentroCapacitacion.isCursoContinuo() && cursoCentroCapacitacion.getProyecto().getResponsable().equals(usuario)) {
            return true;
        }
        if (cursoCentroCapacitacion.isCursoCorporativo() && cursoCentroCapacitacion.getResponsable().equals(usuario)) {
            return true;
        }
        return false;
    }

    public boolean isPagadoSi() {
        if (pagado != null && pagado == 'S') {
            return true;
        }
        if (pagado != null && pagado == 'N') {
            return false;
        }
        if (cursoCentroCapacitacion.isCursoContinuo()
                && !isCreditoSi()) {
            return true;
        }
        return false;
    }

    public Integer getEdadEstudiante() {
        LocalDate fechaNacimiento = estudiante.getPersona().getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaInscripcion = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(fechaNacimiento, fechaInscripcion);
        return period.getYears();

    }

    public boolean isTipoEstudianteParticular() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_PARTICULAR);
    }

    public boolean isTipoEstudianteEgresadoESPE() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_EGRESADO_ESPE);
    }

    public boolean isTipoEstudianteEstudianteESPE() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_ESTUDIANTE_ESPE);
    }

    public boolean isTipoEstudianteMilitaresFFAA() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_MILITARES_FFAA);
    }

    public boolean isTipoEstudianteServidorPublico() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_SERVIDOR_PUBLICO_ESPE);
    }

    public boolean isTipoEstudianteCapacidadesEspeciales() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_CAPACIDADES_ESPECIALES);
    }

    public boolean isTipoEstudianteCorporativo() {
        if (tipoEstudiante == null) {
            return false;
        }
        return tipoEstudiante.getId().equals(TipoEstudiante.ID_CORPORATIVO);
    }

}
