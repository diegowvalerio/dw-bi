package br.com.dwbidiretor.classe.painel;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Cliente_Ano implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal clientes;
	public String ano;
		
	public Cliente_Ano() {
		super();
	}

	public BigDecimal getClientes() {
		return clientes;
	}

	public void setClientes(BigDecimal clientes) {
		this.clientes = clientes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}


}
