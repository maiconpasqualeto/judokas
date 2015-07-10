/**
 * 
 */
package br.com.sixinf.judokas;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.sixinf.judokas.dao.JudokasDAO;
import br.com.sixinf.judokas.entidades.Atleta;

/**
 * @author maicon
 *
 */
public class AtletasImprimirLazyList extends LazyDataModel<Atleta> {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Atleta> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		
		JudokasDAO dao = JudokasDAO.getInstance();
		
		if (sortField == null)
			sortField = "id";
		
		List<Atleta> atletas = dao.buscarAtletasImprimirPaginado(
				first, first + pageSize, sortField, sortOrder, filters);
		
		this.setRowCount(dao.buscarCountAtletasPaginado(filters).intValue());
		
		return atletas;
		
	}
}
