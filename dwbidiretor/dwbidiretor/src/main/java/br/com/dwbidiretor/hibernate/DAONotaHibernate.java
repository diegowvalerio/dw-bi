package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Nota;
import br.com.dwbidiretor.dao.DAONota;

@Dependent
public class DAONotaHibernate extends DAOGenericoHibernate<Nota> implements DAONota,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAONotaHibernate(){
		super(Nota.class);
	}


}
