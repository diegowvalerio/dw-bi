package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.dao.DAOInvestimentoVendedor;

@Dependent
public class DAOInvestimentoVendedorHibernate extends DAOGenericoHibernate<InvestimentoVendedor> implements DAOInvestimentoVendedor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOInvestimentoVendedorHibernate(){
		super(InvestimentoVendedor.class);
	}


}
