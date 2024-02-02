package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PT_Mix;
import br.com.dwbidiretor.dao.DAOPT_Mix;

@Dependent
public class DAOPT_MixHibernate extends DAOGenericoHibernate<PT_Mix> implements DAOPT_Mix,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPT_MixHibernate(){
		super(PT_Mix.class);
	}


}
