package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.CtaResumo;
import br.com.dwbidiretor.dao.DAOCtaResumo;

@Dependent
public class ServicoCtaResumo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCtaResumo dao;
	
	public List<CtaResumo> ctaresumo(String ano){
		return dao.ctaresumo(ano);
	}
	
}
