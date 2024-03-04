package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.UnselectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.VendaCusto;
import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCidadeVenda;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.ServicoVendaCusto;
import br.com.dwbidiretor.servico.ServicoVendaUF;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanVendaCusto implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendaCusto vendacusto = new VendaCusto();

	@Inject
	private ServicoVendaCusto servico;
	private List<VendaCusto> lista = new ArrayList<>();
	private List<VendaCusto> lista2 = new ArrayList<>();
	
	private List<VendaCusto> lista_ano2 = new ArrayList<>();
	private List<VendaCusto> lista2_ano2 = new ArrayList<>();
	
	//filtro produto
	private Produto produto = new Produto();
	private List<Produto> produtos = new ArrayList<>();
	private List<Produto> produtosselecionados = new ArrayList<>();
	@Inject
	private ServicoProduto servicoproduto;
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private Integer filtra = 0;
	private boolean filtro;
	
	private String ano,ano2;
	private String filtroproduto;
	private String filtrovendedor;
	
	private BarChartModel barModel2;

	@PostConstruct
	public void init() {
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(data_grafico);
		ano2 = ""+cal.get(Calendar.YEAR);	
		
		cal.add(Calendar.YEAR,-1);
		ano = ""+cal.get(Calendar.YEAR);
		
		produtos = servicoproduto.produtos();
		listavendedor = servicovendedor.consultavendedor();
		
		filtroproduto = "1";
		filtrovendedor = "-1" ;
		
		createBarModel2();
	}
	
	public void filtrar(){
		if(produtosselecionados.isEmpty()) {
			filtroproduto = "1";
		}else {
			filtroproduto = "";
		}
		
		if (vendedor == null){
			filtrovendedor = "-1" ;			
		}else {
			filtrovendedor = vendedor.getCodigovendedor().toString();
		}
		
		
		for(Produto p:produtosselecionados) {
			filtroproduto = filtroproduto+p.getProdutoid()+",";
		}
		if(!filtroproduto.equals("1")) {
			filtroproduto = filtroproduto.replaceFirst(".$", "");
		}
		
		lista = servico.vendacusto(ano,filtroproduto,filtrovendedor);
		lista2 = servico.vendacustopedido(ano,filtroproduto,filtrovendedor);
		
		lista_ano2 = servico.vendacusto(ano2,filtroproduto,filtrovendedor);
		lista2_ano2 = servico.vendacustopedido(ano2,filtroproduto,filtrovendedor);
		
		
		//System.out.println(filtrovendedor);
		createBarModel2();
	
	}

	/*public void onItemUnselect(UnselectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
	         
	    FacesMessage msg = new FacesMessage();
	    msg.setSummary("Item unselected: " + event.getObject().toString());
	    msg.setSeverity(FacesMessage.SEVERITY_INFO);
	         
	    context.addMessage(null, msg);
	}*/
	
	public void createBarModel2() {
        barModel2 = new BarChartModel();
        ChartData data = new ChartData();
         
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel(""+ano);
        barDataSet.setBackgroundColor("rgba(30, 144, 255)");
        barDataSet.setBorderColor("rgb(0, 0, 255)");
        barDataSet.setBorderWidth(1);
        List<Number> values = new ArrayList<>();
        for(VendaCusto p:lista) {
        		values.add(p.getVenda());
        }
        barDataSet.setData(values);
         
        BarChartDataSet barDataSet2 = new BarChartDataSet();
        barDataSet2.setLabel(""+ano2);
        barDataSet2.setBackgroundColor("rgba(244, 164, 96)");
        barDataSet2.setBorderColor("rgb(160, 82, 45)");
        barDataSet2.setBorderWidth(1);
        List<Number> values2 = new ArrayList<>();
        for(VendaCusto p:lista_ano2) {
    		values2.add(p.getVenda());
        }
        barDataSet2.setData(values2);
 
        data.addChartDataSet(barDataSet);
        data.addChartDataSet(barDataSet2);
         
        List<String> labels = new ArrayList<>();
        if(lista.size() > lista_ano2.size()) {
        	for(VendaCusto p:lista) {
        		 labels.add(p.getMes());
        	}
        }else {
        	for(VendaCusto p:lista_ano2) {
       		 labels.add(p.getMes());
       	}
        	
        }
        data.setLabels(labels);
        barModel2.setData(data);
         
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
         
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Comparativo de Vendas");
        options.setTitle(title);
         
        barModel2.setExtender("my_ext");
        barModel2.setOptions(options);
    }
	
	
	 
	public BarChartModel getBarModel2() {
		return barModel2;
	}

	public void setBarModel2(BarChartModel barModel2) {
		this.barModel2 = barModel2;
	}

	public List<Produto> getProdutosselecionados() {
		return produtosselecionados;
	}

	public List<Vendedor> getListavendedor() {
		return listavendedor;
	}

	public void setListavendedor(List<Vendedor> listavendedor) {
		this.listavendedor = listavendedor;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public String getFiltrovendedor() {
		return filtrovendedor;
	}

	public void setFiltrovendedor(String filtrovendedor) {
		this.filtrovendedor = filtrovendedor;
	}

	public String getFiltroproduto() {
		return filtroproduto;
	}

	public void setFiltroproduto(String filtroproduto) {
		this.filtroproduto = filtroproduto;
	}

	public void setProdutosselecionados(List<Produto> produtosselecionados) {
		this.produtosselecionados = produtosselecionados;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFiltra() {
		return filtra;
	}

	public void setFiltra(Integer filtra) {
		this.filtra = filtra;
	}

	public boolean isFiltro() {
		return filtro;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}


	public VendaCusto getVendacusto() {
		return vendacusto;
	}

	public void setVendacusto(VendaCusto vendacusto) {
		this.vendacusto = vendacusto;
	}

	public List<VendaCusto> getLista() {
		return lista;
	}

	public void setLista(List<VendaCusto> lista) {
		this.lista = lista;
	}

	public List<VendaCusto> getLista2() {
		return lista2;
	}

	public void setLista2(List<VendaCusto> lista2) {
		this.lista2 = lista2;
	}

	public List<VendaCusto> getLista_ano2() {
		return lista_ano2;
	}

	public void setLista_ano2(List<VendaCusto> lista_ano2) {
		this.lista_ano2 = lista_ano2;
	}

	public List<VendaCusto> getLista2_ano2() {
		return lista2_ano2;
	}

	public void setLista2_ano2(List<VendaCusto> lista2_ano2) {
		this.lista2_ano2 = lista2_ano2;
	}

	public String getAno2() {
		return ano2;
	}

	public void setAno2(String ano2) {
		this.ano2 = ano2;
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
	
}
