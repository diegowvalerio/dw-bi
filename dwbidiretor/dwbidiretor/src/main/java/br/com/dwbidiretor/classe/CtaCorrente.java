package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;


public class CtaCorrente implements Serializable {
	private static final long serialVersionUID = 1L;

	private String agencia;
	private String conta;
	private BigDecimal saldo;
	private BigDecimal saldoconciliado;
	private String titular;
	
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getConta() {
		return conta;
	}
	public void setConta(String conta) {
		this.conta = conta;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public BigDecimal getSaldoconciliado() {
		return saldoconciliado;
	}
	public void setSaldoconciliado(BigDecimal saldoconciliado) {
		this.saldoconciliado = saldoconciliado;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	
	
	
	
}
