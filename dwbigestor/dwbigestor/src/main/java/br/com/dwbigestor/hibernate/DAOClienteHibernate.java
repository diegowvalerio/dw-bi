package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Cliente;
import br.com.dwbigestor.dao.DAOCliente;

@Dependent
public class DAOClienteHibernate extends DAOGenericoHibernate<Cliente> implements DAOCliente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClienteHibernate(){
		super(Cliente.class);
	}


}
