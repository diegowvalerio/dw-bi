package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.dao.DAOImagem;
import br.com.dwbidiretor.dao.DAOProduto;

@Dependent
public class ServicoProduto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOProduto dao;
	@Inject
	private DAOImagem daoimg;
	
	
	public List<Produto> produtos(){
		return dao.produtos();
	}
	
	public List<Produto> produtosgrupo(String grupo){
		return dao.produtosgrupo(grupo);
	}
	
	public Imagem imagem(String produtoid) {
		return daoimg.imagem(produtoid);
	}
	
}
