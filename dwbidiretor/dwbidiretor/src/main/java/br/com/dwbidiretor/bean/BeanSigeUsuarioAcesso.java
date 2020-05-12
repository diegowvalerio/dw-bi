package br.com.dwbidiretor.bean;

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

import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.servico.ServicoSigeUsuario;

@Named
@ViewScoped
public class BeanSigeUsuarioAcesso implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeUsuario sigeusuario = new SigeUsuario();
	
	@Inject
	private ServicoSigeUsuario servico;
	private List<SigeUsuario> lista = new ArrayList<>();


	@PostConstruct
	public void init() {
		lista = servico.consultaracesso();
	}
	
	public void salvar(){
		servico.alteraracesso(sigeusuario);
		lista = servico.consultaracesso();
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
	
	public String encaminha() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("sigeusuario", this.sigeusuario);

		return "acesso-modulo";
	}

}
