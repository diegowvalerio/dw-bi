package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Orcamentos;
import br.com.dwbidiretor.dao.DAOOrcamentos;

@Dependent
public class DAOOrcamentosHibernate extends DAOGenericoHibernate<Orcamentos> implements DAOOrcamentos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOOrcamentosHibernate(){
		super(Orcamentos.class);
	}


}
