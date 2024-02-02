package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class PT_Carteira implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String regiao;
	@Id
	private BigDecimal vendedor;
	private String nomevendedor;
	
	private BigInteger ativos; 
	private BigInteger ativos_mes;
	private Double perc_evolucao;
	private Integer pontos_evolucao;
	private BigDecimal novos; 
	private BigDecimal perc_novos;
	private BigDecimal pontos_novos;
	private BigDecimal reativados;
	private BigDecimal perc_reativado; 
	private BigDecimal pontos_reativados;
	
	private BigInteger ativos2; 
	private BigInteger ativos_mes2;
	private Integer perc_evolucao2;
	private Integer pontos_evolucao2;
	private BigDecimal novos2; 
	private BigDecimal perc_novos2;
	private BigDecimal pontos_novos2;
	private BigDecimal reativados2;
	private BigDecimal perc_reativado2; 
	private BigDecimal pontos_reativados2;
	
	private BigInteger ativos3; 
	private BigInteger ativos_mes3;
	private Integer perc_evolucao3;
	private Integer pontos_evolucao3;
	private BigDecimal novos3; 
	private BigDecimal perc_novos3;
	private BigDecimal pontos_novos3;
	private BigDecimal reativados3;
	private BigDecimal perc_reativado3; 
	private BigDecimal pontos_reativados3;
	
	private BigDecimal totaltri1;
	
	
	public PT_Carteira() {
		super();
	}


	public String getRegiao() {
		return regiao;
	}


	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}


	public BigDecimal getVendedor() {
		return vendedor;
	}


	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}


	public String getNomevendedor() {
		return nomevendedor;
	}


	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}


	public BigInteger getAtivos() {
		return ativos;
	}


	public void setAtivos(BigInteger ativos) {
		this.ativos = ativos;
	}


	public BigInteger getAtivos_mes() {
		return ativos_mes;
	}


	public void setAtivos_mes(BigInteger ativos_mes) {
		this.ativos_mes = ativos_mes;
	}


	public Double getPerc_evolucao() {
		return perc_evolucao;
	}


	public void setPerc_evolucao(Double perc_evolucao) {
		this.perc_evolucao = perc_evolucao;
	}

	public BigDecimal getNovos() {
		return novos;
	}


	public void setNovos(BigDecimal novos) {
		this.novos = novos;
	}


	public BigDecimal getPerc_novos() {
		return perc_novos;
	}


	public void setPerc_novos(BigDecimal perc_novos) {
		this.perc_novos = perc_novos;
	}


	public BigDecimal getPontos_novos() {
		return pontos_novos;
	}


	public void setPontos_novos(BigDecimal pontos_novos) {
		this.pontos_novos = pontos_novos;
	}


	public BigDecimal getReativados() {
		return reativados;
	}


	public void setReativados(BigDecimal reativados) {
		this.reativados = reativados;
	}


	public BigDecimal getPerc_reativado() {
		return perc_reativado;
	}


	public void setPerc_reativado(BigDecimal perc_reativado) {
		this.perc_reativado = perc_reativado;
	}


	public BigDecimal getPontos_reativados() {
		return pontos_reativados;
	}


	public void setPontos_reativados(BigDecimal pontos_reativados) {
		this.pontos_reativados = pontos_reativados;
	}


	public BigInteger getAtivos2() {
		return ativos2;
	}


	public void setAtivos2(BigInteger ativos2) {
		this.ativos2 = ativos2;
	}


	public BigInteger getAtivos_mes2() {
		return ativos_mes2;
	}


	public void setAtivos_mes2(BigInteger ativos_mes2) {
		this.ativos_mes2 = ativos_mes2;
	}

	public BigDecimal getNovos2() {
		return novos2;
	}


	public void setNovos2(BigDecimal novos2) {
		this.novos2 = novos2;
	}


	public BigDecimal getPerc_novos2() {
		return perc_novos2;
	}


	public void setPerc_novos2(BigDecimal perc_novos2) {
		this.perc_novos2 = perc_novos2;
	}


	public BigDecimal getPontos_novos2() {
		return pontos_novos2;
	}


	public void setPontos_novos2(BigDecimal pontos_novos2) {
		this.pontos_novos2 = pontos_novos2;
	}


	public BigDecimal getReativados2() {
		return reativados2;
	}


	public void setReativados2(BigDecimal reativados2) {
		this.reativados2 = reativados2;
	}


	public BigDecimal getPerc_reativado2() {
		return perc_reativado2;
	}


	public void setPerc_reativado2(BigDecimal perc_reativado2) {
		this.perc_reativado2 = perc_reativado2;
	}


	public BigDecimal getPontos_reativados2() {
		return pontos_reativados2;
	}


	public void setPontos_reativados2(BigDecimal pontos_reativados2) {
		this.pontos_reativados2 = pontos_reativados2;
	}


	public BigInteger getAtivos3() {
		return ativos3;
	}


	public void setAtivos3(BigInteger ativos3) {
		this.ativos3 = ativos3;
	}


	public BigInteger getAtivos_mes3() {
		return ativos_mes3;
	}


	public void setAtivos_mes3(BigInteger ativos_mes3) {
		this.ativos_mes3 = ativos_mes3;
	}

	public BigDecimal getNovos3() {
		return novos3;
	}


	public Integer getPontos_evolucao2() {
		return pontos_evolucao2;
	}


	public void setPontos_evolucao2(Integer pontos_evolucao2) {
		this.pontos_evolucao2 = pontos_evolucao2;
	}


	public Integer getPontos_evolucao3() {
		return pontos_evolucao3;
	}


	public void setPontos_evolucao3(Integer pontos_evolucao3) {
		this.pontos_evolucao3 = pontos_evolucao3;
	}


	public void setNovos3(BigDecimal novos3) {
		this.novos3 = novos3;
	}


	public BigDecimal getPerc_novos3() {
		return perc_novos3;
	}


	public void setPerc_novos3(BigDecimal perc_novos3) {
		this.perc_novos3 = perc_novos3;
	}


	public BigDecimal getPontos_novos3() {
		return pontos_novos3;
	}


	public void setPontos_novos3(BigDecimal pontos_novos3) {
		this.pontos_novos3 = pontos_novos3;
	}


	public BigDecimal getReativados3() {
		return reativados3;
	}


	public void setReativados3(BigDecimal reativados3) {
		this.reativados3 = reativados3;
	}


	public BigDecimal getPerc_reativado3() {
		return perc_reativado3;
	}


	public void setPerc_reativado3(BigDecimal perc_reativado3) {
		this.perc_reativado3 = perc_reativado3;
	}


	public BigDecimal getPontos_reativados3() {
		return pontos_reativados3;
	}


	public void setPontos_reativados3(BigDecimal pontos_reativados3) {
		this.pontos_reativados3 = pontos_reativados3;
	}


	public BigDecimal getTotaltri1() {
		return totaltri1;
	}


	public void setTotaltri1(BigDecimal totaltri1) {
		this.totaltri1 = totaltri1;
	}


	public Integer getPontos_evolucao() {
		return pontos_evolucao;
	}


	public void setPontos_evolucao(Integer pontos_evolucao) {
		this.pontos_evolucao = pontos_evolucao;
	}


	public Integer getPerc_evolucao2() {
		return perc_evolucao2;
	}


	public void setPerc_evolucao2(Integer perc_evolucao2) {
		this.perc_evolucao2 = perc_evolucao2;
	}


	public Integer getPerc_evolucao3() {
		return perc_evolucao3;
	}


	public void setPerc_evolucao3(Integer perc_evolucao3) {
		this.perc_evolucao3 = perc_evolucao3;
	}

}
