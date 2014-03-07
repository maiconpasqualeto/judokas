/**
 * 
 */
package br.com.sixinf.webtracker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.sixinf.judokas.beans.SegurancaBean;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;


/**
 * @author maicon
 * 
 */
@WebFilter(filterName = "segurancaFilter", urlPatterns = { "*.xhtml" })
public class SegurancaFilter implements Filter {
	
	private static final Logger LOG = Logger.getLogger(SegurancaFilter.class); 

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession ses = req.getSession(false);

			String reqURI = req.getRequestURI();
			String pagina = reqURI.substring(reqURI.lastIndexOf('/') + 1);
			if (reqURI.contains(".xhtml")
					&& !reqURI.contains("home.xhtml") 
					&& !reqURI.contains("error.xhtml") 
					&& !reqURI.contains("javax.faces.resource")
					&& (ses == null || ses.getAttribute(Usuario.SESSION_NOME_USUARIO) == null)) {
				req.getRequestDispatcher("/pages/home.xhtml?faces-redirect=true")
						.forward(req, res);
			} else {
				if (pagina.isEmpty() 
						|| reqURI.contains("home.xhtml") 
						|| reqURI.contains("error.xhtml") 
						|| reqURI.contains("principal.xhtml")
						|| reqURI.contains("javax.faces.resource")) {
					
					chain.doFilter(request, response);
					
				} else {
				
					String tipoUsuario = (String) ses.getAttribute(Usuario.SESSION_TIPO_USUARIO);
					
					if (tipoUsuario != null && 
							!tipoUsuario.isEmpty() &&
							reqURI.contains(".xhtml")){
						boolean permite = false;
						TipoUsuario t = TipoUsuario.valueOf(tipoUsuario);
						switch(t){
						case MASTER:
							if (SegurancaBean.permissoesMaster.contains(pagina))
								permite = true;
							break;
						case ACADEMIA:
							if (SegurancaBean.permissoesAdmin.contains(pagina))
								permite = true;
							break;
						}
						
						if (permite)
							chain.doFilter(request, response);
						else 
							req.getRequestDispatcher("/pages/principal.xhtml?faces-redirect=true")
								.forward(req, res);
						
					}
					
				}
				
			}
			
		} catch (Throwable t) {
			LOG.error("Erro no filtro de requisicoes 'SegurancaFilter'", t);
		}

	}

	@Override
	public void init(FilterConfig config) throws ServletException {

	}

}
