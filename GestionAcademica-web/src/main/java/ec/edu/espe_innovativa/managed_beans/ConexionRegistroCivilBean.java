package ec.edu.espe_innovativa.managed_beans;

import ec.edu.espe_innovativa.entity_bean.Canton;
import ec.edu.espe_innovativa.entity_bean.Parametro;
import ec.edu.espe_innovativa.entity_bean.Parroquia;
import ec.edu.espe_innovativa.entity_bean.Persona;
import ec.edu.espe_innovativa.entity_bean.Provincia;
import ec.edu.espe_innovativa.session_bean.ParametroFacadeLocal;
import ec.gob.ambiente.client.DatosHeader;
import ec.gob.ambiente.client.HeaderHandlerResolver;
import ec.gob.ambiente.client.PermissionClient;
import ec.gob.registrocivil.ConsultaCiudadano;
import ec.gob.registrocivil.ConsultaCiudadano_Service;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Named(value = "conexionRegistroCivilBean")
@ApplicationScoped
public class ConexionRegistroCivilBean {

    @EJB
    private ParametroFacadeLocal parametroFacadeLocal;
    @Inject
    private ProvinciaBean provinciaBean;

    private List<Parametro> parametroList;

    public ConexionRegistroCivilBean() {
    }

    @PostConstruct
    public void init() {
        parametroList = parametroFacadeLocal.findAll();
    }

    public Persona consultarPersona(String cedula) {
        PermissionClient permissionClient = new PermissionClient();
        DatosHeader Headers = new DatosHeader();
        String cedulaRegCivil = getParametro(Parametro.ID_WEB_SERVICE_REG_CIVIL_CEDULA);
        Headers = permissionClient.GeneraToken(cedulaRegCivil);

        ConsultaCiudadano_Service service = new ConsultaCiudadano_Service();
        HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver(Headers);
        service.setHandlerResolver(handlerResolver);
        ConsultaCiudadano port = service.getConsultaCiudadanoPort();

        String codigoInstitucion = getParametro(Parametro.ID_WEB_SERVICE_REG_CIVIL_CODIGO_INSTITUCION);
        String codigoAgencia = getParametro(Parametro.ID_WEB_SERVICE_REG_CIVIL_CODIGO_AGENCIA);
        String usuario = getParametro(Parametro.ID_WEB_SERVICE_REG_CIVIL_USUARIO);
        String contrasenia = getParametro(Parametro.ID_WEB_SERVICE_REG_CIVIL_PASSWORD);;
        ec.gob.registrocivil.Ciudadano result = port.busquedaPorNui(codigoInstitucion, codigoAgencia, usuario, contrasenia, cedula);
        if (result.getCodigoError().equals("000")) {
            Persona persona = new Persona();
            persona.setIdentificacion(cedula);
            try {
                persona.setFechaNacimiento(new SimpleDateFormat("dd/MM/yyyy").parse(result.getFechaNacimiento()));
            } catch (Exception e) {
            }
            if (result.getSexo() != null) {
                if (result.getSexo().equals("HOMBRE")) {
                    persona.setGenero(Persona.GENERO_MASCULINO);
                }
                if (result.getSexo().equals("MUJER")) {
                    persona.setGenero(Persona.GENERO_FEMENINO);
                }
            }
            persona.setNombresCompletos(result.getNombre());
            String direccion = "";
            if (result.getCalle() != null) {
                direccion = result.getCalle();
            }
            if (result.getNumeroCasa() != null) {
                direccion += result.getNumeroCasa();
            }
            if (!direccion.isEmpty()) {
                persona.setDireccion(direccion);
            }
            persona.setParroquia(getParroquia(result.getDomicilio()));
            return persona;
        }
        return null;
    }

    private String getParametro(Integer id) {
        for (Parametro p : parametroList) {
            if (p.getId() == id) {
                return p.getValor();
            }
        }
        return null;
    }

    private Parroquia getParroquia(String cadena) {
        try {
            StringTokenizer st = new StringTokenizer(cadena, "/");
            String nomProvincia = st.nextToken().toUpperCase();
            String nomCanton = st.nextToken().toUpperCase();
            String nomParroquia = st.nextToken().toUpperCase();
            for (Parroquia parr : provinciaBean.getParroquiaList()) {
                if (parr.getNombre().toUpperCase().equals(nomParroquia)) {
                    return parr;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
