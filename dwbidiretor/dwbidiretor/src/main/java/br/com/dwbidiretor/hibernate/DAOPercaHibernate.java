package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.dao.DAOPerca;

@Dependent
public class DAOPercaHibernate extends DAOGenericoHibernate<Perca> implements DAOPerca,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPercaHibernate(){
		super(Perca.class);
	}


}
