package br.com.dwbigestor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbigestor.classe.HCliente;
import br.com.dwbigestor.classe.Mapa;
import br.com.dwbigestor.classe.MixProduto;
import br.com.dwbigestor.classe.Produto;
import br.com.dwbigestor.classe.Venda_Grupo;
import br.com.dwbigestor.classe.Venda_Subgrupo;
import br.com.dwbigestor.classe.Cliente;
import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.classe.VendasEmGeral;
import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.classe.VendedorMetaVenda;

public interface DAOGenerico<E> {
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	public E consultar(Integer id);
	public List<E> consultar();
	
	
	/*grafico*/
	public List<E> movimentodia(Date data);
	
	public List<VendaAnoMes> vendaanomes();
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2,String vendedor1, String vendedor2);
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2);
	
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
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2,String ano, String mes, Date data_grafico,Date data_grafico2);
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2);
	
	public List<Cliente> consultacliente(String palavra);
	
	public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	//elias
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2,String status);
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2, String subgrupo1, String subgrupo2,String cliente1, String cliente2);
	public List<Produto> produtos();
	public List<Venda_Grupo> grupos();
	public List<Venda_Subgrupo> subgrupos();
	public List<Cliente> clientes();

}
