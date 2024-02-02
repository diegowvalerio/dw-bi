package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.dao.DAOPercaProduto;

@Dependent
public class DAOPercaProdutoHibernate extends DAOGenericoHibernate<PercaProduto> implements DAOPercaProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPercaProdutoHibernate(){
		super(PercaProduto.class);
	}


}
