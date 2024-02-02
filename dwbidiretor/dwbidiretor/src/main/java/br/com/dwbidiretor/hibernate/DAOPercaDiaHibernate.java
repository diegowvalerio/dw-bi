package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.dao.DAOPercaDia;

@Dependent
public class DAOPercaDiaHibernate extends DAOGenericoHibernate<PercaDia> implements DAOPercaDia,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPercaDiaHibernate(){
		super(PercaDia.class);
	}


}
