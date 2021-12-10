package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Venda_Grupo;
import br.com.dwbigestor.dao.DAOVenda_Grupo;


@Dependent
public class DAOVenda_GrupoHibernate extends DAOGenericoHibernate<Venda_Grupo> implements DAOVenda_Grupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_GrupoHibernate(){
		super(Venda_Grupo.class);
	}


}
