package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.dao.DAOInvestimentoVendedor;

@Dependent
public class ServicoInvestimentoVendedor implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOInvestimentoVendedor dao;
	
	
	public List<InvestimentoVendedor> investimentovendedor(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		return dao.investimentovendedor(data1, data2, vendedor1,vendedor2, gestor1, gestor2, cliente1,  cliente2);
	}
	
	public List<InvestimentoVendedor> investimentovendedor_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		return dao.investimentovendedor_2(data1, data2, vendedor1,vendedor2, gestor1, gestor2, cliente1,  cliente2);
	}
	
}
