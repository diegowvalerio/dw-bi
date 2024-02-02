package br.com.dwbi.dwbi.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import br.com.dwbi.classe.PT_Carteira;
import br.com.dwbi.dwbi.servico.ServicoPT_Carteira;



@Named
@ViewScoped
public class BeanPT_Carteira implements Serializable {
	private static final long serialVersionUID = 1L;

	private PT_Carteira ptcarteira = new PT_Carteira();
	@Inject
	private ServicoPT_Carteira servico;
	private List<PT_Carteira> lista = new ArrayList<>();
	private List<PT_Carteira> lista2 = new ArrayList<>();
	private List<PT_Carteira> lista3 = new ArrayList<>();
	private List<PT_Carteira> lista4 = new ArrayList<>();
	
	
	private String vendedorlogado;
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String regiao;

	@PostConstruct
	public void init() {
		lista = servico.pt_carteira(regiao, vendedorfiltrado, vendedorfiltrado2);
		lista2 = servico.pt_carteira2(regiao, vendedorfiltrado, vendedorfiltrado2);
		lista3 = servico.pt_carteira3(regiao, vendedorfiltrado, vendedorfiltrado2);
		lista4 = servico.pt_carteira4(regiao, vendedorfiltrado, vendedorfiltrado2);
	}

	public PT_Carteira getPtcarteira() {
		return ptcarteira;
	}

	public void setPtcarteira(PT_Carteira ptcarteira) {
		this.ptcarteira = ptcarteira;
	}

	public List<PT_Carteira> getLista() {
		return lista;
	}

	public void setLista(List<PT_Carteira> lista) {
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
	
	public List<PT_Carteira> getLista2() {
		return lista2;
	}

	public void setLista2(List<PT_Carteira> lista2) {
		this.lista2 = lista2;
	}

	public List<PT_Carteira> getLista3() {
		return lista3;
	}

	public void setLista3(List<PT_Carteira> lista3) {
		this.lista3 = lista3;
	}

	public List<PT_Carteira> getLista4() {
		return lista4;
	}

	public void setLista4(List<PT_Carteira> lista4) {
		this.lista4 = lista4;
	}

	public String gettotal1() {
		float total = 0;

		for (PT_Carteira p : getLista()) {
			total = total + p.getTotaltri1().intValue();
		}

		return ""+total;
	}
	
	public String gettotal2() {
		float total = 0;

		for (PT_Carteira p : getLista2()) {
			total = total + p.getTotaltri1().intValue();
		}

		return ""+total;
	}
		
}
