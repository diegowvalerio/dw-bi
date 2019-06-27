package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.ClientesNovos;
import br.com.dwbi.dwbi.dao.DAOClientesNovos;

@Dependent
public class DAOClientesNovosHibernate extends DAOGenericoHibernate<ClientesNovos> implements DAOClientesNovos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClientesNovosHibernate(){
		super(ClientesNovos.class);
	}


}
