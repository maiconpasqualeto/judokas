/**
 * 
 */
package br.com.sixinf.webtracker.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author maicon
 *
 */
@FacesValidator(value="senhaValidator")
public class SenhaValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		UIInput senha = (UIInput) component.getParent().findComponent("txtSenha");
		String confirmaSenha = value.toString();
		Object objSenha = senha.getSubmittedValue();
		if (objSenha == null)
			objSenha = senha.getLocalValue();
		String txtSenha = objSenha.toString();
		
		if (!txtSenha.equals(confirmaSenha)){
			FacesMessage msg = new FacesMessage("Senhas não conferem");

			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
	}

}
