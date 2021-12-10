package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.dao.DAOSIGEAcesso;


@Dependent
public class ServicoSigeAcesso implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private DAOSIGEAcesso dao;
	
		
	public List<SigeAcesso> consultar_acesso(Integer idlogin){
		return dao.consultar_acesso(idlogin);
	}
	
	public SigeAcesso salvar_acesso(SigeAcesso e){
		return dao.salvar_acesso(e);
	}
	
	public SigeAcesso excluir_acesso(SigeAcesso e){
		return dao.excluir_acesso(e);
	}
		
	
}
