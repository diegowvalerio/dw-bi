package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class VendaCusto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano;
	private String mes;
	private BigDecimal venda;
	private BigDecimal custo;
	private BigDecimal percentual;
	private String qtde;
	private BigDecimal qtdeproducao;

	public VendaCusto() {
		super();
	}


	public String getQtde() {
		return qtde;
	}


	public void setQtde(String qtde) {
		this.qtde = qtde;
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


	public BigDecimal getVenda() {
		return venda;
	}


	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}


	public BigDecimal getCusto() {
		return custo;
	}


	public void setCusto(BigDecimal custo) {
		this.custo = custo;
	}


	public BigDecimal getPercentual() {
		return percentual;
	}


	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}


	public BigDecimal getQtdeproducao() {
		return qtdeproducao;
	}


	public void setQtdeproducao(BigDecimal qtdeproducao) {
		this.qtdeproducao = qtdeproducao;
	}


}
