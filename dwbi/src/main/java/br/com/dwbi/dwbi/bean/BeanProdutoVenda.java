package br.com.dwbi.dwbi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbi.classe.Cliente;
import br.com.dwbi.classe.Produto;
import br.com.dwbi.classe.ProdutoVenda;
import br.com.dwbi.dwbi.servico.ServicoCliente;
import br.com.dwbi.dwbi.servico.ServicoProduto;
import br.com.dwbi.dwbi.servico.ServicoProdutoVenda;

@Named
@ViewScoped
public class BeanProdutoVenda implements Serializable {
	private static final long serialVersionUID = 1L;

	private ProdutoVenda produtovenda = new ProdutoVenda();
	@Inject
	private ServicoProdutoVenda servico;
	private List<ProdutoVenda> lista = new ArrayList<>();
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();
	
	@Inject
	private ServicoProduto servicoproduto;
	private Produto produto = new Produto();
	private List<Produto> produtos = new ArrayList<>();
	private List<Produto> produtosselecionados = new ArrayList<>();

	private String vendedorlogado;
	private String produtofiltrado ="";
	private String clientefiltrado;
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();


	@PostConstruct
	public void init() {
		//produtos = servicoproduto.produtos();
		listacliente = servicocliente.clientes();
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		
		//produtofiltrado = "1";			
		clientefiltrado = "1";		

	}
	
	public void filtrar(){
		/*if(produtosselecionados.isEmpty()) {
			produtofiltrado = "1";
		}else {
			produtofiltrado = "";
		}
		
		for(Produto p:produtosselecionados) {
			produtofiltrado = produtofiltrado+p.getProdutoid()+",";
		}
		if(!produtofiltrado.equals("1")) {
			produtofiltrado = produtofiltrado.replaceFirst(".$", "");
		}*/
		
		if(produtofiltrado.equals("") || produtofiltrado == null) {
			produtofiltrado = "1";
		}
				
		if (cliente == null){
			clientefiltrado = "1";
			
		}else{
			clientefiltrado = cliente.getCodigocliente().toString();
		}
		
		lista = servico.produtos_venda(data_grafico, data_grafico2, produtofiltrado, clientefiltrado);
	}
	

	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
	}
	
	public String getClientefiltrado() {
		return clientefiltrado;
	}

	public void setClientefiltrado(String clientefiltrado) {
		this.clientefiltrado = clientefiltrado;
	}

	

	public ProdutoVenda getProdutovenda() {
		return produtovenda;
	}

	public void setProdutovenda(ProdutoVenda produtovenda) {
		this.produtovenda = produtovenda;
	}

	public List<ProdutoVenda> getLista() {
		return lista;
	}

	public void setLista(List<ProdutoVenda> lista) {
		this.lista = lista;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListacliente() {
		return listacliente;
	}

	public void setListacliente(List<Cliente> listacliente) {
		this.listacliente = listacliente;
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

	public List<Produto> getProdutosselecionados() {
		return produtosselecionados;
	}

	public void setProdutosselecionados(List<Produto> produtosselecionados) {
		this.produtosselecionados = produtosselecionados;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}

	public String getProdutofiltrado() {
		return produtofiltrado;
	}

	public void setProdutofiltrado(String produtofiltrado) {
		this.produtofiltrado = produtofiltrado;
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
