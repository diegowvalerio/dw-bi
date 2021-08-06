package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.dao.DAOPedidoFase;

@Dependent
public class DAOPedidoFaseHibernate extends DAOGenericoHibernate<PedidoFase> implements DAOPedidoFase,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPedidoFaseHibernate(){
		super(PedidoFase.class);
	}


}
