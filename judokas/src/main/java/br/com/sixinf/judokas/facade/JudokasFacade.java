/**
 * 
 */
package br.com.sixinf.judokas.facade;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.swing.ImageIcon;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.judokas.JudokasHelper;
import br.com.sixinf.judokas.dao.JudokasDAO;
import br.com.sixinf.judokas.entidades.Atleta;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;

/**
 * @author maicon
 *
 */
@SuppressWarnings("deprecation")
public class JudokasFacade {
	
	//private static final Logger LOG = Logger.getLogger(JudokasFacade.class);
	private static JudokasFacade facade;
	
	public static JudokasFacade getInstance(){
		if (facade == null)
			facade = new JudokasFacade();
		
		return facade;
	}
	
	private JudokasDAO dao;
	
	public JudokasFacade() {
		dao = JudokasDAO.getInstance();
	}

	public JudokasDAO getDao() {
		return dao;
	}

	public void setDao(JudokasDAO dao) {
		this.dao = dao;
	}
	
	public boolean logar(Usuario usuario) throws LoggerException{
		if (usuario == null || 
				usuario.getNomeUsuario() == null || 
				usuario.getSenha() == null) {
			return false;
		}
		
		Usuario usuarioBanco = dao.buscarUsuario(usuario.getNomeUsuario());
		
		// usuário não existe
		if (usuarioBanco == null) 
			return false;
		
		usuario.setTipoUsuario(usuarioBanco.getTipoUsuario());
		usuario.setPrimeiroLogin(usuarioBanco.getPrimeiroLogin());
				
		// senha não confere
		String senhaSHA2 = Utilitarios.geraHashSHA2(usuario.getSenha());
		if (!senhaSHA2.equals(usuarioBanco.getSenha())) 
			return false;
				
		// login ok
		return true;
	}
	
	/**
	 * 
	 * @param cep
	 *//*
	public void buscarCEP(Atleta atleta){
		InputStream is = null;
		HttpURLConnection connection = null;
		
		if (atleta == null || 
				atleta.getCep() == null)
			return;
		
		try {
			// Objeto URL
			URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?formato=json&cep=" + atleta.getCep());
			
			try {
			
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("Request-Method", "GET");
				connection.setDoInput(true);
				connection.setDoOutput(false);
				connection.connect();
				
				is = connection.getInputStream();
				byte[] buf = Utilitarios.fazLeituraStreamEmByteArray(is);
				JSONObject jo = new JSONObject(new String(buf));
				String resultado = jo.getString("resultado");
				if (resultado.equals("1")) { // cep completo
					String uf = jo.getString("uf");
					String municipio = jo.getString("cidade");
					String tipoLogradouro = jo.getString("tipo_logradouro");
					String logradouro = jo.getString("logradouro");
					String bairro = jo.getString("bairro");
					atleta.setUf(uf);
					atleta.setMunicipio(municipio);
					atleta.setEndereco(tipoLogradouro + " " + logradouro);
					atleta.setBairro(bairro);
				} else 
					if (resultado.equals("2")) { // cep único
						String uf = jo.getString("uf");
						String municipio = jo.getString("cidade");
						atleta.setUf(uf);
						atleta.setMunicipio(municipio);
					}
				
			} finally {
				if (is != null)
					is.close();
			}
			
		} catch (IOException | JSONException e) {
			LOG.error("Erro ao buscar CEP", e);
		}
	}*/
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletas() throws LoggerException{
		// se for usuário MASTER busca todos os atletas se for tipo ACADEMIA busca só os atletas que ele cadastrou.
		List<Atleta> atletas = null;
		TipoUsuario tipoUsuario = JudokasHelper.getTipoUsuarioSessao();
		if (tipoUsuario.equals(TipoUsuario.MASTER))
			atletas = dao.buscarAtletas();
		else {
			String nomeUsuario = JudokasHelper.getUsuarioSessao();
			atletas = dao.buscarAtletasDaAcademia(nomeUsuario);
		}
		return atletas; 
	}
	
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletasAcademia(Long idUsuarioAcademia) throws LoggerException{
		List<Atleta> atletas = dao.buscarAtletasDaAcademia(idUsuarioAcademia);
		return atletas; 
	}
	
	/**
	 * 
	 * @param atleta
	 * @throws LoggerException 
	 */
	public void apagarAtleta(Atleta atleta) throws LoggerException{
		
		dao.exclusaoLogicaGenerica(atleta.getId(), Atleta.class);
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void apagarUsuario(Usuario usuario) throws LoggerException{
		
		dao.exclusaoLogicaGenerica(usuario.getId(), Usuario.class);
	}

	public void salvarAtleta(Atleta atleta, String nomeUsuario) throws LoggerException {
		
		Usuario usuario = dao.buscarUsuario(nomeUsuario);
		atleta.setUsuario(usuario);
		
		if (atleta.getId() == null || atleta.getId().equals(0L)) {
			// atleta novo
			
			atleta.setStatusRegistro('A');
			dao.adicionar(atleta);

		} else {
			dao.alterar(atleta);
		}

	}	
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Usuario> buscarUsuarios() throws LoggerException{
		return dao.buscarUsuarios();
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException
	 */
	public void salvarUsuario(Usuario usuario) throws LoggerException {
			
		if (usuario.getId() == null ||
				usuario.getId().equals(0L)) { // inclusao
			
			usuario.setStatusRegistro('A');
			usuario.setDataRegistro(new Date());
			usuario.setPrimeiroLogin(true);
			usuario.setSenha(Utilitarios.geraHashSHA2(usuario.getNomeUsuario()));
		
			dao.adicionar(usuario);
						
		} else { // alteração
			dao.alterarUsuario(usuario);
		}
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam("fechaDialog", true);
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException
	 */
	public void salvarNovaSenha(Usuario usuario) throws LoggerException {
		Usuario usuarioBanco = dao.buscarUsuario(usuario.getNomeUsuario());
		usuarioBanco.setSenha(Utilitarios.geraHashSHA2(usuario.getSenha()));		
		
		dao.salvarNovaSenha(usuarioBanco);
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException
	 */
	public List<Usuario> buscarUsuariosACADEMIA() throws LoggerException {
		return dao.buscarUsuariosPeloTipo(TipoUsuario.ACADEMIA);
	}

	/**
	 * 
	 * @param a
	 * @return
	 * @throws LoggerException 
	 */
	public Usuario buscarUsuarioAtleta(Atleta a) throws LoggerException {
		return dao.buscarUsuariosAtleta(a);
	}

	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void resetarSenha(Usuario usuario) throws LoggerException {
		usuario.setSenha(Utilitarios.geraHashSHA2(usuario.getNomeUsuario()));
		dao.alterarSenhaUsuario(usuario);
		
	}
	
	public void apagarFotoAtleta(Atleta atleta) throws LoggerException {
		File foto = new File(atleta.getFoto());
		foto.delete();
		
		atleta.setFoto(null);
		
		dao.apagarFoto(atleta);
				
	}

	public void atualizaDataImpressao(List<Long> ids) throws LoggerException {
		dao.atualizaDataImpressao(new Date(), ids);
	}
	
	/**
	 * 
	 * @param dataNascimento
	 * @return
	 */
	public String returnCategoria(Date dataNascimento) {
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
			if (idade >= 15 && idade < 18 )
				categoria = "SUB 18";
		else 
			if (idade >= 18 && idade < 21 )
				categoria = "SUB 21";
		else 
			if (idade >= 21 && idade < 23 )
				categoria = "SUB 23";
		else
			if (idade >= 21 )
				categoria = "SÊNIOR";
		
		return categoria;
	}
	
	/**
	 * 
	 * @param atletasImpressao
	 * @return
	 * @throws LoggerException 
	 */
	public DefaultStreamedContent geraReportCarteirinhas(String academia,
			List<Atleta> atletasImpressao) throws LoggerException{
		
		InputStream is = null;
		
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
			
			String categoria = JudokasFacade.getInstance().returnCategoria(a.getDataNascimento());
			a.setCategoriaImpressao(categoria);
			
			a.setAgremiacaoImpressao(a.getUsuario().getNome());
			
			a.setNome(JudokasHelper.diminuiONome(a.getNome()));
			
			ids.add(a.getId());
		}
		
		try {

			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					atletasImpressao);
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			/*parametros.put("agremiacao", usuarioAcademia.getNome());*/
			Locale locale = new Locale("pt", "BR");
			parametros.put(JRParameter.REPORT_LOCALE, locale);			
			
			JasperPrint print = JasperFillManager.fillReport(arquivo, parametros,
					beanColDataSource);
			
			File temp = new File("temp.zip");
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(temp));
			
			for (int i=0; i<print.getPages().size(); i++) {
				String nome = atletasImpressao.get(i).getNome().replace(' ', '-');
				nome = Utilitarios.removeAcentuacao(nome);
				ZipEntry ze = new ZipEntry(nome + ".jpg");
				zos.putNextEntry(ze);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				BufferedImage reportImage = (BufferedImage) JasperPrintManager.printPageToImage(print, i, 1.6f);
		        ImageIO.write(reportImage, "jpg", baos);
		        
		        zos.write(baos.toByteArray());
				zos.flush();
				zos.closeEntry();
			} 
			
			zos.close();
				        
			is = new FileInputStream(temp);
			
			atualizaDataImpressao(ids);

		} catch (JRException | IOException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao gerar relatório jasper", e);
			return null;
		}
		String nomeDownload = "carteirinhas_";
		if (academia != null)
			nomeDownload += academia + "_";
		else 
			nomeDownload += "todas_";
		
		return new DefaultStreamedContent(is, "application/zip", nomeDownload + 
				new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + ".zip");
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
	 * 
	 * @param atleta
	 * @throws LoggerException
	 */
	public void cancelarCarteirinha(Atleta atleta) throws LoggerException {
		atleta.setDataEmissaoCarteira(null);
		dao.cancelarCarteirinha(atleta);
	}
	
	
	/**
	 * 
	 * @param atletasImpressao
	 * @return
	 * @throws LoggerException 
	 */
	public DefaultStreamedContent geraReportCarteirinhasEmitidas() throws LoggerException{
		
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext contextS = (ServletContext) externalContext.getContext();
		String arquivo = contextS
				.getRealPath("/resources/reports/carteirinhas_impressas.jasper");
		
		try {
			Connection conn = null;
			try {
			
				Map<String, Object> parametros = new HashMap<String, Object>();
				
				InitialContext context = new InitialContext();
				DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/JudokasDS");
				conn = dataSource.getConnection();
				
				JasperPrint print = JasperFillManager.fillReport(
						arquivo, parametros, conn);
				
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
				exporter.exportReport();
				is = new ByteArrayInputStream(baos.toByteArray());
				
			
			} finally {
				if (conn != null)
					conn.close();
			}

		} catch (JRException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao gerar relatório carteiras emitidas jasper", e);
		} catch (SQLException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao carregar driver do banco de dados", e);
		} catch (NamingException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao carregar driver do banco de dados", e);
		}
		
		return new DefaultStreamedContent(is, "application/pdf", "carteirinhas_emitidas.pdf");
	}
	
	/**
	 * 
	 * @param atletasImpressao
	 * @return
	 * @throws LoggerException 
	 */
	public DefaultStreamedContent geraReportCarteirinhasEmitidasDia(Date data) throws LoggerException{
		
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ExternalContext externalContext = FacesContext.getCurrentInstance()
				.getExternalContext();
		ServletContext contextS = (ServletContext) externalContext.getContext();
		String arquivo = contextS
				.getRealPath("/resources/reports/carteirinhas_impressas_dia.jasper");
		
		try {
			Connection conn = null;
			try {
			
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("data", data);
				
				InitialContext context = new InitialContext();
				DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/JudokasDS");
				conn = dataSource.getConnection();
				
				JasperPrint print = JasperFillManager.fillReport(
						arquivo, parametros, conn);
				
				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
				exporter.exportReport();
				is = new ByteArrayInputStream(baos.toByteArray());
				
			
			} finally {
				if (conn != null)
					conn.close();
			}

		} catch (JRException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao gerar relatório carteiras emitidas dia jasper", e);
		} catch (SQLException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao carregar driver do banco de dados", e);
		} catch (NamingException e) {
			Logger.getLogger(JudokasFacade.class).error("Erro ao carregar driver do banco de dados", e);
		}
		
		return new DefaultStreamedContent(is, "application/pdf", "carteirinhas_emitidas_dia.pdf");
	}
}
