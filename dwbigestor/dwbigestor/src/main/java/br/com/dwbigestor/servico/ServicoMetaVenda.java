package br.com.dwbigestor.servico;

import java.io.Serializable;
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
	
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2) {
		return dao.metavenda(vendedor1, vendedor2);
	}
	
}
