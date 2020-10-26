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
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoRetornoAfinacao;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanRetornoAfinacao implements Serializable {
	private static final long serialVersionUID = 1L;

	private RetornoAfinacao retornoafinacao = new RetornoAfinacao();
	@Inject
	private ServicoRetornoAfinacao servico;
	private List<RetornoAfinacao> lista = new ArrayList<>();


	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	private String cfop;

	@PostConstruct
	public void init() {
		

	}
	
	public void filtrar(){
		if (!cfop.equals("")) {
		lista = servico.retornoafinacao(data_grafico, data_grafico2,cfop);
		}
	}

	public RetornoAfinacao getRetornoafinacao() {
		return retornoafinacao;
	}

	public void setRetornoafinacao(RetornoAfinacao retornoafinacao) {
		this.retornoafinacao = retornoafinacao;
	}

	public List<RetornoAfinacao> getLista() {
		return lista;
	}

	public void setLista(List<RetornoAfinacao> lista) {
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

	public String getCfop() {
		return cfop;
	}

	public void setCfop(String cfop) {
		this.cfop = cfop;
	}
	
	
}
