package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
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

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;

import br.com.dwbidiretor.classe.Producao;
import br.com.dwbidiretor.classe.ProducaoDia;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.servico.ServicoProducao;



@Named
@ViewScoped
public class BeanProducao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes,dia,setor,nome;
	int i,t;
	private Date  horaatual = new Date();
	
	private List<Producao> lista = new ArrayList<>();
	private Producao producao = new Producao();
	private List<ProducaoDia> listadia = new ArrayList<>();
	@Inject
	private ServicoProducao servico;
	
	private BarChartModel producao_anomes;
	private BarChartModel producao_dia;
	
	@PostConstruct
	public void init() {
		i = 2;
		setor = "0";
		nome = "GERAL";
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		dia = data.substring(0,2);
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
		
		lista = servico.producao(ano, mes);
		listadia = servico.producaodia(ano, mes,setor,i);
	
		ajustalistadia();
		cria_producao_anomes();
				
	}
	
	public void filtrar(){
		nome = "GERAL";
		
		lista = servico.producao(ano, mes);
		listadia = servico.producaodia(ano, mes,setor,i);
		
		ajustalistadia();
		cria_producao_anomes();
		
	}
	
	public void itemSelect(ItemSelectEvent event) {
	 Producao v = lista.get(event.getItemIndex());
	 
	 listadia = servico.producaodia(ano, mes,v.getSetorid().toString(),1);
	 
	 if(listadia.size()>0) {
		 nome = v.getSetor();
		 ajustalistadia();
	 }
 
}
	
	public void onRowSelect(SelectEvent event) throws ParseException {
		producao = (Producao) event.getObject();
		
		if(producao != null) {
			listadia = servico.producaodia(ano, mes,producao.getSetorid().toString(),1);
			
			 if(listadia.size()>0) {
				 nome = producao.getSetor();
				 ajustalistadia();
			 }
		}
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
					ProducaoDia d = new ProducaoDia();
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
		
		cria_producao_dia();
		
	}
	
	public static String getDayOfWeek(String data) {
	    DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	    DayOfWeek dow = DayOfWeek.from(parser.parse(data));
	    return dow.getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"));
	}

	private void cria_producao_dia() {
		producao_dia = new BarChartModel();
		ChartData data = new ChartData();
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel(nome);
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		listadia.sort(Comparator.comparing(ProducaoDia::getDia));
		
		for (ProducaoDia v : listadia) {
			values.add(v.getQuantidade());
			bgColor.add("rgba(30, 144, 255)");
			labels.add(getDayOfWeek(v.getDia()+"/"+mes+"/"+ano)+"-"+v.getDia());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		producao_dia.setData(data);

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
        producao_dia.setExtender("my_ext");
        producao_dia.setOptions(options);
		
	}
	
	public void cria_producao_anomes() {		
		producao_anomes = new BarChartModel();
		ChartData data = new ChartData();
		
		
		BarChartDataSet hbarDataSet = new BarChartDataSet();
		hbarDataSet.setLabel(mes + "/" + ano);
		List<Number> values = new ArrayList<>();
		List<String> bgColor = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		
		Collections.sort(lista,Collections.reverseOrder(Comparator.comparing(Producao::getQuantidade)));
		for (Producao v : lista) {
			
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);         
			df.setMinimumFractionDigits(0);         
			df.setGroupingUsed(false);
			String result = df.format(v.getQuantidade());
			double aDouble = Double.parseDouble(result);
			
			values.add(aDouble);
			bgColor.add("rgba(30, 144, 255)");
			labels.add(v.getSetor());
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		producao_anomes.setData(data);

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

      
        producao_anomes.setOptions(options);
	}
	

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public BarChartModel getProducao_dia() {
		return producao_dia;
	}

	public void setProducao_dia(BarChartModel producao_dia) {
		this.producao_dia = producao_dia;
	}

	public List<ProducaoDia> getListadia() {
		return listadia;
	}

	public void setListadia(List<ProducaoDia> listadia) {
		this.listadia = listadia;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public BarChartModel getProducao_anomes() {
		return producao_anomes;
	}

	public void setProducao_anomes(BarChartModel producao_anomes) {
		this.producao_anomes = producao_anomes;
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

	public List<Producao> getLista() {
		return lista;
	}

	public void setLista(List<Producao> lista) {
		this.lista = lista;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Producao getProducao() {
		return producao;
	}

	public void setProducao(Producao producao) {
		this.producao = producao;
	}	
	
}
