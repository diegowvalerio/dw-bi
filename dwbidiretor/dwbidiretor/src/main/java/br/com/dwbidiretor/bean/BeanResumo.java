package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoAnaliseClientePedido;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoClientesNovos;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMetaVenda;
import br.com.dwbidiretor.servico.ServicoSigeUsuario;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanResumo implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendasEmGeral vendasEmGeral = new VendasEmGeral();
	@Inject
	private ServicoVendasemGeral servico;
	private List<VendasEmGeral> listavenda = new ArrayList<>();
	private List<VendasEmGeral> listaamostra = new ArrayList<>();
	private List<VendasEmGeral> listabonificacao = new ArrayList<>();
	private List<VendasEmGeral> listabonificacaoexpositor = new ArrayList<>();
	private List<VendasEmGeral> listaexpositor = new ArrayList<>();
	private List<VendasEmGeral> listabrinde = new ArrayList<>();
	private List<VendasEmGeral> listaamostrapaga = new ArrayList<>();
	private List<VendasEmGeral> listatrocadefeito = new ArrayList<>();
	private List<VendasEmGeral> listatrocadefeitodiferente = new ArrayList<>();
	private List<VendasEmGeral> listatrocanegocio = new ArrayList<>();
	private List<VendasEmGeral> listainvestimento = new ArrayList<>();
	private List<VendasEmGeral> listainvestimento_2 = new ArrayList<>();
	private List<VendasEmGeral> listafaturamento = new ArrayList<>();
	
	private ClientesNovos clientesNovos = new ClientesNovos();
	@Inject
	private ServicoClientesNovos servicoclientes;
	private List<ClientesNovos> listaclientes = new ArrayList<>();
	
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	
	//filtro gestor
	private Gestor gestor = new Gestor();	
	@Inject
	private ServicoGestor servicogestor;
	private List<Gestor> listagestor = new ArrayList<>();
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();
	
	/*grafico metavenda*/
	private BarChartModel graficometavenda;
	@Inject
	private ServicoMetaVenda servicometavenda;
	private List<MetaVenda> listametavenda = new ArrayList<>();
	
	@Inject
	private ServicoSigeUsuario servicousuario;
	
	//filtros
	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	
	private String gestorfiltrado;
	private String gestorfiltrado2;
	
	private String clientefiltrado;
	private String clientefiltrado2;
	
	//totais por tipo de pedido
	private float tot_amostra;
	private float tot_amostrapaga;
	private float tot_bonificacao;
	private float tot_bonificacaoexpositor;
	private float tot_expositor;
	private float tot_brinde;
	private float tot_trocadefeito;
	private float tot_trocadefeitodiferente;
	private float tot_trocanegocio;
	private float tot_venda;
	
	private String mes;
	private String ano;
	
	private String latitude;
	private String longitude;
	private String aparelho;

	@PostConstruct
	public void init() {
		
		vendedor = null;
		gestor = null;
		cliente = null;
				
				
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d = new Date();
		servicousuario.registralog("acesso a pagina de inicio", "DIRETOR", formatador.format(d), latitude, longitude,aparelho);
					
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		listavendedor = servicovendedor.consultavendedor();
		vendedorfiltrado = "0";
		vendedorfiltrado2 = "999999";
		
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		gestorfiltrado = "0";
		gestorfiltrado2 = "999999";
		
		clientefiltrado = "0";
		clientefiltrado2 = "999999";
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if ((Date) session.getAttribute("data1") != null) {
			this.data_grafico = (Date) session.getAttribute("data1");
			this.data_grafico2 = (Date) session.getAttribute("data2");
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2 ,clientefiltrado, clientefiltrado2);
			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabonificacaoexpositor = servico.bonificacaoexpositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			
			listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocadefeitodiferente = servico.trocadefeitodiferente(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listainvestimento = servico.investimentooemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listainvestimento_2 = servico.investimentooemgeral_2(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listafaturamento = servico.faturamentoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			
			listaclientes = servicoclientes.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		} else {			
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabonificacaoexpositor = servico.bonificacaoexpositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocadefeitodiferente = servico.trocadefeitodiferente(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listainvestimento = servico.investimentooemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			listainvestimento_2 = servico.investimentooemgeral_2(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);			
			listafaturamento = servico.faturamentoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
			
			listaclientes = servicoclientes.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(data_grafico);
		
		mes = data.substring(3,5);
		ano = data.substring(6,10);

		session.removeAttribute("data1");
		session.removeAttribute("data2");
		session.removeAttribute("vendedor");
		session.removeAttribute("gestor");
		session.removeAttribute("cliente");
		
		/*gerar grafico*/
		createAnimatedModels();
	}
	
	public void ipr() {
		System.out.println("ip:"+longitude+" aparelho:"+aparelho);
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d = new Date();
		servicousuario.registralog("acesso a pagina de inicio -> IP", "DIRETOR", formatador.format(d), latitude, longitude,aparelho);
	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		if (gestor == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}
		
		if (cliente == null){
			clientefiltrado = "0";
			clientefiltrado2 = "999999";
			
		}else{
			clientefiltrado = cliente.getCodigocliente().toString();
			clientefiltrado2 = cliente.getCodigocliente().toString();
		}
		
		listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listabonificacaoexpositor = servico.bonificacaoexpositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listatrocadefeitodiferente = servico.trocadefeitodiferente(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listainvestimento = servico.investimentooemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		listainvestimento_2 = servico.investimentooemgeral_2(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);		
		listafaturamento = servico.faturamentoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		
		listaclientes = servicoclientes.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,clientefiltrado, clientefiltrado2);
		/*gerar grafico*/
		createAnimatedModels();
	}
	
	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
	}

	public List<VendasEmGeral> getListatrocadefeitodiferente() {
		return listatrocadefeitodiferente;
	}

	public void setListatrocadefeitodiferente(List<VendasEmGeral> listatrocadefeitodiferente) {
		this.listatrocadefeitodiferente = listatrocadefeitodiferente;
	}

	public float getTot_trocadefeitodiferente() {
		return tot_trocadefeitodiferente;
	}

	public void setTot_trocadefeitodiferente(float tot_trocadefeitodiferente) {
		this.tot_trocadefeitodiferente = tot_trocadefeitodiferente;
	}

	public String getAparelho() {
		return aparelho;
	}

	public void setAparelho(String aparelho) {
		this.aparelho = aparelho;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<VendasEmGeral> getListabonificacaoexpositor() {
		return listabonificacaoexpositor;
	}

	public void setListabonificacaoexpositor(List<VendasEmGeral> listabonificacaoexpositor) {
		this.listabonificacaoexpositor = listabonificacaoexpositor;
	}

	public float getTot_bonificacaoexpositor() {
		return tot_bonificacaoexpositor;
	}

	public void setTot_bonificacaoexpositor(float tot_bonificacaoexpositor) {
		this.tot_bonificacaoexpositor = tot_bonificacaoexpositor;
	}

	public float getTot_amostra() {
		return tot_amostra;
	}

	public void setTot_amostra(float tot_amostra) {
		this.tot_amostra = tot_amostra;
	}

	public float getTot_amostrapaga() {
		return tot_amostrapaga;
	}

	public void setTot_amostrapaga(float tot_amostrapaga) {
		this.tot_amostrapaga = tot_amostrapaga;
	}

	public float getTot_bonificacao() {
		return tot_bonificacao;
	}

	public void setTot_bonificacao(float tot_bonificacao) {
		this.tot_bonificacao = tot_bonificacao;
	}

	public float getTot_expositor() {
		return tot_expositor;
	}

	public void setTot_expositor(float tot_expositor) {
		this.tot_expositor = tot_expositor;
	}

	public float getTot_brinde() {
		return tot_brinde;
	}

	public void setTot_brinde(float tot_brinde) {
		this.tot_brinde = tot_brinde;
	}

	public float getTot_trocadefeito() {
		return tot_trocadefeito;
	}

	public void setTot_trocadefeito(float tot_trocadefeito) {
		this.tot_trocadefeito = tot_trocadefeito;
	}

	public float getTot_trocanegocio() {
		return tot_trocanegocio;
	}

	public void setTot_trocanegocio(float tot_trocanegocio) {
		this.tot_trocanegocio = tot_trocanegocio;
	}

	public float getTot_venda() {
		return tot_venda;
	}

	public void setTot_venda(float tot_venda) {
		this.tot_venda = tot_venda;
	}

	public String getClientefiltrado() {
		return clientefiltrado;
	}

	public void setClientefiltrado(String clientefiltrado) {
		this.clientefiltrado = clientefiltrado;
	}

	public String getClientefiltrado2() {
		return clientefiltrado2;
	}

	public void setClientefiltrado2(String clientefiltrado2) {
		this.clientefiltrado2 = clientefiltrado2;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListacliente() {
		return listacliente;
	}

	public void setListacliente(List<Cliente> listacliente) {
		this.listacliente = listacliente;
	}

	public List<VendasEmGeral> getListafaturamento() {
		return listafaturamento;
	}

	public void setListafaturamento(List<VendasEmGeral> listafaturamento) {
		this.listafaturamento = listafaturamento;
	}

	public Gestor getGestor() {
		return gestor;
	}

	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	public List<Gestor> getListagestor() {
		return listagestor;
	}

	public void setListagestor(List<Gestor> listagestor) {
		this.listagestor = listagestor;
	}

	public String getGestorfiltrado() {
		return gestorfiltrado;
	}

	public void setGestorfiltrado(String gestorfiltrado) {
		this.gestorfiltrado = gestorfiltrado;
	}

	public String getGestorfiltrado2() {
		return gestorfiltrado2;
	}

	public void setGestorfiltrado2(String gestorfiltrado2) {
		this.gestorfiltrado2 = gestorfiltrado2;
	}

	public BarChartModel getGraficometavenda() {
		return graficometavenda;
	}

	public void setGraficometavenda(BarChartModel graficometavenda) {
		this.graficometavenda = graficometavenda;
	}

	public List<MetaVenda> getListametavenda() {
		return listametavenda;
	}

	public void setListametavenda(List<MetaVenda> listametavenda) {
		this.listametavenda = listametavenda;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<Vendedor> getListavendedor() {
		return listavendedor;
	}

	public void setListavendedor(List<Vendedor> listavendedor) {
		this.listavendedor = listavendedor;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}

	public Date getData_grafico() {
		return data_grafico;
	}

	public void setData_grafico(Date data_grafico) {
		this.data_grafico = data_grafico;
	}

	public Date getData_grafico2() {
		return data_grafico2;
	}

	public void setData_grafico2(Date data_grafico2) {
		this.data_grafico2 = data_grafico2;
	}

	public List<VendasEmGeral> getListavenda() {
		return listavenda;
	}

	public void setListavenda(List<VendasEmGeral> listavenda) {
		this.listavenda = listavenda;
	}

	public String getVendedorfiltrado() {
		return vendedorfiltrado;
	}

	public void setVendedorfiltrado(String vendedorfiltrado) {
		this.vendedorfiltrado = vendedorfiltrado;
	}
	
	
	public VendasEmGeral getVendasEmGeral() {
		return vendasEmGeral;
	}

	public void setVendasEmGeral(VendasEmGeral vendasEmGeral) {
		this.vendasEmGeral = vendasEmGeral;
	}
	
	public String getVendedorfiltrado2() {
		return vendedorfiltrado2;
	}

	public void setVendedorfiltrado2(String vendedorfiltrado2) {
		this.vendedorfiltrado2 = vendedorfiltrado2;
	}


	public ClientesNovos getClientesNovos() {
		return clientesNovos;
	}

	public void setClientesNovos(ClientesNovos clientesNovos) {
		this.clientesNovos = clientesNovos;
	}

	public List<ClientesNovos> getListaclientes() {
		return listaclientes;
	}

	public void setListaclientes(List<ClientesNovos> listaclientes) {
		this.listaclientes = listaclientes;
	}
	public List<VendasEmGeral> getListaamostra() {
		return listaamostra;
	}

	public void setListaamostra(List<VendasEmGeral> listaamostra) {
		this.listaamostra = listaamostra;
	}
	public List<VendasEmGeral> getListabonificacao() {
		return listabonificacao;
	}

	public void setListabonificacao(List<VendasEmGeral> listabonificacao) {
		this.listabonificacao = listabonificacao;
	}
	public List<VendasEmGeral> getListaexpositor() {
		return listaexpositor;
	}

	public void setListaexpositor(List<VendasEmGeral> listaexpositor) {
		this.listaexpositor = listaexpositor;
	}

	public List<VendasEmGeral> getListabrinde() {
		return listabrinde;
	}

	public void setListabrinde(List<VendasEmGeral> listabrinde) {
		this.listabrinde = listabrinde;
	}

	public List<VendasEmGeral> getListainvestimento() {
		return listainvestimento;
	}

	public void setListainvestimento(List<VendasEmGeral> listainvestimento) {
		this.listainvestimento = listainvestimento;
	}

	public List<VendasEmGeral> getListaamostrapaga() {
		return listaamostrapaga;
	}

	public void setListaamostrapaga(List<VendasEmGeral> listaamostrapaga) {
		this.listaamostrapaga = listaamostrapaga;
	}

	public List<VendasEmGeral> getListatrocadefeito() {
		return listatrocadefeito;
	}

	public void setListatrocadefeito(List<VendasEmGeral> listatrocadefeito) {
		this.listatrocadefeito = listatrocadefeito;
	}

	public List<VendasEmGeral> getListatrocanegocio() {
		return listatrocanegocio;
	}

	public void setListatrocanegocio(List<VendasEmGeral> listatrocanegocio) {
		this.listatrocanegocio = listatrocanegocio;
	}

	public List<VendasEmGeral> getListainvestimento_2() {
		return listainvestimento_2;
	}

	public void setListainvestimento_2(List<VendasEmGeral> listainvestimento_2) {
		this.listainvestimento_2 = listainvestimento_2;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	/* dados vendaemgeral */
	public String encaminha2() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/vendaemgeral.xhtml";
	}
	
	/* dados clientesnovos */
	public String encaminha3() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/clientesnovos/clientesnovos.xhtml";
	}
	
	/* dados clientesnovos */
	public String encaminha4() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("ano", this.ano);
		session.setAttribute("mes", this.mes);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);
		return "/pages/relatorios/vendedormetavenda/vendedormetavenda.xhtml";
	}
	
	/* dados amostraemgeral */
	public String encaminha5() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/amostraemgeral.xhtml";
	}
	
	/* dados bonificacaoemgeral */
	public String encaminha6() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/bonificacaoemgeral.xhtml";
	}
	
	/* dados expositoremgeral */
	public String encaminha7() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/expositoremgeral.xhtml";
	}

	/* dados faturamento emgeral */
	public String encaminha8() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/faturamentoemgeral.xhtml";
	}
	
	/* dados faturamento emgeral */
	public String encaminha9() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/brindeemgeral.xhtml";
	}
	
	/* dados investimento emgeral */
	public String encaminha10() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/investimentovendedor.xhtml";
	}
	
	/* dados investimento emgeral */
	public String encaminha11() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/trocadefeitoemgeral.xhtml";
	}
	
	public String encaminha20() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/trocadefeitodiferente.xhtml";
	}
	
	public String encaminha12() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/trocanegocioemgeral.xhtml";
	}
	
	public String encaminha13() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/amostrapagaemgeral.xhtml";
	}
	
	/* dados investimento emgeral */
	public String encaminha14() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/investimentovendedor_2.xhtml";
	}
//bonificacao expositor
	public String encaminha15() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/bonificacaoexpositoremgeral.xhtml";
	}
	
	/* dados clientesnovos */
	public String encaminha16() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/clientesnovos/clientesnovos_efetivado.xhtml";
	}
	
	public String encaminha17() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/totalinvestimento.xhtml";
	}
	/* pegar usuario conectado */
	public String usuarioconectado() {
		String nome;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		// System.out.println(nome);
		return nome;
	}

	public String getValorTotal() {
		float total = 0;
		tot_venda = 0;
		for (VendasEmGeral venda : getListavenda()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_venda = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalAmostra() {
		float total = 0;
		tot_amostra = 0;
		for (VendasEmGeral venda : getListaamostra()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_amostra = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalAmostraPaga() {
		float total = 0;
		tot_amostrapaga = 0;
		for (VendasEmGeral venda : getListaamostrapaga()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_amostrapaga = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalTrocaDefeito() {
		float total = 0;
		tot_trocadefeito = 0;
		for (VendasEmGeral venda : getListatrocadefeito()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_trocadefeito = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalTrocaDefeitoDiferente() {
		float total = 0;
		tot_trocadefeitodiferente = 0;
		for (VendasEmGeral venda : getListatrocadefeitodiferente()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_trocadefeitodiferente = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalTrocaNegocio() {
		float total = 0;
		tot_trocanegocio = 0;
		for (VendasEmGeral venda : getListatrocanegocio()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_trocanegocio = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalBonificacao() {
		float total = 0;
		tot_bonificacao = 0;
		for (VendasEmGeral venda : getListabonificacao()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_bonificacao = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalBonificacaoexpositor() {
		float total = 0;
		tot_bonificacaoexpositor = 0;
		for (VendasEmGeral venda : getListabonificacaoexpositor()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_bonificacaoexpositor = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalBrinde() {
		float total = 0;
		tot_brinde = 0;
		for (VendasEmGeral venda : getListabrinde()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_brinde = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalExpositor() {
		float total = 0;
		tot_expositor = 0;
		for (VendasEmGeral venda : getListaexpositor()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		tot_expositor = total;
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getTotalInvestimento_Entrada_Pedido() {
		float total = 0;
		total = tot_expositor + tot_amostra + tot_bonificacaoexpositor + tot_bonificacao + tot_trocanegocio + tot_brinde;
		
		return new DecimalFormat("###,###.##").format(total);
	}
	public float getPercentual_investimento_entrada_pedido() {		
		float total = 0;
		total = tot_expositor + tot_amostra + tot_bonificacaoexpositor + tot_bonificacao + tot_trocanegocio + tot_brinde;
		if(tot_venda ==0){
			tot_venda = 1;
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(total == 0){
			total = 1;
			atingido = 100;
		}else{
			atingido = (total / tot_venda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public String getValorTotalSobFaturado() {
		float total = 0;
	
		
		for (VendasEmGeral venda : getListainvestimento()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public float getPercentualSobFaturado() {
		
		float total = 0;
		
		float total2 = 0;
		
		for (VendasEmGeral venda : getListainvestimento()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral faturamento : getListafaturamento()) {
			total2 = total2 + faturamento.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(total2 == 0){
			total2 = 1;
			atingido = 100;
		}else{
			atingido = (total / total2)*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public String getValorTotalSobFaturado_2() {
		float total = 0;
	
		
		for (VendasEmGeral venda : getListainvestimento_2()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public float getPercentualSobFaturado_2() {
		
		float total = 0;
		
		float total2 = 0;
		
		for (VendasEmGeral venda : getListainvestimento_2()) {
			total = total + venda.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral faturamento : getListafaturamento()) {
			total2 = total2 + faturamento.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(total2 == 0){
			total2 = 1;
			atingido = 100;
		}else{
			atingido = (total / total2)*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public String getValorTotalFaturamento() {
		float total = 0;

		for (VendasEmGeral faturamento : getListafaturamento()) {
			total = total + faturamento.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	// painel de resumo

	public int getPedidododia() {
		int total = 0;

		for (VendasEmGeral venda : getListavenda()) {
			total++;
		}

		return total;
	}
	
	public int getAmostradodia() {
		int total = 0;

		for (VendasEmGeral amostra : getListaamostra()) {
			total++;
		}

		return total;
	}
	public int getAmostraPagadodia() {
		int total = 0;

		for (VendasEmGeral amostra : getListaamostrapaga()) {
			total++;
		}

		return total;
	}
	public int getTrocaDefeitododia() {
		int total = 0;

		for (VendasEmGeral amostra : getListatrocadefeito()) {
			total++;
		}

		return total;
	}	
	public int getTrocaDefeitoDiferentedodia() {
		int total = 0;

		for (VendasEmGeral amostra : getListatrocadefeitodiferente()) {
			total++;
		}

		return total;
	}
	public int getTrocaNegociododia() {
		int total = 0;

		for (VendasEmGeral amostra : getListatrocanegocio()) {
			total++;
		}

		return total;
	}	
	public int getBonificacaododia() {
		int total = 0;

		for (VendasEmGeral bonificacao : getListabonificacao()) {
			total++;
		}

		return total;
	}
	
	public int getBonificacaoExpositordodia() {
		int total = 0;

		for (VendasEmGeral bonificacao : getListabonificacaoexpositor()) {
			total++;
		}

		return total;
	}
	
	public int getExpositordodia() {
		int total = 0;

		for (VendasEmGeral expositor : getListaexpositor()) {
			total++;
		}

		return total;
	}
	
	public int getBrindedodia() {
		int total = 0;

		for (VendasEmGeral expositor : getListabrinde()) {
			total++;
		}

		return total;
	}
	
	public int getFaturamentododia() {
		int total = 0;

		for (VendasEmGeral faturado : getListafaturamento()) {
			total++;
		}

		return total;
	}
	
	
	public int getTotalClientes() {
		int total = 0;

		for (ClientesNovos cliente : getListaclientes()) {
			total++;
		}

		return total;
	}
	
	public int getTotalClientes_Efetivado() {
		int total = 0;

		for (ClientesNovos cliente : getListaclientes()) {
			if(cliente.getVendas().intValue() > 0) {
			total++;
			}
		}

		return total;
	}
	
	
	public float getPercentualSobPedido_Amostra() {
		
		float tvenda = 0;
		
		float tamostra = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral amostra : getListaamostra()) {
			tamostra = tamostra + amostra.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tamostra == 0){
			tamostra = 1;
			atingido = 100;
		}else{
			atingido = (tamostra / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getPercentualSobPedido_AmostraPaga() {
		
		float tvenda = 0;
		
		float tamostrapaga = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral amostra : getListaamostrapaga()) {
			tamostrapaga = tamostrapaga + amostra.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tamostrapaga == 0){
			tamostrapaga = 1;
			atingido = 100;
		}else{
			atingido = (tamostrapaga / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}

	public float getPercentualSobPedido_Bonificacao() {
		
		float tvenda = 0;
		
		float tbonificacao = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral bonificacao : getListabonificacao()) {
			tbonificacao = tbonificacao + bonificacao.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tbonificacao == 0){
			tbonificacao = 1;
			atingido = 100;
		}else{
			atingido = (tbonificacao / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getPercentualSobPedido_BonificacaoExpositor() {
		
		float tvenda = 0;
		
		float tbonificacao = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral bonificacao : getListabonificacaoexpositor()) {
			tbonificacao = tbonificacao + bonificacao.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tbonificacao == 0){
			tbonificacao = 1;
			atingido = 100;
		}else{
			atingido = (tbonificacao / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getPercentualSobPedido_Expositor() {
		
		float tvenda = 0;
		
		float texpositor = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral expositor : getListaexpositor()) {
			texpositor = texpositor + expositor.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(texpositor == 0){
			texpositor = 1;
			atingido = 100;
		}else{
			atingido = (texpositor / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getPercentualSobPedido_Brinde() {
		
		float tvenda = 0;
		
		float tbrinde = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral brinde : getListabrinde()) {
			tbrinde = tbrinde + brinde.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tbrinde == 0){
			tbrinde = 1;
			atingido = 100;
		}else{
			atingido = (tbrinde / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}

	public float getPercentualSobPedido_TrocaDefeito() {
		
		float tvenda = 0;
		
		float tdefeito = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		
		for (VendasEmGeral defeito : getListatrocadefeito()) {
			tdefeito = tdefeito + defeito.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tdefeito == 0){
			tdefeito = 1;
			atingido = 100;
		}else{
			atingido = (tdefeito / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getPercentualSobPedido_TrocaDefeitoDiferente() {
		
		float tvenda = 0;
		
		float tdefeito = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		
		for (VendasEmGeral defeito : getListatrocadefeitodiferente()) {
			tdefeito = tdefeito + defeito.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tdefeito == 0){
			tdefeito = 1;
			atingido = 100;
		}else{
			atingido = (tdefeito / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	public float getPercentualSobPedido_TrocaNegocio() {
		
		float tvenda = 0;
		
		float tnegocio = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			tvenda = tvenda + venda.getValortotalpedido().floatValue();
		}
		if(tvenda ==0){
			tvenda = 1;
		}
		for (VendasEmGeral negocio : getListatrocanegocio()) {
			tnegocio = tnegocio + negocio.getValortotalpedido().floatValue();
		}
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(tnegocio == 0){
			tnegocio = 1;
			atingido = 100;
		}else{
			atingido = (tnegocio / tvenda )*100;
		}
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getLucrogeral() {
		
		float total = 0;
		float total2 = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			total = total + venda.getValortotalpedido().floatValue();
			total2 = total2 + venda.getValortotalliquidopedido().floatValue();
		}
		
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		if(total2 == 0){
			total2 = 1;
			atingido = 100;
		}else{
			atingido = (total2 / total)*100;
		}
		
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));

	}
	
	public float getMediaLucrogeral() {
		float total = 0;
		float t = 0;
		
		for (VendasEmGeral venda : getListavenda()) {
			float t2 = 0;
			//t2 = (venda.getValortotalliquidopedido().floatValue() / venda.getValortotalpedido().floatValue())*100;
			t2 = venda.getPerc_lucro().floatValue();
			t = t + t2;
			total++;
			//System.out.println(t+" pedido:"+venda.getPedido().toString()+"- "+t2);
		}
		
		float atingido = 0;
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		
		if(t == 0 && atingido ==0) {
			return 0;
		}else {
			atingido = t / total;
			return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));
		}
	}
	
	public void createAnimatedModels() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(data_grafico2);
		
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
		Calendar hoje = Calendar.getInstance();
		
		Date d = new Date();
		graficometavenda = initBarModel();
		graficometavenda.setTitle("Meta x Venda ("+mes+"/"+ano+")");
		graficometavenda.setAnimate(true);
		graficometavenda.setLegendPosition("ne");
		graficometavenda.setSeriesColors("20B2AA,808080");
		Axis yAxis = graficometavenda.getAxis(AxisType.Y);
		yAxis = graficometavenda.getAxis(AxisType.Y);
		yAxis.setMin(0);
		// yAxis.setMax(20000);
		yAxis.setTickFormat("R$ %'.2f");
		yAxis.setLabel("Valor");

		Axis XAxis = graficometavenda.getAxis(AxisType.X);
		XAxis.setLabel("Regi�o");
	}
	
	@SuppressWarnings("null")
	public BarChartModel initBarModel() {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(data_grafico2);
		
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		    	
    	listametavenda = servicometavenda.metavenda(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2,ano,mes,data_grafico, data_grafico2);
    	
    	 BarChartModel model = new BarChartModel();
         
    	if(!listametavenda.isEmpty()){
    		List<String> tipo = new ArrayList<>();
    		for (MetaVenda movimento1: listametavenda){
    			tipo.add(movimento1.getTipo());
    		}
    		List<String> tipoc = new ArrayList<>();
    		for(String c : tipo){
    			if(!tipoc.contains(c)){
    				tipoc.add(c);
    			}
    		}
    			    			
    		List<String> regioes = new ArrayList<>();
    		List<String> regioes2 = new ArrayList<>();
    		for(String c:tipoc){
    		ChartSeries tipopedido = new ChartSeries();
    		tipopedido.setLabel(c);	
    		
    			for (MetaVenda movimento2: listametavenda){
        			if(movimento2.getTipo().equals(c)){
        				tipopedido.set(movimento2.getRegiao(), movimento2.getValor());
        				regioes.add(movimento2.getRegiao());
        			} 
        		}
    		model.addSeries(tipopedido);	
    		}
    		
    	}else{
    		ChartSeries tipopedido = new ChartSeries();
    		tipopedido.set("0",0);
    		model.addSeries(tipopedido);
    	}
                
        return model;
    }

	public String getMetaTotal() {
		float total = 0;

		for (MetaVenda meta : getListametavenda()) {
			if(meta.getTipo().equals("META")){
			total = total + meta.getValor().floatValue();
			}
		}

		return new DecimalFormat("###,###.###").format(total);
	}
	
	public float getMetaAtingida() {
		float totalm = 0;
		float totalv = 0;
		float atingido = 0;
		for (MetaVenda meta : getListametavenda()) {
			if(meta.getTipo().equals("META")){
				totalm = totalm + meta.getValor().floatValue();
			}else{
				totalv = totalv + meta.getValor().floatValue();	
			}
				
		}
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		atingido = (totalv / totalm)*100;
		if (totalm > 0){
			return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));
		}else{
			return Float.parseFloat(formatarFloat.format(0).replace(",", "."));
		}
	}

	public void filtragestor() {
		if (getVendedor() != null){
			List<Gestor> listagestorf = new ArrayList<>();
			gestor = new Gestor();
			listagestorf = servicogestor.consultagestor(getVendedor().getCodigovendedor().toString(),getVendedor().getCodigovendedor().toString());
			gestor = listagestorf.get(0);
		}else{
			//listagestor = new ArrayList<>();
			gestor = new Gestor();
			//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		}
	}

}
