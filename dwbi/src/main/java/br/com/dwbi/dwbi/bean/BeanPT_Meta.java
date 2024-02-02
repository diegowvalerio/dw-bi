package br.com.dwbi.dwbi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.dwbi.classe.PT_Meta;
import br.com.dwbi.classe.PT_Mix;
import br.com.dwbi.dwbi.servico.ServicoPT_Meta;



@Named
@ViewScoped
public class BeanPT_Meta implements Serializable {
	private static final long serialVersionUID = 1L;

	private PT_Meta ptmeta = new PT_Meta();
	@Inject
	private ServicoPT_Meta servico;
	private List<PT_Meta> lista = new ArrayList<>();
	
	
	private String vendedorlogado;
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String regiao;

	@PostConstruct
	public void init() {
		lista = servico.pt_meta(regiao, vendedorfiltrado, vendedorfiltrado2);	
	}

	public PT_Meta getPtmeta() {
		return ptmeta;
	}

	public void setPtmeta(PT_Meta ptmeta) {
		this.ptmeta = ptmeta;
	}

	public List<PT_Meta> getLista() {
		return lista;
	}

	public void setLista(List<PT_Meta> lista) {
		this.lista = lista;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
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

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
	
	public String gettotal1() {
		float total = 0;

		for (PT_Meta p : getLista()) {
			total = total + p.getTotaltri1().intValue();
		}

		return ""+total;
	}
	
	public String gettotal2() {
		float total = 0;

		for (PT_Meta p : getLista()) {
			total = total + p.getTotaltri2().intValue();
		}

		return ""+total;
	}
	
	public String gettotal3() {
		float total = 0;

		for (PT_Meta p : getLista()) {
			total = total + p.getTotaltri3().intValue();
		}

		return ""+total;
	}
	
	public String gettotal4() {
		float total = 0;

		for (PT_Meta p : getLista()) {
			total = total + p.getTotaltri4().intValue();
		}

		return ""+total;
	}
	
	public String gettotalanual() {
		float total = 0;

		for (PT_Meta p : getLista()) {
			total = total + p.getTotalanual().intValue();
		}

		return ""+total;
	}


	
}
