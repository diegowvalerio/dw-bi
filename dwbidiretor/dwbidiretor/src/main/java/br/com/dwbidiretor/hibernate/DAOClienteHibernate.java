package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.dao.DAOCliente;

@Dependent
public class DAOClienteHibernate extends DAOGenericoHibernate<Cliente> implements DAOCliente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClienteHibernate(){
		super(Cliente.class);
	}


}
