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
@FacesValidator("ValidadorNumeroAutorizacion")
public class ValidadorNumeroAutorizacion implements Validator {

    private static final String PATTERN = "[0-9]*";

    private final Pattern pattern;
    private Matcher matcher;

    public ValidadorNumeroAutorizacion() {
        pattern = Pattern.compile(PATTERN);
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        matcher = pattern.matcher(value.toString());
        if (value.toString().length() != 10 || !matcher.matches()) {
            String mensaje = "Error: Número de Autorización incorrecto.";
            if (!matcher.matches()){
                mensaje+= " Sólo se permiten números.";
            }else if (value.toString().length() != 10){
                mensaje+= " Debe tener 10 dígitos.";
            }
            FacesMessage msg = new FacesMessage(mensaje);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
