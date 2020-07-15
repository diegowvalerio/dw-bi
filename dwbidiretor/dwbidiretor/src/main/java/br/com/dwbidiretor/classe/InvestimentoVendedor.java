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

public class InvestimentoVendedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal vendedor;
	private String nomevendedor;
	
	private BigDecimal vlgeralfaturado;
	private BigDecimal vlvendedorfaturado;
	
	private BigDecimal vltotalinvestido;
	
	private BigDecimal pcinvestidovendedor;
	private BigDecimal pcinvestidogeral;
	
	private BigDecimal vlamostra;
	private BigDecimal pcamostra;
	
	private BigDecimal vlbonificacao;
	private BigDecimal pcbonificacao;
	
	private BigDecimal vlexpositor;
	private BigDecimal pcexpositor;
	
	private BigDecimal vlbrinde;
	private BigDecimal pcbrinde;
	
	private BigDecimal vltrocadefeito;
	private BigDecimal pctrocadefeito;
	
	private BigDecimal vltrocanegocio;
	private BigDecimal pctrocanegocio;
	
	private BigDecimal vlbonificacaoexpositor;
	private BigDecimal pcbonificacaoexpositor;

	public InvestimentoVendedor() {
		super();
	}


	public BigDecimal getVlbonificacaoexpositor() {
		return vlbonificacaoexpositor;
	}


	public void setVlbonificacaoexpositor(BigDecimal vlbonificacaoexpositor) {
		this.vlbonificacaoexpositor = vlbonificacaoexpositor;
	}


	public BigDecimal getPcbonificacaoexpositor() {
		return pcbonificacaoexpositor;
	}


	public void setPcbonificacaoexpositor(BigDecimal pcbonificacaoexpositor) {
		this.pcbonificacaoexpositor = pcbonificacaoexpositor;
	}


	public BigDecimal getVendedor() {
		return vendedor;
	}


	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}


	public BigDecimal getVlgeralfaturado() {
		return vlgeralfaturado;
	}


	public void setVlgeralfaturado(BigDecimal vlgeralfaturado) {
		this.vlgeralfaturado = vlgeralfaturado;
	}


	public BigDecimal getVlvendedorfaturado() {
		return vlvendedorfaturado;
	}


	public void setVlvendedorfaturado(BigDecimal vlvendedorfaturado) {
		this.vlvendedorfaturado = vlvendedorfaturado;
	}


	public BigDecimal getVltotalinvestido() {
		return vltotalinvestido;
	}


	public void setVltotalinvestido(BigDecimal vltotalinvestido) {
		this.vltotalinvestido = vltotalinvestido;
	}


	public BigDecimal getPcinvestidovendedor() {
		return pcinvestidovendedor;
	}


	public void setPcinvestidovendedor(BigDecimal pcinvestidovendedor) {
		this.pcinvestidovendedor = pcinvestidovendedor;
	}


	public BigDecimal getPcinvestidogeral() {
		return pcinvestidogeral;
	}


	public void setPcinvestidogeral(BigDecimal pcinvestidogeral) {
		this.pcinvestidogeral = pcinvestidogeral;
	}


	public BigDecimal getVlamostra() {
		return vlamostra;
	}


	public void setVlamostra(BigDecimal vlamostra) {
		this.vlamostra = vlamostra;
	}


	public BigDecimal getPcamostra() {
		return pcamostra;
	}


	public void setPcamostra(BigDecimal pcamostra) {
		this.pcamostra = pcamostra;
	}


	public BigDecimal getVlbonificacao() {
		return vlbonificacao;
	}


	public void setVlbonificacao(BigDecimal vlbonificacao) {
		this.vlbonificacao = vlbonificacao;
	}


	public BigDecimal getPcbonificacao() {
		return pcbonificacao;
	}


	public void setPcbonificacao(BigDecimal pcbonificacao) {
		this.pcbonificacao = pcbonificacao;
	}


	public BigDecimal getVlexpositor() {
		return vlexpositor;
	}


	public void setVlexpositor(BigDecimal vlexpositor) {
		this.vlexpositor = vlexpositor;
	}


	public BigDecimal getPcexpositor() {
		return pcexpositor;
	}


	public void setPcexpositor(BigDecimal pcexpositor) {
		this.pcexpositor = pcexpositor;
	}


	public BigDecimal getVlbrinde() {
		return vlbrinde;
	}


	public void setVlbrinde(BigDecimal vlbrinde) {
		this.vlbrinde = vlbrinde;
	}


	public BigDecimal getPcbrinde() {
		return pcbrinde;
	}


	public void setPcbrinde(BigDecimal pcbrinde) {
		this.pcbrinde = pcbrinde;
	}


	public BigDecimal getVltrocadefeito() {
		return vltrocadefeito;
	}


	public void setVltrocadefeito(BigDecimal vltrocadefeito) {
		this.vltrocadefeito = vltrocadefeito;
	}


	public BigDecimal getPctrocadefeito() {
		return pctrocadefeito;
	}


	public void setPctrocadefeito(BigDecimal pctrocadefeito) {
		this.pctrocadefeito = pctrocadefeito;
	}


	public BigDecimal getVltrocanegocio() {
		return vltrocanegocio;
	}


	public void setVltrocanegocio(BigDecimal vltrocanegocio) {
		this.vltrocanegocio = vltrocanegocio;
	}


	public BigDecimal getPctrocanegocio() {
		return pctrocanegocio;
	}


	public void setPctrocanegocio(BigDecimal pctrocanegocio) {
		this.pctrocanegocio = pctrocanegocio;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((vendedor == null) ? 0 : vendedor.hashCode());
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
		InvestimentoVendedor other = (InvestimentoVendedor) obj;
		if (vendedor == null) {
			if (other.vendedor != null)
				return false;
		} else if (!vendedor.equals(other.vendedor))
			return false;
		return true;
	}
	
	

}
