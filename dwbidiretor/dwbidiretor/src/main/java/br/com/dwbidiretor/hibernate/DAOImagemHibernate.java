package br.com.dwbidiretor.hibernate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;

import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.dao.DAOImagem;

@Dependent
public class DAOImagemHibernate extends DAOGenericoHibernate<Imagem> implements DAOImagem,Serializable {
	private static final long serialVersionUID = 1L;
	
	public DAOImagemHibernate(){
		super(Imagem.class);
	}


}
