package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.MixProduto;
import br.com.dwbi.dwbi.dao.DAOMixProduto;

@Dependent
public class ServicoMixProduto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMixProduto dao;
	
	
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2,String subgrupo1, String subgrupo2,String cliente1, String cliente2,Date data1, Date data2){
		return dao.mixprodutos( vendedor1,vendedor2, gestor1, gestor2, produto1, produto2, grupo1, grupo2,subgrupo1, subgrupo2, cliente1, cliente2, data1, data2);
	}
	
}
