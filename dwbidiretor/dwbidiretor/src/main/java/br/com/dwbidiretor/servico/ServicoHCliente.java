package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.dao.DAOHCliente;

@Dependent
public class ServicoHCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOHCliente dao;
	
	
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String uf, String regiao,String vendedor3,String status){
		return dao.hclientes( vendedor1,vendedor2, gestor1, gestor2, cliente1,  cliente2,uf, regiao,vendedor3,status);
	}
	
}
