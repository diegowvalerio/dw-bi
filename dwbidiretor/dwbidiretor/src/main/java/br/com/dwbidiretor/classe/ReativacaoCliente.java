package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class ReativacaoCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal clienteid;
	private String nomecliente;
	private String cnpjcliente;
	private String ufcliente;
	private String cidadecliente;
	private Date primeiracompra;
	private Date pnultimacompra;
	private Date ultimacompra;
	private Double qtdediaspnultimacompra;
	private BigDecimal gestorid;
	private String nomegestor;
	private BigDecimal vendedorid;
	private String nomevendedor;
	public BigDecimal getClienteid() {
		return clienteid;
	}
	public void setClienteid(BigDecimal clienteid) {
		this.clienteid = clienteid;
	}
	public String getNomecliente() {
		return nomecliente;
	}
	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}
	public String getCnpjcliente() {
		return cnpjcliente;
	}
	public void setCnpjcliente(String cnpjcliente) {
		this.cnpjcliente = cnpjcliente;
	}
	public String getUfcliente() {
		return ufcliente;
	}
	public void setUfcliente(String ufcliente) {
		this.ufcliente = ufcliente;
	}
	public String getCidadecliente() {
		return cidadecliente;
	}
	public void setCidadecliente(String cidadecliente) {
		this.cidadecliente = cidadecliente;
	}
	public Date getPrimeiracompra() {
		return primeiracompra;
	}
	public void setPrimeiracompra(Date primeiracompra) {
		this.primeiracompra = primeiracompra;
	}
	public Date getPnultimacompra() {
		return pnultimacompra;
	}
	public void setPnultimacompra(Date pnultimacompra) {
		this.pnultimacompra = pnultimacompra;
	}
	public Date getUltimacompra() {
		return ultimacompra;
	}
	public void setUltimacompra(Date ultimacompra) {
		this.ultimacompra = ultimacompra;
	}
	public Double getQtdediaspnultimacompra() {
		return qtdediaspnultimacompra;
	}
	public void setQtdediaspnultimacompra(Double qtdediaspnultimacompra) {
		this.qtdediaspnultimacompra = qtdediaspnultimacompra;
	}
	public BigDecimal getGestorid() {
		return gestorid;
	}
	public void setGestorid(BigDecimal gestorid) {
		this.gestorid = gestorid;
	}
	public String getNomegestor() {
		return nomegestor;
	}
	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}
	public BigDecimal getVendedorid() {
		return vendedorid;
	}
	public void setVendedorid(BigDecimal vendedorid) {
		this.vendedorid = vendedorid;
	}
	public String getNomevendedor() {
		return nomevendedor;
	}
	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}
	

	
}
