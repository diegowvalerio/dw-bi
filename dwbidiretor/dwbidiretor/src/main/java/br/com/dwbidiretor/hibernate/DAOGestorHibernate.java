package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.dao.DAOGestor;

@Dependent
public class DAOGestorHibernate extends DAOGenericoHibernate<Gestor> implements DAOGestor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOGestorHibernate(){
		super(Gestor.class);
	}


}
