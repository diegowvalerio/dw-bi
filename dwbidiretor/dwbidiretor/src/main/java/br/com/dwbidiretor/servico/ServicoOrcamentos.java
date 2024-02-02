package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Orcamentos;
import br.com.dwbidiretor.dao.DAOOrcamentos;

@Dependent
public class ServicoOrcamentos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOOrcamentos dao;
	
	public List<Orcamentos> orcamentos(String ano){
		return dao.orcamentos(ano);
	}
	
}
