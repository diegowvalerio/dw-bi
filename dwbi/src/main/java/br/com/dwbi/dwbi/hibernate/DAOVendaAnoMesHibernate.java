package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.dwbi.dao.DAOVendaAnoMes;

@Dependent
public class DAOVendaAnoMesHibernate extends DAOGenericoHibernate<VendaAnoMes> implements DAOVendaAnoMes,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaAnoMesHibernate(){
		super(VendaAnoMes.class);
	}


}
