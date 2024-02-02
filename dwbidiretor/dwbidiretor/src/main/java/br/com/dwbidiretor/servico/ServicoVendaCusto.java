package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendaCusto;
import br.com.dwbidiretor.dao.DAOVendaCusto;

@Dependent
public class ServicoVendaCusto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendaCusto dao;
	
	
	public List<VendaCusto> vendacusto(String ano, String produtos, String vendedor){
		return dao.vendacusto(ano,produtos,vendedor);
	}
	
	public List<VendaCusto> vendacustopedido(String ano, String produtos, String vendedor){
		return dao.vendacustopedido(ano,produtos, vendedor);
	}
	
}
