package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.servico.ServicoPedidoItem;


@Named
@ViewScoped
public class BeanPedidoItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private PedidoItem pedidoitem = new PedidoItem();
	@Inject
	private ServicoPedidoItem servico;
	private List<PedidoItem> listapedidoitem = new ArrayList<>();

	private String vendedorlogado;
	
	private String pedidofiltrado = "0";

	@PostConstruct
	public void init() {
	
	}
	
	public void filtrar(){
		if (pedidofiltrado == null){
			pedidofiltrado = "0";
			
		}else{
			BigDecimal p =  new BigDecimal(pedidofiltrado);
			listapedidoitem = servico.pedidoitem(p);
		}
		
	}

	
	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}
	
	public PedidoItem getPedidoitem() {
		return pedidoitem;
	}

	public void setPedidoitem(PedidoItem pedidoitem) {
		this.pedidoitem = pedidoitem;
	}

	public List<PedidoItem> getListapedidoitem() {
		return listapedidoitem;
	}

	public void setListapedidoitem(List<PedidoItem> listapedidoitem) {
		this.listapedidoitem = listapedidoitem;
	}

	public String getPedidofiltrado() {
		return pedidofiltrado;
	}

	public void setPedidofiltrado(String pedidofiltrado) {
		this.pedidofiltrado = pedidofiltrado;
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
