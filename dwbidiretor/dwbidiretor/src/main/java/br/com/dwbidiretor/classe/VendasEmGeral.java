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

public class VendasEmGeral implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal pedido;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private Date datapedido;
	private BigDecimal valortotalpedido;
	private String prazo;
	private String tipopedido;
	private String tipooperacaocfop;
	private String statuspedido;
	private String nomevendedor;
	
	private Date dataliberadogestor;
	
	private BigDecimal valortotalliquidopedido;
	private BigDecimal perc_lucro;

		

	public VendasEmGeral() {
		super();
	}

	public Date getDataliberadogestor() {
		return dataliberadogestor;
	}

	public void setDataliberadogestor(Date dataliberadogestor) {
		this.dataliberadogestor = dataliberadogestor;
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

	public BigDecimal getPedido() {
		return pedido;
	}

	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
	}

	public Date getDatapedido() {
		return datapedido;
	}

	public void setDatapedido(Date datapedido) {
		this.datapedido = datapedido;
	}

	public BigDecimal getValortotalpedido() {
		return valortotalpedido;
	}

	public void setValortotalpedido(BigDecimal valortotalpedido) {
		this.valortotalpedido = valortotalpedido;
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


	public BigDecimal getValortotalliquidopedido() {
		return valortotalliquidopedido;
	}

	public void setValortotalliquidopedido(BigDecimal valortotalliquidopedido) {
		this.valortotalliquidopedido = valortotalliquidopedido;
	}

	public BigDecimal getPerc_lucro() {
		return perc_lucro;
	}

	public void setPerc_lucro(BigDecimal perc_lucro) {
		this.perc_lucro = perc_lucro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendasEmGeral other = (VendasEmGeral) obj;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		return true;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}
	
	

}
