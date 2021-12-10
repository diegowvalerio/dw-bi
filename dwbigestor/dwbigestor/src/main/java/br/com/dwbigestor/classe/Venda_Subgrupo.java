package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Venda_Subgrupo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal idgrupo;
	public BigDecimal idsubgrupo;
	public String nomesubgrupo;
		
	public BigDecimal valorsubgrupo;
	
	public Venda_Subgrupo() {
		super();
	}
	public BigDecimal getIdgrupo() {
		return idgrupo;
	}

	public void setIdgrupo(BigDecimal idgrupo) {
		this.idgrupo = idgrupo;
	}

	public BigDecimal getIdsubgrupo() {
		return idsubgrupo;
	}

	public void setIdsubgrupo(BigDecimal idsubgrupo) {
		this.idsubgrupo = idsubgrupo;
	}

	public String getNomesubgrupo() {
		return nomesubgrupo;
	}

	public void setNomesubgrupo(String nomesubgrupo) {
		this.nomesubgrupo = nomesubgrupo;
	}

	public BigDecimal getValorsubgrupo() {
		return valorsubgrupo;
	}

	public void setValorsubgrupo(BigDecimal valorsubgrupo) {
		this.valorsubgrupo = valorsubgrupo;
	}

	
}
