package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.dao.DAOVendaGrupoSubGrupoProdutoQuantidadeValor;

@Dependent
public class ServicoVendaGrupoSubGrupoProdutoQuantidadeValor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaGrupoSubGrupoProdutoQuantidadeValor dao;
	
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.vendaGrupoSubGrupoProdutoQuantidadeValor(data1, data2, vendedor1,  vendedor2,  gestor1,  gestor2);
	}
	
}
