package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.dao.DAOSIGEModulo;

@Dependent
public class DAOSIGEModuloHibernate extends DAOSIGEGenericoHibernate<SigeModulo> implements DAOSIGEModulo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGEModuloHibernate(){
		super(SigeModulo.class);
	}


}
