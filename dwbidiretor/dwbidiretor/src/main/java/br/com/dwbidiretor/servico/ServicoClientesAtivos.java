package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ClientesAtivos;
import br.com.dwbidiretor.dao.DAOClienteAtivos;

@Dependent
public class ServicoClientesAtivos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOClienteAtivos dao;

	
	public List<ClientesAtivos> clientesativos(String ano){
		return dao.clientesativos(ano);
	}
	
	public List<ClientesAtivos> clientesativos2(String ano){
		return dao.clientesativos2(ano);
	}
	
}
