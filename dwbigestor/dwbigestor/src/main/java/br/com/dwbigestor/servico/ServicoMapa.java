package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.Mapa;
import br.com.dwbigestor.dao.DAOMapa;

@Dependent
public class ServicoMapa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOMapa dao;
	
	
	public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2){
		return dao.mapa(data1, data2, vendedor1,vendedor2, gestor1, gestor2);
	}
	
}
