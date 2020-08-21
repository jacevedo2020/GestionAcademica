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

/**
 *
 * @author landrade
 */
public class DatosHeader {

    private String fecha;
    private String fechaf;
    private String nonce;
    private String digest;
    private String usuario;

    public DatosHeader()
    {

    }

    public DatosHeader(String fecha,String nonce,String digest)
    {
        this.fecha = fecha;
        this.fechaf = fechaf;
        this.nonce = nonce;
        this.digest = digest;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFechaf() {
        return fechaf;
    }

    public void setFechaf(String fechaf) {
        this.fechaf = fechaf;
    }
    
    
    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    

}
