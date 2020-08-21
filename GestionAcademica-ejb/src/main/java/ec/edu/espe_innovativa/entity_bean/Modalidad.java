package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Modalidad implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
    private boolean seleccionado;

    public Modalidad() {
    }

    public Modalidad(String codigo) {
        this.codigo = codigo;
        switch (codigo) {
            case "P":
                this.nombre = "PRESENCIAL";
                break;
            case "S":
                this.nombre = "SEMIPRESENCIAL";
                break;
            case "V":
                this.nombre = "VIRTUAL";
                break;
            case "O":
                this.nombre = "ONLINE";
                break;
            default:
                break;
        }
    }

    public Modalidad(String codigo, String nombre, boolean seleccionado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.seleccionado = seleccionado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.codigo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Modalidad other = (Modalidad) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }



    @Override
    public String toString() {
        return "Modalidad{" + "codigo=" + codigo + ", nombre=" + nombre + ", seleccionado=" + seleccionado + '}';
    }

}
