package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCidadeVenda;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoTLOcorrencia;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanTLOcorrencias implements Serializable {
	private static final long serialVersionUID = 1L;

	private TLOcorrencia tlocorrencia = new TLOcorrencia();
	@Inject
	private ServicoTLOcorrencia servico;
	private List<TLOcorrencia> lista = new ArrayList<>();

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	private String criador = "TODOS";
	private String status = "TODOS";
	private String tipo = "TODOS";
	
	private Date data_grafico3 = new Date();
	private Date data_grafico4 = new Date();

	@PostConstruct
	public void init() {
	
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		data_grafico3 =c.getTime();
				
	}
	
	public void filtrar(){
		lista = servico.tlocorrencias(criador, data_grafico, data_grafico2, status,tipo,data_grafico3,data_grafico4);
	}

	public Date getData_grafico3() {
		return data_grafico3;
	}

	public void setData_grafico3(Date data_grafico3) {
		this.data_grafico3 = data_grafico3;
	}

	public Date getData_grafico4() {
		return data_grafico4;
	}

	public void setData_grafico4(Date data_grafico4) {
		this.data_grafico4 = data_grafico4;
	}

	public TLOcorrencia getTlocorrencia() {
		return tlocorrencia;
	}

	public void setTlocorrencia(TLOcorrencia tlocorrencia) {
		this.tlocorrencia = tlocorrencia;
	}

	public List<TLOcorrencia> getLista() {
		return lista;
	}

	public void setLista(List<TLOcorrencia> lista) {
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

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
