/**
 * 
 */
package br.com.sixinf.judokas.beans;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.log4j.Logger;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DefaultStreamedContent;
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
	private static final Logger LOG = Logger.getLogger(ImpressaoBean.class);
	
	private DualListModel<Atleta> atletas = new DualListModel<Atleta>();
	private List<Usuario> usuarios = new ArrayList<Usuario>();
	private Usuario usuarioAcademia;
	
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
	
	/**
	 * 
	 */
	public void imprimir() {
		
	}
	
	public StreamedContent printReport() throws JRException, IOException, ClassNotFoundException, SQLException, LoggerException {  
		
		List<Atleta> atletasImpressao = atletas.getTarget();
		
		if (atletasImpressao.isEmpty()) {
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					"Nenhuma atleta selecionado", 
					"Nenhuma atleta selecionado");
			FacesContext.getCurrentInstance().addMessage(null, m);
			return null;
		}	
		
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext contextS = (ServletContext) externalContext.getContext();
		String arquivo = contextS
				.getRealPath("/resources/reports/carteirinhas.jasper");
		List<Long> ids = new ArrayList<Long>();
		for (Atleta a : atletasImpressao) {
			String fotoPath = a.getFoto();
			Image fotoImpressao = null;
			Image graduacaoImpressao = null;
			if (fotoPath != null && !fotoPath.isEmpty()) {
				fotoImpressao = new ImageIcon(fotoPath).getImage();				
			} else 
				fotoImpressao = new ImageIcon(
						contextS.getRealPath("/images/nophoto.jpg")).getImage();
			
			a.setFotoImpressao(fotoImpressao);
			
			String graduacaoPath = returnGraduacaoPath(a.getGraduacao());
			graduacaoImpressao =  new ImageIcon(contextS.getRealPath(graduacaoPath)).getImage();
			a.setGraduacaoImpressao(graduacaoImpressao);
			
			String categoria = returnCategoria(a.getDataNascimento());
			a.setCategoriaImpressao(categoria);
			
			ids.add(a.getId());
		}
		
		try {

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					atletasImpressao);
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("agremiacao", usuarioAcademia.getNome());
						
			JasperPrint print = JasperFillManager.fillReport(arquivo, parametros,
					beanColDataSource);
			
			JRExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
			exporter.exportReport();
			is = new ByteArrayInputStream(baos.toByteArray());

		} catch (JRException e) {
			LOG.error("Erro ao gerar relatório jasper", e);
		}
		
		JudokasFacade.getInstance().atualizaDataImpressao(ids);
				
		return new DefaultStreamedContent(is, "application/pdf", "carteirinhas_" + usuarioAcademia.getNome() + ".pdf");
		
    }	
	
	private String returnCategoria(Date dataNascimento) {
		Calendar hj = GregorianCalendar.getInstance();  
		Calendar nascimento = new GregorianCalendar();
		nascimento.setTime(dataNascimento);  
		         
		int anohj = hj.get(Calendar.YEAR);  
		int anoNascimento = nascimento.get(Calendar.YEAR);  
		int idade = anohj - anoNascimento;
		
		String categoria = "";
		
		if (idade == 7 || idade == 8 )
			categoria = "SUB 9";
		else 
			if (idade == 9 || idade == 10 )
				categoria = "SUB 11";
		else 
			if (idade == 11 || idade == 12 )
				categoria = "SUB 13";
		else 
			if (idade == 13 || idade == 14 )
				categoria = "SUB 15";
		else 
			if (idade == 15 || idade == 16 )
				categoria = "SUB 18";
		else 
			if (idade >= 17 )
				categoria = "+SUB 18";
		
		return categoria;
	}

	/**
	 * 
	 * @param graduacao
	 * @return
	 */
	private String returnGraduacaoPath(String graduacao){
		String path = null;
		if (("BRANCA").equals(graduacao))
			path = "/images/graduacao_branca.png";
		else
			if (("BRANCA/CINZA").equals(graduacao))
				path = "/images/graduacao_branca_cinza.png";
		else 
			if (("CINZA").equals(graduacao))
				path = "/images/graduacao_cinza.png";
		else
			if (("CINZA/AZUL").equals(graduacao))
				path = "/images/graduacao_cinza_azul.png";
		else
			if (("AZUL").equals(graduacao))
				path = "/images/graduacao_azul.png";
		else
			if (("AZUL/AMARELA").equals(graduacao))
				path = "/images/graduacao_azul_amarela.png";
		else 
			if (("AMARELA").equals(graduacao))
				path = "/images/graduacao_amarela.png";
		else
			if (("AMARELA/LARANJA").equals(graduacao))
				path = "/images/graduacao_amarela_laranja.png";
		else
			if (("LARANJA").equals(graduacao))
				path = "/images/graduacao_laranja.png";
		else
			if (("VERDE").equals(graduacao))
				path = "/images/graduacao_verde.png";
		else
			if (("ROXA").equals(graduacao))
				path = "/images/graduacao_roxa.png";
		else
			if (("MARROM").equals(graduacao))
				path = "/images/graduacao_marrom.png";
		else
			if (("PRETA SHO-DAN").equals(graduacao))
				path = "/images/graduacao_preta_1_dan.png";
		else
			if (("PRETA NI-DAN").equals(graduacao))
				path = "/images/graduacao_preta_2_dan.png";
		else
			if (("PRETA SAN-DAN").equals(graduacao))
				path = "/images/graduacao_preta_3_dan.png";
		else
			if (("PRETA YON-DAN").equals(graduacao))
				path = "/images/graduacao_preta_4_dan.png";
		else
			if (("PRETA GO-DAN").equals(graduacao))
				path = "/images/graduacao_preta_5_dan.png";
		else
			if (("VERMELHA E BRANCA ROKU-DAN").equals(graduacao))
				path = "/images/graduacao_vb_6_dan.png";
		else
			if (("VERMELHA E BRANCA SHITI-DAN").equals(graduacao))
				path = "/images/graduacao_vb_7_dan.png";
		else
			if (("VERMELHA E BRANCA RATI-DAN").equals(graduacao))
				path = "/images/graduacao_vb_8_dan.png";
		else
			if (("VERMELHA KIU-DAN ").equals(graduacao))
				path = "/images/graduacao_v_9_dan.png";
		else
			if (("VERMELHA DIU-DAN").equals(graduacao))
				path = "/images/graduacao_v_10_dan.png";
		
		return path;
	}
	
	/**
	 * @throws LoggerException 
	 * 
	 */
	public void buscarAtletas() throws LoggerException {
		atletas.setSource(JudokasFacade.getInstance().buscarAtletasAcademia(usuarioAcademia.getId()));
		
	}
}
