package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class FasePedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal roteiroid;
	private String nomeroteiro;
	private BigDecimal vlpedido; 
	private BigDecimal qtdepedido;
	
	public FasePedido() {
		super();
	}

	public BigDecimal getRoteiroid() {
		return roteiroid;
	}

	public void setRoteiroid(BigDecimal roteiroid) {
		this.roteiroid = roteiroid;
	}

	public String getNomeroteiro() {
		return nomeroteiro;
	}

	public void setNomeroteiro(String nomeroteiro) {
		this.nomeroteiro = nomeroteiro;
	}

	public BigDecimal getVlpedido() {
		return vlpedido;
	}

	public void setVlpedido(BigDecimal vlpedido) {
		this.vlpedido = vlpedido;
	}

	public BigDecimal getQtdepedido() {
		return qtdepedido;
	}

	public void setQtdepedido(BigDecimal qtdepedido) {
		this.qtdepedido = qtdepedido;
	}

	
}
