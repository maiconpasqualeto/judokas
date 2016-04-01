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

/**
 * @author maicon
 *
 */
@FacesValidator(value="nomeValidator")
public class NomeValidator implements Validator {

	/**
	 * 
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String nome = (String) value;
		String[] partes = nome.split(" ");
		for (String parte : partes) {
			if ((parte.length() == 1 ||
					parte.contains(".") ||
					parte.contains(",") ) &&
					!parte.toLowerCase().equals("e") )
				throw new ValidatorException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, 
								"O Nome não pode ser abreviado",
									"O Nome não pode ser abreviado"));
		}
	}

}
