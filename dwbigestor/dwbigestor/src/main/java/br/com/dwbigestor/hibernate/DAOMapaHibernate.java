package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.Mapa;
import br.com.dwbigestor.dao.DAOMapa;

@Dependent
public class DAOMapaHibernate extends DAOGenericoHibernate<Mapa> implements DAOMapa,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOMapaHibernate(){
		super(Mapa.class);
	}


}
