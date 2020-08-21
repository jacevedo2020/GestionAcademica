package com.jvc.medisys.validador;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


/**
 * @author roberto
 *
 */
@FacesValidator(value = "phoneNumberValidator")
public class PhoneNumberValidator implements Validator{
	
	public PhoneNumberValidator() {
    }
	
    private static final String PHONE_NUM= "[(][0-9]{2}[)][0-9]{4}[-]{1}[0-9]{3}";

    public void validate(FacesContext context, UIComponent component, Object value){
        /*create a mask
         */
        Pattern mask = Pattern.compile(PHONE_NUM);
        
        try {
        	if(value.toString().length()==9){
        		value = "("+value.toString().substring(0,2)+")"+""+value.toString().substring(2,6)+"-"+value.toString().substring(6,9);
        	}
		} catch (Exception e) {
		}
        
        String phoneNumber = (String)value;
        
        Matcher matcher = mask.matcher(phoneNumber);
        
        if(!matcher.matches()){
            FacesMessage message = new FacesMessage();
            message.setDetail("Use el formato (00)0000-000");
            message.setSummary("El número de teléfono esta incorrecto");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
