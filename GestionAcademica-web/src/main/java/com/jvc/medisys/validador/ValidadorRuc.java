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
@FacesValidator("ValidadorRuc")
public class ValidadorRuc implements Validator {

    private static final String RUC_PATTERN = "[0-9]*";

    private final Pattern pattern;
    private Matcher matcher;

    public ValidadorRuc() {
        pattern = Pattern.compile(RUC_PATTERN);
    }

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        matcher = pattern.matcher(value.toString());
        if (value.toString().length() != 13 || !matcher.matches() || !ValCedulaRuc.validarCedulaRuc(value.toString())) {
            String mensaje = "Error: " + FacesUtils.getResourceBundle().getString("rucIncorrecto") + ".";
            if (!matcher.matches()) {
                mensaje += " Sólo se permiten números.";
            }
            if (value.toString().length() != 13) {
                mensaje += " Debe tener 13 dígitos.";
            }
            FacesMessage msg = new FacesMessage(mensaje);
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

    }
}
