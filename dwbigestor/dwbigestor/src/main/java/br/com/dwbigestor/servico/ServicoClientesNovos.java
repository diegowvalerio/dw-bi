package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.dao.DAOClientesNovos;

@Dependent
public class ServicoClientesNovos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOClientesNovos dao;
	
	
	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2){
		return dao.clientesnovos(data1, data2,vendedor1, vendedor2);
	}
	
}
