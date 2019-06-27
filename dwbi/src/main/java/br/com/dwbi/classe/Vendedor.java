package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Vendedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigovendedor;
	private String nomevendedor;
	private String cpfcnpj;

	public Vendedor() {
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

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigovendedor == null) ? 0 : codigovendedor.hashCode());
		result = prime * result + ((cpfcnpj == null) ? 0 : cpfcnpj.hashCode());
		result = prime * result + ((nomevendedor == null) ? 0 : nomevendedor.hashCode());
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
		Vendedor other = (Vendedor) obj;
		if (codigovendedor == null) {
			if (other.codigovendedor != null)
				return false;
		} else if (!codigovendedor.equals(other.codigovendedor))
			return false;
		if (cpfcnpj == null) {
			if (other.cpfcnpj != null)
				return false;
		} else if (!cpfcnpj.equals(other.cpfcnpj))
			return false;
		if (nomevendedor == null) {
			if (other.nomevendedor != null)
				return false;
		} else if (!nomevendedor.equals(other.nomevendedor))
			return false;
		return true;
	}
	

}
