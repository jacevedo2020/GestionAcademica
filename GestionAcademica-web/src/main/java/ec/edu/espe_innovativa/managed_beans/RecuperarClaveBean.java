package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Usuario;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.UsuarioFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Named
@ViewScoped
public class RecuperarClaveBean implements Serializable {

    @EJB
    private UsuarioFacadeLocal usuarioFacadeLocal;
    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    private String cedula;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Error: Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    private String mail;

    public RecuperarClaveBean() {
    }

    @PostConstruct
    public void init() {

    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    private boolean claveEnviada;

    public boolean isClaveEnviada() {
        return claveEnviada;
    }

    public void setClaveEnviada(boolean claveEnviada) {
        this.claveEnviada = claveEnviada;
    }

    public void recuperarClave() {
        try {
            Usuario usu = usuarioFacadeLocal.findByIdentificacionEmail(cedula.trim(), mail.trim());
            if (usu == null) {
                FacesUtils.addErrorMessage("No existe un usuario registrado con esta identificación y correo electrónico.");
                return;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = Utils.generarPassword();
            usu.setClave(passwordEncoder.encode(password));
            usu.setClaveExpirada('S');
            usuarioFacadeLocal.edit(usu);
            MensajeCorreo mensajeCorreo = mensajeCorreoFacadeLocal.find(MensajeCorreo.ID_RECUPERACION_CLAVE);

            String mensaje = mensaje = "<table id=\"x_backgroundTable\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#F2F2F2\"><tbody><tr><td><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td width=\"100%\"><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td align=\"center\"><div class=\"x_imgpop\"><img class=\"x_banner\" style=\"display: block; border: none; outline: none; text-decoration: none;\" src=\"https://www.espe-innovativa.edu.ec/wp-content/uploads/2017/04/logo_color.png\" alt=\"\" width=\"286\" height=\"60\" border=\"0\" data-imagetype=\"External\" /></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table><table id=\"x_backgroundTable\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#F2F2F2\"><tbody><tr><td><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td class=\"x_devicewidthInter\" style=\"padding: 20px;\" bgcolor=\"#f6f4f5\" width=\"100%\"><table class=\"x_devicewidthInter\" border=\"0\" width=\"510\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 30px 20px 30px;\" align=\"left\" bgcolor=\"#ffffff\"><strong> Estimado/a: </strong>  $$NOMBRE$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 0px 30px 20px 30px;\" align=\"left\" bgcolor=\"#ffffff\">$$MENSAJE$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 22px; padding: 0px 40px 0px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><table class=\"x_tabla-texto\" style=\"border-collapse: collapse; border: 0px solid #f6f4f5;\" border=\"0\" width=\"440\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\"><tbody><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong>Usuario: </strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$USUARIO$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong> Clave: </strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$CLAVE$$</td></tr></tbody></table></td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><br /> (El envío de este correo es automático, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><br /> Atentamente,</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><strong> ESPE-Innovativa EP </strong></td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 30px 40px 30px;\" bgcolor=\"#ffffff\"><a style=\"font-family: 'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif; line-height: 16px; color: #ffffff; font-weight: 400; text-decoration: none; font-size: 14px; display: inline-block; padding: 10px 24px; background-color: #4184f3; border-radius: 5px; min-width: 90px;\" href=\"https://www.espe-innovativa.edu.ec/\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\"> Ingresar al Sistema </a></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>";

            mensaje = mensaje.replace("$$NOMBRE$$", usu.getPersona().getNombresCompletos());
            mensaje = mensaje.replace("$$MENSAJE$$", mensajeCorreo.getMensaje());
            mensaje = mensaje.replace("$$USUARIO$$", usu.getPersona().getIdentificacion());
            mensaje = mensaje.replace("$$CLAVE$$", password);
            ServicioEnvioMail servicioEnvioMail = new ServicioEnvioMail_Service().getServicioEnvioMailPort();
            servicioEnvioMail.enviarMail(usu.getPersona().getEmail(), mensajeCorreo.getAsunto(), mensaje);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proceso de recuperación de contraseña exitoso.", "La información ha sido registrada exitosamente. Pulse Aceptar para ingresar al sistema."));
            claveEnviada = true;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), e.getMessage()));
        }
    }

}
