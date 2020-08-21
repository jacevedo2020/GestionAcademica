package ec.edu.espe_innovativa.managed_beans;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import ec.edu.espe_innovativa.entity_bean.Curso;
import ec.edu.espe_innovativa.session_bean.CursoFacadeLocal;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.primefaces.event.FileUploadEvent;

@Named(value = "cursoBean")
@ViewScoped
public class CursoBean implements Serializable {

    @EJB
    private CursoFacadeLocal cursoFacadeLocal;
    private Curso curso;
    private List<Curso> cursoList;
    private List<CursoTemp> cursoTempList;
    private boolean creacionMasiva;
    private boolean errorCargaMasiva;
    @Inject
    private ProgramaBean programaBean;

    public CursoBean() {
    }

    @PostConstruct
    private void init() {
        curso = null;
        creacionMasiva = false;
        List<Curso> cursosTodos = cursoFacadeLocal.findAll();
        cursoList = cursosTodos.stream().filter(c -> c.getTipo().equals(Curso.TIPO_CONTINUO)).collect(Collectors.toList());
    }

    public void nuevo() {
        curso = new Curso();
    }

    public void nuevaCreacionMasiva() {
        creacionMasiva = true;
        cursoTempList = new ArrayList<>();
    }

    public void cancelar() {
        init();
    }

    public void grabar() {
        try {
            if (!creacionMasiva) {
                if (cursoList != null && cursoList.stream()
                        .anyMatch(c -> {
                            if (curso.getId() == null) {
                                return c.getNombre().equals(curso.getNombre());
                            } else {
                                return !c.getId().equals(curso.getId()) && c.getNombre().equals(curso.getNombre().trim()) && c.getModalidad().equals(curso.getModalidad());
                            }
                        })) {
                    FacesUtils.addErrorMessage("Ya existe un Curso Continuo con este nombre y modalidad");
                    return;
                }
                if (curso.getId() == null) {
                    cursoFacadeLocal.create(curso);
                } else {
                    cursoFacadeLocal.edit(curso);
                }
            } else {  //carga masiva de cursos
                if (errorCargaMasiva) {
                    String msg = "Existen errores en los registros. Por favor corrígalos y vuelva a subir el archivo.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
                    return;
                }
                if (cursoTempList == null || cursoTempList.isEmpty()) {
                    FacesUtils.addErrorMessage("Debe cargar el archivo Excel con el listado de cursos a crear.");
                    return;
                }
                for (CursoTemp c : cursoTempList) {
                    Curso cursoNuevo = new Curso();
                    cursoNuevo.setTipo(Curso.TIPO_CONTINUO);
                    cursoNuevo.setPrograma(programaBean.getProgramaActivoList().stream().filter(p -> p.getCodigo().equals(c.getCodigoAreaConocimiento())).findFirst().get());
                    cursoNuevo.setCodigo(c.getCodigoCurso());
                    cursoNuevo.setNombre(c.getNombre());
                    cursoNuevo.setModalidad(c.getModalidad().charAt(0));
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                    dfs.setGroupingSeparator('.');
                    dfs.setDecimalSeparator(',');
                    DecimalFormat df = new DecimalFormat("#,##0.0#", dfs);
                    df.setParseBigDecimal(true);
                    cursoNuevo.setPrecio((BigDecimal) df.parse(c.getPrecio()));
                    cursoNuevo.setNroHoras(Integer.valueOf(c.getDuracion()));
                    cursoNuevo.setTipoCertificado(c.getTipoCertificado().charAt(0));
                    cursoNuevo.setNroMinimoParticipantes(Integer.valueOf(c.getNroMinimoParticipantes()));
                    cursoFacadeLocal.create(cursoNuevo);
                }
            }
            init();
            FacesUtils.addMessageRegistroGrabado();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (e.getCause().getCause().getMessage().contains("(codigo)")) {
                    FacesUtils.addErrorMessage("formPrincipal:txtCodigo", "Ya existe un Curso Continuo con este código.");
                    return;
                }
            } catch (Exception e1) {
            }
            FacesUtils.addMessageRegistroNoGrabado();
        }
    }

    public void seleccionar(Curso curso) {
        this.curso = curso;
    }

    public void eliminar(Curso curso) {
        try {
            cursoFacadeLocal.remove(curso);
            init();
            FacesUtils.addMessageRegistroEliminado();
        } catch (Exception e) {
            FacesUtils.addMessageRegistroNoEliminado();
        }
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Curso> getCursoList() {
        return cursoList;
    }

    public void setCursoList(List<Curso> cursoList) {
        this.cursoList = cursoList;
    }

    public boolean isCreacionMasiva() {
        return creacionMasiva;
    }

    public void setCreacionMasiva(boolean creacionMasiva) {
        this.creacionMasiva = creacionMasiva;
    }

    public List<CursoTemp> getCursoTempList() {
        return cursoTempList;
    }

    public void setCursoTempList(List<CursoTemp> cursoTempList) {
        this.cursoTempList = cursoTempList;
    }

    public void subirDocumentoCargaMasiva(FileUploadEvent event) {
        cursoTempList = new ArrayList<>();
        errorCargaMasiva = false;

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(event.getFile().getInputstream()));
                CSVParser parser = CSVFormat.DEFAULT.withDelimiter('|').withHeader().parse(br);) {
            for (CSVRecord record : parser) {
                CursoTemp cursoTemp = new CursoTemp();
                cursoTemp.setCodigoAreaConocimiento(record.get(0).trim());
                cursoTemp.setCodigoCurso(record.get(1).trim());
                cursoTemp.setNombre(record.get(2).trim());
                cursoTemp.setModalidad(record.get(3).trim());
                cursoTemp.setPrecio(record.get(4).trim());
                cursoTemp.setDuracion(record.get(5).trim());
                cursoTemp.setTipoCertificado(record.get(6).trim());
                cursoTemp.setNroMinimoParticipantes(record.get(7).trim());

                if (cursoTemp.getCodigoAreaConocimiento().isEmpty()) {
                    cursoTemp.agregarError("CODIGO AREA CONOCIMIENTO: No existe un valor.");
                } else if (cursoTemp.getCodigoAreaConocimiento().length() > 3) {
                    cursoTemp.agregarError("CÓDIGO ÁREA CONOCIMIENTO. No debe tener más de 3 caracteres.");
                } else if (programaBean.getProgramaActivoList().stream().noneMatch(p -> p.getCodigo().equals(cursoTemp.getCodigoAreaConocimiento()))) {
                    cursoTemp.agregarError("CÓDIGO ÁREA CONOCIMIENTO. No existe en la base datos una Área de Conocimiento con este código.");
                }

                if (cursoTemp.getCodigoCurso().isEmpty()) {
                    cursoTemp.agregarError("CÓDIGO CURSO: No existe un valor.");
                } else if (cursoTempList.stream().anyMatch(c2 -> c2.getCodigoCurso().equals(cursoTemp.getCodigoCurso()))) {
                    cursoTemp.agregarError("CÓDIGO CURSO. En el listado existe otro curso con este código.");
                } else if (cursoList.stream().anyMatch(c2 -> c2.getCodigo().equals(cursoTemp.getCodigoCurso()))) {
                    cursoTemp.agregarError("CÓDIGO CURSO. En la base de datos ya existe un curso con este código.");
                }

                if (cursoTemp.getNombre().isEmpty()) {
                    cursoTemp.agregarError("NOMBRE: No existe un valor.");
                } else if (cursoTemp.getNombre().length() > 100) {
                    cursoTemp.agregarError("NOMBRE. No debe tener más de 100 caracteres.");
                }

                if (cursoTemp.getModalidad().isEmpty()) {
                    cursoTemp.agregarError("MODALIDAD: No existe un valor.");
                } else if (!cursoTemp.getModalidad().equals("V")
                        && !cursoTemp.getModalidad().equals("P")
                        && !cursoTemp.getModalidad().equals("S")) {
                    cursoTemp.agregarError("MODALIDAD. No debe tener más de 1 caracter. Los valores permitidos son: V, P y S.");
                } else if (!cursoTemp.getNombre().isEmpty()) {
                    if (cursoTempList.stream().anyMatch(c2 -> c2.getNombre().equalsIgnoreCase(cursoTemp.getNombre()) && c2.getModalidad().equals(cursoTemp.getModalidad()))) {
                        cursoTemp.agregarError("MODALIDAD. En el listado existe otro curso con este mismo nombre y modalidad.");
                    } else if (cursoList.stream().anyMatch(c2 -> c2.getNombre().equalsIgnoreCase(cursoTemp.getNombre()) && c2.getModalidad().equals(cursoTemp.getModalidad().charAt(0)))) {
                        cursoTemp.agregarError("NOMBRE. En la base de datos ya existe un curso con este mismo nombre y modalidad.");
                    }
                }
                if (cursoTemp.getPrecio().isEmpty()) {
                    cursoTemp.agregarError("PRECIO: No existe un valor.");
                } else {
                    try {
                        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                        dfs.setGroupingSeparator('.');
                        dfs.setDecimalSeparator(',');
                        DecimalFormat df = new DecimalFormat("#,##0.00", dfs);
                        df.setParseBigDecimal(true);
                        BigDecimal precio = (BigDecimal) df.parse(cursoTemp.getPrecio());
                        System.out.println(precio);
                        cursoTemp.setPrecio(df.format(precio));
                    } catch (ParseException e) {
                        cursoTemp.agregarError("PRECIO. No es un valor numérico.");
                    }

                }

                if (cursoTemp.getDuracion().isEmpty()) {
                    cursoTemp.agregarError("DURACION EN HORAS: No existe un valor.");
                } else {
                    try {
                        Integer duracion = Integer.valueOf(cursoTemp.getDuracion());
                        System.out.println(duracion);
                    } catch (Exception e) {
                        e.printStackTrace();
                        cursoTemp.agregarError("DURACION EN HORAS. Valor incorrecto, debe ser un número entero.");
                    }
                }

                if (cursoTemp.getTipoCertificado().isEmpty()) {
                    cursoTemp.agregarError("TIPO CERTIFICADO: No existe un valor.");
                } else if (cursoTemp.getTipoCertificado().length() > 1 || (!cursoTemp.getTipoCertificado().equals("A") && !cursoTemp.getTipoCertificado().equals("P"))) {
                    cursoTemp.agregarError("TIPO CERTIFICADO: No debe tener más de 1 caracter. Los valores permitidos son: A y P.");
                }

                if (cursoTemp.getNroMinimoParticipantes().isEmpty()) {
                    cursoTemp.agregarError("NRO. MINIMO PARTICIPANTES: No existe un valor.");
                } else {
                    try {
                        Integer nroMinimoParticipantes = Integer.valueOf(cursoTemp.getNroMinimoParticipantes());
                        System.out.println(nroMinimoParticipantes);
                    } catch (Exception e) {
                        e.printStackTrace();
                        cursoTemp.agregarError("NRO. MINIMO PARTICIPANTES. Valor incorrecto, debe ser un número entero.");
                    }
                }

                if (cursoTemp.getError() != null && !cursoTemp.getError().isEmpty()) {
                    errorCargaMasiva = true;
                }
                cursoTempList.add(cursoTemp);
            }

            if (errorCargaMasiva) {
                FacesUtils.addErrorMessage("Existen errores en los registros. Por favor corrígalos y vuelva a subir el archivo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    /*public void subirDocumentoCargaMasiva(FileUploadEvent event) {
        cursoCargaMasivaList = new ArrayList<>();
        boolean error = false;
        try {
            Workbook workbook = new XSSFWorkbook(event.getFile().getInputstream());
            Sheet sheet = workbook.getSheetAt(0);
            int noFila = -1;
            for (Row row : sheet) {
                noFila++;
                if (noFila == 0) {
                    continue;
                }
                Curso c = new Curso();

                //CÓDIGO ÁREA CONOCIMIENTO
                if (row.getCell(0) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO ÁREA CONOCIMIENTO. No existe un valor.");
                    error = true;
                } else {

                    try {
                        final String codPrograma = row.getCell(0).getStringCellValue();
                        if (codPrograma.trim().length() > 3) {
                            FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO ÁREA CONOCIMIENTO. No debe tener más de 3 caracteres.");
                            error = true;
                        } else {
                            if (programaBean.getProgramaList().stream().noneMatch(p -> p.getCodigo().equals(codPrograma))) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO ÁREA CONOCIMIENTO. No existe en la base datos una Área de Conocimiento con código '" + codPrograma + "'.");
                                error = true;
                            } else {
                                c.setPrograma(programaBean.getProgramaList().stream().filter(p->p.getCodigo().equals(codPrograma)).findFirst().get());
                            }
                        }
                    } catch (Exception e) {
                        try {
                            final String codPrograma = String.valueOf((int) row.getCell(0).getNumericCellValue());
                            if (codPrograma.trim().length() > 3) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO ÁREA CONOCIMIENTO. No debe tener más de 3 caracteres.");
                                error = true;
                            } else {
                                if (programaBean.getProgramaList().stream().noneMatch(p -> p.getCodigo().equals(codPrograma))) {
                                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO ÁREA CONOCIMIENTO. No existe en la base datos una Área de Conocimiento con código '" + codPrograma + "'.");
                                    error = true;
                                } else {
                                    c.setPrograma(programaBean.getProgramaList().stream().filter(p->p.getCodigo().equals(codPrograma)).findFirst().get());
                                }
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }

                //CÓDIGO CURSO
                if (row.getCell(1) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO CURSO. No existe un valor.");
                    error = true;
                } else {
                    try {
                        c.setCodigo(row.getCell(1).getStringCellValue());
                        if (cursoCargaMasivaList.stream().anyMatch(c2 -> c2.getCodigo().equals(c.getCodigo()))) {
                            FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO CURSO. Hay otro curso con este código '" + c.getCodigo() + "'.");
                            error = true;
                        } else if (cursoList.stream().anyMatch(c2 -> c2.getCodigo().equals(c.getCodigo()))) {
                            FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO CURSO. En la base de datos ya existe un curso con este código '" + c.getCodigo() + "'.");
                            error = true;
                        }
                    } catch (Exception e) {
                        try {
                            c.setCodigo(String.valueOf((int) row.getCell(1).getNumericCellValue()));
                            if (cursoCargaMasivaList.stream().anyMatch(c2 -> c2.getCodigo().equals(c.getCodigo()))) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO CURSO. Hay otro curso con este código '" + c.getCodigo() + "'.");
                                error = true;
                            } else if (cursoList.stream().anyMatch(c2 -> c2.getCodigo().equals(c.getCodigo()))) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: CÓDIGO CURSO. En la base de datos ya existe un curso con este código '" + c.getCodigo() + "'.");
                                error = true;
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                }

                //NOMBRE
                if (row.getCell(2) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NOMBRE. No existe un valor.");
                    error = true;
                } else {
                    String nombre = row.getCell(2).getStringCellValue();
                    if (nombre.trim().length() > 100) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NOMBRE. No debe tener más de 100 caracteres.");
                        error = true;
                    } else {
                        c.setNombre(nombre);
                    }
                }

                //MODALIDAD
                if (row.getCell(3) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: MODALIDAD. No existe un valor.");
                    error = true;
                } else {
                    String modalidad = row.getCell(3).getStringCellValue().trim();
                    if (modalidad.length() > 1 || (!modalidad.equals("V") && !modalidad.equals("P") && !modalidad.equals("S"))) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: MODALIDAD. No debe tener más de 1 caracter. Los valores permitidos son: V, P y S");
                        error = true;
                    } else {
                        c.setModalidad(modalidad.charAt(0));
                        if (c.getNombre() != null) {
                            if (cursoCargaMasivaList.stream().anyMatch(c2 -> c2.getNombre().equalsIgnoreCase(c.getNombre()) && c2.getModalidad().equals(c.getModalidad()))) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NOMBRE. Hay otro curso con este nombre '" + c.getNombre() + "' y esta modalidad '" + c.getModalidad() + "'.");
                                error = true;
                            }else if (cursoList.stream().anyMatch(c2 -> c2.getNombre().equalsIgnoreCase(c.getNombre()) && c2.getModalidad().equals(c.getModalidad()))) {
                                FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NOMBRE. En la base de datos ya existe un curso con este nombre '" + c.getNombre() + "' y esta modalidad '" + c.getModalidad() + "'.");
                                error = true;
                            }

                        }
                    }
                }

                //PRECIO
                if (row.getCell(4) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: PRECIO. No existe un valor.");
                    error = true;
                } else {
                    try {
                        c.setPrecio(new BigDecimal(row.getCell(4).getNumericCellValue()));
                    } catch (Exception e) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: PRECIO. No es un valor numérico.");
                        error = true;
                    }
                }

                //DURACIÓN EN HORAS
                if (row.getCell(5) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: DURACIÓN EN HORAS. No existe un valor.");
                    error = true;
                } else {
                    try {
                        c.setNroHoras((int) row.getCell(5).getNumericCellValue());
                    } catch (Exception e) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: DURACIÓN EN HORAS. No es un valor numérico.");
                        error = true;
                    }
                }

                //TIPO CERTIFICADO
                if (row.getCell(6) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: TIPO CERTIFICADO. No existe un valor.");
                    error = true;
                } else {
                    String tipoCertificado = row.getCell(6).getStringCellValue().trim();
                    if (tipoCertificado.length() > 1 || (!tipoCertificado.equals("A") && !tipoCertificado.equals("P"))) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: TIPO CERTIFICADO. No debe tener más de 1 caracter. Los valores permitidos son: A y P");
                        error = true;
                    } else {
                        c.setTipoCertificado(tipoCertificado.charAt(0));
                    }
                }

                //NRO. MINIMO PARTICIPANTES
                if (row.getCell(6) == null) {
                    FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NRO. MINIMO PARTICIPANTES. No existe un valor.");
                    error = true;
                } else {
                    try {
                        c.setNroMinimoParticipantes((int) row.getCell(7).getNumericCellValue());
                    } catch (Exception e) {
                        FacesUtils.addErrorMessage("Error en Fila: " + noFila + ", Columna: NRO. MINIMO PARTICIPANTES. No es un valor numérico.");
                        error = true;
                    }
                }

                c.setTipo(Curso.TIPO_CONTINUO);
                c.setEstado(Curso.ESTADO_ACTIVO);
                cursoCargaMasivaList.add(c);
            }
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
            FacesUtils.addErrorMessage("No fue posible cargar el archivo seleccionado");
        }
        if (error) {
            cursoCargaMasivaList.clear();
        }

    }*/
    public List<String> getProgramaFiltroList() {
        return getCursoList().stream()
                .map(c -> c.getPrograma().getNombre())
                .distinct()
                .collect(Collectors.toList());
    }
        public List<String> getModalidadFiltroList() {
        return getCursoList().stream()
                .map(c -> c.getModalidadDescripcion())
                .distinct()
                .collect(Collectors.toList());
    }


}
