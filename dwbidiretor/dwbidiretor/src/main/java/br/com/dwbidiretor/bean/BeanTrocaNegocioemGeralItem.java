package br.com.dwbidiretor.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoVendasemGeralItem;

@Named
@ViewScoped
public class BeanTrocaNegocioemGeralItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendasEmGeral vendasEmGeral = new VendasEmGeral();
	private VendasEmGeralItem vendasEmGeralitem = new VendasEmGeralItem();
	@Inject
	private ServicoVendasemGeralItem servico;
	private List<VendasEmGeralItem> listavendaitems = new ArrayList<>();
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();

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

		this.listavendaitems = servico.trocanegocioemgeralitem(this.vendasEmGeral.getPedido());

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
		// System.out.println(nome);
		return nome;
	}

	/* voltar para vendaemgeral */
	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
	}
	
	public String encaminha() {

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("data1", this.data_grafico);
		session.setAttribute("data2", this.data_grafico2);

		if (!session.getAttribute("clientefiltrado").equals("0")){
			listacliente = completaCliente(vendasEmGeral.getNomecliente().toString());
			this.cliente = listacliente.get(0);
			session.setAttribute("cliente", this.cliente);
		}

		return "trocanegocioemgeral";
	}

	public String getValorTotal() {
		float total = 0;

		for (VendasEmGeralItem venda : getListavendaitems()) {
			total = total + venda.getValortotalproduto().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}

	public String getQtdeTotal() {
		int total = 0;

		for (VendasEmGeralItem venda : getListavendaitems()) {
			total = total + venda.getQuantidadeproduto().intValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}

	public VendasEmGeralItem getVendasEmGeralitem() {
		return vendasEmGeralitem;
	}

	public void setVendasEmGeralitem(VendasEmGeralItem vendasEmGeralitem) {
		this.vendasEmGeralitem = vendasEmGeralitem;
	}

	public void enviaitem() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("item", this.vendasEmGeralitem);

	}

	public DefaultStreamedContent imagem() {
		DefaultStreamedContent dsc = null;
		InputStream is = null;
		if (vendasEmGeralitem.getImagem() != null) {
			System.out.println(vendasEmGeralitem.getNomeproduto());
			try {

				is = vendasEmGeralitem.getImagem().getBinaryStream(); 								
				dsc = new DefaultStreamedContent(is, "image/jpeg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dsc;
	}

}
