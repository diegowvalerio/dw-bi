package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.servico.ServicoSigeModulo;

@Named
@ViewScoped
public class BeanSigeModulo implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeModulo sigemodulo = new SigeModulo();
	
	@Inject
	private ServicoSigeModulo servico;
	private List<SigeModulo> lista = new ArrayList<>();


	@PostConstruct
	public void init() { 
		lista = servico.consultarmodulo();
	}
	
	public void salvar(){
		servico.salvar_modulo(sigemodulo);
		lista = servico.consultarmodulo();
	}
	
	public void editar(){
		servico.alterar_modulo(sigemodulo);
		lista = servico.consultarmodulo();
	}

	public SigeModulo getSigemodulo() {
		return sigemodulo;
	}

	public void setSigemodulo(SigeModulo sigemodulo) {
		this.sigemodulo = sigemodulo;
	}

	public List<SigeModulo> getLista() {
		return lista;
	}

	public void setLista(List<SigeModulo> lista) {
		this.lista = lista;
	}


}
