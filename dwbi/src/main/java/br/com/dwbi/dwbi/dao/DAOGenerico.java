package br.com.dwbi.dwbi.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbi.classe.Cliente;
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
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostrapagaemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> brindeemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocanegocioemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido);
	
	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2);
	
	public List<Vendedor> consultavendedor();
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2);
	
	public List<Cliente> consultacliente(String palavra);
	
}
