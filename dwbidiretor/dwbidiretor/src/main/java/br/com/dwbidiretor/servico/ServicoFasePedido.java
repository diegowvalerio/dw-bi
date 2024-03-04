package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.FasePedidoItem;
import br.com.dwbidiretor.dao.DAOFasePedido;
import br.com.dwbidiretor.dao.DAOFasePedidoItem;

@Dependent
public class ServicoFasePedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOFasePedido dao;
	
	@Inject
	private DAOFasePedidoItem dao2;
	
	
	public List<FasePedido> fasepedido(int venda, int outros,Date data1, Date data2,String pedido, String lote){
		return dao.fasepedido(venda, outros,data1,data2,pedido,lote);
	}
	
	public List<FasePedido> fasepedidodatafase(int venda, int outros,Date data1, Date data2,String pedido, String lote,Date fase){
		return dao.fasepedidodatafase(venda, outros,data1,data2,pedido,lote,fase);
	}
	
	
	public List<FasePedidoItem> fasepedidopedido(String pedido){
		return dao2.fasepedidoitem(pedido);
	}
	
}
