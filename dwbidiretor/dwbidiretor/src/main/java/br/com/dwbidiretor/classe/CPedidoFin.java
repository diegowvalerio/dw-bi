package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

public class CPedidoFin implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//criado para verificar um pedido e realizar a liberação pelo comercial, e depois verificar a inadimplencia desses pedidos que foram liberado pelo comercial
	//filtrar por vendedor /gestor /data de faturamento / ***cliente / status do titulo
	@Id
	private BigDecimal pedido;
	private String nomecliente;
	private String nomevendedor;
	private String nomegestor;
	private String statuspedido;
	private String nrnota;
	private Date datafaturamento;
	private String statusnota;
	private BigDecimal tituloid;
	private String nrdocumento;
	private String nrparcela;
	private String statustitulo;
	private Date datatitulo;
	private String origem;
	private BigDecimal valortitulo;
	private Date datavencimento;
	private Date dataquitacao;
	
	private Integer bo_vencido; // 1 - vencido / 0 - aberto

	public CPedidoFin() {
		super();
	}

	public Integer getBo_vencido() {
		return bo_vencido;
	}

	public void setBo_vencido(Integer bo_vencido) {
		this.bo_vencido = bo_vencido;
	}

	public BigDecimal getPedido() {
		return pedido;
	}

	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getStatuspedido() {
		return statuspedido;
	}

	public void setStatuspedido(String statuspedido) {
		this.statuspedido = statuspedido;
	}

	public String getNrnota() {
		return nrnota;
	}

	public void setNrnota(String nrnota) {
		this.nrnota = nrnota;
	}

	public String getStatusnota() {
		return statusnota;
	}

	public void setStatusnota(String statusnota) {
		this.statusnota = statusnota;
	}

	public BigDecimal getTituloid() {
		return tituloid;
	}

	public void setTituloid(BigDecimal tituloid) {
		this.tituloid = tituloid;
	}

	public String getNrdocumento() {
		return nrdocumento;
	}

	public void setNrdocumento(String nrdocumento) {
		this.nrdocumento = nrdocumento;
	}

	public String getNrparcela() {
		return nrparcela;
	}

	public void setNrparcela(String nrparcela) {
		this.nrparcela = nrparcela;
	}

	public String getStatustitulo() {
		return statustitulo;
	}

	public void setStatustitulo(String statustitulo) {
		this.statustitulo = statustitulo;
	}

	public Date getDatatitulo() {
		return datatitulo;
	}

	public void setDatatitulo(Date datatitulo) {
		this.datatitulo = datatitulo;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public BigDecimal getValortitulo() {
		return valortitulo;
	}

	public void setValortitulo(BigDecimal valortitulo) {
		this.valortitulo = valortitulo;
	}

	public Date getDatavencimento() {
		return datavencimento;
	}

	public void setDatavencimento(Date datavencimento) {
		this.datavencimento = datavencimento;
	}

	public Date getDataquitacao() {
		return dataquitacao;
	}

	public void setDataquitacao(Date dataquitacao) {
		this.dataquitacao = dataquitacao;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public String getNomegestor() {
		return nomegestor;
	}

	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}

	public Date getDatafaturamento() {
		return datafaturamento;
	}

	public void setDatafaturamento(Date datafaturamento) {
		this.datafaturamento = datafaturamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
		result = prime * result + ((tituloid == null) ? 0 : tituloid.hashCode());
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
		CPedidoFin other = (CPedidoFin) obj;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		if (tituloid == null) {
			if (other.tituloid != null)
				return false;
		} else if (!tituloid.equals(other.tituloid))
			return false;
		return true;
	}

		

}
