package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.SigeUsuario;
import br.com.dwbi.dwbi.dao.DAOSIGEUsuario;

@Dependent
public class DAOSIGEUsuarioHibernate extends DAOSIGEGenericoHibernate<SigeUsuario> implements DAOSIGEUsuario,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGEUsuarioHibernate(){
		super(SigeUsuario.class);
	}


}
