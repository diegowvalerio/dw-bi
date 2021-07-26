package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.dao.painel.DAOVenda_Grupo;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVenda_GrupoHibernate extends DAOGenericoHibernate<Venda_Grupo> implements DAOVenda_Grupo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVenda_GrupoHibernate(){
		super(Venda_Grupo.class);
	}


}
