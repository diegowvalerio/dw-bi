package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;

import javax.persistence.*;

public class VendaAnoMes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, columnDefinition="varchar(4)")
	private String ano;
	@Column(nullable=false, columnDefinition="varchar(2)")
	private String mes;
	@Column(nullable=false, columnDefinition="numeric(6,2)")
	private BigDecimal valor;
		

	public VendaAnoMes() {
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


	public BigDecimal getValor() {
		return valor;
	}


	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}   
	
	
}
