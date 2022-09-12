package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.dao.DAOClientesAtivosAno;

@Dependent
public class DAOClientesAtivosAnoHibernate extends DAOGenericoHibernate<ClientesAtivosAno> implements DAOClientesAtivosAno,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClientesAtivosAnoHibernate(){
		super(ClientesAtivosAno.class);
	}


}
