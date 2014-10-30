/**
 * 
 */
package br.com.sixinf.judokas.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import br.com.sixinf.ferramentas.dao.BridgeBaseDAO;
import br.com.sixinf.ferramentas.dao.HibernateBaseDAOImp;
import br.com.sixinf.ferramentas.log.LoggerException;
import br.com.sixinf.ferramentas.persistencia.AdministradorPersistencia;
import br.com.sixinf.judokas.entidades.Atleta;
import br.com.sixinf.judokas.entidades.TipoUsuario;
import br.com.sixinf.judokas.entidades.Usuario;

/**
 * @author maicon
 *
 */
public class JudokasDAO extends BridgeBaseDAO {
	
	private static final Logger LOG = Logger.getLogger(JudokasDAO.class);
	
	private static JudokasDAO dao;
	
	public static JudokasDAO getInstance(){
		if (dao == null)
			dao = new JudokasDAO();
		return dao;
	}

	public JudokasDAO() {
		super(new HibernateBaseDAOImp());
	}
	
	public Usuario buscarUsuario(String nomeUsuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Usuario u = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.nomeUsuario = :nomeUsuario ");
			hql.append("and u.statusRegistro = 'A' ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			q.setParameter("nomeUsuario", nomeUsuario);
			q.setMaxResults(1);
			
			u = q.getSingleResult();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar usuario", e, LOG);
		} finally {
            em.close();
        }
		return u;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletas() throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Atleta> atletas = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Atleta a ");
			hql.append("where a.statusRegistro = 'A' ");
			hql.append("order by a.nome ");
			TypedQuery<Atleta> q = em.createQuery(hql.toString(), Atleta.class);
			
			atletas = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar atletas", e, LOG);
		} finally {
            em.close();
        }
		return atletas;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletasDaAcademia(String nomeUsuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Atleta> atletas = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Atleta a join a.usuario u ");
			hql.append("where a.statusRegistro = 'A' ");
			hql.append("and u.nomeUsuario = :nomeUsuario ");
			hql.append("order by a.nome ");
			TypedQuery<Atleta> q = em.createQuery(hql.toString(), Atleta.class);
			q.setParameter("nomeUsuario", nomeUsuario);
			
			atletas = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar atletas por academia", e, LOG);
		} finally {
            em.close();
        }
		return atletas;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletasDaAcademia(Long idUsuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Atleta> atletas = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Atleta a join a.usuario u ");
			hql.append("where a.statusRegistro = 'A' ");
			hql.append("and u.id = :idUsuario ");
			hql.append("order by a.nome ");
			TypedQuery<Atleta> q = em.createQuery(hql.toString(), Atleta.class);
			q.setParameter("idUsuario", idUsuario);
			
			atletas = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar atletas por academia", e, LOG);
		} finally {
            em.close();
        }
		return atletas;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Usuario> buscarUsuarios() throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Usuario> usuarios = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.statusRegistro = 'A' ");
			hql.append("order by u.nome ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			
			usuarios = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar usuarios", e, LOG);
		} finally {
            em.close();
        }
		return usuarios;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Usuario> buscarUsuariosPeloTipo(TipoUsuario tipo) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Usuario> usuarios = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u ");
			hql.append("where u.tipoUsuario = :tipoUsuario ");
			hql.append("and u.statusRegistro = 'A' ");
			hql.append("order by u.nome ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			q.setParameter("tipoUsuario", tipo.name());
			
			usuarios = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar usuarios por tipo", e, LOG);
		} finally {
            em.close();
        }
		return usuarios;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public Usuario buscarUsuariosAtleta(Atleta atleta) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		Usuario usuario = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select u from Usuario u join u.atletas a ");
			hql.append("where a.id = :idAtleta ");
			TypedQuery<Usuario> q = em.createQuery(hql.toString(), Usuario.class);
			q.setParameter("idAtleta", atleta.getId());
			q.setMaxResults(1);
			
			usuario = q.getSingleResult();
			
		} catch (NoResultException e) {
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar usuario do atleta", e, LOG);
		} finally {
            em.close();
        }
		return usuario;
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public void alterarUsuario(Usuario usuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Usuario u ");
	        hql.append("set u.nome = :nome, ");
	        hql.append("u.nomeUsuario = :nomeUsuario, ");
	        hql.append("u.email = :email, ");
	        hql.append("u.tipoUsuario = :tipoUsuario ");
	        hql.append("where u.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", usuario.getId());
	        q.setParameter("nome", usuario.getNome());
	        q.setParameter("nomeUsuario", usuario.getNomeUsuario());
	        q.setParameter("email", usuario.getEmail());
	        q.setParameter("tipoUsuario", usuario.getTipoUsuario());
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao alterar usuario", e, LOG);
		} finally {
            em.close();
        }
	}
	
	public <T> void exclusaoLogicaGenerica(Long id, Class<T> clazz) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update " + clazz.getSimpleName() + " t ");
	        hql.append("set t.statusRegistro = 'D' ");
	        hql.append("where t.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", id);
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao fazer exclusão lógica.", e, LOG);
		} finally {
            em.close();
        }
	}
	
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public void salvarNovaSenha(Usuario usuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Usuario u ");
	        hql.append("set u.senha = :senha, ");
	        hql.append("u.primeiroLogin = :primeiroLogin ");
	        hql.append("where u.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", usuario.getId());
	        q.setParameter("senha", usuario.getSenha());
	        q.setParameter("primeiroLogin", Boolean.FALSE);
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao salvar nova senha", e, LOG);
		} finally {
            em.close();
        }
	}

	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void alterarSenhaUsuario(Usuario usuario) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Usuario u ");
	        hql.append("set u.senha = :senha, ");
	        hql.append("u.primeiroLogin = :primeiroLogin ");
	        hql.append("where u.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", usuario.getId());
	        q.setParameter("senha", usuario.getSenha());
	        q.setParameter("primeiroLogin", Boolean.TRUE);
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao alterar senha do usuario", e, LOG);
		} finally {
            em.close();
        }
	}
	
	/**
	 * 
	 * @param usuario
	 * @throws LoggerException 
	 */
	public void apagarFoto(Atleta atleta) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Atleta a ");
	        hql.append("set a.foto = null ");
	        hql.append("where a.id = :id");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("id", atleta.getId());
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao apagar a foto", e, LOG);
		} finally {
            em.close();
        }
	}

	/**
	 * 
	 * @param dataEmissao
	 * @param ids
	 * @throws LoggerException
	 */
	public void atualizaDataImpressao(Date dataEmissao, List<Long> ids) throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		EntityTransaction t = em.getTransaction();
		try {
			
			StringBuilder hql = new StringBuilder();
	        hql.append("update Atleta a ");
	        hql.append("set a.dataEmissaoCarteira = :dataEmissaoCarteira ");
	        hql.append("where a.id in (:ids)");
	        
	        Query q = em.createQuery(hql.toString());
	        
	        q.setParameter("dataEmissaoCarteira", dataEmissao);
	        q.setParameter("ids", ids);
	        
	        t.begin();
			
			q.executeUpdate();
			
			t.commit();
			
		} catch (Exception e) {
			t.rollback();
			throw new LoggerException("Erro ao atualizar data de emissao das carteirinhas", e, LOG);
		} finally {
            em.close();
        }
		
	}
	
	/**
	 * 
	 * @return
	 * @throws LoggerException 
	 */
	public List<Atleta> buscarAtletasNovos() throws LoggerException {
		EntityManager em = AdministradorPersistencia.getEntityManager();
		
		List<Atleta> atletas = null;
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("select a from Atleta a ");
			hql.append("where a.statusRegistro = 'A' ");
			hql.append("and a.dataEmissaoCarteira is null ");
			hql.append("order by a.usuario.nome, a.nome ");
			TypedQuery<Atleta> q = em.createQuery(hql.toString(), Atleta.class);
			
			atletas = q.getResultList();
			
		} catch (Exception e) {
			throw new LoggerException("Erro ao buscar atletas novos", e, LOG);
		} finally {
            em.close();
        }
		return atletas;
	}
	
}
