package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.dao.DAOMetaVenda;

@Dependent
public class DAOMetaVendaHibernate extends DAOGenericoHibernate<MetaVenda> implements DAOMetaVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMetaVendaHibernate(){
		super(MetaVenda.class);
	}


}
