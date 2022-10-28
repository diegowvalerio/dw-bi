package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.HCliente;
import br.com.dwbi.dwbi.dao.DAOHCliente;

@Dependent
public class DAOHClienteHibernate extends DAOGenericoHibernate<HCliente> implements DAOHCliente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOHClienteHibernate(){
		super(HCliente.class);
	}


}
