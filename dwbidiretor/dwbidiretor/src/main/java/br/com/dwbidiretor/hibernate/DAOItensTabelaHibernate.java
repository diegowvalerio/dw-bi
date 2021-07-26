package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.dao.DAOItensTabela;

@Dependent
public class DAOItensTabelaHibernate extends DAOGenericoHibernate<ItensTabela> implements DAOItensTabela,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOItensTabelaHibernate(){
		super(ItensTabela.class);
	}


}
