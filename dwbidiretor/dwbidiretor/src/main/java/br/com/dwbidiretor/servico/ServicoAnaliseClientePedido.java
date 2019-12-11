package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.dao.DAOAnaliseClientePedido;


@Dependent
public class ServicoAnaliseClientePedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOAnaliseClientePedido dao;
	
	
	public List<AnaliseClientePedido> analiseClientePedidos(Date data1, Date data2, BigDecimal cliente ,BigDecimal pedido){
		return dao.analiseclientepedido(data1, data2, cliente, pedido);
	}
	
}
