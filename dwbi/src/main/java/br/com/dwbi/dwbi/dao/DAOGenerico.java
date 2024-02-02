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
import br.com.dwbi.classe.PT_Meta;
import br.com.dwbi.classe.PT_Mix;
import br.com.dwbi.classe.HCliente;
import br.com.dwbi.classe.MixProduto;
import br.com.dwbi.classe.PT_Carteira;
import br.com.dwbi.classe.Produto;
import br.com.dwbi.classe.Venda_Grupo;
import br.com.dwbi.classe.Venda_Subgrupo;


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
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido, String origem);
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String cliente1, String cliente2);
	
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido, String origem);
	
	public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostrapagaemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> brindeemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido, String origem);
	
	public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocanegocioemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido, String origem);
	
	public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido, String origem);
	
	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2);
	
	public List<Vendedor> consultavendedor();
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2,String ano, String mes, Date data_grafico, Date data_grafico2);
	
	public List<Cliente> consultacliente(String palavra);
	
	//pontução de campanha de representantes
	public List<PT_Meta> pt_meta(String regiao,String vendedor1, String vendedor2);
	public List<PT_Mix> pt_mix(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira2(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira3(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira4(String regiao,String vendedor1, String vendedor2);
	
	//elias mix
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2, String subgrupo1, String subgrupo2,String cliente1, String cliente2,Date data1, Date data2);
	public List<Produto> produtos();
	public List<Venda_Grupo> grupos();
	public List<Venda_Subgrupo> subgrupos();
	public List<Cliente> clientes();
	
	
}
