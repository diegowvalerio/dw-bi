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

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
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
import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_VendaSubgrupo;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendagrupo;


@Named
@ViewScoped
public class BeanPainel_Diretor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes,dia;
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
	
	private int quantidadeDias;
	
	private float proporcional_mensal_faturamento;
	private float proporcional_mensal_pedido;
	private float proporcional_anual;
	
	private HorizontalBarChartModel grafico_pedidos_grupo_mes;
	private HorizontalBarChartModel grafico_pedidos_subgrupo_mes;
	
	@PostConstruct
	public void init() {
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		dia = data.substring(0,2);
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
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
		
		cria_grafico_venda_grupo_mensal();
		cria_grafico_venda_subgrupo_mensal();
		
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
		cria_grafico_venda_grupo_mensal();
		cria_grafico_venda_subgrupo_mensal();
		
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
			if (diretor01.getAtingido_mensal() != null) {
				proporcional_mensal_faturamento = (diretor01.getMeta_mensal_faturamento_p().floatValue()/ quantidadeDias) * Integer.parseInt(dia);
			}

			if (diretor01.getAtingido_mensal_pedido_p() != null) {
				proporcional_mensal_pedido = (diretor01.getMeta_mensal_pedidos_p().floatValue() / quantidadeDias)* Integer.parseInt(dia);
			}
			if (diretor01.getAtingido_anual() != null) {
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
		 System.out.println(v.getNomegrupo());
		 
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
	
	
}
