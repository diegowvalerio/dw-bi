package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.dwbidiretor.classe.Rh_Setor;
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
	
	private List<Rh_Setor> listaSetor = new ArrayList<>();
	private Rh_Setor rh_setor = new Rh_Setor();
	
	@Inject
	private ServicoRh_Folha servico;
	private  byte[] imageBytes;
	private StreamedContent image;
	private DonutChartModel donutModel;
	private String urlimg;
	private String urlimg2;
	private String urlimg3;
	
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
		listaSetor= servico.rh_setor(ano, mes);

		//cria_totalfuncionarios();
		gerarimg();
		gerarimg2();
		//gerarimg3();
	
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
        		+ "    datasets: [{ data: ["+rh_folha.getPerc_clt_funcionarios()+","+rh_folha.getPerc_pj_funcionarios()+"],"
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
        		+ "        backgroundColor: '#FFFFFF', "
        		+ "        borderRadius: 3, "
        		+ "        font: { "
        		+ "          color: 'red', "
        		+ "          weight: 'bold', "
        		+ "        }, "
        		+ "        formatter: (value) => { "
        		+ "          return value + '%'; "
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
        //imageBytes = chart.toByteArray();
		}
	}
	
	
	private void gerarimg2() {
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
        		+ "    datasets: [{ data: ["+rh_folha.getPerc_clt_valor()+","+rh_folha.getPerc_pj_valor()+"],"
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
        		+ "        backgroundColor: '#FFFFFF', "
        		+ "        borderRadius: 3, "
        		+ "        font: { "
        		+ "          color: 'red', "
        		+ "          weight: 'bold', "
        		+ "        }, "
        		+ "        formatter: (value) => { "
        		+ "          return value + '%'; "
        		+ "        }, "
        		+ "      },	"
        		+ "      doughnutlabel: { "
        		+ "        labels: [{ text: '"+rh_folha.getTotal_valor()+"', font: { size: 20 } }, { text: 'Total' }], "
        		+ "      },"
        		+ "    }, "
        		+ "  }, "
        		+ "}");

        // Print the chart image URL
        //System.out.println(chart.getUrl());
        
        	urlimg2 = chart.getUrl();
		
        // Or get the image
        //imageBytes = chart.toByteArray();
		}
	}
	
	private void gerarimg3() {
		rh_setor = listaSetor.get(0);
		String[] setor =  new String[listaSetor.size()];
		BigInteger[] qtde = new BigInteger[listaSetor.size()];
		
		for (int i = 0; i < listaSetor.size(); i++) {
			setor[i]= listaSetor.get(i).getSetor_2();
			qtde[i]= listaSetor.get(i).getQtde();
		}
		
		//System.out.println(Arrays.toString(setor));
		
		if(listaSetor.size()>0) {
        
		
		QuickChart chart = new QuickChart("http","177.72.156.109",8854);
        chart.setWidth(500);
        chart.setHeight(300);
        chart.setVersion("2.9.4");
        chart.setConfig("{ "
        		+ "  type: 'doughnut', "
        		+ "  data: { "
        		+ "    labels: "+Arrays.toString(setor)+", "
        		+ "    datasets: [{ data: "+Arrays.toString(qtde)+","
        		+ "	   backgroundColor: [ "
        		+ "          'rgb(255, 159, 64)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "          'rgb(54, 162, 235)', "
        		+ "        ],"
        		+ "		}], "
        		+ "  }, "
        		+ "  options: { "
        		+ "    plugins: {"
        		+ "		datalabels: { "
        		+ "        display: true, "
        		+ "        backgroundColor: '#FFFFFF', "
        		+ "        borderRadius: 3, "
        		+ "        font: { "
        		+ "          color: 'red', "
        		+ "          weight: 'bold', "
        		+ "        }, "
        		+ "        formatter: (value) => { "
        		+ "          return value + '%'; "
        		+ "        }, "
        		+ "      },	"
        		+ "      doughnutlabel: { "
        		+ "        labels: [{ text: '"+rh_setor.getQtde_geral()+"', font: { size: 20 } }, { text: 'Total' }], "
        		+ "      },"
        		+ "    }, "
        		+ "  }, "
        		+ "}");
        
        	urlimg3 = chart.getUrl();
        	System.out.println(chart.getUrl());
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

	public String getUrlimg3() {
		return urlimg3;
	}

	public void setUrlimg3(String urlimg3) {
		this.urlimg3 = urlimg3;
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

	public String getUrlimg2() {
		return urlimg2;
	}

	public void setUrlimg2(String urlimg2) {
		this.urlimg2 = urlimg2;
	}

	public List<Rh_Setor> getListaSetor() {
		return listaSetor;
	}

	public void setListaSetor(List<Rh_Setor> listaSetor) {
		this.listaSetor = listaSetor;
	}
	
	
	
}
