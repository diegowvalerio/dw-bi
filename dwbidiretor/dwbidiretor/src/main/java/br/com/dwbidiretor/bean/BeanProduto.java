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

import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.servico.ServicoPerca;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.painel.ServicoPainel_Diretor_Vendagrupo;



@ManagedBean
@RequestScoped
public class BeanProduto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Produto> lista = new ArrayList<>();
	@Inject
	private ServicoProduto servico;
	
	//filtro grupo
	private Venda_Grupo grupo = new Venda_Grupo();
	private List<Venda_Grupo> listagrupos = new ArrayList<>();
	@Inject
	private ServicoPainel_Diretor_Vendagrupo servicogrupo;
		
		
	private Produto produto =  new Produto();
	private Imagem img = new Imagem();
	private byte[] imagem;
	private String grupofiltro;
	private StreamedContent productImage;
		
	public byte[] getImagem() {
		return imagem;
	}

	public void setImagem(byte[] imagem) {
		this.imagem = imagem;
	}

	public Imagem getImg() {
		return img;
	}

	public void setImg(Imagem img) {
		this.img = img;
	}

	
	public void setProductImage(StreamedContent productImage) {
		this.productImage = productImage;
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

	@PostConstruct
	public void init() {
		
		listagrupos = servicogrupo.grupos();
					
	}
	
	public void filtrar() throws InterruptedException{
		if (grupo == null){
			grupofiltro = "0";
		}else{
			grupofiltro = grupo.getIdgrupo().toString();
		}
		
		lista = servico.produtosgrupo(grupofiltro);
		
		/*for(Produto p:lista) {
			img = new Imagem();
			img = servico.imagem(p.getProdutoid().toString());
			p.setImg(img.getImg());
		}*/
			
	}
	
	/*public DefaultStreamedContent productImage(String produtoid) {
		DefaultStreamedContent dsc = null;
		InputStream is = null;
			try {

				img = new Imagem();
				img = servico.imagem(produtoid);
				is = new ByteArrayInputStream(img.getImg());								
				dsc = new DefaultStreamedContent(is, "image/jpeg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		return dsc;
	}
	*/
	
	
	public StreamedContent getProductImage() throws IOException, SQLException {

		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		}

		else {
			DefaultStreamedContent dsc = null;
			InputStream is = null;
			
			String id = context.getExternalContext().getRequestParameterMap().get("imgPRD");
			//System.out.println(id);
			img = new Imagem();
			img = servico.imagem(id);
			if(img.getImg() == null) {
				is = new FileInputStream("C:/imagem.png");
				dsc = new DefaultStreamedContent(is, "image/jpeg");
				return dsc;
			}else {
			is = new ByteArrayInputStream(img.getImg());
			dsc = new DefaultStreamedContent(is, "image/jpeg");
			return dsc;
			}
		}
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
	
	
}
