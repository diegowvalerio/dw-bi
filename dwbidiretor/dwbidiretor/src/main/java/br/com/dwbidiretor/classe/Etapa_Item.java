package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "dwbi_projeto_etapa_item")
public class Etapa_Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idetapaitem;

	@ManyToOne
	private Etapa etapa;
	
	@Column
	private Integer ordenacao;
	
	@Column(nullable = false, columnDefinition = "varchar(150)")
	private String responsavel;

	@Column(nullable = false, columnDefinition = "varchar(250)")
	private String descricao;
	
	@Column(nullable=true, columnDefinition="numeric(6,2)")
	private double valorprevisto;
	
	@Column(nullable=true, columnDefinition="numeric(6,2)")
	private double valorconcluido;
	
	@Temporal(TemporalType.DATE)
	private Date dtcadastro;

	@Temporal(TemporalType.DATE)
	private Date dtinicio;

	@Temporal(TemporalType.DATE)
	private Date dtprevista;

	@Temporal(TemporalType.DATE)
	private Date dtconclusao;
	
	@Column(nullable = false, columnDefinition = "varchar(100)")
	private String status; // aberto - andamento - atrasado - concluido - cancelado
	
	@Column(nullable = false, columnDefinition = "varchar(250)")
	private String statusobservacao;

	public Integer getIdetapaitem() {
		return idetapaitem;
	}

	public void setIdetapaitem(Integer idetapaitem) {
		this.idetapaitem = idetapaitem;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

	public Integer getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public double getValorprevisto() {
		return valorprevisto;
	}

	public void setValorprevisto(double valorprevisto) {
		this.valorprevisto = valorprevisto;
	}

	public double getValorconcluido() {
		return valorconcluido;
	}

	public void setValorconcluido(double valorconcluido) {
		this.valorconcluido = valorconcluido;
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

	public String getStatusobservacao() {
		return statusobservacao;
	}

	public void setStatusobservacao(String statusobservacao) {
		this.statusobservacao = statusobservacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((dtcadastro == null) ? 0 : dtcadastro.hashCode());
		result = prime * result + ((dtconclusao == null) ? 0 : dtconclusao.hashCode());
		result = prime * result + ((dtinicio == null) ? 0 : dtinicio.hashCode());
		result = prime * result + ((dtprevista == null) ? 0 : dtprevista.hashCode());
		result = prime * result + ((etapa == null) ? 0 : etapa.hashCode());
		result = prime * result + ((idetapaitem == null) ? 0 : idetapaitem.hashCode());
		result = prime * result + ((ordenacao == null) ? 0 : ordenacao.hashCode());
		result = prime * result + ((responsavel == null) ? 0 : responsavel.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusobservacao == null) ? 0 : statusobservacao.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valorconcluido);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(valorprevisto);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Etapa_Item other = (Etapa_Item) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
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
		if (etapa == null) {
			if (other.etapa != null)
				return false;
		} else if (!etapa.equals(other.etapa))
			return false;
		if (idetapaitem == null) {
			if (other.idetapaitem != null)
				return false;
		} else if (!idetapaitem.equals(other.idetapaitem))
			return false;
		if (ordenacao == null) {
			if (other.ordenacao != null)
				return false;
		} else if (!ordenacao.equals(other.ordenacao))
			return false;
		if (responsavel == null) {
			if (other.responsavel != null)
				return false;
		} else if (!responsavel.equals(other.responsavel))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (statusobservacao == null) {
			if (other.statusobservacao != null)
				return false;
		} else if (!statusobservacao.equals(other.statusobservacao))
			return false;
		if (Double.doubleToLongBits(valorconcluido) != Double.doubleToLongBits(other.valorconcluido))
			return false;
		if (Double.doubleToLongBits(valorprevisto) != Double.doubleToLongBits(other.valorprevisto))
			return false;
		return true;
	}
	
	
}