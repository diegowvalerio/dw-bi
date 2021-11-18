package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

import org.primefaces.component.colorpicker.ColorPicker;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.painel.Cliente_Ano;
import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.classe.painel.Qtde_Ano;
import br.com.dwbidiretor.classe.painel.Qtde_Mes;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.classe.painel.Vendedor_Ano;
import br.com.dwbidiretor.classe.painel.Vendedor_Mes;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Cliente_Ano;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Qtde_Ano;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Qtde_Mes;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_VendaSubgrupo;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendagrupo;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendedor_Ano;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendedor_Mes;


@Named
@ViewScoped
public class BeanPainel_Diretor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes,dia,uf;
	private Date  horaatual = new Date();
	
	private Diretor_01 diretor01 = new Diretor_01();
	private List<Diretor_01> lista = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor servico;
	@Inject
	private ServicoPainel_Diretor_Vendagrupo servicovendagrupo;
	private List<Venda_Grupo> lista_vendagrupo = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_VendaSubgrupo servicovendasubgrupo;
	private List<Venda_Subgrupo> lista_vendasubgrupo = new ArrayList<>();
	
	@Inject
	private ServicoPainel_Diretor_Vendedor_Ano servicoVendedor_Ano;
	private List<Vendedor_Ano> lista_vendVendedor_Ano = new ArrayList<>();
	private Vendedor_Ano vendedor_ano = new Vendedor_Ano();
	@Inject
	private ServicoPainel_Diretor_Vendedor_Mes servicoVendedor_Mes;
	private List<Vendedor_Mes> lista_vendVendedor_Mes = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_Cliente_Ano servicoCliente_Ano;
	private List<Cliente_Ano> lista_vendCliente_Ano = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_Qtde_Ano servicoQtde_Ano;
	private List<Qtde_Ano> lista_Qtde_Ano = new ArrayList<>();
	private Qtde_Ano qtde_ano = new Qtde_Ano();
	@Inject
	private ServicoPainel_Diretor_Qtde_Mes servicoQtde_Mes;
	private List<Qtde_Mes> lista_Qtde_mes = new ArrayList<>();
	
	
	private int quantidadeDias;
	
	private float proporcional_mensal_faturamento;
	private float proporcional_mensal_pedido;
	private float proporcional_anual;
	
	private HorizontalBarChartModel grafico_pedidos_grupo_mes;
	private HorizontalBarChartModel grafico_pedidos_subgrupo_mes;
	private BarChartModel grafico_vendedores_ano;
	private BarChartModel grafico_vendedores_mes;
	private BarChartModel grafico_clientes_ano;
	private BarChartModel grafico_qtde_ano;
	private BarChartModel grafico_qtde_mes;
	
	@PostConstruct
	public void init() {
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		dia = data.substring(0,2);
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		uf = "TD";
		
		Calendar datas = new GregorianCalendar();
		datas.set(Calendar.YEAR, Integer.parseInt(ano));
		datas.set(Calendar.MONTH, Integer.parseInt(mes));
		quantidadeDias = datas.getActualMaximum (Calendar.DAY_OF_MONTH);
		
		lista = servico.diretor_01(ano, mes);
		lista_vendagrupo = servicovendagrupo.venda_grupo(ano, mes);
		if(lista.size()>0) {
			diretor01 = lista.get(0);
			cacula_proporcional_mensal_pedido();
		}else {
			diretor01 = new Diretor_01();
		}
		
		lista_vendVendedor_Ano = servicoVendedor_Ano.vendedor_Ano(uf);
		lista_vendCliente_Ano = servicoCliente_Ano.cliente_Ano(uf);
		lista_Qtde_Ano = servicoQtde_Ano.qtde_Ano(uf);
		
		cria_grafico_venda_grupo_mensal();
		cria_grafico_venda_subgrupo_mensal();
		cria_grafico_vendedores_ano();
		cria_grafico_vendedores_mes();
		cria_grafico_clientes_ano();
		cria_grafico_qtde_ano();
		cria_grafico_qtde_mes();
		
	}
	
	public void filtrar(){
		lista = servico.diretor_01(ano, mes);
		lista_vendagrupo = servicovendagrupo.venda_grupo(ano, mes);
		lista_vendasubgrupo.clear();
		if(lista.size()>0) {
			diretor01 = lista.get(0);
			cacula_proporcional_mensal_pedido();
		}else {
			diretor01 = new Diretor_01();
		}
		
		lista_vendVendedor_Ano = servicoVendedor_Ano.vendedor_Ano(uf);
		lista_vendVendedor_Mes.clear();
		lista_vendCliente_Ano = servicoCliente_Ano.cliente_Ano(uf);
		lista_Qtde_Ano = servicoQtde_Ano.qtde_Ano(uf);
		lista_Qtde_mes.clear();
		
		cria_grafico_venda_grupo_mensal();
		cria_grafico_venda_subgrupo_mensal();
		cria_grafico_vendedores_ano();
		cria_grafico_vendedores_mes();
		cria_grafico_clientes_ano();
		cria_grafico_qtde_ano();
		cria_grafico_qtde_mes();
		
	}
	
	public void cacula_proporcional_mensal_pedido() {
		Date h = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(h);
		
		String mesh = data.substring(3,5);
		String anoh = data.substring(6,10);
		
		Calendar datas = new GregorianCalendar();
		datas.set(Calendar.YEAR, Integer.parseInt(ano));
		datas.set(Calendar.MONTH, Integer.parseInt(mes));
		int diaanos = datas.get(Calendar.DAY_OF_YEAR);
		
		if(!mes.equals(mesh) || !ano.equals(anoh)) {
			proporcional_mensal_faturamento = 0;
		    proporcional_mensal_pedido = 0;
			proporcional_anual = 0;
			
		}else {
			if (diretor01.getAtingido_mensal() != null && diretor01.getMeta_mensal_faturamento_p() != null) {
				proporcional_mensal_faturamento = (diretor01.getMeta_mensal_faturamento_p().floatValue()/ quantidadeDias) * Integer.parseInt(dia);
			}

			if (diretor01.getAtingido_mensal_pedido_p() != null && diretor01.getMeta_mensal_pedidos_p() != null) {
				proporcional_mensal_pedido = (diretor01.getMeta_mensal_pedidos_p().floatValue() / quantidadeDias)* Integer.parseInt(dia);
			}
			if (diretor01.getAtingido_anual() != null && diretor01.getMeta_anual() != null) {
				proporcional_anual = (diretor01.getMeta_anual().floatValue() / 365)* diaanos;
			}
		}
		
	}
	
	public void cria_grafico_venda_grupo_mensal() {
	grafico_pedidos_grupo_mes = new HorizontalBarChartModel();
    ChartData data = new ChartData();

    HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
    hbarDataSet.setLabel("Pedidos por Grupo");

    List<Number> values = new ArrayList<>();
    List<String> bgColor = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    
    
    Collections.sort(lista_vendagrupo,Collections.reverseOrder(Comparator.comparing(Venda_Grupo::getValorgrupo)));
    for(Venda_Grupo v:lista_vendagrupo) {
    	values.add(v.getValorgrupo());
    	bgColor.add("rgba(0, 206, 209)");
    	labels.add(v.getNomegrupo()+": "+v.getValorgrupo());
    }
    hbarDataSet.setData(values);
    hbarDataSet.setBackgroundColor(bgColor);

    data.addChartDataSet(hbarDataSet);
    data.setLabels(labels);
    grafico_pedidos_grupo_mes.setData(data);

    //Options
    BarChartOptions options = new BarChartOptions();
    CartesianScales cScales = new CartesianScales();
    CartesianLinearAxes linearAxes = new CartesianLinearAxes();
    linearAxes.setOffset(true);
    CartesianLinearTicks ticks = new CartesianLinearTicks();
    ticks.setBeginAtZero(true);
    linearAxes.setTicks(ticks);
    cScales.addXAxesData(linearAxes);
    options.setScales(cScales);
    
    Title title = new Title();
    title.setDisplay(true);
    title.setText(mes+"/"+ano);
    options.setTitle(title);

    grafico_pedidos_grupo_mes.setOptions(options);
	}
	
	 public void itemSelect(ItemSelectEvent event) {
		 Venda_Grupo v = lista_vendagrupo.get(event.getItemIndex());
		
		 lista_vendasubgrupo = servicovendasubgrupo.venda_subgrupo(ano, mes, v.getIdgrupo().toString());
		 if(lista_vendasubgrupo.size()>0) {
			 cria_grafico_venda_subgrupo_mensal();
		 }
      
	 }
	 
	 public void cria_grafico_venda_subgrupo_mensal() {
		 	
			grafico_pedidos_subgrupo_mes = new HorizontalBarChartModel();
		    ChartData data = new ChartData();

		    HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
		    hbarDataSet.setLabel("Pedidos por SubGrupo");

		    List<Number> values = new ArrayList<>();
		    List<String> bgColor = new ArrayList<>();
		    List<String> labels = new ArrayList<>();
		    
		    if(lista_vendasubgrupo.size()>0) {
		    Collections.sort(lista_vendasubgrupo,Collections.reverseOrder(Comparator.comparing(Venda_Subgrupo::getValorsubgrupo)));
		    for(Venda_Subgrupo v:lista_vendasubgrupo) {
		    	values.add(v.getValorsubgrupo());
		    	bgColor.add("rgba(135, 206, 250)");
		    	labels.add(v.getNomesubgrupo()+": "+v.getValorsubgrupo());
		    }
		    }else {
		    	values.add(0);
		    	bgColor.add("rgba(135, 206, 250)");
		    	labels.add("Selecione um Grupo");
		    }
		    hbarDataSet.setData(values);
		    hbarDataSet.setBackgroundColor(bgColor);

		    data.addChartDataSet(hbarDataSet);
		    data.setLabels(labels);
		    grafico_pedidos_subgrupo_mes.setData(data);

		    //Options
		    BarChartOptions options = new BarChartOptions();
		    CartesianScales cScales = new CartesianScales();
		    CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		    linearAxes.setOffset(true);
		    CartesianLinearTicks ticks = new CartesianLinearTicks();
		    ticks.setBeginAtZero(true);
		    linearAxes.setTicks(ticks);
		    cScales.addXAxesData(linearAxes);
		    options.setScales(cScales);
		    
		Title title = new Title();
		title.setDisplay(true);
		title.setText(mes+"/"+ano);
		options.setTitle(title);

		grafico_pedidos_subgrupo_mes.setOptions(options);
	}	 
	 
	public void cria_grafico_vendedores_ano() {
		grafico_vendedores_ano = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		if(uf.equals("TD")) {
			hbarDataSet.setLabel("N° Vendedores Faturado");
		}else {
			hbarDataSet.setLabel("N° Vendedores Faturado / UF: "+uf);
		}
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (Vendedor_Ano v : lista_vendVendedor_Ano) {
			values.add(v.getVendedores());
			bgColor.add("rgba(30, 144, 255)");
			labels.add(v.getAno());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		grafico_vendedores_ano.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

		grafico_vendedores_ano.setOptions(options);
	}
	
	public void itemSelect3(ItemSelectEvent event) {
		lista_vendVendedor_Mes.clear();
		vendedor_ano = lista_vendVendedor_Ano.get(event.getItemIndex());
		 
		lista_vendVendedor_Mes = servicoVendedor_Mes.vendedor_Mes(uf, vendedor_ano.getAno().toString());
		 if(lista_vendVendedor_Mes.size()>0) {
			 cria_grafico_vendedores_mes();
		 }
    
	 }
	
	public void cria_grafico_vendedores_mes() {
		grafico_vendedores_mes = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		if(lista_vendVendedor_Mes.size()>0) {
			if(uf.equals("TD")) {
				hbarDataSet.setLabel("N° Vendedores Faturado Mês em "+vendedor_ano.getAno().toString());
			}else {
				hbarDataSet.setLabel("N° Vendedores Faturado Mês em "+vendedor_ano.getAno().toString()+"/ UF: "+uf);
			}
			for (Vendedor_Mes v : lista_vendVendedor_Mes) {
				values.add(v.getVendedores());
				bgColor.add("rgba(30, 144, 255)");
				labels.add(v.getMes());
			}
		}else {
			hbarDataSet.setLabel("N° Vendedores Faturado Mês");
			values.add(0);
			bgColor.add("rgba(30, 144, 255)");
			labels.add("Selecione o Ano");
	    }
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		grafico_vendedores_mes.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        grafico_vendedores_mes.setOptions(options);
	}

	public void cria_grafico_clientes_ano() {
		grafico_clientes_ano = new BarChartModel();
		
		
		ChartData data = new ChartData();

		BarChartDataSet hbarDataSet = new BarChartDataSet();
		if(uf.equals("TD")) {
			hbarDataSet.setLabel("N° Clientes Faturado");
		}else {
			hbarDataSet.setLabel("N° Clientes Faturado / UF: "+uf);
		}
		
		

		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (Cliente_Ano v : lista_vendCliente_Ano) {
			values.add(v.getClientes());
			bgColor.add("rgba(0, 206, 209)");
			labels.add(v.getAno());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		grafico_clientes_ano.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        grafico_clientes_ano.setOptions(options);
       // grafico_clientes_ano.setExtender("chartExtender");
	}
	
	public void cria_grafico_qtde_ano() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		grafico_qtde_ano = new BarChartModel();
		
		
		ChartData data = new ChartData();

		BarChartDataSet hbarDataSet = new BarChartDataSet();
		if(uf.equals("TD")) {
			hbarDataSet.setLabel("N° Peças Faturado");
		}else {
			hbarDataSet.setLabel("N° Peças Faturado / UF: "+uf);
		}
		
		

		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		for (Qtde_Ano v : lista_Qtde_Ano) {
			values.add(v.getQtde());
			bgColor.add("rgba(70, 130, 180)");
			labels.add(v.getAno());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		grafico_qtde_ano.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        grafico_qtde_ano.setOptions(options);
        grafico_qtde_ano.setExtender("my_ext");
	}
	
	public void itemSelect2(ItemSelectEvent event) {
		 lista_Qtde_mes.clear();
		 qtde_ano = lista_Qtde_Ano.get(event.getItemIndex());
		 
		 lista_Qtde_mes = servicoQtde_Mes.qtde_Mes(uf, qtde_ano.getAno().toString());
		 if(lista_Qtde_mes.size()>0) {
			 cria_grafico_qtde_mes();
		 }
     
	 }
	
	public void cria_grafico_qtde_mes() {
		DecimalFormat df = new DecimalFormat("#,###.00");
		grafico_qtde_mes = new BarChartModel();
		
		
		ChartData data = new ChartData();

		BarChartDataSet hbarDataSet = new BarChartDataSet();

		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		if(lista_Qtde_mes.size()>0) {
			if(uf.equals("TD")) {
				hbarDataSet.setLabel("N° Peças Faturado Mês em "+qtde_ano.getAno().toString());
			}else {
				hbarDataSet.setLabel("N° Peças Faturado Mês em "+qtde_ano.getAno().toString()+"/ UF: "+uf);
			}
			
			for (Qtde_Mes v : lista_Qtde_mes) {
				values.add(v.getQtde());
				bgColor.add("rgba(70, 130, 180)");
				labels.add(v.getMes());
			}
		} else {
			hbarDataSet.setLabel("N° Peças Faturado Mês");
			values.add(0);
			bgColor.add("rgba(70, 130, 180)");
			labels.add("Selecione o Ano");
	    }
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		grafico_qtde_mes.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/

        grafico_qtde_mes.setOptions(options);
	}
	

	public List<Cliente_Ano> getLista_vendCliente_Ano() {
		return lista_vendCliente_Ano;
	}

	public void setLista_vendCliente_Ano(List<Cliente_Ano> lista_vendCliente_Ano) {
		this.lista_vendCliente_Ano = lista_vendCliente_Ano;
	}

	public BarChartModel getGrafico_clientes_ano() {
		return grafico_clientes_ano;
	}

	public void setGrafico_clientes_ano(BarChartModel grafico_clientes_ano) {
		this.grafico_clientes_ano = grafico_clientes_ano;
	}

	public HorizontalBarChartModel getGrafico_pedidos_subgrupo_mes() {
		return grafico_pedidos_subgrupo_mes;
	}

	public void setGrafico_pedidos_subgrupo_mes(HorizontalBarChartModel grafico_pedidos_subgrupo_mes) {
		this.grafico_pedidos_subgrupo_mes = grafico_pedidos_subgrupo_mes;
	}

	public List<Venda_Grupo> getLista_vendagrupo() {
		return lista_vendagrupo;
	}

	public void setLista_vendagrupo(List<Venda_Grupo> lista_vendagrupo) {
		this.lista_vendagrupo = lista_vendagrupo;
	}

	public List<Venda_Subgrupo> getLista_vendasubgrupo() {
		return lista_vendasubgrupo;
	}

	public void setLista_vendasubgrupo(List<Venda_Subgrupo> lista_vendasubgrupo) {
		this.lista_vendasubgrupo = lista_vendasubgrupo;
	}

	public HorizontalBarChartModel getGrafico_pedidos_grupo_mes() {
		return grafico_pedidos_grupo_mes;
	}

	public void setGrafico_pedidos_grupo_mes(HorizontalBarChartModel grafico_pedidos_grupo_mes) {
		this.grafico_pedidos_grupo_mes = grafico_pedidos_grupo_mes;
	}

	public float getProporcional_anual() {
		return proporcional_anual;
	}

	public void setProporcional_anual(float proporcional_anual) {
		this.proporcional_anual = proporcional_anual;
	}

	public float getProporcional_mensal_faturamento() {
		return proporcional_mensal_faturamento;
	}

	public void setProporcional_mensal_faturamento(float proporcional_mensal_faturamento) {
		this.proporcional_mensal_faturamento = proporcional_mensal_faturamento;
	}

	public float getProporcional_mensal_pedido() {
		return proporcional_mensal_pedido;
	}

	public void setProporcional_mensal_pedido(float proporcional_mensal_pedido) {
		this.proporcional_mensal_pedido = proporcional_mensal_pedido;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public int getQuantidadeDias() {
		return quantidadeDias;
	}

	public void setQuantidadeDias(int quantidadeDias) {
		this.quantidadeDias = quantidadeDias;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Date getHoraatual() {
		return horaatual;
	}

	public void setHoraatual(Date horaatual) {
		this.horaatual = horaatual;
	}

	public Diretor_01 getDiretor01() {
		return diretor01;
	}

	public void setDiretor01(Diretor_01 diretor01) {
		this.diretor01 = diretor01;
	}

	public List<Diretor_01> getLista() {
		return lista;
	}

	public void setLista(List<Diretor_01> lista) {
		this.lista = lista;
	}

	public List<Vendedor_Ano> getLista_vendVendedor_Ano() {
		return lista_vendVendedor_Ano;
	}

	public void setLista_vendVendedor_Ano(List<Vendedor_Ano> lista_vendVendedor_Ano) {
		this.lista_vendVendedor_Ano = lista_vendVendedor_Ano;
	}

	public BarChartModel getGrafico_vendedores_ano() {
		return grafico_vendedores_ano;
	}

	public void setGrafico_vendedores_ano(BarChartModel grafico_vendedores_ano) {
		this.grafico_vendedores_ano = grafico_vendedores_ano;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public List<Qtde_Ano> getLista_Qtde_Ano() {
		return lista_Qtde_Ano;
	}

	public void setLista_Qtde_Ano(List<Qtde_Ano> lista_Qtde_Ano) {
		this.lista_Qtde_Ano = lista_Qtde_Ano;
	}

	public BarChartModel getGrafico_qtde_ano() {
		return grafico_qtde_ano;
	}

	public void setGrafico_qtde_ano(BarChartModel grafico_qtde_ano) {
		this.grafico_qtde_ano = grafico_qtde_ano;
	}

	public List<Qtde_Mes> getLista_Qtde_mes() {
		return lista_Qtde_mes;
	}

	public void setLista_Qtde_mes(List<Qtde_Mes> lista_Qtde_mes) {
		this.lista_Qtde_mes = lista_Qtde_mes;
	}

	public BarChartModel getGrafico_qtde_mes() {
		return grafico_qtde_mes;
	}

	public void setGrafico_qtde_mes(BarChartModel grafico_qtde_mes) {
		this.grafico_qtde_mes = grafico_qtde_mes;
	}

	public List<Vendedor_Mes> getLista_vendVendedor_Mes() {
		return lista_vendVendedor_Mes;
	}

	public void setLista_vendVendedor_Mes(List<Vendedor_Mes> lista_vendVendedor_Mes) {
		this.lista_vendVendedor_Mes = lista_vendVendedor_Mes;
	}

	public Qtde_Ano getQtde_ano() {
		return qtde_ano;
	}

	public void setQtde_ano(Qtde_Ano qtde_ano) {
		this.qtde_ano = qtde_ano;
	}

	public BarChartModel getGrafico_vendedores_mes() {
		return grafico_vendedores_mes;
	}

	public void setGrafico_vendedores_mes(BarChartModel grafico_vendedores_mes) {
		this.grafico_vendedores_mes = grafico_vendedores_mes;
	}
	
	
}
