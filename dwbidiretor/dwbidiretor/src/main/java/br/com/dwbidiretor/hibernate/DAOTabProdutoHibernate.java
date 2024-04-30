package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.TabProduto;
import br.com.dwbidiretor.dao.DAOTabProduto;

@Dependent
public class DAOTabProdutoHibernate extends DAOGenericoHibernate<TabProduto> implements DAOTabProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOTabProdutoHibernate(){
		super(TabProduto.class);
	}


}
