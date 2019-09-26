package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


public class Mapa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal vendedor;
	private String nomevendedor;
	private BigDecimal cliente;
	private String nomecliente;
	
	private String cidade;
	private String uf;
	private String endereco;
	private String bairro;
	private String cep;
	private String numero;
	
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	private Date ultimacompra;
	
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

	public Date getUltimacompra() {
		return ultimacompra;
	}

	public void setUltimacompra(Date ultimacompra) {
		this.ultimacompra = ultimacompra;
	}

}
