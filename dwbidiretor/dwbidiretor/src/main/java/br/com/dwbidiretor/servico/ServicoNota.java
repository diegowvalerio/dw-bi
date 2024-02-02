package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Nota;
import br.com.dwbidiretor.dao.DAONota;


@Dependent
public class ServicoNota implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAONota dao;
	
	
	public List<Nota> notas(String cliente, String nota){
		return dao.notas(cliente, nota);
	}
	
}
