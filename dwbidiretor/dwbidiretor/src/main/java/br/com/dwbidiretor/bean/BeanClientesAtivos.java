package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.omnifaces.el.ELContextWrapper;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.ClientesAtivos;
import br.com.dwbidiretor.classe.NovasVendas_Cliente;
import br.com.dwbidiretor.classe.Orcamentos;
import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.servico.ServicoClientesAtivos;
import br.com.dwbidiretor.servico.ServicoNovasVendas_Clientes;
import br.com.dwbidiretor.servico.ServicoOrcamentos;

@Named
@ViewScoped
public class BeanClientesAtivos implements Serializable {
	private static final long serialVersionUID = 1L;

	private ClientesAtivos clientesAtivosAno = new ClientesAtivos();
	private NovasVendas_Cliente novasvendas_cliente = new NovasVendas_Cliente();
	private Orcamentos orcamentos = new Orcamentos();

	@Inject
	private ServicoClientesAtivos servico;
	private List<ClientesAtivos> lista = new ArrayList<>();
	private List<ClientesAtivos> lista90 = new ArrayList<>();
	
	@Inject
	private ServicoNovasVendas_Clientes servico2;
	private List<NovasVendas_Cliente> lista2 = new ArrayList<>();
	
	@Inject
	private ServicoOrcamentos servico3;
	private List<Orcamentos> lista3 = new ArrayList<>();

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();

	private BarChartModel ativos;
	private BarChartModel ativos90;
	private BarChartModel novos;
	private BarChartModel orca;
	
	private String ano;

	@PostConstruct
	public void init() {
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(data_grafico);
		ano = ""+cal.get(Calendar.YEAR);
		lista = servico.clientesativos(ano);
		lista90 = servico.clientesativos2(ano);
		lista2 = servico2.novasvendas_clientes(ano);
		lista3 = servico3.orcamentos(ano);
		
		cria_ativos();
		cria_ativos90();
		cria_novos();
		cria_orca();
	}
	
	public void filtrar(){
		
		lista = servico.clientesativos(ano);
		lista90 = servico.clientesativos2(ano);
		lista2 = servico2.novasvendas_clientes(ano);
		lista3 = servico3.orcamentos(ano);
		
		cria_ativos();
		cria_ativos90();
		cria_novos();
		cria_orca();
	}
	
	public void cria_orca() {		
		orca = new BarChartModel();
		ChartData data = new ChartData();
		
		//vendas
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Cancelados");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (Orcamentos v : lista3) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getCancelados());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(238, 106, 80)");
			labels.add(v.getNmes());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		//vendas
		
		//cadastro
		BarChartDataSet hbarDataSet2 = new BarChartDataSet();
		hbarDataSet2.setLabel("Abertos");
		values = new ArrayList<>();
		bgColor = new ArrayList<>();
		labels = new ArrayList<>();
		
		for (Orcamentos v : lista3) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getAbertos());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(202, 255, 112)");
			labels.add(v.getNmes());
		}
		hbarDataSet2.setData(values);
		hbarDataSet2.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet2);
		data.setLabels(labels);
		//cadastro
		
		orca.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        
        CartesianLinearAxes linearx = new CartesianLinearAxes();
        CartesianLinearTicks ticksx = new CartesianLinearTicks();
        ticksx.setMaxRotation(60);
        ticksx.setMinRotation(60);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        orca.setOptions(options);
	}	
	
	public void cria_novos() {		
		novos = new BarChartModel();
		ChartData data = new ChartData();
		
		//vendas
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Compra");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (NovasVendas_Cliente v : lista2) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getNovosvenda());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(30, 144, 255)");
			labels.add(v.getNmes());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		//vendas
		
		//cadastro
		BarChartDataSet hbarDataSet2 = new BarChartDataSet();
		hbarDataSet2.setLabel("Cadastro");
		values = new ArrayList<>();
		bgColor = new ArrayList<>();
		labels = new ArrayList<>();
		
		for (NovasVendas_Cliente v : lista2) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getNovoscadastro());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(102 , 205 , 170)");
			labels.add(v.getNmes());
		}
		hbarDataSet2.setData(values);
		hbarDataSet2.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet2);
		data.setLabels(labels);
		//cadastro
		
		novos.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        
        CartesianLinearAxes linearx = new CartesianLinearAxes();
        CartesianLinearTicks ticksx = new CartesianLinearTicks();
        ticksx.setMaxRotation(60);
        ticksx.setMinRotation(60);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        novos.setOptions(options);
	}
	
	public void cria_ativos() {		
		ativos = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Ativos");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		
		//Collections.sort(lista,Collections.reverseOrder(Comparator.comparing(Perca::getQuantidade)));
		for (ClientesAtivos v : lista) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getQtde());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(28 ,134 ,238)");
			labels.add(v.getNmes()+": "+result);
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);

		ativos.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        
        CartesianLinearAxes linearx = new CartesianLinearAxes();
        CartesianLinearTicks ticksx = new CartesianLinearTicks();
        ticksx.setMaxRotation(60);
        ticksx.setMinRotation(60);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        ativos.setOptions(options);
	}

	public void cria_ativos90() {		
		ativos90 = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Ativos");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		
		//Collections.sort(lista,Collections.reverseOrder(Comparator.comparing(Perca::getQuantidade)));
		for (ClientesAtivos v : lista90) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getQtde());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(72, 209, 204)");
			labels.add(v.getNmes()+": "+result);
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);

		ativos90.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        
        CartesianLinearAxes linearx = new CartesianLinearAxes();
        CartesianLinearTicks ticksx = new CartesianLinearTicks();
        ticksx.setMaxRotation(60);
        ticksx.setMinRotation(60);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        ativos90.setOptions(options);
	}	

	public List<ClientesAtivos> getLista90() {
		return lista90;
	}

	public void setLista90(List<ClientesAtivos> lista90) {
		this.lista90 = lista90;
	}

	public BarChartModel getAtivos90() {
		return ativos90;
	}

	public void setAtivos90(BarChartModel ativos90) {
		this.ativos90 = ativos90;
	}

	public Orcamentos getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(Orcamentos orcamentos) {
		this.orcamentos = orcamentos;
	}

	public List<Orcamentos> getLista3() {
		return lista3;
	}

	public void setLista3(List<Orcamentos> lista3) {
		this.lista3 = lista3;
	}

	public BarChartModel getOrca() {
		return orca;
	}

	public void setOrca(BarChartModel orca) {
		this.orca = orca;
	}

	public BarChartModel getNovos() {
		return novos;
	}

	public void setNovos(BarChartModel novos) {
		this.novos = novos;
	}
	public NovasVendas_Cliente getNovasvendas_cliente() {
		return novasvendas_cliente;
	}

	public void setNovasvendas_cliente(NovasVendas_Cliente novasvendas_cliente) {
		this.novasvendas_cliente = novasvendas_cliente;
	}

	public List<NovasVendas_Cliente> getLista2() {
		return lista2;
	}

	public void setLista2(List<NovasVendas_Cliente> lista2) {
		this.lista2 = lista2;
	}

	public BarChartModel getAtivos() {
		return ativos;
	}

	public void setAtivos(BarChartModel ativos) {
		this.ativos = ativos;
	}

	public ClientesAtivos getClientesAtivosAno() {
		return clientesAtivosAno;
	}

	public void setClientesAtivosAno(ClientesAtivos clientesAtivosAno) {
		this.clientesAtivosAno = clientesAtivosAno;
	}

	public List<ClientesAtivos> getLista() {
		return lista;
	}

	public void setLista(List<ClientesAtivos> lista) {
		this.lista = lista;
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
