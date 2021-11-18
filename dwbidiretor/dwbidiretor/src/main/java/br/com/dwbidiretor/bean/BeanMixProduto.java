package br.com.dwbidiretor.bean;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.w3c.dom.css.RGBColor;

import com.bea.xml.stream.samples.Parse;

import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMixProduto;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanMixProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	private MixProduto mixproduto = new MixProduto();
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoMixProduto servico;
	private List<MixProduto> lista = new ArrayList<>();

	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	
	//filtro gestor
	private Gestor gestor = new Gestor();
	@Inject
	private ServicoGestor servicogestor;
	private List<Gestor> listagestor = new ArrayList<>();

	private String vendedorlogado;
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;

	private LineChartModel grafico_mixqtde;
	
	@PostConstruct
	public void init() {
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if (session.getAttribute("vendedor") != null){
			this.vendedor = (Vendedor) session.getAttribute("vendedor");
			if (vendedor == null){
				vendedorfiltrado = "0";
				vendedorfiltrado2 = "999999";
				
			}else{
				vendedorfiltrado = vendedor.getCodigovendedor().toString();
				vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
			}
		}
		if (vendedor.getCodigovendedor() == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		//verifica filtro gestor
		if (session.getAttribute("gestor") != null){
			gestor = (Gestor) session.getAttribute("gestor");
			if (gestor == null){
				gestorfiltrado = "0";
				gestorfiltrado2 = "999999";
				
			}else{
				gestorfiltrado = gestor.getGestorid().toString();
				gestorfiltrado2 = gestor.getGestorid().toString();
			}
		}
		if (gestor.getGestorid() == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}//fim filtro gestor
		
		
		listavendedor = servicovendedor.consultavendedor();
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		
		session.removeAttribute("vendedor");
		session.removeAttribute("gestor");
		
		geragraficomixqtde();
		
	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		if (gestor == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}
		
		lista = servico.mixprodutos(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		
		geragraficomixqtde();
	}
	
	public void geragraficomixqtde() {
		grafico_mixqtde = new LineChartModel();
		ChartData data = new ChartData();
		LineChartDataSet hbarDataSet = new LineChartDataSet();
		hbarDataSet.setLabel("Quantidade");
		
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		values.add(getQtde2018i());
		bgColor.add("rgba(30, 144, 255)");
		labels.add("2018");
		
		values.add(getQtde2019i());
		bgColor.add("rgba(30, 144, 255)");
		labels.add("2019");
		
		values.add(getQtde2020i());
		bgColor.add("rgba(30, 144, 255)");
		labels.add("2020");
		
		values.add(getQtde2021i());
		bgColor.add("rgba(30, 144, 255)");
		labels.add("2021");
		
		hbarDataSet.setData(values);
		hbarDataSet.setFill(true);
		hbarDataSet.setBackgroundColor("rgba(176, 224, 230)");
		
		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		
		
		grafico_mixqtde.setData(data);
		
		LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        grafico_mixqtde.setOptions(options);
        //grafico_mixqtde.setExtender("my_ext");
		
	}
	
	public int getQtde2018i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2018().intValue();
		}
		return t;
	}
	public int getQtde2019i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2019().intValue();
		}
		return t;
	}
	public int getQtde2020i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2020().intValue();
		}
		return t;
	}
	public int getQtde2021i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2021().intValue();
		}
		return t;
	}
	
	
	public String getQtde2018(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2018().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	public String getQtde2019(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2019().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	public String getQtde2020(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2020().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	public String getQtde2021(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2021().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	public String getQtdetotal(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtdetotal().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	
	public String getVl2018(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2018().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}
	public String getVl2019(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2019().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}
	public String getVl2020(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2020().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}
	public String getVl2021(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2021().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}
	public String getVltotal(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVltotal().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}

	
	public LineChartModel getGrafico_mixqtde() {
		return grafico_mixqtde;
	}

	public void setGrafico_mixqtde(LineChartModel grafico_mixqtde) {
		this.grafico_mixqtde = grafico_mixqtde;
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

	public String getVendedorfiltrado() {
		return vendedorfiltrado;
	}

	public void setVendedorfiltrado(String vendedorfiltrado) {
		this.vendedorfiltrado = vendedorfiltrado;
	}

	public String getVendedorfiltrado2() {
		return vendedorfiltrado2;
	}

	public void setVendedorfiltrado2(String vendedorfiltrado2) {
		this.vendedorfiltrado2 = vendedorfiltrado2;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}

	public MixProduto getMixproduto() {
		return mixproduto;
	}

	public void setMixproduto(MixProduto mixproduto) {
		this.mixproduto = mixproduto;
	}

	public List<MixProduto> getLista() {
		return lista;
	}

	public void setLista(List<MixProduto> lista) {
		this.lista = lista;
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
	
	public void filtragestor() {
		if (getVendedor() != null){
			List<Gestor> listagestorf = new ArrayList<>();
			gestor = new Gestor();
			listagestorf = servicogestor.consultagestor(getVendedor().getCodigovendedor().toString(),getVendedor().getCodigovendedor().toString());
			if(listagestorf.size()>0) {
				gestor = listagestorf.get(0);
			}
		}else{
			//listagestor = new ArrayList<>();
			gestor = new Gestor();
			//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		}
	}
}
