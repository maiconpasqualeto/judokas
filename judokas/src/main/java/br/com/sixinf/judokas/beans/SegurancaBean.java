/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.JudokasHelper;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;
import br.com.sixinf.judokas.facade.JudokasFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="segurancaBean")
@SessionScoped
public class SegurancaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario = new Usuario();
	private boolean renderCadastro;
	private boolean renderAtletas;
	private boolean renderUsuarios;
	private boolean renderRelatorio;
	private boolean renderImpCarteiras;
	// quando usuário for Master mostra no topo da página
	private boolean renderTipoUsuario;
	
	// permissoes
	public static List<String> permissoesMaster = new ArrayList<String>();
	public static List<String> permissoesAdmin = new ArrayList<String>();
	public static List<String> permissoesUser = new ArrayList<String>();
	
	static {
		// MASTER
		permissoesMaster.add("principal.xhtml");
		permissoesMaster.add("atletas.xhtml");
		permissoesMaster.add("usuarios.xhtml");
		permissoesMaster.add("carteiras.xhtml");
		permissoesMaster.add("imprimir.xhtml");
		
		// ACADEMIA
		permissoesAdmin.add("principal.xhtml");
		permissoesAdmin.add("atletas.xhtml");
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isRenderCadastro() {
		return renderCadastro;
	}

	public void setRenderCadastro(boolean renderCadastro) {
		this.renderCadastro = renderCadastro;
	}

	public boolean isRenderAtletas() {
		return renderAtletas;
	}

	public void setRenderAtletas(boolean renderAtletas) {
		this.renderAtletas = renderAtletas;
	}
	
	public boolean isRenderUsuarios() {
		return renderUsuarios;
	}

	public void setRenderUsuarios(boolean renderUsuarios) {
		this.renderUsuarios = renderUsuarios;
	}

	public boolean isRenderRelatorio() {
		return renderRelatorio;
	}

	public void setRenderRelatorio(boolean renderRelatorio) {
		this.renderRelatorio = renderRelatorio;
	}

	public boolean isRenderImpCarteiras() {
		return renderImpCarteiras;
	}

	public void setRenderImpCarteiras(boolean renderImpCarteiras) {
		this.renderImpCarteiras = renderImpCarteiras;
	}

	public boolean isRenderTipoUsuario() {
		return renderTipoUsuario;
	}

	public void setRenderTipoUsuario(boolean renderTipoUsuario) {
		this.renderTipoUsuario = renderTipoUsuario;
	}

	/**
	 * 
	 * @return
	 * @throws LoggerException
	 */
	public String logar() throws LoggerException{
		JudokasFacade facade = JudokasFacade.getInstance();
		
		boolean loginValido = facade.logar(usuario);
		
		if ( loginValido ) {			
			
			if (usuario.getPrimeiroLogin()) {
				
				RequestContext.getCurrentInstance().addCallbackParam("abreDialog", true);
				
			} else {				
				HttpSession sess = JudokasHelper.getSession();
				sess.setAttribute(Usuario.SESSION_NOME_USUARIO, usuario.getNomeUsuario());
				sess.setAttribute(Usuario.SESSION_TIPO_USUARIO, usuario.getTipoUsuario());
				
				String pagina = "/pages/principal.xhtml?faces-redirect=true";
				
				if (usuario.getTipoUsuario().equals(TipoUsuario.MASTER.name())) {
					renderTipoUsuario = true;
					pagina = "/pages/imprimir.xhtml?faces-redirect=true";
				} else 
					renderTipoUsuario = false;
				
				carregaRenderMenusUsuario();
				
				return pagina;
			}
			
		} else {
			FacesMessage m = new FacesMessage(
				FacesMessage.SEVERITY_ERROR, 
				"Usuário ou senha inválidos.", 
				"Usuário ou senha inválidos.");
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
		return null;
	}
	
	public void salvarNovaSenha() throws LoggerException{
		JudokasFacade.getInstance().salvarNovaSenha(usuario);
		
		usuario.setSenha(null);
		
		FacesMessage m = new FacesMessage("Senha alterada com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, m);
		
		RequestContext.getCurrentInstance().addCallbackParam("fechaDialog", true);
	}
	
	public String logoff(){
		usuario = new Usuario();
		renderCadastro = false;
		renderAtletas = false;
		renderUsuarios = false;
		renderRelatorio = false;
		renderImpCarteiras = false;
		
		return "/pages/home.xhtml?faces-redirect=true";
	}
	
	public void carregaRenderMenusUsuario(){
		TipoUsuario t = TipoUsuario.valueOf(usuario.getTipoUsuario());
		switch (t){
		
		case MASTER:
			renderAtletas = true;
			renderUsuarios = true;
			
			renderImpCarteiras = true;
			break;
			
		case ACADEMIA:
			renderAtletas = true;
			renderUsuarios = false;
			
			renderImpCarteiras = false;
			break;
			
		}
		
		// MENU PAI, não esquecer
		// se não houver nenhum submenu, não renderiza o menu pai
		if (!renderAtletas && 
				!renderUsuarios)
			renderCadastro = false;
		else 
			renderCadastro = true;
		
		if (!renderImpCarteiras)
			renderRelatorio = false;
		else 
			renderRelatorio = true;
		
	}
		
	/**
	 * 
	 * @return
	 */
	public String getUsuarioSessao(){
		return JudokasHelper.getUsuarioSessao();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoUsuario(){
		return JudokasHelper.getTipoUsuarioSessao().name();
	}
}
