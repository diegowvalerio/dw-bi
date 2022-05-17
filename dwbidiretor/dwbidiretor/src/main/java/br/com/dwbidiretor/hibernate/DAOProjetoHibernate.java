package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Projeto;
import br.com.dwbidiretor.dao.DAOProjeto;

@Dependent
public class DAOProjetoHibernate extends DAOGenericoHibernate<Projeto> implements DAOProjeto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProjetoHibernate(){
		super(Projeto.class);
	}


}
