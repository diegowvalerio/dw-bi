package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ProdutoEstoque;
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
	
	public List<ProdutoEstoque> produtoestoque(String produto,String almoxarifado,String grupo,String subgrupo, String tipo, int tipop){
		return dao.produtoestoque(produto, almoxarifado, grupo, subgrupo, tipo,tipop);
	}
	
	public Imagem imagem(String produtoid) {
		return daoimg.imagem(produtoid);
	}
	
}
