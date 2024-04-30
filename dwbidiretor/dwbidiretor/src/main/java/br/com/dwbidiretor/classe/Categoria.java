package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class Categoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal categoriaid;
	private String nomecategoria;

	public Categoria() {
		super();
	}

	public BigDecimal getCategoriaid() {
		return categoriaid;
	}

	public void setCategoriaid(BigDecimal categoriaid) {
		this.categoriaid = categoriaid;
	}

	public String getNomecategoria() {
		return nomecategoria;
	}

	public void setNomecategoria(String nomecategoria) {
		this.nomecategoria = nomecategoria;
	}

	

}
