package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.Vendedor;
import br.com.dwbi.dwbi.dao.DAOVendedor;

@Dependent
public class ServicoVendedor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendedor dao;
	
	
	public List<Vendedor> consultavendedor(){
		return dao.consultavendedor();
	}
	
}
