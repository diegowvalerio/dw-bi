package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.servico.ServicoAnaliseClientePedido;
import br.com.dwbidiretor.servico.ServicoCliente;



@Named
@ViewScoped
public class BeanAnaliseClientePedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private AnaliseClientePedido analiseClientePedido = new AnaliseClientePedido();
	@Inject
	private ServicoAnaliseClientePedido servico;
	private List<AnaliseClientePedido> listaAnaliseClientePedido = new ArrayList<>();
	
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();
	private Cliente cliente = new Cliente();

	private String vendedorlogado;
	
	private String pedidofiltrado = "0";
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	public void filtrar(){
	 if (cliente != null){
		if (pedidofiltrado == null){
			pedidofiltrado = "0";
			
		}else{
			BigDecimal p =  new BigDecimal(pedidofiltrado);
			listaAnaliseClientePedido = servico.analiseClientePedidos(data_grafico, data_grafico2, cliente.getCodigocliente(),cliente.getCpfcnpj(), p);
			if(listaAnaliseClientePedido.size() > 0){
			analiseClientePedido = listaAnaliseClientePedido.get(0);
			float tvenda = 0; float tamosta =0; float tamostrapaga=0;
			float tbonificacao =0; float tbonificacaoexpositor =0; float texpositor =0; float tbrinde=0; float ttroca=0; float tnegocios = 0;
			for (AnaliseClientePedido venda : listaAnaliseClientePedido) {
				tvenda = tvenda + venda.getVlvenda().floatValue();
				tamosta = tamosta + venda.getVlamostra().floatValue();
				tamostrapaga = tamostrapaga + venda.getVlamostrapaga().floatValue();
				tbonificacao = tbonificacao + venda.getVlbonificacao().floatValue();
				tbonificacaoexpositor = tbonificacaoexpositor + venda.getVlbonificacaoexpositor().floatValue();
				texpositor = texpositor + venda.getVlexpositor().floatValue();
				tbrinde = tbrinde + venda.getVlbrinde().floatValue();
				ttroca = ttroca + venda.getVltroca().floatValue();
				tnegocios = tnegocios + venda.getVlnegociacoescomerciais().floatValue();
				}
			analiseClientePedido.setAcvlvenda(new BigDecimal(tvenda));
			analiseClientePedido.setAcvlamostra(new BigDecimal(tamosta));
			analiseClientePedido.setAcvlamostrapaga(new BigDecimal(tamostrapaga));
			analiseClientePedido.setAcvlbonificacao(new BigDecimal(tbonificacao));
			analiseClientePedido.setAcvlbonificacaoexpositor(new BigDecimal(tbonificacaoexpositor));
			analiseClientePedido.setAcvlexpositor(new BigDecimal(texpositor));
			analiseClientePedido.setAcvlbrinde(new BigDecimal(tbrinde));
			analiseClientePedido.setAcvltroca(new BigDecimal(ttroca));
			analiseClientePedido.setAcvlnegociacoescomerciais(new BigDecimal(tnegocios));
			if(tvenda == 0)tvenda =1;
			analiseClientePedido.setPcamostra(new BigDecimal((tamosta/tvenda)*100));
			analiseClientePedido.setPcamostrapaga(new BigDecimal((tamostrapaga/tvenda)*100));
			analiseClientePedido.setPcbonificacao(new BigDecimal((tbonificacao/tvenda)*100));
			analiseClientePedido.setPcbonificacaoexpositor(new BigDecimal((tbonificacaoexpositor/tvenda)*100));
			analiseClientePedido.setPcexpositor(new BigDecimal((texpositor/tvenda)*100));
			analiseClientePedido.setPcbrinde(new BigDecimal((tbrinde/tvenda)*100));
			analiseClientePedido.setPctroca(new BigDecimal((ttroca/tvenda)*100));
			analiseClientePedido.setPcnegociacoescomerciais(new BigDecimal((tnegocios/tvenda)*100));
			}
		}
	 }
		
	}
	
	public List<Cliente> completaCliente(String nome) {
		String n = nome.toUpperCase();
		return servicocliente.consultacliente(n);
	}
	
	public String getVlVendaTotal() {
		float total = 0;

		for (AnaliseClientePedido venda : getListaAnaliseClientePedido()) {
			total = total + venda.getVlvenda().floatValue();
		}

		return new DecimalFormat("###,###.###").format(total);
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
	}
	
	public String getPedidofiltrado() {
		return pedidofiltrado;
	}

	public void setPedidofiltrado(String pedidofiltrado) {
		this.pedidofiltrado = pedidofiltrado;
	}

	public AnaliseClientePedido getAnaliseClientePedido() {
		return analiseClientePedido;
	}

	public void setAnaliseClientePedido(AnaliseClientePedido analiseClientePedido) {
		this.analiseClientePedido = analiseClientePedido;
	}

	public List<AnaliseClientePedido> getListaAnaliseClientePedido() {
		return listaAnaliseClientePedido;
	}

	public void setListaAnaliseClientePedido(List<AnaliseClientePedido> listaAnaliseClientePedido) {
		this.listaAnaliseClientePedido = listaAnaliseClientePedido;
	}

	public List<Cliente> getListacliente() {
		return listacliente;
	}

	public void setListacliente(List<Cliente> listacliente) {
		this.listacliente = listacliente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

}
