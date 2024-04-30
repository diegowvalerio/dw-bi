package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;


public class Rh_Folha implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ano;
	private String mes;
	private BigDecimal valor_faturado;
	private BigInteger qtde_clt;
	private BigDecimal valor_clt;
	private BigDecimal perc_clt;
	private BigDecimal valor_faturado_clt;
	private BigInteger qtde_pj;
	private BigDecimal valor_pj;
	private BigDecimal perc_pj;
	private BigDecimal valor_faturado_pj;
	private BigInteger total_funcionarios;
	private BigDecimal total_valor;
	
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
	public BigDecimal getValor_faturado() {
		return valor_faturado;
	}
	public void setValor_faturado(BigDecimal valor_faturado) {
		this.valor_faturado = valor_faturado;
	}
	public BigDecimal getValor_clt() {
		return valor_clt;
	}
	public void setValor_clt(BigDecimal valor_clt) {
		this.valor_clt = valor_clt;
	}
	public BigDecimal getPerc_clt() {
		return perc_clt;
	}
	public void setPerc_clt(BigDecimal perc_clt) {
		this.perc_clt = perc_clt;
	}
	public BigDecimal getValor_faturado_clt() {
		return valor_faturado_clt;
	}
	public void setValor_faturado_clt(BigDecimal valor_faturado_clt) {
		this.valor_faturado_clt = valor_faturado_clt;
	}
	public BigDecimal getValor_pj() {
		return valor_pj;
	}
	public void setValor_pj(BigDecimal valor_pj) {
		this.valor_pj = valor_pj;
	}
	public BigDecimal getPerc_pj() {
		return perc_pj;
	}
	public void setPerc_pj(BigDecimal perc_pj) {
		this.perc_pj = perc_pj;
	}
	public BigDecimal getValor_faturado_pj() {
		return valor_faturado_pj;
	}
	public void setValor_faturado_pj(BigDecimal valor_faturado_pj) {
		this.valor_faturado_pj = valor_faturado_pj;
	}
	public BigInteger getTotal_funcionarios() {
		return total_funcionarios;
	}
	public void setTotal_funcionarios(BigInteger total_funcionarios) {
		this.total_funcionarios = total_funcionarios;
	}
	public BigDecimal getTotal_valor() {
		return total_valor;
	}
	public void setTotal_valor(BigDecimal total_valor) {
		this.total_valor = total_valor;
	}
	public BigInteger getQtde_clt() {
		return qtde_clt;
	}
	public void setQtde_clt(BigInteger qtde_clt) {
		this.qtde_clt = qtde_clt;
	}
	public BigInteger getQtde_pj() {
		return qtde_pj;
	}
	public void setQtde_pj(BigInteger qtde_pj) {
		this.qtde_pj = qtde_pj;
	}

	
}
