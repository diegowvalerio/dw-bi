package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class TabProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal tabelaprecoid;
	private String nometabela;
	private BigDecimal desc_max;
	private BigDecimal produtoid;
	private String nomeproduto;
	private BigDecimal valor_unitario;
	private BigDecimal valor_unitario_liquido;
	private String promocao;
	private BigDecimal valor_promocao;
	private BigDecimal valor_custo;
	public BigDecimal getTabelaprecoid() {
		return tabelaprecoid;
	}
	public void setTabelaprecoid(BigDecimal tabelaprecoid) {
		this.tabelaprecoid = tabelaprecoid;
	}
	public String getNometabela() {
		return nometabela;
	}
	public void setNometabela(String nometabela) {
		this.nometabela = nometabela;
	}
	public BigDecimal getDesc_max() {
		return desc_max;
	}
	public void setDesc_max(BigDecimal desc_max) {
		this.desc_max = desc_max;
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
	public BigDecimal getValor_unitario() {
		return valor_unitario;
	}
	public void setValor_unitario(BigDecimal valor_unitario) {
		this.valor_unitario = valor_unitario;
	}
	public BigDecimal getValor_unitario_liquido() {
		return valor_unitario_liquido;
	}
	public void setValor_unitario_liquido(BigDecimal valor_unitario_liquido) {
		this.valor_unitario_liquido = valor_unitario_liquido;
	}
	public String getPromocao() {
		return promocao;
	}
	public void setPromocao(String promocao) {
		this.promocao = promocao;
	}
	public BigDecimal getValor_promocao() {
		return valor_promocao;
	}
	public void setValor_promocao(BigDecimal valor_promocao) {
		this.valor_promocao = valor_promocao;
	}
	public BigDecimal getValor_custo() {
		return valor_custo;
	}
	public void setValor_custo(BigDecimal valor_custo) {
		this.valor_custo = valor_custo;
	}
	
	
	
}
