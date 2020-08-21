package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Estado;
import ec.edu.espe_innovativa.entity_bean.EstadoInscripcion;
import ec.edu.espe_innovativa.entity_bean.Evaluacion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Parroquia;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.PersonaFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import static java.io.File.separatorChar;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named(value = "inscripcionBean")
@ViewScoped
public class InscripcionBean implements Serializable {

    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;
    @EJB
    private PersonaFacadeLocal personaFacadeLocal;
    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    @Inject
    private ConexionRegistroCivilBean conexionRegistroCivilBean;
    @Inject
    private CursoCentroCapacitacionBean cursoCentroCapacitacionBean;

    private Inscripcion inscripcion;
    private Inscripcion inscripcionEstudiante;
    private List<Inscripcion> inscripcionList;
    @Inject
    private MenuBarBean menuBarBean;
    private Provincia provincia;
    private Canton canton;
    private String tipoMatricula;
    private boolean trabajaActualmente;
    private boolean aplicaCredito;
    private boolean realizaAbono;
    private boolean pagaEmpresa;
    private boolean modoEdicion;
    private String identificacionOriginal;

    public InscripcionBean() {
    }

    @PostConstruct
    public void init() {
        cancelar();
    }

    public void cancelar() {
        menuBarBean.recargarUsuario();
        cursoCentroCapacitacionBean.init();
        inscripcion = null;
        provincia = null;
        canton = null;
    }

    public void seleccionarCurso(CursoCentroCapacitacion cursoCentroCapacitacion) {
        if (!menuBarBean.getUsuario().verificarRolAdministradorCentro() && menuBarBean.getUsuario().verificarInscripcion(cursoCentroCapacitacion)) {
            String msg = "Ud. ya se ha inscrito en este curso, por favor verifique en la opción 'Ver mis Cursos'.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
            return;
        }
        tipoMatricula = "I";
        inicializarInscripcion(cursoCentroCapacitacion);
    }

    private void inicializarInscripcion(CursoCentroCapacitacion cursoCentroCapacitacion) {
        inscripcion = new Inscripcion();
        aplicaCredito = false;
        realizaAbono = true;
        pagaEmpresa = false;
        inscripcion.setCreadoPor(menuBarBean.getUsuario());
        inscripcion.setCursoCentroCapacitacion(cursoCentroCapacitacion);
        if (tipoMatricula.equals("I")) {
            inscripcion.setTipo(Inscripcion.TIPO_ALUMNO);
            if (menuBarBean.getUsuario().verificarRolEstudiante()) {
                inscripcion.setEstudiante(menuBarBean.getUsuario());
            } else {
                Usuario usr = new Usuario(new Persona());
                inscripcion.setEstudiante(usr);
            }
            llenarDatosInscripcion(inscripcion);
        } else {
            inscripcion.setTipo(Inscripcion.TIPO_ALUMNO_GRUPO);
            /*if (menuBarBean.getUsuario().verificarRolEstudiante()) {
                Inscripcion i = new Inscripcion();
                i.setEstudiante(menuBarBean.getUsuario());
                inscripcionList.add(i);
            }*/
        }
    }

    public void onTipoMatriculaChange() {
        inscripcionList = new ArrayList<>();
        inicializarInscripcion(inscripcion.getCursoCentroCapacitacion());
    }

    private void llenarDatosInscripcion(Inscripcion inscripcion) {
        Persona persona = inscripcion.getEstudiante().getPersona();
        trabajaActualmente = persona.isTrabajaActualmenteSi();
        inscripcion.setTrabajaActualmente(trabajaActualmente ? 'S' : 'N');
        inscripcion.setEmpresaTrabajo(persona.getEmpresaTrabajo());
        inscripcion.setDireccionTrabajo(persona.getDireccionTrabajo());
        inscripcion.setTelefonoTrabajo(persona.getTelefonoTrabajo());
        if (persona.getParroquia() != null) {
            provincia = persona.getParroquia().getCanton().getProvincia();
            canton = persona.getParroquia().getCanton();
        } else {
            provincia = null;
            canton = null;
        }

        if (tipoMatricula.equals("I")) {
            if (!Objects.equals(persona.getTipoIdentificacion().getId(), TipoIdentificacion.ID_TIPO_PASAPORTE)) { //si no es pasaporte
                inscripcion.setFacturaTipoIdentificacion(persona.getTipoIdentificacion());
                inscripcion.setFacturaRuc(persona.getIdentificacion());
            }
            inscripcion.setFacturaRazonSocial(persona.getNombresCompletos());
            inscripcion.setFacturaDireccion(persona.getDireccion());
            inscripcion.setFacturaTelefono(persona.getTelefono());
            inscripcion.setFacturaEmail(persona.getEmail());
        }
    }

    private void crearUsuario(Usuario usuario) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordInicial = Utils.generarPassword();
        usuario.setClave(passwordEncoder.encode(passwordInicial));
        usuario.setClaveExpirada('S');
        usuario.agregarRol(new Rol(Rol.ROL_ESTUDIANTE));
        usuarioFacadeLocal.create(usuario);
        MensajeCorreo mensajeCorreo = mensajeCorreoFacadeLocal.find(MensajeCorreo.ID_CREACION_USUARIO);

        //String mensaje = "<table><tbody><tr><td>Estimado(a)</td><td><strong>$$NOMBRE$$</strong></td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>$$MENSAJE$$</td></tr></tbody></table><table border=\"\\&quot;\\\\&quot;1\\\\&quot;\\&quot;\"><tbody><tr><td>Usuario:</td><td>$$USUARIO$$</td></tr><tr><td>Password:</td><td>$$PASSWORD$$</td></tr><tr><td>Url del Sistema:</td><td><table border=\"\\&quot;\\\\&quot;1\\\\&quot;\\&quot;\"><tbody><tr><td><a href=\"$$URL$$\">$$URL$$</a></td></tr></tbody></table></td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>(El env&iacute;o de este correo es autom&aacute;tico, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>Atentamente,</td></tr><tr><td><strong>ESPE-Innovativa EP</strong></td></tr></tbody></table>";
        //String mensaje = "<table><tbody><tr><td>Estimado(a)</td><td><strong>$$NOMBRE$$</strong></td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>$$MENSAJE$$</td></tr></tbody></table><table border=\\\"\\\\&quot;\\\\\\\\&quot;1\\\\\\\\&quot;\\\\&quot;\\\"><tbody><tr><td>Usuario:</td><td>$$USUARIO$$</td></tr><tr><td>Password:</td><td>$$PASSWORD$$</td></tr><tr><td>Url del Sistema:</td><td><table border=\\\"\\\\&quot;\\\\\\\\&quot;1\\\\\\\\&quot;\\\\&quot;\\\"><tbody><tr><td><a href=\"https://accounts.google.com/AccountChooser?Email=josemacevedom@gmail.com&amp;continue=https://myaccount.google.com/alert/nt/1591883105927?rfn%3D28%26rfnc%3D1%26eid%3D-868961007211794567%26et%3D1\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif;line-height:16px;color:#ffffff;font-weight:400;text-decoration:none;font-size:14px;display:inline-block;padding:10px 24px;background-color:#4184F3;border-radius:5px;min-width:90px;\">Ver actividad</a></td></tr></tbody></table></td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>(El env&iacute;o de este correo es autom&aacute;tico, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr></tbody></table><p>&nbsp;</p><table><tbody><tr><td>Atentamente,</td></tr><tr><td><strong>ESPE-Innovativa EP</strong></td></tr></tbody></table>";
        String mensaje = "<table width=\"100%\" bgcolor=\"#F2F2F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"x_backgroundTable\"><tbody><tr><td><table width=\"550\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"x_devicewidth\"><tbody><tr><td width=\"100%\"><table width=\"550\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"x_devicewidth\"><tbody><tr><td align=\"center\"><div class=\"x_imgpop\"><img data-imagetype=\"External\" src=\"https://www.espe-innovativa.edu.ec/wp-content/uploads/2017/04/logo_color.png\" width=\"286\" border=\"0\" height=\"60\" alt=\"\" class=\"x_banner\" style=\"display:block; border:none; outline:none; text-decoration:none\"></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table><table width=\"100%\" bgcolor=\"#F2F2F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"x_backgroundTable\"><tbody><tr><td><table width=\"550\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"x_devicewidth\"><tbody><tr><td width=\"100%\" bgcolor=\"#f6f4f5\" class=\"x_devicewidthInter\" style=\"padding:20px\"><table width=\"510\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"x_devicewidthInter\"><tbody><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:30px; padding-left:30px; padding-bottom:20px; text-align:left; line-height:16px\"><strong>Estimado/a:</strong>&nbsp;$$NOMBRE$$</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:0px; padding-right:30px; padding-left:30px; padding-bottom:20px; text-align:left; line-height:16px\">$$MENSAJE$$</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:0px; padding-right:40px; padding-left:40px; padding-bottom:0px; text-align:left; line-height:22px\"><table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"x_tabla-texto\" width=\"440\" style=\"border-collapse:collapse; border:0px solid #f6f4f5\"><tbody><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Usuario:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$USUARIO$$</td></tr><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Password:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$PASSWORD$$</td></tr></tbody></table></td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><br>(El envío de este correo es automático, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><br>Atentamente,</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><strong>ESPE-Innovativa EP</strong></td></tr><tr><td bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:30px; padding-left:30px; padding-bottom:40px; text-align:left; line-height:16px\"><a href=\"https://www.espe-innovativa.edu.ec/\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" style=\"font-family:'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif; line-height:16px; color:#ffffff; font-weight:400; text-decoration:none; font-size:14px; display:inline-block; padding:10px 24px; background-color:#4184F3; border-radius:5px; min-width:90px\">Ingresar al Sistema</a></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>";
        mensaje = mensaje.replace("$$NOMBRE$$", usuario.getPersona().getNombresCompletos());
        mensaje = mensaje.replace("$$MENSAJE$$", mensajeCorreo.getMensaje());
        mensaje = mensaje.replace("$$USUARIO$$", usuario.getPersona().getIdentificacion());
        mensaje = mensaje.replace("$$PASSWORD$$", passwordInicial);
        mensaje = mensaje.replace("$$URL$$", ParametrosGenerales.URL_APLICACION);

        try {
            ServicioEnvioMail servicioEnvioMail = new ServicioEnvioMail_Service().getServicioEnvioMailPort();
            servicioEnvioMail.enviarMail(usuario.getPersona().getEmail(), mensajeCorreo.getAsunto(), mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void grabar() {
        if (tipoMatricula.equals("I")) {
            grabarInscripcionIndividual();
        } else {
            grabarInscripcionGrupal();
        }
    }

    public void grabarInscripcionIndividual() {
        try {
            if (inscripcion.getCursoCentroCapacitacion().verificarMatriculaEstudiante(inscripcion.getEstudiante().getPersona().getIdentificacion())) {
                FacesUtils.addErrorMessage("Esta persona ya se encuentra matriculado en este curso.");
                return;
            }
            grabarInscripcion(inscripcion);
            cancelar();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void grabarInscripcion(Inscripcion inscripcion) {
        if (inscripcion.getEstudiante() != null) {
            Persona persona = inscripcion.getEstudiante().getPersona();

            Usuario usr = inscripcion.getEstudiante();
            persona.setTrabajaActualmente(inscripcion.getTrabajaActualmente());
            persona.setEmpresaTrabajo(inscripcion.getEmpresaTrabajo());
            persona.setDireccionTrabajo(inscripcion.getDireccionTrabajo());
            persona.setTelefonoTrabajo(inscripcion.getTelefonoTrabajo());

            if (usr.getId() == null) {
                crearUsuario(usr);
            } else {
                if (!usr.verificarRolEstudiante()) {
                    usr.agregarRol(new Rol(Rol.ROL_ESTUDIANTE));
                    usuarioFacadeLocal.edit(usr);
                }
                personaFacadeLocal.edit(persona);
            }

        }

        EstadoInscripcion estadoInscripcion = new EstadoInscripcion();
        estadoInscripcion.setEstado(new Estado(Estado.ID_ESTADO_MATRICULA_REGISTRADA));
        estadoInscripcion.setUsuario(menuBarBean.getUsuario());
        inscripcion.agregarEstado(estadoInscripcion);

        inscripcionFacadeLocal.create(inscripcion);

        if (inscripcion.isFormaPagoEfectivo()) {
            estadoInscripcion = new EstadoInscripcion();
            estadoInscripcion.setEstado(new Estado(Estado.ID_ESTADO_MATRICULA_APROBADA));
            estadoInscripcion.setUsuario(menuBarBean.getUsuario());
            inscripcion.agregarEstado(estadoInscripcion);

            inscripcion.setDocumentoPagoNombre(null);
            inscripcion.setDocumentoPagoUrl(null);
            inscripcionFacadeLocal.edit(inscripcion);
        }
    }

    public void grabarInscripcionGrupal() {
        try {
            if (inscripcionList.size() < 2) {
                FacesUtils.addErrorMessage("Debe agregar al menos 2 estudiantes.");
                return;
            }
            grabarInscripcion(inscripcion);
            if (inscripcion.getDocumentoPagoNombre() != null) {
                String pathAbsolutoDestino = ParametrosGenerales.getDirectorioAbsolutoInscripciones() + separatorChar + inscripcion.getId();
                if (!Files.isDirectory(Paths.get(pathAbsolutoDestino))) {
                    Files.createDirectories(Paths.get(pathAbsolutoDestino));
                }
                pathAbsolutoDestino = pathAbsolutoDestino + separatorChar + inscripcion.getDocumentoPagoNombre();

                String pathRelativoOrigen = menuBarBean.getUsuario().getId().toString();
                String pathAbsolutoOrigen = ParametrosGenerales.getDirectorioAbsolutoTemp() + separatorChar + pathRelativoOrigen + separatorChar + inscripcion.getDocumentoPagoNombre();
                Files.copy(Paths.get(pathAbsolutoOrigen), Paths.get(pathAbsolutoDestino), StandardCopyOption.REPLACE_EXISTING);
                inscripcion.setDocumentoPagoUrl(ParametrosGenerales.URL_INSCRIPCIONES + "/" + inscripcion.getId());
                inscripcionFacadeLocal.edit(inscripcion);
            }
            for (Inscripcion inscripcion : inscripcionList) {
                inscripcion.setInscripcionPadre(this.inscripcion);
                inscripcion.setCursoCentroCapacitacion(this.inscripcion.getCursoCentroCapacitacion());
                inscripcion.setCreadoPor(this.inscripcion.getCreadoPor());
                inscripcion.setFormaPago(this.inscripcion.getFormaPago());
                inscripcion.setCredito(this.inscripcion.getCredito());
                inscripcion.setAbono(this.inscripcion.getAbono());
                inscripcion.setPagaEmpresa(this.inscripcion.getPagaEmpresa());
                inscripcion.setDocumentoPagoNombre(this.inscripcion.getDocumentoPagoNombre());
                inscripcion.setDocumentoPagoUrl(this.inscripcion.getDocumentoPagoUrl());
                inscripcion.setFacturaRuc(this.inscripcion.getFacturaRuc());
                inscripcion.setFacturaRazonSocial(this.inscripcion.getFacturaRazonSocial());
                inscripcion.setFacturaDireccion(this.inscripcion.getFacturaDireccion());
                inscripcion.setFacturaEmail(this.inscripcion.getFacturaEmail());
                inscripcion.setFacturaTelefono(this.inscripcion.getFacturaTelefono());
                inscripcion.setFacturaTipoIdentificacion(this.inscripcion.getFacturaTipoIdentificacion());

                inscripcion.setDocumentoPagoNombre(this.inscripcion.getDocumentoPagoNombre());
                inscripcion.setDocumentoPagoUrl(this.inscripcion.getDocumentoPagoUrl());
                grabarInscripcion(inscripcion);

                /*pathAbsolutoDestino = ParametrosGenerales.getDirectorioAbsolutoInscripciones() + separatorChar + inscripcion.getId();
                if (!Files.isDirectory(Paths.get(pathAbsolutoDestino))) {
                    Files.createDirectories(Paths.get(pathAbsolutoDestino));
                }
                pathAbsolutoDestino = pathAbsolutoDestino + separatorChar + inscripcion.getDocumentoPagoNombre();

                Files.copy(Paths.get(pathAbsolutoOrigen), Paths.get(pathAbsolutoDestino), StandardCopyOption.REPLACE_EXISTING);
                inscripcion.setDocumentoPagoNombre(this.inscripcion.getDocumentoPagoNombre());
                inscripcion.setDocumentoPagoUrl(ParametrosGenerales.URL_INSCRIPCIONES + "/" + inscripcion.getId());
                 */
                inscripcionFacadeLocal.edit(inscripcion);
            }
            cancelar();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void buscarPersona() {
        buscarPersona(inscripcion);
    }

    public void buscarPersonaGrupo() {
        buscarPersona(inscripcionEstudiante);
    }

    private void buscarPersona(Inscripcion inscripcion) {
        Persona personaInscripcion = inscripcion.getEstudiante().getPersona();
        if (inscripcion.getCursoCentroCapacitacion().verificarMatriculaEstudiante(inscripcion.getEstudiante().getPersona().getIdentificacion())) {
            FacesUtils.addErrorMessage("Esta persona ya se encuentra matriculado en este curso.");
        }

        Usuario usr = usuarioFacadeLocal.findByIdentificacion(personaInscripcion.getIdentificacion());
        if (usr == null) {
            usr = new Usuario();
            Persona p = new Persona();
            p.setTipoIdentificacion(personaInscripcion.getTipoIdentificacion());
            p.setIdentificacion(personaInscripcion.getIdentificacion());
            usr.setPersona(p);
            if (personaInscripcion.getTipoIdentificacion().getId() == TipoIdentificacion.ID_TIPO_CEDULA) {
                try {
                    p = conexionRegistroCivilBean.consultarPersona(personaInscripcion.getIdentificacion());
                    if (p != null) {
                        usr.setPersona(p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        inscripcion.setEstudiante(usr);
        llenarDatosInscripcion(inscripcion);
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
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

    public Date getFechaActual() {
        return new Date();
    }

    private StreamedContent archivoAdjunto;

    public StreamedContent getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(StreamedContent archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public void subirDocumentoPago(FileUploadEvent event) {
        try {
            String pathRelativo = menuBarBean.getUsuario().getId().toString();
            String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoTemp() + separatorChar + pathRelativo;
            if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
                Files.createDirectories(Paths.get(pathAbsoluto));
            }
            String nombreDocumento = Utils.depurarNombreDocumento(event.getFile().getFileName());
            pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;
            Files.copy(event.getFile().getInputstream(), Paths.get(pathAbsoluto), StandardCopyOption.REPLACE_EXISTING);
            inscripcion.setDocumentoPagoNombre(nombreDocumento);
            inscripcion.setDocumentoPagoUrl(ParametrosGenerales.URL_TEMP + "/" + pathRelativo);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
    }

    public void onCreditoChange() {
    }

    public void onAbonoChange() {
    }

    public String getTipoMatricula() {
        return tipoMatricula;
    }

    public void setTipoMatricula(String tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }

    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public Inscripcion getInscripcionEstudiante() {
        return inscripcionEstudiante;
    }

    public void setInscripcionEstudiante(Inscripcion inscripcionEstudiante) {
        this.inscripcionEstudiante = inscripcionEstudiante;
    }

    public void abrirEstudianteGrupo() {
        trabajaActualmente = false;
        modoEdicion = false;
        provincia = null;
        canton = null;
        inscripcionEstudiante = new Inscripcion();
        inscripcionEstudiante.setCursoCentroCapacitacion(inscripcion.getCursoCentroCapacitacion());
        inscripcionEstudiante.setTrabajaActualmente('N');
        Usuario usuario = new Usuario();
        inscripcionEstudiante.setEstudiante(usuario);
        inscripcionEstudiante.setTipo(Inscripcion.TIPO_ALUMNO);
    }

    public void agregarEstudianteGrupo() {
        if (!modoEdicion) {
            if (inscripcion.getCursoCentroCapacitacion().verificarMatriculaEstudiante(inscripcionEstudiante.getEstudiante().getPersona().getIdentificacion())) {
                FacesUtils.addErrorMessage("Esta persona ya se encuentra matriculado en este curso.");
                return;
            }
            for (Inscripcion i : inscripcionList) {
                if (i.getEstudiante().getPersona().getIdentificacion().equals(inscripcionEstudiante.getEstudiante().getPersona().getIdentificacion())) {
                    FacesUtils.addErrorMessage("Esta persona ya ha sido agregado al listado.");
                    return;
                }
            }
            inscripcionList.add(inscripcionEstudiante);
        } else {
            int pos = -1;
            for (int i = 0; i < inscripcionList.size(); i++) {
                if (inscripcionList.get(i).getEstudiante().getPersona().getIdentificacion().equals(identificacionOriginal)) {
                    pos = i;
                    break;
                }
            }
            inscripcionList.set(pos, inscripcionEstudiante);
        }
        PrimeFaces.current().executeScript("PF('dlgEstudianteGrupo').hide();");
    }

    public void eliminarEstudianteGrupo(Inscripcion inscripcion) {
        inscripcionList.remove(inscripcion);
    }

    public void seleccionarEstudianteGrupo(Inscripcion inscripcion) {
        try {
            inscripcionEstudiante = (Inscripcion) inscripcion.clone();
            identificacionOriginal = inscripcionEstudiante.getEstudiante().getPersona().getIdentificacion();
            Persona persona = inscripcionEstudiante.getEstudiante().getPersona();
            trabajaActualmente = persona.isTrabajaActualmenteSi();
            if (persona.getParroquia() != null) {
                provincia = persona.getParroquia().getCanton().getProvincia();
                canton = persona.getParroquia().getCanton();
            } else {
                provincia = null;
                canton = null;
            }
            modoEdicion = true;
            PrimeFaces.current().executeScript("PF('dlgEstudianteGrupo').show();");
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Error al seleccionar estudiante");
        }
    }

    public boolean isTrabajaActualmente() {
        return trabajaActualmente;
    }

    public void setTrabajaActualmente(boolean trabajaActualmente) {
        this.trabajaActualmente = trabajaActualmente;
    }

    public boolean isAplicaCredito() {
        return aplicaCredito;
    }

    public void setAplicaCredito(boolean aplicaCredito) {
        this.aplicaCredito = aplicaCredito;
    }

    public boolean isRealizaAbono() {
        return realizaAbono;
    }

    public void setRealizaAbono(boolean realizaAbono) {
        this.realizaAbono = realizaAbono;
    }

    public boolean isPagaEmpresa() {
        return pagaEmpresa;
    }

    public void setPagaEmpresa(boolean pagaEmpresa) {
        this.pagaEmpresa = pagaEmpresa;
    }

    public void onTrabajaActualmenteChange() {
        if (trabajaActualmente) {
            inscripcion.setTrabajaActualmente('S');
            inscripcion.setDireccionTrabajo(inscripcion.getEstudiante().getPersona().getDireccionTrabajo());
            inscripcion.setTelefonoTrabajo(inscripcion.getEstudiante().getPersona().getTelefonoTrabajo());
            inscripcion.setEmpresaTrabajo(inscripcion.getEstudiante().getPersona().getEmpresaTrabajo());
        } else {
            inscripcion.setTrabajaActualmente('N');
            inscripcion.setDireccionTrabajo(null);
            inscripcion.setTelefonoTrabajo(null);
            inscripcion.setEmpresaTrabajo(null);
        }
    }

    public void onTrabajaActualmenteGrupoChange() {
        if (trabajaActualmente) {
            inscripcionEstudiante.setTrabajaActualmente('S');
            inscripcionEstudiante.setDireccionTrabajo(inscripcionEstudiante.getEstudiante().getPersona().getDireccionTrabajo());
            inscripcionEstudiante.setTelefonoTrabajo(inscripcionEstudiante.getEstudiante().getPersona().getTelefonoTrabajo());
            inscripcionEstudiante.setEmpresaTrabajo(inscripcionEstudiante.getEstudiante().getPersona().getEmpresaTrabajo());
        } else {
            inscripcionEstudiante.setTrabajaActualmente('N');
            inscripcionEstudiante.setDireccionTrabajo(null);
            inscripcionEstudiante.setTelefonoTrabajo(null);
            inscripcionEstudiante.setEmpresaTrabajo(null);
        }
    }

    public void onAplicaCreditoChange() {
        inscripcion.setAbono(Inscripcion.ABONO_SI);
        realizaAbono = true;
        inscripcion.setPagaEmpresa(Inscripcion.PAGA_EMPRESA_NO);
        pagaEmpresa = false;
        inscripcion.setFormaPago(null);
        inscripcion.setDocumentoPagoNombre(null);
        inscripcion.setDocumentoPagoUrl(null);
        if (aplicaCredito) {
            inscripcion.setCredito('S');
        } else {
            inscripcion.setCredito('N');
        }
    }

    public void onRealizaAbonoChange() {
        inscripcion.setFormaPago(null);
        inscripcion.setDocumentoPagoNombre(null);
        inscripcion.setDocumentoPagoUrl(null);
        if (realizaAbono) {
            inscripcion.setAbono('S');
        } else {
            inscripcion.setAbono('N');
        }
        pagaEmpresa = false;
        inscripcion.setPagaEmpresa('N');
    }

    public void onPagaEmpresaChange() {
        if (pagaEmpresa) {
            inscripcion.setPagaEmpresa('S');
        } else {
            inscripcion.setPagaEmpresa('N');
        }
    }

}
