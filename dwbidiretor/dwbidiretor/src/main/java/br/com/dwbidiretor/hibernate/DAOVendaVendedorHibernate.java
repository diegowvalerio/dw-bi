package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendaVendedor;
import br.com.dwbidiretor.dao.DAOVendaVendedor;

@Dependent
public class DAOVendaVendedorHibernate extends DAOGenericoHibernate<VendaVendedor> implements DAOVendaVendedor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaVendedorHibernate(){
		super(VendaVendedor.class);
	}


}
