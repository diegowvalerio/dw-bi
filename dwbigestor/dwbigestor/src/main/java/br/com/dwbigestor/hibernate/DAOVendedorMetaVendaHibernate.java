package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.VendedorMetaVenda;
import br.com.dwbigestor.dao.DAOVendedorMetaVenda;

@Dependent
public class DAOVendedorMetaVendaHibernate extends DAOGenericoHibernate<VendedorMetaVenda> implements DAOVendedorMetaVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedorMetaVendaHibernate(){
		super(VendedorMetaVenda.class);
	}


}
