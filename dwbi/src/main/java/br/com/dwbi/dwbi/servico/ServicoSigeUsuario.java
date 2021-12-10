package br.com.dwbi.dwbi.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbi.classe.SigeUsuario;
import br.com.dwbi.dwbi.dao.DAOSIGEUsuario;
import br.com.dwbi.dwbi.hibernate.Transacao;
import br.com.dwbi.dwbi.hibernate.TransacaoSige;

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
	
	
	public void salvar(SigeUsuario usuario){
		dao.salvar(usuario);
	}
	
	public void registralog(String conteudo, String pagina, String data, String latitude, String longetude){
		dao.registralog(conteudo, pagina, data, latitude, longetude);;
	}
	
}
