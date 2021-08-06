package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.dao.DAOFasePedido;

@Dependent
public class ServicoFasePedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOFasePedido dao;
	
	
	public List<FasePedido> fasepedido(int venda, int outros){
		return dao.fasepedido(venda, outros);
	}
	
}
