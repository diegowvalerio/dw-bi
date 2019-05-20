package br.com.dwbigestor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbigestor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.dao.DAOVendaGrupoSubGrupoProdutoQuantidadeValor;

@Dependent
public class DAOVendaGrupoSubGrupoProdutoQuantidadeValorHibernate extends DAOGenericoHibernate<VendaGrupoSubGrupoProdutoQuantidadeValor> implements DAOVendaGrupoSubGrupoProdutoQuantidadeValor,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOVendaGrupoSubGrupoProdutoQuantidadeValorHibernate(){
		super(VendaGrupoSubGrupoProdutoQuantidadeValor.class);
	}


}
