/**
 * 
 */
package br.com.sixinf.judokas.entidades;

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
	
	@Column(name = "identidade")
	private String identidade;
	
	@Column(name = "identidade_org_exp")
	private String orgaoExpedidor;
	
	@Column(name = "cpf")
	private String cpf;
		
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Column(name = "local_nascimento")
	private String localNascimento;
	
	@Column(name = "local_nascimento_uf")
	private String ufNascimento;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "graduacao")
	private String graduacao;
	
	@Column(name = "graduacao_data")
	@Temporal(TemporalType.DATE)
	private Date dataGraduacao;
	
	@Column(name = "endereco")
	private String endereco;
	
	@Column(name = "endereco_numero")
	private String numero;
	
	@Column(name = "bairro")
	private String bairro;
	
	@Column(name = "cep")
	private String cep;
	
	@Column(name = "complemento")
	private String complemento;
	
	@Column(name = "municipio")
	private String municipio;
	
	@Column(name = "uf")
	private String uf;
	
	@Column(name = "nome_mae")
	private String nomeMae;
	
	@Column(name = "email_mae")
	private String emailMae;
	
	@Column(name = "nome_pai")
	private String nomePai;
	
	@Column(name = "email_pai")
	private String emailPai;
	
	@Column(name = "fone_contato")
	private String foneContato;
	
	@Column(name = "status_registro", length = 1)
	private Character statusRegistro;
	
	@Column(name = "foto_path")
	private String foto;
	
	@Column(name = "sexo", length = 1)
	private Character sexo;
	
	@ManyToOne(targetEntity=Usuario.class)
	@JoinColumn(name="id_usuario")
	private Usuario usuario;
	
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

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		this.identidade = identidade;
	}

	public String getOrgaoExpedidor() {
		return orgaoExpedidor;
	}

	public void setOrgaoExpedidor(String orgaoExpedidor) {
		this.orgaoExpedidor = orgaoExpedidor;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getLocalNascimento() {
		return localNascimento;
	}

	public void setLocalNascimento(String localNascimento) {
		this.localNascimento = localNascimento;
	}

	public String getUfNascimento() {
		return ufNascimento;
	}

	public void setUfNascimento(String ufNascimento) {
		this.ufNascimento = ufNascimento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(String graduacao) {
		this.graduacao = graduacao;
	}

	public Date getDataGraduacao() {
		return dataGraduacao;
	}

	public void setDataGraduacao(Date dataGraduacao) {
		this.dataGraduacao = dataGraduacao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getEmailMae() {
		return emailMae;
	}

	public void setEmailMae(String emailMae) {
		this.emailMae = emailMae;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getEmailPai() {
		return emailPai;
	}

	public void setEmailPai(String emailPai) {
		this.emailPai = emailPai;
	}

	public String getFoneContato() {
		return foneContato;
	}

	public void setFoneContato(String foneContato) {
		this.foneContato = foneContato;
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

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public String getCpfMask(){
		return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9); 
	}
	
	public String getFoneContatoMask(){
		return "(" + foneContato.substring(0, 2) + ")" + foneContato.substring(2); 
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
