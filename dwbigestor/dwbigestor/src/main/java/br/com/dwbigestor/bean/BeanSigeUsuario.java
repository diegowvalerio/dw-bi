package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.com.dwbigestor.classe.SigeUsuario;
import br.com.dwbigestor.servico.ServicoSigeUsuario;

@Named
@ViewScoped
public class BeanSigeUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeUsuario sigeusuario = new SigeUsuario();
	
	@Inject
	private ServicoSigeUsuario servico;
	private List<SigeUsuario> lista = new ArrayList<>();


	@PostConstruct
	public void init() { 
		lista = servico.consultar();
	}
	
	public void salvar(){
		servico.alterar(sigeusuario);
		lista = servico.consultar();
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

	public SigeUsuario getSigeusuario() {
		return sigeusuario;
	}

	public void setSigeusuario(SigeUsuario sigeusuario) {
		this.sigeusuario = sigeusuario;
	}

	public List<SigeUsuario> getLista() {
		return lista;
	}

	public void setLista(List<SigeUsuario> lista) {
		this.lista = lista;
	}

}
