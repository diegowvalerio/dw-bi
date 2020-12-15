package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
	private static final Locale LOCAL = new Locale("pt","BR");
	
	private RetornoAfinacao retornoafinacao = new RetornoAfinacao();
	@Inject
	private ServicoRetornoAfinacao servico;
	private List<RetornoAfinacao> lista = new ArrayList<>();
	private List<RetornoAfinacao> lista2 = new ArrayList<>();

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	private String cfop;
	private double vlrnota = 0;
	private double total = 0;

	@PostConstruct
	public void init() {
		lista2 = servico.consulta_relacao();

	}

	public void filtrar() {
		DecimalFormat f = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(LOCAL));
		
		total = 0;
		
		if (!cfop.equals("")) {
			lista = servico.retornoafinacao(data_grafico, data_grafico2, cfop);

			if (lista.size() > 0 && vlrnota > 0) {
				for (RetornoAfinacao r : lista) {
					if (r.getValortotal_servicoafinado() != null && !r.getNomeproduto_cromado().equals("")) {
						total = total + r.getValortotal_servicoafinado().doubleValue();
					}
				}

				for (RetornoAfinacao r : lista) {
					if (r.getValortotal_servicoafinado() != null
							&& !r.getNomeproduto_cromado().equals("")) {
						
						r.setVlrservico_cromado((r.getValor_servicoafinado().doubleValue() / total) * vlrnota);
						r.setVlrtotalservico_cromado(r.getQtde_cromado().doubleValue()*r.getVlrservico_cromado());
					}
				}
			}
		}
	}

	public void excluir() {
		servico.excluir(retornoafinacao);
	}
	
	public String salvar() {
		servico.salvar(retornoafinacao);
		
		return "lista";
	}
	
	public void alterar(){
		servico.alterar(retornoafinacao);
	}
	
	
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
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

	public double getVlrnota() {
		return vlrnota;
	}

	public void setVlrnota(double vlrnota) {
		this.vlrnota = vlrnota;
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

	public List<RetornoAfinacao> getLista2() {
		return lista2;
	}

	public void setLista2(List<RetornoAfinacao> lista2) {
		this.lista2 = lista2;
	}

}
