package br.com.dwbidiretor.classe.painel;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Vendedor_Ano implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal vendedores;
	public String ano;
		
	public Vendedor_Ano() {
		super();
	}

	public BigDecimal getVendedores() {
		return vendedores;
	}

	public void setVendedores(BigDecimal vendedores) {
		this.vendedores = vendedores;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}
	
}
