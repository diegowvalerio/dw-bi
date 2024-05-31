package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class ProdutoVenda implements Serializable {
	private static final long serialVersionUID = 1L;
	

	private String nota;
    private Date datanota;
    private BigDecimal cliente;
    private String nomecliente;
	private BigDecimal produto;
	private String nomeproduto;
	private BigDecimal qtde; 
			

	public ProdutoVenda() {
		super();
	}


	public String getNota() {
		return nota;
	}


	public void setNota(String nota) {
		this.nota = nota;
	}


	public Date getDatanota() {
		return datanota;
	}


	public void setDatanota(Date datanota) {
		this.datanota = datanota;
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


	public BigDecimal getProduto() {
		return produto;
	}


	public void setProduto(BigDecimal produto) {
		this.produto = produto;
	}


	public String getNomeproduto() {
		return nomeproduto;
	}


	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}


	public BigDecimal getQtde() {
		return qtde;
	}


	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
}
