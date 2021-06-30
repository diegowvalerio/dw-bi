package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class LoteEstoque implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal loteid;
	private Date datalote;
	private BigDecimal produtoid;
	private String nomeproduto;
	private BigDecimal qtde; 
	private BigDecimal saldo;
	private BigDecimal perc_atendido;
	
	public LoteEstoque() {
		super();
	}

	public BigDecimal getLoteid() {
		return loteid;
	}

	public void setLoteid(BigDecimal loteid) {
		this.loteid = loteid;
	}

	public Date getDatalote() {
		return datalote;
	}

	public void setDatalote(Date datalote) {
		this.datalote = datalote;
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

	public BigDecimal getQtde() {
		return qtde;
	}

	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getPerc_atendido() {
		return perc_atendido;
	}

	public void setPerc_atendido(BigDecimal perc_atendido) {
		this.perc_atendido = perc_atendido;
	}

	
}
