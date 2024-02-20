package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class FasePedidoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal pedido;
	private BigDecimal produto;
	private String nomeproduto; 
	private BigDecimal qtdepedido;
	private BigDecimal qtdereservada;
	private BigDecimal faltapedido;
	private BigDecimal saldoatual;
	private BigDecimal faltapedidoestoque;
	private BigDecimal qtdepedidogeral;
	private BigDecimal qtdereservadageral;
	private BigDecimal faltapedidogeral;
	private BigDecimal faltapedidoestoquegeral;
	private String importado;
	
	public FasePedidoItem() {
		super();
	}

	public BigDecimal getPedido() {
		return pedido;
	}

	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
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

	public BigDecimal getQtdepedido() {
		return qtdepedido;
	}

	public void setQtdepedido(BigDecimal qtdepedido) {
		this.qtdepedido = qtdepedido;
	}

	public BigDecimal getQtdereservada() {
		return qtdereservada;
	}

	public void setQtdereservada(BigDecimal qtdereservada) {
		this.qtdereservada = qtdereservada;
	}

	public BigDecimal getFaltapedido() {
		return faltapedido;
	}

	public void setFaltapedido(BigDecimal faltapedido) {
		this.faltapedido = faltapedido;
	}

	public BigDecimal getSaldoatual() {
		return saldoatual;
	}

	public void setSaldoatual(BigDecimal saldoatual) {
		this.saldoatual = saldoatual;
	}

	public BigDecimal getFaltapedidoestoque() {
		return faltapedidoestoque;
	}

	public void setFaltapedidoestoque(BigDecimal faltapedidoestoque) {
		this.faltapedidoestoque = faltapedidoestoque;
	}

	public BigDecimal getQtdepedidogeral() {
		return qtdepedidogeral;
	}

	public void setQtdepedidogeral(BigDecimal qtdepedidogeral) {
		this.qtdepedidogeral = qtdepedidogeral;
	}

	public BigDecimal getQtdereservadageral() {
		return qtdereservadageral;
	}

	public void setQtdereservadageral(BigDecimal qtdereservadageral) {
		this.qtdereservadageral = qtdereservadageral;
	}

	public BigDecimal getFaltapedidogeral() {
		return faltapedidogeral;
	}

	public void setFaltapedidogeral(BigDecimal faltapedidogeral) {
		this.faltapedidogeral = faltapedidogeral;
	}

	public BigDecimal getFaltapedidoestoquegeral() {
		return faltapedidoestoquegeral;
	}

	public void setFaltapedidoestoquegeral(BigDecimal faltapedidoestoquegeral) {
		this.faltapedidoestoquegeral = faltapedidoestoquegeral;
	}

	public String getImportado() {
		return importado;
	}

	public void setImportado(String importado) {
		this.importado = importado;
	}

	
}
