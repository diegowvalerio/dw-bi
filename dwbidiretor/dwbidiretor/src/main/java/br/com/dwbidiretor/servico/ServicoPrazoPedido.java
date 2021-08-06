package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.dao.DAOPrazoPedido;

@Dependent
public class ServicoPrazoPedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPrazoPedido dao;
	
	
	public List<PrazoPedido> prazopedido(int venda, int outros, Date data1, Date data2){
		return dao.prazopedido(venda, outros, data1, data2);
	}
	
}
