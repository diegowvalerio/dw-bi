package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Categoria;
import br.com.dwbidiretor.dao.DAOCategoria;

@Dependent
public class ServicoCategoria implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCategoria dao;
	
	
	public List<Categoria> categorias(){
		return dao.categorias();
	}
	
}
