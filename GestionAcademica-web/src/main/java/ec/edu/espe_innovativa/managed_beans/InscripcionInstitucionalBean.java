package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import com.jvc.medisys.validador.ValidadorCedula;
import com.jvc.medisys.validador.ValidadorEmail;
import com.jvc.medisys.validador.ValidadorNumero;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.CentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Evaluacion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.TipoEstudiante;
import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.CursoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.PersonaFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.FileUploadEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named(value = "inscripcionInstitucionalBean")
@ViewScoped
public class InscripcionInstitucionalBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroCapacitacionFacadeLocal;
    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    @EJB
    private CursoFacadeLocal cursoFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @Inject
    private MenuBarBean menuBarBean;
    @Inject
    private ConexionRegistroCivilBean conexionRegistroCivilBean;
    private List<PersonaTemp> personaTempList;
    private Provincia provincia;
    private Canton canton;
    private boolean agregarAlumno;
    private boolean errorCargaMasiva;
    private List<CursoCentroCapacitacion> cursoCentroCapacitacionList;

    private CursoCentroCapacitacion cursoCentroCapacitacion;

    public InscripcionInstitucionalBean() {
    }

    @PostConstruct
    public void init() {
        cursoCentroCapacitacionList = cursoCentroCapacitacionFacadeLocal.findAll().stream().filter(cc -> cc.getCurso().getTipo().equals(Curso.TIPO_CORPORATIVO)).collect(Collectors.toList());
        cursoCentroCapacitacion = null;
        provincia = null;
        canton = null;
    }

    public void nuevoEstudianteCargaMasiva() {
        agregarAlumno = true;
        personaTempList = new ArrayList<>();
    }

    public void eliminarInscripcionEstudiante(Inscripcion inscripcion) {
        cursoCentroCapacitacion.eliminarInscripcion(inscripcion);
    }

    public void eliminarEstudiantesTodos() {
        if (cursoCentroCapacitacion.getInscripcionEstudianteList() == null
                || cursoCentroCapacitacion.getInscripcionEstudianteList().isEmpty()) {
            FacesUtils.addErrorMessage("No existen estudiantes a eliminar");
            return;
        }
        cursoCentroCapacitacion.eliminarInscripcionEstudianteTodos();
    }

    public void nuevo() {
        Curso curso = new Curso();
        curso.setTipo(Curso.TIPO_CORPORATIVO);
        cursoCentroCapacitacion = new CursoCentroCapacitacion();
        cursoCentroCapacitacion.setCentroCapacitacion(new CentroCapacitacion(1));
        cursoCentroCapacitacion.setCurso(curso);
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setCreadoPor(menuBarBean.getUsuario());
        inscripcion.setEstudiante(new Usuario());
        inscripcion.setTipo(Inscripcion.TIPO_EMPRESA);
        inscripcion.setFacturaTipoIdentificacion(new TipoIdentificacion(TipoIdentificacion.ID_TIPO_RUC));
        cursoCentroCapacitacion.agregarInscripcion(inscripcion);
    }

    public void buscarEmpresa() {
        Inscripcion inscripcion = cursoCentroCapacitacion.getInscripcionEmpresa();
        Usuario u = usuarioFacadeLocal.findByIdentificacion(inscripcion.getFacturaRuc());
        if (u == null) {
            u = new Usuario();
        }
        inscripcion.setFacturaRazonSocial(u.getPersona().getNombresCompletos());
        inscripcion.setFacturaDireccion(u.getPersona().getDireccion());
        inscripcion.setFacturaEmail(u.getPersona().getEmail());
        inscripcion.setFacturaTelefono(u.getPersona().getTelefono());
        inscripcion.setEstudiante(u);
        if (u.getPersona().getParroquia() != null) {
            provincia = u.getPersona().getParroquia().getCanton().getProvincia();
            canton = u.getPersona().getParroquia().getCanton();
        }
    }

    public void seleccionar(CursoCentroCapacitacion cursoCentroCapacitacion) {
        this.cursoCentroCapacitacion = cursoCentroCapacitacion;
        provincia = cursoCentroCapacitacion.getInscripcionEmpresa().getEstudiante().getPersona().getParroquia().getCanton().getProvincia();
        canton = cursoCentroCapacitacion.getInscripcionEmpresa().getEstudiante().getPersona().getParroquia().getCanton();
    }

    public void eliminar(CursoCentroCapacitacion cursoCentroCapacitacion) {
        try {
            cursoFacadeLocal.remove(cursoCentroCapacitacion.getCurso());
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public boolean validarHora(FacesContext context, List<UIInput> components, List<Object> values) {
        if (values.get(0) == null && values.get(1) == null) {
            return true;
        }
        if (values.get(0) == null || values.get(1) == null) {
            return false;
        }
        Date hora1 = (Date) values.get(0);
        Date hora2 = (Date) values.get(1);
        return hora2.after(hora1);
    }

    public boolean validarFecha(FacesContext context, List<UIInput> components, List<Object> values) {
        if (values.get(0) == null || values.get(1) == null) {
            return true;
        }
        Date fecha1 = (Date) values.get(0);
        Date fecha2 = (Date) values.get(1);
        return !fecha2.before(fecha1);
    }

    public void grabar() {
        try {
            inscripcionFacadeLocal.createOrEditInscripcionEmpresa(cursoCentroCapacitacion.getInscripcionEmpresa());
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }

    }

    public void subirDocumentoCargaMasiva(FileUploadEvent event) {
        personaTempList = new ArrayList<>();
        errorCargaMasiva = false;
        boolean errorWebServiceRegistroCivil = false;
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(event.getFile().getInputstream()));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter('|').withHeader().parse(br);) {
            for (CSVRecord record : parser) {
                PersonaTemp personaTemp = new PersonaTemp();
                personaTemp.setNombresCompletos(record.get(0).trim());
                //personaTemp.setNombres(record.get(1).trim());
                personaTemp.setTipoIdentificacion(record.get(1).trim());
                personaTemp.setIdentificacion(record.get(2).trim());
                personaTemp.setFechaNacimiento(record.get(3).trim());
                personaTemp.setGenero(record.get(4).trim());
                personaTemp.setEmail(record.get(5).trim().toLowerCase());
                personaTemp.setTelefono(record.get(6).trim());
                personaTemp.setCelular(record.get(7).trim());
                personaTemp.setDiscapacidad(record.get(8).trim());
                personaTemp.setTipoDiscapacidad(record.get(9).trim());

                /*if (personaTemp.getApellidos().isEmpty()) {
                    personaTemp.agregarError("APELLIDOS: No existe un valor.");
                } else if (personaTemp.getApellidos().length() > 30) {
                    personaTemp.agregarError("APELLIDOS. No debe tener más de 30 caracteres.");
                }

                if (personaTemp.getNombres().isEmpty()) {
                    personaTemp.agregarError("NOMBRES: No existe un valor.");
                } else if (personaTemp.getNombres().length() > 30) {
                    personaTemp.agregarError("NOMBRES. No debe tener más de 30 caracteres.");
                }*/
                if (personaTemp.getNombresCompletos().isEmpty()) {
                    personaTemp.agregarError("NOMBRES: No existe un valor.");
                } else if (personaTemp.getNombresCompletos().length() > 60) {
                    personaTemp.agregarError("NOMBRES. No debe tener más de 60 caracteres.");
                }

                if (personaTemp.getTipoIdentificacion().isEmpty()) {
                    personaTemp.agregarError("TIPO IDENTIFICACION: No existe un valor.");
                } else if (personaTemp.getTipoIdentificacion().length() > 1 || (!personaTemp.getTipoIdentificacion().equals("C") && !personaTemp.getTipoIdentificacion().equals("P"))) {
                    personaTemp.agregarError("TIPO IDENTIFICACION: No debe tener más de 1 caracter. Los valores permitidos son: C y P.");
                }

                if (personaTemp.getIdentificacion().isEmpty()) {
                    personaTemp.agregarError("IDENTIFICACION: No existe un valor.");
                } else if (personaTemp.getTipoIdentificacion() != null && personaTemp.getTipoIdentificacion().equals("C")) {
                    ValidadorCedula validadorCedula = new ValidadorCedula();
                    try {
                        validadorCedula.validate(null, null, personaTemp.getIdentificacion());
                        if (!errorWebServiceRegistroCivil) {
                            try {
                                Persona p = conexionRegistroCivilBean.consultarPersona(personaTemp.getIdentificacion());
                                if (p != null) {
                                    personaTemp.setNombresCompletos(p.getNombresCompletos());
                                    personaTemp.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "EC")).format(p.getFechaNacimiento()));
                                    personaTemp.setGenero(p.getGenero().toString());
                                }

                            } catch (Exception e) {
                                errorWebServiceRegistroCivil = true;
                            }

                        }

                    } catch (ValidatorException e) {
                        personaTemp.agregarError("CEDULA/PASAPORTE: " + e.getMessage());
                    }
                } else {
                    ValidadorNumero validadorNumero = new ValidadorNumero();
                    try {
                        validadorNumero.validate(null, null, personaTemp.getIdentificacion());
                    } catch (ValidatorException e) {
                        personaTemp.agregarError("CEDULA/PASAPORTE: Debe tener solamente números.");
                    }
                }
                if (!personaTemp.getIdentificacion().isEmpty()) {
                    if (personaTempList.stream().anyMatch(p -> p.getIdentificacion().equals(personaTemp.getIdentificacion()))) {
                        personaTemp.agregarError("CEDULA/PASAPORTE: Existe otra persona en el listado con esta identificación.");
                    }
                }

                if (personaTemp.getFechaNacimiento().isEmpty()) {
                    personaTemp.agregarError("FECHA NACIMIENTO: No existe un valor.");
                } else {
                    try {
                        StringTokenizer tokens = new StringTokenizer(personaTemp.getFechaNacimiento(), "/");
                        if (tokens.countTokens() != 3) {
                            throw new Exception();
                        }
                        int dia = Integer.parseInt(tokens.nextToken());
                        int mes = Integer.parseInt(tokens.nextToken());
                        int anio = Integer.parseInt(tokens.nextToken());
                        LocalDate fecha = LocalDate.of(anio, mes, dia);
                    } catch (Exception e) {
                        personaTemp.agregarError("FECHA NACIMIENTO: Fecha incorrecta, debe ser en formato DD/MM/AAAA.");
                    }
                }

                if (personaTemp.getGenero().isEmpty()) {
                    personaTemp.agregarError("GENERO: No existe un valor.");
                } else if (personaTemp.getGenero().length() > 1 || (!personaTemp.getGenero().equals("F") && !personaTemp.getGenero().equals("M"))) {
                    personaTemp.agregarError("GENERO: No debe tener más de 1 caracter. Los valores permitidos son: F y M.");
                }

                if (personaTemp.getEmail().isEmpty()) {
                    personaTemp.agregarError("EMAIL: No existe un valor.");
                } else {
                    ValidadorEmail validadorEmail = new ValidadorEmail();
                    try {
                        validadorEmail.validate(null, null, personaTemp.getEmail());
                    } catch (ValidatorException e) {
                        personaTemp.agregarError("EMAIL: " + e.getMessage());
                    }
                }

                if (personaTemp.getTelefono().isEmpty()) {
                    personaTemp.agregarError("TELEFONO CONVENCIONAL: No existe un valor.");
                } else if (personaTemp.getTelefono().length() > 9) {
                    personaTemp.agregarError("TELEFONO CONVENCIONAL: No debe tener más de 9 dígitos.");
                } else {
                    ValidadorNumero validadorNumero = new ValidadorNumero();
                    try {
                        validadorNumero.validate(null, null, personaTemp.getTelefono());
                    } catch (ValidatorException e) {
                        personaTemp.agregarError("TELEFONO CONVENCIONAL: Debe tener solamente números");
                    }
                }

                if (personaTemp.getCelular().isEmpty()) {
                    personaTemp.agregarError("TELEFONO CELULAR: No existe un valor.");
                } else if (personaTemp.getCelular().length() > 10) {
                    personaTemp.agregarError("TELEFONO CELULAR: No debe tener más de 10 dígitos.");
                } else {
                    ValidadorNumero validadorNumero = new ValidadorNumero();
                    try {
                        validadorNumero.validate(null, null, personaTemp.getCelular());
                    } catch (ValidatorException e) {
                        personaTemp.agregarError("TELEFONO CELULAR: Debe tener solamente números");
                    }
                }

                if (personaTemp.getDiscapacidad().isEmpty()) {
                    personaTemp.agregarError("DISCAPACIDAD: No existe un valor.");
                } else if (personaTemp.getDiscapacidad().length() > 1 || (!personaTemp.getDiscapacidad().equals("S") && !personaTemp.getDiscapacidad().equals("N"))) {
                    personaTemp.agregarError("DISCAPACIDAD: No debe tener más de 1 caracter. Los valores permitidos son: S y N.");
                }

                if (personaTemp.getTipoDiscapacidad().isEmpty() && personaTemp.getDiscapacidad().equals("S")) {
                    personaTemp.agregarError("TIPO DISCAPACIDAD: No existe un valor.");
                } else if (personaTemp.getTipoDiscapacidad().length() > 100) {
                    personaTemp.agregarError("APELLIDOS. No debe tener más de 100 caracteres.");
                }

                if (personaTemp.getError() != null && !personaTemp.getError().isEmpty()) {
                    errorCargaMasiva = true;
                }
                personaTempList.add(personaTemp);
            }

            if (errorCargaMasiva) {
                FacesUtils.addErrorMessage("Existen errores en los registros. Por favor corrígalos y vuelva a subir el archivo.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarEstudiantesCargaMasiva() {
        if (errorCargaMasiva) {
            String msg = "Existen errores en los registros. Por favor corrígalos y vuelva a subir el archivo.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
            return;
        }
        if (personaTempList == null || personaTempList.isEmpty()) {
            FacesUtils.addErrorMessage("Debe cargar el archivo Excel con el listado de estudiantes a inscribir.");
            return;
        }

        for (PersonaTemp p : personaTempList) {
            if (cursoCentroCapacitacion.getInscripcionEstudianteList()
                    .stream()
                    .anyMatch(i -> i.getEstudiante().getPersona().getIdentificacion().equals(p.getIdentificacion()))) {
                FacesUtils.addErrorMessage("En el listado de estudiantes de este curso ya existe una persona con la Identificación: " + p.getIdentificacion());
                return;
            }
            Persona persona = null;
            Usuario usuario = usuarioFacadeLocal.findByIdentificacion(p.getIdentificacion());
            if (usuario != null) {
                persona = usuario.getPersona();
            } else {
                persona = new Persona();
                usuario = new Usuario();
                usuario.setPersona(persona);
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String passwordInicial = Utils.generarPassword();
                usuario.setClave(passwordEncoder.encode(passwordInicial));
                usuario.setClaveExpirada('S');
            }
            if (!usuario.verificarRolEstudiante()) {
                usuario.agregarRol(new Rol(Rol.ROL_ESTUDIANTE));
            }

            if (p.getTipoIdentificacion().equals("C")) {
                persona.setTipoIdentificacion(new TipoIdentificacion(TipoIdentificacion.ID_TIPO_CEDULA));
            } else {
                persona.setTipoIdentificacion(new TipoIdentificacion(TipoIdentificacion.ID_TIPO_PASAPORTE));
            }
            persona.setIdentificacion(p.getIdentificacion());
            /*persona.setApellidos(p.getApellidos());
            persona.setNombres(p.getNombres());*/
            persona.setNombresCompletos(p.getNombresCompletos());

            StringTokenizer tokens = new StringTokenizer(p.getFechaNacimiento(), "/");
            int dia = Integer.parseInt(tokens.nextToken());
            int mes = Integer.parseInt(tokens.nextToken());
            int anio = Integer.parseInt(tokens.nextToken());
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            persona.setFechaNacimiento(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            persona.setGenero(p.getGenero().charAt(0));
            persona.setEmail(p.getEmail());
            persona.setTelefono(p.getTelefono());
            persona.setCelular(p.getCelular());
            persona.setTieneDiscapacidad(p.getDiscapacidad().charAt(0));
            persona.setTipoDiscapacidad(p.getTipoDiscapacidad());

            Inscripcion inscripcionEstudiante = new Inscripcion();
            inscripcionEstudiante.setTipoEstudiante(new TipoEstudiante(TipoEstudiante.ID_CORPORATIVO));
            inscripcionEstudiante.setCursoCentroCapacitacion(cursoCentroCapacitacion);
            inscripcionEstudiante.setEstudiante(usuario);
            inscripcionEstudiante.setCreadoPor(menuBarBean.getUsuario());
            inscripcionEstudiante.setTipo(Inscripcion.TIPO_ALUMNO);
            inscripcionEstudiante.setTieneDiscapacidad(persona.getTieneDiscapacidad());
            inscripcionEstudiante.setTipoDiscapacidad(persona.getTipoDiscapacidad());

            /*if (usuario.getId()==null){
                usuarioFacadeLocal.create(usuario);
            }else{
                personaFacadeLocal.edit(persona);
                usuarioFacadeLocal.edit(usuario);
            }
            inscripcionFacadeLocal.create(inscripcion);
            agregarAlumno=false;
            inscripcion = inscripcionFacadeLocal.find(inscripcion.getId());
             */
            cursoCentroCapacitacion.agregarInscripcion(inscripcionEstudiante);
            agregarAlumno = false;
        }

    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public boolean isAgregarAlumno() {
        return agregarAlumno;
    }

    public void setAgregarAlumno(boolean agregarAlumno) {
        this.agregarAlumno = agregarAlumno;
    }

    public List<PersonaTemp> getPersonaTempList() {
        return personaTempList;
    }

    public void setPersonaTempList(List<PersonaTemp> personaTempList) {
        this.personaTempList = personaTempList;
    }

    public CursoCentroCapacitacion getCursoCentroCapacitacion() {
        return cursoCentroCapacitacion;
    }

    public void setCursoCentroCapacitacion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        this.cursoCentroCapacitacion = cursoCentroCapacitacion;
    }

    public List<CursoCentroCapacitacion> getCursoCentroCapacitacionList() {
        return cursoCentroCapacitacionList;
    }

    public void setCursoCentroCapacitacionList(List<CursoCentroCapacitacion> cursoCentroCapacitacionList) {
        this.cursoCentroCapacitacionList = cursoCentroCapacitacionList;
    }

}
