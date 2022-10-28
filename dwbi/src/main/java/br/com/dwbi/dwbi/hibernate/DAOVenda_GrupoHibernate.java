package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.Venda_Grupo;
import br.com.dwbi.dwbi.dao.DAOVenda_Grupo;
import br.com.dwbi.dwbi.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVenda_GrupoHibernate extends DAOGenericoHibernate<Venda_Grupo> implements DAOVenda_Grupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_GrupoHibernate(){
		super(Venda_Grupo.class);
	}


}
