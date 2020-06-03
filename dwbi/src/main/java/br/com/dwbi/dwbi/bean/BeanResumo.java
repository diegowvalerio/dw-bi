package br.com.dwbi.dwbi.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.dwbi.classe.Cliente;
import br.com.dwbi.classe.ClientesNovos;
import br.com.dwbi.classe.MetaVenda;
import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.classe.Vendedor;
import br.com.dwbi.dwbi.servico.ServicoCliente;
import br.com.dwbi.dwbi.servico.ServicoClientesNovos;
import br.com.dwbi.dwbi.servico.ServicoMetaVenda;
import br.com.dwbi.dwbi.servico.ServicoVendasemGeral;
import br.com.dwbi.dwbi.servico.ServicoVendedor;

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
	private List<VendasEmGeral> listaexpositor = new ArrayList<>();
	private List<VendasEmGeral> listabrinde = new ArrayList<>();
	private List<VendasEmGeral> listaamostrapaga = new ArrayList<>();
	private List<VendasEmGeral> listatrocadefeito = new ArrayList<>();
	private List<VendasEmGeral> listatrocanegocio = new ArrayList<>();
	
	private ClientesNovos clientesNovos = new ClientesNovos();
	@Inject
	private ServicoClientesNovos servicoclientes;
	private List<ClientesNovos> listaclientes = new ArrayList<>();
	
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	
	/*grafico metavenda*/
	private BarChartModel graficometavenda;
	@Inject
	private ServicoMetaVenda servicometavenda;
	private List<MetaVenda> listametavenda = new ArrayList<>();
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();
	
	//filtros
	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String clientefiltrado;
	private String clientefiltrado2;

	@PostConstruct
	public void init() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		
		vendedorfiltrado = "0";
		vendedorfiltrado2 = "999999";
		
		clientefiltrado = "0";
		clientefiltrado2 = "999999";
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if ((Date) session.getAttribute("data1") != null) {
			this.data_grafico = (Date) session.getAttribute("data1");
			this.data_grafico2 = (Date) session.getAttribute("data2");
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
			listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2,clientefiltrado, clientefiltrado2);
			
			listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);

			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		} else {			
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
			listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2,clientefiltrado, clientefiltrado2);
			
			listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);

			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		}

		session.removeAttribute("data1");
		session.removeAttribute("data2");
		session.removeAttribute("cliente");
		
		/*gerar grafico*/
		createAnimatedModels();
	}
	
	public void filtrar(){
	
		vendedorfiltrado = "0";
		vendedorfiltrado2 = "999999";
			
		if (cliente == null){
			clientefiltrado = "0";
			clientefiltrado2 = "999999";
			
		}else{
			clientefiltrado = cliente.getCodigocliente().toString();
			clientefiltrado2 = cliente.getCodigocliente().toString();
		}
		
		listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listabrinde = servico.brindeemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2,clientefiltrado, clientefiltrado2);
		
		listaamostrapaga = servico.amostrapagaemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listatrocadefeito = servico.trocadefeitoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);
		listatrocanegocio = servico.trocanegocioemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, clientefiltrado, clientefiltrado2);

		listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
		/*gerar grafico*/
		createAnimatedModels();
	}
	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
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
	
	public List<VendasEmGeral> getListabrinde() {
		return listabrinde;
	}

	public void setListabrinde(List<VendasEmGeral> listabrinde) {
		this.listabrinde = listabrinde;
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

	/* dados vendaemgeral */
	public String encaminha2() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/vendaemgeral.xhtml";
	}
	
	/* dados clientesnovos */
	public String encaminha3() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/clientesnovos/clientesnovos.xhtml";
	}

	/* dados amostraemgeral */
	public String encaminha5() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/amostraemgeral.xhtml";
	}
	
	/* dados bonificacaoemgeral */
	public String encaminha6() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/bonificacaoemgeral.xhtml";
	}
	
	/* dados expositoremgeral */
	public String encaminha7() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/expositoremgeral.xhtml";
	}
	
	public String encaminha9() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/brindeemgeral.xhtml";
	}
	
	public String encaminha11() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/trocadefeitoemgeral.xhtml";
	}
	
	public String encaminha12() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/trocanegocioemgeral.xhtml";
	}
	
	public String encaminha13() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("cliente", this.cliente);
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/amostrapagaemgeral.xhtml";
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

		for (VendasEmGeral venda : getListavenda()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalAmostra() {
		float total = 0;

		for (VendasEmGeral venda : getListaamostra()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalAmostraPaga() {
		float total = 0;

		for (VendasEmGeral venda : getListaamostrapaga()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalTrocaDefeito() {
		float total = 0;

		for (VendasEmGeral venda : getListatrocadefeito()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	public String getValorTotalTrocaNegocio() {
		float total = 0;

		for (VendasEmGeral venda : getListatrocanegocio()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalBonificacao() {
		float total = 0;

		for (VendasEmGeral venda : getListabonificacao()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalBrinde() {
		float total = 0;

		for (VendasEmGeral venda : getListabrinde()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}
	
	public String getValorTotalExpositor() {
		float total = 0;

		for (VendasEmGeral venda : getListaexpositor()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}

	// painel de resumo
	public String getTotalInvestimento_Entrada_Pedido() {
		//totais por tipo de pedido
		float tot_amostra = 0;
		float tot_amostrapaga= 0;
		float tot_bonificacao= 0;
		float tot_expositor= 0;
		float tot_brinde= 0;
		float tot_trocanegocio= 0;
		
		for (VendasEmGeral amostra : getListaamostra()) {
			tot_amostra = tot_amostra + amostra.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral amostrapaga : getListaamostrapaga()) {
			tot_amostrapaga = tot_amostrapaga + amostrapaga.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral trocanegocio : getListatrocanegocio()) {
			tot_trocanegocio = tot_trocanegocio + trocanegocio.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral bonificacao : getListabonificacao()) {
			tot_bonificacao = tot_bonificacao + bonificacao.getValortotalpedido().floatValue();
		}

		for (VendasEmGeral brinde : getListabrinde()) {
			tot_brinde = tot_brinde + brinde.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral expositor : getListaexpositor()) {
			tot_expositor = tot_expositor + expositor.getValortotalpedido().floatValue();
		}
		
		float total = 0;
		total = tot_expositor + tot_amostra + tot_amostrapaga + tot_bonificacao + tot_trocanegocio + tot_brinde;
		
		return new DecimalFormat("###,###.##").format(total);
	}
	
	public float getPercentual_investimento_entrada_pedido() {	
		//totais por tipo de pedido
		float tot_venda = 0;
		float tot_amostra = 0;
		float tot_amostrapaga= 0;
		float tot_bonificacao= 0;
		float tot_expositor= 0;
		float tot_brinde= 0;
		float tot_trocanegocio= 0;
		
		for (VendasEmGeral amostra : getListaamostra()) {
			tot_amostra = tot_amostra + amostra.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral amostrapaga : getListaamostrapaga()) {
			tot_amostrapaga = tot_amostrapaga + amostrapaga.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral trocanegocio : getListatrocanegocio()) {
			tot_trocanegocio = tot_trocanegocio + trocanegocio.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral bonificacao : getListabonificacao()) {
			tot_bonificacao = tot_bonificacao + bonificacao.getValortotalpedido().floatValue();
		}

		for (VendasEmGeral brinde : getListabrinde()) {
			tot_brinde = tot_brinde + brinde.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral expositor : getListaexpositor()) {
			tot_expositor = tot_expositor + expositor.getValortotalpedido().floatValue();
		}
		
		for (VendasEmGeral venda : getListavenda()) {
			tot_venda = tot_venda + venda.getValortotalpedido().floatValue();
		}
		
		float total = 0;
		total = tot_expositor + tot_amostra + tot_amostrapaga + tot_bonificacao + tot_trocanegocio + tot_brinde;
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
	
	public int getBrindedodia() {
		int total = 0;

		for (VendasEmGeral expositor : getListabrinde()) {
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
	
	public int getTotalClientes() {
		int total = 0;

		for (ClientesNovos cliente : getListaclientes()) {
			total++;
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

	
	public void createAnimatedModels() {
		Calendar hoje = Calendar.getInstance();
		
		Date d = new Date();
		graficometavenda = initBarModel();
		graficometavenda.setTitle("Meta x Venda ("+ Integer.valueOf(d.getMonth()+1) +"/"+hoje.get(Calendar.YEAR) +")");
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
		XAxis.setLabel("Região");
	}
	
	@SuppressWarnings("null")
	public BarChartModel initBarModel() {
    	
    	listametavenda = servicometavenda.metavenda(vendedorfiltrado,vendedorfiltrado2);
    	
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

		return new DecimalFormat("###,###.##").format(total);
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

}
