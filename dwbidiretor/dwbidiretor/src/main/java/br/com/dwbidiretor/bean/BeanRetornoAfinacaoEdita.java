package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoRetornoAfinacao;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanRetornoAfinacaoEdita implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Locale LOCAL = new Locale("pt","BR");
	
	private RetornoAfinacao retornoafinacao = new RetornoAfinacao();
	@Inject
	private ServicoRetornoAfinacao servico;
	

	@PostConstruct
	public void init() {
		this.retornoafinacao = getRetornoafinacao();

	}

	public String salvar() {
		if(retornoafinacao.getProduto_cromado() == null) {
			retornoafinacao.setProduto_cromado(new BigDecimal(0));
		}
		servico.salvar(retornoafinacao);
		
		return "lista";
	}


	public RetornoAfinacao getRetornoafinacao() {
		return retornoafinacao;
	}

	public void setRetornoafinacao(RetornoAfinacao retornoafinacao) {
		this.retornoafinacao = retornoafinacao;
	}


}
