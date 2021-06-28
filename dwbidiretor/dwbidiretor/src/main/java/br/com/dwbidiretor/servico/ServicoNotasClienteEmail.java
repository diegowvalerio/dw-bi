package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.dao.DAONotasClienteEmail;

@Dependent
public class ServicoNotasClienteEmail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAONotasClienteEmail dao;
	
	
	public List<NotasClienteEmail> notasclienteemails(String ano,String mes, String dia){
		return dao.notasclienteemails(ano, mes, dia);
	}
	
}
