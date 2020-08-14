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
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido, String origem){
		return dao.vendasemgeralitem(pedido,origem);
	}
	
	public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido, String origem){
		return dao.amostraemgeralitem(pedido,origem);
	}
	
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido, String origem){
		return dao.bonificacaoemgeralitem(pedido,origem);
	}
	
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido){
		return dao.bonificacaoexpositoremgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido, String origem){
		return dao.expositoremgeralitem(pedido,origem);
	}
	
	public VendasEmGeralItem consultaitem(BigDecimal produto){
		return dao.consultaitem(produto);
	}
	
	public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido, String origem){
		return dao.trocadefeitoemgeralitem(pedido,origem);
	}
	public List<VendasEmGeralItem> trocanegocioemgeralitem(BigDecimal pedido){
		return dao.trocanegocioemgeralitem(pedido);
	}
	public List<VendasEmGeralItem> brindeemgeralitem(BigDecimal pedido){
		return dao.brindeemgeralitem(pedido);
	}
	public List<VendasEmGeralItem> amostrapagaemgeralitem(BigDecimal pedido){
		return dao.amostrapagaemgeralitem(pedido);
	}
	
	
}
