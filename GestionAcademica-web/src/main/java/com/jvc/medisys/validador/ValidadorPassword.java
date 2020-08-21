package com.jvc.medisys.validador;

import com.jvc.medisys.icefacesUtil.FacesUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Danny
 */
@FacesValidator("ValidadorPassword")
public class ValidadorPassword implements Validator {

    public ValidadorPassword() {
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {

        String password = value.toString();
        String mensaje = "";
        if (password.length() < 5) {
            mensaje += " Debe tener al menos 5 caracteres.";
        }

        //Validar que tenga al menos un numero
        boolean encontro = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                encontro = true;
                break;
            }
        }
        if (!encontro) {
            mensaje += " Debe tener al menos 1 nómero.";
        }

        //Validar que tenga al menos una letra
        encontro = false;
        for (char c : password.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                encontro = true;
                break;
            }
        }
        if (!encontro) {
            mensaje += " Debe tener al menos 1 letra.";
        }
        if (!mensaje.isEmpty()) {
            FacesMessage msg = new FacesMessage("Error: " + FacesUtils.getResourceBundle().getString("passwordIncorrecto") + mensaje);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);

        }

    }

}
