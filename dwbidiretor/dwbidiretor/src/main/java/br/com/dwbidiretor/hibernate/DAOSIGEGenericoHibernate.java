package br.com.dwbidiretor.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.SigeAcesso;
import br.com.dwbidiretor.classe.SigeModulo;
import br.com.dwbidiretor.classe.SigeUsuario;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.dao.DAOSIGEGenerico;
import br.com.dwbidiretor.fabrica.EntityManagerProducerSige.Corporativo;


public class DAOSIGEGenericoHibernate<E> implements DAOSIGEGenerico<E>, Serializable {
	private static final long serialVersionUID = 1L;

	private Class classeEntidade;
	
	@Inject
	@Corporativo
	protected EntityManager manager;


	public DAOSIGEGenericoHibernate(Class classeEntidade) {
		this.classeEntidade = classeEntidade;
	}

	@Override
	public E salvar(E e) {
		// INSERT INTO dwbi_login (usuario ,senha ,ativo, TIPO) VALUES('4603' ,'00704001000117','SIM','VENDEDOR' )
		//manager.persist(e);
		//return e;
		
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" INSERT INTO dwbi_login (usuario ,senha ,ativo, TIPO) VALUES('"+ ((SigeUsuario) e).getUsuario() +"', '"+ ((SigeUsuario) e).getSenha() +"', '"+ ((SigeUsuario) e).getAtivo() +"', '"+ ((SigeUsuario) e).getTipo() +"')  "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}

	@Override
	public E alterar(E e) {
		//List<SigeUsuario> list = new ArrayList<>();
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" update dwbi_login set senha = '"+ ((SigeUsuario) e).getSenha() +"' where usuario = '"+ usuarioconectado() +"'   "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	
	/* pegar usuario conectado */
	public String usuarioconectado() {
		String nome;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		// System.out.println(nome);
		return nome;
	}

	@Override
	public boolean excluir(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SigeUsuario> consultar() {
		List<SigeUsuario> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		// query.setParameter("vendedorlogado", usuarioconectado());
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			SigeUsuario sigeusuario = new SigeUsuario();

			sigeusuario.setIdlogin((Integer) row[0]);
			sigeusuario.setUsuario((String) row[1]);
			sigeusuario.setSenha((String) row[2]);
			list.add(sigeusuario);

		}
		
		return list;
	}
	
	//liberação
	@Override
	public E alteraracesso(E e) {
		List<SigeUsuario> list = new ArrayList<>();
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" update dwbi_login set senha = '"+ ((SigeUsuario) e).getSenha() +"', ativo = '"+ ((SigeUsuario) e).getAtivo() +"', tipo = '"+ ((SigeUsuario) e).getTipo() +"' where usuario = '"+ ((SigeUsuario) e).getUsuario() +"' and idlogin = '"+ ((SigeUsuario) e).getIdlogin() +"'   "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	
	//consulta acessos
	@Override
	public List<SigeUsuario> consultaracesso() {
		List<SigeUsuario> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" SELECT idlogin,usuario,senha,ativo,tipo from dwbi_login order by idlogin");
		// query.setParameter("vendedorlogado", usuarioconectado());
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			SigeUsuario sigeusuario = new SigeUsuario();

			sigeusuario.setIdlogin((Integer) row[0]);
			sigeusuario.setUsuario((String) row[1]);
			sigeusuario.setSenha((String) row[2]);
			sigeusuario.setAtivo((String) row[3]);
			sigeusuario.setTipo((String) row[4]);
			list.add(sigeusuario);

		}
		
		return list;
	}
	
	//cadastrar modulo
	@Override
	public E salvar_modulo(E e) {
		//List<SigeUsuario> list = new ArrayList<>();
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" INSERT INTO dwbi_acesso_modulo(descricao,identificacao) VALUES ('"+ ((SigeModulo) e).getDescricao() +"', '"+ ((SigeModulo) e).getIdentificacao() +"')  "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	
	//alterar modulo
	@Override
	public E alterar_modulo(E e) {
		//List<SigeModulo> list = new ArrayList<>();
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" update dwbi_acesso_modulo set descricao = '"+ ((SigeModulo) e).getDescricao() +"', identificacao = '"+ ((SigeModulo) e).getIdentificacao() +"' where idmodulo = '"+ ((SigeModulo) e).getIdmodulo() +"'   "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	
	//consultar modulos
	@Override
	public List<SigeModulo> consultarmodulo() {
		List<SigeModulo> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT idmodulo,descricao,identificacao from dwbi_acesso_modulo order by idmodulo");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			SigeModulo sigeModulo = new SigeModulo();

			sigeModulo.setIdmodulo((Integer) row[0]);
			sigeModulo.setDescricao((String) row[1]);
			sigeModulo.setIdentificacao((String) row[2]);
			list.add(sigeModulo);

		}
		
		return list;
	}
	
	//consultar acessos
	@Override
	public List<SigeAcesso> consultar_acesso(Integer idlogin) {
		List<SigeAcesso> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT DWA.IDACESSO, DWA.IDLOGIN,DWA.IDMODULO, dwm.descricao,dwm.identificacao,DWA.ACESSO FROM DWBI_ACESSO DWA "
				+ " INNER JOIN dwbi_acesso_modulo dwm on dwm.idmodulo = dwa.idmodulo  where dwa.idlogin = " +idlogin );

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			SigeAcesso sigeacesso = new SigeAcesso();

			sigeacesso.setIdacesso((Integer) row[0]);
			sigeacesso.setIdlogin((Integer) row[1]);
			sigeacesso.setIdmodulo((Integer) row[2]);
			sigeacesso.setDescricao((String) row[3]);
			sigeacesso.setIdentificacao((String) row[4]);
			sigeacesso.setAcesso((String) row[5]);
			list.add(sigeacesso);

		}
		
		return list;
	}
	
	//cadastrar acessos modulo
	@Override
	public E salvar_acesso(E e) {

		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" INSERT INTO dwbi_acesso (idlogin,idmodulo, acesso) VALUES ('"+ ((SigeAcesso) e).getIdlogin() +"', '"+ ((SigeAcesso) e).getIdmodulo() +"', '"+ ((SigeAcesso) e).getAcesso() +"' )  "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	
	//excluir acessos modulo
	@Override
	public E excluir_acesso(E e) {

		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" delete from dwbi_acesso where idlogin = '"+ ((SigeAcesso) e).getIdlogin() +"' and idmodulo= '"+ ((SigeAcesso) e).getIdmodulo() +"' and idacesso= '"+ ((SigeAcesso) e).getIdacesso() +"' "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
		
		return e;
	}
	

}
