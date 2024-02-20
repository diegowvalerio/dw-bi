package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CtaCorrente;
import br.com.dwbidiretor.dao.DAOCtaCorrente;

@Dependent
public class DAOCtaCorrenteHibernate extends DAOGenericoHibernate<CtaCorrente> implements DAOCtaCorrente,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCtaCorrenteHibernate(){
		super(CtaCorrente.class);
	}


}
