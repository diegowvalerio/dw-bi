package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.servico.ServicoPedidosConferidos;


@Named
@ViewScoped
public class BeanPedidosConferidos implements Serializable {
	private static final long serialVersionUID = 1L;

	private PedidosConferidos pedidosconferidos = new PedidosConferidos();
	@Inject
	private ServicoPedidosConferidos servico;
	private List<PedidosConferidos> lista = new ArrayList<>();

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();


	@PostConstruct
	public void init() {
		lista = servico.pedidosconferidos(data_grafico, data_grafico2);
	}
	
	public void filtrar(){
		lista = servico.pedidosconferidos(data_grafico, data_grafico2);
	}
	
	

	public PedidosConferidos getPedidosconferidos() {
		return pedidosconferidos;
	}

	public void setPedidosconferidos(PedidosConferidos pedidosconferidos) {
		this.pedidosconferidos = pedidosconferidos;
	}

	public List<PedidosConferidos> getLista() {
		return lista;
	}

	public void setLista(List<PedidosConferidos> lista) {
		this.lista = lista;
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
