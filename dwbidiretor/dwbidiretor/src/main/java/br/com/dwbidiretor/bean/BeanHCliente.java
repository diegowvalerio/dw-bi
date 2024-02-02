package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoHCliente;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanHCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private HCliente hcliente = new HCliente();
	private Vendedor vendedor = new Vendedor();
	private Vendedor vendedor2 = new Vendedor();
	@Inject
	private ServicoHCliente servico;
	private List<HCliente> lista = new ArrayList<>();

	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	
	//filtro gestor
	private Gestor gestor = new Gestor();
	@Inject
	private ServicoGestor servicogestor;
	private List<Gestor> listagestor = new ArrayList<>();
	
	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();

	private String vendedorlogado;
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	private String clientefiltrado;
	private String clientefiltrado2;
	private String uf;
	private String regiao;
	private String vendedorfiltrado3;
	private String status;
	
	//totais
	private int ativo,inativo,semiativo,critico,total = 0;

	@PostConstruct
	public void init() {
		status = "1";
		
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
		
		//verifica filtro cliente
		if (session.getAttribute("cliente") != null){
			cliente = (Cliente) session.getAttribute("cliente");
			if (cliente == null){
				clientefiltrado = "0";
				clientefiltrado2 = "999999";
						
			}else{
				clientefiltrado = cliente.getCodigocliente().toString();
				clientefiltrado2 = cliente.getCodigocliente().toString();
				}
		}
		
		if (cliente.getCodigocliente() == null){
				clientefiltrado = "0";
				clientefiltrado2 = "999999";
					
		}else{
				clientefiltrado = cliente.getCodigocliente().toString();
				clientefiltrado2 = cliente.getCodigocliente().toString();
		}//fim filtro cliente
		
		listavendedor = servicovendedor.consultavendedor();
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		
	
		//lista = servico.hclientes(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);
		
		session.removeAttribute("vendedor");
		session.removeAttribute("gestor");
		session.removeAttribute("cliente");
		

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
		
		if (cliente == null){
			clientefiltrado = "0";
			clientefiltrado2 = "999999";
			
		}else{
			clientefiltrado = cliente.getCodigocliente().toString();
			clientefiltrado2 = cliente.getCodigocliente().toString();
		}
		
		if (vendedor2 == null){
			vendedorfiltrado3 = "0";
			
		}else{
			vendedorfiltrado3 = vendedor2.getCodigovendedor().toString();
		}
		
		lista = servico.hclientes(vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2, uf, regiao,vendedorfiltrado3,status);
		
		calculatotais();
	}
	
	private void calculatotais() {
		ativo = 0;
		inativo =0;
		semiativo = 0;
		critico = 0;
		total = 0;
		if(lista.size()>0) {
			for(HCliente c:lista) {
				if(c.getStatus().equals("ATIVO")) {
					ativo++;
				}
				if(c.getStatus().equals("INATIVO")) {
					inativo++;
				}
				if(c.getStatus().equals("SEMI-ATIVO")) {
					semiativo++;
				}
				if(c.getStatus().equals("CRITICO")) {
					critico++;
				}
				total++;
			}
		}
	}
	
	public Vendedor getVendedor2() {
		return vendedor2;
	}

	public void setVendedor2(Vendedor vendedor2) {
		this.vendedor2 = vendedor2;
	}

	public String getVendedorfiltrado3() {
		return vendedorfiltrado3;
	}

	public void setVendedorfiltrado3(String vendedorfiltrado3) {
		this.vendedorfiltrado3 = vendedorfiltrado3;
	}
	
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAtivo() {
		return ativo;
	}

	public void setAtivo(int ativo) {
		this.ativo = ativo;
	}

	public int getInativo() {
		return inativo;
	}

	public void setInativo(int inativo) {
		this.inativo = inativo;
	}

	public int getSemiativo() {
		return semiativo;
	}

	public void setSemiativo(int semiativo) {
		this.semiativo = semiativo;
	}

	public int getCritico() {
		return critico;
	}

	public void setCritico(int critico) {
		this.critico = critico;
	}

	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
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

	public HCliente getHcliente() {
		return hcliente;
	}

	public void setHcliente(HCliente hcliente) {
		this.hcliente = hcliente;
	}

	public List<HCliente> getLista() {
		return lista;
	}

	public void setLista(List<HCliente> lista) {
		this.lista = lista;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Cliente> getListacliente() {
		return listacliente;
	}

	public void setListacliente(List<Cliente> listacliente) {
		this.listacliente = listacliente;
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
			if(listagestorf.size()>0) {
				gestor = listagestorf.get(0);
			}
		}else{
			//listagestor = new ArrayList<>();
			gestor = new Gestor();
			//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		}
	}
}
