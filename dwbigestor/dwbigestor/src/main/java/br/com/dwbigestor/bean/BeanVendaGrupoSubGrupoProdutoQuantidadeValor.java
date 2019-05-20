package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
import br.com.dwbigestor.servico.ServicoVendaAnoMes;
import br.com.dwbigestor.servico.ServicoVendaGrupoSubGrupoProdutoQuantidadeValor;

@Named
@ViewScoped
public class BeanVendaGrupoSubGrupoProdutoQuantidadeValor implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendaGrupoSubGrupoProdutoQuantidadeValor vendaGrupoSubGrupoProdutoQuantidadeValor = new VendaGrupoSubGrupoProdutoQuantidadeValor();
	@Inject
	private ServicoVendaGrupoSubGrupoProdutoQuantidadeValor servico;
	private List<VendaGrupoSubGrupoProdutoQuantidadeValor> listavenda = new ArrayList<>();

	private String vendedorlogado;
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();

	@PostConstruct
	public void init() {
		listavenda = servico.vendaGrupoSubGrupoProdutoQuantidadeValor(data_grafico, data_grafico2);

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
