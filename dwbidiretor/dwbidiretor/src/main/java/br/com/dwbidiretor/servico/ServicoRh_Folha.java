package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Rh_Folha;
import br.com.dwbidiretor.dao.DAORh_Folha;

@Dependent
public class ServicoRh_Folha implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAORh_Folha dao;
	
	public List<Rh_Folha> rh_folha(String ano, String mes) {
		return dao.rh_folha(ano, mes);
	}
	
}
