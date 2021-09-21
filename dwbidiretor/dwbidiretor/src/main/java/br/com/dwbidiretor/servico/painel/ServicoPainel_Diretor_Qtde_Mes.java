package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Qtde_Mes;
import br.com.dwbidiretor.dao.painel.DAOQtde_Mes;

@Dependent
public class ServicoPainel_Diretor_Qtde_Mes implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOQtde_Mes dao;
	
	
	public List<Qtde_Mes> qtde_Mes(String uf, String ano){
		return dao.qtde_Mes(uf,ano);
	}	
	
}
