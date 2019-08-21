package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.dao.DAOVendaAnoMes;

@Dependent
public class ServicoVendaAnoMes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaAnoMes dao;
	
	
	public List<VendaAnoMes> vendaanomes(){
		return dao.vendaanomes();
	}
	
}
