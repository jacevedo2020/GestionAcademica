package ec.edu.espe_innovativa.entity_bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "persona")
@XmlRootElement
public class Persona implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 60)
    @Column(name = "nombres_completos")
    private String nombresCompletos;
    @Size(max = 30)
    @Column(name = "nombres")
    private String nombres;
    @Size(max = 30)
    @Column(name = "apellidos")
    private String apellidos;
    @JoinColumn(name = "id_tipo_identificacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoIdentificacion tipoIdentificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona")
    private List<Usuario> usuarioList;

    @Column(name = "genero")
    private Character genero;
    public final static Character GENERO_FEMENINO = 'F';
    public final static Character GENERO_MASCULINO = 'M';

    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Size(max = 9)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 10)
    @Column(name = "celular")
    private String celular;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Error: Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "email")
    private String email;
    @JoinColumn(name = "direccion_id_parroquia", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Parroquia parroquia;
    @Size(max = 300)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 250)
    @Column(name = "direccion_referencia")
    private String direccionReferencia;
    @Size(max = 30)
    @Column(name = "contacto_nombres")
    private String contactoNombres;
    @Size(max = 30)
    @Column(name = "contacto_apellidos")
    private String contactoApellidos;
    @Size(max = 10)
    @Column(name = "contacto_telefono")
    private String contactoTelefono;
    @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Error: Correo electrónico no válido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "contacto_email")
    private String contactoEmail;
    @Column(name = "trabaja_actualmente")
    private Character trabajaActualmente;
    @Size(max = 50)
    @Column(name = "empresa_trabajo")
    private String empresaTrabajo;
    @Size(max = 300)
    @Column(name = "direccion_trabajo")
    private String direccionTrabajo;
    @Size(max = 10)
    @Column(name = "telefono_trabajo")
    private String telefonoTrabajo;
    @Column(name = "tiene_discapacidad")
    private Character tieneDiscapacidad;
    @Size(max = 100)
    @Column(name = "tipo_discapacidad")
    private String tipoDiscapacidad;

    @Transient
    private Provincia provincia;
    @Transient
    private Canton canton;

    public Persona() {
        tipoIdentificacion = new TipoIdentificacion(TipoIdentificacion.ID_TIPO_CEDULA);
    }

    public Persona(Integer id) {
        this.id = id;
    }

    public Persona(Integer id, String identificacion) {
        this.id = id;
        this.identificacion = identificacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    /*public String getNombresCompletos() {
        if (nombres == null) {
            return "";
        } else {
            return nombres + " " + apellidos;
        }
    }*/
    public String getNombresCompletosMasCedula() {
        if (nombresCompletos == null) {
            return identificacion;
        } else {
            return nombresCompletos + " - " + identificacion;
        }
    }

    public String getCedulaMasNombresCompletos() {
        if (nombresCompletos == null) {
            return identificacion;
        } else {
            return identificacion + " - " + nombresCompletos;
        }
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.espe_innovativa.entity_bean.Persona[ id=" + id + " ]";
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionReferencia() {
        return direccionReferencia;
    }

    public void setDireccionReferencia(String direccionReferencia) {
        this.direccionReferencia = direccionReferencia;
    }

    public String getContactoNombres() {
        return contactoNombres;
    }

    public void setContactoNombres(String contactoNombres) {
        this.contactoNombres = contactoNombres;
    }

    public String getContactoApellidos() {
        return contactoApellidos;
    }

    public void setContactoApellidos(String contactoApellidos) {
        this.contactoApellidos = contactoApellidos;
    }

    public String getContactoTelefono() {
        return contactoTelefono;
    }

    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }

    public String getDatosContacto() {
        String result = "";
        if (contactoNombres != null) {
            result += contactoNombres;
        }
        if (contactoApellidos != null) {
            if (!result.isEmpty()) {
                result += " ";
            }
            result += contactoApellidos;
        }
        if (contactoTelefono != null) {
            if (!result.isEmpty()) {
                result += " - ";
            }
            result += contactoTelefono;
        }
        return result;
    }

    public Parroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(Parroquia parroquia) {
        this.parroquia = parroquia;
    }

    public Character getTrabajaActualmente() {
        return trabajaActualmente;
    }

    public void setTrabajaActualmente(Character trabajaActualmente) {
        this.trabajaActualmente = trabajaActualmente;
    }

    public String getEmpresaTrabajo() {
        return empresaTrabajo;
    }

    public void setEmpresaTrabajo(String empresaTrabajo) {
        this.empresaTrabajo = empresaTrabajo;
    }

    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }

    public void setDireccionTrabajo(String direccionTrabajo) {
        this.direccionTrabajo = direccionTrabajo;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    public Character getTieneDiscapacidad() {
        return tieneDiscapacidad;
    }

    public void setTieneDiscapacidad(Character tieneDiscapacidad) {
        this.tieneDiscapacidad = tieneDiscapacidad;
    }

    public String getTipoDiscapacidad() {
        return tipoDiscapacidad;
    }

    public void setTipoDiscapacidad(String tipoDiscapacidad) {
        this.tipoDiscapacidad = tipoDiscapacidad;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public String getNombresCompletos() {
        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public String getNombreParaCertificado() {
        StringTokenizer st = new StringTokenizer(nombresCompletos, " ");
        String nombreFinal = "";
        while (st.hasMoreElements()) {
            String palabra = (String) st.nextElement();
            if (!nombreFinal.isEmpty()) {
                nombreFinal += " ";
            }
            for (int i = 0; i < palabra.length(); i++) {
                if (i == 0) {
                    nombreFinal += palabra.substring(i, i + 1).toUpperCase();
                } else {
                    nombreFinal += palabra.substring(i, i + 1).toLowerCase();
                }
            }
        }
        return nombreFinal;
    }

    public Integer getEdad() {
        LocalDate fechaNac = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(fechaNac, fechaActual);
        return periodo.getYears();
    }

    public String getProvinciaNombre() {
        if (parroquia != null) {
            return parroquia.getCanton().getProvincia().getNombre();
        }
        return "";
    }

    public String getCantonNombre() {
        if (parroquia != null) {
            return parroquia.getCanton().getNombre();
        }
        return "";
    }

    public String getParroquiaNombre() {
        if (parroquia != null) {
            return parroquia.getNombre();
        }
        return "";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isTrabajaActualmenteSi() {
        if (trabajaActualmente == null) {
            return false;
        }
        return trabajaActualmente == 'S';
    }

}
