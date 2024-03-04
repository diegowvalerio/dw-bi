package br.com.dwbidiretor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbidiretor.classe.PT_Carteira;
import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.classe.CPedidoFin;
import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClienteNovo;
import br.com.dwbidiretor.classe.ClientesAtivos;
import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.CtaCorrente;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.FaseMateriaPrima;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.FasePedidoItem;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.Nota;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.classe.NovasVendas_Cliente;
import br.com.dwbidiretor.classe.Orcamentos;
import br.com.dwbidiretor.classe.P1_FaturadoDia;
import br.com.dwbidiretor.classe.P1_MetaFaturado;
import br.com.dwbidiretor.classe.PT_Meta;
import br.com.dwbidiretor.classe.PT_Mix;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.PedidosConferidosUsuario;
import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.classe.Producao;
import br.com.dwbidiretor.classe.ProducaoDia;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ProdutoRanking;
import br.com.dwbidiretor.classe.ReativacaoCliente;
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.classe.Titulo;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaCusto;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.classe.VendaVendedor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.classe.VendasFrete;
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

	public List<VendasEmGeral> trocadefeitodiferente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<VendasEmGeralItem> trocadefeitodiferenteitem(BigDecimal pedido);
	
	
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
	
	public List<ClienteNovo> clientenovo_venda(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<Vendedor> consultavendedor();
	
	public List<Cliente> consultacliente(String palavra);
	
	public List<Cliente> clientes();
	
	public List<Gestor> consultagestor(String vendedor1, String vendedor2);
	
	public VendasEmGeralItem consultaitem(BigDecimal produto);
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano, String mes, Date data_grafico, Date data_grafico2);
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano, String mes);
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<PedidoItem> pedidoitem(BigDecimal pedido);
	
	public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2);
	
	public List<AnaliseClientePedido> analiseclientepedido(Date data1, Date data2, BigDecimal cliente,String cnpj, BigDecimal pedido);
	
	public List<DadosCliente> dadoscliente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<PedidosConferidos> pedidosconferidos(Date data1, Date data2);
	public List<PedidosConferidosUsuario> pedidosconferidosusuarios(Date data1, Date data2);
	
	public List<RetornoAfinacao> retornoafinacao(Date data1, Date data2, String cfop);
	
	public List<VendasEndereco> vendasendereco(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2);
	
	public List<NotasClienteEmail> notasclienteemails(String ano, String mes, String dia);
	
	public List<MateriaPrimaEstrutura> materiaPrimaEstrutura(String produtoid);
	
	public List<TabelaPreco> tabelapreco();
	
	public List<ItensTabela> itenstabela(String idtabela);
	public ItensTabela itenstabela(String idtabela, String produtoid);
	
	public List<FasePedido> fasepedido(int venda, int outros,Date data1, Date data2, String pedido, String lote);
	public List<FasePedido> fasepedidodatafase(int venda, int outros,Date data1, Date data2, String pedido, String lote,Date fase);
	public List<FasePedidoItem> fasepedidoitem(String pedido);
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro,Date data1, Date data2,String pediddo,String lote);
	public List<FaseMateriaPrima> fasemateriaprima(String pedido,String produto);
	public List<PrazoPedido> prazopedido(int venda, int outros,Date data1, Date data2);
	
	public List<Produto> produtosgrupo(String grupo);
	public Imagem imagem(String produtoid); 
	
	public List<VendasFrete> vendasfrete(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2, String uf);
	
	//willian
	public List<ClientesAtivos> clientesativos(String ano);
	public List<ClientesAtivos> clientesativos2(String ano);
	public List<NovasVendas_Cliente> novasvendas_cliente(String ano);
	public List<Orcamentos> orcamentos(String ano);
	public List<VendaUF> vendauf(String ano,String mes);
	public List<VendaUF> vendaufpedido(String ano,String mes);
	public List<VendaCusto> vendacusto(String ano, String produtos, String vendedor);
	public List<VendaCusto> vendacustopedido(String ano, String produtos, String vendedor);
	public List<VendaVendedor> vendavendedorfatura(String ano, String mes);
	public List<VendaVendedor> vendavendedor(String ano, String mes);
	public List<ProdutoRanking> produtoranking(Date data1, Date data2, String vendedor, String produtos );
	
	public List<P1_MetaFaturado> p1metafaturado(Date data1, Date data2);
	public List<P1_FaturadoDia> p1faturadodia(Date data1, Date data2);
	public List<P1_MetaFaturado> p1metapedido(Date data1, Date data2);
	public List<P1_FaturadoDia> p1pedidodia(Date data1, Date data2);
	
	public List<CtaCorrente> ctacorrente();
	
	//comercial
	public List<Titulo> titulos(String cliente, String status, String nota);
	public List<Nota> notas(String cliente, String nota);
	
	//televendas ocorrencia
	public List<TLOcorrencia> tlocorrencias(String criador, Date data1, Date data2, String status, String tipo,Date data3, Date data4);
	
	//pontução de campanha de representantes
	public List<PT_Meta> pt_meta(String regiao,String vendedor1, String vendedor2);
	public List<PT_Mix> pt_mix(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira2(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira3(String regiao,String vendedor1, String vendedor2);
	public List<PT_Carteira> pt_carteira4(String regiao,String vendedor1, String vendedor2);
	
	//elias
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String uf, String regiao,String vendedor3, String status);
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
	
	//perca produção
	public List<Perca> perca(String ano, String mes,String setor, int i,String tipo, int t);
	public List<PercaDia> percadia(String ano, String mes,String setor, int i,String tipo, int t);
	public List<TipoPerca> tipoperca();
	public List<PercaProduto> percaproduto(String ano, String mes,String setor, int i,String tipo, int t);
	
	//producao
	public List<Producao> producao(String ano, String mes);
	public List<ProducaoDia> producaodia(String ano, String mes, String setor, int i);
	
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
