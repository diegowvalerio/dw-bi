package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.dao.DAOClientesNovos;

@Dependent
public class DAOClientesNovosHibernate extends DAOGenericoHibernate<ClientesNovos> implements DAOClientesNovos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClientesNovosHibernate(){
		super(ClientesNovos.class);
	}


}
