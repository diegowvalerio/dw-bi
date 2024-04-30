package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Categoria;
import br.com.dwbidiretor.dao.DAOCategoria;

@Dependent
public class DAOCategoriaHibernate extends DAOGenericoHibernate<Categoria> implements DAOCategoria,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCategoriaHibernate(){
		super(Categoria.class);
	}


}
