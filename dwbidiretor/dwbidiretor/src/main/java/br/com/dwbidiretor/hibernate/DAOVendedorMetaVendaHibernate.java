package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendedorMetaVenda;
import br.com.dwbidiretor.dao.DAOVendedorMetaVenda;

@Dependent
public class DAOVendedorMetaVendaHibernate extends DAOGenericoHibernate<VendedorMetaVenda> implements DAOVendedorMetaVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedorMetaVendaHibernate(){
		super(VendedorMetaVenda.class);
	}


}
