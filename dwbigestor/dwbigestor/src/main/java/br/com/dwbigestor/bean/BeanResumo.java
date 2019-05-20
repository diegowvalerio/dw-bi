package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.classe.VendasEmGeral;
import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.servico.ServicoClientesNovos;
import br.com.dwbigestor.servico.ServicoVendasemGeral;

@Named
@ViewScoped
public class BeanResumo implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendasEmGeral vendasEmGeral = new VendasEmGeral();
	@Inject
	private ServicoVendasemGeral servico;
	private List<VendasEmGeral> listavenda = new ArrayList<>();
	
	private ClientesNovos clientesNovos = new ClientesNovos();
	@Inject
	private ServicoClientesNovos servicoclientes;
	private List<ClientesNovos> listaclientes = new ArrayList<>();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();

	@PostConstruct
	public void init() {
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if ((Date) session.getAttribute("data1") != null) {
			this.data_grafico = (Date) session.getAttribute("data1");
			this.data_grafico2 = (Date) session.getAttribute("data2");
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2);
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2);
		} else {			
			listavenda = servico.vendasemgeral(data_grafico, data_grafico2);
			listaclientes = servicoclientes.clientesnovos(data_grafico, data_grafico2);
		}

		session.removeAttribute("data1");
		session.removeAttribute("data2");

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

	public List<VendasEmGeral> getListavenda() {
		return listavenda;
	}

	public void setListavenda(List<VendasEmGeral> listavenda) {
		this.listavenda = listavenda;
	}

	public VendasEmGeral getVendasEmGeral() {
		return vendasEmGeral;
	}

	public void setVendasEmGeral(VendasEmGeral vendasEmGeral) {
		this.vendasEmGeral = vendasEmGeral;
	}


	public ClientesNovos getClientesNovos() {
		return clientesNovos;
	}

	public void setClientesNovos(ClientesNovos clientesNovos) {
		this.clientesNovos = clientesNovos;
	}

	public List<ClientesNovos> getListaclientes() {
		return listaclientes;
	}

	public void setListaclientes(List<ClientesNovos> listaclientes) {
		this.listaclientes = listaclientes;
	}

	/* dados vendaemgeral */
	public String encaminha2() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		// session.setAttribute("vendaGeral", this.vendasEmGeral);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/vendaemgeral/vendaemgeral.xhtml";
	}
	
	/* dados clientesnovos */
	public String encaminha3() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		return "/pages/relatorios/clientesnovos/clientesnovos.xhtml";
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

	public String getValorTotal() {
		float total = 0;

		for (VendasEmGeral venda : getListavenda()) {
			total = total + venda.getValortotalpedido().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}

	// painel de resumo

	public int getPedidododia() {
		int total = 0;

		for (VendasEmGeral venda : getListavenda()) {
			total++;
		}

		return total;
	}
	
	
	public int getTotalClientes() {
		int total = 0;

		for (ClientesNovos cliente : getListaclientes()) {
			total++;
		}

		return total;
	}
}
