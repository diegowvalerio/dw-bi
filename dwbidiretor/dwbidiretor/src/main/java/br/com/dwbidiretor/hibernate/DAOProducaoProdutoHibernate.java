package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.ProducaoProduto;
import br.com.dwbidiretor.dao.DAOProducaoProduto;

@Dependent
public class DAOProducaoProdutoHibernate extends DAOGenericoHibernate<ProducaoProduto> implements DAOProducaoProduto,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOProducaoProdutoHibernate(){
		super(ProducaoProduto.class);
	}


}
