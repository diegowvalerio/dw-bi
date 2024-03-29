package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.dao.DAOMetaVenda;

@Dependent
public class ServicoMetaVenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMetaVenda dao;
	
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2,String ano, String mes, Date data_grafico, Date data_grafico2) {
		return dao.metavenda(vendedor1, vendedor2, gestor1, gestor2,ano,mes,data_grafico, data_grafico2);
	}
	
}
