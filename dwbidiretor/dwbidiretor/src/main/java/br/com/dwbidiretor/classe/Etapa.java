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
@Table(name = "dwbi_projeto_etapa")
public class Etapa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idetapa;

	@ManyToOne
	private Projeto projeto;
	
	@Column
	private Integer ordenacao;

	@Column(nullable = false, columnDefinition = "varchar(50)")
	private String nome;
	
	@Column(nullable = false, columnDefinition = "varchar(150)")
	private String autoridade;
	
	@Column(nullable = false, columnDefinition = "varchar(150)")
	private String responsabilidade;

	@Column(nullable = false, columnDefinition = "varchar(100)")
	private String status; // aberto - andamento - atrasado - concluido - cancelado
	
	@Temporal(TemporalType.DATE)
	private Date dtcadastro;

	@Temporal(TemporalType.DATE)
	private Date dtinicio;
	
	@Temporal(TemporalType.DATE)
	private Date dtprevista;

	@Temporal(TemporalType.DATE)
	private Date dtconclusao;
	
	@OneToMany(mappedBy="etapa", cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE },orphanRemoval = true,fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
    private List<Etapa_Item> items = new ArrayList<>();
	
	

	public String getAutoridade() {
		return autoridade;
	}

	public void setAutoridade(String autoridade) {
		this.autoridade = autoridade;
	}

	public String getResponsabilidade() {
		return responsabilidade;
	}

	public void setResponsabilidade(String responsabilidade) {
		this.responsabilidade = responsabilidade;
	}

	public Etapa() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public Integer getIdetapa() {
		return idetapa;
	}

	public void setIdetapa(Integer idetapa) {
		this.idetapa = idetapa;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Integer getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}

	public List<Etapa_Item> getItems() {
		return items;
	}

	public void setItems(List<Etapa_Item> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autoridade == null) ? 0 : autoridade.hashCode());
		result = prime * result + ((dtcadastro == null) ? 0 : dtcadastro.hashCode());
		result = prime * result + ((dtconclusao == null) ? 0 : dtconclusao.hashCode());
		result = prime * result + ((dtinicio == null) ? 0 : dtinicio.hashCode());
		result = prime * result + ((dtprevista == null) ? 0 : dtprevista.hashCode());
		result = prime * result + ((idetapa == null) ? 0 : idetapa.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((ordenacao == null) ? 0 : ordenacao.hashCode());
		result = prime * result + ((projeto == null) ? 0 : projeto.hashCode());
		result = prime * result + ((responsabilidade == null) ? 0 : responsabilidade.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Etapa other = (Etapa) obj;
		if (autoridade == null) {
			if (other.autoridade != null)
				return false;
		} else if (!autoridade.equals(other.autoridade))
			return false;
		if (dtcadastro == null) {
			if (other.dtcadastro != null)
				return false;
		} else if (!dtcadastro.equals(other.dtcadastro))
			return false;
		if (dtconclusao == null) {
			if (other.dtconclusao != null)
				return false;
		} else if (!dtconclusao.equals(other.dtconclusao))
			return false;
		if (dtinicio == null) {
			if (other.dtinicio != null)
				return false;
		} else if (!dtinicio.equals(other.dtinicio))
			return false;
		if (dtprevista == null) {
			if (other.dtprevista != null)
				return false;
		} else if (!dtprevista.equals(other.dtprevista))
			return false;
		if (idetapa == null) {
			if (other.idetapa != null)
				return false;
		} else if (!idetapa.equals(other.idetapa))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (ordenacao == null) {
			if (other.ordenacao != null)
				return false;
		} else if (!ordenacao.equals(other.ordenacao))
			return false;
		if (projeto == null) {
			if (other.projeto != null)
				return false;
		} else if (!projeto.equals(other.projeto))
			return false;
		if (responsabilidade == null) {
			if (other.responsabilidade != null)
				return false;
		} else if (!responsabilidade.equals(other.responsabilidade))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	

}
