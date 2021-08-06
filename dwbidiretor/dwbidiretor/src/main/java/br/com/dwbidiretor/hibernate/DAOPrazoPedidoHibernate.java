package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.dao.DAOPrazoPedido;

@Dependent
public class DAOPrazoPedidoHibernate extends DAOGenericoHibernate<PrazoPedido> implements DAOPrazoPedido,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPrazoPedidoHibernate(){
		super(PrazoPedido.class);
	}


}
