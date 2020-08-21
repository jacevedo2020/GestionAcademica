package ec.edu.espe_innovativa.managed_beans;

public class CursoTemp {

    private String codigoAreaConocimiento;
    private String codigoCurso;
    private String nombre;
    private String modalidad;
    private String precio;
    private String duracion;
    private String tipoCertificado;
    private String nroMinimoParticipantes;
    private String error;

    public String getCodigoAreaConocimiento() {
        return codigoAreaConocimiento;
    }

    public void setCodigoAreaConocimiento(String codigoAreaConocimiento) {
        this.codigoAreaConocimiento = codigoAreaConocimiento;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getNroMinimoParticipantes() {
        return nroMinimoParticipantes;
    }

    public void setNroMinimoParticipantes(String nroMinimoParticipantes) {
        this.nroMinimoParticipantes = nroMinimoParticipantes;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void agregarError(String nuevoError) {
        if (error == null) {
            error = "";
        } else {
            error += "\n";
        }
        error += nuevoError;
    }

}
