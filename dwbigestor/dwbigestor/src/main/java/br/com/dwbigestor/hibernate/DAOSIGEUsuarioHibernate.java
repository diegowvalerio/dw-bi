package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.SigeUsuario;
import br.com.dwbigestor.dao.DAOSIGEUsuario;

@Dependent
public class DAOSIGEUsuarioHibernate extends DAOSIGEGenericoHibernate<SigeUsuario> implements DAOSIGEUsuario,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGEUsuarioHibernate(){
		super(SigeUsuario.class);
	}


}
