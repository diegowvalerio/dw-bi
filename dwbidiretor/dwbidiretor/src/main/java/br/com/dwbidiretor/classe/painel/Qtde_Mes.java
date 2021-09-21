package br.com.dwbidiretor.classe.painel;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Qtde_Mes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal qtde;
	public String ano;
	public String mes;
		
	public Qtde_Mes() {
		super();
	}

	public BigDecimal getQtde() {
		return qtde;
	}

	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
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

	
}
