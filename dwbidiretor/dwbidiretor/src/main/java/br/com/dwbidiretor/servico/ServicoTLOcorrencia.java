package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.dao.DAOTLOcorrencia;

@Dependent
public class ServicoTLOcorrencia implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOTLOcorrencia dao;
	
	
	public List<TLOcorrencia> tlocorrencias(String criador, Date data1, Date data2, String status, String tipo, Date data3, Date data4){
		return dao.tlocorrencias(criador, data1, data2, status, tipo, data3, data4);
	}
	
}
