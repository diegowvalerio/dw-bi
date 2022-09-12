package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.dao.DAOCPedidoLog;
import br.com.dwbidiretor.fabrica.EntityManagerProducerSige.Corporativo;
import br.com.dwbidiretor.hibernate.TransacaoSige;

@Dependent
public class ServicoCPedidoLog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCPedidoLog dao;
	
	@TransacaoSige
	public void Ssalvar(CPedidoLog pedido){
		try {
			if(pedido.getIdlog() == null){
				dao.Ssalvar(pedido);
			}else{
				dao.Salterar(pedido);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@TransacaoSige
	public boolean Sexcluir(Integer id){
		return dao.Sexcluir(id);
	}
	
	public List<CPedidoLog> Sconsultar(){
		return dao.Sconsultar();
	}
	
	public List<CPedidoLog> cpedidolog(String pedido){
		return dao.cpedidolog(pedido);
	}
	
	
	
	
}
