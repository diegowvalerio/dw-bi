package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.servico.ServicoVendaAnoMes;

@Named
@ViewScoped
public class BeanVenda implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendaAnoMes vendaAnoMes = new VendaAnoMes();
	@Inject
	private ServicoVendaAnoMes servico;
	private List<VendaAnoMes> listavendaanomes = new ArrayList<>();

	private LineChartModel animatedModel1;
	private BarChartModel animatedModel2;

	private String vendedorlogado;

	@PostConstruct
	public void init() {
		createAnimatedModels();
		listavendaanomes = servico.vendaanomes();

	}

	public List<VendaAnoMes> getListavendaanomes() {
		return listavendaanomes;
	}

	public void setListavendaanomes(List<VendaAnoMes> listavendaanomes) {
		this.listavendaanomes = listavendaanomes;
	}

	public LineChartModel getAnimatedModel1() {
		return animatedModel1;
	}

	public void setAnimatedModel1(LineChartModel animatedModel1) {
		this.animatedModel1 = animatedModel1;
	}

	public BarChartModel getAnimatedModel2() {
		return animatedModel2;
	}

	public void setAnimatedModel2(BarChartModel animatedModel2) {
		this.animatedModel2 = animatedModel2;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}

	public void createAnimatedModels() {
		animatedModel1 = initLinearModel();
		animatedModel1.setTitle("Line Chart");
		animatedModel1.setAnimate(true);
		animatedModel1.setLegendPosition("se");
		Axis yAxis = animatedModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax(10);

		animatedModel2 = initBarModel();
		animatedModel2.setTitle("Venda Ano x Mês");
		animatedModel2.setAnimate(true);
		animatedModel2.setLegendPosition("ne");
		yAxis = animatedModel2.getAxis(AxisType.Y);
		yAxis.setMin(0);
		// yAxis.setMax(20000);
		yAxis.setTickFormat("R$ %'.2f");
		yAxis.setLabel("Valor");

		Axis XAxis = animatedModel2.getAxis(AxisType.X);
		XAxis.setLabel("Mês");


	}
	
	 private LineChartModel initLinearModel() {
	        LineChartModel model = new LineChartModel();
	 
	        LineChartSeries series1 = new LineChartSeries();
	        series1.setLabel("Series 1");
	 
	        series1.set(1, 2);
	        series1.set(2, 1);
	        series1.set(3, 3);
	        series1.set(4, 6);
	        series1.set(5, 8);
	 
	        LineChartSeries series2 = new LineChartSeries();
	        series2.setLabel("Series 2");
	 
	        series2.set(1, 6);
	        series2.set(2, 3);
	        series2.set(3, 2);
	        series2.set(4, 7);
	        series2.set(5, 9);
	 
	        model.addSeries(series1);
	        model.addSeries(series2);
	         
	        return model;
	    }
	 
	 @SuppressWarnings("null")
		public BarChartModel initBarModel() {
	    	
	    	listavendaanomes = servico.vendaanomes();
	    	
	    	 BarChartModel model = new BarChartModel();
	         
	    	if(!listavendaanomes.isEmpty()){
	    		List<String> tipo = new ArrayList<>();
	    		for (VendaAnoMes movimento1: listavendaanomes){
	    			tipo.add(movimento1.getAno());
	    		}
	    		List<String> tipoc = new ArrayList<>();
	    		for(String c : tipo){
	    			if(!tipoc.contains(c)){
	    				tipoc.add(c);
	    			}
	    		}
	    			    			
	    		//ChartSeries tipopedido = new ChartSeries();
	    		for(String c:tipoc){
	    		ChartSeries tipopedido = new ChartSeries();
	    		tipopedido.setLabel(c);
	    		List<String> mes = new ArrayList<>();
	    		//int i = 00;	
	    			for (VendaAnoMes movimento1: listavendaanomes){
	        			if(movimento1.getAno().equals(c)){
	        				//tipopedido.set(movimento1.getMes(), movimento1.getValor());
	        				//i++;
	        				mes.add(movimento1.getMes());
	        			} 
	        		}
	    			for(int x =1;x <13; x++){
	    				if(!mes.contains(String.format("%02d", new Object[] {x}))){
	    					tipopedido.set(String.format("%02d", new Object[] {x}), 0);
	    				}else{
	    					for (VendaAnoMes movimento1: listavendaanomes){
	    	        			if(movimento1.getAno().equals(c) && movimento1.getMes().equals(String.format("%02d", new Object[] {x}))){
	    	        				tipopedido.set(movimento1.getMes(), movimento1.getValor());
	    	        			} 
	    	        		}
	    					
	    				}
	    			}
	    		model.addSeries(tipopedido);	
	    		}
	    		
	    	}else{
	    		ChartSeries tipopedido = new ChartSeries();
	    		tipopedido.set("0",0);
	    		model.addSeries(tipopedido);
	    	}
	                
	        return model;
	    }

	 
	 /*relatorios normais*/
	/* pegar usuario conectado */
	public String usuarioconectado() {
		String nome;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		//System.out.println(nome);
		return nome;
	}
	/* get e set */

	public void rel_vendaanomes_lista() {
		vendedorlogado = usuarioconectado();
		Relatorio report = new Relatorio();
		report.rel_vendaanomes_lista(vendedorlogado);
	}
}
