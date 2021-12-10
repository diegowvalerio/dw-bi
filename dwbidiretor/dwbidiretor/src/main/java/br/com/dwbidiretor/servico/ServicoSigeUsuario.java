package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.dao.DAOSIGEUsuario;
import br.com.dwbidiretor.hibernate.Transacao;
import br.com.dwbidiretor.hibernate.TransacaoSige;

@Dependent
public class ServicoSigeUsuario implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private DAOSIGEUsuario dao;
	
	//@TransacaoSige
	public SigeUsuario alterar(SigeUsuario e){
		return dao.alterar(e);
	}
	
	public List<SigeUsuario> consultar(){
		return dao.consultar();
	}
	
	public List<SigeUsuario> consultaracesso(){
		return dao.consultaracesso();
	}
	
	public SigeUsuario alteraracesso(SigeUsuario e){
		return dao.alteraracesso(e);
	}
	
	
	public void salvar(SigeUsuario usuario){
		dao.salvar(usuario);
	}
	
	public void registralog(String conteudo, String pagina, String data, String latitude, String longetude, String aparelho){
		dao.registralog(conteudo, pagina, data, latitude, longetude, aparelho);
	}
}
