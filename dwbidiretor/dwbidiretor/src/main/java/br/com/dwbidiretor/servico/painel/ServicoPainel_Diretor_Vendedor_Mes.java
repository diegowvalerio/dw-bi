package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Vendedor_Mes;
import br.com.dwbidiretor.dao.painel.DAOVendedor_Mes;

@Dependent
public class ServicoPainel_Diretor_Vendedor_Mes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendedor_Mes dao;
	
	
	public List<Vendedor_Mes> vendedor_Mes(String uf,String ano){
		return dao.vendedor_Mes(uf,ano);
	}	
	
}
