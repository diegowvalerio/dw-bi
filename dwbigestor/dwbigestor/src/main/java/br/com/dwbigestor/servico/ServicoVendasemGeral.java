package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.VendasEmGeral;
import br.com.dwbigestor.dao.DAOVendasemGeral;

@Dependent
public class ServicoVendasemGeral implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeral dao;
	
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2){
		return dao.vendasemgeral(data1, data2, vendedor1,vendedor2);
	}
	
}