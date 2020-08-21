package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Rol;
import ec.edu.espe_innovativa.entity_bean.TipoIdentificacion;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named
@ViewScoped
public class RegistroUsuarioBean implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    private Usuario usuario;
    @Inject
    private ConexionRegistroCivilBean conexionRegistroCivilBean;

    public RegistroUsuarioBean() {
    }

    @PostConstruct
    public void init() {
        usuario = new Usuario();
    }

    public void grabar() {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String passwordInicial = Utils.generarPassword();
            usuario.setClave(passwordEncoder.encode(passwordInicial));
            usuario.setClaveExpirada('S');
            usuario.agregarRol(new Rol(Rol.ROL_ESTUDIANTE));
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
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void buscarUsuario() {
        Usuario usuarioTemp = usuarioFacadeLocal.findByIdentificacion(usuario.getPersona().getIdentificacion());
        if (usuarioTemp != null) {
            FacesUtils.addErrorMessage("formPrincipal:txtCedula", "Ya existe un Usuario con este No. Identificación");
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
