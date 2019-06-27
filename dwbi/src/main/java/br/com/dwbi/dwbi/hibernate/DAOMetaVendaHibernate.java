package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.MetaVenda;
import br.com.dwbi.dwbi.dao.DAOMetaVenda;

@Dependent
public class DAOMetaVendaHibernate extends DAOGenericoHibernate<MetaVenda> implements DAOMetaVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMetaVendaHibernate(){
		super(MetaVenda.class);
	}


}
