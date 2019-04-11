package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbi.dwbi.dao.DAOVendaGrupoSubGrupoProdutoQuantidadeValor;

@Dependent
public class ServicoVendaGrupoSubGrupoProdutoQuantidadeValor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaGrupoSubGrupoProdutoQuantidadeValor dao;
	
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2){
		return dao.vendaGrupoSubGrupoProdutoQuantidadeValor(data1, data2);
	}
	
}
