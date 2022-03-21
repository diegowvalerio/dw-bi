package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.dao.DAOCidadeVenda;

@Dependent
public class DAOCidadeVendaHibernate extends DAOGenericoHibernate<CidadeVenda> implements DAOCidadeVenda,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOCidadeVendaHibernate(){
		super(CidadeVenda.class);
	}


}
