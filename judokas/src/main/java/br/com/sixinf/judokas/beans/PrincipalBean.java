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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.dao.JudokasDAO;
import br.com.sixinf.judokas.entidades.Atleta;
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
	
	private List<Atleta> atletasCadastrados = new ArrayList<Atleta>();
	private DualListModel<Atleta> atletasPrint = new DualListModel<Atleta>();
	
	@PostConstruct
	public void init() {
		try {
			
			atletasCadastrados = JudokasDAO.getInstance().buscarAtletasNovos();
			atletasPrint.setSource(atletasCadastrados);
			
		} catch (LoggerException e) {
			Logger.getLogger(PrincipalBean.class).error("Erro no método init", e);
		}
	}

	public List<Atleta> getAtletasCadastrados() {
		return atletasCadastrados;
	}

	public void setAtletasCadastrados(List<Atleta> atletasCadastrados) {
		this.atletasCadastrados = atletasCadastrados;
	}

	public DualListModel<Atleta> getAtletasPrint() {
		return atletasPrint;
	}

	public void setAtletasPrint(DualListModel<Atleta> atletasPrint) {
		this.atletasPrint = atletasPrint;
	}

	
	public StreamedContent printReport() throws JRException, IOException, ClassNotFoundException, SQLException, LoggerException {  
		StreamedContent retorno = JudokasFacade.getInstance().geraReportCarteirinhas(atletasPrint.getTarget());
		
		init();
		
		return retorno; 
    }
	
}
