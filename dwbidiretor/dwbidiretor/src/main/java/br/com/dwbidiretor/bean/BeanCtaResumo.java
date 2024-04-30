package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dwbidiretor.classe.CtaResumo;
import br.com.dwbidiretor.servico.ServicoCtaResumo;


@Named
@ViewScoped
public class BeanCtaResumo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano;
	private Date  horaatual = new Date();
	
	private List<CtaResumo> lista = new ArrayList<>();
	private CtaResumo ctaresumo = new CtaResumo();
	
	private double cta1;
	private double cta2;
	private double cta3;
	private double cta4;
	private double cta5;
	private double cta6;
	private double cta7;
	private double cta8;
	private double cta9;
	private double cta10;
	private double cta11;
	private double cta12;
	
	private List<CtaResumo> lista_receber = new ArrayList<>();
	private List<CtaResumo> lista_vencido = new ArrayList<>();
	private List<CtaResumo> lista_pagar = new ArrayList<>();
	private List<CtaResumo> lista_previsao = new ArrayList<>();
	
	@Inject
	private ServicoCtaResumo servico;

	
	@PostConstruct
	public void init() {
		
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		ano = data.substring(6,10);
				
	}
	
	public void filtrar(){
		
		lista = servico.ctaresumo(ano);		
	
	}
	
	public void calcula_jan(double value) {
		cta1 += value;
	}
	public void calcula_fev(double value) {
		cta2 += value;
	}
	public void calcula_mar(double value) {
		cta3 += value;
	}
	public void calcula_abr(double value) {
		cta4 += value;
	}
	public void calcula_mai(double value) {
		cta5 += value;
	}
	public void calcula_jun(double value) {
		cta6 += value;
	}
	public void calcula_jul(double value) {
		cta7 += value;
	}
	public void calcula_ago(double value) {
		cta8 += value;
	}
	public void calcula_set(double value) {
		cta9 += value;
	}
	public void calcula_out(double value) {
		cta10 += value;
	}
	public void calcula_nov(double value) {
		cta11 += value;
	}
	public void calcula_dez(double value) {
		cta12 += value;
	}

	public double getTotal_jan() {
		double aux = cta1;
		cta1 = 0;
		return aux;
	}
	public double getTotal_fev() {
		double aux = cta2;
		cta2 = 0;
		return aux;
	}
	public double getTotal_mar() {
		double aux = cta3;
		cta3 = 0;
		return aux;
	}
	public double getTotal_abr() {
		double aux = cta4;
		cta4 = 0;
		return aux;
	}
	public double getTotal_mai() {
		double aux = cta5;
		cta5 = 0;
		return aux;
	}
	public double getTotal_jun() {
		double aux = cta6;
		cta6 = 0;
		return aux;
	}
	public double getTotal_jul() {
		double aux = cta7;
		cta7 = 0;
		return aux;
	}
	public double getTotal_ago() {
		double aux = cta8;
		cta8 = 0;
		return aux;
	}
	public double getTotal_set() {
		double aux = cta9;
		cta9 = 0;
		return aux;
	}
	public double getTotal_out() {
		double aux = cta10;
		cta10 = 0;
		return aux;
	}
	public double getTotal_nov() {
		double aux = cta11;
		cta11 = 0;
		return aux;
	}
	public double getTotal_dez() {
		double aux = cta12;
		cta12 = 0;
		return aux;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Date getHoraatual() {
		return horaatual;
	}

	public void setHoraatual(Date horaatual) {
		this.horaatual = horaatual;
	}

	public List<CtaResumo> getLista() {
		return lista;
	}

	public void setLista(List<CtaResumo> lista) {
		this.lista = lista;
	}

	public CtaResumo getCtaresumo() {
		return ctaresumo;
	}

	public void setCtaresumo(CtaResumo ctaresumo) {
		this.ctaresumo = ctaresumo;
	}

	public List<CtaResumo> getLista_receber() {
		return lista_receber;
	}

	public void setLista_receber(List<CtaResumo> lista_receber) {
		this.lista_receber = lista_receber;
	}

	public List<CtaResumo> getLista_vencido() {
		return lista_vencido;
	}

	public void setLista_vencido(List<CtaResumo> lista_vencido) {
		this.lista_vencido = lista_vencido;
	}

	public List<CtaResumo> getLista_pagar() {
		return lista_pagar;
	}

	public void setLista_pagar(List<CtaResumo> lista_pagar) {
		this.lista_pagar = lista_pagar;
	}

	public List<CtaResumo> getLista_previsao() {
		return lista_previsao;
	}

	public void setLista_previsao(List<CtaResumo> lista_previsao) {
		this.lista_previsao = lista_previsao;
	}

	public double getCta1() {
		return cta1;
	}

	public void setCta1(double cta1) {
		this.cta1 = cta1;
	}

	public double getCta2() {
		return cta2;
	}

	public void setCta2(double cta2) {
		this.cta2 = cta2;
	}

	public double getCta3() {
		return cta3;
	}

	public void setCta3(double cta3) {
		this.cta3 = cta3;
	}

	public double getCta4() {
		return cta4;
	}

	public void setCta4(double cta4) {
		this.cta4 = cta4;
	}

	public double getCta5() {
		return cta5;
	}

	public void setCta5(double cta5) {
		this.cta5 = cta5;
	}

	public double getCta6() {
		return cta6;
	}

	public void setCta6(double cta6) {
		this.cta6 = cta6;
	}

	public double getCta7() {
		return cta7;
	}

	public void setCta7(double cta7) {
		this.cta7 = cta7;
	}

	public double getCta8() {
		return cta8;
	}

	public void setCta8(double cta8) {
		this.cta8 = cta8;
	}

	public double getCta9() {
		return cta9;
	}

	public void setCta9(double cta9) {
		this.cta9 = cta9;
	}

	public double getCta10() {
		return cta10;
	}

	public void setCta10(double cta10) {
		this.cta10 = cta10;
	}

	public double getCta11() {
		return cta11;
	}

	public void setCta11(double cta11) {
		this.cta11 = cta11;
	}

	public double getCta12() {
		return cta12;
	}

	public void setCta12(double cta12) {
		this.cta12 = cta12;
	}
	
	
	
}
