package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;

import javax.persistence.*;


public class PedidoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tipopedido;
	private String faseatual;
	private String statuspedido;
	@Id
	private BigDecimal pedido;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String cpfcnpj;
	
	private BigDecimal codigoproduto;
	private String nomeproduto;
	private BigDecimal qtdepedido;
	private String ean;
	
	private BigDecimal qtdevenda;
	private BigDecimal qtdeamostra;
	private BigDecimal qtdeamostrapaga;
	private BigDecimal qtdebonificacao;
	private BigDecimal qtdeexpositor;
	private BigDecimal qtdetroca;
	private BigDecimal qtdenegociacoescomerciais;
	
	private Integer sige_qtde_venda;
	private Integer sige_qtde_amostra;
	private Integer sige_qtde_bonificacao;
	private Integer sige_qtde_expositor;
	private Integer sige_qtde_troca;	

	public PedidoItem() {
		super();
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

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public BigDecimal getCodigoproduto() {
		return codigoproduto;
	}

	public void setCodigoproduto(BigDecimal codigoproduto) {
		this.codigoproduto = codigoproduto;
	}

	public String getNomeproduto() {
		return nomeproduto;
	}

	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}

	public BigDecimal getQtdepedido() {
		return qtdepedido;
	}

	public void setQtdepedido(BigDecimal qtdepedido) {
		this.qtdepedido = qtdepedido;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public BigDecimal getQtdevenda() {
		return qtdevenda;
	}

	public void setQtdevenda(BigDecimal qtdevenda) {
		this.qtdevenda = qtdevenda;
	}

	public BigDecimal getQtdeamostra() {
		return qtdeamostra;
	}

	public void setQtdeamostra(BigDecimal qtdeamostra) {
		this.qtdeamostra = qtdeamostra;
	}

	public BigDecimal getQtdeamostrapaga() {
		return qtdeamostrapaga;
	}

	public void setQtdeamostrapaga(BigDecimal qtdeamostrapaga) {
		this.qtdeamostrapaga = qtdeamostrapaga;
	}

	public BigDecimal getQtdebonificacao() {
		return qtdebonificacao;
	}

	public void setQtdebonificacao(BigDecimal qtdebonificacao) {
		this.qtdebonificacao = qtdebonificacao;
	}

	public BigDecimal getQtdeexpositor() {
		return qtdeexpositor;
	}

	public void setQtdeexpositor(BigDecimal qtdeexpositor) {
		this.qtdeexpositor = qtdeexpositor;
	}

	public BigDecimal getQtdetroca() {
		return qtdetroca;
	}

	public void setQtdetroca(BigDecimal qtdetroca) {
		this.qtdetroca = qtdetroca;
	}

	public BigDecimal getQtdenegociacoescomerciais() {
		return qtdenegociacoescomerciais;
	}

	public void setQtdenegociacoescomerciais(BigDecimal qtdenegociacoescomerciais) {
		this.qtdenegociacoescomerciais = qtdenegociacoescomerciais;
	}

	public Integer getSige_qtde_venda() {
		return sige_qtde_venda;
	}

	public void setSige_qtde_venda(Integer sige_qtde_venda) {
		this.sige_qtde_venda = sige_qtde_venda;
	}

	public Integer getSige_qtde_amostra() {
		return sige_qtde_amostra;
	}

	public void setSige_qtde_amostra(Integer sige_qtde_amostra) {
		this.sige_qtde_amostra = sige_qtde_amostra;
	}

	public Integer getSige_qtde_bonificacao() {
		return sige_qtde_bonificacao;
	}

	public void setSige_qtde_bonificacao(Integer sige_qtde_bonificacao) {
		this.sige_qtde_bonificacao = sige_qtde_bonificacao;
	}

	public Integer getSige_qtde_expositor() {
		return sige_qtde_expositor;
	}

	public void setSige_qtde_expositor(Integer sige_qtde_expositor) {
		this.sige_qtde_expositor = sige_qtde_expositor;
	}

	public Integer getSige_qtde_troca() {
		return sige_qtde_troca;
	}

	public void setSige_qtde_troca(Integer sige_qtde_troca) {
		this.sige_qtde_troca = sige_qtde_troca;
	}
	
}
