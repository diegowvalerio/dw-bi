package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.dao.DAOPedidoItem;


@Dependent
public class ServicoPedidoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPedidoItem dao;
	
	
	public List<PedidoItem> pedidoitem(BigDecimal pedido){
		return dao.pedidoitem(pedido);
	}
	
}
