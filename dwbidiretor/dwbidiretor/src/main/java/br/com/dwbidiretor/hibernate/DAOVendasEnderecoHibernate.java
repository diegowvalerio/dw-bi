package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.dao.DAOVendasEndereco;

@Dependent
public class DAOVendasEnderecoHibernate extends DAOGenericoHibernate<VendasEndereco> implements DAOVendasEndereco,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendasEnderecoHibernate(){
		super(VendasEndereco.class);
	}


}
