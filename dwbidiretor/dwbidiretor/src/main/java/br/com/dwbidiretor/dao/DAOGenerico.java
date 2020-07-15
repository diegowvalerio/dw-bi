package br.com.dwbidiretor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.VendedorMetaVenda;

public interface DAOGenerico<E> {
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	public E consultar(Integer id);
	public List<E> consultar();
	
	
	/*grafico*/
	public List<E> movimentodia(Date data);
	
	public List<VendaAnoMes> vendaanomes();
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido);
	
public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocanegocioemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> brindeemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> amostrapagaemgeralitem(BigDecimal pedido);
	
	public List<VendasEmGeral> investimentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeral> investimentoemgeral_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> investimentoemgeralitem(BigDecimal pedido);
	
	public List<InvestimentoVendedor> investimentovendedor(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<InvestimentoVendedor> investimentovendedor_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);

	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<Vendedor> consultavendedor();
	
	public List<Cliente> consultacliente(String palavra);
	
	public List<Gestor> consultagestor(String vendedor1, String vendedor2);
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<PedidoItem> pedidoitem(BigDecimal pedido);
	
	public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<AnaliseClientePedido> analiseclientepedido(Date data1, Date data2, BigDecimal cliente,String cnpj, BigDecimal pedido);
	
	public List<DadosCliente> dadoscliente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
}
