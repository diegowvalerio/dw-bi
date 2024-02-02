package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
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
import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.classe.VendaVendedor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCidadeVenda;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendaUF;
import br.com.dwbidiretor.servico.ServicoVendaVendedor;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanVendaVendedor implements Serializable {
	private static final long serialVersionUID = 1L;

	private VendaVendedor vendav = new VendaVendedor();

	@Inject
	private ServicoVendaVendedor servico;
	private List<VendaVendedor> lista = new ArrayList<>();
	private List<VendaVendedor> lista2 = new ArrayList<>();
	
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private Integer filtra = 0;
	private boolean filtro;
	
	private String ano,mes;

	@PostConstruct
	public void init() {
		
		Calendar cal = Calendar.getInstance();
    	cal.setTime(data_grafico);
		ano = ""+cal.get(Calendar.YEAR);
		mes =  ""+cal.get(Calendar.MONTH);
	
	
	}
	
	public void filtrar(){
		
		lista = servico.vendavendedor(ano, mes);
		lista2 = servico.vendavendedorfatura(ano, mes);
	
	}
	
	public String getTotal1() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia1().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal2() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia2().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal3() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia3().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal4() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia4().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal5() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia5().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal6() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia6().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal7() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia7().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal8() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia8().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal9() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia9().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal10() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia10().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal11() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia11().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal12() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia12().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal13() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia13().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal14() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia14().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal15() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia15().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal16() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia16().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal17() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia17().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal18() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia18().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal19() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia19().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal20() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia20().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal21() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia21().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal22() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia22().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal23() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia23().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal24() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia24().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal25() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia25().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal26() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia26().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal27() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia27().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal28() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia28().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal29() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia29().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal30() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia30().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal31() {
		float total = 0;
		for (VendaVendedor v : getLista()) {
			total = total + v.getDia31().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	
	
	public String getTotal1x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia1().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal2x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia2().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal3x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia3().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal4x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia4().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal5x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia5().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal6x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia6().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal7x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia7().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal8x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia8().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal9x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia9().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal10x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia10().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal11x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia11().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal12x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia12().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal13x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia13().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal14x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia14().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal15x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia15().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal16x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia16().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal17x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia17().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal18x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia18().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal19x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia19().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal20x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia20().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal21x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia21().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal22x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia22().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal23x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia23().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal24x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia24().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal25x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia25().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal26x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia26().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal27x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia27().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal28x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia28().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal29x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia29().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal30x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia30().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
	}
	public String getTotal31x() {
		float total = 0;
		for (VendaVendedor v : getLista2()) {
			total = total + v.getDia31().floatValue();
		}
		return new DecimalFormat("###,###.###").format(total);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getFiltra() {
		return filtra;
	}

	public void setFiltra(Integer filtra) {
		this.filtra = filtra;
	}

	public boolean isFiltro() {
		return filtro;
	}

	public void setFiltro(boolean filtro) {
		this.filtro = filtro;
	}


	public VendaVendedor getVendav() {
		return vendav;
	}

	public void setVendav(VendaVendedor vendav) {
		this.vendav = vendav;
	}

	public List<VendaVendedor> getLista() {
		return lista;
	}

	public void setLista(List<VendaVendedor> lista) {
		this.lista = lista;
	}

	public List<VendaVendedor> getLista2() {
		return lista2;
	}

	public void setLista2(List<VendaVendedor> lista2) {
		this.lista2 = lista2;
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
