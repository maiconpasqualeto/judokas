/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.AtletasImprimirLazyList;
import br.com.sixinf.judokas.dao.JudokasDAO;
import br.com.sixinf.judokas.entidades.Atleta;
import br.com.sixinf.judokas.entidades.Usuario;
import br.com.sixinf.judokas.facade.JudokasFacade;

/**
 * @author maicon
 *
 */
@ManagedBean
@ViewScoped
public class PrincipalBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private List<Atleta> atletasCadastrados = new ArrayList<Atleta>();
	private DualListModel<Atleta> atletasPrint = new DualListModel<Atleta>();
	private Usuario usuarioAcademia;
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private LazyDataModel<Atleta> dataModel;
	
	@PostConstruct
	public void init() {
		try {
			
			List<Atleta> atletasCadastrados = JudokasDAO.getInstance().buscarAtletasNovos();
			atletasPrint.setSource(atletasCadastrados);
			
			dataModel = new AtletasImprimirLazyList();
			
			usuarios = JudokasDAO.getInstance().buscarUsuarios();
			
		} catch (LoggerException e) {
			Logger.getLogger(PrincipalBean.class).error("Erro no método init", e);
		}
	}

	public DualListModel<Atleta> getAtletasPrint() {
		return atletasPrint;
	}

	public void setAtletasPrint(DualListModel<Atleta> atletasPrint) {
		this.atletasPrint = atletasPrint;
	}

	public Usuario getUsuarioAcademia() {
		return usuarioAcademia;
	}

	public void setUsuarioAcademia(Usuario usuarioAcademia) {
		this.usuarioAcademia = usuarioAcademia;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public LazyDataModel<Atleta> getDataModel() {
		return dataModel;
	}

	public void setDataModel(LazyDataModel<Atleta> dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws LoggerException
	 */
	public StreamedContent printReport() throws JRException, IOException, ClassNotFoundException, SQLException, LoggerException {  
		if (atletasPrint.getTarget().isEmpty()) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					"Nenhuma atleta selecionado", 
					"Nenhuma atleta selecionado");
			FacesContext.getCurrentInstance().addMessage(null, m);
			return null;
		}	
		
		String academia = null;
		if (usuarioAcademia != null)
			academia = usuarioAcademia.getNome().replace(' ', '_');
		
		StreamedContent retorno = 
				JudokasFacade.getInstance().geraReportCarteirinhas(
						academia, atletasPrint.getTarget());
		
		atletasPrint.getTarget().clear();
		
		//atletasCadastrados = JudokasDAO.getInstance().buscarAtletasNovos();
		
		return retorno; 
    }
	
	/**
	 * 
	 * @throws LoggerException
	 */
	public void buscarAtletas() throws LoggerException {
		if (usuarioAcademia != null) {
			atletasPrint.setSource(
					JudokasDAO.getInstance().buscarNovosAtletasDaAcademia(
							usuarioAcademia.getId()));
		} else {
			atletasPrint.setSource(
					JudokasDAO.getInstance().buscarAtletasNovos());
		}
		atletasPrint.getTarget().clear();
			
	}
		
}
