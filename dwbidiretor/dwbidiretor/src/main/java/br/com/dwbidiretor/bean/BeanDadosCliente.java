package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoDadosCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanDadosCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private DadosCliente dadoscliente = new DadosCliente();
	@Inject
	private ServicoDadosCliente servico;
	private List<DadosCliente> listavenda = new ArrayList<>();
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	
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

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	private String clientefiltrado;
	private String clientefiltrado2;

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
					
		//listavenda = servico.dadoscliente(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);

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
		listavenda = servico.dadoscliente(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2, clientefiltrado, clientefiltrado2);
	}
	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
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
	
	public DadosCliente getDadoscliente() {
		return dadoscliente;
	}

	public void setDadoscliente(DadosCliente dadoscliente) {
		this.dadoscliente = dadoscliente;
	}

	public List<DadosCliente> getListavenda() {
		return listavenda;
	}

	public void setListavenda(List<DadosCliente> listavenda) {
		this.listavenda = listavenda;
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
