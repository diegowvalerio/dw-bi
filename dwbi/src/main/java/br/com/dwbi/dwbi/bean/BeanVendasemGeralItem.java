package br.com.dwbi.dwbi.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
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

import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.dwbi.servico.ServicoVendasemGeralItem;

@Named
@ViewScoped
public class BeanVendasemGeralItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendasEmGeral vendasEmGeral = new VendasEmGeral();
	@Inject
	private ServicoVendasemGeralItem servico;
	private List<VendasEmGeralItem> listavendaitems = new ArrayList<>();

	private String vendedorlogado;
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();

	@PostConstruct
	public void init() {
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		this.data_grafico = (Date) session.getAttribute("data1");
		this.data_grafico2 = (Date) session.getAttribute("data2");
		this.vendasEmGeral = (VendasEmGeral) session.getAttribute("vendaGeral");
		session.removeAttribute("vendaGeral");
		session.removeAttribute("data1");
		session.removeAttribute("data2");
				
		this.listavendaitems = servico.vendasemgeralitem(this.vendasEmGeral.getPedido(),this.vendasEmGeral.getOrigem());
		

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
	
	public VendasEmGeral getVendasEmGeral() {
		return vendasEmGeral;
	}

	public void setVendasEmGeral(VendasEmGeral vendasEmGeral) {
		this.vendasEmGeral = vendasEmGeral;
	}

	public List<VendasEmGeralItem> getListavendaitems() {
		return listavendaitems;
	}

	public void setListavendaitems(List<VendasEmGeralItem> listavendaitems) {
		this.listavendaitems = listavendaitems;
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
		//System.out.println(nome);
		return nome;
	}
	
	/* voltar para vendaemgeral */
	public String encaminha() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "vendaemgeral";
	}
	
	public String getValorTotal() {
        float total = 0;
 
        for(VendasEmGeralItem venda : getListavendaitems()){
            total= total + venda.getValortotalproduto().floatValue();
        }
 
        return new DecimalFormat("###,###.###").format(total);
    }

	public String getQtdeTotal() {
        int total = 0;
 
        for(VendasEmGeralItem venda : getListavendaitems()){
            total= total + venda.getQuantidadeproduto().intValue();
        }
 
        return new DecimalFormat("###,###.###").format(total);
    }

}
