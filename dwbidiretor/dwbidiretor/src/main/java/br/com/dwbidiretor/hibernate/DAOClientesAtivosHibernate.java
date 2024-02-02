package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ClientesAtivos;
import br.com.dwbidiretor.dao.DAOClienteAtivos;

@Dependent
public class DAOClientesAtivosHibernate extends DAOGenericoHibernate<ClientesAtivos> implements DAOClienteAtivos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClientesAtivosHibernate(){
		super(ClientesAtivos.class);
	}


}
