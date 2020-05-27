package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SigeUsuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer idlogin;
	private String usuario;
	private String senha;
	private String ativo;
	private String tipo;

	public SigeUsuario() {
		super();
	}

	public Integer getIdlogin() {
		return idlogin;
	}

	public void setIdlogin(Integer idlogin) {
		this.idlogin = idlogin;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idlogin == null) ? 0 : idlogin.hashCode());
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
		SigeUsuario other = (SigeUsuario) obj;
		if (idlogin == null) {
			if (other.idlogin != null)
				return false;
		} else if (!idlogin.equals(other.idlogin))
			return false;
		return true;
	}


}
