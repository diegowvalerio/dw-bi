package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.PT_Meta;
import br.com.dwbi.dwbi.dao.DAOPT_Meta;

@Dependent
public class DAOPT_MetaHibernate extends DAOGenericoHibernate<PT_Meta> implements DAOPT_Meta,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPT_MetaHibernate(){
		super(PT_Meta.class);
	}


}
