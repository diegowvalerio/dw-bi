package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.dao.painel.DAOVenda_SubGrupo;

@Dependent
public class ServicoPainel_Diretor_VendaSubgrupo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVenda_SubGrupo dao_vendagrupo;
	
	
	public List<Venda_Subgrupo> venda_subgrupo(String ano, String mes, String idgrupo){
		return dao_vendagrupo.venda_subgrupo(ano, mes,idgrupo);
	}	
	
}
