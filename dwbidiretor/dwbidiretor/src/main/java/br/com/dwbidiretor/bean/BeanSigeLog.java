package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dwbidiretor.classe.SigeLog;
import br.com.dwbidiretor.servico.ServicoSigeLog;

@Named
@ViewScoped
public class BeanSigeLog implements Serializable {
	private static final long serialVersionUID = 1L;

	private SigeLog log = new SigeLog();
	
	String usuario;
	String conteudo;
	String datahora;
	String tipo;
	String ip;
	
	@Inject
	private ServicoSigeLog servico;
	private List<SigeLog> lista = new ArrayList<>();

	@PostConstruct
	public void init() { 
		tipo = "TODOS";
	}
	
	public void filtrar(){
		lista = servico.consultalog(usuario, conteudo, datahora, ip, tipo);
		Collections.sort(lista,Collections.reverseOrder(Comparator.comparing(SigeLog::getIdlog)));
	}

	public SigeLog getLog() {
		return log;
	}

	public void setLog(SigeLog log) {
		this.log = log;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public String getDatahora() {
		return datahora;
	}

	public void setDatahora(String datahora) {
		this.datahora = datahora;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public ServicoSigeLog getServico() {
		return servico;
	}

	public void setServico(ServicoSigeLog servico) {
		this.servico = servico;
	}

	public List<SigeLog> getLista() {
		return lista;
	}

	public void setLista(List<SigeLog> lista) {
		this.lista = lista;
	}
	
	

}
