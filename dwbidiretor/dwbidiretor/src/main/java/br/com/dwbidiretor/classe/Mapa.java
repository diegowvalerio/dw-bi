package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.google.gson.annotations.Expose;


public class Mapa implements Serializable {
	private static final long serialVersionUID = 1L;
	@Expose
	private BigDecimal vendedor;
	@Expose
	private String nomevendedor;
	@Expose
	private BigDecimal cliente;
	@Expose
	private String nomecliente;
	@Expose
	private String cidade;
	@Expose
	private String uf;
	@Expose
	private String endereco;
	@Expose
	private String bairro;
	@Expose
	private String cep;
	@Expose
	private String numero;
	@Expose
	private BigDecimal latitude;
	@Expose
	private BigDecimal longitude;
	@Expose
	private String ultimacompra;
	@Expose
	private BigDecimal totalperiodo;
	
	public Mapa() {
		super();
	}

	public BigDecimal getVendedor() {
		return vendedor;
	}

	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
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

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getUltimacompra() {
		return ultimacompra;
	}

	public void setUltimacompra(String ultimacompra) {
		this.ultimacompra = ultimacompra;
	}

	public BigDecimal getTotalperiodo() {
		return totalperiodo;
	}

	public void setTotalperiodo(BigDecimal totalperiodo) {
		this.totalperiodo = totalperiodo;
	}

}
