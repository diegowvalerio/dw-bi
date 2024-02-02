package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.PT_Carteira;
import br.com.dwbidiretor.dao.DAOPT_Carteira;

@Dependent
public class ServicoPT_Carteira implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPT_Carteira dao;

	public List<PT_Carteira> pt_carteira(String regiao,String vendedor1, String vendedor2){
		return dao.pt_carteira(regiao, vendedor1, vendedor2);
	}
	
	public List<PT_Carteira> pt_carteira2(String regiao,String vendedor1, String vendedor2){
		return dao.pt_carteira2(regiao, vendedor1, vendedor2);
	}
	
	public List<PT_Carteira> pt_carteira3(String regiao,String vendedor1, String vendedor2){
		return dao.pt_carteira3(regiao, vendedor1, vendedor2);
	}
	
	public List<PT_Carteira> pt_carteira4(String regiao,String vendedor1, String vendedor2){
		return dao.pt_carteira4(regiao, vendedor1, vendedor2);
	}

}
