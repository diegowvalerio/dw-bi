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

import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.servico.ServicoSigeAcesso;
import br.com.dwbidiretor.servico.ServicoSigeModulo;
import br.com.dwbidiretor.servico.ServicoSigeUsuario;

@Named
@ViewScoped
public class BeanSigeUsuarioAcessoModulo implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeUsuario sigeusuario = new SigeUsuario();
	private SigeAcesso sigeAcesso = new SigeAcesso();
	private SigeModulo sigeModulo = new SigeModulo();
	
	@Inject
	private ServicoSigeAcesso servico_acesso;
	private List<SigeAcesso> lista_acesso = new ArrayList<>();
	
	@Inject
	private ServicoSigeModulo servico_modulo;
	private List<SigeModulo> lista_modulo = new ArrayList<>();

	@PostConstruct
	public void init() {
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpSession session = (HttpSession) request.getSession();
		this.sigeusuario = (SigeUsuario) session.getAttribute("sigeusuario");
		session.removeAttribute("sigeusuario");
		
		this.lista_acesso = servico_acesso.consultar_acesso(this.sigeusuario.getIdlogin());
		this.lista_modulo = servico_modulo.consultarmodulo();
	}
	
	public void salvar(){
		sigeAcesso.setIdlogin(sigeusuario.getIdlogin());
		sigeAcesso.setAcesso("SIM");
		sigeAcesso.setIdmodulo(sigeModulo.getIdmodulo());
		
		servico_acesso.salvar_acesso(sigeAcesso);
		lista_acesso = servico_acesso.consultar_acesso(sigeusuario.getIdlogin());
	}
	
	public void excluir(){
		servico_acesso.excluir_acesso(sigeAcesso);
		lista_acesso = servico_acesso.consultar_acesso(sigeusuario.getIdlogin());
	}

	public SigeUsuario getSigeusuario() {
		return sigeusuario;
	}

	public void setSigeusuario(SigeUsuario sigeusuario) {
		this.sigeusuario = sigeusuario;
	}

	public SigeAcesso getSigeAcesso() {
		return sigeAcesso;
	}

	public void setSigeAcesso(SigeAcesso sigeAcesso) {
		this.sigeAcesso = sigeAcesso;
	}

	public List<SigeAcesso> getLista_acesso() {
		return lista_acesso;
	}

	public void setLista_acesso(List<SigeAcesso> lista_acesso) {
		this.lista_acesso = lista_acesso;
	}

	public List<SigeModulo> getLista_modulo() {
		return lista_modulo;
	}

	public void setLista_modulo(List<SigeModulo> lista_modulo) {
		this.lista_modulo = lista_modulo;
	}

	public SigeModulo getSigeModulo() {
		return sigeModulo;
	}

	public void setSigeModulo(SigeModulo sigeModulo) {
		this.sigeModulo = sigeModulo;
	}

}
