package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ProdutoRanking;
import br.com.dwbidiretor.dao.DAOProdutoRanking;

@Dependent
public class DAOProdutoRankingHibernate extends DAOGenericoHibernate<ProdutoRanking> implements DAOProdutoRanking,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProdutoRankingHibernate(){
		super(ProdutoRanking.class);
	}


}
