package ec.gob.ambiente.client;


import java.math.BigInteger;
import javax.xml.ws.WebServiceRef;

/**
 * Hello world!
 *
 */
public class RegistroCedula
{

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/bsg.gob.ec/sw/RC/BSGSW01_Consultar_Cedula.wsdl")
   //private static WSRegistroCivilConsultaCedula_Service service = new WSRegistroCivilConsultaCedula_Service();

    public static void main(String[] args) {
        PermissionClient permissionClient = new PermissionClient();
        try { // Call Web Service Operation
            
            DatosHeader Headers= new DatosHeader();
            
            //AuxValidarPermisoRespuesta validarPermiso = new AuxValidarPermisoRespuesta();
            Headers = permissionClient.GeneraToken("");

            
	ec.gob.registrocivil.ConsultaCiudadano_Service service = new ec.gob.registrocivil.ConsultaCiudadano_Service();
           HeaderHandlerResolver handlerResolver=new HeaderHandlerResolver(Headers);
            service.setHandlerResolver(handlerResolver); 	
                ec.gob.registrocivil.ConsultaCiudadano port = service.getConsultaCiudadanoPort();
	 // TODO initialize WS operation arguments here
                
	java.lang.String codigoInstitucion = "3";  //La claves que se les entrego
	java.lang.String codigoAgencia = "224";
	java.lang.String usuario = "espeinn1";
	java.lang.String contrasenia = "In0v@reG%3";
	java.lang.String nui = "0301514816";
	// TODO process result here
	ec.gob.registrocivil.Ciudadano result = port.busquedaPorNui(codigoInstitucion, codigoAgencia, usuario, contrasenia, nui);
	System.out.println("Result = "+result.getNombre());     	    
            
            
            
        } catch (Exception ex) {
            // TODO handle custom exceptions here
            System.out.println("  --> "+ex.getMessage());
            ex.printStackTrace();
        }      
        

    }

}


