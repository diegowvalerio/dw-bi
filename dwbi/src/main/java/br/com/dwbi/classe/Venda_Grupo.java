package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Venda_Grupo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal idgrupo;
	public String nomegrupo;
		
	public BigDecimal valorgrupo;
	
	public Venda_Grupo() {
		super();
	}

	public BigDecimal getIdgrupo() {
		return idgrupo;
	}

	public void setIdgrupo(BigDecimal idgrupo) {
		this.idgrupo = idgrupo;
	}

	public String getNomegrupo() {
		return nomegrupo;
	}

	public void setNomegrupo(String nomegrupo) {
		this.nomegrupo = nomegrupo;
	}

	public BigDecimal getValorgrupo() {
		return valorgrupo;
	}

	public void setValorgrupo(BigDecimal valorgrupo) {
		this.valorgrupo = valorgrupo;
	}

	
}
