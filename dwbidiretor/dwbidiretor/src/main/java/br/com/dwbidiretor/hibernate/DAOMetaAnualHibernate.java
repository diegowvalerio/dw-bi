package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.MetaAnual;
import br.com.dwbidiretor.dao.DAOMetaAnual;

@Dependent
public class DAOMetaAnualHibernate extends DAOGenericoHibernate<MetaAnual> implements DAOMetaAnual,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMetaAnualHibernate(){
		super(MetaAnual.class);
	}


}
