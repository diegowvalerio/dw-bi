package br.com.dwbigestor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.classe.VendasEmGeral;
import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.classe.Vendedor;

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
