package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Produto;
import br.com.dwbigestor.dao.DAOProduto;

@Dependent
public class DAOProdutoHibernate extends DAOGenericoHibernate<Produto> implements DAOProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProdutoHibernate(){
		super(Produto.class);
	}


}
