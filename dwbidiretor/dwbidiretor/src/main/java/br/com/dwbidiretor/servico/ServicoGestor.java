package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.dao.DAOGestor;

@Dependent
public class ServicoGestor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOGestor dao;
	
	
	public List<Gestor> consultagestor(String vendedor1, String vendedor2){
		return dao.consultagestor(vendedor1, vendedor2);
	}
	
}
