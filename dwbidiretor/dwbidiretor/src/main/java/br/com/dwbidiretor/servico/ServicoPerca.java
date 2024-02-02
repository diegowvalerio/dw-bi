package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.dao.DAOPerca;
import br.com.dwbidiretor.dao.DAOPercaDia;
import br.com.dwbidiretor.dao.DAOPercaProduto;
import br.com.dwbidiretor.dao.DAOTipoPerca;

@Dependent
public class ServicoPerca implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOPerca dao;
	@Inject
	private DAOPercaDia dao2;
	@Inject
	private DAOTipoPerca dao3;
	@Inject
	private DAOPercaProduto dao4;
	
	
	public List<Perca> perca(String ano, String mes,String setor, int i,String tipo, int t) {
		return dao.perca(ano,mes,setor,i, tipo, t);
	}
	
	public List<PercaDia> percadia(String ano, String mes,String setor, int i,String tipo, int t){
		return dao2.percadia(ano, mes, setor, i, tipo, t);
	}
	
	public List<TipoPerca> tipoperca(){
		return dao3.tipoperca();
	}
	
	public List<PercaProduto> percaproduto(String ano, String mes,String setor, int i,String tipo, int t){
		return dao4.percaproduto(ano, mes, setor, i, tipo, t);
	}
	
}
