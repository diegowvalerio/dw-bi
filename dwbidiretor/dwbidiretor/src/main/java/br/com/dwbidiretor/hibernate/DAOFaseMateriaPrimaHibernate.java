package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.FaseMateriaPrima;
import br.com.dwbidiretor.dao.DAOFaseMateriaPrima;

@Dependent
public class DAOFaseMateriaPrimaHibernate extends DAOGenericoHibernate<FaseMateriaPrima> implements DAOFaseMateriaPrima,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOFaseMateriaPrimaHibernate(){
		super(FaseMateriaPrima.class);
	}


}
