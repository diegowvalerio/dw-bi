package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class TabelaPreco implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal id;
	private String nometabela;
	private BigDecimal perc_desconto;

	public TabelaPreco() {
		super();
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getNometabela() {
		return nometabela;
	}

	public void setNometabela(String nometabela) {
		this.nometabela = nometabela;
	}

	public BigDecimal getPerc_desconto() {
		return perc_desconto;
	}

	public void setPerc_desconto(BigDecimal perc_desconto) {
		this.perc_desconto = perc_desconto;
	}

	

}
