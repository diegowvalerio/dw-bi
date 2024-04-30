package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class CtaResumo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String ano;
	private String planocontaid;
	private String nomeplanoconta;
	private String status;
	private BigDecimal jan;
	private BigDecimal fev;
	private BigDecimal mar;
	private BigDecimal abr;
	private BigDecimal mai;
	private BigDecimal jun;
	private BigDecimal jul;
	private BigDecimal ago;
	private BigDecimal set;
	private BigDecimal out;
	private BigDecimal nov;
	private BigDecimal dez;
	
	
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getPlanocontaid() {
		return planocontaid;
	}
	public void setPlanocontaid(String planocontaid) {
		this.planocontaid = planocontaid;
	}
	public String getNomeplanoconta() {
		return nomeplanoconta;
	}
	public void setNomeplanoconta(String nomeplanoconta) {
		this.nomeplanoconta = nomeplanoconta;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getJan() {
		return jan;
	}
	public void setJan(BigDecimal jan) {
		this.jan = jan;
	}
	public BigDecimal getFev() {
		return fev;
	}
	public void setFev(BigDecimal fev) {
		this.fev = fev;
	}
	public BigDecimal getMar() {
		return mar;
	}
	public void setMar(BigDecimal mar) {
		this.mar = mar;
	}
	public BigDecimal getAbr() {
		return abr;
	}
	public void setAbr(BigDecimal abr) {
		this.abr = abr;
	}
	public BigDecimal getMai() {
		return mai;
	}
	public void setMai(BigDecimal mai) {
		this.mai = mai;
	}
	public BigDecimal getJun() {
		return jun;
	}
	public void setJun(BigDecimal jun) {
		this.jun = jun;
	}
	public BigDecimal getJul() {
		return jul;
	}
	public void setJul(BigDecimal jul) {
		this.jul = jul;
	}
	public BigDecimal getAgo() {
		return ago;
	}
	public void setAgo(BigDecimal ago) {
		this.ago = ago;
	}
	public BigDecimal getSet() {
		return set;
	}
	public void setSet(BigDecimal set) {
		this.set = set;
	}
	public BigDecimal getOut() {
		return out;
	}
	public void setOut(BigDecimal out) {
		this.out = out;
	}
	public BigDecimal getNov() {
		return nov;
	}
	public void setNov(BigDecimal nov) {
		this.nov = nov;
	}
	public BigDecimal getDez() {
		return dez;
	}
	public void setDez(BigDecimal dez) {
		this.dez = dez;
	}
	
	
	
	
}
