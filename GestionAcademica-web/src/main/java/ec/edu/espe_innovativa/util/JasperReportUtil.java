package ec.edu.espe_innovativa.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Named
@ApplicationScoped
public class JasperReportUtil {

    @Resource(lookup = "java:/capacitacion_innovativa")
    private DataSource dataSource;

    public JasperReportUtil() {
    }

    private void export(final String nombreReporte, TipoReporte tipoReporte, Map<String, Object> params, Object dataSource) throws Exception {
        FacesContext fContext = FacesContext.getCurrentInstance();
        ExternalContext eContext = fContext.getExternalContext();
        ServletContext sContext = (ServletContext) eContext.getContext();
        HttpServletResponse response = (HttpServletResponse) eContext.getResponse();

        String pathAplicacion = sContext.getRealPath("");

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("APPLICATION_DIR", pathAplicacion);
        if (params != null) {
            parametros.putAll(params);
        }
        JasperPrint jasperPrint;
        InputStream inputStream = new FileInputStream(pathAplicacion + File.separator + "jrxml" + File.separator + nombreReporte + ".jasper");
        if (dataSource instanceof Connection) {
            jasperPrint = JasperFillManager.fillReport(inputStream, parametros, (Connection) dataSource);
        } else {
            jasperPrint = JasperFillManager.fillReport(inputStream, parametros, (JRBeanCollectionDataSource) dataSource);
        }
        if (tipoReporte.equals(TipoReporte.PDF)) {

            //ASI FUNCIONA EN CUALQUIER APLICACION DE CAPACITACION, Y AQUI NO , SALE LOS SIMBOLOS EXTRAÑOS
            /*
            response.setContentType("application/pdf");
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
             */
            //ASI FUNCIONA EN INNOVATIVA SALUD, PERO SE ABRE EN UN XHTML
            /*
            response.setContentType("application/pdf");
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exporter.exportReport();
             */
            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + nombreReporte + ".pdf");
            response.setContentLength(os.size());
            response.getOutputStream().write(os.toByteArray());

        } else if (tipoReporte.equals(TipoReporte.XLSX)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-disposition", "attachment; filename=" + nombreReporte + ".xlsx");
            response.setContentLength(os.size());
            response.getOutputStream().write(os.toByteArray());
        }
        fContext.responseComplete();
    }

    public void exportToPdf(final String nombreReporte, Map<String, Object> params) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            export(nombreReporte, TipoReporte.PDF, params, conn);
        }
    }

    public void exportToPdf(final String nombreReporte, Map<String, Object> params, Collection<?> objectList) throws Exception {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objectList);
        export(nombreReporte, TipoReporte.PDF, params, ds);
    }

    public void exportToXlsx(final String nombreReporte, Map<String, Object> params) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            export(nombreReporte, TipoReporte.XLSX, params, conn);
        }
    }

    public void exportToXlsx(final String nombreReporte, Map<String, Object> params, Collection<?> objectList) throws Exception {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objectList);
        export(nombreReporte, TipoReporte.XLSX, params, ds);
    }

}
