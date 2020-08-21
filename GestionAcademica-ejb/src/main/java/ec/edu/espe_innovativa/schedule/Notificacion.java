package ec.edu.espe_innovativa.schedule;

import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.session_bean.InscripcionFacadeLocal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;

//@Startup
//@Singleton
public class Notificacion {

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
            System.out.println("*************  DIAS: " +  dias);
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
            System.out.println("*************  INTERVALO: " +  intervalo);
            LocalDate fechaEnvio = fechaInicio;
            System.out.println("FECHA ACTUAL: " + fechaActual);
            System.out.println("FECHA ENVIO: " + fechaEnvio);
            do {
                System.out.println("FECHA ENVIO: " + fechaEnvio);
                if (fechaEnvio.isEqual(fechaActual)) {
                    System.out.println(i);
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
    
    private void enviarNotificacion(Inscripcion inscripcion){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("****************  ENVIANDO NOTIFICACION .....");
        System.out.println(inscripcion);
    }
}
