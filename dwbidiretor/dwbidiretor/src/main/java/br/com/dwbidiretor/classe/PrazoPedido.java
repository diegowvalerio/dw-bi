package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;


public class PrazoPedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal pedidoid;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String tipopedido;
	private String fase_atual;
	private String status;
	private BigDecimal valor;
	
	private String dt_digitacao;
	private int dias_digitacao;
	
	private String dt_financeiro;
	private int dias_financeiro;
	
	private String dt_conferencia;
	private int dias_conferencia;

	private String dt_analisegestor;
	private int dias_analisegestor;
	
	private String dt_color;
	private int dias_color;
	
	private String dt_programacao;
	private int dias_programacao;
	
	private String dt_producao;
	private int dias_producao;
	
	private String dt_producaocolor;
	private int dias_producaocolor;
	
	private String dt_expedicao;
	private int dias_expedicao;
	
	private String dt_faturamento;
	private int dias_faturamento;
	
	private String dt_posvenda;
	private int dias_posvenda;
	
	private int totaldias;

	public PrazoPedido() {
		super();
	}

	public BigDecimal getPedidoid() {
		return pedidoid;
	}

	public void setPedidoid(BigDecimal pedidoid) {
		this.pedidoid = pedidoid;
	}

	

	public String getFase_atual() {
		return fase_atual;
	}

	public void setFase_atual(String fase_atual) {
		this.fase_atual = fase_atual;
	}

	public BigDecimal getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(BigDecimal codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipopedido() {
		return tipopedido;
	}

	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDt_digitacao() {
		return dt_digitacao;
	}

	public void setDt_digitacao(String dt_digitacao) {
		this.dt_digitacao = dt_digitacao;
	}

	public int getDias_digitacao() {
		return dias_digitacao;
	}

	public void setDias_digitacao(int dias_digitacao) {
		this.dias_digitacao = dias_digitacao;
	}

	public String getDt_financeiro() {
		return dt_financeiro;
	}

	public void setDt_financeiro(String dt_financeiro) {
		this.dt_financeiro = dt_financeiro;
	}

	public int getDias_financeiro() {
		return dias_financeiro;
	}

	public void setDias_financeiro(int dias_financeiro) {
		this.dias_financeiro = dias_financeiro;
	}

	public String getDt_conferencia() {
		return dt_conferencia;
	}

	public void setDt_conferencia(String dt_conferencia) {
		this.dt_conferencia = dt_conferencia;
	}

	public int getDias_conferencia() {
		return dias_conferencia;
	}

	public void setDias_conferencia(int dias_conferencia) {
		this.dias_conferencia = dias_conferencia;
	}

	public String getDt_analisegestor() {
		return dt_analisegestor;
	}

	public void setDt_analisegestor(String dt_analisegestor) {
		this.dt_analisegestor = dt_analisegestor;
	}

	public int getDias_analisegestor() {
		return dias_analisegestor;
	}

	public void setDias_analisegestor(int dias_analisegestor) {
		this.dias_analisegestor = dias_analisegestor;
	}

	public String getDt_color() {
		return dt_color;
	}

	public void setDt_color(String dt_color) {
		this.dt_color = dt_color;
	}

	public int getDias_color() {
		return dias_color;
	}

	public void setDias_color(int dias_color) {
		this.dias_color = dias_color;
	}

	public String getDt_programacao() {
		return dt_programacao;
	}

	public void setDt_programacao(String dt_programacao) {
		this.dt_programacao = dt_programacao;
	}

	public int getDias_programacao() {
		return dias_programacao;
	}

	public void setDias_programacao(int dias_programacao) {
		this.dias_programacao = dias_programacao;
	}

	public String getDt_producao() {
		return dt_producao;
	}

	public void setDt_producao(String dt_producao) {
		this.dt_producao = dt_producao;
	}

	public int getDias_producao() {
		return dias_producao;
	}

	public void setDias_producao(int dias_producao) {
		this.dias_producao = dias_producao;
	}

	public String getDt_producaocolor() {
		return dt_producaocolor;
	}

	public void setDt_producaocolor(String dt_producaocolor) {
		this.dt_producaocolor = dt_producaocolor;
	}

	public int getDias_producaocolor() {
		return dias_producaocolor;
	}

	public void setDias_producaocolor(int dias_producaocolor) {
		this.dias_producaocolor = dias_producaocolor;
	}

	public String getDt_expedicao() {
		return dt_expedicao;
	}

	public void setDt_expedicao(String dt_expedicao) {
		this.dt_expedicao = dt_expedicao;
	}

	public int getDias_expedicao() {
		return dias_expedicao;
	}

	public void setDias_expedicao(int dias_expedicao) {
		this.dias_expedicao = dias_expedicao;
	}

	public String getDt_faturamento() {
		return dt_faturamento;
	}

	public void setDt_faturamento(String dt_faturamento) {
		this.dt_faturamento = dt_faturamento;
	}

	public int getDias_faturamento() {
		return dias_faturamento;
	}

	public void setDias_faturamento(int dias_faturamento) {
		this.dias_faturamento = dias_faturamento;
	}

	public String getDt_posvenda() {
		return dt_posvenda;
	}

	public void setDt_posvenda(String dt_posvenda) {
		this.dt_posvenda = dt_posvenda;
	}

	public int getDias_posvenda() {
		return dias_posvenda;
	}

	public void setDias_posvenda(int dias_posvenda) {
		this.dias_posvenda = dias_posvenda;
	}

	public int getTotaldias() {
		return totaldias;
	}

	public void setTotaldias(int totaldias) {
		this.totaldias = totaldias;
	}

	

}
