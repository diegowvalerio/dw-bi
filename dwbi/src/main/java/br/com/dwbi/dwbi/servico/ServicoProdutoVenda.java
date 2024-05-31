package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.ProdutoVenda;
import br.com.dwbi.dwbi.dao.DAOProdutoVenda;

@Dependent
public class ServicoProdutoVenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOProdutoVenda dao;
		
	public List<ProdutoVenda> produtos_venda(Date data1,Date data2, String produtos, String cliente){
		return dao.produtos_venda(data1, data2, produtos, cliente);
	}
	
	
}
