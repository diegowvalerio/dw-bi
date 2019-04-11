package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

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

import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbi.dwbi.dao.DAOGenerico;

public class DAOGenericoHibernate<E> implements DAOGenerico<E>, Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	protected EntityManager manager;
	private Class classeEntidade;

	public DAOGenericoHibernate(Class classeEntidade) {
		this.classeEntidade = classeEntidade;
	}

	@Override
	public E salvar(E e) {
		manager.persist(e);
		return e;
	}

	@Override
	public E alterar(E e) {
		return manager.merge(e);
	}

	@Override
	public boolean excluir(Integer id) {
		E e = consultar(id);
		manager.remove(e);
		return true;
	}

	@Override
	public E consultar(Integer id) {
		return (E) manager.find(classeEntidade, id);
	}

	@Override
	public List<E> consultar() {
		return manager.createQuery("from " + classeEntidade.getSimpleName()).getResultList();
	}

	/* grafico */
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<E> movimentodia(Date data) {
		// boolean bo = true;
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classeEntidade);

		criteria.add(Restrictions.eq("dtvenda", data));

		return criteria.list();
	}
	
	/*pegar usuario conectado*/
	public String usuarioconectado(){
		String nome;		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
		    nome = ((UserDetails)principal).getUsername();
		} else {
		    nome = principal.toString();
		}
		//System.out.println(nome);
		return nome;
	}

	/* venda ano mes */
	public List<VendaAnoMes> vendaanomes(){
		List<VendaAnoMes> list = new ArrayList<>();
		
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						//"SELECT * FROM("
						" SELECT "
						+" TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') AS ANO, "
						+" TO_CHAR(EN.DT_PEDIDOVENDA,'MM') AS MES, "
						+" SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VL_TOTAL "
						+" FROM PEDIDOVENDA EN "
						+" INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID "
						+" WHERE "
						+" CF.TIPOOPERACAO_CFOP = 'VENDA' "
						+" AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+" AND EN.VENDEDOR1ID = ' " +usuarioconectado()+ " ' "
						+" GROUP BY "
						+" TO_CHAR(EN.DT_PEDIDOVENDA,'MM'), "
						+" TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') "
						+" ORDER BY "
						+" TO_CHAR(EN.DT_PEDIDOVENDA,'MM'),TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') ");
		//query.setParameter("vendedorlogado", usuarioconectado());
		List<Object[]> lista = query.getResultList();

        for(Object[] row : lista){
        	VendaAnoMes vendaAnoMes = new VendaAnoMes();
            

            vendaAnoMes.setAno((String) row[0]);
            vendaAnoMes.setMes((String) row[1]);      
            vendaAnoMes.setValor((BigDecimal) row[2]);
            //System.out.println((String) row[0]+","+(String) row[1]+","+(BigDecimal) row[2]);
            list.add(vendaAnoMes);

        }
		
		return list;
	}
	
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1, Date data2){
		List<VendaGrupoSubGrupoProdutoQuantidadeValor> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						//"SELECT * FROM("
						" SELECT "
						+" sub.NOME_SUBGRUPOPRODUTO as subgrupo, "
						+" sum(it.qt_pedidovenda_item) qtde, "
						+" sum(IT.vl_total_pedidovenda_item) vlr "
						+" from pedidovenda p "
						+" INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
						+" INNER JOIN PRODUTO PR ON PR.PRODUTOID = IT.PRODUTOID "
						+" INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+" INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+" inner join subgrupoproduto sub on sub.subgrupoprodutoid = pr.subgrupoprodutoid "
						+" inner join GRUPOPRODUTO gru on gru.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
						+" WHERE p.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+" AND CF.tipooperacao_cfop = 'VENDA'and p.VENDEDOR1ID = ' " +usuarioconectado()+ " ' "
						+" and p.DT_PEDIDOVENDA between ' " +dataFormatada+ " ' and ' " +dataFormatada2+ " ' "
						+" group by sub.NOME_SUBGRUPOPRODUTO "
						+" order by sub.NOME_SUBGRUPOPRODUTO ");
		
		List<Object[]> lista = query.getResultList();

        for(Object[] row : lista){
        	VendaGrupoSubGrupoProdutoQuantidadeValor vendaGrupoSubGrupoProdutoQuantidadeValor = new VendaGrupoSubGrupoProdutoQuantidadeValor();
            

        	vendaGrupoSubGrupoProdutoQuantidadeValor.setSubgrupo((String) row[0]);
        	vendaGrupoSubGrupoProdutoQuantidadeValor.setQuantidade((BigDecimal) row[1]);      
        	vendaGrupoSubGrupoProdutoQuantidadeValor.setValor((BigDecimal) row[2]);
            //System.out.println((String) row[0]+","+(String) row[1]+","+(BigDecimal) row[2]);
            list.add(vendaGrupoSubGrupoProdutoQuantidadeValor);
        }
		
		return list;
	}

}
