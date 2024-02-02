package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class P1_MetaFaturado implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal faturado;
	private BigDecimal meta;
	private BigDecimal perc;
	

	public P1_MetaFaturado() {
		super();
	}


	public BigDecimal getFaturado() {
		return faturado;
	}


	public void setFaturado(BigDecimal faturado) {
		this.faturado = faturado;
	}


	public BigDecimal getMeta() {
		return meta;
	}


	public void setMeta(BigDecimal meta) {
		this.meta = meta;
	}


	public BigDecimal getPerc() {
		return perc;
	}


	public void setPerc(BigDecimal perc) {
		this.perc = perc;
	}



}
