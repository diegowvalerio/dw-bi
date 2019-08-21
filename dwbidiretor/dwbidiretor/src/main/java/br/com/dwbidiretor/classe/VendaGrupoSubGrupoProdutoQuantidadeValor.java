package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;

import javax.persistence.*;

public class VendaGrupoSubGrupoProdutoQuantidadeValor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false, columnDefinition="varchar(150)")
	private String subgrupo;
	//@Column(nullable=false, columnDefinition="varchar(2)")
	private BigDecimal quantidade;
	@Column(nullable=false, columnDefinition="numeric(6,2)")
	private BigDecimal valor;
		

	public VendaGrupoSubGrupoProdutoQuantidadeValor() {
		super();
	}

	public String getSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
