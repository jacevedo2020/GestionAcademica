package ec.edu.espe_innovativa.schedule;

import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;

@Startup
@Singleton
public class Notificacion {

    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    @EJB
    private InscripcionFacadeLocal inscripcionFacadeLocal;

    //@Schedule(hour = "*", minute = "*", second = "0")
    private void verificarInscripciones(Timer timer) {
        List<Inscripcion> inscripcionList = inscripcionFacadeLocal.findByIniciadas();
        for (Inscripcion i : inscripcionList) {
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaInicio = i.getCursoCentroCapacitacion().getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate fechaFin = i.getCursoCentroCapacitacion().getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Period periodo = Period.between(fechaInicio, fechaFin);
            int dias = periodo.getDays();
            int intervalo;
            if (dias <= 5) {
                intervalo = 1;
            } else if (dias <= 10) {
                intervalo = 2;
            } else if (dias <= 15) {
                intervalo = 3;
            } else {
                intervalo = 5;
            }
            LocalDate fechaEnvio = fechaInicio;
            do {
                if (fechaEnvio.isEqual(fechaActual)) {
                    enviarNotificacion(i);
                    break;
                }
                fechaEnvio = fechaEnvio.plusDays(intervalo);
                if (fechaEnvio.isAfter(fechaActual)) {
                    break;
                }
            } while (true);

        }

    }

    private void enviarNotificacion(Inscripcion inscripcion) {
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("****************  ENVIANDO NOTIFICACION .....");
        System.out.println(inscripcion);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        MensajeCorreo mensajeCorreo = mensajeCorreoFacadeLocal.find(MensajeCorreo.ID_MATRICULA_PENDIENTE_APROBAR);
        String mensaje = "<table id=\"x_backgroundTable\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#F2F2F2\"><tbody><tr><td><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td width=\"100%\"><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td align=\"center\"><div class=\"x_imgpop\"><img class=\"x_banner\" style=\"display: block; border: none; outline: none; text-decoration: none;\" src=\"https://www.espe-innovativa.edu.ec/wp-content/uploads/2017/04/logo_color.png\" alt=\"\" width=\"286\" height=\"60\" border=\"0\" data-imagetype=\"External\" /></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table><table id=\"x_backgroundTable\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#F2F2F2\"><tbody><tr><td><table class=\"x_devicewidth\" border=\"0\" width=\"550\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td class=\"x_devicewidthInter\" style=\"padding: 20px;\" bgcolor=\"#f6f4f5\" width=\"100%\"><table class=\"x_devicewidthInter\" border=\"0\" width=\"510\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tbody><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 30px 20px 30px;\" align=\"left\" bgcolor=\"#ffffff\"><strong> Estimado/a: </strong>  $$DESTINATARIO$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 0px 30px 20px 30px;\" align=\"left\" bgcolor=\"#ffffff\">$$MENSAJE$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 22px; padding: 0px 40px 0px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><table class=\"x_tabla-texto\" style=\"border-collapse: collapse; border: 0px solid #f6f4f5;\" border=\"0\" width=\"440\" cellspacing=\"0\" cellpadding=\"0\" align=\"left\"><tbody><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong> Curso: </strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$CURSO$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong> Modalidad: </strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$MODALIDAD$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong>Fecha Inicio:</strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$FECHA_INICIO$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong>Fecha Fin:</strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$FECHA_FIN$$</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 3px 5px 0px;\" align=\"right\" bgcolor=\"#ffffff\" width=\"35%\"><strong>Estudiante:</strong></td><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 5px 20px 5px 3px;\" align=\"left\" bgcolor=\"#ffffff\" width=\"65%\">$$ESTUDIANTE$$</td></tr></tbody></table></td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><br /> (El envío de este correo es automático, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><br /> Atentamente,</td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: justify; line-height: 22px; padding: 5px 40px 5px 40px;\" align=\"left\" bgcolor=\"#ffffff\"><strong> ESPE-Innovativa EP </strong></td></tr><tr><td class=\"x_txt\" style=\"font-family: Arial,sans-serif; color: #333333; font-size: 12px; text-align: left; line-height: 16px; padding: 20px 30px 40px 30px;\" bgcolor=\"#ffffff\"><a style=\"font-family: 'Google Sans',Roboto,RobotoDraft,Helvetica,Arial,sans-serif; line-height: 16px; color: #ffffff; font-weight: 400; text-decoration: none; font-size: 14px; display: inline-block; padding: 10px 24px; background-color: #4184f3; border-radius: 5px; min-width: 90px;\" href=\"https://www.espe-innovativa.edu.ec/\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\"> Ingresar al Sistema </a></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>";
        mensaje = mensaje.replace("$$DESTINATARIO", inscripcion.getEstudiante().getPersona().getNombresCompletos());
        mensaje = mensaje.replace("$$MENSAJE$$", mensajeCorreo.getMensaje());
        mensaje = mensaje.replace("$$CURSO$$", inscripcion.getCursoCentroCapacitacion().getCurso().getNombre());
        mensaje = mensaje.replace("$$MODALIDAD$$", inscripcion.getCursoCentroCapacitacion().getModalidadDescripcion());
        mensaje = mensaje.replace("$$FECHA_INICIO$$", df.format(inscripcion.getCursoCentroCapacitacion().getFechaInicio()));
        mensaje = mensaje.replace("$$FECHA_FIN$$", df.format(inscripcion.getCursoCentroCapacitacion().getFechaFin()));
        mensaje = mensaje.replace("$$ESTUDIANTE$$", inscripcion.getEstudiante().getPersona().getNombresCompletosMasCedula());

        //Enviar mail al estudiante
        try {
            //ServicioEnvioMail servicioEnvioMail = new ServicioEnvioMail_Service().getServicioEnvioMailPort();
            //servicioEnvioMail.enviarMail(inscripcion.getEstudiante().getPersona().getEmail(), mensajeCorreo.getAsunto(), mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Enviar mail al analista
        Persona destinatario = null;
        if (inscripcion.getInscripcionPadre() != null) {
            if (inscripcion.getInscripcionPadre().isInscripcionEmpresa()) {
                destinatario = inscripcion.getInscripcionPadre().getCursoCentroCapacitacion().getResponsable().getPersona();
            }
        }
        if (destinatario == null) {
            inscripcion.getCursoCentroCapacitacion().getCurso().getProyecto().getResponsable().getPersona();
        }
        mensaje = mensaje.replace("$$DESTINATARIO", destinatario.getNombresCompletos());
        try {
            //ServicioEnvioMail servicioEnvioMail = new ServicioEnvioMail_Service().getServicioEnvioMailPort();
            //servicioEnvioMail.enviarMail(destinatario.getEmail(), mensajeCorreo.getAsunto(), mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
