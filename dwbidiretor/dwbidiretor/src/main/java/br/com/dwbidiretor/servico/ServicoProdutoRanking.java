package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ProdutoRanking;
import br.com.dwbidiretor.dao.DAOProdutoRanking;


@Dependent
public class ServicoProdutoRanking implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOProdutoRanking dao;

	
	public List<ProdutoRanking> produtoranking(Date data1, Date data2, String vendedor, String produtos ){
		return dao.produtoranking(data1, data2, vendedor, produtos);
	}
	
	
}
