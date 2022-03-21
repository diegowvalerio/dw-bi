package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class CidadeVenda implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigocidade;
	private String ufcidade;
	private String nomecidade;
	private Date dataultimacompra;
	private Date dataprimeiracompra;

	public CidadeVenda() {
		super();
	}

	public BigDecimal getCodigocidade() {
		return codigocidade;
	}

	public void setCodigocidade(BigDecimal codigocidade) {
		this.codigocidade = codigocidade;
	}

	public String getUfcidade() {
		return ufcidade;
	}

	public void setUfcidade(String ufcidade) {
		this.ufcidade = ufcidade;
	}

	public String getNomecidade() {
		return nomecidade;
	}

	public void setNomecidade(String nomecidade) {
		this.nomecidade = nomecidade;
	}

	public Date getDataultimacompra() {
		return dataultimacompra;
	}

	public void setDataultimacompra(Date dataultimacompra) {
		this.dataultimacompra = dataultimacompra;
	}

	public Date getDataprimeiracompra() {
		return dataprimeiracompra;
	}

	public void setDataprimeiracompra(Date dataprimeiracompra) {
		this.dataprimeiracompra = dataprimeiracompra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigocidade == null) ? 0 : codigocidade.hashCode());
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
		CidadeVenda other = (CidadeVenda) obj;
		if (codigocidade == null) {
			if (other.codigocidade != null)
				return false;
		} else if (!codigocidade.equals(other.codigocidade))
			return false;
		return true;
	}

	
}
