package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.dao.DAOClientesAtivosAno;

@Dependent
public class ServicoClientesAtivosAno implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOClientesAtivosAno dao;
	
	
	public List<ClientesAtivosAno> clientesativosano(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano){
		return dao.clientesativosano(vendedor1, vendedor2, gestor1, gestor2, ano);
	}
	
}
