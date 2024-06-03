package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class PercaProduto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String setor;
	public String ano;
	public String mes;
	public BigDecimal produtoid;
	public String nomeproduto;
	public BigDecimal quantidade;
	public BigDecimal tipopercaid;
	public String tipoperca;
	public BigDecimal valor;
	
	public BigDecimal getProdutoid() {
		return produtoid;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
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

	public BigDecimal getTipopercaid() {
		return tipopercaid;
	}

	public void setTipopercaid(BigDecimal tipopercaid) {
		this.tipopercaid = tipopercaid;
	}

	public String getTipoperca() {
		return tipoperca;
	}

	public void setTipoperca(String tipoperca) {
		this.tipoperca = tipoperca;
	}

	public PercaProduto() {
		super();
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	

	
}
