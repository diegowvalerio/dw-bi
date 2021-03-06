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

	private Integer sige_vl_venda;
	private Integer sige_vl_amostra;
	private Integer sige_vl_bonificacao;
	private Integer sige_vl_expositor;
	private Integer sige_vl_troca;
	
	private BigDecimal vlvenda;
	private BigDecimal vlamostra;
	private BigDecimal vlamostrapaga;
	private BigDecimal vlbonificacao;
	private BigDecimal vlexpositor;
	private BigDecimal vltroca;
	private BigDecimal vlnegociacoescomerciais;
	
	private BigDecimal pc_comissao;
	private BigDecimal pc_lucro_visao14;
	
	private BigDecimal qtdebonificacaoexpositor;
	private BigDecimal vlbonificacaoexpositor;
	
	public PedidoItem() {
		super();
	}

	public BigDecimal getQtdebonificacaoexpositor() {
		return qtdebonificacaoexpositor;
	}

	public void setQtdebonificacaoexpositor(BigDecimal qtdebonificacaoexpositor) {
		this.qtdebonificacaoexpositor = qtdebonificacaoexpositor;
	}

	public BigDecimal getVlbonificacaoexpositor() {
		return vlbonificacaoexpositor;
	}

	public void setVlbonificacaoexpositor(BigDecimal vlbonificacaoexpositor) {
		this.vlbonificacaoexpositor = vlbonificacaoexpositor;
	}

	public BigDecimal getPc_comissao() {
		return pc_comissao;
	}

	public void setPc_comissao(BigDecimal pc_comissao) {
		this.pc_comissao = pc_comissao;
	}

	public BigDecimal getPc_lucro_visao14() {
		return pc_lucro_visao14;
	}

	public void setPc_lucro_visao14(BigDecimal pc_lucro_visao14) {
		this.pc_lucro_visao14 = pc_lucro_visao14;
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

	public Integer getSige_vl_venda() {
		return sige_vl_venda;
	}

	public void setSige_vl_venda(Integer sige_vl_venda) {
		this.sige_vl_venda = sige_vl_venda;
	}

	public Integer getSige_vl_amostra() {
		return sige_vl_amostra;
	}

	public void setSige_vl_amostra(Integer sige_vl_amostra) {
		this.sige_vl_amostra = sige_vl_amostra;
	}

	public Integer getSige_vl_bonificacao() {
		return sige_vl_bonificacao;
	}

	public void setSige_vl_bonificacao(Integer sige_vl_bonificacao) {
		this.sige_vl_bonificacao = sige_vl_bonificacao;
	}

	public Integer getSige_vl_expositor() {
		return sige_vl_expositor;
	}

	public void setSige_vl_expositor(Integer sige_vl_expositor) {
		this.sige_vl_expositor = sige_vl_expositor;
	}

	public Integer getSige_vl_troca() {
		return sige_vl_troca;
	}

	public void setSige_vl_troca(Integer sige_vl_troca) {
		this.sige_vl_troca = sige_vl_troca;
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
