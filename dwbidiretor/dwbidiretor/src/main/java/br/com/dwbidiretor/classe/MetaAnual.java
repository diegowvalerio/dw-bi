package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class MetaAnual implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String ano;
	private BigDecimal valor_meta_anual;
	private BigDecimal atingido_meta_anual;
	private BigDecimal perc_meta_anual;
	
	private String mes;
	private BigDecimal valor_meta_vendedor;
	private BigDecimal atingido_meta_vendedor;
	private BigDecimal perc_meta_vendedor;
	
	private String num_docs;
	private BigDecimal ticket_medio;
	private String clientes_atendidos;
	private String qtde_clientes_novos;
	
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public BigDecimal getValor_meta_anual() {
		return valor_meta_anual;
	}
	public void setValor_meta_anual(BigDecimal valor_meta_anual) {
		this.valor_meta_anual = valor_meta_anual;
	}
	public BigDecimal getAtingido_meta_anual() {
		return atingido_meta_anual;
	}
	public void setAtingido_meta_anual(BigDecimal atingido_meta_anual) {
		this.atingido_meta_anual = atingido_meta_anual;
	}
	public BigDecimal getPerc_meta_anual() {
		return perc_meta_anual;
	}
	public void setPerc_meta_anual(BigDecimal perc_meta_anual) {
		this.perc_meta_anual = perc_meta_anual;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public BigDecimal getValor_meta_vendedor() {
		return valor_meta_vendedor;
	}
	public void setValor_meta_vendedor(BigDecimal valor_meta_vendedor) {
		this.valor_meta_vendedor = valor_meta_vendedor;
	}
	public BigDecimal getAtingido_meta_vendedor() {
		return atingido_meta_vendedor;
	}
	public void setAtingido_meta_vendedor(BigDecimal atingido_meta_vendedor) {
		this.atingido_meta_vendedor = atingido_meta_vendedor;
	}
	public BigDecimal getPerc_meta_vendedor() {
		return perc_meta_vendedor;
	}
	public void setPerc_meta_vendedor(BigDecimal perc_meta_vendedor) {
		this.perc_meta_vendedor = perc_meta_vendedor;
	}
	public String getNum_docs() {
		return num_docs;
	}
	public void setNum_docs(String num_docs) {
		this.num_docs = num_docs;
	}
	public BigDecimal getTicket_medio() {
		return ticket_medio;
	}
	public void setTicket_medio(BigDecimal ticket_medio) {
		this.ticket_medio = ticket_medio;
	}
	public String getClientes_atendidos() {
		return clientes_atendidos;
	}
	public void setClientes_atendidos(String clientes_atendidos) {
		this.clientes_atendidos = clientes_atendidos;
	}
	public String getQtde_clientes_novos() {
		return qtde_clientes_novos;
	}
	public void setQtde_clientes_novos(String qtde_clientes_novos) {
		this.qtde_clientes_novos = qtde_clientes_novos;
	}
		

}
