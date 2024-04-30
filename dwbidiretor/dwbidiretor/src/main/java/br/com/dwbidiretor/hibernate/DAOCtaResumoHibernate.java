package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CtaResumo;
import br.com.dwbidiretor.dao.DAOCtaResumo;

@Dependent
public class DAOCtaResumoHibernate extends DAOGenericoHibernate<CtaResumo> implements DAOCtaResumo,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCtaResumoHibernate(){
		super(CtaResumo.class);
	}


}
