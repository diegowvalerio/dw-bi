package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class NotasClienteEmail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigocliente;
	private String nomecliente;
	private String email;

	public NotasClienteEmail() {
		super();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigocliente == null) ? 0 : codigocliente.hashCode());
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
		NotasClienteEmail other = (NotasClienteEmail) obj;
		if (codigocliente == null) {
			if (other.codigocliente != null)
				return false;
		} else if (!codigocliente.equals(other.codigocliente))
			return false;
		return true;
	}

	

}
