package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.dao.DAOTabelaPreco;

@Dependent
public class DAOTabelaPrecoHibernate extends DAOGenericoHibernate<TabelaPreco> implements DAOTabelaPreco,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOTabelaPrecoHibernate(){
		super(TabelaPreco.class);
	}


}
