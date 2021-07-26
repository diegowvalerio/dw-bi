package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.dao.painel.DAODiretor_01;

@Dependent
public class ServicoPainel_Diretor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAODiretor_01 dao;
	
	public List<Diretor_01> diretor_01(String ano, String mes){
	 	 return dao.diretor_01(ano, mes);		
	}
	
	
	
}
