package br.com.dwbidiretor.servico.painel;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.painel.Cliente_Ano;
import br.com.dwbidiretor.dao.painel.DAOCliente_Ano;

@Dependent
public class ServicoPainel_Diretor_Cliente_Ano implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCliente_Ano dao_Cliente_Ano;
	
	
	public List<Cliente_Ano> cliente_Ano(String ano){
		return dao_Cliente_Ano.cliente_Ano(ano);
	}	
	
}
