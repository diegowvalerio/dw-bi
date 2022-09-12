package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.dao.DAOCPedido;

@Dependent
public class DAOCPedidoHibernate extends DAOGenericoHibernate<CPedido> implements DAOCPedido,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCPedidoHibernate(){
		super(CPedido.class);
	}


}
