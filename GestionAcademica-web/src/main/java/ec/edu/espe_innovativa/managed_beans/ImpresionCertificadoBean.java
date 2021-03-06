package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Certificado;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.MensajeCorreo;
import ec.edu.espe_innovativa.entity_bean.Parametro;
import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import ec.edu.espe_innovativa.session_bean.CertificadoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.MensajeCorreoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.ParametroFacadeLocal;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail;
import ec.edu.espe_innovativa.ws.ServicioEnvioMail_Service;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "impresionCertificadoBean")
@ViewScoped
public class ImpresionCertificadoBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroFacadeLocal;
    @EJB
    private ParametroFacadeLocal parametroFacadeLocal;
    @EJB
    private CertificadoFacadeLocal certificadoFacadeLocal;
    @EJB
    private MensajeCorreoFacadeLocal mensajeCorreoFacadeLocal;
    private Parametro parametroSecuencialCertificados;
    @Inject
    private MenuBarBean menuBarBean;

    private List<CursoCentroCapacitacion> cursoCentroTodosList;
    private List<CursoCentroCapacitacion> cursoCentroList;
    private CursoCentroCapacitacion cursoCentro;
    private String tipoCurso;
    private PlantillaCertificado plantilla;
    private boolean seleccionarTodos;

    public ImpresionCertificadoBean() {
    }

    @PostConstruct
    public void init() {
        tipoCurso = Curso.TIPO_CONTINUO.toString();
        cursoCentroTodosList = cursoCentroFacadeLocal.findAll();
        cursoCentro = null;
        tipoCursoChange();
    }

    public void tipoCursoChange() {
        cursoCentroList = cursoCentroTodosList.stream()
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)) && cc.isCertificadoGeneracionLista())
                .collect(Collectors.toList());
    }

    public void seleccionarCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
        seleccionarTodos = false;
    }

    public void mostrarMensajeError() {
        FacesUtils.addErrorMessage("Debe seleccionar al menos un estudiante.");
    }

    public void confirmarImpresion() {
        try {
            for (Inscripcion inscripcion : cursoCentro.getInscripcionCertificadoImpresionPendienteList()) {
                if (inscripcion.isSeleccionado()) {
                    Certificado certificado = inscripcion.getUltimoCertificado();
                    certificado.registrarImpresion(menuBarBean.getUsuario());
                    certificadoFacadeLocal.edit(certificado);

                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    MensajeCorreo mensajeCorreo = mensajeCorreoFacadeLocal.find(MensajeCorreo.ID_CERTIFICADO_POR_RETIRAR);
                    String mensaje = "<table width=\"100%\" bgcolor=\"#F2F2F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"x_backgroundTable\"><tbody><tr><td><table width=\"550\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"x_devicewidth\"><tbody><tr><td width=\"100%\"><table width=\"550\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"x_devicewidth\"><tbody><tr><td align=\"center\"><div class=\"x_imgpop\"><img data-imagetype=\"External\" src=\"https://www.espe-innovativa.edu.ec/wp-content/uploads/2017/04/logo_color.png\" width=\"286\" border=\"0\" height=\"60\" alt=\"\" class=\"x_banner\" style=\"display:block; border:none; outline:none; text-decoration:none\"></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table><table width=\"100%\" bgcolor=\"#F2F2F2\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" id=\"x_backgroundTable\"><tbody><tr><td><table width=\"550\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" class=\"x_devicewidth\"><tbody><tr><td width=\"100%\" bgcolor=\"#f6f4f5\" class=\"x_devicewidthInter\" style=\"padding:20px\"><table width=\"510\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"x_devicewidthInter\"><tbody><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:30px; padding-left:30px; padding-bottom:20px; text-align:left; line-height:16px\"><strong>Estimado/a:</strong>&nbsp;$$NOMBRE$$</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:0px; padding-right:30px; padding-left:30px; padding-bottom:20px; text-align:left; line-height:16px\">$$MENSAJE$$</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:0px; padding-right:40px; padding-left:40px; padding-bottom:0px; text-align:left; line-height:22px\"><table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"x_tabla-texto\" width=\"440\" style=\"border-collapse:collapse; border:0px solid #f6f4f5\"><tbody><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Curso:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:20px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$CURSO$$</td></tr><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Modalidad:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$MODALIDAD$$</td></tr><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Duración en Horas:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$HORAS$$</td></tr><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Fecha Inicio:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$FECHA_INICIO$$</td></tr><tr><td width=\"35%\" align=\"right\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:3px; padding-left:0px; padding-bottom:5px; text-align:left; line-height:16px\"><strong>Fecha Finalización:</strong></td><td width=\"65%\" align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:20px; padding-left:3px; padding-bottom:5px; text-align:left; line-height:16px\">$$FECHA_FIN$$</td></tr></tbody></table></td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><br>(El envío de este correo es automático, por favor no lo responda. Si tiene alguna inquietud o sugerencia, por favor comunicarse al (593-2)3820 800)</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><br>Atentamente,</td></tr><tr><td align=\"left\" bgcolor=\"#ffffff\" class=\"x_txt\" style=\"font-family:Arial,sans-serif; color:#333333; font-size:12px; padding-top:5px; padding-right:40px; padding-left:40px; padding-bottom:5px; text-align:justify; line-height:22px\"><strong>ESPE-Innovativa EP</strong></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>";
                    mensaje = mensaje.replace("$$NOMBRE$$", inscripcion.getEstudiante().getPersona().getNombresCompletos());
                    mensaje = mensaje.replace("$$MENSAJE$$", mensajeCorreo.getMensaje());
                    mensaje = mensaje.replace("$$CURSO$$", inscripcion.getCursoCentroCapacitacion().getCurso().getNombre());
                    mensaje = mensaje.replace("$$MODALIDAD$$", inscripcion.getCursoCentroCapacitacion().getModalidadDescripcion());
                    mensaje = mensaje.replace("$$HORAS$$", inscripcion.getCursoCentroCapacitacion().getNroHoras().toString());
                    mensaje = mensaje.replace("$$FECHA_INICIO$$", df.format(inscripcion.getCursoCentroCapacitacion().getFechaInicio()));
                    mensaje = mensaje.replace("$$FECHA_FIN$$", df.format(inscripcion.getCursoCentroCapacitacion().getFechaFin()));

                    try {
                        ServicioEnvioMail servicioEnvioMail = new ServicioEnvioMail_Service().getServicioEnvioMailPort();
                        servicioEnvioMail.enviarMail(inscripcion.getEstudiante().getPersona().getEmail(), mensajeCorreo.getAsunto(), mensaje);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            cursoCentro = cursoCentroFacadeLocal.find(cursoCentro.getId());
            seleccionarTodos = false;
            FacesUtils.addInfoMessage("Registro de impresión exitosa.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addErrorMessage("Ocurrió un error inesperado al registrar la impresión de certificados.");
        }
    }

    public void seleccionarTodosChange() {
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoImpresionPendienteList()) {
            i.setSeleccionado(seleccionarTodos);
        }
    }

    public void seleccionarChange() {
        seleccionarTodos = true;
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoImpresionPendienteList()) {
            if (!i.isSeleccionado()) {
                seleccionarTodos = false;
                break;
            }
        }
    }

    public List<CursoCentroCapacitacion> getCursoCentroList() {
        return cursoCentroList;
    }

    public void setCursoCentroList(List<CursoCentroCapacitacion> cursoCentroList) {
        this.cursoCentroList = cursoCentroList;
    }

    public CursoCentroCapacitacion getCursoCentro() {
        return cursoCentro;
    }

    public void setCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public List<String> getProgramaFiltroList() {
        return cursoCentroList.stream().map(cc -> cc.getCurso().getPrograma().getNombre()).distinct().collect(Collectors.toList());
    }

    public List<String> getCursoFiltroList() {
        return cursoCentroList.stream().map(cc -> cc.getCurso().getNombre()).distinct().collect(Collectors.toList());
    }

    public List<String> getCentroCapacitacionFiltroList() {
        return cursoCentroList.stream().map(cc -> cc.getCentroCapacitacion().getNombre()).distinct().collect(Collectors.toList());
    }

    public PlantillaCertificado getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(PlantillaCertificado plantilla) {
        this.plantilla = plantilla;
    }

    public boolean isSeleccionarTodos() {
        return seleccionarTodos;
    }

    public void setSeleccionarTodos(boolean seleccionarTodos) {
        this.seleccionarTodos = seleccionarTodos;
    }

    public boolean isExistenSeleccionados() {
        if (cursoCentroList == null) {
            return false;
        }
        return cursoCentro.getInscripcionCertificadoGeneracionOkList().stream().anyMatch(i -> i.isSeleccionado());
    }

}
