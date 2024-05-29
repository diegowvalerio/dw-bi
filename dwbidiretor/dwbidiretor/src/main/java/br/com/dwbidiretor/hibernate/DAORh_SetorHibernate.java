package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Rh_Setor;
import br.com.dwbidiretor.dao.DAORh_Setor;

@Dependent
public class DAORh_SetorHibernate extends DAOGenericoHibernate<Rh_Setor> implements DAORh_Setor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAORh_SetorHibernate(){
		super(Rh_Setor.class);
	}


}
