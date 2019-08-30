package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.VendedorMetaVenda;
import br.com.dwbidiretor.dao.DAOVendedorMetaVenda;

@Dependent
public class ServicoVendedorMetaVenda implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOVendedorMetaVenda dao;
	
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2) {
		return dao.vendedormetavenda(vendedor1, vendedor2, gestor1, gestor2);
	}
	
}
