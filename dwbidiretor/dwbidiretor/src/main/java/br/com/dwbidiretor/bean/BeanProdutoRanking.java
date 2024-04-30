package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.UnselectEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Categoria;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ProdutoRanking;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCategoria;
import br.com.dwbidiretor.servico.ServicoProduto;
import br.com.dwbidiretor.servico.ServicoProdutoRanking;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanProdutoRanking implements Serializable {
	private static final long serialVersionUID = 1L;

	private ProdutoRanking produtoranking = new ProdutoRanking();

	@Inject
	private ServicoProdutoRanking servico;
	private List<ProdutoRanking> lista = new ArrayList<>();
	
	//filtro produto
	@Inject
	private ServicoProduto servicoproduto;
	private Produto produto = new Produto();
	private List<Produto> produtos = new ArrayList<>();
	private List<Produto> produtosselecionados = new ArrayList<>();	
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	
	@Inject
	private ServicoCategoria servicoCategoria;
	private List<Categoria> categorias = new ArrayList<>();
	private Categoria categoria = new Categoria();
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private Integer filtra = 0;
	private boolean filtro;
	
	private String filtroproduto;
	private String filtrovendedor;
	private String filtrocategoria;

	@PostConstruct
	public void init() {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		produtos = servicoproduto.produtos();
		listavendedor = servicovendedor.consultavendedor();
		
		categorias = servicoCategoria.categorias();
		
		filtroproduto = "1";
		filtrovendedor = "-1" ;
		filtrocategoria = "-1";
	}
	
	public void filtrar(){
		if(produtosselecionados.isEmpty()) {
			filtroproduto = "1";
		}else {
			filtroproduto = "";
		}
		
		if (vendedor == null){
			filtrovendedor = "-1" ;			
		}else {
			filtrovendedor = vendedor.getCodigovendedor().toString();
		}
		
		if(categoria == null) {
			filtrocategoria = "-1";
		}else {
			filtrocategoria = categoria.getCategoriaid().toString();
		}
		
		
		for(Produto p:produtosselecionados) {
			filtroproduto = filtroproduto+p.getProdutoid()+",";
		}
		if(!filtroproduto.equals("1")) {
			filtroproduto = filtroproduto.replaceFirst(".$", "");
		}
		
		lista = servico.produtoranking(data_grafico, data_grafico2, filtrovendedor, filtroproduto, filtrocategoria);
	
	}

	public String gettotalqtde() {
		float total = 0;

		for (ProdutoRanking p : getLista()) {
			total = total + p.getQtde().intValue();
		}
		return ""+total;
	}
	
	public String gettotalvalor() {
		float t = 0;

		for (ProdutoRanking p : getLista()) {
			t = t + p.getVenda().floatValue();
		}
		
		return NumberFormat.getCurrencyInstance().format(t);
	}
	 
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getFiltrocategoria() {
		return filtrocategoria;
	}

	public void setFiltrocategoria(String filtrocategoria) {
		this.filtrocategoria = filtrocategoria;
	}

	public ProdutoRanking getProdutoranking() {
		return produtoranking;
	}

	public void setProdutoranking(ProdutoRanking produtoranking) {
		this.produtoranking = produtoranking;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Produto> getProdutosselecionados() {
		return produtosselecionados;
	}

	public List<Vendedor> getListavendedor() {
		return listavendedor;
	}

	public void setListavendedor(List<Vendedor> listavendedor) {
		this.listavendedor = listavendedor;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public String getFiltrovendedor() {
		return filtrovendedor;
	}

	public void setFiltrovendedor(String filtrovendedor) {
		this.filtrovendedor = filtrovendedor;
	}

	public String getFiltroproduto() {
		return filtroproduto;
	}

	public void setFiltroproduto(String filtroproduto) {
		this.filtroproduto = filtroproduto;
	}

	public void setProdutosselecionados(List<Produto> produtosselecionados) {
		this.produtosselecionados = produtosselecionados;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFiltra() {
		return filtra;
	}

	public void setFiltra(Integer filtra) {
		this.filtra = filtra;
	}

	public boolean isFiltro() {
		return filtro;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}


	public ProdutoRanking getVendacusto() {
		return produtoranking;
	}

	public void setVendacusto(ProdutoRanking produtoranking) {
		this.produtoranking = produtoranking;
	}

	public List<ProdutoRanking> getLista() {
		return lista;
	}

	public void setLista(List<ProdutoRanking> lista) {
		this.lista = lista;
	}

	/* pegar usuario conectado */
	public String usuarioconectado() {
		String nome;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		// System.out.println(nome);
		return nome;
	}
	
}
