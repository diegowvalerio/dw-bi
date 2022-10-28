package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CPedidoFin;
import br.com.dwbidiretor.dao.DAOCPedidoFin;

@Dependent
public class DAOCPedidoFinHibernate extends DAOGenericoHibernate<CPedidoFin> implements DAOCPedidoFin,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCPedidoFinHibernate(){
		super(CPedidoFin.class);
	}


}
