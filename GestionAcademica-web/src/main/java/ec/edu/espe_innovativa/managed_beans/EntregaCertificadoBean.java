package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import com.jvc.medisys.medisysweb.menu.navegador.MenuBarBean;
import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Certificado;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.entity_bean.CursoCentroCapacitacion;
import ec.edu.espe_innovativa.entity_bean.EnvioCertificado;
import ec.edu.espe_innovativa.entity_bean.Inscripcion;
import ec.edu.espe_innovativa.entity_bean.Parametro;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.session_bean.CertificadoFacadeLocal;
import ec.edu.espe_innovativa.session_bean.CursoCentroCapacitacionFacadeLocal;
import ec.edu.espe_innovativa.session_bean.ParametroFacadeLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

@Named(value = "entregaCertificadoBean")
@ViewScoped
public class EntregaCertificadoBean implements Serializable {

    @EJB
    private CursoCentroCapacitacionFacadeLocal cursoCentroFacadeLocal;
    @EJB
    private ParametroFacadeLocal parametroFacadeLocal;
    @EJB
    private CertificadoFacadeLocal certificadoFacadeLocal;
    private Parametro parametroSecuencialCertificados;
    @Inject
    private MenuBarBean menuBarBean;

    private List<CursoCentroCapacitacion> cursoCentroTodosList;
    private List<CursoCentroCapacitacion> cursoCentroList;
    private List<Inscripcion> inscripcionList;
    private CursoCentroCapacitacion cursoCentro;
    private String tipoCurso;
    private PlantillaCertificado plantilla;
    private boolean seleccionarTodos;
    private boolean seleccionarTodos2;
    private boolean formularioConfirmacionEnvio;

    private EnvioCertificado envioCertificado;
    private Provincia provincia;
    private Canton canton;

    
    private Inscripcion inscripcion;
    public EntregaCertificadoBean() {
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
                .filter(cc -> cc.getCurso().getTipo().equals(tipoCurso.charAt(0)) && cc.isCertificadoImpresionLista())
                .collect(Collectors.toList());
    }

    public void seleccionarCursoCentro(CursoCentroCapacitacion cursoCentro) {
        this.cursoCentro = cursoCentro;
        seleccionarTodos = false;
    }

    public void mostrarMensajeErrorEnvio() {
        FacesUtils.addErrorMessage("Debe seleccionar al menos un estudiante para entregar o enviar el certificado.");
    }

    public void mostrarMensajeErrorEnvio2() {
        FacesUtils.addErrorMessage("Debe seleccionar al menos un estudiante para devolver el certificado.");
    }

    public void iniciarConfirmacionEnvio() {
        envioCertificado = new EnvioCertificado();
        envioCertificado.setTipoRegistro(EnvioCertificado.TIPO_REGISTRO_ENTREGA_PRESENCIAL);
        envioCertificado.setFecha(new Date());
        
        formularioConfirmacionEnvio = true;
        inscripcionList = new ArrayList<>();
        for (Inscripcion insc : cursoCentro.getInscripcionCertificadoImpresionOkList()) {
            if (insc.isSeleccionado()) {
                inscripcionList.add(insc);
            }
        }
        tipoRegistroChange();

    }
    public void iniciarConfirmacionDevolucion() {
        envioCertificado = new EnvioCertificado();
        envioCertificado.setTipoRegistro(EnvioCertificado.TIPO_REGISTRO_DEVOLUCION);
        envioCertificado.setFecha(new Date());
        
        formularioConfirmacionEnvio = true;
        inscripcionList = new ArrayList<>();
        for (Inscripcion insc : cursoCentro.getInscripcionCertificadoImpresionOkList()) {
            if (insc.isSeleccionado2()) {
                inscripcionList.add(insc);
            }
        }
    }

    public void tipoRegistroChange() {
        provincia=null;
        canton=null;
        envioCertificado.setEntregadoATercero(EnvioCertificado.ENTREGADO_A_TERCERO_NO);
        if (cursoCentro.isCursoContinuo()) {
            if (inscripcionList.size() == 1) {
                Persona p = inscripcionList.get(0).getEstudiante().getPersona();
                //envioCertificado.setDestinatarioApellidos(p.getApellidos());
                envioCertificado.setDestinatarioNombres(p.getNombresCompletos());
                if (envioCertificado.isTipoRegistroEntregaPresencial()) {
                    envioCertificado.setDestinatarioParroquia(null);
                    envioCertificado.setDestinatarioDireccion(null);
                    envioCertificado.setDestinatarioReferencia(null);
                    envioCertificado.setGuia(null);
                } else {
                    envioCertificado.setDestinatarioParroquia(p.getParroquia());
                    envioCertificado.setDestinatarioDireccion(p.getDireccion());
                    envioCertificado.setDestinatarioReferencia(p.getDireccionReferencia());
                }
            }
        }else{  //curso corporativo
            Persona p = cursoCentro.getInscripcionEmpresa().getEstudiante().getPersona();
            //envioCertificado.setDestinatarioApellidos(p.getContactoApellidos());
            envioCertificado.setDestinatarioNombres(p.getContactoNombres());
                if (envioCertificado.isTipoRegistroEntregaPresencial()) {
                    envioCertificado.setDestinatarioParroquia(null);
                    envioCertificado.setDestinatarioDireccion(null);
                    envioCertificado.setDestinatarioReferencia(null);
                    envioCertificado.setGuia(null);
                } else {
                    envioCertificado.setDestinatarioParroquia(p.getParroquia());
                    envioCertificado.setDestinatarioDireccion(p.getDireccion());
                    envioCertificado.setDestinatarioReferencia(p.getDireccionReferencia());
                }
        }
        if (envioCertificado.getDestinatarioParroquia()!=null){
            canton= envioCertificado.getDestinatarioParroquia().getCanton();
            provincia= canton.getProvincia();
        }
    }

    public void cancelarConfirmacionEnvio() {
        envioCertificado = null;
        formularioConfirmacionEnvio = false;
    }

    public void confirmarEnvio() {
        try {
            for (Inscripcion inscripcion : inscripcionList) {
                Certificado certificado = inscripcion.getUltimoCertificado();

                EnvioCertificado envioCertificadoNuevo = new EnvioCertificado();
                envioCertificadoNuevo.setTipoRegistro(envioCertificado.getTipoRegistro());
                envioCertificadoNuevo.setEnviadoPor(menuBarBean.getUsuario());
                envioCertificadoNuevo.setFecha(envioCertificado.getFecha());
                envioCertificadoNuevo.setGuia(envioCertificado.getGuia());
                envioCertificadoNuevo.setCertificado(certificado);
                envioCertificadoNuevo.setDestinatarioApellidos(envioCertificado.getDestinatarioApellidos());
                envioCertificadoNuevo.setDestinatarioNombres(envioCertificado.getDestinatarioNombres());
                envioCertificadoNuevo.setEntregadoATercero(envioCertificado.getEntregadoATercero());
                envioCertificadoNuevo.setDestinatarioParroquia(envioCertificado.getDestinatarioParroquia());
                envioCertificadoNuevo.setDestinatarioDireccion(envioCertificado.getDestinatarioDireccion());
                envioCertificadoNuevo.setDestinatarioReferencia(envioCertificado.getDestinatarioReferencia());

                certificado.agregarEnvio(envioCertificadoNuevo);
                certificadoFacadeLocal.edit(certificado);
            }
            cursoCentro = cursoCentroFacadeLocal.find(cursoCentro.getId());
            seleccionarTodos = false;
            if (envioCertificado.isTipoRegistroEntregaPresencial())
                FacesUtils.addInfoMessage("Registro de entrega de certificado exitoso.");
            else
                FacesUtils.addInfoMessage("Registro de envío de certificado exitoso.");
            cancelarConfirmacionEnvio();
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addErrorMessage("Ocurrió un error inesperado al registrar el envío/entrega de certificados.");
        }
    }

    public void confirmarDevolucion() {
        try {
            for (Inscripcion inscripcion : inscripcionList) {
                Certificado certificado = inscripcion.getUltimoCertificado();

                EnvioCertificado envioCertificadoNuevo = new EnvioCertificado();
                envioCertificadoNuevo.setTipoRegistro(envioCertificado.getTipoRegistro());
                envioCertificadoNuevo.setEnviadoPor(menuBarBean.getUsuario());
                envioCertificadoNuevo.setFecha(envioCertificado.getFecha());
                envioCertificadoNuevo.setCertificado(certificado);
                certificado.agregarEnvio(envioCertificadoNuevo);
                certificadoFacadeLocal.edit(certificado);
            }
            cursoCentro = cursoCentroFacadeLocal.find(cursoCentro.getId());
            seleccionarTodos = false;
            cancelarConfirmacionEnvio();
            FacesUtils.addInfoMessage("Registro de devolución de certificado exitoso.");
        } catch (Exception e) {
            e.printStackTrace();
            FacesUtils.addErrorMessage("Ocurrió un error inesperado al registrar la impresión de certificados.");
        }
    }

    public void seleccionarTodosChange() {
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoEntregaEnvioPendienteList()) {
            i.setSeleccionado(seleccionarTodos);
        }
    }

    public void seleccionarTodos2Change() {
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoEnvioOkList()) {
            i.setSeleccionado2(seleccionarTodos2);
        }
    }

    public void seleccionarChange() {
        seleccionarTodos = true;
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoEntregaEnvioPendienteList()) {
            if (!i.isSeleccionado()) {
                seleccionarTodos = false;
                break;
            }
        }
    }

    public void seleccionar2Change() {
        seleccionarTodos2 = true;
        for (Inscripcion i : cursoCentro.getInscripcionCertificadoEnvioOkList()) {
            if (!i.isSeleccionado2()) {
                seleccionarTodos2 = false;
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
        return cursoCentro.getInscripcionCertificadoImpresionOkList().stream()
                .anyMatch(i -> i.isSeleccionado());
    }

    public boolean isExistenSeleccionados2() {
        if (cursoCentroList == null) {
            return false;
        }
        return cursoCentro.getInscripcionCertificadoImpresionOkList().stream()
                .anyMatch(i -> i.isSeleccionado2());
    }

    public boolean isFormularioConfirmacionEnvio() {
        return formularioConfirmacionEnvio;
    }

    public void setFormularioConfirmacionEnvio(boolean formularioConfirmacionEnvio) {
        this.formularioConfirmacionEnvio = formularioConfirmacionEnvio;
    }

    public List<Inscripcion> getInscripcionList() {
        return inscripcionList;
    }

    public void setInscripcionList(List<Inscripcion> inscripcionList) {
        this.inscripcionList = inscripcionList;
    }

    public boolean isSeleccionarTodos2() {
        return seleccionarTodos2;
    }

    public void setSeleccionarTodos2(boolean seleccionarTodos2) {
        this.seleccionarTodos2 = seleccionarTodos2;
    }

    public EnvioCertificado getEnvioCertificado() {
        return envioCertificado;
    }

    public void setEnvioCertificado(EnvioCertificado envioCertificado) {
        this.envioCertificado = envioCertificado;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
        canton=null;
        envioCertificado.setDestinatarioParroquia(null);
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
        envioCertificado.setDestinatarioParroquia(null);
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    
}
