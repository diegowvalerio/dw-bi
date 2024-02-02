package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.PT_Mix;
import br.com.dwbi.dwbi.dao.DAOPT_Mix;

@Dependent
public class ServicoPT_Mix implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPT_Mix dao;

	public List<PT_Mix> pt_mix(String regiao,String vendedor1, String vendedor2){
		return dao.pt_mix(regiao, vendedor1, vendedor2);
	}

}
