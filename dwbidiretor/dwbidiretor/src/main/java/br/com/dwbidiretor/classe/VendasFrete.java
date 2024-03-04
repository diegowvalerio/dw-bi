package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

public class VendasFrete implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private String pedido;
	private BigDecimal codigocliente;
	private String nomecliente;
	private Date data;
	private BigDecimal valortotal;
	private String prazo;
	private String tipopedido;
	private String tipooperacaocfop;
	private String statuspedido;
	private String cidade;
	private String uf;
	private String nomevendedor;
	private String nomegestor;
	private BigDecimal transporte;
	private String nometransporte;
	private BigDecimal vlfrete;
	private String nconhecimentofrete;

	public VendasFrete() {
		super();
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
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

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValortotal() {
		return valortotal;
	}

	public void setValortotal(BigDecimal valortotal) {
		this.valortotal = valortotal;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getTipooperacaocfop() {
		return tipooperacaocfop;
	}

	public void setTipooperacaocfop(String tipooperacaocfop) {
		this.tipooperacaocfop = tipooperacaocfop;
	}

	public String getStatuspedido() {
		return statuspedido;
	}

	public void setStatuspedido(String statuspedido) {
		this.statuspedido = statuspedido;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public String getNomegestor() {
		return nomegestor;
	}

	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}

	public BigDecimal getTransporte() {
		return transporte;
	}

	public void setTransporte(BigDecimal transporte) {
		this.transporte = transporte;
	}

	public String getNometransporte() {
		return nometransporte;
	}

	public void setNometransporte(String nometransporte) {
		this.nometransporte = nometransporte;
	}

	public BigDecimal getVlfrete() {
		return vlfrete;
	}

	public void setVlfrete(BigDecimal vlfrete) {
		this.vlfrete = vlfrete;
	}

	public String getNconhecimentofrete() {
		return nconhecimentofrete;
	}

	public void setNconhecimentofrete(String nconhecimentofrete) {
		this.nconhecimentofrete = nconhecimentofrete;
	}

	
	

}
