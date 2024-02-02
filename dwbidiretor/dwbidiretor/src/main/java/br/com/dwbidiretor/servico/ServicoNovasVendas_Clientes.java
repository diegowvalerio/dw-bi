package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.NovasVendas_Cliente;
import br.com.dwbidiretor.dao.DAONovasVendas_Clientes;

@Dependent
public class ServicoNovasVendas_Clientes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAONovasVendas_Clientes dao;

	
	public List<NovasVendas_Cliente> novasvendas_clientes(String ano){
		return dao.novasvendas_cliente(ano);
	}
	
}
