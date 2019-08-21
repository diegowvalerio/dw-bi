package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.dao.DAOVendaGrupoSubGrupoProdutoQuantidadeValor;

@Dependent
public class DAOVendaGrupoSubGrupoProdutoQuantidadeValorHibernate extends DAOGenericoHibernate<VendaGrupoSubGrupoProdutoQuantidadeValor> implements DAOVendaGrupoSubGrupoProdutoQuantidadeValor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaGrupoSubGrupoProdutoQuantidadeValorHibernate(){
		super(VendaGrupoSubGrupoProdutoQuantidadeValor.class);
	}


}
