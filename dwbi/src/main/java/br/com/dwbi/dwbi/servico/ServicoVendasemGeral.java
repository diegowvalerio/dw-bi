package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.dwbi.dao.DAOVendasemGeral;

@Dependent
public class ServicoVendasemGeral implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendasemGeral dao;
	
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.vendasemgeral(data1, data2, vendedor1,vendedor2, cliente1, cliente2);
	}
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.amostraemgeral(data1, data2, vendedor1,vendedor2, cliente1, cliente2);
	}
	
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.bonificacaoemgeral(data1, data2, vendedor1,vendedor2, cliente1, cliente2);
	}
	
	public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.bonificacaoexpositoremgeral(data1, data2, vendedor1,vendedor2, cliente1, cliente2);
	}
	
	public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.expositoremgeral(data1, data2, vendedor1,vendedor2, cliente1, cliente2);
	}
	
	public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.brindeemgeral(data1, data2, vendedor1,vendedor2, cliente1,  cliente2);
	}
	public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2){
		return dao.trocadefeitoemgeral(data1, data2, vendedor1,vendedor2, cliente1,  cliente2);
	}
	public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2){
		return dao.trocanegocioemgeral(data1, data2, vendedor1,vendedor2,  cliente1,  cliente2);
	}
	
	public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2){
		return dao.amostrapagaemgeral(data1, data2, vendedor1,vendedor2, cliente1,  cliente2);
	}
	
}
