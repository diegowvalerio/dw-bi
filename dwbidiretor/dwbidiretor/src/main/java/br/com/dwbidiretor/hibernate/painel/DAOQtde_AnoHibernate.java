package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Qtde_Ano;
import br.com.dwbidiretor.dao.painel.DAOQtde_Ano;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOQtde_AnoHibernate extends DAOGenericoHibernate<Qtde_Ano> implements DAOQtde_Ano,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOQtde_AnoHibernate(){
		super(Qtde_Ano.class);
	}


}
