package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Titulo;
import br.com.dwbidiretor.dao.DAOTitulo;


@Dependent
public class ServicoTitulo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOTitulo dao;
	
	
	public List<Titulo> titulos(String cliente, String status, String nota){
		return dao.titulos(cliente, status, nota);
	}
	
}
