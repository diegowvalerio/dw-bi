package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class ProdutoEstoque implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal produtoid;
	private String nomeproduto;
	private String referencia;
	private String grupo;
	private String subgrupo;
	private String tipoproduto;
	private String almoxarifado;
	private BigDecimal estoque;
	private String nome_tipoproduto;
	
	public ProdutoEstoque() {
		super();
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
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	public String getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}
	public String getTipoproduto() {
		return tipoproduto;
	}
	public void setTipoproduto(String tipoproduto) {
		this.tipoproduto = tipoproduto;
	}
	public String getAlmoxarifado() {
		return almoxarifado;
	}
	public void setAlmoxarifado(String almoxarifado) {
		this.almoxarifado = almoxarifado;
	}
	public BigDecimal getEstoque() {
		return estoque;
	}
	public void setEstoque(BigDecimal estoque) {
		this.estoque = estoque;
	}

	public String getNome_tipoproduto() {
		return nome_tipoproduto;
	}

	public void setNome_tipoproduto(String nome_tipoproduto) {
		this.nome_tipoproduto = nome_tipoproduto;
	}
	
	
	
	
	
}
