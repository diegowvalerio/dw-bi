package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendasFrete;
import br.com.dwbidiretor.dao.DAOVendasFrete;

@Dependent
public class DAOVendasFreteHibernate extends DAOGenericoHibernate<VendasFrete> implements DAOVendasFrete,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasFreteHibernate(){
		super(VendasFrete.class);
	}


}
