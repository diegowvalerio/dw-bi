package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.FaseMateriaPrima;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.dao.DAOFaseMateriaPrima;
import br.com.dwbidiretor.dao.DAOPedidoFase;

@Dependent
public class ServicoPedidoFase implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPedidoFase dao;
	
	@Inject
	private DAOFaseMateriaPrima dao2;
	
	
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro,Date data1, Date data2,String pedido,String lote){
		return dao.pedidofase(venda, outros, roteiro,data1,data2,pedido,lote);
	}
	
	public List<FaseMateriaPrima> fasemateriaprima(String pedido,String produto){
		return dao2.fasemateriaprima(pedido, produto);
	}
}
