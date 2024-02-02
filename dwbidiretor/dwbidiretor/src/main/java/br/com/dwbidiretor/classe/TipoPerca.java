package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class TipoPerca implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BigDecimal id;
	public String tipoperca;
	
	public TipoPerca() {
		super();
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTipoperca() {
		return tipoperca;
	}

	public void setTipoperca(String tipoperca) {
		this.tipoperca = tipoperca;
	}

}
