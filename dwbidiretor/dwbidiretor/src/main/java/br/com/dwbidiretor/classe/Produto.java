package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal produtoid;
	private String nomeproduto;
	private String referencia;
	private BigDecimal grupoid;
	private BigDecimal subgrupoid;
	
	public BigDecimal getProdutoid() {
		return produtoid;
	}
	public void setProdutoid(BigDecimal produtoid) {
		this.produtoid = produtoid;
	}
	public String getNomeproduto() {
		return nomeproduto;
	}
	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public BigDecimal getGrupoid() {
		return grupoid;
	}
	public void setGrupoid(BigDecimal grupoid) {
		this.grupoid = grupoid;
	}
	public BigDecimal getSubgrupoid() {
		return subgrupoid;
	}
	public void setSubgrupoid(BigDecimal subgrupoid) {
		this.subgrupoid = subgrupoid;
	}
	
	
	
}
