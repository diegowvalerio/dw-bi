package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.PT_Meta;
import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCidadeVenda;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoPT_Meta;
import br.com.dwbidiretor.servico.ServicoTLOcorrencia;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanPT_Meta implements Serializable {
	private static final long serialVersionUID = 1L;

	private PT_Meta ptmeta = new PT_Meta();
	@Inject
	private ServicoPT_Meta servico;
	private List<PT_Meta> lista = new ArrayList<>();
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	
	private String vendedorlogado;
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String regiao;

	@PostConstruct
	public void init() {
		listavendedor = servicovendedor.consultavendedor();	
	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		
		lista = servico.pt_meta(regiao, vendedorfiltrado, vendedorfiltrado2);
	}

	public PT_Meta getPtmeta() {
		return ptmeta;
	}

	public void setPtmeta(PT_Meta ptmeta) {
		this.ptmeta = ptmeta;
	}

	public List<PT_Meta> getLista() {
		return lista;
	}

	public void setLista(List<PT_Meta> lista) {
		this.lista = lista;
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

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	
}
