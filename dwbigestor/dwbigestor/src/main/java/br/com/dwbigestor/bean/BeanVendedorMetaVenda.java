package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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

import br.com.dwbigestor.classe.VendedorMetaVenda;
import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.servico.ServicoVendedorMetaVenda;
import br.com.dwbigestor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanVendedorMetaVenda implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendedorMetaVenda vendedormetavenda = new VendedorMetaVenda();
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoVendedorMetaVenda servico;
	private List<VendedorMetaVenda> lista = new ArrayList<>();

	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;

	@PostConstruct
	public void init() {
		listavendedor = servicovendedor.consultavendedor();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if (session.getAttribute("vendedor") != null){
			this.vendedor = (Vendedor) session.getAttribute("vendedor");
			if (vendedor == null){
				vendedorfiltrado = "0";
				vendedorfiltrado2 = "999999";
				
			}else{
				vendedorfiltrado = vendedor.getCodigovendedor().toString();
				vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
			}
		}
		if (vendedor.getCodigovendedor() == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
				
		lista = servico.vendedormetavenda(vendedorfiltrado,vendedorfiltrado2);
		
		session.removeAttribute("vendedor");
		
		

	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		lista = servico.vendedormetavenda(vendedorfiltrado,vendedorfiltrado2);
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

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
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
	

	public VendedorMetaVenda getVendedormetavenda() {
		return vendedormetavenda;
	}

	public void setVendedormetavenda(VendedorMetaVenda vendedormetavenda) {
		this.vendedormetavenda = vendedormetavenda;
	}

	public List<VendedorMetaVenda> getLista() {
		return lista;
	}

	public void setLista(List<VendedorMetaVenda> lista) {
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
	
	public float getMetaAtingida() {
		float atingido = 0;
		
		NumberFormat formatarFloat= new DecimalFormat("0.00");
		formatarFloat.setMaximumFractionDigits(2);
		
		
		atingido = (vendedormetavenda.getValorvenda().floatValue() / vendedormetavenda.getValormeta().floatValue())*100;
		return Float.parseFloat(formatarFloat.format(atingido).replace(",", "."));
	}

}
