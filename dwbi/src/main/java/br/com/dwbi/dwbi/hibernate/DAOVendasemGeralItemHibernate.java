package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;


import br.com.dwbi.classe.VendasEmGeralItem;

import br.com.dwbi.dwbi.dao.DAOVendasemGeralItem;

@Dependent
public class DAOVendasemGeralItemHibernate extends DAOGenericoHibernate<VendasEmGeralItem> implements DAOVendasemGeralItem,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasemGeralItemHibernate(){
		super(VendasEmGeralItem.class);
	}


}
