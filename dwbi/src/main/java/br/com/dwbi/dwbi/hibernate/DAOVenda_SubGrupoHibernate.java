package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.Venda_Subgrupo;
import br.com.dwbi.dwbi.dao.DAOVenda_SubGrupo;
import br.com.dwbi.dwbi.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVenda_SubGrupoHibernate extends DAOGenericoHibernate<Venda_Subgrupo> implements DAOVenda_SubGrupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_SubGrupoHibernate(){
		super(Venda_Subgrupo.class);
	}


}
