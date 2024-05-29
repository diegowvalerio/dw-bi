package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Producao;
import br.com.dwbidiretor.dao.DAOProducao;

@Dependent
public class DAOProducaoHibernate extends DAOGenericoHibernate<Producao> implements DAOProducao,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProducaoHibernate(){
		super(Producao.class);
	}


}
 