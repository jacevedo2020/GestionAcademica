/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.ambiente.client;

import ec.gob.bsg.accesobsgservice.AccesoBSGService;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author jruales
 */
public class PermissionClient {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/www.bsg.gob.ec/sw/STI/BSGSW08_Acceder_BSG.wsdl")
    private AccesoBSGService service = new AccesoBSGService();

    public DatosHeader GeneraToken(String cedula) {
        try { // Call Web Service Operation
            
            DatosHeader Headers = new DatosHeader();
            
            ec.gob.bsg.accesobsgservice.AccesoBSGService service = new ec.gob.bsg.accesobsgservice.AccesoBSGService();
            ec.gob.bsg.accesobsgservice.BSG04AccederBSG port = service.getBSG04AccederBSGPort();
            // TODO initialize WS operation arguments here
            ec.gob.bsg.accesobsgservice.ValidarPermisoPeticion validarPermisoPeticion  = new ec.gob.bsg.accesobsgservice.ValidarPermisoPeticion();
            // TODO process result here
            validarPermisoPeticion.setCedula(cedula);//La cedula que les proporcionamos
            validarPermisoPeticion.setUrlsw("https://www.bsg.gob.ec/sw/RC/BSGSW03_Consultar_Ciudadano?wsdl");
            
            ec.gob.bsg.accesobsgservice.ValidarPermisoRespuesta result = port.validarPermiso(validarPermisoPeticion);
	

            if (!result.getMensaje().getCodError().equals("000"))
            {
                System.out.println(result.getMensaje().getDesError());
                Headers.setNonce(result.getMensaje().getDesError());
                Headers.setDigest(result.getMensaje().getDesError());
                Headers.setFecha(result.getMensaje().getDesError());
                Headers.setFechaf(result.getMensaje().getDesError());
            }
            else
            {  
                
                Headers.setNonce(result.getNonce());
                Headers.setDigest(result.getDigest());
                Headers.setFecha(result.getFecha());
                Headers.setFechaf(result.getFechaF());
                Headers.setUsuario(cedula);//La cedula que les proporcionamos
            }
            System.out.println("Acceso = " + result.getMensaje().getDesError());
            return Headers;
        } catch (Exception ex) {
            System.out.println("  ERROR "+ex.getMessage());
            ex.printStackTrace();
    // TODO handle custom exceptions here
            return null;
        }
    }

}
