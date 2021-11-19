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

import org.hibernate.cfg.ImprovedNamingStrategy;
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
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMixProduto;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.ServicoVendedor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendagrupo;

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
	
	//filtro produto
	private Produto produto = new Produto();
	private List<Produto> produtos = new ArrayList<>();
	@Inject
	private ServicoProduto servicoproduto;
	
	//filtro grupo
	private Venda_Grupo grupo = new Venda_Grupo();
	private List<Venda_Grupo> listagrupos = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_Vendagrupo servicogrupo;

	private String vendedorlogado;
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	
	private String produtofiltro;
	private String produtofiltro2;
	private String grupofiltro;
	private String grupofiltro2;

	private LineChartModel grafico_mixqtde;
	private LineChartModel grafico_mixvl;
	
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
		produtos = servicoproduto.produtos();
		listagrupos = servicogrupo.grupos();
		
		session.removeAttribute("vendedor");
		session.removeAttribute("gestor");
		
		geragraficomixqtde();
		geragraficomixvl();
		
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
		
		if (produto == null){
			produtofiltro = "0";
			produtofiltro2 = "999999999";
			
		}else{
			produtofiltro = produto.getProdutoid().toString();
			produtofiltro2 = produto.getProdutoid().toString();
		}	
		
		if (grupo == null){
			grupofiltro = "0";
			grupofiltro2 = "999999";
			
		}else{
			grupofiltro = grupo.getIdgrupo().toString();
			grupofiltro2 = grupo.getIdgrupo().toString();
		}
		
		
		lista = servico.mixprodutos(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, produtofiltro, produtofiltro2, grupofiltro, grupofiltro2);
		
		geragraficomixqtde();
		geragraficomixvl();
	}
	
	public void geragraficomixqtde() {
		grafico_mixqtde = new LineChartModel();
		ChartData data = new ChartData();
		LineChartDataSet hbarDataSet = new LineChartDataSet();
		hbarDataSet.setLabel("Quantidade");
		
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		values.add(getQtde2018i());
		labels.add("2018");
		
		values.add(getQtde2019i());
		labels.add("2019");
		
		values.add(getQtde2020i());
		labels.add("2020");
		
		values.add(getQtde2021i());
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
	
	public void geragraficomixvl() {
		grafico_mixvl = new LineChartModel();
		ChartData data = new ChartData();
		LineChartDataSet hbarDataSet = new LineChartDataSet();
		hbarDataSet.setLabel("Valor");
		
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		values.add(getVl2018i());
		labels.add("2018");
		
		values.add(getVl2019i());
		labels.add("2019");
		
		values.add(getVl2020i());
		labels.add("2020");
		
		values.add(getVl2021i());
		labels.add("2021");
		
		hbarDataSet.setData(values);
		hbarDataSet.setFill(true);
		hbarDataSet.setBackgroundColor("rgba(255, 222, 173)");
		
		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		
		
		grafico_mixvl.setData(data);
		
		LineChartOptions options = new LineChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        grafico_mixvl.setOptions(options);
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
	
	public float getVl2018i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2018().floatValue();
		}
		return t;
	}
	public float getVl2019i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2019().floatValue();
		}
		return t;
	}
	public float getVl2020i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2020().floatValue();
		}
		return t;
	}
	public float getVl2021i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2021().floatValue();
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

	
	public Venda_Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Venda_Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Venda_Grupo> getListagrupos() {
		return listagrupos;
	}

	public void setListagrupos(List<Venda_Grupo> listagrupos) {
		this.listagrupos = listagrupos;
	}

	public String getProdutofiltro() {
		return produtofiltro;
	}

	public void setProdutofiltro(String produtofiltro) {
		this.produtofiltro = produtofiltro;
	}

	public String getProdutofiltro2() {
		return produtofiltro2;
	}

	public void setProdutofiltro2(String produtofiltro2) {
		this.produtofiltro2 = produtofiltro2;
	}

	public LineChartModel getGrafico_mixvl() {
		return grafico_mixvl;
	}

	public void setGrafico_mixvl(LineChartModel grafico_mixvl) {
		this.grafico_mixvl = grafico_mixvl;
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
	
	public void filtraporgrupo() {
		List<Produto> fprodutos = new ArrayList<Produto>();
		
		if(getGrupo() != null) {
			for(Produto p:produtos) {
				if(p.getGrupoid().equals(getGrupo().idgrupo)) {
					fprodutos.add(p);
				}
			}
		}
		
		if(fprodutos.size() > 0) {
			produtos.clear();
			produtos.addAll(fprodutos);
		}else {
			produtos = servicoproduto.produtos();
		}
	}
	
}
