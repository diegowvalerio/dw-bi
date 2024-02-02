package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.FasePedidoItem;
import br.com.dwbidiretor.dao.DAOFasePedidoItem;

@Dependent
public class DAOFasePedidoItemHibernate extends DAOGenericoHibernate<FasePedidoItem> implements DAOFasePedidoItem,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOFasePedidoItemHibernate(){
		super(FasePedidoItem.class);
	}


}
