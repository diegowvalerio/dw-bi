package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.dao.DAOVendaAnoMes;

@Dependent
public class ServicoVendaAnoMes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaAnoMes dao;
	
	
	public List<VendaAnoMes> vendaanomes(){
		return dao.vendaanomes();
	}
	
}
