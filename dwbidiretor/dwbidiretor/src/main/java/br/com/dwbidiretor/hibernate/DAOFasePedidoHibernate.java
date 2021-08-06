package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.dao.DAOFasePedido;

@Dependent
public class DAOFasePedidoHibernate extends DAOGenericoHibernate<FasePedido> implements DAOFasePedido,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOFasePedidoHibernate(){
		super(FasePedido.class);
	}


}
