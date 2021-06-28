package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanPainel_Diretor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String ano,mes;
	private Date  horaatual = new Date();
	
	@PostConstruct
	public void init() {
		horaatual = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		
		mes = data.substring(3,5);
		ano = data.substring(6,10);
		
	}
	
	public void filtrar(){
		
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}
	
	
}
