package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.dwbidiretor.classe.TabProduto;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoTabProduto;


@Named
@ViewScoped
public class BeanTabProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	private TabProduto tabproduto = new TabProduto();
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoTabProduto servico;
	private List<TabProduto> lista = new ArrayList<>();
	
	private String vendedorlogado;
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	
	private Integer filtra = 0;
	private boolean filtro;

	@PostConstruct
	public void init() {
	
	}
	
	public void filtrar(){
		lista = servico.tabproduto("182");
		if(lista.size()>0) {
			tabproduto = lista.get(0);
		}
	}
	

	public TabProduto getTabproduto() {
		return tabproduto;
	}

	public void setTabproduto(TabProduto tabproduto) {
		this.tabproduto = tabproduto;
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

	public TabProduto getCidadevenda() {
		return tabproduto;
	}

	public void setCidadevenda(TabProduto tabproduto) {
		this.tabproduto = tabproduto;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<TabProduto> getLista() {
		return lista;
	}

	public void setLista(List<TabProduto> lista) {
		this.lista = lista;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}

	public String getVendedorfiltrado() {
		return vendedorfiltrado;
	}

	public void setVendedorfiltrado(String vendedorfiltrado) {
		this.vendedorfiltrado = vendedorfiltrado;
	}

	public String getVendedorfiltrado2() {
		return vendedorfiltrado2;
	}

	public void setVendedorfiltrado2(String vendedorfiltrado2) {
		this.vendedorfiltrado2 = vendedorfiltrado2;
	}

	public String getGestorfiltrado() {
		return gestorfiltrado;
	}

	public void setGestorfiltrado(String gestorfiltrado) {
		this.gestorfiltrado = gestorfiltrado;
	}

	public String getGestorfiltrado2() {
		return gestorfiltrado2;
	}

	public void setGestorfiltrado2(String gestorfiltrado2) {
		this.gestorfiltrado2 = gestorfiltrado2;
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
