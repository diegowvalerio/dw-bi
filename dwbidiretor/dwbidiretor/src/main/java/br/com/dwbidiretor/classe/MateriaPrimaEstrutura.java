package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class MateriaPrimaEstrutura implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private BigDecimal produtoid;
	private String nomeproduto;
	private BigDecimal vl_custo;
	
	private BigDecimal produtoid_acabado;
	private String nomeproduto_acabado;
	private BigDecimal qtde_estrutura;
	private BigDecimal custo_ficha;	
	private String tipoproduto;
	private BigDecimal custo_acabado;
	private BigDecimal valor_tabela;
	private BigDecimal tabelaprecoid;
	
	private BigDecimal novovalorvenda;
	
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
	public BigDecimal getVl_custo() {
		return vl_custo;
	}
	public void setVl_custo(BigDecimal vl_custo) {
		this.vl_custo = vl_custo;
	}
	public BigDecimal getProdutoid_acabado() {
		return produtoid_acabado;
	}
	public void setProdutoid_acabado(BigDecimal produtoid_acabado) {
		this.produtoid_acabado = produtoid_acabado;
	}
	public String getNomeproduto_acabado() {
		return nomeproduto_acabado;
	}
	public void setNomeproduto_acabado(String nomeproduto_acabado) {
		this.nomeproduto_acabado = nomeproduto_acabado;
	}
	public BigDecimal getQtde_estrutura() {
		return qtde_estrutura;
	}
	public void setQtde_estrutura(BigDecimal qtde_estrutura) {
		this.qtde_estrutura = qtde_estrutura;
	}
	public BigDecimal getCusto_ficha() {
		return custo_ficha;
	}
	public void setCusto_ficha(BigDecimal custo_ficha) {
		this.custo_ficha = custo_ficha;
	}
	public String getTipoproduto() {
		return tipoproduto;
	}
	public void setTipoproduto(String tipoproduto) {
		this.tipoproduto = tipoproduto;
	}
	public BigDecimal getCusto_acabado() {
		return custo_acabado;
	}
	public void setCusto_acabado(BigDecimal custo_acabado) {
		this.custo_acabado = custo_acabado;
	}
	public BigDecimal getValor_tabela() {
		return valor_tabela;
	}
	public void setValor_tabela(BigDecimal valor_tabela) {
		this.valor_tabela = valor_tabela;
	}
	public BigDecimal getTabelaprecoid() {
		return tabelaprecoid;
	}
	public void setTabelaprecoid(BigDecimal tabelaprecoid) {
		this.tabelaprecoid = tabelaprecoid;
	}
	public BigDecimal getNovovalorvenda() {
		return novovalorvenda;
	}
	public void setNovovalorvenda(BigDecimal novovalorvenda) {
		this.novovalorvenda = novovalorvenda;
	}
	
	
}
