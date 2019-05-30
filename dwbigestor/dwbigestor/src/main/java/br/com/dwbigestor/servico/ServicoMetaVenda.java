package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.dao.DAOMetaVenda;

@Dependent
public class ServicoMetaVenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMetaVenda dao;
	
	
	public List<MetaVenda> metavenda(){
		return dao.metavenda();
	}
	
}
