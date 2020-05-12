package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class SigeModulo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer idmodulo;
	@Column(nullable=false,columnDefinition="varchar(150)")
	private String descricao;
	@Column(nullable=false,columnDefinition="varchar(150)")
	private String identificacao;

	public SigeModulo() {
		super();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idmodulo == null) ? 0 : idmodulo.hashCode());
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
		SigeModulo other = (SigeModulo) obj;
		if (idmodulo == null) {
			if (other.idmodulo != null)
				return false;
		} else if (!idmodulo.equals(other.idmodulo))
			return false;
		return true;
	}


}
