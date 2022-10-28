package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.MixProduto;
import br.com.dwbi.dwbi.dao.DAOMixProduto;

@Dependent
public class DAOMixProdutoHibernate extends DAOGenericoHibernate<MixProduto> implements DAOMixProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMixProdutoHibernate(){
		super(MixProduto.class);
	}


}
