package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Producao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal setorid;
	public String setor;
	public String ano;
	public String mes;
	public BigDecimal quantidade;
	
	public Producao() {
		super();
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
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

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getSetorid() {
		return setorid;
	}

	public void setSetorid(BigDecimal setorid) {
		this.setorid = setorid;
	}

	

	
}
