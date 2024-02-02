package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;

import br.com.dwbidiretor.classe.P1_FaturadoDia;
import br.com.dwbidiretor.classe.P1_MetaFaturado;
import br.com.dwbidiretor.servico.ServicoP1_FaturadoDia;
import br.com.dwbidiretor.servico.ServicoP1_MetaFaturado;


@Named
@ViewScoped
public class BeanP1_Painel implements Serializable {
	private static final long serialVersionUID = 1L;

	private P1_MetaFaturado metavenda = new P1_MetaFaturado();
	@Inject
	private ServicoP1_MetaFaturado servico;
	private List<P1_MetaFaturado> lista = new ArrayList<>();
	private List<P1_MetaFaturado> listapedido = new ArrayList<>();
	
	private P1_FaturadoDia vendadia = new P1_FaturadoDia();
	@Inject
	private ServicoP1_FaturadoDia servico2;
	private List<P1_FaturadoDia> lista2 = new ArrayList<>();
	private List<P1_FaturadoDia> lista2pedido = new ArrayList<>();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private BarChartModel faturadometa;
	private BarChartModel faturadodia;
	
	private Date data_grafico3 = new Date();
	private Date data_grafico4 = new Date();
	
	private BarChartModel pedidometa;
	private BarChartModel pedidodia;

	@PostConstruct
	public void init() {
	
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		

		lista = servico.p1metafaturado(data_grafico, data_grafico2);
		lista2= servico2.p1faturadodia(data_grafico, data_grafico2);
		
		cria_faturadometa();
		ajustalistadia();
		
			

		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico3 =c.getTime();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico4 =c.getTime();
		
		listapedido = servico.p1metapedido(data_grafico3, data_grafico4);
		lista2pedido= servico2.p1pedidodia(data_grafico3, data_grafico4);
		cria_pedidometa();
		ajustalistadiapedido();
		
	}
	
	public void filtrar(){	
		
		lista = servico.p1metafaturado(data_grafico, data_grafico2);
		lista2= servico2.p1faturadodia(data_grafico, data_grafico2);
		
		cria_faturadometa();
		ajustalistadia();
		
		listapedido = servico.p1metapedido(data_grafico3, data_grafico4);
		lista2pedido= servico2.p1pedidodia(data_grafico3, data_grafico4);
		cria_pedidometa();
		ajustalistadiapedido();
		
	}
	
	public List<Date> getRangeDate(String dataInicial, String dataFinal) {

        try {
            //cria lista
            List<Date> dates = new ArrayList();
            //cria formatador de datas
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            //adiciona a primeira data
            dates.add(sdf.parse(dataInicial));
            //cria objetos calendars
            Calendar initialDate = Calendar.getInstance();
            Calendar finalDate = Calendar.getInstance();
            //seta data inicial e final nos calendars setando o dia para 1
            initialDate.setTime(dates.get(0));
            finalDate.setTime(sdf.parse(dataFinal));

            while (initialDate.before(finalDate)) {
                //adiciona um mês a data
                initialDate.add(Calendar.DATE, 1);
                //adiciona na lista
                dates.add(new Date(initialDate.toInstant().toEpochMilli()));
            }
            return dates;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
	private void ajustalistadiapedido() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data_grafico3);
		String dataFormatada2 = formato.format(data_grafico4);
		
		List<Date> datas = getRangeDate(dataFormatada, dataFormatada2);
				
		for(Date d :datas) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			
			int h = 1;
			for(P1_FaturadoDia dia:lista2pedido) {
				if(Integer.parseInt(dia.getAno()) == calendar.get(Calendar.YEAR) && Integer.parseInt(dia.getMes()) == (calendar.get(Calendar.MONTH)+1) && Integer.parseInt(dia.getDia()) == calendar.get(Calendar.DAY_OF_MONTH)) {
					h =0;
				}
			}
			if(h == 1) {
				P1_FaturadoDia dia = new P1_FaturadoDia();

				dia.setAno(""+calendar.get(Calendar.YEAR));
				if ((calendar.get(Calendar.MONTH)+1) < 10) {
					dia.setMes("0"+(calendar.get(Calendar.MONTH)+1));
				}else {
					dia.setMes(""+(calendar.get(Calendar.MONTH)+1));
				}
				if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
					dia.setDia("0"+calendar.get(Calendar.DAY_OF_MONTH));
				}else {
					dia.setDia(""+calendar.get(Calendar.DAY_OF_MONTH));
				}
				
				dia.setFaturado(new BigDecimal(0));
				lista2pedido.add(dia);
			}
		}
		
		lista2pedido.sort(Comparator.comparing(P1_FaturadoDia::getMes).thenComparing(P1_FaturadoDia::getDia));
		cria_pedidodia();
		
	}
	
	private void cria_pedidodia() {		
		pedidodia = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Pedido x Dia");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (P1_FaturadoDia v : lista2pedido) {
			values.add(v.getFaturado());
			bgColor.add("rgba(135, 206, 250)");
			labels.add(getDayOfWeek(v.getDia()+"/"+v.getMes()+"/"+v.getAno())+"-"+v.getDia()+"/"+v.getMes());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		pedidodia.setData(data);

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
        ticksx.setMaxRotation(90);
        ticksx.setMinRotation(90);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        pedidodia.setOptions(options);
		
	}
	
	private void ajustalistadia() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data_grafico);
		String dataFormatada2 = formato.format(data_grafico2);
		
		List<Date> datas = getRangeDate(dataFormatada, dataFormatada2);
				
		for(Date d :datas) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			
			int h = 1;
			for(P1_FaturadoDia dia:lista2) {
				if(Integer.parseInt(dia.getAno()) == calendar.get(Calendar.YEAR) && Integer.parseInt(dia.getMes()) == (calendar.get(Calendar.MONTH)+1) && Integer.parseInt(dia.getDia()) == calendar.get(Calendar.DAY_OF_MONTH)) {
					h =0;
				}
			}
			if(h == 1) {
				P1_FaturadoDia dia = new P1_FaturadoDia();

				dia.setAno(""+calendar.get(Calendar.YEAR));
				if ((calendar.get(Calendar.MONTH)+1) < 10) {
					dia.setMes("0"+(calendar.get(Calendar.MONTH)+1));
				}else {
					dia.setMes(""+(calendar.get(Calendar.MONTH)+1));
				}
				if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
					dia.setDia("0"+calendar.get(Calendar.DAY_OF_MONTH));
				}else {
					dia.setDia(""+calendar.get(Calendar.DAY_OF_MONTH));
				}
				
				dia.setFaturado(new BigDecimal(0));
				lista2.add(dia);
			}
		}
		
		lista2.sort(Comparator.comparing(P1_FaturadoDia::getMes).thenComparing(P1_FaturadoDia::getDia));
		cria_faturadodia();
		
	}
	
	private void cria_faturadodia() {		
		faturadodia = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Faturado x Dia");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (P1_FaturadoDia v : lista2) {
			values.add(v.getFaturado());
			bgColor.add("rgba(30, 144, 255)");
			labels.add(getDayOfWeek(v.getDia()+"/"+v.getMes()+"/"+v.getAno())+"-"+v.getDia()+"/"+v.getMes());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		faturadodia.setData(data);

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
        ticksx.setMaxRotation(90);
        ticksx.setMinRotation(90);
        linearx.setTicks(ticksx);
        cScales.addXAxesData(linearx);
        
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);

        faturadodia.setOptions(options);
		
	}
	
	public static String getDayOfWeek(String data) {
	    DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	    DayOfWeek dow = DayOfWeek.from(parser.parse(data));
	    return dow.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
	}

	
	private void cria_faturadometa() {
		faturadometa = new BarChartModel();
		ChartData data = new ChartData();
			
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		//hbarDataSet.setLabel("Faturado");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (P1_MetaFaturado v : lista) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getFaturado());
			result.replaceAll(",", ".");
			
			double aDouble = v.getFaturado().doubleValue();
			
			values.add(aDouble);
			bgColor.add("rgba(28 ,134 ,238)");
			labels.add("Faturado ");
		}
		
		for (P1_MetaFaturado v : lista) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getMeta());
			result.replaceAll(",", ".");
			
			double aDouble = v.getMeta().doubleValue();
			
			values.add(aDouble);
			bgColor.add("rgba(102 , 205 , 170)");
			labels.add("Meta");
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);
		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		
		faturadometa.setData(data);

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
        
        Legend legend = new Legend();
        legend.setDisplay(false);
        options.setLegend(legend);
                
        faturadometa.setOptions(options);
       
	}
	
	private void cria_pedidometa() {
		pedidometa = new BarChartModel();
		ChartData data = new ChartData();
			
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		//hbarDataSet.setLabel("Faturado");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for (P1_MetaFaturado v : listapedido) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getFaturado());
			result.replaceAll(",", ".");
			
			double aDouble = v.getFaturado().doubleValue();
			
			values.add(aDouble);
			bgColor.add("rgba(135, 206, 250)");
			labels.add("Pedido ");
		}
		
		for (P1_MetaFaturado v : listapedido) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getMeta());
			result.replaceAll(",", ".");
			
			double aDouble = v.getMeta().doubleValue();
			
			values.add(aDouble);
			bgColor.add("rgba(64, 224, 208)");
			labels.add("Meta");
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);
		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		
		pedidometa.setData(data);

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
        
        Legend legend = new Legend();
        legend.setDisplay(false);
        options.setLegend(legend);
                
        pedidometa.setOptions(options);
       
	}

	
	public List<P1_FaturadoDia> getLista2pedido() {
		return lista2pedido;
	}

	public void setLista2pedido(List<P1_FaturadoDia> lista2pedido) {
		this.lista2pedido = lista2pedido;
	}

	public BarChartModel getPedidometa() {
		return pedidometa;
	}

	public void setPedidometa(BarChartModel pedidometa) {
		this.pedidometa = pedidometa;
	}

	public BarChartModel getPedidodia() {
		return pedidodia;
	}

	public void setPedidodia(BarChartModel pedidodia) {
		this.pedidodia = pedidodia;
	}

	public List<P1_MetaFaturado> getListapedido() {
		return listapedido;
	}

	public void setListapedido(List<P1_MetaFaturado> listapedido) {
		this.listapedido = listapedido;
	}

	public Date getData_grafico3() {
		return data_grafico3;
	}

	public void setData_grafico3(Date data_grafico3) {
		this.data_grafico3 = data_grafico3;
	}

	public Date getData_grafico4() {
		return data_grafico4;
	}

	public void setData_grafico4(Date data_grafico4) {
		this.data_grafico4 = data_grafico4;
	}

	public BarChartModel getFaturadodia() {
		return faturadodia;
	}

	public void setFaturadodia(BarChartModel faturadodia) {
		this.faturadodia = faturadodia;
	}

	public P1_FaturadoDia getVendadia() {
		return vendadia;
	}

	public void setVendadia(P1_FaturadoDia vendadia) {
		this.vendadia = vendadia;
	}

	public List<P1_FaturadoDia> getLista2() {
		return lista2;
	}

	public void setLista2(List<P1_FaturadoDia> lista2) {
		this.lista2 = lista2;
	}

	public BarChartModel getFaturadometa() {
		return faturadometa;
	}

	public void setFaturadometa(BarChartModel faturadometa) {
		this.faturadometa = faturadometa;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
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

	public P1_MetaFaturado getMetavenda() {
		return metavenda;
	}

	public void setMetavenda(P1_MetaFaturado metavenda) {
		this.metavenda = metavenda;
	}

	public List<P1_MetaFaturado> getLista() {
		return lista;
	}

	public void setLista(List<P1_MetaFaturado> lista) {
		this.lista = lista;
	}

}
