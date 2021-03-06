/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.entidades.Atleta;
import br.com.sixinf.judokas.entidades.Usuario;
import br.com.sixinf.judokas.facade.JudokasFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="impressaoBean")
@ViewScoped
public class ImpressaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	//private static final Logger LOG = Logger.getLogger(ImpressaoBean.class);
	
	private DualListModel<Atleta> atletas = new DualListModel<Atleta>();
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private Usuario usuarioAcademia;
	private Date data;
	
	public DualListModel<Atleta> getAtletas() {
		return atletas;
	}

	public void setAtletas(DualListModel<Atleta> atletas) {
		this.atletas = atletas;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario getUsuarioAcademia() {
		return usuarioAcademia;
	}

	public void setUsuarioAcademia(Usuario usuarioAcademia) {
		this.usuarioAcademia = usuarioAcademia;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		try {
			JudokasFacade facade = JudokasFacade.getInstance();
			List<Atleta> source = new ArrayList<Atleta>();
			List<Atleta> target = new ArrayList<Atleta>();
			atletas = new DualListModel<Atleta>(source, target);
			usuarios = facade.buscarUsuariosACADEMIA();
			
		} catch (LoggerException e) {
			Logger.getLogger(ImpressaoBean.class).error("Erro no método init", e);
		}
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public void onTransfer(TransferEvent event){
		
	}
	
	
	public StreamedContent printReport() throws JRException, IOException, ClassNotFoundException, SQLException, LoggerException {  
		
		String academia = null;
		if (usuarioAcademia != null)
			academia = usuarioAcademia.getNome().replace(' ', '_');
		
		StreamedContent retorno = 
				JudokasFacade.getInstance().geraReportCarteirinhas(
						academia, atletas.getTarget());
		
		atletas.getTarget().clear();
		
		return retorno;
    }
	
	
	
	/**
	 * @throws LoggerException data)(
	 * 
	 */
	public void buscarAtletas() throws LoggerException {
		atletas.setSource(JudokasFacade.getInstance().buscarAtletasAcademia(usuarioAcademia.getId()));
		
	}
	
	/**
	 * @throws LoggerException 
	 * 
	 */
	public StreamedContent geraCarteirinhasEmitidas() throws LoggerException{
		return JudokasFacade.getInstance().geraReportCarteirinhasEmitidas();
	}
	
	/**
	 * @throws LoggerException 
	 * 
	 */
	public StreamedContent geraCarteirinhasEmitidasDia() throws LoggerException{
		return JudokasFacade.getInstance().geraReportCarteirinhasEmitidasDia(data);
	}
}
