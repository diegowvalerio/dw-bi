package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.dao.painel.DAOVenda_SubGrupo;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVenda_SubGrupoHibernate extends DAOGenericoHibernate<Venda_Subgrupo> implements DAOVenda_SubGrupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_SubGrupoHibernate(){
		super(Venda_Subgrupo.class);
	}


}
