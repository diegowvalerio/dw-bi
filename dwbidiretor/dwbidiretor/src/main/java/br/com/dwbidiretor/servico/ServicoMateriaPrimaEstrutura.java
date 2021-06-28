package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.dao.DAOMateriaPrimaEstrutura;

@Dependent
public class ServicoMateriaPrimaEstrutura implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMateriaPrimaEstrutura dao;
	
	
	public List<MateriaPrimaEstrutura> materiaPrimaEstrutura(String produtoid){
		return dao.materiaPrimaEstrutura(produtoid);
	}
	
}
