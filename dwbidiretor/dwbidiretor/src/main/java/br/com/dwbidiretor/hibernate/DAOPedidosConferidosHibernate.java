package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.dao.DAOPedidosConferidos;

@Dependent
public class DAOPedidosConferidosHibernate extends DAOGenericoHibernate<PedidosConferidos> implements DAOPedidosConferidos,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPedidosConferidosHibernate(){
		super(PedidosConferidos.class);
	}


}
