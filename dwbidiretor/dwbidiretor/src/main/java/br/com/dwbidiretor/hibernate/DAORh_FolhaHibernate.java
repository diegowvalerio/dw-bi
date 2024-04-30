package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Rh_Folha;
import br.com.dwbidiretor.dao.DAORh_Folha;

@Dependent
public class DAORh_FolhaHibernate extends DAOGenericoHibernate<Rh_Folha> implements DAORh_Folha,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAORh_FolhaHibernate(){
		super(Rh_Folha.class);
	}


}
