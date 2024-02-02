package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class NovasVendas_Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano;
	private String mes;
	private String nmes;
	private BigDecimal novosvenda;
	private BigDecimal novoscadastro;


	public NovasVendas_Cliente() {
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


	public BigDecimal getNovosvenda() {
		return novosvenda;
	}


	public void setNovosvenda(BigDecimal novosvenda) {
		this.novosvenda = novosvenda;
	}


	public BigDecimal getNovoscadastro() {
		return novoscadastro;
	}


	public void setNovoscadastro(BigDecimal novoscadastro) {
		this.novoscadastro = novoscadastro;
	}


	

}