package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "proyecto")
@XmlRootElement
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1)
    @Column(name = "numero1")
    private String numero1;
    @Size(max = 2)
    @Column(name = "numero2")
    private String numero2;
    @Size(max = 4)
    @Column(name = "numero3")
    private String numero3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "estado")
    private Character estado;

    private final static Character ESTADO_EN_EJECUCION = 'E';
    private final static Character ESTADO_SUSPENDIDO = 'S';
    private final static Character ESTADO_FINALIZADO = 'F';

    @JoinColumn(name = "id_responsable", referencedColumnName = "id")
    @ManyToOne
    private Usuario responsable;

    public Proyecto() {
        this.numero1="1";
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

    public String getNumero1() {
        return numero1;
    }

    public void setNumero1(String numero1) {
        this.numero1 = numero1;
    }

    public String getNumero2() {
        return numero2;
    }

    public void setNumero2(String numero2) {
        this.numero2 = numero2;
    }

    public String getNumero3() {
        return numero3;
    }

    public void setNumero3(String numero3) {
        this.numero3 = numero3;
    }

    public String getNumeroCompleto() {
        if (numero1 == null || numero1.isEmpty() || numero2 == null || numero2.isEmpty() || numero3 == null || numero3.isEmpty()) {
            return "";
        }
        return numero1 + "." + numero2 + "." + numero3;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
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
        if (!(object instanceof Proyecto)) {
            return false;
        }
        Proyecto other = (Proyecto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Proyecto[ id=" + id + " ]";
    }

    public String getEstadoDescripcion() {
        if (estado.equals(ESTADO_EN_EJECUCION)) {
            return "En Ejecución";
        } else if (estado.equals(ESTADO_SUSPENDIDO)) {
            return "Suspendido";
        } else if (estado.equals(ESTADO_FINALIZADO)) {
            return "Finalizado";
        } else {
            return "";
        }
    }
}
