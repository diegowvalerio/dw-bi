package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Vendedor_Ano;
import br.com.dwbidiretor.dao.painel.DAOVendedor_Ano;

@Dependent
public class ServicoPainel_Diretor_Vendedor_Ano implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendedor_Ano dao_vendedor_Ano;
	
	
	public List<Vendedor_Ano> vendedor_Ano(String ano){
		return dao_vendedor_Ano.vendedor_Ano(ano);
	}	
	
}
