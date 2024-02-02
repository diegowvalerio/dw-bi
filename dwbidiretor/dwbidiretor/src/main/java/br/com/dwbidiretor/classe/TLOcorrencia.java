package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class TLOcorrencia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal ocorrenciaid;
	private Date dataocorrencia;
	private String contato;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String criador;
	private String status;
	private String tipo;
	private String origem;
	private String resumo;
	private String responsavelatual;
	private String ultimaresposta;
	private String modulo;
	private BigInteger qtdevendas;
	private Date primeiravenda;
	private Date ultimavenda;
	private BigDecimal totalvenda;
	private Date dataultima;

	public BigDecimal getTotalvenda() {
		return totalvenda;
	}

	public Date getDataultima() {
		return dataultima;
	}

	public void setDataultima(Date dataultima) {
		this.dataultima = dataultima;
	}

	public void setTotalvenda(BigDecimal totalvenda) {
		this.totalvenda = totalvenda;
	}

	public BigInteger getQtdevendas() {
		return qtdevendas;
	}

	public void setQtdevendas(BigInteger qtdevendas) {
		this.qtdevendas = qtdevendas;
	}

	public Date getPrimeiravenda() {
		return primeiravenda;
	}

	public void setPrimeiravenda(Date primeiravenda) {
		this.primeiravenda = primeiravenda;
	}

	public Date getUltimavenda() {
		return ultimavenda;
	}

	public void setUltimavenda(Date ultimavenda) {
		this.ultimavenda = ultimavenda;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public TLOcorrencia() {
		super();
	}

	public BigDecimal getOcorrenciaid() {
		return ocorrenciaid;
	}

	public void setOcorrenciaid(BigDecimal ocorrenciaid) {
		this.ocorrenciaid = ocorrenciaid;
	}

	public Date getDataocorrencia() {
		return dataocorrencia;
	}

	public void setDataocorrencia(Date dataocorrencia) {
		this.dataocorrencia = dataocorrencia;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public String getResponsavelatual() {
		return responsavelatual;
	}

	public void setResponsavelatual(String responsavelatual) {
		this.responsavelatual = responsavelatual;
	}

	public String getUltimaresposta() {
		return ultimaresposta;
	}

	public void setUltimaresposta(String ultimaresposta) {
		this.ultimaresposta = ultimaresposta;
	}

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

}