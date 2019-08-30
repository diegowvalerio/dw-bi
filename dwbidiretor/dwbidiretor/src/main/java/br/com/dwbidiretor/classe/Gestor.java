package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Gestor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigogestor;
	private String nomegestor;
	private BigDecimal gestorid;

	public Gestor() {
		super();
	}

	public BigDecimal getCodigogestor() {
		return codigogestor;
	}

	public void setCodigogestor(BigDecimal codigogestor) {
		this.codigogestor = codigogestor;
	}

	public String getNomegestor() {
		return nomegestor;
	}

	public void setNomegestor(String nomegestor) {
		this.nomegestor = nomegestor;
	}

	public BigDecimal getGestorid() {
		return gestorid;
	}

	public void setGestorid(BigDecimal gestorid) {
		this.gestorid = gestorid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gestorid == null) ? 0 : gestorid.hashCode());
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
		Gestor other = (Gestor) obj;
		if (gestorid == null) {
			if (other.gestorid != null)
				return false;
		} else if (!gestorid.equals(other.gestorid))
			return false;
		return true;
	}


}
