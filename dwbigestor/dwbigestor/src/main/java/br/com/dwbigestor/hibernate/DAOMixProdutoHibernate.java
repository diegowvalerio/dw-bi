package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.MixProduto;
import br.com.dwbigestor.dao.DAOMixProduto;

@Dependent
public class DAOMixProdutoHibernate extends DAOGenericoHibernate<MixProduto> implements DAOMixProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMixProdutoHibernate(){
		super(MixProduto.class);
	}


}
