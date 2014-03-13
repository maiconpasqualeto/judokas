/**
 * 
 */
package br.com.sixinf.judokas.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.Utilitarios;

/**
 * @author maicon
 *
 */
public class DisplayImage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
        try {
        	
        	InputStream is = null;
        	OutputStream os = null;
        	
        	try {
        	
	            String arquivo = request.getParameter("arquivo");
	            
	            if (arquivo == null || 
	            		arquivo.isEmpty())
	            	return;
	            
	            is = new FileInputStream(new File(arquivo));
	            byte[] bytes = Utilitarios.fazLeituraStreamEmByteArray(is);
	            
	            response.reset();
	            response.setContentType("image/jpeg");
	            os = response.getOutputStream();
	            os.write(bytes);
	            os.flush();
	            
        	} finally {
        		if (is != null)
        			is.close();
        		if (os != null)
        			os.close();
        	}
 
        } catch (Exception e) {
            Logger.getLogger(DisplayImage.class).error("Erro ao buscar arquivo de imagem", e);
        }
	}

}
