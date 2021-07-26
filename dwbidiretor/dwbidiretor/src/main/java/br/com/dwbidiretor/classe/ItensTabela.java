package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class ItensTabela implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal tabelaprecoid;
	private BigDecimal valor_tabela;
	private BigDecimal produtoid;
	private String nomeproduto;
	private BigDecimal novovalorvenda;
	public BigDecimal getTabelaprecoid() {
		return tabelaprecoid;
	}
	public void setTabelaprecoid(BigDecimal tabelaprecoid) {
		this.tabelaprecoid = tabelaprecoid;
	}
	public BigDecimal getValor_tabela() {
		return valor_tabela;
	}
	public void setValor_tabela(BigDecimal valor_tabela) {
		this.valor_tabela = valor_tabela;
	}
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
	public BigDecimal getNovovalorvenda() {
		return novovalorvenda;
	}
	public void setNovovalorvenda(BigDecimal novovalorvenda) {
		this.novovalorvenda = novovalorvenda;
	}
	
	
}
