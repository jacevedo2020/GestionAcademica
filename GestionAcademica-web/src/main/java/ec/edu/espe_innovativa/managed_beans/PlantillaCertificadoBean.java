package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.PlantillaCertificado;
import ec.edu.espe_innovativa.session_bean.PlantillaCertificadoFacadeLocal;
import ec.edu.espe_innovativa.util.Utils;
import static java.io.File.separatorChar;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;

@Named(value = "plantillaCertificadoBean")
@ViewScoped
public class PlantillaCertificadoBean implements Serializable {

    @EJB
    private PlantillaCertificadoFacadeLocal plantillaCertificadoFacadeLocal;
    private List<PlantillaCertificado> plantillaList;
    private PlantillaCertificado plantilla;

    public PlantillaCertificadoBean() {
    }

    @PostConstruct
    public void init() {
        plantillaList = plantillaCertificadoFacadeLocal.findAll();
        plantilla = null;
    }

    public void nuevo() {
        Integer id = plantillaList.stream().mapToInt(p -> p.getId()).max().orElse(0);
        plantilla = new PlantillaCertificado();
        plantilla.setId(id + 1);
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (plantilla.getFechaCreacion() == null) {
                plantillaCertificadoFacadeLocal.create(plantilla);
            } else {
                plantillaCertificadoFacadeLocal.edit(plantilla);
            }
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            if (e.getCause().getCause().getMessage().contains("(nombre)")) {
                FacesUtils.addErrorMessage("formPrincipal:txtNombre", "Ya existe una Plantilla con este nombre.");
                return;
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void eliminar(PlantillaCertificado plantillaCertificado) {
        try {
            plantillaCertificadoFacadeLocal.remove(plantillaCertificado);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public void subirDocumentoPago(FileUploadEvent event) {
        try {
            String pathRelativo= plantilla.getId().toString();
            String pathAbsoluto = ParametrosGenerales.getDirectorioAbsolutoPlantillas()+ separatorChar + pathRelativo;

            if (!Files.isDirectory(Paths.get(pathAbsoluto))) {
                Files.createDirectories(Paths.get(pathAbsoluto));
            }
            String nombreDocumento = Utils.depurarNombreDocumento(event.getFile().getFileName());
            pathAbsoluto = pathAbsoluto + separatorChar + nombreDocumento;
             Files.copy(event.getFile().getInputstream(), Paths.get(pathAbsoluto), StandardCopyOption.REPLACE_EXISTING);

            plantilla.setDocumentoPlantillaNombre(nombreDocumento);
            plantilla.setDocumentoPlantillaUrl(ParametrosGenerales.URL_PLANTILLAS + "/" + pathRelativo);
        } catch (Exception e) {
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
    }

    public List<PlantillaCertificado> getPlantillaList() {
        return plantillaList;
    }
    public List<PlantillaCertificado> getPlantillaActivaList() {
        return plantillaList.stream().filter(p->p.getEstado()=='A').collect(Collectors.toList());
    }

    public void setPlantillaList(List<PlantillaCertificado> plantillaList) {
        this.plantillaList = plantillaList;
    }

    public PlantillaCertificado getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(PlantillaCertificado plantilla) {
        this.plantilla = plantilla;
    }

}
