package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.dwbi.dao.DAOVendaAnoMes;

@Dependent
public class ServicoVendaAnoMes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaAnoMes dao;
	
	
	public List<VendaAnoMes> vendaanomes(){
		return dao.vendaanomes();
	}
	
}
