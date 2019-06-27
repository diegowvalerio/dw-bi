package br.com.dwbi.dwbi.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbi.classe.ClientesNovos;
import br.com.dwbi.classe.MetaVenda;
import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.classe.Vendedor;

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
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2);
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido);
	
	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2);
	
	public List<Vendedor> consultavendedor();
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2);
	
}
