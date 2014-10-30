/**
 * 
 */
package br.com.sixinf.judokas.entidades;

import java.awt.Image;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.sixinf.ferramentas.persistencia.Entidade;

/**
 * @author maicon
 *
 */
@Entity
@Table(name = "atleta", schema = "public")
public class Atleta implements Serializable, Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="seqAtleta", sequenceName="atleta_id_seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="seqAtleta")
	@Column(name="id")
	private Long id;
	
	@Column(name = "nome", length = 30)
	private String nome;
	
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Column(name = "graduacao")
	private String graduacao;
		
	@Column(name = "funcao")
	private String funcao;
		
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;
	
	@Column(name = "foto_path")
	private String foto;
	
	@Column(name = "data_emissao_carteira")
	@Temporal(TemporalType.DATE)
	private Date dataEmissaoCarteira;
		
	@ManyToOne(targetEntity=Usuario.class)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
	@Column(name = "matricula_zempo")
	private String matriculaZempo;
	
	@Transient
	private Image fotoImpressao;
	
	@Transient
	private Image graduacaoImpressao;
	
	@Transient
	private String categoriaImpressao;
	
	@Transient
	private String agremiacaoImpressao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(String graduacao) {
		this.graduacao = graduacao;
	}

	public Character getStatusRegistro() {
		return statusRegistro;
	}

	public void setStatusRegistro(Character statusRegistro) {
		this.statusRegistro = statusRegistro;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public Date getDataEmissaoCarteira() {
		return dataEmissaoCarteira;
	}

	public void setDataEmissaoCarteira(Date dataEmissaoCarteira) {
		this.dataEmissaoCarteira = dataEmissaoCarteira;
	}

	public Image getFotoImpressao() {
		return fotoImpressao;
	}

	public void setFotoImpressao(Image fotoImpressao) {
		this.fotoImpressao = fotoImpressao;
	}

	public Image getGraduacaoImpressao() {
		return graduacaoImpressao;
	}

	public void setGraduacaoImpressao(Image graduacaoImpressao) {
		this.graduacaoImpressao = graduacaoImpressao;
	}

	public String getCategoriaImpressao() {
		return categoriaImpressao;
	}

	public void setCategoriaImpressao(String categoriaImpressao) {
		this.categoriaImpressao = categoriaImpressao;
	}

	public String getMatriculaZempo() {
		return matriculaZempo;
	}

	public void setMatriculaZempo(String matriculaZempo) {
		this.matriculaZempo = matriculaZempo;
	}

	public String getAgremiacaoImpressao() {
		return agremiacaoImpressao;
	}

	public void setAgremiacaoImpressao(String agremiacaoImpressao) {
		this.agremiacaoImpressao = agremiacaoImpressao;
	}

	@Override
	public Long getIdentificacao() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atleta other = (Atleta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
