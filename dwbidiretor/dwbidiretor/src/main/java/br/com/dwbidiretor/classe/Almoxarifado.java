package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Almoxarifado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal almoxarifadoid;
	private String nome;

	public Almoxarifado() {
		super();
	}

	public BigDecimal getAlmoxarifadoid() {
		return almoxarifadoid;
	}

	public void setAlmoxarifadoid(BigDecimal almoxarifadoid) {
		this.almoxarifadoid = almoxarifadoid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
}
