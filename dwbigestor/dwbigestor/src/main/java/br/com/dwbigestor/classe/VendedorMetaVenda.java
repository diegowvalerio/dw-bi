package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;


public class VendedorMetaVenda implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigDecimal codigovendedor;
	private String nomevendedor;
	private BigDecimal valormeta;
	private BigDecimal valorvenda;
	private BigDecimal atingidometa;
	private String cordacoluna;

	public VendedorMetaVenda() {
		super();
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

	public BigDecimal getValormeta() {
		return valormeta;
	}

	public void setValormeta(BigDecimal valormeta) {
		this.valormeta = valormeta;
	}

	public BigDecimal getValorvenda() {
		return valorvenda;
	}

	public void setValorvenda(BigDecimal valorvenda) {
		this.valorvenda = valorvenda;
	}

	public BigDecimal getAtingidometa() {
		return atingidometa;
	}

	public void setAtingidometa(BigDecimal atingidometa) {
		this.atingidometa = atingidometa;
	}

	public String getCordacoluna() {
		return cordacoluna;
	}

	public void setCordacoluna(String cordacoluna) {
		this.cordacoluna = cordacoluna;
	}

}
