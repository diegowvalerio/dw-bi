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
public class BeanSigeUsuarioAcesso_Novo implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeUsuario sigeusuario = new SigeUsuario();
	
	@Inject
	private ServicoSigeUsuario servico;

	@PostConstruct
	public void init() {
		this.sigeusuario = getSigeusuario();
	}
	
	public String salvar_novo(){
		servico.salvar(sigeusuario);
		return "usuarios";
	}
	

	public SigeUsuario getSigeusuario() {
		return sigeusuario;
	}

	public void setSigeusuario(SigeUsuario sigeusuario) {
		this.sigeusuario = sigeusuario;
	}

}
