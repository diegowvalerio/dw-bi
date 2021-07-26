package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.dao.DAOItensTabela;


@Dependent
public class ServicoItensTabela implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOItensTabela dao;
	
	
	public ItensTabela itenstabela(String id, String produtoid){
		return dao.itenstabela(id,produtoid);
	}
	
	public List<ItensTabela> itenstabela(String id){
		return dao.itenstabela(id);
	}
	
}
