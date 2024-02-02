package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PT_Meta;
import br.com.dwbidiretor.dao.DAOPT_Meta;

@Dependent
public class DAOPT_MetaHibernate extends DAOGenericoHibernate<PT_Meta> implements DAOPT_Meta,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPT_MetaHibernate(){
		super(PT_Meta.class);
	}


}
