package br.com.dwbidiretor.bean;

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

import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoClientesNovos;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMetaVenda;
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
	private List<VendasEmGeral> listaexpositor = new ArrayList<>();
	
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
	
	/*grafico metavenda*/
	private BarChartModel graficometavenda;
	@Inject
	private ServicoMetaVenda servicometavenda;
	private List<MetaVenda> listametavenda = new ArrayList<>();
	
	//filtros
	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	
	private String gestorfiltrado;
	private String gestorfiltrado2;

	@PostConstruct
	public void init() {
		
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
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if ((Date) session.getAttribute("data1") != null) {
			this.data_grafico = (Date) session.getAttribute("data1");
			this.data_grafico2 = (Date) session.getAttribute("data2");
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		} else {			
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
			
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		}

		session.removeAttribute("data1");
		session.removeAttribute("data2");
		
		/*gerar grafico*/
		createAnimatedModels();
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
		
		listavenda = servico.vendasemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		listaamostra = servico.amostraemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		listabonificacao = servico.bonificacaoemgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		listaexpositor = servico.expositoremgeral(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		
		listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		/*gerar grafico*/
		createAnimatedModels();
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

	/* dados vendaemgeral */
	public String encaminha2() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
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
		return "/pages/relatorios/vendedormetavenda/vendedormetavenda.xhtml";
	}
	
	/* dados amostraemgeral */
	public String encaminha5() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
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
		session.setAttribute("vendedor", this.vendedor);
		session.setAttribute("gestor", this.gestor);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/expositoremgeral.xhtml";
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

		return new DecimalFormat("###,###.###").format(total);
	}
	public String getValorTotalAmostra() {
		float total = 0;

		for (VendasEmGeral venda : getListaamostra()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}
	
	public String getValorTotalBonificacao() {
		float total = 0;

		for (VendasEmGeral venda : getListabonificacao()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}
	
	public String getValorTotalExpositor() {
		float total = 0;

		for (VendasEmGeral venda : getListaexpositor()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
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
	
	public int getBonificacaododia() {
		int total = 0;

		for (VendasEmGeral bonificacao : getListabonificacao()) {
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
		XAxis.setLabel("Regi�o");
	}
	
	@SuppressWarnings("null")
	public BarChartModel initBarModel() {
    	
    	listametavenda = servicometavenda.metavenda(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
    	
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
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));
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
