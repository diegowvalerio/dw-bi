package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.SigeLog;
import br.com.dwbidiretor.dao.DAOSIGELog;

@Dependent
public class DAOSIGELogHibernate extends DAOSIGEGenericoHibernate<SigeLog> implements DAOSIGELog,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOSIGELogHibernate(){
		super(SigeLog.class);
	}


}
