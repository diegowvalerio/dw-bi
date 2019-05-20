package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.dao.DAOVendaAnoMes;

@Dependent
public class DAOVendaAnoMesHibernate extends DAOGenericoHibernate<VendaAnoMes> implements DAOVendaAnoMes,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaAnoMesHibernate(){
		super(VendaAnoMes.class);
	}


}
