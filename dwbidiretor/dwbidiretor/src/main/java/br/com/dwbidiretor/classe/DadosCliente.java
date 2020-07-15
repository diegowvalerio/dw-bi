package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


public class DadosCliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigovendedor;
	private String nomevendedor;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String endereco;
	private String numeroendereco;
	private String cidade;
	private String cep;
	private String uf;
	
	private BigDecimal acvlvenda;
	private BigDecimal acvlamostra;
	private BigDecimal acvlamostrapaga;
	private BigDecimal acvlbonificacao;
	private BigDecimal acvlexpositor;
	private BigDecimal acvlbrinde;
	private BigDecimal acvltroca;
	private BigDecimal acvlnegociacoescomerciais;
	
	private BigDecimal vlvenda;
	private BigDecimal vlamostra;
	private BigDecimal vlamostrapaga;
	private BigDecimal vlbonificacao;
	private BigDecimal vlexpositor;
	private BigDecimal vlbrinde;
	private BigDecimal vltroca;
	private BigDecimal vlnegociacoescomerciais;
	
	private BigDecimal acvlbonificacaoexpositor;
	private BigDecimal vlbonificacaoexpositor;
	
	public DadosCliente() {
		super();
	}

	public BigDecimal getAcvlbonificacaoexpositor() {
		return acvlbonificacaoexpositor;
	}

	public void setAcvlbonificacaoexpositor(BigDecimal acvlbonificacaoexpositor) {
		this.acvlbonificacaoexpositor = acvlbonificacaoexpositor;
	}

	public BigDecimal getVlbonificacaoexpositor() {
		return vlbonificacaoexpositor;
	}

	public void setVlbonificacaoexpositor(BigDecimal vlbonificacaoexpositor) {
		this.vlbonificacaoexpositor = vlbonificacaoexpositor;
	}

	public String getNumeroendereco() {
		return numeroendereco;
	}

	public void setNumeroendereco(String numeroendereco) {
		this.numeroendereco = numeroendereco;
	}

	public BigDecimal getAcvlvenda() {
		return acvlvenda;
	}

	public void setAcvlvenda(BigDecimal acvlvenda) {
		this.acvlvenda = acvlvenda;
	}

	public BigDecimal getAcvlamostra() {
		return acvlamostra;
	}

	public void setAcvlamostra(BigDecimal acvlamostra) {
		this.acvlamostra = acvlamostra;
	}

	public BigDecimal getAcvlamostrapaga() {
		return acvlamostrapaga;
	}

	public void setAcvlamostrapaga(BigDecimal acvlamostrapaga) {
		this.acvlamostrapaga = acvlamostrapaga;
	}

	public BigDecimal getAcvlbonificacao() {
		return acvlbonificacao;
	}

	public void setAcvlbonificacao(BigDecimal acvlbonificacao) {
		this.acvlbonificacao = acvlbonificacao;
	}

	public BigDecimal getAcvlexpositor() {
		return acvlexpositor;
	}

	public void setAcvlexpositor(BigDecimal acvlexpositor) {
		this.acvlexpositor = acvlexpositor;
	}

	public BigDecimal getAcvlbrinde() {
		return acvlbrinde;
	}

	public void setAcvlbrinde(BigDecimal acvlbrinde) {
		this.acvlbrinde = acvlbrinde;
	}

	public BigDecimal getAcvltroca() {
		return acvltroca;
	}

	public void setAcvltroca(BigDecimal acvltroca) {
		this.acvltroca = acvltroca;
	}

	public BigDecimal getAcvlnegociacoescomerciais() {
		return acvlnegociacoescomerciais;
	}

	public void setAcvlnegociacoescomerciais(BigDecimal acvlnegociacoescomerciais) {
		this.acvlnegociacoescomerciais = acvlnegociacoescomerciais;
	}

	public BigDecimal getVlvenda() {
		return vlvenda;
	}

	public void setVlvenda(BigDecimal vlvenda) {
		this.vlvenda = vlvenda;
	}

	public BigDecimal getVlamostra() {
		return vlamostra;
	}

	public void setVlamostra(BigDecimal vlamostra) {
		this.vlamostra = vlamostra;
	}

	public BigDecimal getVlamostrapaga() {
		return vlamostrapaga;
	}

	public void setVlamostrapaga(BigDecimal vlamostrapaga) {
		this.vlamostrapaga = vlamostrapaga;
	}

	public BigDecimal getVlbonificacao() {
		return vlbonificacao;
	}

	public void setVlbonificacao(BigDecimal vlbonificacao) {
		this.vlbonificacao = vlbonificacao;
	}

	public BigDecimal getVlexpositor() {
		return vlexpositor;
	}

	public void setVlexpositor(BigDecimal vlexpositor) {
		this.vlexpositor = vlexpositor;
	}

	public BigDecimal getVltroca() {
		return vltroca;
	}

	public void setVltroca(BigDecimal vltroca) {
		this.vltroca = vltroca;
	}

	public BigDecimal getVlnegociacoescomerciais() {
		return vlnegociacoescomerciais;
	}

	public void setVlnegociacoescomerciais(BigDecimal vlnegociacoescomerciais) {
		this.vlnegociacoescomerciais = vlnegociacoescomerciais;
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

	public BigDecimal getVlbrinde() {
		return vlbrinde;
	}

	public void setVlbrinde(BigDecimal vlbrinde) {
		this.vlbrinde = vlbrinde;
	}

	public BigDecimal getCodigovendedor() {
		return codigovendedor;
	}

	public void setCodigovendedor(BigDecimal codigovendedor) {
		this.codigovendedor = codigovendedor;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	
}
