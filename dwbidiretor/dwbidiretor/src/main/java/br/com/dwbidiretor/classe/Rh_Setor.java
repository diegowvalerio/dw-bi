package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;


public class Rh_Setor implements Serializable {
	private static final long serialVersionUID = 1L;

	private String setor;
	private BigInteger qtde;
	private BigInteger qtde_geral;
	private BigDecimal perc_qtde_setor;
	private BigDecimal valor_setor;
	private BigDecimal valor_faturado;
	private BigDecimal perc_valor_setor;
	private BigDecimal perc_valor_pessoa;
	private BigDecimal valor_pessoa;
	private String setor_2;
	
	public String getSetor() {
		return setor;
	}
	public void setSetor(String setor) {
		this.setor = setor;
	}
	public BigInteger getQtde() {
		return qtde;
	}
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	public BigInteger getQtde_geral() {
		return qtde_geral;
	}
	public void setQtde_geral(BigInteger qtde_geral) {
		this.qtde_geral = qtde_geral;
	}
	public BigDecimal getPerc_qtde_setor() {
		return perc_qtde_setor;
	}
	public void setPerc_qtde_setor(BigDecimal perc_qtde_setor) {
		this.perc_qtde_setor = perc_qtde_setor;
	}
	public BigDecimal getValor_setor() {
		return valor_setor;
	}
	public void setValor_setor(BigDecimal valor_setor) {
		this.valor_setor = valor_setor;
	}
	public BigDecimal getValor_faturado() {
		return valor_faturado;
	}
	public void setValor_faturado(BigDecimal valor_faturado) {
		this.valor_faturado = valor_faturado;
	}
	public BigDecimal getPerc_valor_setor() {
		return perc_valor_setor;
	}
	public void setPerc_valor_setor(BigDecimal perc_valor_setor) {
		this.perc_valor_setor = perc_valor_setor;
	}
	public BigDecimal getPerc_valor_pessoa() {
		return perc_valor_pessoa;
	}
	public void setPerc_valor_pessoa(BigDecimal perc_valor_pessoa) {
		this.perc_valor_pessoa = perc_valor_pessoa;
	}
	public BigDecimal getValor_pessoa() {
		return valor_pessoa;
	}
	public void setValor_pessoa(BigDecimal valor_pessoa) {
		this.valor_pessoa = valor_pessoa;
	}
	public String getSetor_2() {
		return setor_2;
	}
	public void setSetor_2(String setor_2) {
		this.setor_2 = setor_2;
	}


	
}
