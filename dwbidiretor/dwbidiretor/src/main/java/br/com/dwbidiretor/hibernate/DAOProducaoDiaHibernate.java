package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ProducaoDia;
import br.com.dwbidiretor.dao.DAOProducaoDia;

@Dependent
public class DAOProducaoDiaHibernate extends DAOGenericoHibernate<ProducaoDia> implements DAOProducaoDia,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProducaoDiaHibernate(){
		super(ProducaoDia.class);
	}


}
