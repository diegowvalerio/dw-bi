package br.com.dwbigestor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbigestor.classe.SigeUsuario;


public interface DAOSIGEGenerico<E> {
	
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	
	public List<SigeUsuario> consultar();
	public void registralog(String conteudo, String pagina, String data, String latitude, String longetude);
	
}
