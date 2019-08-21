package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.dao.DAOClientesNovos;

@Dependent
public class DAOClientesNovosHibernate extends DAOGenericoHibernate<ClientesNovos> implements DAOClientesNovos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClientesNovosHibernate(){
		super(ClientesNovos.class);
	}


}
