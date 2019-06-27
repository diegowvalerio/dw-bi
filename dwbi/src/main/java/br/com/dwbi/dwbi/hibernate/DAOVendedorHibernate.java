package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.Vendedor;
import br.com.dwbi.dwbi.dao.DAOVendedor;

@Dependent
public class DAOVendedorHibernate extends DAOGenericoHibernate<Vendedor> implements DAOVendedor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedorHibernate(){
		super(Vendedor.class);
	}


}
