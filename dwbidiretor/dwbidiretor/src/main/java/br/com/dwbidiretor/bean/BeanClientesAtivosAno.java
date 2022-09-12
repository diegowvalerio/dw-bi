package br.com.dwbidiretor.bean;

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

import org.omnifaces.el.ELContextWrapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoClientesAtivosAno;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanClientesAtivosAno implements Serializable {
	private static final long serialVersionUID = 1L;

	private ClientesAtivosAno clientesAtivosAno = new ClientesAtivosAno();
	private Vendedor vendedor = new Vendedor();
	@Inject
	private ServicoClientesAtivosAno servico;
	private List<ClientesAtivosAno> lista = new ArrayList<>();

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
	private String ano;

	@PostConstruct
	public void init() {
		
		vendedorfiltrado = "0";
		vendedorfiltrado2 = "999999";
	
		gestorfiltrado = "0";
		gestorfiltrado2 = "999999";
				
		
		clientefiltrado = "0";
		clientefiltrado2 = "999999";
				
		listavendedor = servicovendedor.consultavendedor();
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(data_grafico);
		ano = ""+cal.get(Calendar.YEAR);

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
		
		
		lista = servico.clientesativosano(vendedorfiltrado, vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, ano);
	}
	

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public ClientesAtivosAno getClientesAtivosAno() {
		return clientesAtivosAno;
	}

	public void setClientesAtivosAno(ClientesAtivosAno clientesAtivosAno) {
		this.clientesAtivosAno = clientesAtivosAno;
	}

	public List<ClientesAtivosAno> getLista() {
		return lista;
	}

	public void setLista(List<ClientesAtivosAno> lista) {
		this.lista = lista;
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
