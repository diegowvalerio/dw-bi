package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.dao.DAOSIGEAcesso;

@Dependent
public class DAOSIGEAcessoHibernate extends DAOSIGEGenericoHibernate<SigeAcesso> implements DAOSIGEAcesso,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGEAcessoHibernate(){
		super(SigeAcesso.class);
	}


}
