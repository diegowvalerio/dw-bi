package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class ProdutoRanking implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal produto;
	private String nomeproduto;
	private BigDecimal venda;
	private BigDecimal qtde;
	private String nomemesa;
	

	public ProdutoRanking() {
		super();
	}


	public String getNomemesa() {
		return nomemesa;
	}


	public void setNomemesa(String nomemesa) {
		this.nomemesa = nomemesa;
	}


	public BigDecimal getProduto() {
		return produto;
	}


	public void setProduto(BigDecimal produto) {
		this.produto = produto;
	}


	public String getNomeproduto() {
		return nomeproduto;
	}


	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}


	public BigDecimal getVenda() {
		return venda;
	}


	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}


	public BigDecimal getQtde() {
		return qtde;
	}


	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}

}
