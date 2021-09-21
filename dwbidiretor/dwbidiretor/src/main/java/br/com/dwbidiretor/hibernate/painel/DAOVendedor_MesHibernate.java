package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Vendedor_Mes;
import br.com.dwbidiretor.dao.painel.DAOVendedor_Mes;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVendedor_MesHibernate extends DAOGenericoHibernate<Vendedor_Mes> implements DAOVendedor_Mes,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedor_MesHibernate(){
		super(Vendedor_Mes.class);
	}


}
