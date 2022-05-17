package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ReativacaoCliente;
import br.com.dwbidiretor.dao.DAOReativacaoCliente;

@Dependent
public class ServicoReativaoCliente implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOReativacaoCliente dao;
	
	
	public List<ReativacaoCliente> reativacaocliente(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2){
		return dao.reativacaocliente(vendedor1, vendedor2, gestor1, gestor2, data1, data2);
	}
	
}
