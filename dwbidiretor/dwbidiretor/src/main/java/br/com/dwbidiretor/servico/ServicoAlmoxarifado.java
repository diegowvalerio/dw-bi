package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Almoxarifado;
import br.com.dwbidiretor.dao.DAOAlmoxarifado;

@Dependent
public class ServicoAlmoxarifado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOAlmoxarifado dao;
	
	
	public List<Almoxarifado> almoxarifados(){
		return dao.almoxarifados();
	}
	
}
