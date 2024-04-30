package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.donut.DonutChartDataSet;
import org.primefaces.model.charts.donut.DonutChartModel;
import org.primefaces.model.charts.donut.DonutChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;

import br.com.dwbidiretor.classe.Rh_Folha;
import br.com.dwbidiretor.servico.ServicoRh_Folha;
import io.quickchart.QuickChart;



@ManagedBean
@RequestScoped
public class BeanRh implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes;
	private Date  horaatual = new Date();
	
	private List<Rh_Folha> lista = new ArrayList<>();
	private Rh_Folha rh_folha = new Rh_Folha();
	@Inject
	private ServicoRh_Folha servico;
	private  byte[] imageBytes;
	private StreamedContent image;
	private DonutChartModel donutModel;
	private String urlimg;
	
	@PostConstruct
	public void init() {
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
		//cria_totalfuncionarios();
	}
	
	public void filtrar(){
		
		lista = servico.rh_folha(ano, mes);		

		//cria_totalfuncionarios();
		gerarimg();
	}
	
	private void gerarimg() {
		if(lista.size()>0) {
        	rh_folha = lista.get(0);
		
		QuickChart chart = new QuickChart("http","177.72.156.109",8854);
        chart.setWidth(500);
        chart.setHeight(300);
        chart.setVersion("2.9.4");
        chart.setConfig("{ "
        		+ "  type: 'doughnut', "
        		+ "  data: { "
        		+ "    labels: ['CLT', 'PJ'], "
        		+ "    datasets: [{ data: ["+rh_folha.getQtde_clt()+","+rh_folha.getQtde_pj()+"],"
        		+ "	   backgroundColor: [ "
        		+ "          'rgb(255, 159, 64)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "        ],"
        		+ "		}], "
        		+ "  }, "
        		+ "  options: { "
        		+ "    plugins: {"
        		+ "		datalabels: { "
        		+ "        display: true, "
        		+ "        backgroundColor: '#ccc', "
        		+ "        borderRadius: 3, "
        		+ "        font: { "
        		+ "          color: 'red', "
        		+ "          weight: 'bold', "
        		+ "        }, "
        		+ "      },	"
        		+ "      doughnutlabel: { "
        		+ "        labels: [{ text: '"+rh_folha.getTotal_funcionarios()+"', font: { size: 20 } }, { text: 'Total' }], "
        		+ "      },"
        		+ "    }, "
        		+ "  }, "
        		+ "}");

        // Print the chart image URL
        //System.out.println(chart.getUrl());
        
        	urlimg = chart.getUrl();
		
        // Or get the image
        imageBytes = chart.toByteArray();
		}
	}
	
	private void cria_totalfuncionarios() {
		donutModel = new DonutChartModel();
        ChartData data = new ChartData();
         
        DonutChartDataSet dataSet = new DonutChartDataSet();
        List<Number> values = new ArrayList<>();
        
        if(lista.size()>0) {
        	rh_folha = lista.get(0);
            
            values.add(rh_folha.getQtde_clt());
            values.add(rh_folha.getQtde_pj());
        }else {
        	 values.add(0);
        }
        
        
        dataSet.setData(values);
         
        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 205, 86)");
        bgColors.add("rgb(54, 162, 235)");

        dataSet.setBackgroundColor(bgColors);
         
        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("CLT");
        labels.add("PJ");
        
        data.setLabels(labels);
         
        DonutChartOptions options = new DonutChartOptions() ;
        Legend legend = new Legend();
        legend.setDisplay(true);
        options.setLegend(legend);
        
       // donutModel.setOptions(options);
        donutModel.setExtender("chartExtender");
        donutModel.setData(data);
        
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

	public List<Rh_Folha> getLista() {
		return lista;
	}

	public void setLista(List<Rh_Folha> lista) {
		this.lista = lista;
	}

	public Rh_Folha getRh_folha() {
		return rh_folha;
	}

	public void setRh_folha(Rh_Folha rh_folha) {
		this.rh_folha = rh_folha;
	}

	public DonutChartModel getDonutModel() {
		return donutModel;
	}

	public void setDonutModel(DonutChartModel donutModel) {
		this.donutModel = donutModel;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public String getUrlimg() {
		return urlimg;
	}

	public void setUrlimg(String urlimg) {
		this.urlimg = urlimg;
	}
	
	
	
}
