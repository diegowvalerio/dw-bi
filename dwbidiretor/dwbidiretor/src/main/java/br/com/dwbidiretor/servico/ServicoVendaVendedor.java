package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendaVendedor;
import br.com.dwbidiretor.dao.DAOVendaVendedor;

@Dependent
public class ServicoVendaVendedor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaVendedor dao;
	
	
	public List<VendaVendedor> vendavendedor(String ano, String mes){
		return dao.vendavendedor(ano, mes);
	}
	public List<VendaVendedor> vendavendedorfatura(String ano, String mes){
		return dao.vendavendedorfatura(ano, mes);
	}
	
	
	
	
}
