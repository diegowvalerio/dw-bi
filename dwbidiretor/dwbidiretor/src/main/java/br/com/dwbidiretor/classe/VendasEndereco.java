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

public class VendasEndereco implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal pedido;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private String endereco;
	private String numero;
	private String cidade;
	private String cep;
	private String uf;
	private BigDecimal codigovendedor;
	private String nomevendedor;
	private String nomegestor;
	
	private Date datapedido;
	private BigDecimal valortotalpedido;
		

	public VendasEndereco() {
		super();
	}


	public BigDecimal getPedido() {
		return pedido;
	}


	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
	}


	public BigDecimal getCodigocliente() {
		return codigocliente;
	}


	public void setCodigocliente(BigDecimal codigocliente) {
		this.codigocliente = codigocliente;
	}


	public String getNomecliente() {
		return nomecliente;
	}


	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getCidade() {
		return cidade;
	}


	public void setCidade(String cidade) {
		this.cidade = cidade;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getUf() {
		return uf;
	}


	public void setUf(String uf) {
		this.uf = uf;
	}


	public String getNomevendedor() {
		return nomevendedor;
	}


	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}


	public String getNomegestor() {
		return nomegestor;
	}


	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}


	public Date getDatapedido() {
		return datapedido;
	}


	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
	}


	public BigDecimal getValortotalpedido() {
		return valortotalpedido;
	}


	public void setValortotalpedido(BigDecimal valortotalpedido) {
		this.valortotalpedido = valortotalpedido;
	}


	public BigDecimal getCodigovendedor() {
		return codigovendedor;
	}


	public void setCodigovendedor(BigDecimal codigovendedor) {
		this.codigovendedor = codigovendedor;
	}

	
	

}
