package br.com.dwbidiretor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.classe.CPedidoFin;
import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ReativacaoCliente;
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.VendedorMetaVenda;
import br.com.dwbidiretor.classe.painel.Cliente_Ano;
import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.classe.painel.Qtde_Ano;
import br.com.dwbidiretor.classe.painel.Qtde_Mes;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.classe.painel.Vendedor_Ano;
import br.com.dwbidiretor.classe.painel.Vendedor_Mes;

public interface DAOGenerico<E> {
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	public E consultar(Integer id);
	public List<E> consultar();
	
	//banco sige
	public E Ssalvar(E e);
	public E Salterar(E e);
	public boolean Sexcluir(Integer id);
	public E Sconsultar(Integer id);
	public List<E> Sconsultar();
	
	
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
	
	public List<VendasEmGeral> investimentoemgeral_3(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> investimentoemgeralitem(BigDecimal pedido);
	
	public List<InvestimentoVendedor> investimentovendedor(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<InvestimentoVendedor> investimentovendedor_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);

	public List<ClientesNovos> clientesnovos(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<ClientesNovos> clientesnovos_efetivado(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<Vendedor> consultavendedor();
	
	public List<Cliente> consultacliente(String palavra);
	
	public List<Cliente> clientes();
	
	public List<Gestor> consultagestor(String vendedor1, String vendedor2);
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano, String mes);
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano, String mes);
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<PedidoItem> pedidoitem(BigDecimal pedido);
	
	public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<AnaliseClientePedido> analiseclientepedido(Date data1, Date data2, BigDecimal cliente,String cnpj, BigDecimal pedido);
	
	public List<DadosCliente> dadoscliente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<PedidosConferidos> pedidosconferidos(Date data1, Date data2);
	
	public List<RetornoAfinacao> retornoafinacao(Date data1, Date data2, String cfop);
	
	public List<VendasEndereco> vendasendereco(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<NotasClienteEmail> notasclienteemails(String ano, String mes, String dia);
	
	public List<MateriaPrimaEstrutura> materiaPrimaEstrutura(String produtoid);
	
	public List<TabelaPreco> tabelapreco();
	
	public List<ItensTabela> itenstabela(String idtabela);
	public ItensTabela itenstabela(String idtabela, String produtoid);
	
	public List<FasePedido> fasepedido(int venda, int outros);
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro);
	public List<PrazoPedido> prazopedido(int venda, int outros,Date data1, Date data2);
	
	//elias
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2, String subgrupo1, String subgrupo2,String cliente1, String cliente2,Date data1, Date data2);
	public List<Produto> produtos();
	public List<Venda_Grupo> grupos();
	public List<Venda_Subgrupo> subgrupos();
	public List<CidadeVenda> cidadevenda(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2, Integer filtra);
	public List<ReativacaoCliente> reativacaocliente(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2);
	public List<ClientesAtivosAno> clientesativosano(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano);
	
	//Jaqueline
	public List<CPedido> cpedido(BigDecimal pedido);
	public List<CPedido> cpedidoLista(Date data1, Date data2, String status);
	public List<CPedidoLog> cpedidolog(String pedido);
	public List<CPedidoFin> cpedidofin(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String status, int bo_vencido );
	
	//painel diretor
	public List<Diretor_01> diretor_01(String ano, String mes);
	public List<Venda_Grupo> venda_grupo(String ano, String mes);
	public List<Venda_Subgrupo> venda_subgrupo(String ano, String mes, String idgrupo);
	public List<Vendedor_Ano> vendedor_Ano(String uf);
	public List<Vendedor_Mes> vendedor_Mes(String uf, String  ano);
	public List<Cliente_Ano> cliente_Ano(String uf);
	public List<Qtde_Ano> qtde_Ano(String uf);
	public List<Qtde_Mes> qtde_Mes(String uf, String ano); 
	
	//tabela de telação de itens
	public List<RetornoAfinacao> consultar_relacao();
	public E salvar_relacao(E e);
	public E alterar_relacao(E e);
	public E excluir_relacao(E e);
	
}
