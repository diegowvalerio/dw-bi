package br.com.dwbi.dwbi.dao;


import java.util.Date;
import java.util.List;

import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;

public interface DAOGenerico<E> {
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	public E consultar(Integer id);
	public List<E> consultar();
	
	
	/*grafico*/
	public List<E> movimentodia(Date data);
	
	public List<VendaAnoMes> vendaanomes();
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2);
	
	
}
