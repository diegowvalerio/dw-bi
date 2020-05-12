package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.dao.DAOSIGEModulo;
import br.com.dwbidiretor.dao.DAOSIGEUsuario;
import br.com.dwbidiretor.hibernate.Transacao;
import br.com.dwbidiretor.hibernate.TransacaoSige;

@Dependent
public class ServicoSigeModulo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private DAOSIGEModulo dao;
	
		
	public List<SigeModulo> consultarmodulo(){
		return dao.consultarmodulo();
	}
	
	public SigeModulo salvar_modulo(SigeModulo e){
		return dao.salvar_modulo(e);
	}
	
	public SigeModulo alterar_modulo(SigeModulo e){
		return dao.alterar_modulo(e);
	}
	
	
	
}
