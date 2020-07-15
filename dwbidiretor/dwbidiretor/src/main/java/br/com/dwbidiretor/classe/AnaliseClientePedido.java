package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


public class AnaliseClientePedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private BigDecimal codigocliente;
	private String nomecliente;
	
	private BigDecimal pedidoindividual;
	private BigDecimal vlpedido;
	private String tipopedido;
	private String faseatual;
	private String status;
	private String origem;
	
	private BigDecimal pedido;
	private Date datapedido;
	private BigDecimal vlvenda;
	private BigDecimal vlamostra;
	private BigDecimal vlamostrapaga;
	private BigDecimal vlbonificacao;
	private BigDecimal vlexpositor;
	private BigDecimal vlbrinde;
	private BigDecimal vltroca;
	private BigDecimal vlnegociacoescomerciais;
	private String statuspedido;
	
	private Integer sige_vlvenda;
	private Integer sige_vlamostra;
	private Integer sige_vlbonificacao;
	private Integer sige_vlexpositor;
	private Integer sige_vltroca;
	
	private BigDecimal acvlvenda;
	private BigDecimal acvlamostra;
	private BigDecimal acvlamostrapaga;
	private BigDecimal acvlbonificacao;
	private BigDecimal acvlexpositor;
	private BigDecimal acvlbrinde;
	private BigDecimal acvltroca;
	private BigDecimal acvlnegociacoescomerciais;
	
	private BigDecimal pcamostra;
	private BigDecimal pcamostrapaga;
	private BigDecimal pcbonificacao;
	private BigDecimal pcexpositor;
	private BigDecimal pcbrinde;
	private BigDecimal pctroca;
	private BigDecimal pcnegociacoescomerciais;
	
	private BigDecimal vlbonificacaoexpositor;
	private BigDecimal acvlbonificacaoexpositor;
	private BigDecimal pcbonificacaoexpositor;
	
	public AnaliseClientePedido() {
		super();
	}

	public BigDecimal getVlbonificacaoexpositor() {
		return vlbonificacaoexpositor;
	}

	public void setVlbonificacaoexpositor(BigDecimal vlbonificacaoexpositor) {
		this.vlbonificacaoexpositor = vlbonificacaoexpositor;
	}

	public BigDecimal getAcvlbonificacaoexpositor() {
		return acvlbonificacaoexpositor;
	}

	public void setAcvlbonificacaoexpositor(BigDecimal acvlbonificacaoexpositor) {
		this.acvlbonificacaoexpositor = acvlbonificacaoexpositor;
	}

	public BigDecimal getPcbonificacaoexpositor() {
		return pcbonificacaoexpositor;
	}

	public void setPcbonificacaoexpositor(BigDecimal pcbonificacaoexpositor) {
		this.pcbonificacaoexpositor = pcbonificacaoexpositor;
	}

	public Date getDatapedido() {
		return datapedido;
	}

	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
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

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getFaseatual() {
		return faseatual;
	}

	public void setFaseatual(String faseatual) {
		this.faseatual = faseatual;
	}

	public String getStatuspedido() {
		return statuspedido;
	}

	public void setStatuspedido(String statuspedido) {
		this.statuspedido = statuspedido;
	}

	public BigDecimal getPedido() {
		return pedido;
	}

	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
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

	public BigDecimal getPedidoindividual() {
		return pedidoindividual;
	}

	public void setPedidoindividual(BigDecimal pedidoindividual) {
		this.pedidoindividual = pedidoindividual;
	}

	public BigDecimal getVlpedido() {
		return vlpedido;
	}

	public void setVlpedido(BigDecimal vlpedido) {
		this.vlpedido = vlpedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getVlbrinde() {
		return vlbrinde;
	}

	public void setVlbrinde(BigDecimal vlbrinde) {
		this.vlbrinde = vlbrinde;
	}

	public Integer getSige_vlvenda() {
		return sige_vlvenda;
	}

	public void setSige_vlvenda(Integer sige_vlvenda) {
		this.sige_vlvenda = sige_vlvenda;
	}

	public Integer getSige_vlamostra() {
		return sige_vlamostra;
	}

	public void setSige_vlamostra(Integer sige_vlamostra) {
		this.sige_vlamostra = sige_vlamostra;
	}

	public Integer getSige_vlbonificacao() {
		return sige_vlbonificacao;
	}

	public void setSige_vlbonificacao(Integer sige_vlbonificacao) {
		this.sige_vlbonificacao = sige_vlbonificacao;
	}

	public Integer getSige_vlexpositor() {
		return sige_vlexpositor;
	}

	public void setSige_vlexpositor(Integer sige_vlexpositor) {
		this.sige_vlexpositor = sige_vlexpositor;
	}

	public Integer getSige_vltroca() {
		return sige_vltroca;
	}

	public void setSige_vltroca(Integer sige_vltroca) {
		this.sige_vltroca = sige_vltroca;
	}

	public BigDecimal getPcamostra() {
		return pcamostra;
	}

	public void setPcamostra(BigDecimal pcamostra) {
		this.pcamostra = pcamostra;
	}

	public BigDecimal getPcamostrapaga() {
		return pcamostrapaga;
	}

	public void setPcamostrapaga(BigDecimal pcamostrapaga) {
		this.pcamostrapaga = pcamostrapaga;
	}

	public BigDecimal getPcbonificacao() {
		return pcbonificacao;
	}

	public void setPcbonificacao(BigDecimal pcbonificacao) {
		this.pcbonificacao = pcbonificacao;
	}

	public BigDecimal getPcexpositor() {
		return pcexpositor;
	}

	public void setPcexpositor(BigDecimal pcexpositor) {
		this.pcexpositor = pcexpositor;
	}

	public BigDecimal getPcbrinde() {
		return pcbrinde;
	}

	public void setPcbrinde(BigDecimal pcbrinde) {
		this.pcbrinde = pcbrinde;
	}

	public BigDecimal getPctroca() {
		return pctroca;
	}

	public void setPctroca(BigDecimal pctroca) {
		this.pctroca = pctroca;
	}

	public BigDecimal getPcnegociacoescomerciais() {
		return pcnegociacoescomerciais;
	}

	public void setPcnegociacoescomerciais(BigDecimal pcnegociacoescomerciais) {
		this.pcnegociacoescomerciais = pcnegociacoescomerciais;
	}
	
}
