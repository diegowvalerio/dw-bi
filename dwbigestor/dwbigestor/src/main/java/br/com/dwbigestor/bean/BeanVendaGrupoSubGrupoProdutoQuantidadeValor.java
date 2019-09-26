package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
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

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.servico.ServicoVendaAnoMes;
import br.com.dwbigestor.servico.ServicoVendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanVendaGrupoSubGrupoProdutoQuantidadeValor implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendaGrupoSubGrupoProdutoQuantidadeValor vendaGrupoSubGrupoProdutoQuantidadeValor = new VendaGrupoSubGrupoProdutoQuantidadeValor();
	@Inject
	private ServicoVendaGrupoSubGrupoProdutoQuantidadeValor servico;
	private List<VendaGrupoSubGrupoProdutoQuantidadeValor> listavenda = new ArrayList<>();

	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;

	@PostConstruct
	public void init() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		data_grafico2 =c.getTime();
		
		listavendedor = servicovendedor.consultavendedor();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		if (session.getAttribute("vendedor") != null){
			vendedor = (Vendedor) session.getAttribute("vendedor");
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
		if ((Date) session.getAttribute("data1") != null) {
			this.data_grafico = (Date) session.getAttribute("data1");
			this.data_grafico2 = (Date) session.getAttribute("data2");
			listavenda = servico.vendaGrupoSubGrupoProdutoQuantidadeValor(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
		} else {			
			listavenda = servico.vendaGrupoSubGrupoProdutoQuantidadeValor(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
		}

		session.removeAttribute("data1");
		session.removeAttribute("data2");
		session.removeAttribute("vendedor");
	}
	
	public void filtrar(){
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		listavenda = servico.vendaGrupoSubGrupoProdutoQuantidadeValor(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2);
	}

	public VendaGrupoSubGrupoProdutoQuantidadeValor getVendaGrupoSubGrupoProdutoQuantidadeValor() {
		return vendaGrupoSubGrupoProdutoQuantidadeValor;
	}

	public void setVendaGrupoSubGrupoProdutoQuantidadeValor(
			VendaGrupoSubGrupoProdutoQuantidadeValor vendaGrupoSubGrupoProdutoQuantidadeValor) {
		this.vendaGrupoSubGrupoProdutoQuantidadeValor = vendaGrupoSubGrupoProdutoQuantidadeValor;
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
	 
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> getListavenda() {
		return listavenda;
	}

	public void setListavenda(List<VendaGrupoSubGrupoProdutoQuantidadeValor> listavenda) {
		this.listavenda = listavenda;
	}
	
	public String getValorTotal() {
        float total = 0;
 
        for(VendaGrupoSubGrupoProdutoQuantidadeValor venda : getListavenda()) {
            total= total + venda.getValor().floatValue();
        }
 
        return new DecimalFormat("###,###.###").format(total);
    }

	public String getQtdeTotal() {
        int total = 0;
 
        for(VendaGrupoSubGrupoProdutoQuantidadeValor venda : getListavenda()) {
            total= total + venda.getQuantidade().intValue();
        }
 
        return new DecimalFormat("###,###.###").format(total);
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
		//System.out.println(nome);
		return nome;
	}

}
