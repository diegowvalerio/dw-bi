package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.dao.DAODadosCliente;

@Dependent
public class DAODadosClienteHibernate extends DAOGenericoHibernate<DadosCliente> implements DAODadosCliente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAODadosClienteHibernate(){
		super(DadosCliente.class);
	}


}
