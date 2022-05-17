package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.Projeto;
import br.com.dwbidiretor.dao.DAOProjeto;
import br.com.dwbidiretor.fabrica.EntityManagerProducerSige.Corporativo;
import br.com.dwbidiretor.hibernate.TransacaoSige;

@Dependent
public class ServicoProjeto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAOProjeto dao;
	
	@TransacaoSige
	public void Ssalvar(Projeto projeto){
		try {
			if(projeto.getIdprojeto() == null){
				dao.Ssalvar(projeto);
			}else{
				dao.Salterar(projeto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@TransacaoSige
	public boolean Sexcluir(Integer id){
		return dao.Sexcluir(id);
	}
	
	public List<Projeto> Sconsultar(){
		return dao.Sconsultar();
	}
	
	
}
