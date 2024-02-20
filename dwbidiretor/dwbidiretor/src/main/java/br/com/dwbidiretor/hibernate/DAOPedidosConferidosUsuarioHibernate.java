package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.PedidosConferidosUsuario;
import br.com.dwbidiretor.dao.DAOPedidosConferidosUsuario;

@Dependent
public class DAOPedidosConferidosUsuarioHibernate extends DAOGenericoHibernate<PedidosConferidosUsuario> implements DAOPedidosConferidosUsuario,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOPedidosConferidosUsuarioHibernate(){
		super(PedidosConferidosUsuario.class);
	}


}
