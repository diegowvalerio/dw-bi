package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.ClienteNovo;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.dao.DAOClienteNovo;
import br.com.dwbidiretor.dao.DAOClientesNovos;

@Dependent
public class ServicoClientesNovos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOClientesNovos dao;
	
	@Inject
	private DAOClienteNovo dao2;
	
	
	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		return dao.clientesnovos(data1, data2,vendedor1, vendedor2, gestor1, gestor2, cliente1,  cliente2);
	}
	
	public List<ClientesNovos> clientesnovos_efetivado(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		return dao.clientesnovos_efetivado(data1, data2,vendedor1, vendedor2, gestor1, gestor2, cliente1,  cliente2);
	}
	
	public List<ClienteNovo> clientenovo_venda(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao2.clientenovo_venda(data1, data2, vendedor1, vendedor2, gestor1, gestor2);
	}
}
