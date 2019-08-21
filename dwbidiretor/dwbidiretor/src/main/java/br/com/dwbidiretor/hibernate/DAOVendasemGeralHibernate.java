package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.dao.DAOVendasemGeral;

@Dependent
public class DAOVendasemGeralHibernate extends DAOGenericoHibernate<VendasEmGeral> implements DAOVendasemGeral,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasemGeralHibernate(){
		super(VendasEmGeral.class);
	}


}
