package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.CPedidoFin;
import br.com.dwbidiretor.dao.DAOCPedidoFin;

@Dependent
public class ServicoCPedidoFin implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOCPedidoFin dao;
	
	
	public List<CPedidoFin> cpedidofin(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String status, int bo_vencido ){
		return dao.cpedidofin(data1, data2, vendedor1, vendedor2, gestor1, gestor2,cliente1, cliente2, status, bo_vencido);
	}
	
}
