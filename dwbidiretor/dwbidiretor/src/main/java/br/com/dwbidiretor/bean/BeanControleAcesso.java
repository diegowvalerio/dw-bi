package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.servico.ServicoSigeAcesso;
import br.com.dwbidiretor.servico.ServicoSigeUsuario;

@ManagedBean
@SessionScoped
public class BeanControleAcesso implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeUsuario sigeusuario = new SigeUsuario();
	private SigeAcesso sigeAcesso = new SigeAcesso();
	
	@Inject
	private ServicoSigeUsuario servico;
	private List<SigeUsuario> lista = new ArrayList<>();
	
	@Inject
	private ServicoSigeAcesso servicoAcesso;
	private List<SigeAcesso> listaacesso = new ArrayList<>();

	@PostConstruct
	public void init() { 
		lista = servico.consultar();
		sigeusuario = lista.get(0);
		
		listaacesso= servicoAcesso.consultar_acesso(sigeusuario.getIdlogin());
	}
	
	public boolean acesso(String tipo){
		boolean t = false;
		for (SigeAcesso acesso : listaacesso) {
			if (acesso.getIdentificacao().equals(tipo)){
				t = true;
			}	
		}
		return t;
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

	public SigeAcesso getSigeAcesso() {
		return sigeAcesso;
	}

	public void setSigeAcesso(SigeAcesso sigeAcesso) {
		this.sigeAcesso = sigeAcesso;
	}

	public List<SigeAcesso> getListaacesso() {
		return listaacesso;
	}

	public void setListaacesso(List<SigeAcesso> listaacesso) {
		this.listaacesso = listaacesso;
	}

}
