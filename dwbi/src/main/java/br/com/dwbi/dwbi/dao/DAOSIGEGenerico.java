package br.com.dwbi.dwbi.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbi.classe.SigeUsuario;


public interface DAOSIGEGenerico<E> {
	
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	
	public List<SigeUsuario> consultar();
	
}
