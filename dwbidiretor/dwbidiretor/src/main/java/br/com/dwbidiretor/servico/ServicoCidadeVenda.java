package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.dao.DAOCidadeVenda;

@Dependent
public class ServicoCidadeVenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCidadeVenda dao;
	
	public List<CidadeVenda> cidadevenda(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2,Integer filtra){
		return dao.cidadevenda(vendedor1, vendedor2, gestor1, gestor2, data1, data2, filtra);
	}
	
}
