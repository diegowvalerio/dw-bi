package br.com.dwbidiretor.hibernate.painel;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.painel.Qtde_Mes;
import br.com.dwbidiretor.dao.painel.DAOQtde_Mes;
import br.com.dwbidiretor.hibernate.DAOGenericoHibernate;


@Dependent
public class DAOQtde_MesHibernate extends DAOGenericoHibernate<Qtde_Mes> implements DAOQtde_Mes,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOQtde_MesHibernate(){
		super(Qtde_Mes.class);
	}


}
