package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.TabProduto;
import br.com.dwbidiretor.dao.DAOTabProduto;

@Dependent
public class ServicoTabProduto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOTabProduto dao;
	
	public List<TabProduto> tabproduto(String idtabela){
		return dao.tabproduto(idtabela);
	}
}
