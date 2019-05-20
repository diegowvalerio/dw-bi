package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.dao.DAOVendasemGeralItem;

@Dependent
public class DAOVendasemGeralItemHibernate extends DAOGenericoHibernate<VendasEmGeralItem> implements DAOVendasemGeralItem,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasemGeralItemHibernate(){
		super(VendasEmGeralItem.class);
	}


}
