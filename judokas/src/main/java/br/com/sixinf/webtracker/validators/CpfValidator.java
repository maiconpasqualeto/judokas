/**
 * 
 */
package br.com.sixinf.webtracker.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.sixinf.ferramentas.Utilitarios;

/**
 * @author maicon
 *
 */
@FacesValidator(value="cpfValidator")
public class CpfValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String cpf = value.toString();
		cpf = Utilitarios.removeMascara(cpf);
		if (!Utilitarios.validaCpf(cpf)) {
			FacesMessage msg = new FacesMessage("o CPF digitado não é inválido");

			msg.setSeverity(FacesMessage.SEVERITY_ERROR);

			throw new ValidatorException(msg);
		}
			
	}

}
