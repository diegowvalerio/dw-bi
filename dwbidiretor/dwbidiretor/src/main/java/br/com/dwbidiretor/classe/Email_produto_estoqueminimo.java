package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class Email_produto_estoqueminimo implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal produtoid;
	private String nomeproduto;
	private BigDecimal saldoatual;
	private BigDecimal saldominimo;
	
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
	public BigDecimal getSaldoatual() {
		return saldoatual;
	}
	public void setSaldoatual(BigDecimal saldoatual) {
		this.saldoatual = saldoatual;
	}
	public BigDecimal getSaldominimo() {
		return saldominimo;
	}
	public void setSaldominimo(BigDecimal saldominimo) {
		this.saldominimo = saldominimo;
	}
	
	
	
	
}
