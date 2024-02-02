package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.dao.DAOTLOcorrencia;

@Dependent
public class DAOTLOcorrenciaHibernate extends DAOGenericoHibernate<TLOcorrencia> implements DAOTLOcorrencia,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOTLOcorrenciaHibernate(){
		super(TLOcorrencia.class);
	}


}
