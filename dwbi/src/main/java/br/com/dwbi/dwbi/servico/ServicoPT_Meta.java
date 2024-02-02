package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.PT_Meta;
import br.com.dwbi.dwbi.dao.DAOPT_Meta;

@Dependent
public class ServicoPT_Meta implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPT_Meta dao;

	public List<PT_Meta> pt_meta(String regiao,String vendedor1, String vendedor2){
		return dao.pt_meta(regiao, vendedor1, vendedor2);
	}

}
