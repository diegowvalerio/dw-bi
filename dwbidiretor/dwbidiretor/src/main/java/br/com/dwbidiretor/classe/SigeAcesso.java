package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class SigeAcesso implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer idacesso;
	private Integer idlogin;
	private Integer idmodulo;
	private String acesso;
	
	private String descricao;
	private String identificacao;

	public SigeAcesso() {
		super();
	}

	public Integer getIdacesso() {
		return idacesso;
	}

	public void setIdacesso(Integer idacesso) {
		this.idacesso = idacesso;
	}

	public Integer getIdlogin() {
		return idlogin;
	}

	public void setIdlogin(Integer idlogin) {
		this.idlogin = idlogin;
	}

	public Integer getIdmodulo() {
		return idmodulo;
	}

	public void setIdmodulo(Integer idmodulo) {
		this.idmodulo = idmodulo;
	}
	

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getAcesso() {
		return acesso;
	}

	public void setAcesso(String acesso) {
		this.acesso = acesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idacesso == null) ? 0 : idacesso.hashCode());
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
		SigeAcesso other = (SigeAcesso) obj;
		if (idacesso == null) {
			if (other.idacesso != null)
				return false;
		} else if (!idacesso.equals(other.idacesso))
			return false;
		return true;
	}

}
