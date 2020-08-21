package com.jvc.medisys.validador;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public class Validador {

    /**
     * Numero de Provincias del Ecuador
     */
    public static final int NUMERO_DE_PROVINCIAS = 24;

    /**
     * Este método permite verificar si una cédula de identidad es verdadera
     * retorna true si es válida, caso contrario retorna false.
     *
     * @param cedula Cédula de Identidad Ecuatoriana de 10 digitos.
     * @return Si es verdadera true, si es falsa false
     */
    public void validarISSFA(FacesContext context, UIComponent validate, Object value) {
        String codISSFA = (String) value;

        // verifica que tenga 10 dígitos y que contenga solo valores numéricos
        if (!(((codISSFA.length() == 10) || (codISSFA.length() == 0)) && codISSFA.matches("^[0-9]{10}$"))) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("Codigo ISSFA invalido");
            context.addMessage(validate.getClientId(context), msg);
            return;
        } else {
            ((UIInput) validate).setValid(true);
        }
    }
    private boolean cedulaSiNo = false;

    public void validarCedula(FacesContext context, UIComponent validate, Object value) {
        String cedula = (String) value;
        if (!cedula.equals("")) {
            // verifica que tenga 10 dígitos y que contenga solo valores numéricos

            if (!((cedula.length() == 10) && cedula.matches("^[0-9]{10}$"))) {
                ((UIInput) validate).setValid(false);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Número de Cédula Inválido" , null);
                context.addMessage(validate.getClientId(context), msg);
                return;
            }
            if (cedula.substring(0, 2).equals("32") && !this.cedulaSiNo) {
                ((UIInput) validate).setValid(true);
                return;
            }


        // verifica que los dos primeros dígitos correspondan a un valor entre 1
            // y NUMERO_DE_PROVINCIAS
            int prov = Integer.parseInt(cedula.substring(0, 2));

            if (!((prov > 0) && (prov <= NUMERO_DE_PROVINCIAS))) {
                ((UIInput) validate).setValid(false);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Número de Cédula Inválido" , null);
                context.addMessage(validate.getClientId(context), msg);
                return;
            }

            // verifica que el último dígito de la cédula sea válido
            int[] d = new int[10];

            // Asignamos el string a un array
            for (int i = 0; i < d.length; i++) {
                d[i] = Integer.parseInt(cedula.charAt(i) + "");
            }

            int imp = 0;
            int par = 0;

            // sumamos los duplos de posición impar
            for (int i = 0; i < d.length; i += 2) {
                d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
                imp += d[i];
            }

            // sumamos los digitos de posición par
            for (int i = 1; i < (d.length - 1); i += 2) {
                par += d[i];
            }

            // Sumamos los dos resultados
            int suma = imp + par;

            // Restamos de la decena superior
            int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1) + "0") - suma;

            // Si es diez el décimo dígito es cero
            d10 = (d10 == 10) ? 0 : d10;

        // si el décimo dígito calculado es igual al digitado la cédula es
            // correcta
            if (d10 == d[9]) {
                ((UIInput) validate).setValid(true);
                return;
            } else {
                ((UIInput) validate).setValid(false);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Número de Cédula Inválido" , null);
                context.addMessage(validate.getClientId(context), msg);
                return;
            }
        }
    }

    // Compara fecha de nacimiento annio y mes menor que fecha actual
    public void compararFechaNacimiento(FacesContext context, UIComponent validate, Object value) {

        Date fechaInicio = (Date) value;

        Date fechaFin = new Date();
        boolean flag;
        //
        // java.util.Calendar c = java.util.Calendar.getInstance();
        // c.setTime(fechaInicio);
        //
        // int y1 = c.get(java.util.Calendar.YEAR);
        // int m1 = c.get(java.util.Calendar.MONTH + 1);
        // int d1 = c.get(java.util.Calendar.DAY_OF_MONTH);
        //
        // java.util.Calendar b = java.util.Calendar.getInstance();
        // b.setTime(fechaFin);
        //
        // int y2 = b.get(java.util.Calendar.YEAR);
        // int m2 = b.get(java.util.Calendar.MONTH + 1);
        // int d2 = b.get(java.util.Calendar.DAY_OF_MONTH);

        int comparacionFecha = fechaInicio.compareTo(fechaFin);

        if (comparacionFecha > 0) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("Fecha de nacimiento inválida.");

            context.addMessage(validate.getClientId(context), msg);
            flag = false;
        } else {
            ((UIInput) validate).setValid(true);
            flag = true;
        }
        return;// return flag;
    }

    public void valTexto(FacesContext context, UIComponent validate, Object value) {
        String texto = (String) value;
        texto = texto.toUpperCase();
        if (!(texto.matches("[^0-9]*"))) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("INGRESE SOLO LETRAS");
            context.addMessage(validate.getClientId(context), msg);
            return;// return false;
        }
        ((UIInput) validate).setValid(true);
        return; // return true;
    }

    public void valTelefono(FacesContext context, UIComponent validate, Object value) {
        String telefono = eliminacionMascaraTelefono((String) value);
        if (!(telefono.matches("\\d*\\d{6}"))) {

            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("Numero telefonico Invalido");
            context.addMessage(validate.getClientId(context), msg);
            return;// return false;
        }
        ((UIInput) validate).setValid(true);
        return; // return true;
    }

    public void valMayusculas(FacesContext context, UIComponent validate, Object value) {
        String texto = (String) value;
        if (!(texto.matches("[A-Z]*"))) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("SOLO LETRAS MAYUSCULAS");
            context.addMessage(validate.getClientId(context), msg);
            return;// return false;
        }
        ((UIInput) validate).setValid(false);
        return; // return true;
    }

    /**
     * @param context
     * @param validate
     * @param value
     * @author roberto
     * @category usada para validar el rango de fechas n ke se puede asignar un
     * turno
     *
     */
    public void valFechaTurnoUnAnio(FacesContext context, UIComponent validate, Object value) {
        Date fechaInicial = new Date();
        Date fechaFinal = (Date) value;
        int totDias = diferenciaEntre2Fechas(fechaInicial, fechaFinal);

        if (totDias > 360) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("Exede el rango de 1 año");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(validate.getClientId(context), msg);
            // return 1;
        } else if (totDias < 0) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("No se puede asignar a una fecha anterior al día de hoy");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(validate.getClientId(context), msg);
            // return -1;
        } else if ((totDias > 0) && (totDias < 360)) {
            // return 0;
        }
    }

    /**
     * @param fechaInicial
     * @param fechaFinal
     * @return numero de dias de diferencia entre 2 fechas
     * @author roberto
     */
    public int diferenciaEntre2Fechas(Date fechaInicial, Date fechaFinal) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);

        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String fechaFinalString = df.format(fechaFinal);

        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //long fechaInicialMs = fechaInicial.getTime();
        //long fechaFinalMs = fechaFinal.getTime();
        //Calendar calendarInicial = Calendar.getInstance();
        //Calendar calendarFinal = Calendar.getInstance();
        //calendarInicial.set(fechaInicial.getYear(), fechaInicial.getMonth(), fechaInicial.getDay());
        //calendarFinal.set(fechaFinal.getYear(), fechaFinal.getMonth(), fechaFinal.getDay());
        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        //int dias =  (int) daysBetween(calendarInicial, calendarFinal);
        return (int) dias;
    }

    /**
     * Using Calendar - THE CORRECT WAY*
     */
    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    // public static void main(String args[]){
    // Validador Obj = new Validador();
    // Calendar objCalendar = Calendar.getInstance();
    // objCalendar.set(Calendar.YEAR, 2010);
    // objCalendar.set(Calendar.MONTH, 10);
    // objCalendar.set(Calendar.DAY_OF_MONTH, 31);
    // System.out.println(objCalendar.getTime());
    // Date obj = new Date();
    // System.out.println(Obj.diferenciaEntre2Fechas(new Date(),
    // objCalendar.getTime()));
    // }
    /**
     * @param cadena
     * @return true si es una letra//palabra
     * @author roberto
     */
    public static boolean isString(String cadena) {
        // return cadena.toUpperCase().matches("[A-Z][a-zA-Z]*");
        return cadena.toUpperCase().matches("[^0-9]*");
    }

    /**
     * @param cadena
     * @return true si es numero
     * @author roberto
     */
    public static boolean isNumeric(String cadena) {
        try {
            Long.parseLong(cadena);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean esDecimal(String cadena) {
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String eliminacionMascaraTelefono(String telefonoMascara) {
        String cadenaSinMascara = "";
        if (telefonoMascara.startsWith("(")) {
            telefonoMascara = telefonoMascara.replace("(", "");
            telefonoMascara = telefonoMascara.replace(")", "");
            telefonoMascara = telefonoMascara.replace(" ", "");

            StringTokenizer tokens = new StringTokenizer(telefonoMascara, "-");
            cadenaSinMascara = tokens.nextToken();
            cadenaSinMascara += tokens.nextToken();
        } else {
            cadenaSinMascara = telefonoMascara;
        }
        return cadenaSinMascara;
    }
    private String mascaraTelefono = "javascript:return dFilter (event.keyCode, this, '(##) ####-###');"
            + "var dFilterStep;"
            + "function dFilterStrip (dFilterTemp, dFilterMask){dFilterMask = replace(dFilterMask,\'#\',\'\');"
            + "for (dFilterStep = 0; dFilterStep < dFilterMask.length++; dFilterStep++)"
            + "{"
            + "dFilterTemp = replace(dFilterTemp,dFilterMask.substring(dFilterStep,dFilterStep+1),'');"
            + "}"
            + "return dFilterTemp;}"
            + "function dFilterMax (dFilterMask){"
            + "dFilterTemp = dFilterMask;"
            + "for (dFilterStep = 0; dFilterStep < (dFilterMask.length+1); dFilterStep++){"
            + "if (dFilterMask.charAt(dFilterStep)!=\'#\'){"
            + "dFilterTemp = replace(dFilterTemp,dFilterMask.charAt(dFilterStep),\'\');}"
            + "}"
            + "return dFilterTemp.length;}"
            + "function dFilter (key, textbox, dFilterMask){"
            + "dFilterNum = dFilterStrip(textbox.value, dFilterMask);"
            + "if (key==9){"
            + "return true;}"
            + "else if (key==8&&dFilterNum.length!=0){"
            + "dFilterNum = dFilterNum.substring(0,dFilterNum.length-1);}"
            + "else if ( ((key>47&&key<58)||(key>95&&key<106)) && dFilterNum.length<dFilterMax(dFilterMask) ){"
            + "dFilterNum=dFilterNum+String.fromCharCode(key);}" + "var dFilterFinal='';"
            + "for (dFilterStep = 0; dFilterStep < dFilterMask.length; dFilterStep++){"
            + "if (dFilterMask.charAt(dFilterStep)==\'#\'){" + "if (dFilterNum.length!=0){"
            + "dFilterFinal = dFilterFinal + dFilterNum.charAt(0);"
            + "dFilterNum = dFilterNum.substring(1,dFilterNum.length);}" + "else{"
            + "dFilterFinal = dFilterFinal + \"\";}}" + "else if (dFilterMask.charAt(dFilterStep)!=\'#\'){"
            + "dFilterFinal = dFilterFinal + dFilterMask.charAt(dFilterStep);}}"
            + "textbox.value = dFilterFinal;"
            + "return false;}"
            + "function replace(fullString,text,by) {"
            + // Replaces text with by in string
            "var strLength = fullString.length, txtLength = text.length;"
            + "if ((strLength == 0) || (txtLength == 0)) return fullString;"
            + "var i = fullString.indexOf(text);"
            + "if ((!i) && (text != fullString.substring(0,txtLength))) return fullString;"
            + "if (i == -1) return fullString;" + "var newstr = fullString.substring(0,i) + by;"
            + "if (i+txtLength < strLength)"
            + "newstr += replace(fullString.substring(i+txtLength,strLength),text,by);"
            + "return newstr;}";
    // forma de uso
    // onkeypress="#{fichaPerinatal.validador.validarNumeros};"
    private String validarNumeros = "javascript:return validarCodigo(event);" + "function validarCodigo(e)"
            + "{" + "    var key=(document.all) ? e.keyCode : e.which;" + "	if(key == 0 || key == 8)"
            + "		return true;" + "        if ((key < 48 || key > 57))" + "        {"
            + "            return false;" + "        }" + "}";
    // forma de uso
    // onkeypress="javascript:return maskInputTo(this,4,2,event); #{fichaPerinatal.validador.mascaraDecimales};"
    private String mascaraDecimales = ""
            + "function maskInputTo(inputElement, maxLength, decimalPlaces,e) {"
            + "var i;"
            + "var key=(document.all) ? e.keyCode : e.which;"
            + // modificado por alberto garcia para incluir a la coma
            // "var exceptions = [ 0 , 8 , 46 , 37 , 39 , 13 , 9 ] ;"+
            "var exceptions = [ 0 , 8 , 44 , 46 , 37 , 39 , 13 , 9 ] ;" + "var isException=false;"
            + "var isDot=((190==key)&&(new String(inputElement.value).indexOf(\".\") <=0)) ;"
            + "var k=String.fromCharCode(key);	" + "var sel, rng, r2, curPos=-1;"
            + "if(typeof inputElement.selectionStart==\"number\") {" + "	curPos=inputElement.selectionStart;"
            + "} else if(document.selection && inputElement.createTextRange) {" + "	sel=document.selection;"
            + "	if(sel) {" + "		r2=sel.createRange();" + "		rng=inputElement.createTextRange();"
            + "		rng.setEndPoint(\"EndToStart\", r2);" + "		curPos=rng.text.length;" + "	}" + "}"
            + "for(i=0;i < exceptions.length;i++)" + "  if(exceptions[i]==key)" + "	 isException=true;"
            + "if(isNaN(k) && (!isException) && (!isDot))" + "	return false;" + "else {"
            + "	var p=new String(inputElement.value+k).indexOf(\".\");"
            + "	if(((p<inputElement.value.length-decimalPlaces && curPos>p) || isDot) && p>-1 && (!isException))"
            + "		return false;"
            + "	else if(inputElement.value.length>=((p>-1)||isDot?maxLength+1:maxLength) && (!isException))"
            + "		return false;" + "	else if (decimalPlaces==0 && isDot)" + "		return false;" + "}" + "}";

    // Validacion: sviOcular
    public void validarSviOcular(FacesContext context, UIComponent validate, Object value) {
        Long valor = (Long) value;

        if (valor < 0 || valor > 4) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("El rango de Ocular debe ser de 0 a 4.");

            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(validate.getClientId(context), msg);
            // return 1;
        }
    }

    public void validarSviVerbal(FacesContext context, UIComponent validate, Object value) {
        Long valor = (Long) value;

        if (valor < 0 || valor > 5) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("El rango de Verbal debe ser de 0 a 5.");

            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(validate.getClientId(context), msg);
            // return 1;
        }
    }

    public void validarSviMotora(FacesContext context, UIComponent validate, Object value) {
        Long valor = (Long) value;

        if (valor < 0 || valor > 6) {
            ((UIInput) validate).setValid(false);
            FacesMessage msg = new FacesMessage("El rango de Motora debe ser de 0 a 6.");

            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage(validate.getClientId(context), msg);
            // return 1;
        }
    }

    // Getters y setters
    public void setMascaraTelefono(String mascaraTelefono) {
        this.mascaraTelefono = mascaraTelefono;
    }

    public String getMascaraTelefono() {
        return this.mascaraTelefono;
    }

    public void setValidarNumeros(String validarNumeros) {
        this.validarNumeros = validarNumeros;
    }

    public String getValidarNumeros() {
        return this.validarNumeros;
    }

    public void setMascaraDecimales(String mascaraDecimales) {
        this.mascaraDecimales = mascaraDecimales;
    }

    public String getMascaraDecimales() {
        return this.mascaraDecimales;
    }

    public void setCedulaSiNo(boolean cedulaSiNo) {
        this.cedulaSiNo = cedulaSiNo;
    }

    public boolean isCedulaSiNo() {
        return this.cedulaSiNo;
    }
}
