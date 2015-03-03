/**
 * 
 */
package br.com.sixinf.judokas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import br.com.sixinf.ferramentas.Utilitarios;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;

/**
 * @author maicon
 *
 */
public class JudokasHelper {
	
	private static final Logger LOG = Logger.getLogger(JudokasHelper.class);
	
	/**
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
	}

	/**
	 * 
	 * @return
	 */
	public static String getUsuarioSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		Object usuario = session.getAttribute(Usuario.SESSION_NOME_USUARIO);
		if (usuario != null)
			return usuario.toString();
		
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public static TipoUsuario getTipoUsuarioSessao() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		Object tipoUsuario = session.getAttribute(Usuario.SESSION_TIPO_USUARIO);
		if (tipoUsuario != null) 
			return TipoUsuario.valueOf(tipoUsuario.toString());			
		
		
		return null;
	}
	
	/**
	 * 
	 * @param destino
	 * @param fileUploaded
	 */
	public static void copyUploadFileToFile(File destino, UploadedFile fileUploaded){
		FileOutputStream fos = null;
		InputStream is = null;
		
        try {
			fos = new  FileOutputStream(destino);
			is = fileUploaded.getInputstream();
			
			byte[] buff = new byte[2048];
			while ( is.read(buff)  > -1){
				fos.write(buff);
			}
			
			fos.flush();
			
		} catch (FileNotFoundException e) {
			LOG.error("Arquivo não encontrado.", e);
		} catch (IOException e) {
			LOG.error("Erro de IO.", e);
		} finally {
			try {
				if (fos != null){
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				LOG.error("Erro ao fechar os arquivos.", e);
			}
		}
	}
	
	/**
	 * Funcao que anbrevia nomes. 	 * 
	 * @param nome
	 * @return
	 */
	public static String diminuiONome(String nome) {
		return Utilitarios.abreviaNome(nome.trim(), 1, 33);
	}
}
