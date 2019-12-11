package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.dao.DAOAnaliseClientePedido;

@Dependent
public class DAOAnaliseClientePedidoHibernate extends DAOGenericoHibernate<AnaliseClientePedido> implements DAOAnaliseClientePedido,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOAnaliseClientePedidoHibernate(){
		super(AnaliseClientePedido.class);
	}


}
