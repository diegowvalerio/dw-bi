package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.classe.Etapa;
import br.com.dwbidiretor.msg.FacesMessageUtil;
import br.com.dwbidiretor.servico.ServicoCPedido;
import br.com.dwbidiretor.servico.ServicoCPedidoLog;


@Named
@ViewScoped
public class BeanLiberacaoPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private CPedido cpedido = new CPedido();
	private List<CPedido> lista = new ArrayList<>();
	@Inject
	private ServicoCPedido servico;
	private String pedido;
	
	private CPedidoLog clog = new CPedidoLog();
	private List<CPedidoLog> logs = new ArrayList<>();
	@Inject
	private ServicoCPedidoLog servicolog;
	private Date data;
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	private String status ;
	@PostConstruct
	public void init() {	
		data = new Date();

	}
	
	public void filtrar(){
		
		BigDecimal p = new BigDecimal(pedido);
		lista.clear();
		lista = servico.cpedido(p);	
		logs = servicolog.cpedidolog(p.toString());
	}
	
public void filtrar2(){
	
	if(status != null) {
		lista.clear();
		lista = servico.cpedidoLista(data_grafico, data_grafico2,status);
	}else {
		FacesMessageUtil.addMensagemWarn("Selecione o Status desejado !");
		//System.out.println(status);
	}

	}
	
	public String salvar(){
		try {
			servicolog.Ssalvar(clog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "liberacaopedido";
	}
	
	
	public void addLog() {
		clog = new CPedidoLog();
		clog.setUsuario(usuarioconectado());
		clog.setPedido(pedido);
		clog.setData(new Date());
		
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public CPedido getCpedido() {
		return cpedido;
	}

	public void setCpedido(CPedido cpedido) {
		this.cpedido = cpedido;
	}

	public List<CPedido> getLista() {
		return lista;
	}

	public void setLista(List<CPedido> lista) {
		this.lista = lista;
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	public CPedidoLog getClog() {
		return clog;
	}

	public void setClog(CPedidoLog clog) {
		this.clog = clog;
	}

	public List<CPedidoLog> getLogs() {
		return logs;
	}

	public void setLogs(List<CPedidoLog> logs) {
		this.logs = logs;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	
	
}
