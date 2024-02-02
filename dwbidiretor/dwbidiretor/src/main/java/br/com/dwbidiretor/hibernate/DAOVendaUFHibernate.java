package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.dao.DAOVendaUF;

@Dependent
public class DAOVendaUFHibernate extends DAOGenericoHibernate<VendaUF> implements DAOVendaUF,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaUFHibernate(){
		super(VendaUF.class);
	}


}
