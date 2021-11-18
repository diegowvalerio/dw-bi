package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.dao.DAOMixProduto;

@Dependent
public class ServicoMixProduto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMixProduto dao;
	
	
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.mixprodutos( vendedor1,vendedor2, gestor1, gestor2);
	}
	
}
