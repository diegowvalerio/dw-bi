package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.dao.DAOCliente;

@Dependent
public class ServicoCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCliente dao;
	
	
	public List<Cliente> consultacliente(String palavra){
		List<Cliente> pro = null;
		if(!palavra.equals("")){
			pro = dao.consultacliente(palavra);
		}
		return pro;
	}
	
	public List<Cliente> clientes(){
		return dao.clientes();
	}
	
}
