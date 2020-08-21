package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "opcion")
@XmlRootElement
public class Opcion implements Serializable, Comparable<Opcion> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 50)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private Character estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "url")
    private String url;
    @OneToMany(mappedBy = "opcionPadre")
    private List<Opcion> opcionList;
    @JoinColumn(name = "id_opcion_padre", referencedColumnName = "id")
    @ManyToOne
    private Opcion opcionPadre;

    @Column(name = "orden")
    private Integer orden;

    @Transient
    private boolean seleccionado;

    public Opcion() {
    }

    public Opcion(Integer id) {
        this.id = id;
    }

    public Opcion(Integer id, String nombre, Character estado, String url) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.url = url;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @XmlTransient
    public List<Opcion> getOpcionList() {
        return opcionList;
    }

    public void setOpcionList(List<Opcion> opcionList) {
        this.opcionList = opcionList;
    }

    public Opcion getOpcionPadre() {
        return opcionPadre;
    }

    public void setOpcionPadre(Opcion opcionPadre) {
        this.opcionPadre = opcionPadre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }


    
    public String getPathMenu() {
        String result = nombre;
        Opcion op = this;
        while (op.getOpcionPadre() != null) {
            result = op.getOpcionPadre().getNombre() + "/" + result;
            op = op.getOpcionPadre();
        }
        return result;
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
        if (!(object instanceof Opcion)) {
            return false;
        }
        Opcion other = (Opcion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "\nOpcion{" + "id=" + id + ", nombre=" + nombre + ", seleccionado=" + isSeleccionado() + '}';
    }

    public Opcion getOpcionNivelCero() {
        Opcion opcionNivelCero = this;
        while (opcionNivelCero.getOpcionPadre() != null) {
            opcionNivelCero = opcionNivelCero.getOpcionPadre();
        }
        return opcionNivelCero;
    }

    public boolean esOpcionPadre(Opcion posiblePadre) {
        if (this.equals(posiblePadre)) {
            return false;
        }
        Opcion opcion = this;
        while (opcion.getOpcionPadre() != null && !opcion.equals(posiblePadre)) {
            opcion = opcion.getOpcionPadre();
        }
        if (opcion.equals(posiblePadre)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Opcion o) {
        return this.orden.compareTo(o.orden);
    }
}
