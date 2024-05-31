package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.ProdutoVenda;
import br.com.dwbi.dwbi.dao.DAOProdutoVenda;

@Dependent
public class DAOProdutoVendaHibernate extends DAOGenericoHibernate<ProdutoVenda> implements DAOProdutoVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProdutoVendaHibernate(){
		super(ProdutoVenda.class);
	}


}
