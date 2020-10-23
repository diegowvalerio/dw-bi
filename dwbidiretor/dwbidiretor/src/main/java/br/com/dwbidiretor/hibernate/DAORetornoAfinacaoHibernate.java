package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.dao.DAORetornoAfinacao;

@Dependent
public class DAORetornoAfinacaoHibernate extends DAOGenericoHibernate<RetornoAfinacao> implements DAORetornoAfinacao,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAORetornoAfinacaoHibernate(){
		super(RetornoAfinacao.class);
	}


}
