package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.PT_Carteira;
import br.com.dwbi.dwbi.dao.DAOPT_Carteira;

@Dependent
public class DAOPT_CarteiraHibernate extends DAOGenericoHibernate<PT_Carteira> implements DAOPT_Carteira,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPT_CarteiraHibernate(){
		super(PT_Carteira.class);
	}


}
