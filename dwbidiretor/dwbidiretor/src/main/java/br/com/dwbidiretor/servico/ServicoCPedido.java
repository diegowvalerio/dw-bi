package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.dao.DAOCPedido;

@Dependent
public class ServicoCPedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCPedido dao;
	
	
	public List<CPedido> cpedido(BigDecimal pedido){
		return dao.cpedido(pedido);
	}
	
	public List<CPedido> cpedidoLista(Date data1, Date data2, String status){
		return dao.cpedidoLista(data1, data2,status);
	}
	
}
