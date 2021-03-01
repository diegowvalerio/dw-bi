package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.dao.DAOVendasEndereco;

@Dependent
public class ServicoVendasEndereco implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasEndereco dao;
	
	
	public List<VendasEndereco> vendasendereco(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		return dao.vendasendereco(data1, data2, vendedor1,vendedor2, gestor1, gestor2, cliente1,  cliente2);
	}
		
	
}
