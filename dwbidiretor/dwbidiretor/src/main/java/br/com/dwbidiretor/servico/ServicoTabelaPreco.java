package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.dao.DAOTabelaPreco;

@Dependent
public class ServicoTabelaPreco implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOTabelaPreco dao;
	
	
	public List<TabelaPreco> tabelaprecos(){
		return dao.tabelapreco();
	}
	
}
