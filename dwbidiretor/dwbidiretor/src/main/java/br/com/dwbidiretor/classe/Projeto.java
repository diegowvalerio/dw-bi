package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="dwbi_projeto")
public class Projeto  implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	private Integer idprojeto;
	
	@Column(nullable=false,columnDefinition="varchar(50)")
	private String nome;
	
	@Column(nullable=false,columnDefinition="varchar(250)")
	private String descricao;
	
	@Column(nullable=false,columnDefinition="varchar(100)")
	private String status; //aberto - andamento - atrasado - concluido - cancelado
	
	@Temporal(TemporalType.DATE)
	private Date dtcadastro;
	
	@Temporal(TemporalType.DATE)
	private Date dtinicio;
	
	@Temporal(TemporalType.DATE)
	private Date dtprevista;
	
	@Temporal(TemporalType.DATE)
	private Date dtconclusao;
	
	@OneToMany(mappedBy="projeto", cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE },orphanRemoval = true,fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
    private List<Etapa> etapas = new ArrayList<>();
		
	public Projeto() {
		super();
	}

	public Integer getIdprojeto() {
		return idprojeto;
	}

	public void setIdprojeto(Integer idprojeto) {
		this.idprojeto = idprojeto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDtcadastro() {
		return dtcadastro;
	}

	public void setDtcadastro(Date dtcadastro) {
		this.dtcadastro = dtcadastro;
	}

	public Date getDtinicio() {
		return dtinicio;
	}

	public void setDtinicio(Date dtinicio) {
		this.dtinicio = dtinicio;
	}

	public Date getDtprevista() {
		return dtprevista;
	}

	public void setDtprevista(Date dtprevista) {
		this.dtprevista = dtprevista;
	}

	public Date getDtconclusao() {
		return dtconclusao;
	}

	public void setDtconclusao(Date dtconclusao) {
		this.dtconclusao = dtconclusao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Etapa> getEtapas() {
		return etapas;
	}

	public void setEtapas(List<Etapa> etapas) {
		this.etapas = etapas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idprojeto == null) ? 0 : idprojeto.hashCode());
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
		Projeto other = (Projeto) obj;
		if (idprojeto == null) {
			if (other.idprojeto != null)
				return false;
		} else if (!idprojeto.equals(other.idprojeto))
			return false;
		return true;
	}   
	 
   
}
