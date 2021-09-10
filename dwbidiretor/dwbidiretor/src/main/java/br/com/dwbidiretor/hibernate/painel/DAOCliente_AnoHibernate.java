package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Cliente_Ano;
import br.com.dwbidiretor.dao.painel.DAOCliente_Ano;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOCliente_AnoHibernate extends DAOGenericoHibernate<Cliente_Ano> implements DAOCliente_Ano,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCliente_AnoHibernate(){
		super(Cliente_Ano.class);
	}


}
