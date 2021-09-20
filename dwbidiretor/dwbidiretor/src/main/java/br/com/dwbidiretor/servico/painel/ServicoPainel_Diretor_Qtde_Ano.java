package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Qtde_Ano;
import br.com.dwbidiretor.dao.painel.DAOQtde_Ano;

@Dependent
public class ServicoPainel_Diretor_Qtde_Ano implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOQtde_Ano dao;
	
	
	public List<Qtde_Ano> qtde_Ano(String uf){
		return dao.qtde_Ano(uf);
	}	
	
}
