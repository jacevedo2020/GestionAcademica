package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "curso_centro_capacitacion")
@XmlRootElement
public class CursoCentroCapacitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio")
    private BigDecimal precio;
    @Column(name = "nro_horas")
    private Integer nroHoras;
    @Column(name = "modalidad")
    private Character modalidad;
    @Column(name = "tipo_certificado")
    private Character tipoCertificado;
    @Column(name = "certificado_emitido_por")
    private Character certificadoEmitidoPor;

    public final static Character CERTIFICADO_EMITIDO_POR_INNOVATIVA = 'I';
    public final static Character CERTIFICADO_EMITIDO_POR_SENESCYT = 'S';

    @JoinColumn(name = "id_proyecto", referencedColumnName = "id")
    @ManyToOne
    private Proyecto proyecto;
    @JoinColumn(name = "creado_por", referencedColumnName = "id")
    @ManyToOne
    private Usuario creadoPor;
    @JoinColumn(name = "id_instructor", referencedColumnName = "id")
    @ManyToOne
    private Usuario instructor;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @Column(name = "fecha_inicio_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioInscripcion;
    @Column(name = "fecha_fin_inscripcion")
    @Temporal(TemporalType.DATE)
    private Date fechaFinInscripcion;
    @JoinColumn(name = "id_centro_capacitacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CentroCapacitacion centroCapacitacion;
    @JoinColumn(name = "id_curso", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Curso curso;
    @Column(name = "ciclo")
    private Integer ciclo;

    @JoinColumn(name = "id_usuario_aprobacion_notas", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioAprobacionNotas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoCentroCapacitacion", orphanRemoval = true)
    private List<Inscripcion> inscripcionList;

    @OneToMany(mappedBy = "cursoCentroCapacitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarioList;

    @JoinColumn(name = "id_responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario responsable;

    public CursoCentroCapacitacion() {
        fechaCreacion = new Date();
        horarioList = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            Horario h = new Horario();
            h.setCursoCentroCapacitacion(this);
            h.setDia(i);
            horarioList.add(h);
        }

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public CentroCapacitacion getCentroCapacitacion() {
        return centroCapacitacion;
    }

    public void setCentroCapacitacion(CentroCapacitacion centroCapacitacion) {
        this.centroCapacitacion = centroCapacitacion;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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

    public Usuario getInstructor() {
        return instructor;
    }

    public void setInstructor(Usuario instructor) {
        this.instructor = instructor;
    }

    public Date getFechaInicioInscripcion() {
        return fechaInicioInscripcion;
    }

    public void setFechaInicioInscripcion(Date fechaInicioInscripcion) {
        this.fechaInicioInscripcion = fechaInicioInscripcion;
    }

    public Date getFechaFinInscripcion() {
        return fechaFinInscripcion;
    }

    public void setFechaFinInscripcion(Date fechaFinInscripcion) {
        this.fechaFinInscripcion = fechaFinInscripcion;
    }

    public Character getCertificadoEmitidoPor() {
        return certificadoEmitidoPor;
    }

    public void setCertificadoEmitidoPor(Character certificadoEmitidoPor) {
        this.certificadoEmitidoPor = certificadoEmitidoPor;
    }

    @XmlTransient
    @JsonIgnore
    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public Usuario getUsuarioAprobacionNotas() {
        return usuarioAprobacionNotas;
    }

    public void setUsuarioAprobacionNotas(Usuario usuarioAprobacionNotas) {
        this.usuarioAprobacionNotas = usuarioAprobacionNotas;
    }

    public Character getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(Character tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    @XmlTransient
    @JsonIgnore
    public List<Horario> getHorarioList() {
        if (horarioList != null) {
            horarioList.sort((h1, h2) -> h1.getDia().compareTo(h2.getDia()));
        }
        return horarioList;
    }

    public void setHorarioList(List<Horario> horarioList) {
        this.horarioList = horarioList;
    }

    public Integer getCiclo() {
        return ciclo;
    }

    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
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
        if (!(object instanceof CursoCentroCapacitacion)) {
            return false;
        }
        CursoCentroCapacitacion other = (CursoCentroCapacitacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion[ id=" + id + " ]";
    }

    public String getModalidadDescripcion() {
        if (modalidad.equals(Curso.MODALIDAD_PRESENCIAL)) {
            return "Presencial";
        } else if (modalidad.equals(Curso.MODALIDAD_SEMIPRESENCIAL)) {
            return "Semipresencial";
        } else if (modalidad.equals(Curso.MODALIDAD_VIRTUAL)) {
            return "Virtual";
        } else if (modalidad.equals(Curso.MODALIDAD_ONLINE)) {
            return "Online";
        } else {
            return "";
        }
    }

    /**
     *
     * @return Inscripciones de estudiantes para los cursos corporativos
     * discrimina las inscripciones de la empresa
     */
    public List<Inscripcion> getInscripcionEstudianteList() {
        if (inscripcionList == null) {
            return new ArrayList<>();
        }
        return inscripcionList.stream()
                .filter(i -> i.isInscripcionEstudiante())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscipcion de la empresa
     */
    public Inscripcion getInscripcionEmpresa() {
        if (inscripcionList == null || inscripcionList.isEmpty()) {
            return null;
        }
        return inscripcionList.stream()
                .filter(i -> i.isInscripcionEmpresa())
                .findFirst()
                .orElse(null);
    }

    /**
     *
     * @return Inscripciones de estudiantes que estan pendientes de aprobacion
     * (validacion de pago)
     */
    public List<Inscripcion> getInscripcionEstudiantePendienteAprobacionList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEstadoMatriculaRegistrada())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones de estudiantes que han sido anuladas
     */
    public List<Inscripcion> getInscripcionEstudianteAnuladoList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEstadoMatriculaAnulada())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones de estudiantes que hayan sido aprobadas
     * (verificadas el pago)
     */
    public List<Inscripcion> getInscripcionEstudianteAprobadaList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEstadoMatriculaAprobada())
                .collect(Collectors.toList());
    }

    public List<Inscripcion> getInscripcionHabilitadaParaGenerarCertificadoList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEstadoHabilitadoParaGenerarCertificado())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones de estudiantes aprobadas y pendientes de aprobacion
     */
    public List<Inscripcion> getInscripcionHabilitadaParaRegistroNotaList() {
        /*List<Inscripcion> listaTemp = getInscripcionEstudiantePendienteAprobacionList();
        listaTemp.addAll(getInscripcionEstudianteAprobadaList());
        listaTemp.sort((i1, i2) -> i1.getEstudiante().getPersona().getNombresCompletos().compareTo(i2.getEstudiante().getPersona().getNombresCompletos()));*/
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEstadoHabilitadoParaRegistroNotas())
                .sorted((i1, i2) -> i1.getEstudiante().getPersona().getNombresCompletos().compareTo(i2.getEstudiante().getPersona().getNombresCompletos()))
                .collect(Collectors.toList());
    }

    public boolean isTieneInscripcionesHabilitadasParaRegistroNota() {
        return !getInscripcionHabilitadaParaRegistroNotaList().isEmpty();
    }

    /**
     *
     * @return Inscripciones de estudiantes que estan pendientes de finalizar el
     * ingreso de evaluacion por parte del instructor. toma en cuentas las q
     * estan en estado pendiente ingreso o solicitado recalificacion
     */
    public List<Inscripcion> getInscripcionEvaluacionFinalizacionPendienteList() {
        for (Inscripcion inscripcion : getInscripcionHabilitadaParaRegistroNotaList()) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println(inscripcion);
            System.out.println(inscripcion.isEvaluacionEstadoPendienteIngreso());
            System.out.println(inscripcion.isEvaluacionEstadoSolicitadoRecalificacion());
        }
        return getInscripcionHabilitadaParaRegistroNotaList().stream()
                .filter(i -> i.isEvaluacionEstadoPendienteIngreso() || i.isEvaluacionEstadoSolicitadoRecalificacion())
                .collect(Collectors.toList());
    }

    public List<Inscripcion> getInscripcionEvaluacionPendienteDigitarList() {
        return getInscripcionHabilitadaParaRegistroNotaList().stream()
                .filter(i -> i.isEvaluacionPendienteDigitar())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones que estan pendientes aprobar la evaluacion
     * ingresasa por el instructor
     */
    public List<Inscripcion> getInscripcionEvaluacionAprobacionPendienteList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEvaluacionEstadoPendienteAprobacion())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones que ha sido solicitado la recalificacion, por el
     * analista al instructor y que el instructor aun no finaliza la
     * recalificacion
     */
    public List<Inscripcion> getInscripcionEvaluacionRecalificacionSolicitadaList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEvaluacionEstadoSolicitadoRecalificacion())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones que el analista ha aprobado la evaluacion ingresada
     * por el instructor
     */
    public List<Inscripcion> getInscripcionEvaluacionAprobacionOkList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isEvaluacionEstadoAprobado())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones que yan han sido generados el certificado
     */
    public List<Inscripcion> getInscripcionCertificadoGeneracionOkList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoGeneracionOk())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return inscripciones que estan generados el certificado pero que aun no
     * han sido impresos
     */
    public List<Inscripcion> getInscripcionCertificadoImpresionPendienteList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoImpresionPendiente())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return inscripciones que estan generados el certificado y ya han sido
     * impresos
     *
     */
    public List<Inscripcion> getInscripcionCertificadoImpresionOkList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoImpresionOk())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones cuya evaluacion ya ha sido aprobada por el
     * analista, y el estudiante si aprueba el curso en base a esa evaluacion
     */
    public List<Inscripcion> getInscripcionEstudianteApruebaCursoConfirmadoList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isAlumnoApruebaCursoConfirmado())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return Inscripciones cuya evaluacion ya ha sido aprobada por el
     * analista, el estudiante si aprueba el curso en base a esa evaluacion pero
     * aun no se genera el certificado
     */
    public List<Inscripcion> getInscripcionCertificadoGeneracionPendienteList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoGeneracionPendiente())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return inscripciones que estan impresos el certificado pero que aun no
     * han sido enviados o entregados
     */
    public List<Inscripcion> getInscripcionCertificadoEntregaEnvioPendienteList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoEntregaEnvioPendiente())
                .collect(Collectors.toList());
    }

    /**
     *
     * @return inscripciones que estan impresos y el certificado ha sido enviado
     * por correos del ecuador
     */
    public List<Inscripcion> getInscripcionCertificadoEnvioOkList() {
        return getInscripcionEstudianteList().stream()
                .filter(i -> i.isCertificadoEnvioOk())
                .collect(Collectors.toList());
    }

    public String getTipoCertificacionDescripcion() {
        if (tipoCertificado.equals(Curso.TIPO_CERTIFICADO_ASISTENCIA)) {
            return "Asistencia";
        } else if (tipoCertificado.equals(Curso.TIPO_CERTIFICADO_APROBACION)) {
            return "Aprobación";
        } else {
            return "";
        }
    }

    public String getHorarioString() {
        Map<Integer, String> dias = new HashMap<>();
        dias.put(0, "LUN");
        dias.put(1, "MAR");
        dias.put(2, "MIE");
        dias.put(3, "JUE");
        dias.put(4, "VIE");
        dias.put(5, "SAB");
        dias.put(6, "DOM");

        String cadena = "";
        int diaInicio = 0;
        Date hora1 = getHorarioList().get(diaInicio).getHoraDesde();
        Date hora2 = getHorarioList().get(diaInicio).getHoraHasta();
        for (int i = 1; i < 7; i++) {
            if (hora1 == null) {
                if (getHorarioList().get(i).getHoraDesde() != null) {
                    diaInicio = i;
                    hora1 = getHorarioList().get(diaInicio).getHoraDesde();
                    hora2 = getHorarioList().get(diaInicio).getHoraHasta();
                }
                continue;
            }
            if (getHorarioList().get(i).getHoraDesde() == null || !getHorarioList().get(i).getHoraDesde().equals(hora1) || !getHorarioList().get(i).getHoraHasta().equals(hora2)) {
                if (!cadena.isEmpty()) {
                    cadena += ", ";
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                if ((diaInicio + 1) == i) {
                    cadena += dias.get(diaInicio) + " " + sdf.format(hora1) + " - " + sdf.format(hora2);
                } else {
                    cadena += dias.get(diaInicio) + " a " + dias.get(i - 1) + " " + sdf.format(hora1) + " - " + sdf.format(hora2);
                }
                diaInicio = i;
                hora1 = getHorarioList().get(diaInicio).getHoraDesde();
                hora2 = getHorarioList().get(diaInicio).getHoraHasta();
            }
        }
        return cadena;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        if (inscripcionList == null) {
            inscripcionList = new ArrayList<>();
        }
        inscripcion.setCursoCentroCapacitacion(this);
        inscripcionList.add(inscripcion);
    }

    public void eliminarInscripcion(Inscripcion inscripcion) {
        inscripcionList.remove(inscripcion);
    }

    public void eliminarInscripcionEstudianteTodos() {
        inscripcionList.removeIf(i -> i.getTipo().equals(Inscripcion.TIPO_ALUMNO));
    }

    public void cambiarEstadoEvaluacionPendienteAprobacion() {
        getInscripcionEvaluacionFinalizacionPendienteList().forEach(i -> i.setEstadoEvaluacion(Inscripcion.ESTADO_EVALUACION_PENDIENTE_APROBACION));
    }

    /**
     *
     * @return True si tiene al menos una insripcion esn estado PENDIENTE DE
     * APROBACION
     */
    public boolean isTieneInscripcionesEstadoPendienteAprobacion() {
        return !getInscripcionEstudiantePendienteAprobacionList().isEmpty();
    }

    /**
     *
     * @return True si tiene al menos una insripcion esn estado APROBADO
     */
    public boolean isTieneInscripcionesEstadoAprobado() {
        return !getInscripcionEstudianteAprobadaList().isEmpty();
    }

    /**
     *
     * @return true si se han ingresado las evaluaciones para todos los alumnos,
     * cuya inscripcion ha sido aprobada
     */
    /*public boolean isEvaluacionRegistroOk() {
         return getInscripcionEvaluacionFinalizacionPendienteList().isEmpty();
    }*/
    /**
     *
     * @return true si existe al menos una inscripcion que no ha sido registrada
     * la evaluacion ya sea porque aun no ingresa el instructor o el analista
     * solicito la recalificacion Sin importar si la inscripcion fue aprobada o
     * si esta en pendiente de aprobacion
     */
    public boolean isTieneInscripcionesEvaluacionPendiente() {

        /*return !getInscripcionEvaluacionFinalizacionPendienteList().isEmpty()
                || !getInscripcionEstudiantePendienteAprobacionList().isEmpty();*/
        return !getInscripcionEvaluacionFinalizacionPendienteList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que no ha sido registrada
     * la evaluacion ya sea porque aun no ingresa el instructor o el analista
     * solicito la recalificacion Solo para inscripciones aprobadas
     */
    public boolean isTieneInscripcionesAprobadasEvaluacionPendiente() {

        return !getInscripcionEvaluacionFinalizacionPendienteList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que no se ha digitado la
     * evaluacion (puntaje y/o asistencia) Solo de las inscipciones aprobadas
     */
    public boolean isTieneInscripcionesEvaluacionPendienteDigitar() {
        return !getInscripcionEvaluacionPendienteDigitarList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que el instructor haya
     * ingresado la evaluacion y que al analista aun no aprueba. false, caso
     * contrario
     */
    public boolean isTieneInscripcionesEvaluacionPendienteAprobar() {
        return !getInscripcionEvaluacionAprobacionPendienteList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que no este aprobada la
     * evaluacion ya sea que el instructor ya haya solicitado la aprobacion, o
     * ya sea q el instructor aun no finaliza el registro de evaluacion o ya sea
     * que la inscripcion aun no es aprobada
     */
    public boolean isTieneInscripcionesEvaluacionPorAprobar() {
        return !getInscripcionEvaluacionAprobacionPendienteList().isEmpty()
                || !getInscripcionEvaluacionRecalificacionSolicitadaList().isEmpty()
                || !getInscripcionEvaluacionFinalizacionPendienteList().isEmpty()
                || !getInscripcionEstudiantePendienteAprobacionList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que el analista ha
     * solicitado la recalificacion pero el instructor aun no lo hace
     */
    public boolean isEvaluacionRecalificacionPendiente() {
        return !getInscripcionEvaluacionRecalificacionSolicitadaList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion, para la cual el analista
     * ha aprobado la calificacion ingresada por el instructor
     */
    public boolean isEvaluacionAprobada() {
        return !getInscripcionEvaluacionAprobacionOkList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion, que con las notas
     * confirmadas el estudiante aprueba el curso y no ha sido generado aun el
     * certificado
     */
    public boolean isCertificadoGeneracionPendiente() {
        return !getInscripcionCertificadoGeneracionPendienteList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que ha sido generado el
     * certificado
     */
    public boolean isCertificadoGeneracionLista() {
        return !getInscripcionCertificadoGeneracionOkList().isEmpty();
    }

    /**
     *
     * @return true si existe al menos una inscripcion que ha sido impreso el
     * certificado
     */
    public boolean isCertificadoImpresionLista() {
        return !getInscripcionCertificadoImpresionOkList().isEmpty();
    }

    public boolean isActivoParaInscripcion() {
        Date hoy = new Date();
        return fechaInicioInscripcion.before(hoy) && fechaFinInscripcion.after(hoy);
    }

    public boolean isCursoCorporativo() {
        return Objects.equals(curso.getTipo(), Curso.TIPO_CORPORATIVO);
    }

    public boolean isCursoContinuo() {
        return Objects.equals(curso.getTipo(), Curso.TIPO_CONTINUO);
    }

    public void actualizarEvaluacion(Usuario usuario) {
        getInscripcionEvaluacionFinalizacionPendienteList().forEach(i -> i.actualizarValoresEvaluacion(usuario));
    }

    public void aprobarEvaluacion(Usuario usuario) {
        getInscripcionEvaluacionAprobacionPendienteList().forEach(i -> i.aprobarEvaluacion(usuario));
    }

    public boolean isTipoCertificadoAprobacion() {
        return Objects.equals(tipoCertificado, Curso.TIPO_CERTIFICADO_APROBACION);
    }

    public boolean isTipoCertificadoAsistencia() {
        return Objects.equals(tipoCertificado, Curso.TIPO_CERTIFICADO_ASISTENCIA);
    }

    public boolean isModalidadVirtual() {
        return Objects.equals(modalidad, Curso.MODALIDAD_VIRTUAL);
    }

    public boolean isCertificadoEmitidoPorSenescyt() {
        return Objects.equals(certificadoEmitidoPor, CERTIFICADO_EMITIDO_POR_SENESCYT);
    }

    public boolean isCertificadoEmitidoPorInnovativa() {
        return Objects.equals(certificadoEmitidoPor, CERTIFICADO_EMITIDO_POR_INNOVATIVA);
    }

    public boolean isDireccionObligatoria() {
        return Objects.equals(modalidad, Curso.MODALIDAD_VIRTUAL) || Objects.equals(modalidad, Curso.MODALIDAD_ONLINE);
    }

    public boolean verificarMatriculaEstudiante(String identificacion) {
        if (inscripcionList == null) {
            return false;
        }
        for (Inscripcion inscripcion : inscripcionList) {
            if (!inscripcion.isEstadoMatriculaAnulada()
                    && inscripcion.getEstudiante() != null
                    && inscripcion.getEstudiante().getPersona().getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public long getTotalPersonalParticular() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteServidorPublico()).count();
    }

    public long getTotalPersonalEgresadoESPE() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteEgresadoESPE()).count();
    }

    public long getTotalPersonalEstudianteESPE() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteEstudianteESPE()).count();
    }

    public long getTotalPersonalMilitaresFFAA() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteMilitaresFFAA()).count();
    }

    public long getTotalPersonalServidorPublicoESPE() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteServidorPublico()).count();
    }

    public long getTotalPersonalCapacidadesEspeciales() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteCapacidadesEspeciales()).count();
    }

    public long getTotalPersonalCorporativo() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isTipoEstudianteCorporativo()).count();
    }
    public long getTotalAprobados() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isAlumnoApruebaCursoConfirmado()).count();
    }
    public long getTotalReprobados() {
        if (getInscripcionEstudianteList() == null) {
            return 0;
        }
        return getInscripcionEstudianteList().stream().filter(i -> i.isAlumnoRepruebaCursoConfirmado()).count();
    }

    public Canton getCiudad(){
        if (isCursoContinuo()){
            return getCentroCapacitacion().getCanton();
        }else{
            return curso.getCanton();
        }
    }

}
