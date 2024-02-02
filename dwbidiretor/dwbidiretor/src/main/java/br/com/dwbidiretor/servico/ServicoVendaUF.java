package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.dao.DAOVendaUF;

@Dependent
public class ServicoVendaUF implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaUF dao;
	
	
	public List<VendaUF> vendauf(String ano, String mes){
		return dao.vendauf(ano, mes);
	}
	
	public List<VendaUF> vendaufpedido(String ano, String mes){
		return dao.vendaufpedido(ano, mes);
	}
	
}
