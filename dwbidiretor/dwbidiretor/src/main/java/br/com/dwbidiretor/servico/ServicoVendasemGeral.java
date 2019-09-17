package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.dao.DAOVendasemGeral;

@Dependent
public class ServicoVendasemGeral implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeral dao;
	
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.vendasemgeral(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.amostraemgeral(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.bonificacaoemgeral(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
	public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.expositoremgeral(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.faturamentoemgeral(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
}
