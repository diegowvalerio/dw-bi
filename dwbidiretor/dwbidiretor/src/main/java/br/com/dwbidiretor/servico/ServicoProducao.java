package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Producao;
import br.com.dwbidiretor.classe.ProducaoDia;
import br.com.dwbidiretor.classe.ProducaoProduto;
import br.com.dwbidiretor.dao.DAOProducao;
import br.com.dwbidiretor.dao.DAOProducaoDia;
import br.com.dwbidiretor.dao.DAOProducaoProduto;

@Dependent
public class ServicoProducao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOProducao dao;
	@Inject
	private DAOProducaoDia dao2;
	
	@Inject
	private DAOProducaoProduto dao3;
	
	
	public List<Producao> producao(String ano, String mes) {
		return dao.producao(ano,mes);
	}
	
	public List<ProducaoDia> producaodia(String ano, String mes, String setor, int i){
		return dao2.producaodia(ano, mes,setor,i);
	}
	
	public List<ProducaoProduto> producaoproduto(String ano, String mes, String setor, String dia){
		return dao2.producaoproduto(ano, mes,setor,dia);
	}
	
}
