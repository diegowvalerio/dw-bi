package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class PedidoFase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal pedidoid;
	private Date datapedido;
	private BigDecimal codigocliente;
	private String nomecliente;
	private BigDecimal valor;
	private String tipopedido;
	private String status;
	private String dataentradafase;
	private Double diasnafase;
	private String lote;

	public PedidoFase() {
		super();
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public BigDecimal getPedidoid() {
		return pedidoid;
	}

	public void setPedidoid(BigDecimal pedidoid) {
		this.pedidoid = pedidoid;
	}

	public Date getDatapedido() {
		return datapedido;
	}

	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getDiasnafase() {
		return diasnafase;
	}

	public void setDiasnafase(Double diasnafase) {
		this.diasnafase = diasnafase;
	}

	public String getDataentradafase() {
		return dataentradafase;
	}

	public void setDataentradafase(String dataentradafase) {
		this.dataentradafase = dataentradafase;
	}

	

}
