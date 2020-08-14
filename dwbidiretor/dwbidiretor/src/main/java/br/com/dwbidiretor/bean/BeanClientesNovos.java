package br.com.dwbidiretor.bean;

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

import org.omnifaces.el.ELContextWrapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoClientesNovos;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanClientesNovos implements Serializable {
	private static final long serialVersionUID = 1L;

	private ClientesNovos clientesNovos = new ClientesNovos();
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoClientesNovos servico;
	private List<ClientesNovos> lista = new ArrayList<>();

	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	
	//filtro gestor
	private Gestor gestor = new Gestor();
	@Inject
	private ServicoGestor servicogestor;
	private List<Gestor> listagestor = new ArrayList<>();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	private String clientefiltrado;
	private String clientefiltrado2;
	private int acumulatorAux;
	private int acumulatorAux2;

	@PostConstruct
	public void init() {
		
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
		//verifica filtro gestor
		if (session.getAttribute("gestor") != null){
			gestor = (Gestor) session.getAttribute("gestor");
			if (gestor == null){
				gestorfiltrado = "0";
				gestorfiltrado2 = "999999";
				
			}else{
				gestorfiltrado = gestor.getGestorid().toString();
				gestorfiltrado2 = gestor.getGestorid().toString();
			}
		}
		if (gestor.getGestorid() == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}//fim filtro gestor
		
		listavendedor = servicovendedor.consultavendedor();
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		
		if ((Date) session.getAttribute("data1") != null) {
			data_grafico = (Date) session.getAttribute("data1");
			data_grafico2 = (Date) session.getAttribute("data2");
			lista = servico.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);
		} else {			
			lista = servico.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);
		}

		session.removeAttribute("data1");
		session.removeAttribute("data2");
		session.removeAttribute("vendedor");
		session.removeAttribute("gestor");
		

	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		if (gestor == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}
		lista = servico.clientesnovos_efetivado(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);
	}
	
	//valor quebra por vendedor
	public void acumulateValue(int value)
    {
		if (value > 1) {
			value = 1;
		}
        acumulatorAux += value;
    }

	public int getAcumulatedValue() {
        int aux = acumulatorAux ;
        acumulatorAux = 0;
        return aux;
    }
	
	
	//2
	public void acumulateValue2(int value)
    {
		if (value > 1) {
			value = 1;
		}
        acumulatorAux2 += value;
    }

	public int getAcumulatedValue2() {
        int aux = acumulatorAux2 ;
        acumulatorAux2 = 0;
        return aux;
    }
	
	
	
	//fim
	
	public int getAcumulatorAux() {
		return acumulatorAux;
	}

	public int getAcumulatorAux2() {
		return acumulatorAux2;
	}

	public void setAcumulatorAux2(int acumulatorAux2) {
		this.acumulatorAux2 = acumulatorAux2;
	}

	public void setAcumulatorAux(int acumulatorAux) {
		this.acumulatorAux = acumulatorAux;
	}

	public String getClientefiltrado() {
		return clientefiltrado;
	}

	public void setClientefiltrado(String clientefiltrado) {
		this.clientefiltrado = clientefiltrado;
	}

	public String getClientefiltrado2() {
		return clientefiltrado2;
	}

	public void setClientefiltrado2(String clientefiltrado2) {
		this.clientefiltrado2 = clientefiltrado2;
	}

	public Gestor getGestor() {
		return gestor;
	}

	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	public List<Gestor> getListagestor() {
		return listagestor;
	}

	public void setListagestor(List<Gestor> listagestor) {
		this.listagestor = listagestor;
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
	
	public int getTotalClientes_Efetivado() {
		int total = 0;

		for (ClientesNovos cliente : getLista()) {
			if(cliente.getVendas().intValue() > 0) {
			total++;
			}
		}

		return total;
	}
	
	public void filtragestor() {
		if (getVendedor() != null){
			List<Gestor> listagestorf = new ArrayList<>();
			gestor = new Gestor();
			listagestorf = servicogestor.consultagestor(getVendedor().getCodigovendedor().toString(),getVendedor().getCodigovendedor().toString());
			gestor = listagestorf.get(0);
		}else{
			//listagestor = new ArrayList<>();
			gestor = new Gestor();
			//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		}
	}
}
