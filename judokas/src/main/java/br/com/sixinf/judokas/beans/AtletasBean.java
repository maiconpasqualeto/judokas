/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.JudokasHelper;
import br.com.sixinf.judokas.entidades.Atleta;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;
import br.com.sixinf.judokas.facade.JudokasFacade;

/**
 * @author maicon
 *
 */
@ManagedBean(name="atletasBean")
@ViewScoped
public class AtletasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PATH = System.getProperty("user.home") + "/fotos_judokas/";
	
	private Atleta atleta;
	private Atleta atletaSelecionado;
	private List<Atleta> atletas = new ArrayList<Atleta>(0);
	private List<String> graduacoes = new ArrayList<String>(0);
	private UploadedFile uploadedFile;  
	private StreamedContent foto;
	private boolean renderAcademias;
	private String nomeUsuarioAcademia;
	private List<Usuario> academias = new ArrayList<Usuario>(0);
	private List<String> funcoes = new ArrayList<String>(0);
	
	@ManagedProperty(value="#{segurancaBean}")
	private SegurancaBean segurancaBean;
	
	public AtletasBean() {
		graduacoes.add("BRANCA");
		graduacoes.add("BRANCA/CINZA");
		graduacoes.add("CINZA");
		graduacoes.add("CINZA/AZUL");
		graduacoes.add("AZUL");
		graduacoes.add("AZUL/AMARELA");
		graduacoes.add("AMARELA");
		graduacoes.add("AMARELA/LARANJA");
		graduacoes.add("LARANJA");
		graduacoes.add("VERDE");
		graduacoes.add("ROXA");
		graduacoes.add("MARROM");
		graduacoes.add("PRETA SHO-DAN");
		graduacoes.add("PRETA NI-DAN");
		graduacoes.add("PRETA SAN-DAN");
		graduacoes.add("PRETA YON-DAN");
		graduacoes.add("PRETA GO-DAN");
		graduacoes.add("VERMELHA E BRANCA ROKU-DAN");
		graduacoes.add("VERMELHA E BRANCA SHITI-DAN");
		graduacoes.add("VERMELHA E BRANCA RATI-DAN");
		graduacoes.add("VERMELHA KIU-DAN ");
		graduacoes.add("VERMELHA DIU-DAN");
		
		funcoes.add("Atleta");
		funcoes.add("Para-Atleta");
		funcoes.add("Árbitro");
		funcoes.add("Dirigente");
		funcoes.add("Professor");
		funcoes.add("Técnico");
		funcoes.add("Auxiliar Técnico");
		funcoes.add("Kodansha");
		funcoes.add("Outros");
	}
	
	public Atleta getAtleta() {
		return atleta;
	}
	
	public void setAtleta(Atleta atleta) {
		this.atleta = atleta;
	}
	
	public List<Atleta> getAtletas() {
		return atletas;
	}
	
	public void setAtletas(List<Atleta> atletas) {
		this.atletas = atletas;
	}

	public List<String> getGraduacoes() {
		return graduacoes;
	}

	public void setGraduacoes(List<String> graduacoes) {
		this.graduacoes = graduacoes;
	}
	
	public Atleta getAtletaSelecionado() {
		return atletaSelecionado;
	}

	public void setAtletaSelecionado(Atleta atletaSelecionado) {
		this.atletaSelecionado = atletaSelecionado;
	}

	public SegurancaBean getSegurancaBean() {
		return segurancaBean;
	}

	public void setSegurancaBean(SegurancaBean segurancaBean) {
		this.segurancaBean = segurancaBean;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public StreamedContent getFoto() {
	    return foto;
	}

	public void setFoto(StreamedContent foto) {
		this.foto = foto;
	}

	public boolean isRenderAcademias() {
		return renderAcademias;
	}

	public void setRenderAcademias(boolean renderAcademias) {
		this.renderAcademias = renderAcademias;
	}

	public List<Usuario> getAcademias() {
		return academias;
	}

	public void setAcademias(List<Usuario> academias) {
		this.academias = academias;
	}

	public String getNomeUsuarioAcademia() {
		return nomeUsuarioAcademia;
	}

	public void setNomeUsuarioAcademia(String nomeUsuarioAcademia) {
		this.nomeUsuarioAcademia = nomeUsuarioAcademia;
	}

	public List<String> getFuncoes() {
		return funcoes;
	}

	public void setFuncoes(List<String> funcoes) {
		this.funcoes = funcoes;
	}

	/**
	 * 
	 * @param event
	 *//*
	public void buscarCEP(ValueChangeEvent event){
		String cep = event.getNewValue().toString();
		atleta.setCep(cep);
		
		JudokasFacade.getInstance().buscarCEP(atleta);
	}*/
	
	@PostConstruct
	public void init() {
		try {
			atletas = JudokasFacade.getInstance().buscarAtletas();
			TipoUsuario tipo = JudokasHelper.getTipoUsuarioSessao();
			if (tipo.equals(TipoUsuario.MASTER)) {
				renderAcademias = true;
				academias = JudokasFacade.getInstance().buscarUsuariosACADEMIA();
			}
			atleta = new Atleta();
			nomeUsuarioAcademia = null;
		} catch (LoggerException e) {
			Logger.getLogger(AtletasBean.class).error("Erro no método init", e);
		}
	}
	
	public void salvarFecha() throws LoggerException {
		salvar();
		
		RequestContext.getCurrentInstance().addCallbackParam("fechaDialog", true);
	}
	
	public void salvar() throws LoggerException {
		
		TipoUsuario tipo = JudokasHelper.getTipoUsuarioSessao();
		String nomeUsuario = null;
		
		if (tipo.equals(TipoUsuario.ACADEMIA))			
			nomeUsuario = JudokasHelper.getUsuarioSessao();
		else 
			nomeUsuario = nomeUsuarioAcademia;
		
		JudokasFacade.getInstance().salvarAtleta(atleta, nomeUsuario);
		
		init();
		
		FacesMessage m = new FacesMessage("Registro salvo com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, m);
	}
	
	/**
	 * 
	 * @param event
	 * @throws FileNotFoundException 
	 */
	public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException {  
		uploadedFile = event.getFile();
		File f = new File(PATH + uploadedFile.getFileName());
		JudokasHelper.copyUploadFileToFile(f, uploadedFile);
		atleta.setFoto(f.getAbsolutePath());		
    } 
	
	/**
	 * 
	 * @param event
	 */
	public void onSelect(SelectEvent event) {
		Atleta a = (Atleta) event.getObject();
		atleta = a;
	}
	
	/**
	 * 
	 * @param a
	 * @throws LoggerException 
	 */
	public void delete(Atleta a) throws LoggerException {
		JudokasFacade.getInstance().apagarAtleta(a);
		
		init();
		
		FacesMessage m = new FacesMessage("Registro excluído com sucesso!");
		FacesContext.getCurrentInstance().addMessage(null, m);
	}
	
	/**
	 * 
	 * @param a
	 * @throws LoggerException 
	 */
	public void editar(Atleta a) throws LoggerException {
		atleta = a;
		nomeUsuarioAcademia = JudokasFacade.getInstance().buscarUsuarioAtleta(a).getNomeUsuario();
	}
	
	/**
	 * 
	 */
	public void novo(){
		atleta = new Atleta();		
	}
	
	public void removerFoto() throws LoggerException{
		JudokasFacade.getInstance().apagarFotoAtleta(atleta);
	}
}
