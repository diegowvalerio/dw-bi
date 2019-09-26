package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.dao.DAOMapa;



@Dependent
public class DAOMapaHibernate extends DAOGenericoHibernate<Mapa> implements DAOMapa,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMapaHibernate(){
		super(Mapa.class);
	}


}
