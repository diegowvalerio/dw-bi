package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.Venda_Subgrupo;
import br.com.dwbigestor.dao.DAOVenda_SubGrupo;

@Dependent
public class ServicoPainel_Diretor_VendaSubgrupo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVenda_SubGrupo dao_vendagrupo;

	
	public List<Venda_Subgrupo> subgrupos(){
		return dao_vendagrupo.subgrupos();
	}
	
}
