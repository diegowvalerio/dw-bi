package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.dao.DAOVendedor;

@Dependent
public class DAOVendedorHibernate extends DAOGenericoHibernate<Vendedor> implements DAOVendedor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedorHibernate(){
		super(Vendedor.class);
	}


}
