package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

public class Titulo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal id;
	private String sigla;
	private String nrdocumento;
	private String nrparcela;
	private BigDecimal cliente;
	private String nomecliente;
	private String uf;
	private String cidade;
	private String formacobranca;
	private String dtvencimento;
	private String status;
	private String status2;
	private String origem;
	private String valor;
	private String email;
	private String emailoutro;

	public Titulo() {
		super();
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNrdocumento() {
		return nrdocumento;
	}

	public void setNrdocumento(String nrdocumento) {
		this.nrdocumento = nrdocumento;
	}

	public String getNrparcela() {
		return nrparcela;
	}

	public void setNrparcela(String nrparcela) {
		this.nrparcela = nrparcela;
	}

	public BigDecimal getCliente() {
		return cliente;
	}

	public void setCliente(BigDecimal cliente) {
		this.cliente = cliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getFormacobranca() {
		return formacobranca;
	}

	public void setFormacobranca(String formacobranca) {
		this.formacobranca = formacobranca;
	}

	public String getDtvencimento() {
		return dtvencimento;
	}

	public void setDtvencimento(String dtvencimento) {
		this.dtvencimento = dtvencimento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus2() {
		return status2;
	}

	public void setStatus2(String status2) {
		this.status2 = status2;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailoutro() {
		return emailoutro;
	}

	public void setEmailoutro(String emailoutro) {
		this.emailoutro = emailoutro;
	}

	

}
