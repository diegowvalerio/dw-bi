package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.dao.DAOPedidoItem;


@Dependent
public class DAOPedidoItemHibernate extends DAOGenericoHibernate<PedidoItem> implements DAOPedidoItem,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPedidoItemHibernate(){
		super(PedidoItem.class);
	}


}
