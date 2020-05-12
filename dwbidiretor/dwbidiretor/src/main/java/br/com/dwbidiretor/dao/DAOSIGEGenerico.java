package br.com.dwbidiretor.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.classe.SigeUsuario;


public interface DAOSIGEGenerico<E> {
	
	public E salvar(E e);
	public E alterar(E e);
	public boolean excluir(Integer id);
	
	public List<SigeUsuario> consultar();
	
	//liberação de acesso
	public E alteraracesso(E e);
	public List<SigeUsuario> consultaracesso();
	
	
	//cadastro de modulos
	public E salvar_modulo(E e);
	public E alterar_modulo(E e);
	public List<SigeModulo> consultarmodulo();
	
	//conceder acesso
	public List<SigeAcesso> consultar_acesso(Integer idlogin);
	public E salvar_acesso(E e);
	public E excluir_acesso(E e);
}
