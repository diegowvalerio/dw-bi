package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.dao.painel.DAODiretor_01;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;



@Dependent
public class DAODiretor_01Hibernate extends DAOGenericoHibernate<Diretor_01> implements DAODiretor_01,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAODiretor_01Hibernate(){
		super(Diretor_01.class);
	}


}
