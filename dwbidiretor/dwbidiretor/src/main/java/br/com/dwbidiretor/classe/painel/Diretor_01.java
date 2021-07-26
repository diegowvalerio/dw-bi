package br.com.dwbidiretor.classe.painel;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Diretor_01 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String ano;
	public String mes;
	public BigDecimal meta_anual;
	public BigDecimal atingido_anual;
	public BigDecimal perc_atingido_anual;
	
	public BigDecimal meta_mensal;
	public BigDecimal atingido_mensal;
	public BigDecimal perc_atingido_mensal;
	
	public BigDecimal num_docs;
	public BigDecimal ticket_medio;
	public BigDecimal clientes_atendidos;
	public BigDecimal clientes_novos;
	
	public BigDecimal meta_mensal_pedidos_p;
	public BigDecimal meta_mensal_faturamento_p;
	public BigDecimal atingido_mensal_pedido_p;
	public BigDecimal perc_atingido_mensal_pedido_p;
	
	public BigDecimal num_docs_anual;
	public BigDecimal ticket_medio_anual;
	
	public BigDecimal num_docs_pedido;
	public BigDecimal ticket_medio_pedido;
	
	
	
	public Diretor_01() {
		super();
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public BigDecimal getMeta_anual() {
		return meta_anual;
	}

	public void setMeta_anual(BigDecimal meta_anual) {
		this.meta_anual = meta_anual;
	}

	public BigDecimal getAtingido_anual() {
		return atingido_anual;
	}

	public void setAtingido_anual(BigDecimal atingido_anual) {
		this.atingido_anual = atingido_anual;
	}

	public BigDecimal getPerc_atingido_anual() {
		return perc_atingido_anual;
	}

	public void setPerc_atingido_anual(BigDecimal perc_atingido_anual) {
		this.perc_atingido_anual = perc_atingido_anual;
	}

	public BigDecimal getMeta_mensal() {
		return meta_mensal;
	}

	public void setMeta_mensal(BigDecimal meta_mensal) {
		this.meta_mensal = meta_mensal;
	}

	public BigDecimal getAtingido_mensal() {
		return atingido_mensal;
	}

	public void setAtingido_mensal(BigDecimal atingido_mensal) {
		this.atingido_mensal = atingido_mensal;
	}

	public BigDecimal getPerc_atingido_mensal() {
		return perc_atingido_mensal;
	}

	public void setPerc_atingido_mensal(BigDecimal perc_atingido_mensal) {
		this.perc_atingido_mensal = perc_atingido_mensal;
	}

	public BigDecimal getNum_docs() {
		return num_docs;
	}

	public void setNum_docs(BigDecimal num_docs) {
		this.num_docs = num_docs;
	}

	public BigDecimal getTicket_medio() {
		return ticket_medio;
	}

	public void setTicket_medio(BigDecimal ticket_medio) {
		this.ticket_medio = ticket_medio;
	}

	public BigDecimal getClientes_atendidos() {
		return clientes_atendidos;
	}

	public void setClientes_atendidos(BigDecimal clientes_atendidos) {
		this.clientes_atendidos = clientes_atendidos;
	}

	public BigDecimal getClientes_novos() {
		return clientes_novos;
	}

	public void setClientes_novos(BigDecimal clientes_novos) {
		this.clientes_novos = clientes_novos;
	}

	public BigDecimal getMeta_mensal_pedidos_p() {
		return meta_mensal_pedidos_p;
	}

	public void setMeta_mensal_pedidos_p(BigDecimal meta_mensal_pedidos_p) {
		this.meta_mensal_pedidos_p = meta_mensal_pedidos_p;
	}

	public BigDecimal getMeta_mensal_faturamento_p() {
		return meta_mensal_faturamento_p;
	}

	public void setMeta_mensal_faturamento_p(BigDecimal meta_mensal_faturamento_p) {
		this.meta_mensal_faturamento_p = meta_mensal_faturamento_p;
	}

	public BigDecimal getAtingido_mensal_pedido_p() {
		return atingido_mensal_pedido_p;
	}

	public void setAtingido_mensal_pedido_p(BigDecimal atingido_mensal_pedido_p) {
		this.atingido_mensal_pedido_p = atingido_mensal_pedido_p;
	}

	public BigDecimal getPerc_atingido_mensal_pedido_p() {
		return perc_atingido_mensal_pedido_p;
	}

	public void setPerc_atingido_mensal_pedido_p(BigDecimal perc_atingido_mensal_pedido_p) {
		this.perc_atingido_mensal_pedido_p = perc_atingido_mensal_pedido_p;
	}

	public BigDecimal getNum_docs_anual() {
		return num_docs_anual;
	}

	public void setNum_docs_anual(BigDecimal num_docs_anual) {
		this.num_docs_anual = num_docs_anual;
	}

	public BigDecimal getTicket_medio_anual() {
		return ticket_medio_anual;
	}

	public void setTicket_medio_anual(BigDecimal ticket_medio_anual) {
		this.ticket_medio_anual = ticket_medio_anual;
	}

	public BigDecimal getNum_docs_pedido() {
		return num_docs_pedido;
	}

	public void setNum_docs_pedido(BigDecimal num_docs_pedido) {
		this.num_docs_pedido = num_docs_pedido;
	}

	public BigDecimal getTicket_medio_pedido() {
		return ticket_medio_pedido;
	}

	public void setTicket_medio_pedido(BigDecimal ticket_medio_pedido) {
		this.ticket_medio_pedido = ticket_medio_pedido;
	}

	
}
