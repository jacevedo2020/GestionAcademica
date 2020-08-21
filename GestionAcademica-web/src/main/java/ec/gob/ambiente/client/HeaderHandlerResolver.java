/*

*
* Copyright (C) 2013 Libreria para Cliente Consulta Títulos development team.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
******************************************************
*  SUBSECRETARIA DE TECNOLOGIAS DE LA INFORMACION
*   DIRECCION DE INTEROPERABILIDAD GUBERNAMENTAL
*
* FECHA CREACION : 02 -02-2013
* AUTHOR: DIG DIRECCION DE INTEROPERABILIDAD GUBERNAMENTAL
* PROGRAMADOR:LUIGGI ANDRADE 
* CONCEPTO   :  Consulta De Titulos de Ciudadanos
* Utilización del servicio web de la senascyt  
*
*
* MODIFICACION (Fecha, Autor y la descripción de la modificación)
*
*
*******************************************************

 */

package ec.gob.ambiente.client;


import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;


/**
 *
 * @author landrade
 */
public class HeaderHandlerResolver implements HandlerResolver {
    
DatosHeader headerData= new DatosHeader();    
   
public HeaderHandlerResolver(DatosHeader HeaderList){
    headerData.setDigest(HeaderList.getDigest());
    headerData.setNonce(HeaderList.getNonce());
    headerData.setFecha(HeaderList.getFecha());
    headerData.setFechaf(HeaderList.getFechaf());
    headerData.setUsuario(HeaderList.getUsuario());
}    
    
public List<Handler> getHandlerChain(PortInfo portInfo) {
      List<Handler> handlerChain = new ArrayList<Handler>();

      HeaderHandler hh = new HeaderHandler(headerData);

      handlerChain.add(hh);

      return handlerChain;
   }
}
