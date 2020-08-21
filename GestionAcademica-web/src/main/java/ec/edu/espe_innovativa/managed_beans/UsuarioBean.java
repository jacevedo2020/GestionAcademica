package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.RolFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named(value = "usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private RolFacadeLocal rolFacadeLocal;
    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    private Usuario usuario;
    private Usuario usuarioCambioPassword;
    private List<Usuario> usuarioList;
    private List<Rol> rolList;
    private List<Rol> rolSeleccionList;
    private String password;

    @Inject
    private ConexionRegistroCivilBean conexionRegistroCivilBean;

    public UsuarioBean() {
    }

    @PostConstruct
    private void init() {
        rolList = rolFacadeLocal.findAll();
        initUsuarios();
    }

    private void initUsuarios() {
        usuario = null;
        usuarioList = usuarioFacadeLocal.findAll();
    }

    public void nuevo() {
        usuario = new Usuario();
    }

    public void cancelar() {
        initUsuarios();
    }

    public void grabar() {
        try {
            if (usuario.getId() == null) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String passwordInicial = Utils.generarPassword();
                usuario.setClave(passwordEncoder.encode(passwordInicial));
                usuario.setClaveExpirada('S');
                usuarioFacadeLocal.create(usuario);
                MensajeCorreo mensajeCorreo = mensajeCorreoFacadeLocal.find(MensajeCorreo.ID_CREACION_USUARIO);

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
            } else {
                usuarioFacadeLocal.edit(usuario);
            }
            initUsuarios();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(Usuario usuario) {
        this.usuario = usuario;
    }

    public void eliminar(Usuario usuario) {
        try {
            usuarioFacadeLocal.remove(usuario);
            initUsuarios();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public void buscarUsuario() {
        Usuario usuarioTemp = usuarioFacadeLocal.findByIdentificacion(usuario.getPersona().getIdentificacion());
        if (usuarioTemp != null) {
            FacesUtils.addErrorMessage("formPrincipal:txtIdentificacion", "Ya existe un Usuario con este No. Identificación");
            return;
        }
        if (usuario.getPersona().getTipoIdentificacion().getId() == TipoIdentificacion.ID_TIPO_CEDULA) {
            try {
                Persona p = conexionRegistroCivilBean.consultarPersona(usuario.getPersona().getIdentificacion());
                if (p != null) {
                    usuario.setPersona(p);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void nuevoRol() {
        rolSeleccionList = rolList.stream().filter(o -> !usuario.verificarRolAsignado(o.getId())).collect(Collectors.toList());
        rolSeleccionList.forEach(o -> o.setSeleccionado(false));
        PrimeFaces.current().executeScript("PF('dlgRoles').show();");
    }

    public void agregarRoles() {
        usuario.agregarRoles(rolSeleccionList.stream().filter(o -> o.isSeleccionado()).collect(Collectors.toList()));
        PrimeFaces.current().executeScript("PF('dlgRoles').hide();");

    }

    public void seleccionarCambiarPassword(Usuario usuario) {
        this.usuarioCambioPassword = usuario;
        password = "";
        PrimeFaces.current().executeScript("PF('dlgResetearPassword').show();");
    }

    public void cambiarContrasenia() {
        try {
            if (password.length() < 5) {
                FacesUtils.addErrorMessage("formPrincipal:formResetearPassword:pwd1", "La contraseña debe tener mínimo 5 caracteres.");
                return;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            usuarioCambioPassword.setClave(passwordEncoder.encode(this.password));
            usuarioCambioPassword.setClaveExpirada('S');
            usuarioFacadeLocal.edit(usuarioCambioPassword);
            PrimeFaces.current().executeScript("PF('dlgResetearPassword').hide();");
            FacesUtils.addInfoMessage("Password reseteado exitosamente.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Rol> getRolSeleccionList() {
        return rolSeleccionList;
    }

    public void setRolSeleccionList(List<Rol> rolSeleccionList) {
        this.rolSeleccionList = rolSeleccionList;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public List<Usuario> getUsuarioInstructorList() {
        return usuarioList.stream().filter(u -> u.verificarRolInstructor()).collect(Collectors.toList());
    }

    public List<Usuario> getUsuarioAnalistaList() {
        return usuarioList.stream().filter(u -> u.verificarRolAnalista()).collect(Collectors.toList());
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getUsuarioCambioPassword() {
        return usuarioCambioPassword;
    }

    public void setUsuarioCambioPassword(Usuario usuarioCambioPassword) {
        this.usuarioCambioPassword = usuarioCambioPassword;
    }

}
