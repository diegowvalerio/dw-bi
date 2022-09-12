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

public class CPedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//criado para verificar um pedido e realizar a liberação pelo comercial, e depois verificar a inadimplencia desses pedidos que foram liberado pelo comercial
	@Id
	private BigDecimal pedido;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private Date datapedido;
	private BigDecimal valortotalpedido;
	private String tipopedido;
	private String statuspedido;
	private String nomevendedor;
	private String nomegestor;
	
	private String liberado;

	public CPedido() {
		super();
	}

	public BigDecimal getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(BigDecimal codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public BigDecimal getPedido() {
		return pedido;
	}

	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
	}

	public Date getDatapedido() {
		return datapedido;
	}

	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
	}

	public BigDecimal getValortotalpedido() {
		return valortotalpedido;
	}

	public void setValortotalpedido(BigDecimal valortotalpedido) {
		this.valortotalpedido = valortotalpedido;
	}

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getStatuspedido() {
		return statuspedido;
	}

	public void setStatuspedido(String statuspedido) {
		this.statuspedido = statuspedido;
	}

	public String getNomegestor() {
		return nomegestor;
	}

	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
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
		CPedido other = (CPedido) obj;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		return true;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public String getLiberado() {
		return liberado;
	}

	public void setLiberado(String liberado) {
		this.liberado = liberado;
	}
	
	

}
