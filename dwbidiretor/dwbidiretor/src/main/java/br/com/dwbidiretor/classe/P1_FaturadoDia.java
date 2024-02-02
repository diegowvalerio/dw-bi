package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class P1_FaturadoDia implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ano;
	private String mes;
	private String dia;
	private BigDecimal faturado;
	

	public P1_FaturadoDia() {
		super();
	}


	public BigDecimal getFaturado() {
		return faturado;
	}


	public void setFaturado(BigDecimal faturado) {
		this.faturado = faturado;
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


	public String getDia() {
		return dia;
	}


	public void setDia(String dia) {
		this.dia = dia;
	}


}
