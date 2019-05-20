package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.dao.DAOVendasemGeralItem;

@Dependent
public class ServicoVendasemGeralItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeralItem dao;
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido){
		return dao.vendasemgeralitem(pedido);
	}
	
	
}
