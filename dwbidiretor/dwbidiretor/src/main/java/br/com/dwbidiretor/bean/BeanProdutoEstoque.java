package br.com.dwbidiretor.bean;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;

import br.com.dwbidiretor.classe.Almoxarifado;
import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ProdutoEstoque;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.servico.ServicoAlmoxarifado;
import br.com.dwbidiretor.servico.ServicoPerca;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_VendaSubgrupo;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendagrupo;


@Named
@ViewScoped
public class BeanProdutoEstoque implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<ProdutoEstoque> listaproduto_estoque = new ArrayList<>();
	
	//filtro produto
	private List<Produto> lista = new ArrayList<>();
	private Produto produto =  new Produto();
	@Inject
	private ServicoProduto servico;
	
	//filtro grupo
	private Venda_Grupo grupo = new Venda_Grupo();
	private List<Venda_Grupo> listagrupos = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_Vendagrupo servicogrupo;
	
	//filtro Subgrupo
	private Venda_Subgrupo subgrupo = new Venda_Subgrupo();
	private List<Venda_Subgrupo> listaSubgrupos = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_VendaSubgrupo servicoSubgrupo;
	
	//filtro almoxarifado
	private Almoxarifado almoxarifado = new Almoxarifado();
	private List<Almoxarifado> listaalmoxarifados = new ArrayList<>();
	@Inject
	private ServicoAlmoxarifado servicoalmoxarifado;
			
	
	private String grupofiltro = "-1";
	private String subgrupofiltro = "-1";
	private String produtofiltro = "-1";
	private String almoxarifadofiltro = "-1";
	private String tipofiltro = "-1";
	int selecionado  = -1;
	

	@PostConstruct
	public void init() {
		
		listagrupos = servicogrupo.grupos();
		listaSubgrupos = servicoSubgrupo.subgrupos();
		listaalmoxarifados = servicoalmoxarifado.almoxarifados();
		lista = servico.produtos();
	}
	
	public void filtrar() throws InterruptedException{
		if (grupo == null){
			grupofiltro = "-1";
		}else{
			grupofiltro = grupo.getIdgrupo().toString();
		}
		
		if (subgrupo == null){
			subgrupofiltro = "-1";
		}else{
			subgrupofiltro = subgrupo.getIdsubgrupo().toString();
		}
		
		if (produto == null){
			produtofiltro = "-1";
		}else{
			produtofiltro = produto.getProdutoid().toString();
		}
		
		if (almoxarifado == null){
			almoxarifadofiltro = "-1";
		}else{
			almoxarifadofiltro = almoxarifado.getAlmoxarifadoid().toString();
		}
		
		listaproduto_estoque = servico.produtoestoque(produtofiltro, almoxarifadofiltro, grupofiltro, subgrupofiltro, tipofiltro,selecionado);
			
	}
	
	public int getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(int selecionado) {
		this.selecionado = selecionado;
	}

	public Venda_Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Venda_Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Venda_Grupo> getListagrupos() {
		return listagrupos;
	}

	public void setListagrupos(List<Venda_Grupo> listagrupos) {
		this.listagrupos = listagrupos;
	}

	public String getGrupofiltro() {
		return grupofiltro;
	}

	public void setGrupofiltro(String grupofiltro) {
		this.grupofiltro = grupofiltro;
	}

	public List<Produto> getLista() {
		return lista;
	}

	public void setLista(List<Produto> lista) {
		this.lista = lista;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ServicoProduto getServico() {
		return servico;
	}

	public void setServico(ServicoProduto servico) {
		this.servico = servico;
	}

	public ServicoPainel_Diretor_Vendagrupo getServicogrupo() {
		return servicogrupo;
	}

	public void setServicogrupo(ServicoPainel_Diretor_Vendagrupo servicogrupo) {
		this.servicogrupo = servicogrupo;
	}

	public Venda_Subgrupo getSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(Venda_Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}

	public List<Venda_Subgrupo> getListaSubgrupos() {
		return listaSubgrupos;
	}

	public void setListaSubgrupos(List<Venda_Subgrupo> listaSubgrupos) {
		this.listaSubgrupos = listaSubgrupos;
	}

	public ServicoPainel_Diretor_VendaSubgrupo getServicoSubgrupo() {
		return servicoSubgrupo;
	}

	public void setServicoSubgrupo(ServicoPainel_Diretor_VendaSubgrupo servicoSubgrupo) {
		this.servicoSubgrupo = servicoSubgrupo;
	}

	public Almoxarifado getAlmoxarifado() {
		return almoxarifado;
	}

	public void setAlmoxarifado(Almoxarifado almoxarifado) {
		this.almoxarifado = almoxarifado;
	}

	public List<Almoxarifado> getListaalmoxarifados() {
		return listaalmoxarifados;
	}

	public void setListaalmoxarifados(List<Almoxarifado> listaalmoxarifados) {
		this.listaalmoxarifados = listaalmoxarifados;
	}

	public ServicoAlmoxarifado getServicoalmoxarifado() {
		return servicoalmoxarifado;
	}

	public void setServicoalmoxarifado(ServicoAlmoxarifado servicoalmoxarifado) {
		this.servicoalmoxarifado = servicoalmoxarifado;
	}

	public String getSubgrupofiltro() {
		return subgrupofiltro;
	}

	public void setSubgrupofiltro(String subgrupofiltro) {
		this.subgrupofiltro = subgrupofiltro;
	}

	public String getProdutofiltro() {
		return produtofiltro;
	}

	public void setProdutofiltro(String produtofiltro) {
		this.produtofiltro = produtofiltro;
	}

	public String getAlmoxarifadofiltro() {
		return almoxarifadofiltro;
	}

	public void setAlmoxarifadofiltro(String almoxarifadofiltro) {
		this.almoxarifadofiltro = almoxarifadofiltro;
	}

	public String getTipofiltro() {
		return tipofiltro;
	}

	public void setTipofiltro(String tipofiltro) {
		this.tipofiltro = tipofiltro;
	}

	public List<ProdutoEstoque> getListaproduto_estoque() {
		return listaproduto_estoque;
	}

	public void setListaproduto_estoque(List<ProdutoEstoque> listaproduto_estoque) {
		this.listaproduto_estoque = listaproduto_estoque;
	}
	
	
}
