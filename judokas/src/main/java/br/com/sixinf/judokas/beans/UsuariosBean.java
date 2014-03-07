/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;
import br.com.sixinf.judokas.facade.JudokasFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="usuariosBean")
@ViewScoped
public class UsuariosBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	private List<Usuario> usuarios = new ArrayList<Usuario>(0);
	private List<String> tiposUsuario = new ArrayList<String>(0);
	private boolean renderTipoUsuario;
	
	public UsuariosBean() {
		tiposUsuario.add(TipoUsuario.ACADEMIA.name());
		tiposUsuario.add(TipoUsuario.MASTER.name());
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<String> getTiposUsuario() {
		return tiposUsuario;
	}

	public void setTiposUsuario(List<String> tiposUsuario) {
		this.tiposUsuario = tiposUsuario;
	}

	public boolean isRenderTipoUsuario() {
		return renderTipoUsuario;
	}

	public void setRenderTipoUsuario(boolean renderTipoUsuario) {
		this.renderTipoUsuario = renderTipoUsuario;
	}

	/**
	 * @throws LoggerException 
	 * 
	 */
	@PostConstruct
	public void init() {
		try {
			usuarios = JudokasFacade.getInstance().buscarUsuarios();
			usuario = new Usuario();
		} catch (LoggerException e) {
			Logger.getLogger(UsuariosBean.class).error("Erro no método init", e);
		}
	}
	
	/**
	 * 
	 * @param u
	 */
	public void editar(Usuario u) {
		usuario = u;
		renderTipoUsuario = false;
	}
	
	/**
	 * 
	 * @param u
	 * @throws LoggerException 
	 */
	public void delete(Usuario u) throws LoggerException {
		JudokasFacade.getInstance().apagarUsuario(u);
		init();
		
		FacesMessage m = new FacesMessage("Registro excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, m);
	}
	
	/**
	 * 
	 */
	public void novo(){
		usuario = new Usuario();
		renderTipoUsuario = true;
	}
	
	/**
	 * @throws LoggerException 
	 * 
	 */
	public void salvarEFechar() throws LoggerException {
		salvar();
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("fechaDialog", true);
	}
	
	/**
	 * @throws LoggerException 
	 * 
	 */
	public void salvar() throws LoggerException{
		JudokasFacade.getInstance().salvarUsuario(usuario);
		
		init();		
		
		FacesMessage m = new FacesMessage("Registro salvo com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, m);
	}
	
	public void salvarENovo() throws LoggerException{
		salvar();
		
		renderTipoUsuario = true;
	}

	/**
	 * @throws LoggerException 
	 * 
	 */
	public void resetarSenha() throws LoggerException {
		JudokasFacade.getInstance().resetarSenha(usuario);
		
		FacesMessage m = new FacesMessage("A senha foi resetada!");
		FacesContext.getCurrentInstance().addMessage(null, m);
	}
	
}
