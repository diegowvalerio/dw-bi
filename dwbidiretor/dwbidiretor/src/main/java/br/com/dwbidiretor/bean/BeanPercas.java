package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.servico.ServicoPerca;



@Named
@ViewScoped
public class BeanPercas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes,dia,setor,tipo;
	int i,t;
	private Date  horaatual = new Date();
	
	private List<Perca> lista = new ArrayList<>();
	private List<PercaDia> listadia = new ArrayList<>();
	private List<TipoPerca>  listatipo = new ArrayList<>();
	private TipoPerca tipoperca = new TipoPerca();
	private List<PercaProduto>  listaproduto = new ArrayList<>();
	@Inject
	private ServicoPerca servico;
	
	private BarChartModel perca_anomes;
	private BarChartModel perca_dia;
	
	@PostConstruct
	public void init() {
		setor = "TODOS";
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		dia = data.substring(0,2);
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
		tipo = "0";
		t=2;
		i=2;
		
		listatipo = servico.tipoperca();
		lista = servico.perca(ano, mes,setor,i,tipo, t);
		listadia = servico.percadia(ano, mes, setor, i, tipo, t);
		listaproduto = servico.percaproduto(ano, mes, setor, i, tipo, t);
		
		ajustalistadia();
		cria_perca_anomes();
				
	}
	
	public void filtrar(){
		if(!setor.equals("TODOS")) {
			i = 1;
		}else {
			i = 2;
		}
		
		if(getTipoperca() != null) {
			tipo = tipoperca.getId().toString();
			t = 1;
		}else {
			tipo = "0";
			t = 2;
		}
			
		lista = servico.perca(ano, mes,setor,i,tipo,t);
		listadia = servico.percadia(ano, mes, setor, i,tipo,t);
		listaproduto = servico.percaproduto(ano, mes, setor, i, tipo, t);
		ajustalistadia();
		cria_perca_anomes();
		
	}
	
	private void ajustalistadia() {
		Calendar calendario = Calendar.getInstance(); 
		calendario.set(Integer.parseInt(ano), Integer.parseInt(mes), 0);
		calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dias = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		//teste
			
		if(listadia.size()>0) {
			
			for(int x = 1; x <= dias;x++){
				int h = 1;
				for(int y = 0; y < listadia.size();y++) {
					if(Integer.parseInt(listadia.get(y).getDia()) == x) {
						h=0;
					}	
				}
				if(h==1) {
					PercaDia d = new PercaDia();
					if(x<10) {
						d.setDia("0"+x);
					}else {
						d.setDia(""+x);
					}
					d.setQuantidade(new BigDecimal(0));
					listadia.add(d);
				}
			}
		}
		
		cria_perca_dia();
		
	}
	
	public static String getDayOfWeek(String data) {
	    DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	    DayOfWeek dow = DayOfWeek.from(parser.parse(data));
	    return dow.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
	}

	private void cria_perca_dia() {
		perca_dia = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Perca Setor x Dia");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		listadia.sort(Comparator.comparing(PercaDia::getDia));
		
		for (PercaDia v : listadia) {
			values.add(v.getQuantidade());
			bgColor.add("rgba(30, 144, 255)");
			labels.add(getDayOfWeek(v.getDia()+"/"+mes+"/"+ano)+"-"+v.getDia());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		perca_dia.setData(data);

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

		/*Title title = new Title();
		title.setDisplay(true);
		title.setText(mes + "/" + ano);
		options.setTitle(title);*/
        perca_dia.setExtender("my_ext");
        perca_dia.setOptions(options);
		
	}
	
	public void cria_perca_anomes() {		
		perca_anomes = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel("Perca Setor x Mês");
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		
		Collections.sort(lista,Collections.reverseOrder(Comparator.comparing(Perca::getQuantidade)));
		for (Perca v : lista) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getQuantidade());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(30, 144, 255)");
			labels.add(v.getSetor()+": "+result);
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		perca_anomes.setData(data);

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

        perca_anomes.setOptions(options);
	}
	
	public String getqtdetotal() {
	float total = 0;

	for (PercaProduto p : getListaproduto()) {
		total = total + p.getQuantidade().intValue();
	}

	return new DecimalFormat("###,###.###").format(total);
}
	public String getvalortotal() {
		float total = 0;

		for (PercaProduto p : getListaproduto()) {
			total = total + p.getValor().floatValue();
		}

		return new DecimalFormat("###,###.##").format(total);
	}

	public List<PercaProduto> getListaproduto() {
		return listaproduto;
	}

	public void setListaproduto(List<PercaProduto> listaproduto) {
		this.listaproduto = listaproduto;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public List<TipoPerca> getListatipo() {
		return listatipo;
	}

	public void setListatipo(List<TipoPerca> listatipo) {
		this.listatipo = listatipo;
	}

	public TipoPerca getTipoperca() {
		return tipoperca;
	}

	public void setTipoperca(TipoPerca tipoperca) {
		this.tipoperca = tipoperca;
	}

	public BarChartModel getPerca_dia() {
		return perca_dia;
	}

	public void setPerca_dia(BarChartModel perca_dia) {
		this.perca_dia = perca_dia;
	}

	public List<PercaDia> getListadia() {
		return listadia;
	}

	public void setListadia(List<PercaDia> listadia) {
		this.listadia = listadia;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public BarChartModel getPerca_anomes() {
		return perca_anomes;
	}

	public void setPerca_anomes(BarChartModel perca_anomes) {
		this.perca_anomes = perca_anomes;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
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

	public List<Perca> getLista() {
		return lista;
	}

	public void setLista(List<Perca> lista) {
		this.lista = lista;
	}	
	
}
