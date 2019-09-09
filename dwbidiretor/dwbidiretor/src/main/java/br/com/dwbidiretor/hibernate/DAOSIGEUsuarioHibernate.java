package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.dao.DAOSIGEUsuario;

@Dependent
public class DAOSIGEUsuarioHibernate extends DAOSIGEGenericoHibernate<SigeUsuario> implements DAOSIGEUsuario,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGEUsuarioHibernate(){
		super(SigeUsuario.class);
	}


}
