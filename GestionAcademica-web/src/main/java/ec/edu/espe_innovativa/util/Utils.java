package ec.edu.espe_innovativa.util;

import java.util.Random;

public class Utils {

    public static String generarPassword() {
        String password = "";
        int minimo = 65;
        int maximo = 90;
        for (int i = 0; i < 3; i++) {
            Random r = new Random();
            int letra = r.ints(minimo, (maximo + 1)).limit(1).findFirst().getAsInt();
            password += (char) letra;
        }
        minimo = 48;
        maximo = 57;
        for (int i = 0; i < 3; i++) {
            Random r = new Random();
            int letra = r.ints(minimo, (maximo + 1)).limit(1).findFirst().getAsInt();
            password += (char) letra;
        }
        return password;
    }
        public static String depurarNombreDocumento(String nombre){
        String result = nombre;
        result= result.replace("á", "a");
        result= result.replace("é", "e");
        result= result.replace("í", "i");
        result= result.replace("ó", "o");
        result= result.replace("ú", "u");
        result= result.replace("Á", "A");
        result= result.replace("É", "E");
        result= result.replace("Í", "I");
        result= result.replace("Ó", "O");
        result= result.replace("Ú", "U");
        result= result.replace("ñ", "n");
        result= result.replace("Ñ", "N");
        return result;
    }

}
