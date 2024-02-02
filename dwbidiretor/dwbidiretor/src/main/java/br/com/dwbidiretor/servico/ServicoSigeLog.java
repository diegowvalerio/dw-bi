package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.SigeLog;
import br.com.dwbidiretor.dao.DAOSIGELog;

@Dependent
public class ServicoSigeLog implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private DAOSIGELog dao;
	
	public List<SigeLog> consultalog(String usuario, String conteudo,String datahora, String ip, String tipo){
		return dao.consultalog(usuario, conteudo, datahora, ip, tipo);
	}
		
}
