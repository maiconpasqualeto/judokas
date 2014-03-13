/**
 * 
 */
package br.com.sixinf.webtracker.converters;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.com.sixinf.judokas.entidades.Atleta;

/**
 * @author maicon
 *
 */
@FacesConverter(value="atletaConverter")
public class AtletaConverter implements Converter {
	
	private AtletaComparator ac = new AtletaComparator();

	@Override
	public Object getAsObject(FacesContext context, UIComponent comp,
			String value) {
		
		if (value != null) {
			Atleta a = new Atleta();
			a.setId(Long.valueOf(value));
			
			@SuppressWarnings("unchecked")
			DualListModel<Atleta> dualList = (DualListModel<Atleta>) ((PickList) comp).getValue();  
			List<Atleta> midiasSource = dualList.getSource();
			Collections.sort(midiasSource, ac);
			int idx = Collections.binarySearch(midiasSource, a, ac);
			if(idx < 0) {
				List<Atleta> midiasTarget = dualList.getTarget();
				Collections.sort(midiasTarget, ac);
				idx = Collections.binarySearch(midiasTarget, a, ac);
				
				a = midiasTarget.get(idx);
			} else {
				a = midiasSource.get(idx);
			}
			
			return a;
		}
		
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		if (value != null) {
			Atleta a = (Atleta) value;
			return a.getId().toString();
		}
		
		return null;
	}
	
	private class AtletaComparator implements Comparator<Atleta> {
		
		@Override
		public int compare(Atleta a1, Atleta a2) {
			return a1.getId().compareTo(a2.getId());
		}
	}

}
