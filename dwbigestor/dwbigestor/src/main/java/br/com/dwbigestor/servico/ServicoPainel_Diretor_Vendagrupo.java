package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.Venda_Grupo;
import br.com.dwbigestor.dao.DAOVenda_Grupo;

@Dependent
public class ServicoPainel_Diretor_Vendagrupo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVenda_Grupo dao_vendagrupo;	
	
	public List<Venda_Grupo> grupos(){
		return dao_vendagrupo.grupos();
	}
	
}
