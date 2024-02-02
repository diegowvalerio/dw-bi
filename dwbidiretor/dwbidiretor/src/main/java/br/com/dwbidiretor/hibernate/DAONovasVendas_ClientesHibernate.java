package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.NovasVendas_Cliente;
import br.com.dwbidiretor.dao.DAONovasVendas_Clientes;

@Dependent
public class DAONovasVendas_ClientesHibernate extends DAOGenericoHibernate<NovasVendas_Cliente> implements DAONovasVendas_Clientes,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAONovasVendas_ClientesHibernate(){
		super(NovasVendas_Cliente.class);
	}


}
