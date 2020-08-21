package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_estudiante")
@XmlRootElement
public class TipoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private Character estado;

    public final static Integer ID_PARTICULAR = 1;
    public final static Integer ID_EGRESADO_ESPE = 2;
    public final static Integer ID_ESTUDIANTE_ESPE = 3;
    public final static Integer ID_MILITARES_FFAA = 4;
    public final static Integer ID_SERVIDOR_PUBLICO_ESPE = 5;
    public final static Integer ID_CAPACIDADES_ESPECIALES = 6;
    public final static Integer ID_CORPORATIVO = 7;

    public TipoEstudiante() {
    }

    public TipoEstudiante(Integer id) {
        this.id = id;
    }

    public TipoEstudiante(Integer id, String nombre, Character estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEstudiante)) {
            return false;
        }
        TipoEstudiante other = (TipoEstudiante) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.TipoEstudiante[ id=" + id + " ]";
    }

}
