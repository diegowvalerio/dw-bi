package br.com.dwbigestor.bean;

import java.io.Serializable;
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

import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.servico.ServicoClientesNovos;


@Named
@ViewScoped
public class BeanClientesNovos implements Serializable {
	private static final long serialVersionUID = 1L;

	private ClientesNovos clientesNovos = new ClientesNovos();
	@Inject
	private ServicoClientesNovos servico;
	private List<ClientesNovos> lista = new ArrayList<>();


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
			lista = servico.clientesnovos(data_grafico, data_grafico2);
		} else {			
			lista = servico.clientesnovos(data_grafico, data_grafico2);
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
	

	public ClientesNovos getClientesNovos() {
		return clientesNovos;
	}

	public void setClientesNovos(ClientesNovos clientesNovos) {
		this.clientesNovos = clientesNovos;
	}

	public List<ClientesNovos> getLista() {
		return lista;
	}

	public void setLista(List<ClientesNovos> lista) {
		this.lista = lista;
	}

	/* dados clientesnovos */
	public String encaminha2() {
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

	// painel de resumo

	public int getTotalClientes() {
		int total = 0;

		for (ClientesNovos cliente : getLista()) {
			total++;
		}

		return total;
	}
}
