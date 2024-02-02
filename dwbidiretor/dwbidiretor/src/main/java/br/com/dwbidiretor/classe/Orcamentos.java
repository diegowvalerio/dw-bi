package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class Orcamentos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano;
	private String mes;
	private String nmes;
	private BigDecimal cancelados;
	private BigDecimal abertos;

	public Orcamentos() {
		super();
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getNmes() {
		return nmes;
	}

	public void setNmes(String nmes) {
		this.nmes = nmes;
	}

	public BigDecimal getCancelados() {
		return cancelados;
	}

	public void setCancelados(BigDecimal cancelados) {
		this.cancelados = cancelados;
	}

	public BigDecimal getAbertos() {
		return abertos;
	}

	public void setAbertos(BigDecimal abertos) {
		this.abertos = abertos;
	}

	
}
