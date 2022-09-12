package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.dao.DAOCPedidoLog;

@Dependent
public class DAOCPedidoLogHibernate extends DAOGenericoHibernate<CPedidoLog> implements DAOCPedidoLog,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCPedidoLogHibernate(){
		super(CPedidoLog.class);
	}


}
