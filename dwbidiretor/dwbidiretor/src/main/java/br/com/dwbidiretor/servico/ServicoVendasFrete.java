package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendasFrete;
import br.com.dwbidiretor.dao.DAOVendasFrete;

@Dependent
public class ServicoVendasFrete implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasFrete dao;
	
	
	public List<VendasFrete> vendasfrete(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2, String uf){
		return dao.vendasfrete(data1, data2, vendedor1,vendedor2, cliente1,  cliente2,uf);
	}
		
	
}
