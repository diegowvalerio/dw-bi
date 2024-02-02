package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.P1_MetaFaturado;
import br.com.dwbidiretor.dao.DAOP1_MetaFaturado;

@Dependent
public class ServicoP1_MetaFaturado implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOP1_MetaFaturado dao;
	
	public List<P1_MetaFaturado> p1metafaturado(Date data1, Date data2){
		return dao.p1metafaturado(data1, data2);
	}
	
	public List<P1_MetaFaturado> p1metapedido(Date data1, Date data2){
		return dao.p1metapedido(data1, data2);
	}
	
}
