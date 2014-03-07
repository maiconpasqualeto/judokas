/**
 * 
 */
package br.com.sixinf.webtracker.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author maicon
 *
 */
@FacesConverter(value="inputMaskConverter") 
public class InputMaskConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if(value != null && !value.isEmpty()) {  
			value = value.replaceAll("[- /.]", "");  
			value = value.replaceAll("[-()]", "");  
        }  
        return value;  
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return value.toString();  
	}
}
