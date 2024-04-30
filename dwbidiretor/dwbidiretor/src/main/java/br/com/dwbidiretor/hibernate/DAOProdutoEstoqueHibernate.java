package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ProdutoEstoque;
import br.com.dwbidiretor.dao.DAOProdutoEstoque;

@Dependent
public class DAOProdutoEstoqueHibernate extends DAOGenericoHibernate<ProdutoEstoque> implements DAOProdutoEstoque,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProdutoEstoqueHibernate(){
		super(ProdutoEstoque.class);
	}


}
