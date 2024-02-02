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

public class Nota implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sigla;
	private String tipopedido;
	private String nrnota;
	private BigDecimal cliente;
	private String nomecliente;
	private String dtnota;
	private String status;
	private String valor;
	private String chavexml;

	public Nota() {
		super();
	}

	public String getChavexml() {
		return chavexml;
	}

	public void setChavexml(String chavexml) {
		this.chavexml = chavexml;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getNrnota() {
		return nrnota;
	}

	public void setNrnota(String nrnota) {
		this.nrnota = nrnota;
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

	public String getDtnota() {
		return dtnota;
	}

	public void setDtnota(String dtnota) {
		this.dtnota = dtnota;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	
}
