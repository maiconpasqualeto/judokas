/**
 * 
 */
package br.com.sixinf.judokas.listener;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.ferramentas.persistencia.PersistenciaException;
import br.com.sixinf.judokas.beans.AtletasBean;

/**
 * @author maicon
 *
 */
public class JudokasListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		AdministradorPersistencia.close();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			
			AdministradorPersistencia.createEntityManagerFactory("judokas");
			
			File f = new File(AtletasBean.PATH);
			if ( !f.exists() )
				f.mkdirs();
			
		} catch (PersistenciaException e) {
			Logger.getLogger(JudokasListener.class).error(e);
		}
		
		
		
	}

}
