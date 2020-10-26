package br.com.dwbidiretor.servico;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.dao.DAORetornoAfinacao;

@Dependent
public class ServicoRetornoAfinacao implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DAORetornoAfinacao dao;
	
	
	public List<RetornoAfinacao> retornoafinacao(Date data1, Date data2, String cfop){
		return dao.retornoafinacao(data1, data2 , cfop);
	}
	
}
