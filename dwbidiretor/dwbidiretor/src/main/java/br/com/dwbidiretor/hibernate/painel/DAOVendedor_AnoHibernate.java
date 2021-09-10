package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Vendedor_Ano;
import br.com.dwbidiretor.dao.painel.DAOVendedor_Ano;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOVendedor_AnoHibernate extends DAOGenericoHibernate<Vendedor_Ano> implements DAOVendedor_Ano,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendedor_AnoHibernate(){
		super(Vendedor_Ano.class);
	}


}
