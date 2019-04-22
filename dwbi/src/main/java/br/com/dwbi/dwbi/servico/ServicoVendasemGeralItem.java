package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.dwbi.dao.DAOVendasemGeralItem;

@Dependent
public class ServicoVendasemGeralItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeralItem dao;
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido){
		return dao.vendasemgeralitem(pedido);
	}
	
	
}
