package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Almoxarifado;
import br.com.dwbidiretor.dao.DAOAlmoxarifado;

@Dependent
public class DAOAlmoxarifadoHibernate extends DAOGenericoHibernate<Almoxarifado> implements DAOAlmoxarifado,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOAlmoxarifadoHibernate(){
		super(Almoxarifado.class);
	}


}
