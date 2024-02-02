package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.P1_MetaFaturado;
import br.com.dwbidiretor.dao.DAOP1_MetaFaturado;

@Dependent
public class DAOP1_MetaFaturadoHibernate extends DAOGenericoHibernate<P1_MetaFaturado> implements DAOP1_MetaFaturado,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOP1_MetaFaturadoHibernate(){
		super(P1_MetaFaturado.class);
	}


}
