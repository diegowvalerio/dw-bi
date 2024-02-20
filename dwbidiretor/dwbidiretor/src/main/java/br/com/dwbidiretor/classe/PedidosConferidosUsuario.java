package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class PedidosConferidosUsuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private BigDecimal usuario;
	private String nomeusuario;
	private BigDecimal pedidos;
	private BigDecimal valor;

	public PedidosConferidosUsuario() {
		super();
	}

	public BigDecimal getUsuario() {
		return usuario;
	}

	public void setUsuario(BigDecimal usuario) {
		this.usuario = usuario;
	}

	public String getNomeusuario() {
		return nomeusuario;
	}

	public void setNomeusuario(String nomeusuario) {
		this.nomeusuario = nomeusuario;
	}

	public BigDecimal getPedidos() {
		return pedidos;
	}

	public void setPedidos(BigDecimal pedidos) {
		this.pedidos = pedidos;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


}
