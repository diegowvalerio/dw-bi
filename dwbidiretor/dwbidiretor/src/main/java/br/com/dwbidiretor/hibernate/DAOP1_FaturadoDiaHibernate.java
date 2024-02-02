package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.P1_FaturadoDia;
import br.com.dwbidiretor.dao.DAOP1_FaturadoDia;

@Dependent
public class DAOP1_FaturadoDiaHibernate extends DAOGenericoHibernate<P1_FaturadoDia> implements DAOP1_FaturadoDia,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOP1_FaturadoDiaHibernate(){
		super(P1_FaturadoDia.class);
	}


}
