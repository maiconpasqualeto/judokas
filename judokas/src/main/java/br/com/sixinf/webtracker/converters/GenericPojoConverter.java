/**
 * 
 */
package br.com.sixinf.webtracker.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@FacesConverter(value="genericPojoConverter")
public class GenericPojoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		if (value != null && !value.isEmpty())
			return component.getAttributes().get(value);			
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		if (value == null)
			return "";
		
		Entidade e = (Entidade) value;
		
		component.getAttributes().put(e.getIdentificacao().toString(), value);
		
		return e.getIdentificacao().toString();
	}

}
