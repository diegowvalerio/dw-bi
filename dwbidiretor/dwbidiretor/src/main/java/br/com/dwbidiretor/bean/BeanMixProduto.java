package br.com.dwbidiretor.bean;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMixProduto;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.ServicoVendedor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_VendaSubgrupo;
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
	
	//filtro Subgrupo
	private Venda_Subgrupo subgrupo = new Venda_Subgrupo();
	private List<Venda_Subgrupo> listaSubgrupos = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_VendaSubgrupo servicoSubgrupo;
	
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();

	private String vendedorlogado;
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	
	private String produtofiltro;
	private String produtofiltro2;
	private String grupofiltro;
	private String grupofiltro2;
	private String subgrupofiltro;
	private String subgrupofiltro2;
	private String clientefiltrado;
	private String clientefiltrado2;

	private LineChartModel grafico_mixqtde;
	private LineChartModel grafico_mixvl;
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	@PostConstruct
	public void init() {
		GregorianCalendar gc= new GregorianCalendar();
		gc.set(2000, 00, 01);
		data_grafico.setTime(gc.getTimeInMillis());
		
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
		listaSubgrupos = servicoSubgrupo.subgrupos();
		listacliente = servicocliente.clientes();
		
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
		
		if (subgrupo == null){
			subgrupofiltro = "0";
			subgrupofiltro2 = "999999";
			
		}else{
			subgrupofiltro = subgrupo.getIdsubgrupo().toString();
			subgrupofiltro2 = subgrupo.getIdsubgrupo().toString();
		}
		
		if (cliente == null){
			clientefiltrado = "0";
			clientefiltrado2 = "99999999";
			
		}else{
			clientefiltrado = cliente.getCodigocliente().toString();
			clientefiltrado2 = cliente.getCodigocliente().toString();
		}
		
		
		lista = servico.mixprodutos(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, produtofiltro, produtofiltro2, grupofiltro, grupofiltro2, subgrupofiltro, subgrupofiltro2, clientefiltrado, clientefiltrado2,data_grafico, data_grafico2);
		
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
		
		values.add(getQtde2022i());
		labels.add("2022");
		
		values.add(getQtde2023i());
		labels.add("2023");
		
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
		
		values.add(getVl2022i());
		labels.add("2022");
		
		values.add(getVl2023i());
		labels.add("2023");
		
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
	public int getQtde2022i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2022().intValue();
		}
		return t;
	}
	public int getQtde2023i(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2023().intValue();
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
	public float getVl2022i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2022().floatValue();
		}
		return t;
	}
	public float getVl2023i(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2023().floatValue();
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
	public String getQtde2022(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2022().intValue();
		}
		return NumberFormat.getNumberInstance().format(t);
	}
	public String getQtde2023(){
		int t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getQtde2023().intValue();
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
	public String getVl2022(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2022().floatValue();
		}
		return NumberFormat.getCurrencyInstance().format(t);
	}
	public String getVl2023(){
		float t = 0 ;
		for(MixProduto m:lista) {
			t = t + m.getVl2023().floatValue();
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

	public Venda_Subgrupo getSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(Venda_Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}

	public List<Venda_Subgrupo> getListaSubgrupos() {
		return listaSubgrupos;
	}

	public void setListaSubgrupos(List<Venda_Subgrupo> listaSubgrupos) {
		this.listaSubgrupos = listaSubgrupos;
	}

	public String getGrupofiltro() {
		return grupofiltro;
	}

	public void setGrupofiltro(String grupofiltro) {
		this.grupofiltro = grupofiltro;
	}

	public String getGrupofiltro2() {
		return grupofiltro2;
	}

	public void setGrupofiltro2(String grupofiltro2) {
		this.grupofiltro2 = grupofiltro2;
	}

	public String getSubgrupofiltro() {
		return subgrupofiltro;
	}

	public void setSubgrupofiltro(String subgrupofiltro) {
		this.subgrupofiltro = subgrupofiltro;
	}

	public String getSubgrupofiltro2() {
		return subgrupofiltro2;
	}

	public void setSubgrupofiltro2(String subgrupofiltro2) {
		this.subgrupofiltro2 = subgrupofiltro2;
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
	
	public void filtraporproduto() {
		if(getProduto() != null) {
			for(Venda_Grupo sub: listagrupos) {
				if(getProduto().getGrupoid().equals(sub.getIdgrupo())) {
					grupo = sub;
				}
			}
			for(Venda_Subgrupo sub: listaSubgrupos) {
				if(getProduto().getSubgrupoid().equals(sub.getIdsubgrupo())) {
					subgrupo = sub;
				}
			}
		}
	}
	
	public void filtraporsubgrupo() {
		
		List<Produto> fprodutos = new ArrayList<Produto>();
		List<Venda_Grupo> fsubs = new ArrayList<Venda_Grupo>();
		listagrupos = servicogrupo.grupos();
		
		if(getSubgrupo() != null) {
			for(Produto p:produtos) {
				if(p.getGrupoid().equals(getSubgrupo().idgrupo)) {
					fprodutos.add(p);
				}
			}
			
			for(Venda_Grupo sub: listagrupos) {
				if(sub.getIdgrupo().equals(getSubgrupo().idgrupo)) {
					fsubs.add(sub);
				}
			}
		}
		
		if(fprodutos.size() > 0) {
			produtos.clear();
			produtos.addAll(fprodutos);
		}else {
			produtos = servicoproduto.produtos();
		}
		
		if(fsubs.size() > 0) {
			listagrupos.clear();
			listagrupos.addAll(fsubs);
			grupo = listagrupos.get(0);
		}
		
	}
	
	public void filtraporgrupo() {
		
		List<Produto> fprodutos = new ArrayList<Produto>();
		List<Venda_Subgrupo> fsubs = new ArrayList<Venda_Subgrupo>();
		listaSubgrupos = servicoSubgrupo.subgrupos();
		
		if(getGrupo() != null) {
			for(Produto p:produtos) {
				if(p.getGrupoid().equals(getGrupo().idgrupo)) {
					fprodutos.add(p);
				}
			}
			
			for(Venda_Subgrupo sub: listaSubgrupos) {
				if(sub.getIdgrupo().equals(getGrupo().idgrupo)) {
					fsubs.add(sub);
				}
			}
		}
		
		if(fprodutos.size() > 0) {
			produtos.clear();
			produtos.addAll(fprodutos);
		}else {
			produtos = servicoproduto.produtos();
		}
		
		if(fsubs.size() > 0) {
			listaSubgrupos.clear();
			listaSubgrupos.addAll(fsubs);
		}
		
	}
	
}
