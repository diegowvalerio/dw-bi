package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Venda_Subgrupo;
import br.com.dwbigestor.dao.DAOVenda_SubGrupo;


@Dependent
public class DAOVenda_SubGrupoHibernate extends DAOGenericoHibernate<Venda_Subgrupo> implements DAOVenda_SubGrupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_SubGrupoHibernate(){
		super(Venda_Subgrupo.class);
	}


}
