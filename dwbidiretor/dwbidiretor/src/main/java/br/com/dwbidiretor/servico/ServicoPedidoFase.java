package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.dao.DAOPedidoFase;

@Dependent
public class ServicoPedidoFase implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPedidoFase dao;
	
	
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro){
		return dao.pedidofase(venda, outros, roteiro);
	}
	
}
