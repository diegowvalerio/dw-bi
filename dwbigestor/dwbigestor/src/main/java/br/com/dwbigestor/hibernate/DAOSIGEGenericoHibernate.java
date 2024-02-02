package br.com.dwbigestor.hibernate;

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


import br.com.dwbigestor.classe.SigeUsuario;
import br.com.dwbigestor.dao.DAOSIGEGenerico;
import br.com.dwbigestor.fabrica.EntityManagerProducerSige.Corporativo;


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
		manager.persist(e);
		return e;
	}

	@Override
	public E alterar(E e) {
		List<SigeUsuario> list = new ArrayList<>();
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
	
	@Override
	public void registralog(String conteudo, String pagina, String data, String latitude, String longetude) {
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				" INSERT INTO dwbi_log(usuario,conteudo,tipo,datahora, ip) values('"+usuarioconectado()+"', '"+conteudo+"', '"+pagina+"', '"+data+"', '"+longetude+"') "
					       +" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println(e2);
		}
		
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
	

}
