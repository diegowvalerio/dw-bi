package br.com.dwbigestor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbigestor.classe.SigeUsuario;
import br.com.dwbigestor.dao.DAOSIGEUsuario;
import br.com.dwbigestor.hibernate.Transacao;
import br.com.dwbigestor.hibernate.TransacaoSige;

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
		dao.registralog(conteudo, pagina, data,  latitude,  longetude);
	}
	
}
