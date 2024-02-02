package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.P1_FaturadoDia;
import br.com.dwbidiretor.dao.DAOP1_FaturadoDia;

@Dependent
public class ServicoP1_FaturadoDia implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOP1_FaturadoDia dao;
	
	public List<P1_FaturadoDia> p1faturadodia(Date data1, Date data2){
		return dao.p1faturadodia(data1, data2);
	}
	
	public List<P1_FaturadoDia> p1pedidodia(Date data1, Date data2){
		return dao.p1pedidodia(data1, data2);
	}
	
}
