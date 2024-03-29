package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.dao.DAOVendasemGeralItem;

@Dependent
public class ServicoVendasemGeralItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeralItem dao;
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido){
		return dao.vendasemgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido){
		return dao.amostraemgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido){
		return dao.bonificacaoemgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido){
		return dao.bonificacaoexpositoremgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido){
		return dao.expositoremgeralitem(pedido);
	}
	
	public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido){
		return dao.trocadefeitoemgeralitem(pedido);
	}
	public List<VendasEmGeralItem> trocadefeitodiferenteitem(BigDecimal pedido){
		return dao.trocadefeitodiferenteitem(pedido);
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
	
	public List<VendasEmGeralItem> investimentoemgeralitem(BigDecimal pedido){
		return dao.investimentoemgeralitem(pedido);
	}
	
	public VendasEmGeralItem consultaitem(BigDecimal produto){
		return dao.consultaitem(produto);
	}
	
	
	
}
