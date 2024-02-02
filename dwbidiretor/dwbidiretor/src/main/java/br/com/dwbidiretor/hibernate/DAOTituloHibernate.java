package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Titulo;
import br.com.dwbidiretor.dao.DAOTitulo;

@Dependent
public class DAOTituloHibernate extends DAOGenericoHibernate<Titulo> implements DAOTitulo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOTituloHibernate(){
		super(Titulo.class);
	}


}
