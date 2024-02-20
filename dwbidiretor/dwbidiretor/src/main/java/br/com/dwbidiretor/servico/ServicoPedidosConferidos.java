package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.PedidosConferidosUsuario;
import br.com.dwbidiretor.dao.DAOPedidosConferidos;
import br.com.dwbidiretor.dao.DAOPedidosConferidosUsuario;

@Dependent
public class ServicoPedidosConferidos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPedidosConferidos dao;
	@Inject
	private DAOPedidosConferidosUsuario dao2;
	
	public List<PedidosConferidos> pedidosconferidos(Date data1, Date data2){
		return dao.pedidosconferidos(data1, data2);
	}
	
	public List<PedidosConferidosUsuario> pedidosconferidosusuarios(Date data1, Date data2){
		return dao2.pedidosconferidosusuarios(data1, data2);
	}
	
}
