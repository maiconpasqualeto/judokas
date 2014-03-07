/**
 * 
 */
package br.com.sixinf.judokas.facade;

import java.util.Date;
import java.util.List;

import org.primefaces.context.RequestContext;

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
}
