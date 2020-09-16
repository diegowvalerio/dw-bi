package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class PedidosConferidos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date datapedido;
	private BigDecimal pedido;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String tipopedido;
	private BigDecimal peso;
	private BigDecimal volume;	
	private String datainicio;
	private String datafim;
	private BigDecimal vlrpedido;
	private String usuario;

	public PedidosConferidos() {
		super();
	}

	public Date getDatapedido() {
		return datapedido;
	}

	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
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

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public BigDecimal getPeso() {
		return peso;
	}

	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}


	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getVlrpedido() {
		return vlrpedido;
	}

	public void setVlrpedido(BigDecimal vlrpedido) {
		this.vlrpedido = vlrpedido;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getDatainicio() {
		return datainicio;
	}

	public void setDatainicio(String datainicio) {
		this.datainicio = datainicio;
	}

	public String getDatafim() {
		return datafim;
	}

	public void setDatafim(String datafim) {
		this.datafim = datafim;
	}

	
	

}
