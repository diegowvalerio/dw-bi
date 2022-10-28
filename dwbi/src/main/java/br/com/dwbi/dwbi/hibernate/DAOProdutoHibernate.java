package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.Produto;
import br.com.dwbi.dwbi.dao.DAOProduto;

@Dependent
public class DAOProdutoHibernate extends DAOGenericoHibernate<Produto> implements DAOProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProdutoHibernate(){
		super(Produto.class);
	}


}
