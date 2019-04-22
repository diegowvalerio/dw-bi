package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.dwbi.dao.DAOVendasemGeral;

@Dependent
public class DAOVendasemGeralHibernate extends DAOGenericoHibernate<VendasEmGeral> implements DAOVendasemGeral,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasemGeralHibernate(){
		super(VendasEmGeral.class);
	}


}
