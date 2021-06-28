package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.dao.DAONotasClienteEmail;

@Dependent
public class DAONotasClienteEmailHibernate extends DAOGenericoHibernate<NotasClienteEmail> implements DAONotasClienteEmail,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAONotasClienteEmailHibernate(){
		super(NotasClienteEmail.class);
	}


}
