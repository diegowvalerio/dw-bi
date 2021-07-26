package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.dao.painel.DAOVenda_Grupo;

@Dependent
public class ServicoPainel_Diretor_Vendagrupo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVenda_Grupo dao_vendagrupo;
	
	
	public List<Venda_Grupo> venda_grupo(String ano, String mes){
		return dao_vendagrupo.venda_grupo(ano, mes);
	}	
	
}
