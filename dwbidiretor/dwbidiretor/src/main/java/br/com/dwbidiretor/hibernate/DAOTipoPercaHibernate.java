package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.dao.DAOTipoPerca;

@Dependent
public class DAOTipoPercaHibernate extends DAOGenericoHibernate<TipoPerca> implements DAOTipoPerca,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOTipoPercaHibernate(){
		super(TipoPerca.class);
	}


}
