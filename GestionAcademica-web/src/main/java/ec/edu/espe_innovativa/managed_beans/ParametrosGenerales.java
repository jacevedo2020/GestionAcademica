package ec.edu.espe_innovativa.managed_beans;

import static java.io.File.separatorChar;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
public class ParametrosGenerales{
    public static final String URL_APLICACION= "http:/localhost:8086/GestionAcademica-web";
    public static final String DIRECTORIO_RAIZ = "documentos_adjuntos";
    public static final String DIRECTORIO_PLANTILLAS = DIRECTORIO_RAIZ + separatorChar + "plantillas";
    public static final String DIRECTORIO_CERTIFICADOS = DIRECTORIO_RAIZ + separatorChar + "certificados";
    public static final String DIRECTORIO_MODIFICACION_EVALUACION = DIRECTORIO_RAIZ + separatorChar + "modificacion_evaluacion";
    public static final String DIRECTORIO_INSCRIPCIONES = DIRECTORIO_RAIZ + separatorChar + "inscripciones";
    public static final String DIRECTORIO_TEMP = DIRECTORIO_RAIZ + separatorChar + "temp";
    public static final String URL_INSCRIPCIONES = "/" + DIRECTORIO_INSCRIPCIONES.replace("\\", "/");
    public static final String URL_PLANTILLAS = "/" + DIRECTORIO_PLANTILLAS.replace("\\", "/");
    public static final String URL_CERTIFICADOS = "/" + DIRECTORIO_CERTIFICADOS.replace("\\", "/");
    public static final String URL_MODIFICACION_EVALUACION = "/" + DIRECTORIO_MODIFICACION_EVALUACION.replace("\\", "/");
    public static final String URL_TEMP = "/" + DIRECTORIO_TEMP.replace("\\", "/");
    
    private static ServletContext getServletContext(){
        return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    }
    public static String getDirectorioAbsolutoInscripciones(){
            String path = getServletContext().getRealPath("") + separatorChar + DIRECTORIO_INSCRIPCIONES;
            return path;
    }
    public static String getDirectorioAbsolutoPlantillas(){
            String path = getServletContext().getRealPath("") + separatorChar + DIRECTORIO_PLANTILLAS;
            return path;
    }
    public static String getDirectorioAbsolutoCertificados(){
            String path = getServletContext().getRealPath("") + separatorChar + DIRECTORIO_CERTIFICADOS;
            return path;
    }
    public static String getDirectorioAbsolutoModificacionEvaluacion(){
            String path = getServletContext().getRealPath("") + separatorChar + DIRECTORIO_MODIFICACION_EVALUACION;
            return path;
    }
    public static String getDirectorioAbsolutoTemp(){
            String path = getServletContext().getRealPath("") + separatorChar + DIRECTORIO_TEMP;
            return path;
    }
}
