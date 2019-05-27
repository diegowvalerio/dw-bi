package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.dao.DAOVendedor;

@Dependent
public class DAOVendedorHibernate extends DAOGenericoHibernate<Vendedor> implements DAOVendedor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedorHibernate(){
		super(Vendedor.class);
	}


}
