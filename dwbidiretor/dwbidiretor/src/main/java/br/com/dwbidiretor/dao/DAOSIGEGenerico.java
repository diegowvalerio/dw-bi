package br.com.dwbidiretor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbidiretor.classe.SigeUsuario;


public interface DAOSIGEGenerico<E> {
	
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	
	public List<SigeUsuario> consultar();
	
	//liberação de acesso
	public E alteraracesso(E e);
	public List<SigeUsuario> consultaracesso();
}
