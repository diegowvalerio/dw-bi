package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ReativacaoCliente;
import br.com.dwbidiretor.dao.DAOReativacaoCliente;



@Dependent
public class DAOReativacaoClienteHibernate extends DAOGenericoHibernate<ReativacaoCliente> implements DAOReativacaoCliente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOReativacaoClienteHibernate(){
		super(ReativacaoCliente.class);
	}


}
