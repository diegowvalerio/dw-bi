package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ClienteNovo;
import br.com.dwbidiretor.dao.DAOClienteNovo;

@Dependent
public class DAOClienteNovoHibernate extends DAOGenericoHibernate<ClienteNovo> implements DAOClienteNovo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOClienteNovoHibernate(){
		super(ClienteNovo.class);
	}


}
