package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.dao.DAOMateriaPrimaEstrutura;

@Dependent
public class DAOMateriaPrimaEstruturaHibernate extends DAOGenericoHibernate<MateriaPrimaEstrutura> implements DAOMateriaPrimaEstrutura,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMateriaPrimaEstruturaHibernate(){
		super(MateriaPrimaEstrutura.class);
	}


}
