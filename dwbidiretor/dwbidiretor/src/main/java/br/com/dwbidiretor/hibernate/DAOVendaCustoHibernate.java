package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendaCusto;
import br.com.dwbidiretor.dao.DAOVendaCusto;

@Dependent
public class DAOVendaCustoHibernate extends DAOGenericoHibernate<VendaCusto> implements DAOVendaCusto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaCustoHibernate(){
		super(VendaCusto.class);
	}


}
