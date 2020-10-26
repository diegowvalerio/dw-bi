package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;

import javax.persistence.*;


public class RetornoAfinacao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal produto_usinado;
	private String nomeproduto_usinado;
	private BigDecimal qtde_usinado;
	
	private BigDecimal produto_afinado;
	private String nomeproduto_afinado;
	private BigDecimal valor_servicoafinado;
	private BigDecimal valortotal_servicoafinado;
	
	private BigDecimal produto_cromado;
	private String nomeproduto_cromado;
	private BigDecimal qtde_cromado;
	
	
	public BigDecimal getQtde_cromado() {
		return qtde_cromado;
	}


	public void setQtde_cromado(BigDecimal qtde_cromado) {
		this.qtde_cromado = qtde_cromado;
	}


	public RetornoAfinacao() {
		super();
	}


	public BigDecimal getProduto_usinado() {
		return produto_usinado;
	}


	public void setProduto_usinado(BigDecimal produto_usinado) {
		this.produto_usinado = produto_usinado;
	}


	public String getNomeproduto_usinado() {
		return nomeproduto_usinado;
	}


	public void setNomeproduto_usinado(String nomeproduto_usinado) {
		this.nomeproduto_usinado = nomeproduto_usinado;
	}

	public BigDecimal getQtde_usinado() {
		return qtde_usinado;
	}


	public void setQtde_usinado(BigDecimal qtde_usinado) {
		this.qtde_usinado = qtde_usinado;
	}


	public BigDecimal getProduto_afinado() {
		return produto_afinado;
	}


	public void setProduto_afinado(BigDecimal produto_afinado) {
		this.produto_afinado = produto_afinado;
	}


	public String getNomeproduto_afinado() {
		return nomeproduto_afinado;
	}


	public void setNomeproduto_afinado(String nomeproduto_afinado) {
		this.nomeproduto_afinado = nomeproduto_afinado;
	}


	public BigDecimal getValor_servicoafinado() {
		return valor_servicoafinado;
	}


	public void setValor_servicoafinado(BigDecimal valor_servicoafinado) {
		this.valor_servicoafinado = valor_servicoafinado;
	}


	public BigDecimal getValortotal_servicoafinado() {
		return valortotal_servicoafinado;
	}


	public void setValortotal_servicoafinado(BigDecimal valortotal_servicoafinado) {
		this.valortotal_servicoafinado = valortotal_servicoafinado;
	}


	public BigDecimal getProduto_cromado() {
		return produto_cromado;
	}


	public void setProduto_cromado(BigDecimal produto_cromado) {
		this.produto_cromado = produto_cromado;
	}


	public String getNomeproduto_cromado() {
		return nomeproduto_cromado;
	}


	public void setNomeproduto_cromado(String nomeproduto_cromado) {
		this.nomeproduto_cromado = nomeproduto_cromado;
	}

	

}
