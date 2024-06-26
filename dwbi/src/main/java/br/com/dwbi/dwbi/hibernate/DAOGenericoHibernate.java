package br.com.dwbi.dwbi.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
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

import br.com.dwbi.classe.Cliente;
import br.com.dwbi.classe.ClientesNovos;
import br.com.dwbi.classe.MetaVenda;
import br.com.dwbi.classe.VendaAnoMes;
import br.com.dwbi.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbi.classe.VendasEmGeral;
import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.classe.Vendedor;
import br.com.dwbi.dwbi.dao.DAOGenerico;
import br.com.dwbi.dwbi.fabrica.EntityManagerProducerSige.Corporativo;
import br.com.dwbi.classe.PT_Meta;
import br.com.dwbi.classe.PT_Mix;
import br.com.dwbi.classe.HCliente;
import br.com.dwbi.classe.Produto;
import br.com.dwbi.classe.ProdutoVenda;
import br.com.dwbi.classe.MixProduto;
import br.com.dwbi.classe.PT_Carteira;
import br.com.dwbi.classe.Venda_Grupo;
import br.com.dwbi.classe.Venda_Subgrupo;

public class DAOGenericoHibernate<E> implements DAOGenerico<E>, Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	protected EntityManager manager;
	private Class classeEntidade;
	
	//sige
	@Inject
	@Corporativo
	protected EntityManager managerSige;

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
	
	public List<PT_Carteira> pt_carteira(String regiao,String vendedor1, String vendedor2){
		List<PT_Carteira> p = new ArrayList<>();
		
				String sql =
						 " select   "
								 + " y.regiao,  "
								 + " y.vendedor,  "
								 + " y.nomevendedor,  "
								 + " y.ativos,  "
								 + " y.ativos_mes,  "
								 + " y.pontos_evolucao,  "
								 + " y.novos,  "
								 + " y.novos *10 pontos_novos,  "
								 + " y.reativados,  "
								 + " y.reativados *5 pontos_reativado,  "
								 + "   y.ativos2,  "
								 + " y.ativos_mes2,  "
								 + " y.pontos_evolucao2,  "
								 + " y.novos2,  "
								 + " y.novos2 *10 pontos_novos2,  "
								 + " y.reativados2,   "
								 + " y.reativados2 *5 pontos_reativado2,  "
								 + "   y.ativos3,  "
								 + " y.ativos_mes3,  "
								 + " y.pontos_evolucao3,  "
								 + " y.novos3,   "
								 + " y.novos3 *10 pontos_novos3,  "
								 + " y.reativados3,  "
								 + " y.reativados3 *5 pontos_reativado3,  "
								 + " coalesce(y.pontos_evolucao,0) + coalesce(y.pontos_evolucao2,0) + coalesce(y.pontos_evolucao3,0) + coalesce((y.novos *10),0)+coalesce((y.novos2 *10),0)+coalesce((y.novos3 *10),0)+coalesce((y.reativados *5),0)+coalesce((y.reativados2 *5),0)+coalesce((y.reativados3 *5),0) totaltri1  "
								 + "   from(  "
								 + " select   "
								 + " r.NOME_REGIAO regiao,  "
								 + " v.CADCFTVID vendedor,  "
								 + " vv.NOME_CADCFTV nomevendedor,  "
								 + " x.ativos,  "
								 + " x.ativos_mes,  "
								 + " case when (x.ativos_mes-x.ativos) > 0 then 10 when (x.ativos_mes-x.ativos) < 0 then -10 else 0 end pontos_evolucao,  "
								 + " x.novos,  "
								 + " x.reativados,  "
								 + " 	x.ativos2,  "
								 + " x.ativos_mes2,  "
								 + " case when (x.ativos_mes2-x.ativos2) > 0 then 10 when (x.ativos_mes2-x.ativos2) < 0 then -10 else 0 end pontos_evolucao2,   "
								 + " x.novos2,  "
								 + " x.reativados2,  "
								 + "   x.ativos3,  "
								 + " x.ativos_mes3,  "
								 + " case when (x.ativos_mes3-x.ativos3) > 0 then 10 when (x.ativos_mes3-x.ativos3) < 0 then -10 else 0 end pontos_evolucao3,   "
								 + " x.novos3,  "
								 + " x.reativados3 "
				/*"select  "
				+ " y.regiao, "
				+ " y.vendedor, "
				+ " y.nomevendedor, "
				+ " y.ativos, "
				+ " y.ativos_mes, "
				+ " y.perc_evolucao, "
				+ " y.perc_evolucao *10 pontos_evolucao, "
				+ " y.novos, "
				+ " y.perc_novos, "
				+ " y.perc_novos *10 pontos_novos, "
				+ " y.reativados, "
				+ " y.perc_reativado, "
				+ " y.perc_reativado *10 pontos_reativado, "
				+ "  "
				+ " y.ativos2, "
				+ " y.ativos_mes2, "
				+ " y.perc_evolucao2, "
				+ " y.perc_evolucao2 *10 pontos_evolucao2, "
				+ " y.novos2, "
				+ " y.perc_novos2, "
				+ " y.perc_novos2 *10 pontos_novos2, "
				+ " y.reativados2, "
				+ " y.perc_reativado2, "
				+ " y.perc_reativado2 *10 pontos_reativado2, "
				+ "  "
				+ " y.ativos3, "
				+ " y.ativos_mes3, "
				+ " y.perc_evolucao3, "
				+ " y.perc_evolucao3 *10 pontos_evolucao3, "
				+ " y.novos3, "
				+ " y.perc_novos3, "
				+ " y.perc_novos3 *10 pontos_novos3, "
				+ " y.reativados3, "
				+ " y.perc_reativado3, "
				+ " y.perc_reativado3 *10 pontos_reativado3, "
				+ "  "
				+ " ((coalesce(y.perc_evolucao,0) *10)+(nvl(y.perc_novos,0) *10)+(nvl(y.perc_reativado,0) *10)) + ((coalesce(y.perc_evolucao2,0) *10)+(nvl(y.perc_novos2,0) *10)+(nvl(y.perc_reativado2,0) *10)) + ((coalesce(y.perc_evolucao3,0) *10)+(nvl(y.perc_novos3,0) *10)+(nvl(y.perc_reativado3,0) *10)) totaltri1 "
				+ "  "
				+ " from( "
				+ " select  "
				+ " r.NOME_REGIAO regiao, "
				+ " v.CADCFTVID vendedor, "
				+ " vv.NOME_CADCFTV nomevendedor, "
				+ " x.ativos, "
				+ " x.ativos_mes, "
				+ " case when ((x.ativos_mes-x.ativos)/x.ativos)*100 > 0 then "
				+ " ((x.ativos_mes-x.ativos)/x.ativos)*100+(1-((((x.ativos_mes-x.ativos)/x.ativos)*100)-trunc((((x.ativos_mes-x.ativos)/x.ativos)*100)))) else "
				+ " ((x.ativos_mes-x.ativos)/x.ativos)*100-(1+((((x.ativos_mes-x.ativos)/x.ativos)*100)-trunc((((x.ativos_mes-x.ativos)/x.ativos)*100)))) end perc_evolucao, "
				+ " x.novos, "
				+ " ((x.novos/x.ativos)*100)+ (1-(((x.novos/x.ativos)*100)-trunc(((x.novos/x.ativos)*100))))  perc_novos, "
				+ " x.reativados, "
				+ " ((x.reativados/x.ativos)*100)+ (1-(((x.reativados/x.ativos)*100)-trunc(((x.reativados/x.ativos)*100)))) perc_reativado, "
				+ "  "
				+ " x.ativos2, "
				+ " x.ativos_mes2, "
				+ " case when ((x.ativos_mes2-x.ativos2)/x.ativos2)*100 > 0 then "
				+ " ((x.ativos_mes2-x.ativos2)/x.ativos2)*100+(1-((((x.ativos_mes2-x.ativos2)/x.ativos2)*100)-trunc((((x.ativos_mes2-x.ativos2)/x.ativos2)*100)))) else "
				+ " ((x.ativos_mes2-x.ativos2)/x.ativos2)*100-(1+((((x.ativos_mes2-x.ativos2)/x.ativos2)*100)-trunc((((x.ativos_mes2-x.ativos2)/x.ativos2)*100)))) end perc_evolucao2, "
				+ " x.novos2, "
				+ " ((x.novos2/x.ativos2)*100)+ (1-(((x.novos2/x.ativos2)*100)-trunc(((x.novos2/x.ativos2)*100))))  perc_novos2, "
				+ " x.reativados2, "
				+ " ((x.reativados2/x.ativos2)*100)+ (1-(((x.reativados2/x.ativos2)*100)-trunc(((x.reativados2/x.ativos2)*100)))) perc_reativado2, "
				+ "  "
				+ " x.ativos3, "
				+ " x.ativos_mes3, "
				+ " case when ((x.ativos_mes3-x.ativos3)/x.ativos3)*100 > 0 then "
				+ " ((x.ativos_mes3-x.ativos3)/x.ativos3)*100+(1-((((x.ativos_mes3-x.ativos3)/x.ativos3)*100)-trunc((((x.ativos_mes3-x.ativos3)/x.ativos3)*100)))) else "
				+ " ((x.ativos_mes3-x.ativos3)/x.ativos3)*100-(1+((((x.ativos_mes3-x.ativos3)/x.ativos3)*100)-trunc((((x.ativos_mes3-x.ativos3)/x.ativos3)*100)))) end perc_evolucao3, "
				+ " x.novos3, "
				+ " ((x.novos3/x.ativos3)*100)+ (1-(((x.novos3/x.ativos3)*100)-trunc(((x.novos3/x.ativos3)*100))))  perc_novos3, "
				+ " x.reativados3, "
				+ " ((x.reativados3/x.ativos3)*100)+ (1-(((x.reativados3/x.ativos3)*100)-trunc(((x.reativados3/x.ativos3)*100)))) perc_reativado3 "
				+ "  "*/
				+ " from vendedor v "
				+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
				+ " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID "
				+ " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID "
				+ "  "
				+ " left join( "
				+ " select  "
				+ " cc.VENDEDORID1 vendedor, "
				+ " sum(novos.clientes) novos, "
				+ " sum(mes01.vendas_reativacao) reativados, "
				+ " sum(ativos.nativos) ativos, "
				+ " sum(ativosmes.mativos) ativos_mes, "
				+ "  "
				+ " sum(novos2.clientes) novos2, "
				+ " sum(mes02.vendas_reativacao) reativados2, "
				+ " sum(ativos2.nativos) ativos2, "
				+ " sum(ativosmes2.mativos) ativos_mes2, "
				+ "  "
				+ " sum(novos3.clientes) novos3, "
				+ " sum(mes03.vendas_reativacao) reativados3, "
				+ " sum(ativos3.nativos) ativos3, "
				+ " sum(ativosmes3.mativos) ativos_mes3 "
				+ "  "
				+ " from cadcftv c  "
				+ " inner join cliente cc on cc.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-01-2023' and '31-01-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos on novos.cadcftvid = c.cadcftvid  "
				+ "  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-12-2022', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-12-2022' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo on inativo.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-12-2022', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-12-2022' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos on ativos.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes on ativosmes.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2023' and '31-01-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes01 on mes01.cadcftvid = inativo.cadcftvid  "
				//+ " --* "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-02-2023' and '28-02-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos2 on novos2.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo2 on inativo2.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos2 on ativos2.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes2 on ativosmes2.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-02-2023' and '28-02-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes02 on mes02.cadcftvid = inativo2.cadcftvid  "
				//+ " --* "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-03-2023' and '31-03-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos3 on novos3.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo3 on inativo3.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos3 on ativos3.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-03-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-03-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes3 on ativosmes3.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-03-2023' and '31-03-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes03 on mes03.cadcftvid = inativo3.cadcftvid  "
				//+ " --* "
				+ " group by cc.VENDEDORID1 "
				+ " )x on x.vendedor = v.CADCFTVID "
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3) "
				+ " and v.cadcftvid =  "+ usuarioconectado()
				+ " order by v.CADCFTVID)y " ;
				
				//System.out.println(sql);
				
				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PT_Carteira pr = new PT_Carteira();
			
			pr.setRegiao((String)row[0]);
			pr.setVendedor((BigDecimal)row[1]);
			pr.setNomevendedor((String)row[2]);
			
			pr.setAtivos((BigInteger)row[3]);
			pr.setAtivos_mes((BigInteger)row[4]);
			//pr.setPerc_evolucao((Double)row[5]);
			pr.setPontos_evolucao((Integer)row[5]);
			pr.setNovos((BigDecimal)row[6]);
			//pr.setPerc_novos((BigDecimal)row[8]);
			pr.setPontos_novos((BigDecimal)row[7]);
			pr.setReativados((BigDecimal)row[8]);
			//pr.setPerc_reativado((BigDecimal)row[11]);
			pr.setPontos_reativados((BigDecimal)row[9]);
			
			pr.setAtivos2((BigInteger)row[10]);
			pr.setAtivos_mes2((BigInteger)row[11]);
			//pr.setPerc_evolucao2((Double)row[15]);
			pr.setPontos_evolucao2((Integer)row[12]);
			pr.setNovos2((BigDecimal)row[13]);
			//pr.setPerc_novos2((BigDecimal)row[18]);
			pr.setPontos_novos2((BigDecimal)row[14]);
			pr.setReativados2((BigDecimal)row[15]);
			//pr.setPerc_reativado2((BigDecimal)row[21]);
			pr.setPontos_reativados2((BigDecimal)row[16]);
			
			pr.setAtivos3((BigInteger)row[17]);
			pr.setAtivos_mes3((BigInteger)row[18]);
			//pr.setPerc_evolucao3((Double)row[25]);
			pr.setPontos_evolucao3((Integer)row[19]);
			pr.setNovos3((BigDecimal)row[20]);
			//pr.setPerc_novos3((BigDecimal)row[28]);
			pr.setPontos_novos3((BigDecimal)row[21]);
			pr.setReativados3((BigDecimal)row[22]);
			//pr.setPerc_reativado3((BigDecimal)row[31]);
			pr.setPontos_reativados3((BigDecimal)row[23]);
			
			pr.setTotaltri1((BigDecimal)row[24]);
			
			
			p.add(pr);
		}
		
		return p;
	}
	
	public List<PT_Carteira> pt_carteira2(String regiao,String vendedor1, String vendedor2){
		List<PT_Carteira> p = new ArrayList<>();
		
				String sql =
						" select  "
								+ " y.regiao, "
								+ " y.vendedor, "
								+ " y.nomevendedor, "
								+ "  y.ativos4, "
								+ " y.ativos_mes4, "
								+ " y.pontos_evolucao4, "
								+ " y.novos4, "
								+ " y.novos4 *10 pontos_novos4, "
								+ " y.reativados4, "
								+ " y.reativados4 *5 pontos_reativado4, "
								+ "  y.ativos5, "
								+ " y.ativos_mes5, "
								+ " y.pontos_evolucao5, "
								+ " y.novos5, "
								+ " y.novos5 *10 pontos_novos5, "
								+ " y.reativados5, "
								+ " y.reativados5 *5 pontos_reativado5, "
								+ "  y.ativos6, "
								+ " y.ativos_mes6, "
								+ " y.pontos_evolucao6, "
								+ " y.novos6, "
								+ " y.novos6 *10 pontos_novos6, "
								+ " y.reativados6, "
								+ " y.reativados6 *5 pontos_reativado6, "
								+ "  coalesce(y.pontos_evolucao4,0) + coalesce(y.pontos_evolucao5,0) + coalesce(y.pontos_evolucao6,0) + coalesce((y.novos4 *10),0)+coalesce((y.novos5 *10),0)+coalesce((y.novos6 *10),0)+coalesce((y.reativados4 *5),0)+coalesce((y.reativados5 *5),0)+coalesce((y.reativados6 *5),0) totaltri1  "
								+ "   from( "
								+ " select  "
								+ " r.NOME_REGIAO regiao, "
								+ " v.CADCFTVID vendedor, "
								+ " vv.NOME_CADCFTV nomevendedor, "
								+ "  x.ativos4, "
								+ " x.ativos_mes4, "
								+ " case when (x.ativos_mes4-x.ativos4) > 0 then 10 when (x.ativos_mes4-x.ativos4) < 0 then -10 else 0 end pontos_evolucao4, "
								+ " x.novos4, "
								+ " x.reativados4, "
								+ "  x.ativos5, "
								+ " x.ativos_mes5, "
								+ " case when (x.ativos_mes5-x.ativos5) > 0 then 10 when (x.ativos_mes5-x.ativos5) < 0 then -10 else 0 end pontos_evolucao5,  "
								+ " x.novos5, "
								+ " x.reativados5, "
								+ "  x.ativos6, "
								+ " x.ativos_mes6, "
								+ " case when (x.ativos_mes6-x.ativos6) > 0 then 10 when (x.ativos_mes6-x.ativos6) < 0 then -10 else 0 end pontos_evolucao6,  "
								+ " x.novos6, "
								+ " x.reativados6 "	
				/*" select "
				+ " y.regiao,"
				+ " y.vendedor,"
				+ " y.nomevendedor,"
				+ " "
				+ " y.ativos4,"
				+ " y.ativos_mes4,"
				+ " y.perc_evolucao4,"
				+ " y.perc_evolucao4 *10 pontos_evolucao4,"
				+ " y.novos4,"
				+ " y.perc_novos4,"
				+ " y.perc_novos4 *10 pontos_novos4,"
				+ " y.reativados4,"
				+ " y.perc_reativado4,"
				+ " y.perc_reativado4 *10 pontos_reativado4,"
				+ " "
				+ " y.ativos5,"
				+ " y.ativos_mes5,"
				+ " y.perc_evolucao5,"
				+ " y.perc_evolucao5 *10 pontos_evolucao5,"
				+ " y.novos5,"
				+ " y.perc_novos5,"
				+ " y.perc_novos5 *10 pontos_novos5,"
				+ " y.reativados5,"
				+ " y.perc_reativado5,"
				+ " y.perc_reativado5 *10 pontos_reativado5,"
				+ " "
				+ " y.ativos6,"
				+ " y.ativos_mes6,"
				+ " y.perc_evolucao6,"
				+ " y.perc_evolucao6 *10 pontos_evolucao6,"
				+ " y.novos6,"
				+ " y.perc_novos6,"
				+ " y.perc_novos6 *10 pontos_novos6,"
				+ " y.reativados6,"
				+ " y.perc_reativado6,"
				+ " y.perc_reativado6 *10 pontos_reativado6,"
				+ " "
				+ " ((coalesce(y.perc_evolucao4,0) *10)+(nvl(y.perc_novos4,0) *10)+(nvl(y.perc_reativado4,0) *10)) + ((coalesce(y.perc_evolucao5,0) *10)+(nvl(y.perc_novos5,0) *10)+(nvl(y.perc_reativado5,0) *10)) + ((coalesce(y.perc_evolucao6,0) *10)+(nvl(y.perc_novos6,0) *10)+(nvl(y.perc_reativado6,0) *10)) totaltri2"
				+ " "
				+ " from("
				+ " select "
				+ " r.NOME_REGIAO regiao,"
				+ " v.CADCFTVID vendedor,"
				+ " vv.NOME_CADCFTV nomevendedor,"
				+ " "
				+ " x.ativos4,"
				+ " x.ativos_mes4,"
				+ " case when ((x.ativos_mes4-x.ativos4)/x.ativos4)*100 > 0 then"
				+ " ((x.ativos_mes4-x.ativos4)/x.ativos4)*100+(1-((((x.ativos_mes4-x.ativos4)/x.ativos4)*100)-trunc((((x.ativos_mes4-x.ativos4)/x.ativos4)*100)))) else"
				+ " ((x.ativos_mes4-x.ativos4)/x.ativos4)*100-(1+((((x.ativos_mes4-x.ativos4)/x.ativos4)*100)-trunc((((x.ativos_mes4-x.ativos4)/x.ativos4)*100)))) end perc_evolucao4,"
				+ " x.novos4,"
				+ " ((x.novos4/x.ativos4)*100)+ (1-(((x.novos4/x.ativos4)*100)-trunc(((x.novos4/x.ativos4)*100))))  perc_novos4,"
				+ " x.reativados4,"
				+ " ((x.reativados4/x.ativos4)*100)+ (1-(((x.reativados4/x.ativos4)*100)-trunc(((x.reativados4/x.ativos4)*100)))) perc_reativado4,"
				+ " "
				+ " x.ativos5,"
				+ " x.ativos_mes5,"
				+ " case when ((x.ativos_mes5-x.ativos5)/x.ativos5)*100 > 0 then"
				+ " ((x.ativos_mes5-x.ativos5)/x.ativos5)*100+(1-((((x.ativos_mes5-x.ativos5)/x.ativos5)*100)-trunc((((x.ativos_mes5-x.ativos5)/x.ativos5)*100)))) else"
				+ " ((x.ativos_mes5-x.ativos5)/x.ativos5)*100-(1+((((x.ativos_mes5-x.ativos5)/x.ativos5)*100)-trunc((((x.ativos_mes5-x.ativos5)/x.ativos5)*100)))) end perc_evolucao5,"
				+ " x.novos5,"
				+ " ((x.novos5/x.ativos5)*100)+ (1-(((x.novos5/x.ativos5)*100)-trunc(((x.novos5/x.ativos5)*100))))  perc_novos5,"
				+ " x.reativados5,"
				+ " ((x.reativados5/x.ativos5)*100)+ (1-(((x.reativados5/x.ativos5)*100)-trunc(((x.reativados5/x.ativos5)*100)))) perc_reativado5,"
				+ " "
				+ " x.ativos6,"
				+ " x.ativos_mes6,"
				+ " case when ((x.ativos_mes6-x.ativos6)/x.ativos6)*100 > 0 then"
				+ " ((x.ativos_mes6-x.ativos6)/x.ativos6)*100+(1-((((x.ativos_mes6-x.ativos6)/x.ativos6)*100)-trunc((((x.ativos_mes6-x.ativos6)/x.ativos6)*100)))) else"
				+ " ((x.ativos_mes6-x.ativos6)/x.ativos6)*100-(1+((((x.ativos_mes6-x.ativos6)/x.ativos6)*100)-trunc((((x.ativos_mes6-x.ativos6)/x.ativos6)*100)))) end perc_evolucao6,"
				+ " x.novos6,"
				+ " ((x.novos6/x.ativos6)*100)+ (1-(((x.novos6/x.ativos6)*100)-trunc(((x.novos6/x.ativos6)*100))))  perc_novos6,"
				+ " x.reativados6,"
				+ " ((x.reativados6/x.ativos6)*100)+ (1-(((x.reativados6/x.ativos6)*100)-trunc(((x.reativados6/x.ativos6)*100)))) perc_reativado6"
				+ " "*/
				+ " from vendedor v"
				+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID"
				+ " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID"
				+ " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID"
				+ " "
				+ " left join("
				+ " select "
				+ " cc.VENDEDORID1 vendedor,"
				+ " sum(novos4.clientes) novos4,"
				+ " sum(mes04.vendas_reativacao) reativados4,"
				+ " sum(ativos4.nativos) ativos4,"
				+ " sum(ativosmes4.mativos) ativos_mes4,"
				+ " "
				+ " sum(novos5.clientes) novos5,"
				+ " sum(mes05.vendas_reativacao) reativados5,"
				+ " sum(ativos5.nativos) ativos5,"
				+ " sum(ativosmes5.mativos) ativos_mes5,"
				+ " "
				+ " sum(novos6.clientes) novos6,"
				+ " sum(mes06.vendas_reativacao) reativados6,"
				+ " sum(ativos6.nativos) ativos6,"
				+ " sum(ativosmes6.mativos) ativos_mes6"
				+ " "
				+ " from cadcftv c "
				+ " inner join cliente cc on cc.CADCFTVID = c.CADCFTVID"
				+ " "
				+ " left join( "
				+ " select "
				+ " p.cadcftvid,"
				+ " count(*) clientes "
				+ " from("
				+ " select "
				+ " p.cadcftvid, "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra"
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " group by p.cadcftvid )p"
				+ " where p.primeiracompra between '01-04-2023' and '30-04-2023'"
				+ " group by p.cadcftvid"
				+ " ) novos4 on novos4.cadcftvid = c.cadcftvid "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID,x.dias  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('31-03-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-03-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias > 180 "
				+ " )inativo4 on inativo4.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 nativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('31-03-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-03-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativos4 on ativos4.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 mativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('30-04-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-04-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativosmes4 on ativosmes4.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select "
				+ " p.cadcftvid, "
				+ " count(*) vendas_reativacao "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.DT_PEDIDOVENDA between  '01-04-2023' and '30-04-2023'"
				+ " group by p.cadcftvid "
				+ " ) mes04 on mes04.cadcftvid = inativo4.cadcftvid "
				+ " left join( "
				+ " select "
				+ " p.cadcftvid,"
				+ " count(*) clientes "
				+ " from("
				+ " select "
				+ " p.cadcftvid, "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra"
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " group by p.cadcftvid )p"
				+ " where p.primeiracompra between '01-05-2023' and '31-05-2023'"
				+ " group by p.cadcftvid"
				+ " ) novos5 on novos5.cadcftvid = c.cadcftvid "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID,x.dias  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('30-04-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-04-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias > 180 "
				+ " )inativo5 on inativo5.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 nativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('30-04-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-04-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativos5 on ativos5.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 mativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('31-05-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-05-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativosmes5 on ativosmes5.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select "
				+ " p.cadcftvid, "
				+ " count(*) vendas_reativacao "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.DT_PEDIDOVENDA between  '01-05-2023' and '31-05-2023'"
				+ " group by p.cadcftvid "
				+ " ) mes05 on mes05.cadcftvid = inativo5.cadcftvid"
				
				+ " left join( "
				+ " select "
				+ " p.cadcftvid,"
				+ " count(*) clientes "
				+ " from("
				+ " select "
				+ " p.cadcftvid, "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra"
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " group by p.cadcftvid )p"
				+ " where p.primeiracompra between '01-06-2023' and '30-06-2023'"
				+ " group by p.cadcftvid"
				+ " ) novos6 on novos6.cadcftvid = c.cadcftvid "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID,x.dias  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('31-05-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-05-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias > 180 "
				+ " )inativo6 on inativo6.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 nativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('31-05-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-05-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativos6 on ativos6.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select x.CADCFTVID ,1 mativos  from("
				+ " select "
				+ " p.CADCFTVID,"
				+ " extract(days from TO_DATE('30-06-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-06-2023'"
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID)x"
				+ " where x.dias <= 180 "
				+ " )ativosmes6 on ativosmes6.CADCFTVID = c.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select "
				+ " p.cadcftvid, "
				+ " count(*) vendas_reativacao "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.DT_PEDIDOVENDA between  '01-06-2023' and '30-06-2023'"
				+ " group by p.cadcftvid "
				+ " ) mes06 on mes06.cadcftvid = inativo6.cadcftvid"
				+ " "
				+ " group by cc.VENDEDORID1"
				+ " )x on x.vendedor = v.CADCFTVID"
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3)"
				+ "  and v.cadcftvid =  "+ usuarioconectado()
				+ "  order by v.CADCFTVID)y " ;			
				
				//System.out.println(sql);
				
				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
PT_Carteira pr = new PT_Carteira();
			
pr.setRegiao((String)row[0]);
pr.setVendedor((BigDecimal)row[1]);
pr.setNomevendedor((String)row[2]);

pr.setAtivos((BigInteger)row[3]);
pr.setAtivos_mes((BigInteger)row[4]);
//pr.setPerc_evolucao((Double)row[5]);
pr.setPontos_evolucao((Integer)row[5]);
pr.setNovos((BigDecimal)row[6]);
//pr.setPerc_novos((BigDecimal)row[8]);
pr.setPontos_novos((BigDecimal)row[7]);
pr.setReativados((BigDecimal)row[8]);
//pr.setPerc_reativado((BigDecimal)row[11]);
pr.setPontos_reativados((BigDecimal)row[9]);

pr.setAtivos2((BigInteger)row[10]);
pr.setAtivos_mes2((BigInteger)row[11]);
//pr.setPerc_evolucao2((Double)row[15]);
pr.setPontos_evolucao2((Integer)row[12]);
pr.setNovos2((BigDecimal)row[13]);
//pr.setPerc_novos2((BigDecimal)row[18]);
pr.setPontos_novos2((BigDecimal)row[14]);
pr.setReativados2((BigDecimal)row[15]);
//pr.setPerc_reativado2((BigDecimal)row[21]);
pr.setPontos_reativados2((BigDecimal)row[16]);

pr.setAtivos3((BigInteger)row[17]);
pr.setAtivos_mes3((BigInteger)row[18]);
//pr.setPerc_evolucao3((Double)row[25]);
pr.setPontos_evolucao3((Integer)row[19]);
pr.setNovos3((BigDecimal)row[20]);
//pr.setPerc_novos3((BigDecimal)row[28]);
pr.setPontos_novos3((BigDecimal)row[21]);
pr.setReativados3((BigDecimal)row[22]);
//pr.setPerc_reativado3((BigDecimal)row[31]);
pr.setPontos_reativados3((BigDecimal)row[23]);

pr.setTotaltri1((BigDecimal)row[24]);
			
			
			p.add(pr);
		}
		
		return p;
	}
	
	public List<PT_Carteira> pt_carteira3(String regiao,String vendedor1, String vendedor2){
		List<PT_Carteira> p = new ArrayList<>();
		
				String sql =
						"  select   "
								+ " y.regiao,  "
								+ " y.vendedor,  "
								+ " y.nomevendedor,  "
								+ "   y.ativos7,  "
								+ " y.ativos_mes7,   "
								+ " y.pontos_evolucao7,  "
								+ " y.novos7,   "
								+ " y.novos7 *10 pontos_novos7,  "
								+ " y.reativados7,  "
								+ " y.reativados7 *5 pontos_reativado7,  "
								+ "   y.ativos8,  "
								+ " y.ativos_mes8,  "
								+ " y.pontos_evolucao8,  "
								+ " y.novos8,   "
								+ " y.novos8 *10 pontos_novos8,  "
								+ " y.reativados8,  "
								+ " y.reativados8 *5 pontos_reativado8,  "
								+ "   y.ativos9,  "
								+ " y.ativos_mes9,   "
								+ " y.pontos_evolucao9,  "
								+ " y.novos9,  "
								+ " y.novos9 *10 pontos_novos9,  "
								+ " y.reativados9,  "
								+ " y.reativados9 *5 pontos_reativado9,  "
								+ "  coalesce(y.pontos_evolucao7,0) + coalesce(y.pontos_evolucao8,0) + coalesce(y.pontos_evolucao9,0) + coalesce((y.novos7 *10),0)+coalesce((y.novos8 *10),0)+coalesce((y.novos9 *10),0)+coalesce((y.reativados7 *5),0)+coalesce((y.reativados8 *5),0)+coalesce((y.reativados9 *5),0) totaltri1  "
								+ "    from(  "
								+ " select   "
								+ " r.NOME_REGIAO regiao,  "
								+ " v.CADCFTVID vendedor,  "
								+ " vv.NOME_CADCFTV nomevendedor,  "
								+ "   x.ativos7,  "
								+ " x.ativos_mes7,  "
								+ " case when (x.ativos_mes7-x.ativos7) > 0 then 10 when (x.ativos_mes7-x.ativos7) < 0 then -10 else 0 end pontos_evolucao7,  "
								+ " x.novos7,  "
								+ " x.reativados7,   "
								+ "   x.ativos8,  "
								+ " x.ativos_mes8,  "
								+ " case when (x.ativos_mes8-x.ativos8) > 0 then 10 when (x.ativos_mes8-x.ativos8) < 0 then -10 else 0 end pontos_evolucao8,  "
								+ " x.novos8,   "
								+ " x.reativados8,   "
								+ "   x.ativos9,  "
								+ " x.ativos_mes9,  "
								+ " case when (x.ativos_mes9-x.ativos9) > 0 then 10 when (x.ativos_mes9-x.ativos9) < 0 then -10 else 0 end pontos_evolucao9,  "
								+ " x.novos9,   "
								+ " x.reativados9 "
				/*" select  "
				+ " y.regiao, "
				+ " y.vendedor, "
				+ " y.nomevendedor, "
				+ "  "
				+ " y.ativos7, "
				+ " y.ativos_mes7, "
				+ " y.perc_evolucao7, "
				+ " y.perc_evolucao7 *10 pontos_evolucao7, "
				+ " y.novos7, "
				+ " y.perc_novos7, "
				+ " y.perc_novos7 *10 pontos_novos7, "
				+ " y.reativados7, "
				+ " y.perc_reativado7, "
				+ " y.perc_reativado7 *10 pontos_reativado7, "
				+ "  "
				+ " y.ativos8, "
				+ " y.ativos_mes8, "
				+ " y.perc_evolucao8, "
				+ " y.perc_evolucao8 *10 pontos_evolucao8, "
				+ " y.novos8, "
				+ " y.perc_novos8, "
				+ " y.perc_novos8 *10 pontos_novos8, "
				+ " y.reativados8, "
				+ " y.perc_reativado8, "
				+ " y.perc_reativado8 *10 pontos_reativado8, "
				+ "  "
				+ " y.ativos9, "
				+ " y.ativos_mes9, "
				+ " y.perc_evolucao9, "
				+ " y.perc_evolucao9 *10 pontos_evolucao9, "
				+ " y.novos9, "
				+ " y.perc_novos9, "
				+ " y.perc_novos9 *10 pontos_novos9, "
				+ " y.reativados9, "
				+ " y.perc_reativado9, "
				+ " y.perc_reativado9 *10 pontos_reativado9, "
				+ "  "
				+ " ((coalesce(y.perc_evolucao7,0) *10)+(nvl(y.perc_novos7,0) *10)+(nvl(y.perc_reativado7,0) *10)) + ((coalesce(y.perc_evolucao8,0) *10)+(nvl(y.perc_novos8,0) *10)+(nvl(y.perc_reativado8,0) *10)) + ((coalesce(y.perc_evolucao9,0) *10)+(nvl(y.perc_novos9,0) *10)+(nvl(y.perc_reativado9,0) *10)) totaltri2 "
				+ "  "
				+ " from( "
				+ " select  "
				+ " r.NOME_REGIAO regiao, "
				+ " v.CADCFTVID vendedor, "
				+ " vv.NOME_CADCFTV nomevendedor, "
				+ "  "
				+ " x.ativos7, "
				+ " x.ativos_mes7, "
				+ " case when ((x.ativos_mes7-x.ativos7)/x.ativos7)*100 > 0 then "
				+ " ((x.ativos_mes7-x.ativos7)/x.ativos7)*100+(1-((((x.ativos_mes7-x.ativos7)/x.ativos7)*100)-trunc((((x.ativos_mes7-x.ativos7)/x.ativos7)*100)))) else "
				+ " ((x.ativos_mes7-x.ativos7)/x.ativos7)*100-(1+((((x.ativos_mes7-x.ativos7)/x.ativos7)*100)-trunc((((x.ativos_mes7-x.ativos7)/x.ativos7)*100)))) end perc_evolucao7, "
				+ " x.novos7, "
				+ " ((x.novos7/x.ativos7)*100)+ (1-(((x.novos7/x.ativos7)*100)-trunc(((x.novos7/x.ativos7)*100))))  perc_novos7, "
				+ " x.reativados7, "
				+ " ((x.reativados7/x.ativos7)*100)+ (1-(((x.reativados7/x.ativos7)*100)-trunc(((x.reativados7/x.ativos7)*100)))) perc_reativado7, "
				+ "  "
				+ " x.ativos8, "
				+ " x.ativos_mes8, "
				+ " case when ((x.ativos_mes8-x.ativos8)/x.ativos8)*100 > 0 then "
				+ " ((x.ativos_mes8-x.ativos8)/x.ativos8)*100+(1-((((x.ativos_mes8-x.ativos8)/x.ativos8)*100)-trunc((((x.ativos_mes8-x.ativos8)/x.ativos8)*100)))) else "
				+ " ((x.ativos_mes8-x.ativos8)/x.ativos8)*100-(1+((((x.ativos_mes8-x.ativos8)/x.ativos8)*100)-trunc((((x.ativos_mes8-x.ativos8)/x.ativos8)*100)))) end perc_evolucao8, "
				+ " x.novos8, "
				+ " ((x.novos8/x.ativos8)*100)+ (1-(((x.novos8/x.ativos8)*100)-trunc(((x.novos8/x.ativos8)*100))))  perc_novos8, "
				+ " x.reativados8, "
				+ " ((x.reativados8/x.ativos8)*100)+ (1-(((x.reativados8/x.ativos8)*100)-trunc(((x.reativados8/x.ativos8)*100)))) perc_reativado8, "
				+ "  "
				+ " x.ativos9, "
				+ " x.ativos_mes9, "
				+ " case when ((x.ativos_mes9-x.ativos9)/x.ativos9)*100 > 0 then "
				+ " ((x.ativos_mes9-x.ativos9)/x.ativos9)*100+(1-((((x.ativos_mes9-x.ativos9)/x.ativos9)*100)-trunc((((x.ativos_mes9-x.ativos9)/x.ativos9)*100)))) else "
				+ " ((x.ativos_mes9-x.ativos9)/x.ativos9)*100-(1+((((x.ativos_mes9-x.ativos9)/x.ativos9)*100)-trunc((((x.ativos_mes9-x.ativos9)/x.ativos9)*100)))) end perc_evolucao9, "
				+ " x.novos9, "
				+ " ((x.novos9/x.ativos9)*100)+ (1-(((x.novos9/x.ativos9)*100)-trunc(((x.novos9/x.ativos9)*100))))  perc_novos9, "
				+ " x.reativados9, "
				+ " ((x.reativados9/x.ativos9)*100)+ (1-(((x.reativados9/x.ativos9)*100)-trunc(((x.reativados9/x.ativos9)*100)))) perc_reativado9 "
				+ "  "*/
				+ " from vendedor v "
				+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
				+ " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID "
				+ " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID "
				+ "  "
				+ " left join( "
				+ " select  "
				+ " cc.VENDEDORID1 vendedor, "
				+ " sum(novos7.clientes) novos7, "
				+ " sum(mes07.vendas_reativacao) reativados7, "
				+ " sum(ativos7.nativos) ativos7, "
				+ " sum(ativosmes7.mativos) ativos_mes7, "
				+ "  "
				+ " sum(novos8.clientes) novos8, "
				+ " sum(mes08.vendas_reativacao) reativados8, "
				+ " sum(ativos8.nativos) ativos8, "
				+ " sum(ativosmes8.mativos) ativos_mes8, "
				+ "  "
				+ " sum(novos9.clientes) novos9, "
				+ " sum(mes09.vendas_reativacao) reativados9, "
				+ " sum(ativos9.nativos) ativos9, "
				+ " sum(ativosmes9.mativos) ativos_mes9 "
				+ "  "
				+ " from cadcftv c  "
				+ " inner join cliente cc on cc.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-07-2023' and '31-07-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos7 on novos7.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-06-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-06-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo7 on inativo7.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-06-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-06-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos7 on ativos7.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-07-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-07-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes7 on ativosmes7.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-07-2023' and '31-07-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes07 on mes07.cadcftvid = inativo7.cadcftvid "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-08-2023' and '31-08-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos8 on novos8.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-07-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-07-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo8 on inativo8.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-07-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-07-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos8 on ativos8.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-08-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-08-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes8 on ativosmes8.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-08-2023' and '31-08-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes08 on mes08.cadcftvid = inativo8.cadcftvid "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-09-2023' and '30-09-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos9 on novos9.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-08-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-08-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo9 on inativo9.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-08-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-08-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos9 on ativos9.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-09-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-09-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes9 on ativosmes9.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-09-2023' and '30-09-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes09 on mes09.cadcftvid = inativo9.cadcftvid "
				+ "  "
				+ " group by cc.VENDEDORID1 "
				+ " )x on x.vendedor = v.CADCFTVID "
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3) "
				+ "  and v.cadcftvid =  "+ usuarioconectado()
				+ "  order by v.CADCFTVID)y " ;				
				
				//System.out.println(sql);
				
				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
PT_Carteira pr = new PT_Carteira();
			
pr.setRegiao((String)row[0]);
pr.setVendedor((BigDecimal)row[1]);
pr.setNomevendedor((String)row[2]);

pr.setAtivos((BigInteger)row[3]);
pr.setAtivos_mes((BigInteger)row[4]);
//pr.setPerc_evolucao((Double)row[5]);
pr.setPontos_evolucao((Integer)row[5]);
pr.setNovos((BigDecimal)row[6]);
//pr.setPerc_novos((BigDecimal)row[8]);
pr.setPontos_novos((BigDecimal)row[7]);
pr.setReativados((BigDecimal)row[8]);
//pr.setPerc_reativado((BigDecimal)row[11]);
pr.setPontos_reativados((BigDecimal)row[9]);

pr.setAtivos2((BigInteger)row[10]);
pr.setAtivos_mes2((BigInteger)row[11]);
//pr.setPerc_evolucao2((Double)row[15]);
pr.setPontos_evolucao2((Integer)row[12]);
pr.setNovos2((BigDecimal)row[13]);
//pr.setPerc_novos2((BigDecimal)row[18]);
pr.setPontos_novos2((BigDecimal)row[14]);
pr.setReativados2((BigDecimal)row[15]);
//pr.setPerc_reativado2((BigDecimal)row[21]);
pr.setPontos_reativados2((BigDecimal)row[16]);

pr.setAtivos3((BigInteger)row[17]);
pr.setAtivos_mes3((BigInteger)row[18]);
//pr.setPerc_evolucao3((Double)row[25]);
pr.setPontos_evolucao3((Integer)row[19]);
pr.setNovos3((BigDecimal)row[20]);
//pr.setPerc_novos3((BigDecimal)row[28]);
pr.setPontos_novos3((BigDecimal)row[21]);
pr.setReativados3((BigDecimal)row[22]);
//pr.setPerc_reativado3((BigDecimal)row[31]);
pr.setPontos_reativados3((BigDecimal)row[23]);

pr.setTotaltri1((BigDecimal)row[24]);
			
			
			p.add(pr);
		}
		
		return p;
	}
	
	public List<PT_Carteira> pt_carteira4(String regiao,String vendedor1, String vendedor2){
		List<PT_Carteira> p = new ArrayList<>();
		
				String sql =
						"select   "
								+ " y.regiao,  "
								+ " y.vendedor,  "
								+ " y.nomevendedor,  "
								+ "   y.ativos10,  "
								+ " y.ativos_mes10,  "
								+ " y.pontos_evolucao10,  "
								+ " y.novos10,  "
								+ " y.novos10 *10 pontos_novos10,  "
								+ " y.reativados10,  "
								+ " y.reativados10 *5 pontos_reativado10,  "
								+ "   y.ativos11,  "
								+ " y.ativos_mes11,   "
								+ " y.pontos_evolucao11,  "
								+ " y.novos11,  "
								+ " y.novos11 *10 pontos_novos11,  "
								+ " y.reativados11,  "
								+ " y.reativados11 *5 pontos_reativado11,  "
								+ "   y.ativos12,  "
								+ " y.ativos_mes12,   "
								+ " y.pontos_evolucao12,  "
								+ " y.novos12,  "
								+ " y.novos12 *10 pontos_novos12,  "
								+ " y.reativados12,  "
								+ " y.reativados12 *5 pontos_reativado12,  "
								+ "  coalesce(y.pontos_evolucao10,0) + coalesce(y.pontos_evolucao11,0) + coalesce(y.pontos_evolucao12,0) + coalesce((y.novos10 *10),0)+coalesce((y.novos11 *10),0)+coalesce((y.novos12 *10),0)+coalesce((y.reativados10 *5),0)+coalesce((y.reativados11 *5),0)+coalesce((y.reativados12 *5),0) totaltri1  "
								+ "  from(  "
								+ " select   "
								+ " r.NOME_REGIAO regiao,  "
								+ " v.CADCFTVID vendedor,  "
								+ " vv.NOME_CADCFTV nomevendedor,  "
								+ "   x.ativos10,  "
								+ " x.ativos_mes10,  "
								+ " case when (x.ativos_mes10-x.ativos10) > 0 then 10 when (x.ativos_mes10-x.ativos10) < 0 then -10 else 0 end pontos_evolucao10,  "
								+ " x.novos10,  "
								+ " x.reativados10,  "
								+ "   x.ativos11,  "
								+ " x.ativos_mes11,  "
								+ "case when (x.ativos_mes11-x.ativos11) > 0 then 10 when (x.ativos_mes11-x.ativos11) < 0 then -10 else 0 end pontos_evolucao11,  "
								+ " x.novos11,  "
								+ " x.reativados11,  "
								+ "   x.ativos12,  "
								+ " x.ativos_mes12,  "
								+ " case when (x.ativos_mes12-x.ativos12) > 0 then 10 when (x.ativos_mes12-x.ativos12) < 0 then -10 else 0 end pontos_evolucao12,  "
								+ " x.novos12,  "
								+ " x.reativados12"						
				/*" select  "
				+ " y.regiao, "
				+ " y.vendedor, "
				+ " y.nomevendedor, "
				+ "  "
				+ " y.ativos10, "
				+ " y.ativos_mes10, "
				+ " y.perc_evolucao10, "
				+ " y.perc_evolucao10 *10 pontos_evolucao10, "
				+ " y.novos10, "
				+ " y.perc_novos10, "
				+ " y.perc_novos10 *10 pontos_novos10, "
				+ " y.reativados10, "
				+ " y.perc_reativado10, "
				+ " y.perc_reativado10 *10 pontos_reativado10, "
				+ "  "
				+ " y.ativos11, "
				+ " y.ativos_mes11, "
				+ " y.perc_evolucao11, "
				+ " y.perc_evolucao11 *10 pontos_evolucao11, "
				+ " y.novos11, "
				+ " y.perc_novos11, "
				+ " y.perc_novos11 *10 pontos_novos11, "
				+ " y.reativados11, "
				+ " y.perc_reativado11, "
				+ " y.perc_reativado11 *10 pontos_reativado11, "
				+ "  "
				+ " y.ativos12, "
				+ " y.ativos_mes12, "
				+ " y.perc_evolucao12, "
				+ " y.perc_evolucao12 *10 pontos_evolucao12, "
				+ " y.novos12, "
				+ " y.perc_novos12, "
				+ " y.perc_novos12 *10 pontos_novos12, "
				+ " y.reativados12, "
				+ " y.perc_reativado12, "
				+ " y.perc_reativado12 *10 pontos_reativado12, "
				+ "  "
				+ " ((coalesce(y.perc_evolucao10,0) *10)+(nvl(y.perc_novos10,0) *10)+(nvl(y.perc_reativado10,0) *10)) + ((coalesce(y.perc_evolucao11,0) *10)+(nvl(y.perc_novos11,0) *10)+(nvl(y.perc_reativado11,0) *10)) + ((coalesce(y.perc_evolucao12,0) *10)+(nvl(y.perc_novos12,0) *10)+(nvl(y.perc_reativado12,0) *10)) totaltri2 "
				+ "  "
				+ " from( "
				+ " select  "
				+ " r.NOME_REGIAO regiao, "
				+ " v.CADCFTVID vendedor, "
				+ " vv.NOME_CADCFTV nomevendedor, "
				+ "  "
				+ " x.ativos10, "
				+ " x.ativos_mes10, "
				+ " case when ((x.ativos_mes10-x.ativos10)/x.ativos10)*100 > 0 then "
				+ " ((x.ativos_mes10-x.ativos10)/x.ativos10)*100+(1-((((x.ativos_mes10-x.ativos10)/x.ativos10)*100)-trunc((((x.ativos_mes10-x.ativos10)/x.ativos10)*100)))) else "
				+ " ((x.ativos_mes10-x.ativos10)/x.ativos10)*100-(1+((((x.ativos_mes10-x.ativos10)/x.ativos10)*100)-trunc((((x.ativos_mes10-x.ativos10)/x.ativos10)*100)))) end perc_evolucao10, "
				+ " x.novos10, "
				+ " ((x.novos10/x.ativos10)*100)+ (1-(((x.novos10/x.ativos10)*100)-trunc(((x.novos10/x.ativos10)*100))))  perc_novos10, "
				+ " x.reativados10, "
				+ " ((x.reativados10/x.ativos10)*100)+ (1-(((x.reativados10/x.ativos10)*100)-trunc(((x.reativados10/x.ativos10)*100)))) perc_reativado10, "
				+ "  "
				+ " x.ativos11, "
				+ " x.ativos_mes11, "
				+ " case when ((x.ativos_mes11-x.ativos11)/x.ativos11)*100 > 0 then "
				+ " ((x.ativos_mes11-x.ativos11)/x.ativos11)*100+(1-((((x.ativos_mes11-x.ativos11)/x.ativos11)*100)-trunc((((x.ativos_mes11-x.ativos11)/x.ativos11)*100)))) else "
				+ " ((x.ativos_mes11-x.ativos11)/x.ativos11)*100-(1+((((x.ativos_mes11-x.ativos11)/x.ativos11)*100)-trunc((((x.ativos_mes11-x.ativos11)/x.ativos11)*100)))) end perc_evolucao11, "
				+ " x.novos11, "
				+ " ((x.novos11/x.ativos11)*100)+ (1-(((x.novos11/x.ativos11)*100)-trunc(((x.novos11/x.ativos11)*100))))  perc_novos11, "
				+ " x.reativados11, "
				+ " ((x.reativados11/x.ativos11)*100)+ (1-(((x.reativados11/x.ativos11)*100)-trunc(((x.reativados11/x.ativos11)*100)))) perc_reativado11, "
				+ "  "
				+ " x.ativos12, "
				+ " x.ativos_mes12, "
				+ " case when ((x.ativos_mes12-x.ativos12)/x.ativos12)*100 > 0 then "
				+ " ((x.ativos_mes12-x.ativos12)/x.ativos12)*100+(1-((((x.ativos_mes12-x.ativos12)/x.ativos12)*100)-trunc((((x.ativos_mes12-x.ativos12)/x.ativos12)*100)))) else "
				+ " ((x.ativos_mes12-x.ativos12)/x.ativos12)*100-(1+((((x.ativos_mes12-x.ativos12)/x.ativos12)*100)-trunc((((x.ativos_mes12-x.ativos12)/x.ativos12)*100)))) end perc_evolucao12, "
				+ " x.novos12, "
				+ " ((x.novos12/x.ativos12)*100)+ (1-(((x.novos12/x.ativos12)*100)-trunc(((x.novos12/x.ativos12)*100))))  perc_novos12, "
				+ " x.reativados12, "
				+ " ((x.reativados12/x.ativos12)*100)+ (1-(((x.reativados12/x.ativos12)*100)-trunc(((x.reativados12/x.ativos12)*100)))) perc_reativado12 "
				+ "  "*/
				+ " from vendedor v "
				+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
				+ " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID "
				+ " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID "
				+ "  "
				+ " left join( "
				+ " select  "
				+ " cc.VENDEDORID1 vendedor, "
				+ " sum(novos10.clientes) novos10, "
				+ " sum(mes10.vendas_reativacao) reativados10, "
				+ " sum(ativos10.nativos) ativos10, "
				+ " sum(ativosmes10.mativos) ativos_mes10, "
				+ "  "
				+ " sum(novos11.clientes) novos11, "
				+ " sum(mes11.vendas_reativacao) reativados11, "
				+ " sum(ativos11.nativos) ativos11, "
				+ " sum(ativosmes11.mativos) ativos_mes11, "
				+ "  "
				+ " sum(novos12.clientes) novos12, "
				+ " sum(mes12.vendas_reativacao) reativados12, "
				+ " sum(ativos12.nativos) ativos12, "
				+ " sum(ativosmes12.mativos) ativos_mes12 "
				+ "  "
				+ " from cadcftv c  "
				+ " inner join cliente cc on cc.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-10-2023' and '31-10-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos10 on novos10.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-09-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-09-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo10 on inativo10.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-09-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-09-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos10 on ativos10.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-10-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-10-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes10 on ativosmes10.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-10-2023' and '31-10-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes10 on mes10.cadcftvid = inativo10.cadcftvid "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-11-2023' and '30-11-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos11 on novos11.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-10-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-10-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo11 on inativo11.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-10-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-10-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos11 on ativos11.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-11-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-11-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes11 on ativosmes11.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-11-2023' and '30-11-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes11 on mes11.cadcftvid = inativo11.cadcftvid "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid, "
				+ " count(*) clientes  "
				+ " from( "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " min(p.DT_PEDIDOVENDA) primeiracompra "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " group by p.cadcftvid )p "
				+ " where p.primeiracompra between '01-12-2023' and '31-12-2023' "
				+ " group by p.cadcftvid "
				+ " ) novos12 on novos12.cadcftvid = c.cadcftvid  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID,x.dias  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-11-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-11-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias > 180  "
				+ " )inativo12 on inativo12.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 nativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('30-11-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '30-11-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativos12 on ativos12.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select x.CADCFTVID ,1 mativos  from( "
				+ " select  "
				+ " p.CADCFTVID, "
				+ " extract(days from TO_DATE('31-12-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-12-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID)x "
				+ " where x.dias <= 180  "
				+ " )ativosmes12 on ativosmes12.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.cadcftvid,  "
				+ " count(*) vendas_reativacao  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between  '01-12-2023' and '31-12-2023' "
				+ " group by p.cadcftvid  "
				+ " ) mes12 on mes12.cadcftvid = inativo12.cadcftvid  "
				
				+ " group by cc.VENDEDORID1 "
				+ " )x on x.vendedor = v.CADCFTVID "
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3) "
				+ " and v.cadcftvid =  "+ usuarioconectado()
				+ " order by v.CADCFTVID)y " ;
				
				//System.out.println(sql);
				
				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
PT_Carteira pr = new PT_Carteira();
			
pr.setRegiao((String)row[0]);
pr.setVendedor((BigDecimal)row[1]);
pr.setNomevendedor((String)row[2]);

pr.setAtivos((BigInteger)row[3]);
pr.setAtivos_mes((BigInteger)row[4]);
//pr.setPerc_evolucao((Double)row[5]);
pr.setPontos_evolucao((Integer)row[5]);
pr.setNovos((BigDecimal)row[6]);
//pr.setPerc_novos((BigDecimal)row[8]);
pr.setPontos_novos((BigDecimal)row[7]);
pr.setReativados((BigDecimal)row[8]);
//pr.setPerc_reativado((BigDecimal)row[11]);
pr.setPontos_reativados((BigDecimal)row[9]);

pr.setAtivos2((BigInteger)row[10]);
pr.setAtivos_mes2((BigInteger)row[11]);
//pr.setPerc_evolucao2((Double)row[15]);
pr.setPontos_evolucao2((Integer)row[12]);
pr.setNovos2((BigDecimal)row[13]);
//pr.setPerc_novos2((BigDecimal)row[18]);
pr.setPontos_novos2((BigDecimal)row[14]);
pr.setReativados2((BigDecimal)row[15]);
//pr.setPerc_reativado2((BigDecimal)row[21]);
pr.setPontos_reativados2((BigDecimal)row[16]);

pr.setAtivos3((BigInteger)row[17]);
pr.setAtivos_mes3((BigInteger)row[18]);
//pr.setPerc_evolucao3((Double)row[25]);
pr.setPontos_evolucao3((Integer)row[19]);
pr.setNovos3((BigDecimal)row[20]);
//pr.setPerc_novos3((BigDecimal)row[28]);
pr.setPontos_novos3((BigDecimal)row[21]);
pr.setReativados3((BigDecimal)row[22]);
//pr.setPerc_reativado3((BigDecimal)row[31]);
pr.setPontos_reativados3((BigDecimal)row[23]);

pr.setTotaltri1((BigDecimal)row[24]);
			
			p.add(pr);
		}
		
		return p;
	}
	
	public List<PT_Meta> pt_meta(String regiao,String vendedor1, String vendedor2){
		List<PT_Meta> p = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
						+ " x.regiao,x.vendedor,x.nomevendedor, "
						+ " x.mes,x.meta,x.vl_mes_pedido,x.atingido,x.pontos, "
						+ " x.mes2,x.meta2,x.valor2,x.atingido2,x.pontos2, "
						+ " x.mes3,x.meta3,x.valor3,x.atingido3,x.pontos3, "
						+ " x.mes4,x.meta4,x.valor4,x.atingido4,x.pontos4, "
						+ " x.mes5,x.meta5,x.valor5,x.atingido5,x.pontos5, "
						+ " x.mes6,x.meta6,x.valor6,x.atingido6,x.pontos6, "
						+ " x.mes7,x.meta7,x.valor7,x.atingido7,x.pontos7, "
						+ " x.mes8,x.meta8,x.valor8,x.atingido8,x.pontos8, "
						+ " x.mes9,x.meta9,x.valor9,x.atingido9,x.pontos9, "
						+ " x.mes10,x.meta10,x.valor10,x.atingido10,x.pontos10, "
						+ " x.mes11,x.meta11,x.valor11,x.atingido11,x.pontos11, "
						+ " x.mes12,x.meta12,x.valor12,x.atingido12,x.pontos12, "
						+ " x.pontos+x.pontos2+x.pontos3 totaltri1, "
						+ " x.pontos4+x.pontos5+x.pontos6 totaltri2, "
						+ " x.pontos7+x.pontos8+x.pontos9 totaltri3, "
						+ " x.pontos10+x.pontos11+x.pontos12 totaltri4, "
						+ " x.pontos+x.pontos2+x.pontos3+x.pontos4+x.pontos5+x.pontos6+x.pontos7+x.pontos8+x.pontos9+x.pontos10+x.pontos11+x.pontos12 totalanual from( "
						+ " select  "
						+ " r.NOME_REGIAO regiao, "
						+ " v.CADCFTVID vendedor, "
						+ " vv.NOME_CADCFTV nomevendedor,			 "
						+ " meta.mes, "
						+ " meta.meta, "
						+ " vendas.vl_mes_pedido, "
						+ " round(((vendas.vl_mes_pedido/meta.meta)*100),2) atingido, "
						+ " case  "
						+ " when round(((vendas.vl_mes_pedido/meta.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas.vl_mes_pedido/meta.meta)*100),2) >= 130 then 200 "
						+ " when round(((vendas.vl_mes_pedido/meta.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas.vl_mes_pedido/meta.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas.vl_mes_pedido/meta.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos,		 "
						+ " meta2.mes mes2, "
						+ " meta2.meta meta2, "
						+ " vendas2.vl_mes_pedido valor2, "
						+ " round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) atingido2, "
						+ " case  "
						+ " when round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) >= 130 then 200 "
						+ " when round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas2.vl_mes_pedido/meta2.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos2,			 "
						+ " meta3.mes mes3, "
						+ " meta3.meta meta3, "
						+ " vendas3.vl_mes_pedido valor3, "
						+ " round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) atingido3, "
						+ " case  "
						+ " when round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) >= 130 then 200 "
						+ " when round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas3.vl_mes_pedido/meta3.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos3, "
						+ " meta4.mes mes4, "
						+ " meta4.meta meta4, "
						+ " vendas4.vl_mes_pedido valor4, "
						+ " round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) atingido4, "
						+ " case  "
						+ " when round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) >= 140 then 200 "
						+ " when round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas4.vl_mes_pedido/meta4.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos4, "
						+ " meta5.mes mes5, "
						+ " meta5.meta meta5, "
						+ " vendas5.vl_mes_pedido valor5, "
						+ " round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) atingido5, "
						+ " case  "
						+ " when round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) >= 150 then 200 "
						+ " when round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas5.vl_mes_pedido/meta5.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos5, "
						+ " meta6.mes mes6, "
						+ " meta6.meta meta6, "
						+ " vendas6.vl_mes_pedido valor6, "
						+ " round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) atingido6, "
						+ " case  "
						+ " when round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) >= 160 then 200 "
						+ " when round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas6.vl_mes_pedido/meta6.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos6, "
						+ " meta7.mes mes7, "
						+ " meta7.meta meta7, "
						+ " vendas7.vl_mes_pedido valor7, "
						+ " round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) atingido7, "
						+ " case  "
						+ " when round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) >= 170 then 200 "
						+ " when round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas7.vl_mes_pedido/meta7.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos7, "
						+ " meta8.mes mes8, "
						+ " meta8.meta meta8, "
						+ " vendas8.vl_mes_pedido valor8, "
						+ " round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) atingido8, "
						+ " case  "
						+ " when round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) >= 180 then 200 "
						+ " when round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas8.vl_mes_pedido/meta8.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos8, "
						+ " meta9.mes mes9, "
						+ " meta9.meta meta9, "
						+ " vendas9.vl_mes_pedido valor9, "
						+ " round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) atingido9, "
						+ " case  "
						+ " when round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) >= 190 then 200 "
						+ " when round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas9.vl_mes_pedido/meta9.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos9, "
						+ " meta10.mes mes10, "
						+ " meta10.meta meta10, "
						+ " vendas10.vl_mes_pedido valor10, "
						+ " round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) atingido10, "
						+ " case  "
						+ " when round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) >= 1100 then 200 "
						+ " when round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas10.vl_mes_pedido/meta10.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos10, "
						+ " meta11.mes mes11, "
						+ " meta11.meta meta11, "
						+ " vendas11.vl_mes_pedido valor11, "
						+ " round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) atingido11, "
						+ " case  "
						+ " when round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) >= 1110 then 120 "
						+ " when round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas11.vl_mes_pedido/meta11.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos11, "
						+ " meta12.mes mes12, "
						+ " meta12.meta meta12, "
						+ " vendas12.vl_mes_pedido valor12, "
						+ " round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) atingido12, "
						+ " case  "
						+ " when round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) >= 150 then 300 "
						+ " when round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) >= 1120 then 200 "
						+ " when round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) >= 100 then 100 "
						+ " when round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) >= 90 then 80 "
						+ " when round(((vendas12.vl_mes_pedido/meta12.meta)*100),2) >= 80 then 60  "
						+ " else 0 end pontos12 "
						+ " from vendedor v "
						+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
						+ " inner join cliente vc on vc.CADCFTVID = vv.CADCFTVID "
						+ " inner join regiao r on r.regiaoid = vc.REGIAOID		 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes, "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta on meta.vendedor = v.CADCFTVID and meta.mes = '01' and meta.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM'),p.VENDEDOR1ID "
						+ " )vendas on vendas.ano = meta.ano and vendas.mes = meta.mes and vendas.vendedor = meta.vendedor	 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes, "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta2 on meta2.vendedor = v.CADCFTVID and meta2.mes = '02' and meta2.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM'),p.VENDEDOR1ID "
						+ " )vendas2 on vendas2.ano = meta2.ano and vendas2.mes = meta2.mes and vendas2.vendedor = meta2.vendedor		 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta3 on meta3.vendedor = v.CADCFTVID and meta3.mes = '03' and meta3.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM'),p.VENDEDOR1ID "
						+ " )vendas3 on vendas3.ano = meta3.ano and vendas3.mes = meta3.mes and vendas3.vendedor = meta3.vendedor "
						+ "  left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes, "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta4 on meta4.vendedor = v.CADCFTVID and meta4.mes = '04' and meta4.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '04' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-03-2023' and '25-04-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas4 on vendas4.ano = meta4.ano and vendas4.mes = meta4.mes and vendas4.vendedor = meta4.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta5 on meta5.vendedor = v.CADCFTVID and meta5.mes = '05' and meta5.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '05' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-04-2023' and '25-05-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas5 on vendas5.ano = meta5.ano and vendas5.mes = meta5.mes and vendas5.vendedor = meta5.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta6 on meta6.vendedor = v.CADCFTVID and meta6.mes = '06' and meta6.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '06' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-05-2023' and '25-06-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas6 on vendas6.ano = meta6.ano and vendas6.mes = meta6.mes and vendas6.vendedor = meta6.vendedor				 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta7 on meta7.vendedor = v.CADCFTVID and meta7.mes = '07' and meta7.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '07' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-06-2023' and '25-07-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas7 on vendas7.ano = meta7.ano and vendas7.mes = meta7.mes and vendas7.vendedor = meta7.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta8 on meta8.vendedor = v.CADCFTVID and meta8.mes = '08' and meta8.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '08' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-07-2023' and '25-08-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas8 on vendas8.ano = meta8.ano and vendas8.mes = meta8.mes and vendas8.vendedor = meta8.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta9 on meta9.vendedor = v.CADCFTVID and meta9.mes = '09' and meta9.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '09' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-08-2023' and '25-09-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas9 on vendas9.ano = meta9.ano and vendas9.mes = meta9.mes and vendas9.vendedor = meta9.vendedor		 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta10 on meta10.vendedor = v.CADCFTVID and meta10.mes = '10' and meta10.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '10' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-09-2023' and '25-10-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas10 on vendas10.ano = meta10.ano and vendas10.mes = meta10.mes and vendas10.vendedor = meta10.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta11 on meta11.vendedor = v.CADCFTVID and meta11.mes = '11' and meta11.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '11' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-10-2023' and '25-11-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas11 on vendas11.ano = meta11.ano and vendas11.mes = meta11.mes and vendas11.vendedor = meta11.vendedor			 "
						+ " left join( "
						+ " select  "
						+ "  cast(m.ANO_METAVENDEDOR as text) ano,   "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes,  "
						+ " m.VALOR_METAVENDEDOR meta, "
						+ " m.CADCFTVID vendedor "
						+ " from META_VENDEDOR m  "
						+ " )meta12 on meta12.vendedor = v.CADCFTVID and meta12.mes = '12' and meta12.ano = '2023' "
						+ " left join( "
						+ " select  "
						+ " '2023' ano, "
						+ " '12' mes, "
						+ " round(sum(it.VL_UNIT_PEDIDOVENDA_ITEM * it.QT_INI_PEDIDOVENDA_ITEM),2) vl_mes_pedido, "
						+ " p.VENDEDOR1ID vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA'  "
						+ " and p.DT_PEDIDOVENDA between  '26-11-2023' and '30-12-2023' "
						+ " group by p.VENDEDOR1ID "
						+ " )vendas12 on vendas12.ano = meta12.ano and vendas12.mes = meta12.mes and vendas12.vendedor = meta12.vendedor "
				+ "  "
				+ " where (meta.meta > 0 or meta2.meta > 0) "
				+ " and v.cadcftvid =  "+ usuarioconectado()
				+ " )x "
				);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PT_Meta pr = new PT_Meta();
			
			pr.setRegiao((String)row[0]);
			pr.setVendedor((BigDecimal)row[1]);
			pr.setNomevendedor((String)row[2]);
			
			pr.setMes((String)row[3]);
			pr.setMeta((BigDecimal)row[4]);
			pr.setValor((BigDecimal)row[5]);
			pr.setAtingido((BigDecimal)row[6]);
			pr.setPontos((Integer)row[7]);
			
			pr.setMes2((String)row[8]);
			pr.setMeta2((BigDecimal)row[9]);
			pr.setValor2((BigDecimal)row[10]);
			pr.setAtingido2((BigDecimal)row[11]);
			pr.setPontos2((Integer)row[12]);
			
			pr.setMes3((String)row[13]);
			pr.setMeta3((BigDecimal)row[14]);
			pr.setValor3((BigDecimal)row[15]);
			pr.setAtingido3((BigDecimal)row[16]);
			pr.setPontos3((Integer)row[17]);
			
			pr.setMes4((String)row[18]);
			pr.setMeta4((BigDecimal)row[19]);
			pr.setValor4((BigDecimal)row[20]);
			pr.setAtingido4((BigDecimal)row[21]);
			pr.setPontos4((Integer)row[22]);
			
			pr.setMes5((String)row[23]);
			pr.setMeta5((BigDecimal)row[24]);
			pr.setValor5((BigDecimal)row[25]);
			pr.setAtingido5((BigDecimal)row[26]);
			pr.setPontos5((Integer)row[27]);
			
			pr.setMes6((String)row[28]);
			pr.setMeta6((BigDecimal)row[29]);
			pr.setValor6((BigDecimal)row[30]);
			pr.setAtingido6((BigDecimal)row[31]);
			pr.setPontos6((Integer)row[32]);
			
			pr.setMes7((String)row[33]);
			pr.setMeta7((BigDecimal)row[34]);
			pr.setValor7((BigDecimal)row[35]);
			pr.setAtingido7((BigDecimal)row[36]);
			pr.setPontos7((Integer)row[37]);
			
			pr.setMes8((String)row[38]);
			pr.setMeta8((BigDecimal)row[39]);
			pr.setValor8((BigDecimal)row[40]);
			pr.setAtingido8((BigDecimal)row[41]);
			pr.setPontos8((Integer)row[42]);
			
			pr.setMes9((String)row[43]);
			pr.setMeta9((BigDecimal)row[44]);
			pr.setValor9((BigDecimal)row[45]);
			pr.setAtingido9((BigDecimal)row[46]);
			pr.setPontos9((Integer)row[47]);
			
			pr.setMes10((String)row[48]);
			pr.setMeta10((BigDecimal)row[49]);
			pr.setValor10((BigDecimal)row[50]);
			pr.setAtingido10((BigDecimal)row[51]);
			pr.setPontos10((Integer)row[52]);
			
			pr.setMes11((String)row[53]);
			pr.setMeta11((BigDecimal)row[54]);
			pr.setValor11((BigDecimal)row[55]);
			pr.setAtingido11((BigDecimal)row[56]);
			pr.setPontos11((Integer)row[57]);
			
			pr.setMes12((String)row[58]);
			pr.setMeta12((BigDecimal)row[59]);
			pr.setValor12((BigDecimal)row[60]);
			pr.setAtingido12((BigDecimal)row[61]);
			pr.setPontos12((Integer)row[62]);
			
			pr.setTotaltri1((Integer)row[63]);
			pr.setTotaltri2((Integer)row[64]);
			pr.setTotaltri3((Integer)row[65]);
			pr.setTotaltri4((Integer)row[66]);
			pr.setTotalanual((Integer)row[67]);
							

			p.add(pr);
		}
		
		return p;
	}
	
	public List<PT_Mix> pt_mix(String regiao,String vendedor1, String vendedor2){
		List<PT_Mix> p = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
				+ " x.regiao,x.vendedor,x.nomevendedor,x.cliente,x.nomecliente, "
				+ " x.mes,x.qtmixmedio,x.mixqtde,x.pontos, "
				+ " x.mes2,x.qtmixmedio2,x.mixqtde2,x.pontos2, "
				+ " x.mes3,x.qtmixmedio3,x.mixqtde3,x.pontos3, "
				+ " x.mes4,x.qtmixmedio4,x.mixqtde4,x.pontos4, "
				+ " x.mes5,x.qtmixmedio5,x.mixqtde5,x.pontos5, "
				+ " x.mes6,x.qtmixmedio6,x.mixqtde6,x.pontos6, "
				+ " x.mes7,x.qtmixmedio7,x.mixqtde7,x.pontos7, "
				+ " x.mes8,x.qtmixmedio8,x.mixqtde8,x.pontos8, "
				+ " x.mes9,x.qtmixmedio9,x.mixqtde9,x.pontos9, "
				+ " x.mes10,x.qtmixmedio10,x.mixqtde10,x.pontos10, "
				+ " x.mes11,x.qtmixmedio11,x.mixqtde11,x.pontos11, "
				+ " x.mes12,x.qtmixmedio12,x.mixqtde12,x.pontos12, "
				+ " x.pontos+x.pontos2+x.pontos3 totaltri1, "
				+ " x.pontos4+x.pontos5+x.pontos6 totaltri2, "
				+ " x.pontos7+x.pontos8+x.pontos9 totaltri3, "
				+ " x.pontos10+x.pontos11+x.pontos12 totaltri4, "
				+ " x.pontos+x.pontos2+x.pontos3+x.pontos4+x.pontos5+x.pontos6+x.pontos7+x.pontos8+x.pontos9+x.pontos10+x.pontos11+x.pontos12 totalanual from( "
				+ " select r.NOME_REGIAO regiao,v.CADCFTVID vendedor,vv.NOME_CADCFTV nomevendedor,c.CADCFTVID cliente,cc.NOME_CADCFTV nomecliente, "
				+ " '01' mes, "
				+ " mixmedio01.qtmixmedio, "
				+ " mix01.mixqtde, "
				+ " case when mix01.mixqtde > mixmedio01.qtmixmedio and mixmedio01.qtmixmedio > 0 and (((mix01.mixqtde - mixmedio01.qtmixmedio)/mixmedio01.qtmixmedio)*100)>=10 then 2 else 0 end pontos,  "
				+ " '02' mes2,  "
				+ " mixmedio02.qtmixmedio2,  "
				+ " mix02.mixqtde2,  "
				+ " case when mix02.mixqtde2 > mixmedio02.qtmixmedio2 and mixmedio02.qtmixmedio2 > 0 and (((mix02.mixqtde2 - mixmedio02.qtmixmedio2)/mixmedio02.qtmixmedio2)*100)>=10 then 2 else 0 end pontos2,  "
				+ " '03' mes3,  "
				+ " mixmedio03.qtmixmedio3,  "
				+ " mix03.mixqtde3,  "
				+ " case when mix03.mixqtde3 > mixmedio03.qtmixmedio3 and mixmedio03.qtmixmedio3 > 0 and (((mix03.mixqtde3 - mixmedio03.qtmixmedio3)/mixmedio03.qtmixmedio3)*100)>=10 then 2 else 0 end pontos3,  "
				+ " '04' mes4,  "
				+ " mixmedio04.qtmixmedio4,  "
				+ " mix04.mixqtde4,  "
				+ " case when mix04.mixqtde4 > mixmedio04.qtmixmedio4 and mixmedio04.qtmixmedio4 > 0 and (((mix04.mixqtde4 - mixmedio04.qtmixmedio4)/mixmedio04.qtmixmedio4)*100)>=10 then 2 else 0 end pontos4,  "
				+ " '05' mes5,  "
				+ " mixmedio05.qtmixmedio5,  "
				+ " mix05.mixqtde5,  "
				+ " case when mix05.mixqtde5 > mixmedio05.qtmixmedio5 and mixmedio05.qtmixmedio5 > 0 and (((mix05.mixqtde5 - mixmedio05.qtmixmedio5)/mixmedio05.qtmixmedio5)*100)>=10 then 2 else 0 end pontos5,  "
				+ " '06' mes6,  "
				+ " mixmedio06.qtmixmedio6,  "
				+ " mix06.mixqtde6,  "
				+ " case when mix06.mixqtde6 > mixmedio06.qtmixmedio6 and mixmedio06.qtmixmedio6 > 0 and (((mix06.mixqtde6 - mixmedio06.qtmixmedio6)/mixmedio06.qtmixmedio6)*100)>=10 then 2 else 0 end pontos6,  "
				+ " '07' mes7,  "
				+ " mixmedio07.qtmixmedio7,  "
				+ " mix07.mixqtde7,  "
				+ " case when mix07.mixqtde7 > mixmedio07.qtmixmedio7 and mixmedio07.qtmixmedio7 > 0 and (((mix07.mixqtde7 - mixmedio07.qtmixmedio7)/mixmedio07.qtmixmedio7)*100)>=10 then 2 else 0 end pontos7,  "
				+ " '08' mes8,  "
				+ " mixmedio08.qtmixmedio8,  "
				+ " mix08.mixqtde8,  "
				+ " case when mix08.mixqtde8 > mixmedio08.qtmixmedio8 and mixmedio08.qtmixmedio8 > 0 and (((mix08.mixqtde8 - mixmedio08.qtmixmedio8)/mixmedio08.qtmixmedio8)*100)>=10 then 2 else 0 end pontos8,  "
				+ " '09' mes9,  "
				+ " mixmedio09.qtmixmedio9,  "
				+ " mix09.mixqtde9,  "
				+ " case when mix09.mixqtde9 > mixmedio09.qtmixmedio9 and mixmedio09.qtmixmedio9 > 0 and (((mix09.mixqtde9 - mixmedio09.qtmixmedio9)/mixmedio09.qtmixmedio9)*100)>=10 then 2 else 0 end pontos9,  "
				+ " '10' mes10,  "
				+ " mixmedio10.qtmixmedio10,  "
				+ " mix10.mixqtde10,  "
				+ " case when mix10.mixqtde10 > mixmedio10.qtmixmedio10 and mixmedio10.qtmixmedio10 > 0 and (((mix10.mixqtde10 - mixmedio10.qtmixmedio10)/mixmedio10.qtmixmedio10)*100)>=10 then 2 else 0 end pontos10,  "
				+ " '11' mes11,  "
				+ " mixmedio11.qtmixmedio11,  "
				+ " mix11.mixqtde11,  "
				+ " case when mix11.mixqtde11 > mixmedio11.qtmixmedio11 and mixmedio11.qtmixmedio11 > 0 and (((mix11.mixqtde11 - mixmedio11.qtmixmedio11)/mixmedio11.qtmixmedio11)*100)>=10 then 2 else 0 end pontos11,  "
				+ " '12' mes12,  "
				+ " mixmedio12.qtmixmedio12,  "
				+ " mix12.mixqtde12,  "
				+ " case when mix12.mixqtde12 > mixmedio12.qtmixmedio12 and mixmedio12.qtmixmedio12 > 0 and (((mix12.mixqtde12 - mixmedio12.qtmixmedio12)/mixmedio12.qtmixmedio12)*100)>=10 then 2 else 0 end pontos12  "
				+ "  "
				+ " from vendedor v "
				+ " inner join cliente c on c.VENDEDORID1 = v.CADCFTVID "
				+ " inner join CADCFTV cc on cc.CADCFTVID = c.CADCFTVID "
				+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
				+ " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID "
				+ " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-12-2022' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio01 on mixmedio01.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2023' and '31-01-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix01 on mix01.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas2,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio2  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-01-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio02 on mixmedio02.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde2  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-02-2023' and '28-02-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix02 on mix02.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas3,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio3  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '28-02-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio03 on mixmedio03.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde3  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-03-2023' and '31-03-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix03 on mix03.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas4,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio4  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-03-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio04 on mixmedio04.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde4  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-04-2023' and '30-04-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix04 on mix04.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas5,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio5  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '30-04-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio05 on mixmedio05.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde5  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-05-2023' and '31-05-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix05 on mix05.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas6,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio6  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-05-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio06 on mixmedio06.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde6  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-06-2023' and '30-06-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix06 on mix06.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas7,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio7  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '30-06-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio07 on mixmedio07.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde7  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-07-2023' and '31-07-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix07 on mix07.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas8,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio8  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-07-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio08 on mixmedio08.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde8  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-08-2023' and '31-08-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix08 on mix08.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas9,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio9  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-08-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio09 on mixmedio09.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde9  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-09-2023' and '30-09-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix09 on mix09.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas10,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio10  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '30-09-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio10 on mixmedio10.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde10  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-10-2023' and '31-10-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix10 on mix10.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas11,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio11  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '31-10-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio11 on mixmedio11.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde11  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-11-2023' and '30-11-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix11 on mix11.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " y.CADCFTVID,  "
				+ " count(y.CADCFTVID) qtvendas12,  "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio12  "
				+ " from (select  "
				+ " x.CADCFTVID,  "
				+ " x.pedidovendaid,  "
				+ " count(x.produtoid) qt  "
				+ " from(SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid,  "
				+ " p.pedidovendaid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-01-2000' and '30-11-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID,x.pedidovendaid)y  "
				+ " group by y.CADCFTVID  "
				+ " ) mixmedio12 on mixmedio12.CADCFTVID = c.CADCFTVID  "
				+ "  "
				+ " left join (  "
				+ " select  "
				+ " x.CADCFTVID,  "
				+ " count(x.produtoid) mixqtde12  "
				+ " from  "
				+ " (SELECT DISTINCT  "
				+ " p.CADCFTVID,  "
				+ " it.produtoid  "
				+ " from pedidovenda_item it  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " and p.DT_PEDIDOVENDA between '01-12-2023' and '31-12-2023' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x  "
				+ " group by x.CADCFTVID  "
				+ " )mix12 on mix12.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " where v.cadcftvid =  "+ usuarioconectado()
				+ " order by v.CADCFTVID,cc.NOME_CADCFTV)x "
				);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PT_Mix pr = new PT_Mix();
			
			pr.setRegiao((String)row[0]);
			pr.setVendedor((BigDecimal)row[1]);
			pr.setNomevendedor((String)row[2]);
			pr.setCliente((BigDecimal)row[3]);
			pr.setNomecliente((String)row[4]);
			
			pr.setMes((String)row[5]);
			pr.setMixmedio((BigDecimal)row[6]);
			pr.setMixmes((BigInteger)row[7]);
			pr.setPontos((Integer)row[8]);
			
			pr.setMes2((String)row[9]);
			pr.setMixmedio2((BigDecimal)row[10]);
			pr.setMixmes2((BigInteger)row[11]);
			pr.setPontos2((Integer)row[12]);
			
			pr.setMes3((String)row[13]);
			pr.setMixmedio3((BigDecimal)row[14]);
			pr.setMixmes3((BigInteger)row[15]);
			pr.setPontos3((Integer)row[16]);
			
			pr.setMes4((String)row[17]);
			pr.setMixmedio4((BigDecimal)row[18]);
			pr.setMixmes4((BigInteger)row[19]);
			pr.setPontos4((Integer)row[20]);
			
			pr.setMes5((String)row[21]);
			pr.setMixmedio5((BigDecimal)row[22]);
			pr.setMixmes5((BigInteger)row[23]);
			pr.setPontos5((Integer)row[24]);
			
			pr.setMes6((String)row[25]);
			pr.setMixmedio6((BigDecimal)row[26]);
			pr.setMixmes6((BigInteger)row[27]);
			pr.setPontos6((Integer)row[28]);
			
			pr.setMes7((String)row[29]);
			pr.setMixmedio7((BigDecimal)row[30]);
			pr.setMixmes7((BigInteger)row[31]);
			pr.setPontos7((Integer)row[32]);
			
			pr.setMes8((String)row[33]);
			pr.setMixmedio8((BigDecimal)row[34]);
			pr.setMixmes8((BigInteger)row[35]);
			pr.setPontos8((Integer)row[36]);
			
			pr.setMes9((String)row[37]);
			pr.setMixmedio9((BigDecimal)row[38]);
			pr.setMixmes9((BigInteger)row[39]);
			pr.setPontos9((Integer)row[40]);
			
			pr.setMes10((String)row[41]);
			pr.setMixmedio10((BigDecimal)row[42]);
			pr.setMixmes10((BigInteger)row[43]);
			pr.setPontos10((Integer)row[44]);
			
			pr.setMes11((String)row[45]);
			pr.setMixmedio11((BigDecimal)row[46]);
			pr.setMixmes11((BigInteger)row[47]);
			pr.setPontos11((Integer)row[48]);
			
			pr.setMes12((String)row[49]);
			pr.setMixmedio12((BigDecimal)row[50]);
			pr.setMixmes12((BigInteger)row[51]);
			pr.setPontos12((Integer)row[52]);
			
			pr.setTotaltri1((Integer)row[53]);
			pr.setTotaltri2((Integer)row[54]);
			pr.setTotaltri3((Integer)row[55]);
			pr.setTotaltri4((Integer)row[56]);
			pr.setTotalanual((Integer)row[57]);
							

			p.add(pr);
		}
		return p;
	}	
	
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2){
		List<HCliente> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT "
				+ " C.CADCFTVID, "
				+ " C.NOME_CADCFTV, "
				+ " en.END_ENDCADCFTV ENDERECO, "
				+ " en.NRO_ENDCADCFTV NUMERO, "
				+ " en.CEP_ENDCADCFTV CEP, "
				+ " en.UF_CIDADE UF , "
				+ " en.NOME_CIDADE, "
				+ " V.CADCFTVID VENDEDOR, "
				+ " V.NOME_CADCFTV NOME_VENDEDOR, "
				+ " G.NOME_GESTOR, "
				+ " "
				+ " VENDAS2018.JANEIRO2018, "
				+ " VENDAS2018.FEVEREIRO2018, "
				+ " VENDAS2018.MARCO2018, "
				+ " VENDAS2018.ABRIL2018, "
				+ " VENDAS2018.MAIO2018, "
				+ " VENDAS2018.JUNHO2018, "
				+ " VENDAS2018.JULHO2018, "
				+ " VENDAS2018.AGOSTO2018, "
				+ " VENDAS2018.SETEMBRO2018, "
				+ " VENDAS2018.OUTUBRO2018, "
				+ " VENDAS2018.NOVEMBRO2018, "
				+ " VENDAS2018.DEZEMBRO2018, "
				+ " VENDAS2018.TOTALANO2018, "
				+ "  "
				+ " VENDAS2019.JANEIRO2019, "
				+ " VENDAS2019.FEVEREIRO2019, "
				+ " VENDAS2019.MARCO2019, "
				+ " VENDAS2019.ABRIL2019, "
				+ " VENDAS2019.MAIO2019, "
				+ " VENDAS2019.JUNHO2019, "
				+ " VENDAS2019.JULHO2019, "
				+ " VENDAS2019.AGOSTO2019, "
				+ " VENDAS2019.SETEMBRO2019, "
				+ " VENDAS2019.OUTUBRO2019, "
				+ " VENDAS2019.NOVEMBRO2019, "
				+ " VENDAS2019.DEZEMBRO2019, "
				+ " VENDAS2019.TOTALANO2019, "
				+ "  "
				+ " VENDAS2020.JANEIRO2020, "
				+ " VENDAS2020.FEVEREIRO2020, "
				+ " VENDAS2020.MARCO2020, "
				+ " VENDAS2020.ABRIL2020, "
				+ " VENDAS2020.MAIO2020, "
				+ " VENDAS2020.JUNHO2020, "
				+ " VENDAS2020.JULHO2020, "
				+ " VENDAS2020.AGOSTO2020, "
				+ " VENDAS2020.SETEMBRO2020, "
				+ " VENDAS2020.OUTUBRO2020, "
				+ " VENDAS2020.NOVEMBRO2020, "
				+ " VENDAS2020.DEZEMBRO2020, "
				+ " VENDAS2020.TOTALANO2020, "
				+ "  "
				+ " VENDAS2021.JANEIRO2021, "
				+ " VENDAS2021.FEVEREIRO2021, "
				+ " VENDAS2021.MARCO2021, "
				+ " VENDAS2021.ABRIL2021, "
				+ " VENDAS2021.MAIO2021, "
				+ " VENDAS2021.JUNHO2021, "
				+ " VENDAS2021.JULHO2021, "
				+ " VENDAS2021.AGOSTO2021, "
				+ " VENDAS2021.SETEMBRO2021, "
				+ " VENDAS2021.OUTUBRO2021, "
				+ " VENDAS2021.NOVEMBRO2021, "
				+ " VENDAS2021.DEZEMBRO2021, "
				+ " VENDAS2021.TOTALANO2021,"
				+ " nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0) + nvl(VENDAS2022.TOTALANO2022,0)+ nvl(VENDAS2023.TOTALANO2023,0)+ nvl(VENDAS2024.TOTALANO2024,0) totalgeral, "
				+ " mix.mixqtde, "
				+ " mixmedio.qtmixmedio, "
				+ " round((nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0)+ nvl(VENDAS2022.TOTALANO2022,0)+ nvl(VENDAS2023.TOTALANO2023,0)+ nvl(VENDAS2024.TOTALANO2024,0))/mixmedio.qtvendas,2) ticketmedio,  "
				+ " freq.vendas, "
				+ " freq.primeira, "
				+ " freq.ultima, "
				+ " freq.frequencia, "
				+ " C.CNPJCPF_CADCFTV cnpjcpf, "
				+ " en.BAIRRO_ENDCADCFTV bairro, "
				+ "  case  when round(extract(days from CURRENT_DATE - freq.ultima)) <= 90 then 'ATIVO' "
				+ " when round(extract(days from CURRENT_DATE - freq.ultima)) > 90 and round(extract(days from CURRENT_DATE - freq.ultima)) <= 180 and (round(extract(days from CURRENT_DATE - freq.ultima))+extract(days from(date_trunc('month',current_date) + interval '1 month' - interval '1 day')- CURRENT_DATE)) >= 180 then 'CRITICO' "
				+ " when round(extract(days from CURRENT_DATE - freq.ultima)) > 90 and round(extract(days from CURRENT_DATE - freq.ultima)) <= 180 then 'SEMI-ATIVO' "
				+ " when round(extract(days from CURRENT_DATE - freq.ultima)) > 180 then 'INATIVO' "
				+ " ELSE 'INATIVO' END AS STATUS, "
				
				+ " VENDAS2022.JANEIRO2022, "
				+ " VENDAS2022.FEVEREIRO2022, "
				+ " VENDAS2022.MARCO2022, "
				+ " VENDAS2022.ABRIL2022, "
				+ " VENDAS2022.MAIO2022, "
				+ " VENDAS2022.JUNHO2022, "
				+ " VENDAS2022.JULHO2022, "
				+ " VENDAS2022.AGOSTO2022, "
				+ " VENDAS2022.SETEMBRO2022, "
				+ " VENDAS2022.OUTUBRO2022, "
				+ " VENDAS2022.NOVEMBRO2022, "
				+ " VENDAS2022.DEZEMBRO2022, "
				+ " VENDAS2022.TOTALANO2022, "
				
				+ " VENDAS2023.JANEIRO2023, "
				+ " VENDAS2023.FEVEREIRO2023, "
				+ " VENDAS2023.MARCO2023, "
				+ " VENDAS2023.ABRIL2023, "
				+ " VENDAS2023.MAIO2023, "
				+ " VENDAS2023.JUNHO2023, "
				+ " VENDAS2023.JULHO2023, "
				+ " VENDAS2023.AGOSTO2023, "
				+ " VENDAS2023.SETEMBRO2023, "
				+ " VENDAS2023.OUTUBRO2023, "
				+ " VENDAS2023.NOVEMBRO2023, "
				+ " VENDAS2023.DEZEMBRO2023, "
				+ " VENDAS2023.TOTALANO2023, "
				
				+ " VENDAS2024.JANEIRO2024, "
				+ " VENDAS2024.FEVEREIRO2024, "
				+ " VENDAS2024.MARCO2024, "
				+ " VENDAS2024.ABRIL2024, "
				+ " VENDAS2024.MAIO2024, "
				+ " VENDAS2024.JUNHO2024, "
				+ " VENDAS2024.JULHO2024, "
				+ " VENDAS2024.AGOSTO2024, "
				+ " VENDAS2024.SETEMBRO2024, "
				+ " VENDAS2024.OUTUBRO2024, "
				+ " VENDAS2024.NOVEMBRO2024, "
				+ " VENDAS2024.DEZEMBRO2024, "
				+ " VENDAS2024.TOTALANO2024 "
				
				+ " FROM CADCFTV C "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = C.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
				+ " LEFT join (SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV, V.BAIRRO_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV  "
				+ " group by cadcftvid )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID ) EN ON EN.CADCFTVID = c.CADCFTVID "
				+ "  "
				+ " LEFT JOIN( "
				+ " select  "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "
				+ "  "
				+ " sum(janeiro) JANEIRO2018, "
				+ " sum(fevereiro) FEVEREIRO2018, "
				+ " sum(marco) MARCO2018, "
				+ " sum(abril) ABRIL2018 , "
				+ " sum(maio) MAIO2018, "
				+ " sum(junho) JUNHO2018, "
				+ " sum(julho) JULHO2018 , "
				+ " sum(agosto) AGOSTO2018, "
				+ " sum(setembro) SETEMBRO2018, "
				+ " sum(outubro) OUTUBRO2018, "
				+ " sum(novembro) NOVEMBRO2018, "
				+ " sum(dezembro) DEZEMBRO2018, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2018 "
				+ "  "
				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
				+ "  "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2018') "
				+ "  "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2018 ON VENDAS2018.CLIENTE = C.CADCFTVID "
				+ "  "
				+ " LEFT JOIN( "
				+ " select  "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "
				+ "  "
				+ " sum(janeiro) JANEIRO2019, "
				+ " sum(fevereiro) FEVEREIRO2019, "
				+ " sum(marco) MARCO2019, "
				+ " sum(abril) ABRIL2019 , "
				+ " sum(maio) MAIO2019, "
				+ " sum(junho) JUNHO2019, "
				+ " sum(julho) JULHO2019 , "
				+ " sum(agosto) AGOSTO2019, "
				+ " sum(setembro) SETEMBRO2019, "
				+ " sum(outubro) OUTUBRO2019, "
				+ " sum(novembro) NOVEMBRO2019, "
				+ " sum(dezembro) DEZEMBRO2019, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2019 "
				+ "  "
				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
				+ "  "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2019') "
				+ "  "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2019 ON VENDAS2019.CLIENTE = C.CADCFTVID "
				+ "  "
				+ " LEFT JOIN( "
				+ " select  "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "
				+ "  "
				+ " sum(janeiro) JANEIRO2020, "
				+ " sum(fevereiro) FEVEREIRO2020, "
				+ " sum(marco) MARCO2020, "
				+ " sum(abril) ABRIL2020 , "
				+ " sum(maio) MAIO2020, "
				+ " sum(junho) JUNHO2020, "
				+ " sum(julho) JULHO2020 , "
				+ " sum(agosto) AGOSTO2020, "
				+ " sum(setembro) SETEMBRO2020, "
				+ " sum(outubro) OUTUBRO2020, "
				+ " sum(novembro) NOVEMBRO2020, "
				+ " sum(dezembro) DEZEMBRO2020, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2020 "
				+ "  "
				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
				+ "  "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2020') "
				+ "  "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2020 ON VENDAS2020.CLIENTE = C.CADCFTVID "
				+ "  "
				+ " LEFT JOIN( "
				+ " select  "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "
				+ "  "
				+ " sum(janeiro) JANEIRO2021, "
				+ " sum(fevereiro) FEVEREIRO2021, "
				+ " sum(marco) MARCO2021, "
				+ " sum(abril) ABRIL2021 , "
				+ " sum(maio) MAIO2021, "
				+ " sum(junho) JUNHO2021, "
				+ " sum(julho) JULHO2021 , "
				+ " sum(agosto) AGOSTO2021, "
				+ " sum(setembro) SETEMBRO2021, "
				+ " sum(outubro) OUTUBRO2021, "
				+ " sum(novembro) NOVEMBRO2021, "
				+ " sum(dezembro) DEZEMBRO2021, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2021 "
				+ "  "
				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
				+ "  "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ " "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2021') "
				+ " "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2021 ON VENDAS2021.CLIENTE = C.CADCFTVID "
				
				+ " LEFT JOIN( "
				+ " select "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "

				+ " sum(janeiro) JANEIRO2022, "
				+ " sum(fevereiro) FEVEREIRO2022, "
				+ " sum(marco) MARCO2022, "
				+ " sum(abril) ABRIL2022 , " 
				+ " sum(maio) MAIO2022, "
				+ " sum(junho) JUNHO2022, "
				+ " sum(julho) JULHO2022 , "
				+ " sum(agosto) AGOSTO2022, "
				+ " sum(setembro) SETEMBRO2022, "
				+ " sum(outubro) OUTUBRO2022, "
				+ " sum(novembro) NOVEMBRO2022, "
				+ " sum(dezembro) DEZEMBRO2022, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2022 "

				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "

				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "

				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2022') "

				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2022 ON VENDAS2022.CLIENTE = C.CADCFTVID "
				
				+ " LEFT JOIN( " + " select " + " CLIENTE, " + " NOME_CLIENTE, " + " "
				+ " sum(janeiro) JANEIRO2023, " + " sum(fevereiro) FEVEREIRO2023, " + " sum(marco) MARCO2023, "
				+ " sum(abril) ABRIL2023 ,  " + " sum(maio) MAIO2023, " + " sum(junho) JUNHO2023, "
				+ " sum(julho) JULHO2023 , " + " sum(agosto) AGOSTO2023, " + " sum(setembro) SETEMBRO2023, "
				+ " sum(outubro) OUTUBRO2023, " + " sum(novembro) NOVEMBRO2023, "
				+ " sum(dezembro) DEZEMBRO2023, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2023 "
				+ " " + " from( " + " select " + " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, " + " "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ " " + " from pedidovenda p " + " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' " + " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2023') "
				+ " " + " )x " + " group by " + " CLIENTE, " + " NOME_CLIENTE "
				+ " ) VENDAS2023 ON VENDAS2023.CLIENTE = C.CADCFTVID	 "
				
				+ " LEFT JOIN( " + " select " + " CLIENTE, " + " NOME_CLIENTE, " + " "
				+ " sum(janeiro) JANEIRO2024, " + " sum(fevereiro) FEVEREIRO2024, " + " sum(marco) MARCO2024, "
				+ " sum(abril) ABRIL2024 ,  " + " sum(maio) MAIO2024, " + " sum(junho) JUNHO2024, "
				+ " sum(julho) JULHO2024 , " + " sum(agosto) AGOSTO2024, " + " sum(setembro) SETEMBRO2024, "
				+ " sum(outubro) OUTUBRO2024, " + " sum(novembro) NOVEMBRO2024, "
				+ " sum(dezembro) DEZEMBRO2024, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2024 "
				+ " " + " from( " + " select " + " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, " + " "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ " " + " from pedidovenda p " + " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' " + " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2024') "
				+ " " + " )x " + " group by " + " CLIENTE, " + " NOME_CLIENTE "
				+ " ) VENDAS2024 ON VENDAS2024.CLIENTE = C.CADCFTVID	 "				
				
				+ " left join ( "
				+ " select "
				+ " x.CADCFTVID, "
				+ " count(x.produtoid) mixqtde "
				+ " from "
				+ " (SELECT DISTINCT "
				+ " p.CADCFTVID, "
				+ " it.produtoid "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x "
				+ " group by x.CADCFTVID "
				+ " )mix on mix.CADCFTVID = c.CADCFTVID "
				+ ""
				
				+ " left join( "
				+ " select "
				+ " y.CADCFTVID, "
				+ " count(y.CADCFTVID) qtvendas, "
				+ " round(sum(y.qt)/count(y.CADCFTVID)) qtmixmedio "
				+ " from (select "
				+ " x.CADCFTVID, "
				+ " x.pedidovendaid, "
				+ " count(x.produtoid) qt "
				+ " from(SELECT DISTINCT "
				+ " p.CADCFTVID, "
				+ " it.produtoid, "
				+ " p.pedidovendaid "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x "
				+ " group by x.CADCFTVID,x.pedidovendaid)y "
				+ " group by y.CADCFTVID "
				+ " ) mixmedio on mixmedio.CADCFTVID = c.CADCFTVID "
				
				+ " left join( "
				+ " select "
				+ " p.CADCFTVID, "
				+ " count(p.CADCFTVID) vendas, "
				+ " min(p.DT_PEDIDOVENDA) primeira, "
				+ " max(p.DT_PEDIDOVENDA) ultima, "
				+ " round(extract(days from max(P.DT_PEDIDOVENDA) - min(P.DT_PEDIDOVENDA) )/count(p.CADCFTVID)) frequencia "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID "
				+ " )freq on freq.CADCFTVID = c.CADCFTVID "
				
				//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
				
				+ " WHERE C.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
				+ " AND C.ATIVO_CADCFTV = 'SIM' "
				
				+ " and C.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and v.cadcftvid =  "+ usuarioconectado()
				//+"  AND V2.GESTORID = G.GESTORID   "
				);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			HCliente hCliente = new HCliente();
			hCliente.setCodigocliente((BigDecimal) row[0]);
			hCliente.setNomecliente((String) row[1]);
			hCliente.setEndereco((String) row[2]);
			hCliente.setNumeroendereco((String) row[3]);
			hCliente.setCep((String) row[4]);
			hCliente.setUf((String) row[5]);
			hCliente.setCidade((String) row[6]);
			hCliente.setCodigovendedor((BigDecimal) row[7]);
			hCliente.setNomevendedor((String) row[8]);
			hCliente.setGestor((String) row[9]);
			
			hCliente.setJaneiro2018((BigDecimal) row[10]);
			hCliente.setFevereiro2018((BigDecimal) row[11]);
			hCliente.setMarco2018((BigDecimal) row[12]);
			hCliente.setAbril2018((BigDecimal) row[13]);
			hCliente.setMaio2018((BigDecimal) row[14]);
			hCliente.setJunho2018((BigDecimal) row[15]);
			hCliente.setJulho2018((BigDecimal) row[16]);
			hCliente.setAgosto2018((BigDecimal) row[17]);
			hCliente.setSetembro2018((BigDecimal) row[18]);
			hCliente.setOutrubo2018((BigDecimal) row[19]);
			hCliente.setNovembro2018((BigDecimal) row[20]);
			hCliente.setDezembro2018((BigDecimal) row[21]);
			hCliente.setTotal2018((BigDecimal) row[22]);
			
			hCliente.setJaneiro2019((BigDecimal) row[23]);
			hCliente.setFevereiro2019((BigDecimal) row[24]);
			hCliente.setMarco2019((BigDecimal) row[25]);
			hCliente.setAbril2019((BigDecimal) row[26]);
			hCliente.setMaio2019((BigDecimal) row[27]);
			hCliente.setJunho2019((BigDecimal) row[28]);
			hCliente.setJulho2019((BigDecimal) row[29]);
			hCliente.setAgosto2019((BigDecimal) row[30]);
			hCliente.setSetembro2019((BigDecimal) row[31]);
			hCliente.setOutrubo2019((BigDecimal) row[32]);
			hCliente.setNovembro2019((BigDecimal) row[33]);
			hCliente.setDezembro2019((BigDecimal) row[34]);
			hCliente.setTotal2019((BigDecimal) row[35]);
			
			hCliente.setJaneiro2020((BigDecimal) row[36]);
			hCliente.setFevereiro2020((BigDecimal) row[37]);
			hCliente.setMarco2020((BigDecimal) row[38]);
			hCliente.setAbril2020((BigDecimal) row[39]);
			hCliente.setMaio2020((BigDecimal) row[40]);
			hCliente.setJunho2020((BigDecimal) row[41]);
			hCliente.setJulho2020((BigDecimal) row[42]);
			hCliente.setAgosto2020((BigDecimal) row[43]);
			hCliente.setSetembro2020((BigDecimal) row[44]);
			hCliente.setOutrubo2020((BigDecimal) row[45]);
			hCliente.setNovembro2020((BigDecimal) row[46]);
			hCliente.setDezembro2020((BigDecimal) row[47]);
			hCliente.setTotal2020((BigDecimal) row[48]);
			
			hCliente.setJaneiro2021((BigDecimal) row[49]);
			hCliente.setFevereiro2021((BigDecimal) row[50]);
			hCliente.setMarco2021((BigDecimal) row[51]);
			hCliente.setAbril2021((BigDecimal) row[52]);
			hCliente.setMaio2021((BigDecimal) row[53]);
			hCliente.setJunho2021((BigDecimal) row[54]);
			hCliente.setJulho2021((BigDecimal) row[55]);
			hCliente.setAgosto2021((BigDecimal) row[56]);
			hCliente.setSetembro2021((BigDecimal) row[57]);
			hCliente.setOutrubo2021((BigDecimal) row[58]);
			hCliente.setNovembro2021((BigDecimal) row[59]);
			hCliente.setDezembro2021((BigDecimal) row[60]);
			hCliente.setTotal2021((BigDecimal) row[61]);
			
			hCliente.setTotalgeral((BigDecimal) row[62]);
			hCliente.setMixqtde((BigInteger) row[63]);
			hCliente.setMixqtdemedio((BigDecimal) row[64]);
			hCliente.setTicketmedio((BigDecimal) row[65]);
			hCliente.setNvendas((BigInteger) row[66]);
			hCliente.setPrimeiravenda((Date) row[67]);
			hCliente.setUltimavenda((Date) row[68]);
			hCliente.setFrequenciamedia((Double) row[69]);
			hCliente.setCnpjcpf((String) row[70]);
			hCliente.setBairro((String) row[71]);
			hCliente.setStatus((String) row[72]);
			
			hCliente.setJaneiro2022((BigDecimal) row[73]);
			hCliente.setFevereiro2022((BigDecimal) row[74]);
			hCliente.setMarco2022((BigDecimal) row[75]);
			hCliente.setAbril2022((BigDecimal) row[76]);
			hCliente.setMaio2022((BigDecimal) row[77]);
			hCliente.setJunho2022((BigDecimal) row[78]);
			hCliente.setJulho2022((BigDecimal) row[79]);
			hCliente.setAgosto2022((BigDecimal) row[80]);
			hCliente.setSetembro2022((BigDecimal) row[81]);
			hCliente.setOutrubo2022((BigDecimal) row[82]);
			hCliente.setNovembro2022((BigDecimal) row[83]);
			hCliente.setDezembro2022((BigDecimal) row[84]);
			hCliente.setTotal2022((BigDecimal) row[85]);
			
			hCliente.setJaneiro2023((BigDecimal) row[86]);
			hCliente.setFevereiro2023((BigDecimal) row[87]);
			hCliente.setMarco2023((BigDecimal) row[88]);
			hCliente.setAbril2023((BigDecimal) row[89]);
			hCliente.setMaio2023((BigDecimal) row[90]);
			hCliente.setJunho2023((BigDecimal) row[91]);
			hCliente.setJulho2023((BigDecimal) row[92]);
			hCliente.setAgosto2023((BigDecimal) row[93]);
			hCliente.setSetembro2023((BigDecimal) row[94]);
			hCliente.setOutrubo2023((BigDecimal) row[95]);
			hCliente.setNovembro2023((BigDecimal) row[96]);
			hCliente.setDezembro2023((BigDecimal) row[97]);
			hCliente.setTotal2023((BigDecimal) row[98]);
			
			hCliente.setJaneiro2024((BigDecimal) row[99]);
			hCliente.setFevereiro2024((BigDecimal) row[100]);
			hCliente.setMarco2024((BigDecimal) row[101]);
			hCliente.setAbril2024((BigDecimal) row[102]);
			hCliente.setMaio2024((BigDecimal) row[103]);
			hCliente.setJunho2024((BigDecimal) row[104]);
			hCliente.setJulho2024((BigDecimal) row[105]);
			hCliente.setAgosto2024((BigDecimal) row[106]);
			hCliente.setSetembro2024((BigDecimal) row[107]);
			hCliente.setOutrubo2024((BigDecimal) row[108]);
			hCliente.setNovembro2024((BigDecimal) row[109]);
			hCliente.setDezembro2024((BigDecimal) row[110]);
			hCliente.setTotal2024((BigDecimal) row[111]);
			
			list.add(hCliente);
		}
		
		return list;
	}	
	
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2, String subgrupo1, String subgrupo2,String cliente1, String cliente2,Date data1, Date data2){
		List<MixProduto> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " pr.produtoid, "
				+ " pr.NOME_PRODUTO, "
				+ " MIXQTDE.QTDE2018 , "
				+ " MIXQTDE.QTDE2019 , "
				+ " MIXQTDE.QTDE2020 , "
				+ " MIXQTDE.QTDE2021 , "
				+ " MIXQTDE.QTDETOTAL , "
				+ " MIXVL.VL2018 , "
				+ " MIXVL.VL2019 , "
				+ " MIXVL.VL2020 , "
				+ " MIXVL.VL2021 , "
				+ " MIXVL.VLTOTAL,  "
				+ " MIXQTDE.QTDE2022, "
				+ " MIXVL.VL2022, "
				+ " MIXQTDE.QTDE2023, "
				+ " MIXVL.VL2023, "
				+ " MIXQTDE.QTDE2024, "
				+ " MIXVL.VL2024 "
				+ " from produto pr "
				+ " "
				+ " inner join( "
				+ " SELECT DISTINCT "
				+ " it.produtoid                          "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " inner join cliente cl on cl.cadcftvid = p.cadcftvid "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " and cl.VENDEDORID1 = "+ usuarioconectado()
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " )mixpr on mixpr.produtoid = pr.produtoid  "
				+ " "
				+ " "
				+ " LEFT JOIN( "
				+ " SELECT MIXQTDE.produtoid, "
				+ " SUM(MIXQTDE.QTDE2018) QTDE2018, "
				+ " SUM(MIXQTDE.QTDE2019) QTDE2019, "
				+ " SUM(MIXQTDE.QTDE2020) QTDE2020, "
				+ " SUM(MIXQTDE.QTDE2021) QTDE2021, "
				+ " SUM(MIXQTDE.QTDE2022) QTDE2022, "
				+ " SUM(MIXQTDE.QTDE2023) QTDE2023, "
				+ " SUM(MIXQTDE.QTDE2024) QTDE2024, "
				+ " SUM(MIXQTDE.QTDE2018)+SUM(MIXQTDE.QTDE2019)+ SUM(MIXQTDE.QTDE2020)+SUM(MIXQTDE.QTDE2021)+SUM(MIXQTDE.QTDE2022)+SUM(MIXQTDE.QTDE2023)+SUM(MIXQTDE.QTDE2024) QTDETOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2018' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2018, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2019' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2019, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2020' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2020, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2021' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2021, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2022' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2022,  "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2023' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2023,  "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2024' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2024  "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " inner join cliente cl on cl.cadcftvid = p.cadcftvid "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and cl.VENDEDORID1 = "+ usuarioconectado()
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " )MIXQTDE GROUP BY MIXQTDE.produtoid "
				+ " )MIXQTDE ON MIXQTDE.produtoid = PR.PRODUTOID "
				+ " "
				+ " LEFT JOIN( "
				+ " SELECT MIXVL.produtoid, "
				+ " SUM(MIXVL.VL2018) VL2018, "
				+ " SUM(MIXVL.VL2019) VL2019, "
				+ " SUM(MIXVL.VL2020) VL2020, "
				+ " SUM(MIXVL.VL2021) VL2021, "
				+ " SUM(MIXVL.VL2022) VL2022, "
				+ " SUM(MIXVL.VL2023) VL2023, "
				+ " SUM(MIXVL.VL2024) VL2024, "
				+ " SUM(MIXVL.VL2018)+SUM(MIXVL.VL2019)+ SUM(MIXVL.VL2020)+SUM(MIXVL.VL2021)+SUM(MIXVL.VL2022)+SUM(MIXVL.VL2023)+SUM(MIXVL.VL2024) VLTOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2018' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2018, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2019' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2019, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2020' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2020, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2021' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2021, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2022' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2022,  "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2023' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2023,  "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2024' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2024  "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " inner join cliente cl on cl.cadcftvid = p.cadcftvid "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and cl.VENDEDORID1 = "+ usuarioconectado()
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " )MIXVL GROUP BY MIXVL.produtoid "
				+ " )MIXVL ON MIXVL.produtoid = PR.PRODUTOID "
				+ " "
				//+ " where pr.TP_PRODUTO = 'ACABADO' "
				+ " ORDER BY  pr.NOME_PRODUTO "
				+ "");
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			MixProduto mix= new MixProduto();
		
			mix.setCodigoproduto((BigDecimal)row[0]);
			mix.setNomeproduto((String)row[1]);
			
			mix.setQtde2018((BigDecimal)row[2]);
			mix.setQtde2019((BigDecimal)row[3]);
			mix.setQtde2020((BigDecimal)row[4]);
			mix.setQtde2021((BigDecimal)row[5]);
			mix.setQtdetotal((BigDecimal)row[6]);
			
			mix.setVl2018((BigDecimal)row[7]);
			mix.setVl2019((BigDecimal)row[8]);
			mix.setVl2020((BigDecimal)row[9]);
			mix.setVl2021((BigDecimal)row[10]);
			mix.setVltotal((BigDecimal)row[11]);
			
			mix.setQtde2022((BigDecimal)row[12]);
			mix.setVl2022((BigDecimal)row[13]);
			
			mix.setQtde2023((BigDecimal)row[14]);
			mix.setVl2023((BigDecimal)row[15]);
			
			mix.setQtde2024((BigDecimal)row[16]);
			mix.setVl2024((BigDecimal)row[17]);
			
			list.add(mix);
		}
				
		return list;
	}
	
	public List<Cliente> clientes() {
		List<Cliente> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " V.CADCFTVID cliente, " 
						+ " v.NOME_CADCFTV nome_cliente, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID AND V2.VENDEDORID1 = " + usuarioconectado()	
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+ " order by v.cadcftvid ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			Cliente cliente = new Cliente();

			cliente.setCodigocliente((BigDecimal) row[0]);
			cliente.setNomecliente((String) row[1]);
			cliente.setCpfcnpj((String) row[2]);
			
			list.add(cliente);
		}
		return list;
	}
	
	public List<Produto> produtos(){
		List<Produto> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
				+ "pr.produtoid, "
				+ "pr.REFERENCIA_PRODUTO, "
				+ "pr.NOME_PRODUTO, "
				+ "pr.SUBGRUPOPRODUTOID, "
				+ "sub.GRUPOPRODUTOID "
				+ "from produto pr "
				+ "inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ "inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ "WHERE pr.TP_PRODUTO = 'ACABADO' ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Produto p = new Produto();
			p.setProdutoid((BigDecimal)row[0]);
			p.setReferencia((String) row[1]);
			p.setNomeproduto((String) row[2]);
			p.setSubgrupoid((BigDecimal)row[3]);
			p.setGrupoid((BigDecimal) row[4]);
			
			list.add(p);
		}
		
		return list;
	}
	
	public List<Venda_Grupo> grupos(){
		List<Venda_Grupo> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
				+ "gr.GRUPOPRODUTOID, "
				+ "gr.NOME_GRUPOPRODUTO "
				+ "from GRUPOPRODUTO gr ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Venda_Grupo v = new Venda_Grupo();
			v.setIdgrupo((BigDecimal) row[0]);
			v.setNomegrupo((String) row[1]);
			
			list.add(v);
		}
		return list;
		
	}
	public List<Venda_Subgrupo> subgrupos(){
		List<Venda_Subgrupo> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
				+ "gr.GRUPOPRODUTOID, "
				+ "gr.SUBGRUPOPRODUTOID, "
				+ "gr.NOME_SUBGRUPOPRODUTO "
				+ "from SUBGRUPOPRODUTO gr ");
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Venda_Subgrupo v = new Venda_Subgrupo();
			v.setIdgrupo((BigDecimal) row[0]);
			v.setIdsubgrupo((BigDecimal) row[1]);
			v.setNomesubgrupo((String) row[2]);
			
			list.add(v);
		}
		return list;
	}

	/* venda ano mes */
	public List<VendaAnoMes> vendaanomes() {
		List<VendaAnoMes> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" SELECT " + " TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') AS ANO, " 
						+ " TO_CHAR(EN.DT_PEDIDOVENDA,'MM') AS MES, "
						+ " SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VL_TOTAL " 
						+ " FROM PEDIDOVENDA EN "
						+ " INNER JOIN VENDEDOR V ON V.CADCFTVID = EN.VENDEDOR1ID "
					
						+ " INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID " 
						+ " WHERE "
						+ " CF.TIPOOPERACAO_CFOP = 'VENDA' "
						+ " AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+ " AND EN.VENDEDOR1ID =  "+ usuarioconectado()
						+ " GROUP BY "
						+ " TO_CHAR(EN.DT_PEDIDOVENDA,'MM'), " + " TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') " 
						+ " ORDER BY "
						+ " TO_CHAR(EN.DT_PEDIDOVENDA,'MM'),TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') ");
		// query.setParameter("vendedorlogado", usuarioconectado());
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			VendaAnoMes vendaAnoMes = new VendaAnoMes();

			vendaAnoMes.setAno((String) row[0]);
			vendaAnoMes.setMes((String) row[1]);
			vendaAnoMes.setValor((BigDecimal) row[2]);
			// System.out.println((String) row[0]+","+(String)
			// row[1]+","+(BigDecimal) row[2]);
			list.add(vendaAnoMes);

		}
		

		return list;
	}
	//venda por grupo e subgrupo
	public List<VendaGrupoSubGrupoProdutoQuantidadeValor> vendaGrupoSubGrupoProdutoQuantidadeValor(Date data1,
			Date data2) {
		List<VendaGrupoSubGrupoProdutoQuantidadeValor> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" SELECT " + " sub.NOME_SUBGRUPOPRODUTO as subgrupo, " + " sum(it.qt_pedidovenda_item) qtde, "
						+ " sum(IT.vl_total_pedidovenda_item) vlr " + " from pedidovenda p "
						+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
						+ " INNER JOIN PRODUTO PR ON PR.PRODUTOID = IT.PRODUTOID "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " inner join subgrupoproduto sub on sub.subgrupoprodutoid = pr.subgrupoprodutoid "
						+ " inner join GRUPOPRODUTO gru on gru.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
						+ " WHERE p.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " group by sub.NOME_SUBGRUPOPRODUTO " + " order by sub.NOME_SUBGRUPOPRODUTO ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			VendaGrupoSubGrupoProdutoQuantidadeValor vendaGrupoSubGrupoProdutoQuantidadeValor = new VendaGrupoSubGrupoProdutoQuantidadeValor();

			vendaGrupoSubGrupoProdutoQuantidadeValor.setSubgrupo((String) row[0]);
			vendaGrupoSubGrupoProdutoQuantidadeValor.setQuantidade((BigDecimal) row[1]);
			vendaGrupoSubGrupoProdutoQuantidadeValor.setValor((BigDecimal) row[2]);
			// System.out.println((String) row[0]+","+(String)
			// row[1]+","+(BigDecimal) row[2]);
			list.add(vendaGrupoSubGrupoProdutoQuantidadeValor);
		}

		return list;
	}
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String cliente1, String cliente2) {
		List<VendasEmGeral> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
						" select " 
						+ " CI.CADCFTVID AS CLIENTE, " 
						+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
						+ " p.NR_NOTA_PEDIDOVENDA nota, " 
						+ " P.DT_FATURAMENTO_PEDIDOVENDA AS DATAfaturamento, "
						+ " p.vl_totalprod_pedidovenda, " 
						+ " pg.nome_formapagto as prazo, "
						+ " t.desc_tipo_pedido as tipo_pedido, " 
						+ " CF.tipooperacao_cfop, " 
						+ " p.status_pedidovenda, "
						+ " v.NOME_CADCFTV nome_vendedor "
						+ " from pedidovenda p "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
						+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
						+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " inner join roteiro r on r.roteiroid = p.roteiroid "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " where p.status_pedidovenda in ('FATURADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
						+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
						+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
						+ " ORDER BY P.NR_NOTA_PEDIDOVENDA  ");

		List<Object[]> lista = query.getResultList();
		
		

		for (Object[] row : lista) {
			VendasEmGeral vendasEmGeral = new VendasEmGeral();
			
			String nota = (String) row[2];
			Integer notr = Integer.valueOf(nota);
			BigDecimal notag = new BigDecimal(notr.intValue());

			vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
			vendasEmGeral.setNomecliente((String) row[1] );
			vendasEmGeral.setPedido(notag);
			vendasEmGeral.setDatapedido((Date) row[3] );
			vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
			vendasEmGeral.setPrazo((String) row[5] );
			vendasEmGeral.setTipopedido((String) row[6] );
			vendasEmGeral.setTipooperacaocfop((String) row[7] );
			vendasEmGeral.setStatuspedido((String) row[8] );
			vendasEmGeral.setNomevendedor((String) row[9] );
			
			
			
			list.add(vendasEmGeral);
		}

		return list;
	}
	
	//pedidos de venda
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
		List<VendasEmGeral> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
		String SdataFormatada = formato2.format(data1);
		String SdataFormatada2 = formato2.format(data2);

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
						" select " 
						+ " CI.CADCFTVID AS CLIENTE, " 
						+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
						+ " p.pedidovendaid pedido, " 
						+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
						+ " p.vl_totalprod_pedidovenda, " 
						+ " pg.nome_formapagto as prazo, "
						+ " t.desc_tipo_pedido as tipo_pedido, " 
						+ " CF.tipooperacao_cfop, " 
						+ " p.status_pedidovenda, "
						+ " v.NOME_CADCFTV nome_vendedor, "
						+ " 'SEVEN' as origem "
						+ " from pedidovenda p "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
						+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
						+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " inner join roteiro r on r.roteiroid = p.roteiroid "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' " 
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
						+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
						+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
						+ " ORDER BY P.PEDIDOVENDAID  ");

		List<Object[]> lista = query.getResultList();
		
		

		for (Object[] row : lista) {
			VendasEmGeral vendasEmGeral = new VendasEmGeral();

			vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
			vendasEmGeral.setNomecliente((String) row[1] );
			vendasEmGeral.setPedido((BigDecimal) row[2] );
			vendasEmGeral.setDatapedido((Date) row[3] );
			vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
			vendasEmGeral.setPrazo((String) row[5] );
			vendasEmGeral.setTipopedido((String) row[6] );
			vendasEmGeral.setTipooperacaocfop((String) row[7] );
			vendasEmGeral.setStatuspedido((String) row[8] );
			vendasEmGeral.setNomevendedor((String) row[9] );
			vendasEmGeral.setOrigem((String) row[10] );
			
			list.add(vendasEmGeral);
		}
		
		//adicionando parte do SIge
		javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
			" select s.Cod_cli_for as cod_cliente, "
			+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
			+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
			+" 'VENDA' as tipooperacao,'Fechado'as statuspedido,v.Nome_cadastro nome_vendedor, 'SIGE' as origem "
			
			+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
			+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
			+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
			+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
			+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
			+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
			+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
			+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
			+" where s.Cod_tipo_mv in ('520')and p.Cod_tipo_mv in ('510')  "
			+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
			+" and s.Cod_vend_comp ="+ usuarioconectado()
			+" and s.Cod_cli_for between ' " + cliente1 + " ' and ' " + cliente2 + "' "
			+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
			
		
		List<Object[]> listasige = querySige.getResultList();
		if(listasige.size()>0){
		for (Object[] rowsige : listasige) {
			VendasEmGeral vendasEmGeral = new VendasEmGeral();
			
			int scliente = (Integer) rowsige[0];
			int spedido = (Integer) rowsige[2];

			vendasEmGeral.setCodigocliente(new BigDecimal(scliente));
			vendasEmGeral.setNomecliente((String) rowsige[1] );
			vendasEmGeral.setPedido(new BigDecimal(spedido) );
			vendasEmGeral.setDatapedido((Date) rowsige[3] );
			vendasEmGeral.setValortotalpedido((BigDecimal) rowsige[4] );
			vendasEmGeral.setPrazo((String) rowsige[5] );
			vendasEmGeral.setTipopedido((String) rowsige[6] );
			vendasEmGeral.setTipooperacaocfop((String) rowsige[7] );
			vendasEmGeral.setStatuspedido((String) rowsige[8] );
			vendasEmGeral.setNomevendedor((String) rowsige[9] );
			vendasEmGeral.setOrigem((String) rowsige[10] );
			
			list.add(vendasEmGeral);			
			}
		}
		//fim da parte do sige

		return list;
	}
	//detalhes do pedido de venda
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido, String origem) {
		List<VendasEmGeralItem> list = new ArrayList<>();
		
		if (origem.equals("SEVEN")) {
		javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
						" select " 
						+ " CI.CADCFTVID AS CLIENTE, " 
						+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
						+ " p.pedidovendaid pedido, " 
						+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
						+ " p.vl_totalprod_pedidovenda, " 
						+ " pg.nome_formapagto as prazo, "
						+ " t.desc_tipo_pedido as tipo_pedido, " 
						+ " CF.tipooperacao_cfop, " 
						+ " p.status_pedidovenda, "
						+ " it.produtoid, " 
						+ " it.ds_produto_pedidovenda_item, " 
						+ " it.qt_pedidovenda_item, "
						+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
						+ " IT.vl_total_pedidovenda_item, "
						+ " x1.NR_NOTA_PEDIDOVENDA,  " 
						+ " x1.DT_SAIDA_PEDIDOVENDA,  "
						+ " x1.STATUS_PEDIDOVENDA status_nota, " 
						+ " IMG.IMAGEM_PRODUTO "
						+ " from pedidovenda p "
						+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
						+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
						+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
						+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " inner join roteiro r on r.roteiroid = p.roteiroid "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " left join ( " 
						+ " select " 
						+ " nota.NR_NOTA_PEDIDOVENDA, " 
						+ " nota.DT_SAIDA_PEDIDOVENDA,  "
						+ " nota.STATUS_PEDIDOVENDA, " 
						+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
						+ " IT_nota.produtoid "
						+ " from PEDIDOVENDA_ITEM IT_nota "
						+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
						+ " group by  "
						+ " nota.NR_NOTA_PEDIDOVENDA, " 
						+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
						+ " nota.STATUS_PEDIDOVENDA, "
						+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
						+ " IT_nota.produtoid "
						+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' " 
						+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
						+ " and p.pedidovendaid = ' " + pedido + " ' " 
						+ " ORDER BY P.PEDIDOVENDAID  ");
		List<Object[]> lista2 = query2.getResultList();
		
		for (Object[] row2 : lista2) {
			
				
			VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
			
			vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
			vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
			vendasEmGeralItem.setNomeproduto((String) row2[10] );
			vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
			vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
			vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
			vendasEmGeralItem.setNota((String) row2[14] );
			vendasEmGeralItem.setDatanota((Date) row2[15] );
			vendasEmGeralItem.setStatusnota((String) row2[16] );
			//vendasEmGeralItem.setImagem((Blob) row2[17] );
			
			list.add(vendasEmGeralItem);
		}
		}
		if (origem.equals("SIGE")) {
			//adicionando parte do SIge
			javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
				" select s.Cod_cli_for as cod_cliente, "
				+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
				+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
				+" 'VENDA' as tipooperacao,'Fechado'as statuspedido, v.Nome_cadastro nome_vendedor, 'SIGE' as origem, "
				+" cast(it.Cod_produto as integer)produto, pr.Desc_produto_est, cast(it.Qtde_pri as integer) qtde , it.Valor_unitario, it.Valor_liquido "
				
				+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
				+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
				+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
				+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
				+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
				+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
				+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
				+" inner join tbSaidasItem it on it.Chave_fato = p.Chave_fato "
				+" inner join tbproduto pr on pr.Cod_produto = it.Cod_produto "
				+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
				+" where s.Cod_tipo_mv in ('520','523','527') and p.Cod_tipo_mv in ('510')  "
				+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
				+" and s.Cod_vend_comp ="+ usuarioconectado()
				+" AND P.Num_docto ="+ pedido);
			
			List<Object[]> listasige = querySige.getResultList();
			
			if(listasige.size()>0){
			for (Object[] rowsige : listasige) {
				VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
				
				int spedido = (Integer) rowsige[2];
				int sproduto = (Integer) rowsige[11];
				int sqtde = (Integer) rowsige[13];
				
				vendasEmGeralItem.setPedido(new BigDecimal(spedido));
				vendasEmGeralItem.setCodigoproduto(new BigDecimal(sproduto));
				vendasEmGeralItem.setNomeproduto((String) rowsige[12] );
				vendasEmGeralItem.setQuantidadeproduto(new BigDecimal(sqtde));
				vendasEmGeralItem.setValorunitarioproduto((BigDecimal) rowsige[14] );
				vendasEmGeralItem.setValortotalproduto((BigDecimal) rowsige[15] );
				vendasEmGeralItem.setNota(null);
				vendasEmGeralItem.setDatanota(null);
				vendasEmGeralItem.setStatusnota(null);
				vendasEmGeralItem.setImagem(null );
				
				
				list.add(vendasEmGeralItem);
			}
				
			}
			
		}
		return list;
		
	}
	
	//pedidos de amostra
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
			List<VendasEmGeral> list = new ArrayList<>();
			
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String dataFormatada = formato.format(data1);
			String dataFormatada2 = formato.format(data2);
			
			SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
			String SdataFormatada = formato2.format(data1);
			String SdataFormatada2 = formato2.format(data2);

			javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
					// "SELECT * FROM("
							" select " 
							+ " CI.CADCFTVID AS CLIENTE, " 
							+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
							+ " p.pedidovendaid pedido, " 
							+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
							+ " p.vl_totalprod_pedidovenda, " 
							+ " pg.nome_formapagto as prazo, "
							+ " t.desc_tipo_pedido as tipo_pedido, " 
							+ " CF.tipooperacao_cfop, " 
							+ " p.status_pedidovenda, "
							+ " v.NOME_CADCFTV nome_vendedor, 'SEVEN' as origem "
							+ " from pedidovenda p "
							+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
							+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
							+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
							+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
							+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
							+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
							+ " and p.TIPOPEDIDOID = 4  AND P.VENDEDOR1ID =  "+ usuarioconectado()
							+ " ORDER BY P.PEDIDOVENDAID  ");

			List<Object[]> lista = query.getResultList();
			
			

			for (Object[] row : lista) {
				VendasEmGeral vendasEmGeral = new VendasEmGeral();

				vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
				vendasEmGeral.setNomecliente((String) row[1] );
				vendasEmGeral.setPedido((BigDecimal) row[2] );
				vendasEmGeral.setDatapedido((Date) row[3] );
				vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
				vendasEmGeral.setPrazo((String) row[5] );
				vendasEmGeral.setTipopedido((String) row[6] );
				vendasEmGeral.setTipooperacaocfop((String) row[7] );
				vendasEmGeral.setStatuspedido((String) row[8] );
				vendasEmGeral.setNomevendedor((String) row[9] );
				vendasEmGeral.setOrigem((String) row[10] );
				
				
				list.add(vendasEmGeral);
			}
			
			//adicionando parte do SIge
			javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
				" select s.Cod_cli_for as cod_cliente, "
				+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
				+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
				+" 'AMOSTRA' as tipooperacao,'Fechado'as statuspedido,v.Nome_cadastro nome_vendedor, 'SIGE' as origem "
				
				+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
				+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
				+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
				+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
				+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
				+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
				+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
				+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
				+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('516')  "
				+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
				+" and s.Cod_vend_comp ="+ usuarioconectado()
				+" and s.Cod_cli_for between ' " + cliente1 + " ' and ' " + cliente2 + "' "
				+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
			
			List<Object[]> listasige = querySige.getResultList();
			if(listasige.size()>0){
			for (Object[] rowsige : listasige) {
				VendasEmGeral vendasEmGeral = new VendasEmGeral();
				
				int scliente = (Integer) rowsige[0];
				int spedido = (Integer) rowsige[2];

				vendasEmGeral.setCodigocliente(new BigDecimal(scliente));
				vendasEmGeral.setNomecliente((String) rowsige[1] );
				vendasEmGeral.setPedido(new BigDecimal(spedido) );
				vendasEmGeral.setDatapedido((Date) rowsige[3] );
				vendasEmGeral.setValortotalpedido((BigDecimal) rowsige[4] );
				vendasEmGeral.setPrazo((String) rowsige[5] );
				vendasEmGeral.setTipopedido((String) rowsige[6] );
				vendasEmGeral.setTipooperacaocfop((String) rowsige[7] );
				vendasEmGeral.setStatuspedido((String) rowsige[8] );
				vendasEmGeral.setNomevendedor((String) rowsige[9] );
				vendasEmGeral.setOrigem((String) rowsige[10] );
				
				list.add(vendasEmGeral);			
				}
			}
			//fim da parte do sige

			return list;
		}
	
	//detalhes do pedido de amostra
		public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido,String origem){
			List<VendasEmGeralItem> list = new ArrayList<>();
			
			if (origem.equals("SEVEN")) {
			javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
					// "SELECT * FROM("
							" select " 
							+ " CI.CADCFTVID AS CLIENTE, " 
							+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
							+ " p.pedidovendaid pedido, " 
							+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
							+ " p.vl_totalprod_pedidovenda, " 
							+ " pg.nome_formapagto as prazo, "
							+ " t.desc_tipo_pedido as tipo_pedido, " 
							+ " CF.tipooperacao_cfop, " 
							+ " p.status_pedidovenda, "
							+ " it.produtoid, " 
							+ " it.ds_produto_pedidovenda_item, " 
							+ " it.qt_pedidovenda_item, "
							+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
							+ " IT.vl_total_pedidovenda_item, "
							+ " x1.NR_NOTA_PEDIDOVENDA,  " 
							+ " x1.DT_SAIDA_PEDIDOVENDA,  "
							+ " x1.STATUS_PEDIDOVENDA status_nota, " 
							+ " IMG.IMAGEM_PRODUTO "
							+ " from pedidovenda p "
							+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
							+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
							+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
							+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
							+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
							+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " left join ( " 
							+ " select " 
							+ " nota.NR_NOTA_PEDIDOVENDA, " 
							+ " nota.DT_SAIDA_PEDIDOVENDA,  "
							+ " nota.STATUS_PEDIDOVENDA, " 
							+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
							+ " IT_nota.produtoid "
							+ " from PEDIDOVENDA_ITEM IT_nota "
							+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
							+ " group by  "
							+ " nota.NR_NOTA_PEDIDOVENDA, " 
							+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
							+ " nota.STATUS_PEDIDOVENDA, "
							+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
							+ " IT_nota.produtoid "
							+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
							+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
							+ " and p.TIPOPEDIDOID = 4 and p.pedidovendaid = ' " + pedido + " ' " 
							+ " ORDER BY P.PEDIDOVENDAID  ");
			List<Object[]> lista2 = query2.getResultList();
			
			for (Object[] row2 : lista2) {
				
					
				VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
				
				vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
				vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
				vendasEmGeralItem.setNomeproduto((String) row2[10] );
				vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
				vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
				vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
				vendasEmGeralItem.setNota((String) row2[14] );
				vendasEmGeralItem.setDatanota((Date) row2[15] );
				vendasEmGeralItem.setStatusnota((String) row2[16] );
				//vendasEmGeralItem.setImagem((Blob) row2[17] );
				
				list.add(vendasEmGeralItem);
			}
			}
			if (origem.equals("SIGE")) {
				//adicionando parte do SIge
				javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
					" select s.Cod_cli_for as cod_cliente, "
					+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
					+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
					+" 'VENDA' as tipooperacao,'Fechado'as statuspedido, v.Nome_cadastro nome_vendedor, 'SIGE' as origem, "
					+" cast(it.Cod_produto as integer)produto, pr.Desc_produto_est, cast(it.Qtde_pri as integer) qtde , it.Valor_unitario, it.Valor_liquido "
					
					+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
					+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
					+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
					+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
					+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
					+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
					+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
					+" inner join tbSaidasItem it on it.Chave_fato = p.Chave_fato "
					+" inner join tbproduto pr on pr.Cod_produto = it.Cod_produto "
					+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
					+" where s.Cod_tipo_mv in ('520','523','527') and p.Cod_tipo_mv in ('516')  "
					+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
					+" and s.Cod_vend_comp ="+ usuarioconectado()
					+" AND P.Num_docto ="+ pedido);
				
				List<Object[]> listasige = querySige.getResultList();
				
				if(listasige.size()>0){
				for (Object[] rowsige : listasige) {
					VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
					
					int spedido = (Integer) rowsige[2];
					int sproduto = (Integer) rowsige[11];
					int sqtde = (Integer) rowsige[13];
					
					vendasEmGeralItem.setPedido(new BigDecimal(spedido));
					vendasEmGeralItem.setCodigoproduto(new BigDecimal(sproduto));
					vendasEmGeralItem.setNomeproduto((String) rowsige[12] );
					vendasEmGeralItem.setQuantidadeproduto(new BigDecimal(sqtde));
					vendasEmGeralItem.setValorunitarioproduto((BigDecimal) rowsige[14] );
					vendasEmGeralItem.setValortotalproduto((BigDecimal) rowsige[15] );
					vendasEmGeralItem.setNota(null);
					vendasEmGeralItem.setDatanota(null);
					vendasEmGeralItem.setStatusnota(null);
					vendasEmGeralItem.setImagem(null );
					
					
					list.add(vendasEmGeralItem);
				}
					
				}
			}
			return list;
			
		}

		public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
			List<VendasEmGeral> list = new ArrayList<>();
			
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String dataFormatada = formato.format(data1);
			String dataFormatada2 = formato.format(data2);

			javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
					// "SELECT * FROM("
							" select " 
							+ " CI.CADCFTVID AS CLIENTE, " 
							+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
							+ " p.pedidovendaid pedido, " 
							+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
							+ " p.vl_totalprod_pedidovenda, " 
							+ " pg.nome_formapagto as prazo, "
							+ " t.desc_tipo_pedido as tipo_pedido, " 
							+ " CF.tipooperacao_cfop, " 
							+ " p.status_pedidovenda, "
							+ " v.NOME_CADCFTV nome_vendedor "
							+ " from pedidovenda p "
							+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
							+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
							+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
							+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop = 'VENDA' AND P.VENDEDOR1ID =  "+ usuarioconectado()
							+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
							+ " and p.TIPOPEDIDOID = 6 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
							+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
							+ " ORDER BY P.PEDIDOVENDAID  ");

			List<Object[]> lista = query.getResultList();
			
			

			for (Object[] row : lista) {
				VendasEmGeral vendasEmGeral = new VendasEmGeral();

				vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
				vendasEmGeral.setNomecliente((String) row[1] );
				vendasEmGeral.setPedido((BigDecimal) row[2] );
				vendasEmGeral.setDatapedido((Date) row[3] );
				vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
				vendasEmGeral.setPrazo((String) row[5] );
				vendasEmGeral.setTipopedido((String) row[6] );
				vendasEmGeral.setTipooperacaocfop((String) row[7] );
				vendasEmGeral.setStatuspedido((String) row[8] );
				vendasEmGeral.setNomevendedor((String) row[9] );
				
				
				
				list.add(vendasEmGeral);
			}

			return list;
		}
		
		public List<VendasEmGeralItem> amostrapagaemgeralitem(BigDecimal pedido) {
			List<VendasEmGeralItem> list = new ArrayList<>();
			
			/*lista 2*/
			javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
					// "SELECT * FROM("
							" select " 
							+ " CI.CADCFTVID AS CLIENTE, " 
							+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
							+ " p.pedidovendaid pedido, " 
							+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
							+ " p.vl_totalprod_pedidovenda, " 
							+ " pg.nome_formapagto as prazo, "
							+ " t.desc_tipo_pedido as tipo_pedido, " 
							+ " CF.tipooperacao_cfop, " 
							+ " p.status_pedidovenda, "
							+ " it.produtoid, " 
							+ " it.ds_produto_pedidovenda_item, " 
							+ " it.qt_pedidovenda_item, "
							+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
							+ " IT.vl_total_pedidovenda_item, "
							+ " x1.NR_NOTA_PEDIDOVENDA,  " 
							+ " x1.DT_SAIDA_PEDIDOVENDA,  "
							+ " x1.STATUS_PEDIDOVENDA status_nota, " 
							+ " IMG.IMAGEM_PRODUTO "
							+ " from pedidovenda p "
							+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
							+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
							+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
							+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
							+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
							+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " left join ( " 
							+ " select " 
							+ " nota.NR_NOTA_PEDIDOVENDA, " 
							+ " nota.DT_SAIDA_PEDIDOVENDA,  "
							+ " nota.STATUS_PEDIDOVENDA, " 
							+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
							+ " IT_nota.produtoid "
							+ " from PEDIDOVENDA_ITEM IT_nota "
							+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
							+ " group by  "
							+ " nota.NR_NOTA_PEDIDOVENDA, " 
							+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
							+ " nota.STATUS_PEDIDOVENDA, "
							+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
							+ " IT_nota.produtoid "
							+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop = 'VENDA' "
							+ " and p.TIPOPEDIDOID = 6 and p.pedidovendaid = ' " + pedido + " ' " 
							+ " ORDER BY P.PEDIDOVENDAID  ");
			List<Object[]> lista2 = query2.getResultList();
			
			for (Object[] row2 : lista2) {
				
					
				VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
				
				vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
				vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
				vendasEmGeralItem.setNomeproduto((String) row2[10] );
				vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
				vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
				vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
				vendasEmGeralItem.setNota((String) row2[14] );
				vendasEmGeralItem.setDatanota((Date) row2[15] );
				vendasEmGeralItem.setStatusnota((String) row2[16] );
				//vendasEmGeralItem.setImagem((Blob) row2[17] );
				
				list.add(vendasEmGeralItem);
			}
			return list;
			
		}

		//pedidos de troca defeito
		public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2) {
				List<VendasEmGeral> list = new ArrayList<>();
					
					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
					String dataFormatada = formato.format(data1);
					String dataFormatada2 = formato.format(data2);
					
					SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
					String SdataFormatada = formato2.format(data1);
					String SdataFormatada2 = formato2.format(data2);

					javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
							// "SELECT * FROM("
									" select " 
									+ " CI.CADCFTVID AS CLIENTE, " 
									+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
									+ " p.pedidovendaid pedido, " 
									+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
									+ " p.vl_totalprod_pedidovenda, " 
									+ " pg.nome_formapagto as prazo, "
									+ " t.desc_tipo_pedido as tipo_pedido, " 
									+ " CF.tipooperacao_cfop, " 
									+ " p.status_pedidovenda, "
									+ " v.NOME_CADCFTV nome_vendedor, 'SEVEN' as origem "
									+ " from pedidovenda p "
									+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
									+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
									+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
									+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
									+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
									+ " inner join roteiro r on r.roteiroid = p.roteiroid "
									//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA' AND P.VENDEDOR1ID =  "+ usuarioconectado()
									+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
									+ " and p.TIPOPEDIDOID = 2 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
									+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
									+ " ORDER BY P.PEDIDOVENDAID  ");

					List<Object[]> lista = query.getResultList();
					
					

					for (Object[] row : lista) {
						VendasEmGeral vendasEmGeral = new VendasEmGeral();

						vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
						vendasEmGeral.setNomecliente((String) row[1] );
						vendasEmGeral.setPedido((BigDecimal) row[2] );
						vendasEmGeral.setDatapedido((Date) row[3] );
						vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
						vendasEmGeral.setPrazo((String) row[5] );
						vendasEmGeral.setTipopedido((String) row[6] );
						vendasEmGeral.setTipooperacaocfop((String) row[7] );
						vendasEmGeral.setStatuspedido((String) row[8] );
						vendasEmGeral.setNomevendedor((String) row[9] );
						vendasEmGeral.setOrigem((String) row[10] );
						
						
						list.add(vendasEmGeral);
					}
					
					//adicionando parte do SIge
					javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
						" select s.Cod_cli_for as cod_cliente, "
						+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
						+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
						+" 'AMOSTRA' as tipooperacao,'Fechado'as statuspedido,v.Nome_cadastro nome_vendedor, 'SIGE' as origem "
						
						+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
						+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
						+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
						+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
						+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
						+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
						+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
						+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
						+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('515')  "
						+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
						+" and s.Cod_vend_comp ="+ usuarioconectado()
						+" and s.Cod_cli_for between ' " + cliente1 + " ' and ' " + cliente2 + "' "
						+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
					
					List<Object[]> listasige = querySige.getResultList();
					if(listasige.size()>0){
					for (Object[] rowsige : listasige) {
						VendasEmGeral vendasEmGeral = new VendasEmGeral();
						
						int scliente = (Integer) rowsige[0];
						int spedido = (Integer) rowsige[2];

						vendasEmGeral.setCodigocliente(new BigDecimal(scliente));
						vendasEmGeral.setNomecliente((String) rowsige[1] );
						vendasEmGeral.setPedido(new BigDecimal(spedido) );
						vendasEmGeral.setDatapedido((Date) rowsige[3] );
						vendasEmGeral.setValortotalpedido((BigDecimal) rowsige[4] );
						vendasEmGeral.setPrazo((String) rowsige[5] );
						vendasEmGeral.setTipopedido((String) rowsige[6] );
						vendasEmGeral.setTipooperacaocfop((String) rowsige[7] );
						vendasEmGeral.setStatuspedido((String) rowsige[8] );
						vendasEmGeral.setNomevendedor((String) rowsige[9] );
						vendasEmGeral.setOrigem((String) rowsige[10] );
						
						list.add(vendasEmGeral);			
						}
					}
					//fim da parte do sige

					return list;
				}
			
			//detalhes do pedido de amostra
				public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido, String origem) {
					List<VendasEmGeralItem> list = new ArrayList<>();
					
					if (origem.equals("SEVEN")) {
					javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
							// "SELECT * FROM("
									" select " 
									+ " CI.CADCFTVID AS CLIENTE, " 
									+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
									+ " p.pedidovendaid pedido, " 
									+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
									+ " p.vl_totalprod_pedidovenda, " 
									+ " pg.nome_formapagto as prazo, "
									+ " t.desc_tipo_pedido as tipo_pedido, " 
									+ " CF.tipooperacao_cfop, " 
									+ " p.status_pedidovenda, "
									+ " it.produtoid, " 
									+ " it.ds_produto_pedidovenda_item, " 
									+ " it.qt_pedidovenda_item, "
									+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
									+ " IT.vl_total_pedidovenda_item, "
									+ " x1.NR_NOTA_PEDIDOVENDA,  " 
									+ " x1.DT_SAIDA_PEDIDOVENDA,  "
									+ " x1.STATUS_PEDIDOVENDA status_nota, " 
									+ " IMG.IMAGEM_PRODUTO "
									+ " from pedidovenda p "
									+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
									+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
									+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
									+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
									+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
									+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
									+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
									+ " inner join roteiro r on r.roteiroid = p.roteiroid "
									//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " left join ( " 
									+ " select " 
									+ " nota.NR_NOTA_PEDIDOVENDA, " 
									+ " nota.DT_SAIDA_PEDIDOVENDA,  "
									+ " nota.STATUS_PEDIDOVENDA, " 
									+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
									+ " IT_nota.produtoid "
									+ " from PEDIDOVENDA_ITEM IT_nota "
									+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
									+ " group by  "
									+ " nota.NR_NOTA_PEDIDOVENDA, " 
									+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
									+ " nota.STATUS_PEDIDOVENDA, "
									+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
									+ " IT_nota.produtoid "
									+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA' "
									+ " and p.TIPOPEDIDOID = 2 and p.pedidovendaid = ' " + pedido + " ' " 
									+ " ORDER BY P.PEDIDOVENDAID  ");
					List<Object[]> lista2 = query2.getResultList();
					
					for (Object[] row2 : lista2) {
						
							
						VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
						
						vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
						vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
						vendasEmGeralItem.setNomeproduto((String) row2[10] );
						vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
						vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
						vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
						vendasEmGeralItem.setNota((String) row2[14] );
						vendasEmGeralItem.setDatanota((Date) row2[15] );
						vendasEmGeralItem.setStatusnota((String) row2[16] );
						//vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
				}
					if (origem.equals("SIGE")) {
						//adicionando parte do SIge
						javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
							" select s.Cod_cli_for as cod_cliente, "
							+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
							+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
							+" 'VENDA' as tipooperacao,'Fechado'as statuspedido, v.Nome_cadastro nome_vendedor, 'SIGE' as origem, "
							+" cast(it.Cod_produto as integer)produto, pr.Desc_produto_est, cast(it.Qtde_pri as integer) qtde , it.Valor_unitario, it.Valor_liquido "
							
							+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
							+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
							+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
							+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
							+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
							+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
							+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
							+" inner join tbSaidasItem it on it.Chave_fato = p.Chave_fato "
							+" inner join tbproduto pr on pr.Cod_produto = it.Cod_produto "
							+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
							+" where s.Cod_tipo_mv in ('520','523','527') and p.Cod_tipo_mv in ('515')  "
							+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
							+" and s.Cod_vend_comp ="+ usuarioconectado()
							+" AND P.Num_docto ="+ pedido);
						
						List<Object[]> listasige = querySige.getResultList();
						
						if(listasige.size()>0){
						for (Object[] rowsige : listasige) {
							VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
							
							int spedido = (Integer) rowsige[2];
							int sproduto = (Integer) rowsige[11];
							int sqtde = (Integer) rowsige[13];
							
							vendasEmGeralItem.setPedido(new BigDecimal(spedido));
							vendasEmGeralItem.setCodigoproduto(new BigDecimal(sproduto));
							vendasEmGeralItem.setNomeproduto((String) rowsige[12] );
							vendasEmGeralItem.setQuantidadeproduto(new BigDecimal(sqtde));
							vendasEmGeralItem.setValorunitarioproduto((BigDecimal) rowsige[14] );
							vendasEmGeralItem.setValortotalproduto((BigDecimal) rowsige[15] );
							vendasEmGeralItem.setNota(null);
							vendasEmGeralItem.setDatanota(null);
							vendasEmGeralItem.setStatusnota(null);
							vendasEmGeralItem.setImagem(null );
							
							
							list.add(vendasEmGeralItem);
						}
							
						}
					}
					return list;
					
				}

				//pedidos de troca defeito
				public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2) {
						List<VendasEmGeral> list = new ArrayList<>();
							
							SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
							String dataFormatada = formato.format(data1);
							String dataFormatada2 = formato.format(data2);

							javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
									// "SELECT * FROM("
											" select " 
											+ " CI.CADCFTVID AS CLIENTE, " 
											+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
											+ " p.pedidovendaid pedido, " 
											+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
											+ " p.vl_totalprod_pedidovenda, " 
											+ " pg.nome_formapagto as prazo, "
											+ " t.desc_tipo_pedido as tipo_pedido, " 
											+ " CF.tipooperacao_cfop, " 
											+ " p.status_pedidovenda, "
											+ " v.NOME_CADCFTV nome_vendedor "
											+ " from pedidovenda p "
											+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
											+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
											+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
											+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
											+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
											+ " inner join roteiro r on r.roteiroid = p.roteiroid "
											//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
											//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
											+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
											+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
											+ " AND CF.tipooperacao_cfop <> 'VENDA' AND P.VENDEDOR1ID =  "+ usuarioconectado()
											+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
											+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
											+ " and p.TIPOPEDIDOID = 13 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
											+ " ORDER BY P.PEDIDOVENDAID  ");

							List<Object[]> lista = query.getResultList();
							
							

							for (Object[] row : lista) {
								VendasEmGeral vendasEmGeral = new VendasEmGeral();

								vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
								vendasEmGeral.setNomecliente((String) row[1] );
								vendasEmGeral.setPedido((BigDecimal) row[2] );
								vendasEmGeral.setDatapedido((Date) row[3] );
								vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
								vendasEmGeral.setPrazo((String) row[5] );
								vendasEmGeral.setTipopedido((String) row[6] );
								vendasEmGeral.setTipooperacaocfop((String) row[7] );
								vendasEmGeral.setStatuspedido((String) row[8] );
								vendasEmGeral.setNomevendedor((String) row[9] );
								
								
								
								list.add(vendasEmGeral);
							}

							return list;
						}
				//detalhes do pedido de amostra
				public List<VendasEmGeralItem> trocanegocioemgeralitem(BigDecimal pedido) {
					List<VendasEmGeralItem> list = new ArrayList<>();
					
					/*lista 2*/
					javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
							// "SELECT * FROM("
									" select " 
									+ " CI.CADCFTVID AS CLIENTE, " 
									+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
									+ " p.pedidovendaid pedido, " 
									+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
									+ " p.vl_totalprod_pedidovenda, " 
									+ " pg.nome_formapagto as prazo, "
									+ " t.desc_tipo_pedido as tipo_pedido, " 
									+ " CF.tipooperacao_cfop, " 
									+ " p.status_pedidovenda, "
									+ " it.produtoid, " 
									+ " it.ds_produto_pedidovenda_item, " 
									+ " it.qt_pedidovenda_item, "
									+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
									+ " IT.vl_total_pedidovenda_item, "
									+ " x1.NR_NOTA_PEDIDOVENDA,  " 
									+ " x1.DT_SAIDA_PEDIDOVENDA,  "
									+ " x1.STATUS_PEDIDOVENDA status_nota, " 
									+ " IMG.IMAGEM_PRODUTO "
									+ " from pedidovenda p "
									+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
									+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
									+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
									+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
									+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
									+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
									+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
									+ " inner join roteiro r on r.roteiroid = p.roteiroid "
									//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								    //+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " left join ( " 
									+ " select " 
									+ " nota.NR_NOTA_PEDIDOVENDA, " 
									+ " nota.DT_SAIDA_PEDIDOVENDA,  "
									+ " nota.STATUS_PEDIDOVENDA, " 
									+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
									+ " IT_nota.produtoid "
									+ " from PEDIDOVENDA_ITEM IT_nota "
									+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
									+ " group by  "
									+ " nota.NR_NOTA_PEDIDOVENDA, " 
									+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
									+ " nota.STATUS_PEDIDOVENDA, "
									+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
									+ " IT_nota.produtoid "
									+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA'  "
									+ " and p.TIPOPEDIDOID = 13 and p.pedidovendaid = ' " + pedido + " ' " 
									+ " ORDER BY P.PEDIDOVENDAID  ");
					List<Object[]> lista2 = query2.getResultList();
					
					for (Object[] row2 : lista2) {
						
							
						VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
						
						vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
						vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
						vendasEmGeralItem.setNomeproduto((String) row2[10] );
						vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
						vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
						vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
						vendasEmGeralItem.setNota((String) row2[14] );
						vendasEmGeralItem.setDatanota((Date) row2[15] );
						vendasEmGeralItem.setStatusnota((String) row2[16] );
						//vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
}
				
				//pedidos de amostra
				public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2) {
						List<VendasEmGeral> list = new ArrayList<>();
						
						SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
						String dataFormatada = formato.format(data1);
						String dataFormatada2 = formato.format(data2);

						javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
								// "SELECT * FROM("
										" select " 
										+ " CI.CADCFTVID AS CLIENTE, " 
										+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
										+ " p.pedidovendaid pedido, " 
										+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
										+ " p.vl_totalprod_pedidovenda, " 
										+ " pg.nome_formapagto as prazo, "
										+ " t.desc_tipo_pedido as tipo_pedido, " 
										+ " CF.tipooperacao_cfop, " 
										+ " p.status_pedidovenda, "
										+ " v.NOME_CADCFTV nome_vendedor "
										+ " from pedidovenda p "
										+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
										+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
										+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
										+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
										+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
										+ " inner join roteiro r on r.roteiroid = p.roteiroid "
										//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
										//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
										+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
										+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
										+ " AND CF.tipooperacao_cfop <> 'VENDA' AND P.VENDEDOR1ID =  "+ usuarioconectado() 
										+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
										+ " and p.TIPOPEDIDOID = 14 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
										+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
										+ " ORDER BY P.PEDIDOVENDAID  ");

						List<Object[]> lista = query.getResultList();
						
						

						for (Object[] row : lista) {
							VendasEmGeral vendasEmGeral = new VendasEmGeral();

							vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
							vendasEmGeral.setNomecliente((String) row[1] );
							vendasEmGeral.setPedido((BigDecimal) row[2] );
							vendasEmGeral.setDatapedido((Date) row[3] );
							vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
							vendasEmGeral.setPrazo((String) row[5] );
							vendasEmGeral.setTipopedido((String) row[6] );
							vendasEmGeral.setTipooperacaocfop((String) row[7] );
							vendasEmGeral.setStatuspedido((String) row[8] );
							vendasEmGeral.setNomevendedor((String) row[9] );
							
							
							
							list.add(vendasEmGeral);
						}

						return list;
					}
				
				//detalhes do pedido de amostra
					public List<VendasEmGeralItem> brindeemgeralitem(BigDecimal pedido) {
						List<VendasEmGeralItem> list = new ArrayList<>();
						
						/*lista 2*/
						javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
								// "SELECT * FROM("
										" select " 
										+ " CI.CADCFTVID AS CLIENTE, " 
										+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
										+ " p.pedidovendaid pedido, " 
										+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
										+ " p.vl_totalprod_pedidovenda, " 
										+ " pg.nome_formapagto as prazo, "
										+ " t.desc_tipo_pedido as tipo_pedido, " 
										+ " CF.tipooperacao_cfop, " 
										+ " p.status_pedidovenda, "
										+ " it.produtoid, " 
										+ " it.ds_produto_pedidovenda_item, " 
										+ " it.qt_pedidovenda_item, "
										+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
										+ " IT.vl_total_pedidovenda_item, "
										+ " x1.NR_NOTA_PEDIDOVENDA,  " 
										+ " x1.DT_SAIDA_PEDIDOVENDA,  "
										+ " x1.STATUS_PEDIDOVENDA status_nota, " 
										+ " IMG.IMAGEM_PRODUTO "
										+ " from pedidovenda p "
										+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
										+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
										+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
										+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
										+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
										+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
										+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
										+ " inner join roteiro r on r.roteiroid = p.roteiroid "
										//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
										//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
										+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
										+ " left join ( " 
										+ " select " 
										+ " nota.NR_NOTA_PEDIDOVENDA, " 
										+ " nota.DT_SAIDA_PEDIDOVENDA,  "
										+ " nota.STATUS_PEDIDOVENDA, " 
										+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
										+ " IT_nota.produtoid "
										+ " from PEDIDOVENDA_ITEM IT_nota "
										+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
										+ " group by  "
										+ " nota.NR_NOTA_PEDIDOVENDA, " 
										+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
										+ " nota.STATUS_PEDIDOVENDA, "
										+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
										+ " IT_nota.produtoid "
										+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

										+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
										+ " AND CF.tipooperacao_cfop <> 'VENDA'  "
										+ " and p.TIPOPEDIDOID = 14 and p.pedidovendaid = ' " + pedido + " ' " 
										+ " ORDER BY P.PEDIDOVENDAID  ");
						List<Object[]> lista2 = query2.getResultList();
						
						for (Object[] row2 : lista2) {
							
								
							VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
							
							vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
							vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
							vendasEmGeralItem.setNomeproduto((String) row2[10] );
							vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
							vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
							vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
							vendasEmGeralItem.setNota((String) row2[14] );
							vendasEmGeralItem.setDatanota((Date) row2[15] );
							vendasEmGeralItem.setStatusnota((String) row2[16] );
							//vendasEmGeralItem.setImagem((Blob) row2[17] );
							
							list.add(vendasEmGeralItem);
						}
						return list;
						
					}
					
	//pedidos de bonificação
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
				List<VendasEmGeral> list = new ArrayList<>();
				
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				String dataFormatada = formato.format(data1);
				String dataFormatada2 = formato.format(data2);

				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " v.NOME_CADCFTV nome_vendedor, 'SEVEN' as origem "
								+ " from pedidovenda p "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
								+ " and p.TIPOPEDIDOID = 3  AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " ORDER BY P.PEDIDOVENDAID  ");

				List<Object[]> lista = query.getResultList();
				
				

				for (Object[] row : lista) {
					VendasEmGeral vendasEmGeral = new VendasEmGeral();

					vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
					vendasEmGeral.setNomecliente((String) row[1] );
					vendasEmGeral.setPedido((BigDecimal) row[2] );
					vendasEmGeral.setDatapedido((Date) row[3] );
					vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
					vendasEmGeral.setPrazo((String) row[5] );
					vendasEmGeral.setTipopedido((String) row[6] );
					vendasEmGeral.setTipooperacaocfop((String) row[7] );
					vendasEmGeral.setStatuspedido((String) row[8] );
					vendasEmGeral.setNomevendedor((String) row[9] );
					vendasEmGeral.setOrigem((String) row[10] );
					
					
					list.add(vendasEmGeral);
				}
				
				//adicionando parte do SIge
				
				SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
				String SdataFormatada = formato2.format(data1);
				String SdataFormatada2 = formato2.format(data2);
				
				javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
					" select s.Cod_cli_for as cod_cliente, "
					+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
					+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
					+" 'AMOSTRA' as tipooperacao,'Fechado'as statuspedido,v.Nome_cadastro nome_vendedor, 'SIGE' as origem "
					
					+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
					+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
					+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
					+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
					+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
					+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
					+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
					+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
					+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('512')  "
					+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
					+" and s.Cod_vend_comp ="+ usuarioconectado()
					+" and s.Cod_cli_for between ' " + cliente1 + " ' and ' " + cliente2 + "' "
					+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
				
				List<Object[]> listasige = querySige.getResultList();
				if(listasige.size()>0){
				for (Object[] rowsige : listasige) {
					VendasEmGeral vendasEmGeral = new VendasEmGeral();
					
					int scliente = (Integer) rowsige[0];
					int spedido = (Integer) rowsige[2];

					vendasEmGeral.setCodigocliente(new BigDecimal(scliente));
					vendasEmGeral.setNomecliente((String) rowsige[1] );
					vendasEmGeral.setPedido(new BigDecimal(spedido) );
					vendasEmGeral.setDatapedido((Date) rowsige[3] );
					vendasEmGeral.setValortotalpedido((BigDecimal) rowsige[4] );
					vendasEmGeral.setPrazo((String) rowsige[5] );
					vendasEmGeral.setTipopedido((String) rowsige[6] );
					vendasEmGeral.setTipooperacaocfop((String) rowsige[7] );
					vendasEmGeral.setStatuspedido((String) rowsige[8] );
					vendasEmGeral.setNomevendedor((String) rowsige[9] );
					vendasEmGeral.setOrigem((String) rowsige[10] );
					
					list.add(vendasEmGeral);			
					}
				}
				//fim da parte do sige

				return list;
			}
		
	//detalhes do pedido de bonificação
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido, String origem) {
	List<VendasEmGeralItem> list = new ArrayList<>();
		if (origem.equals("SEVEN")) {
				javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " it.produtoid, " 
								+ " it.ds_produto_pedidovenda_item, " 
								+ " it.qt_pedidovenda_item, "
								+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
								+ " IT.vl_total_pedidovenda_item, "
								+ " x1.NR_NOTA_PEDIDOVENDA,  " 
								+ " x1.DT_SAIDA_PEDIDOVENDA,  "
								+ " x1.STATUS_PEDIDOVENDA status_nota, " 
								+ " IMG.IMAGEM_PRODUTO "
								+ " from pedidovenda p "
								+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
								+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " left join ( " 
								+ " select " 
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  "
								+ " nota.STATUS_PEDIDOVENDA, " 
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
								+ " IT_nota.produtoid "
								+ " from PEDIDOVENDA_ITEM IT_nota "
								+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
								+ " group by  "
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
								+ " nota.STATUS_PEDIDOVENDA, "
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
								+ " IT_nota.produtoid "
								+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " and p.TIPOPEDIDOID = 3 and p.pedidovendaid = ' " + pedido + " ' " 
								+ " ORDER BY P.PEDIDOVENDAID  ");
				List<Object[]> lista2 = query2.getResultList();
				
				for (Object[] row2 : lista2) {
					
						
					VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
					
					vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
					vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
					vendasEmGeralItem.setNomeproduto((String) row2[10] );
					vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
					vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
					vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
					vendasEmGeralItem.setNota((String) row2[14] );
					vendasEmGeralItem.setDatanota((Date) row2[15] );
					vendasEmGeralItem.setStatusnota((String) row2[16] );
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				}
				if (origem.equals("SIGE")) {
					//adicionando parte do SIge
					javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
						" select s.Cod_cli_for as cod_cliente, "
						+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
						+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
						+" 'VENDA' as tipooperacao,'Fechado'as statuspedido, v.Nome_cadastro nome_vendedor, 'SIGE' as origem, "
						+" cast(it.Cod_produto as integer)produto, pr.Desc_produto_est, cast(it.Qtde_pri as integer) qtde , it.Valor_unitario, it.Valor_liquido "
						
						+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
						+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
						+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
						+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
						+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
						+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
						+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
						+" inner join tbSaidasItem it on it.Chave_fato = p.Chave_fato "
						+" inner join tbproduto pr on pr.Cod_produto = it.Cod_produto "
						+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
						+" where s.Cod_tipo_mv in ('520','523','527') and p.Cod_tipo_mv in ('512')  "
						+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
						+" and s.Cod_vend_comp ="+ usuarioconectado()
						+" AND P.Num_docto ="+ pedido);
					
					List<Object[]> listasige = querySige.getResultList();
					
					if(listasige.size()>0){
					for (Object[] rowsige : listasige) {
						VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
						
						int spedido = (Integer) rowsige[2];
						int sproduto = (Integer) rowsige[11];
						int sqtde = (Integer) rowsige[13];
						
						vendasEmGeralItem.setPedido(new BigDecimal(spedido));
						vendasEmGeralItem.setCodigoproduto(new BigDecimal(sproduto));
						vendasEmGeralItem.setNomeproduto((String) rowsige[12] );
						vendasEmGeralItem.setQuantidadeproduto(new BigDecimal(sqtde));
						vendasEmGeralItem.setValorunitarioproduto((BigDecimal) rowsige[14] );
						vendasEmGeralItem.setValortotalproduto((BigDecimal) rowsige[15] );
						vendasEmGeralItem.setNota(null);
						vendasEmGeralItem.setDatanota(null);
						vendasEmGeralItem.setStatusnota(null);
						vendasEmGeralItem.setImagem(null );
						
						
						list.add(vendasEmGeralItem);
					}
						
					}
				}
				return list;
				
			}
	
	//pedidos de bonificação
	public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
				List<VendasEmGeral> list = new ArrayList<>();
				
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				String dataFormatada = formato.format(data1);
				String dataFormatada2 = formato.format(data2);

				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " v.NOME_CADCFTV nome_vendedor "
								+ " from pedidovenda p "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
								+ " and p.TIPOPEDIDOID = 16  AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " ORDER BY P.PEDIDOVENDAID  ");

				List<Object[]> lista = query.getResultList();
				
				

				for (Object[] row : lista) {
					VendasEmGeral vendasEmGeral = new VendasEmGeral();

					vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
					vendasEmGeral.setNomecliente((String) row[1] );
					vendasEmGeral.setPedido((BigDecimal) row[2] );
					vendasEmGeral.setDatapedido((Date) row[3] );
					vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
					vendasEmGeral.setPrazo((String) row[5] );
					vendasEmGeral.setTipopedido((String) row[6] );
					vendasEmGeral.setTipooperacaocfop((String) row[7] );
					vendasEmGeral.setStatuspedido((String) row[8] );
					vendasEmGeral.setNomevendedor((String) row[9] );
					
					
					
					list.add(vendasEmGeral);
				}

				return list;
			}
		
	//detalhes do pedido de bonificação
	public List<VendasEmGeralItem> bonificacaoexpositoremgeralitem(BigDecimal pedido) {
	List<VendasEmGeralItem> list = new ArrayList<>();
				/*lista 2*/
				javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " it.produtoid, " 
								+ " it.ds_produto_pedidovenda_item, " 
								+ " it.qt_pedidovenda_item, "
								+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
								+ " IT.vl_total_pedidovenda_item, "
								+ " x1.NR_NOTA_PEDIDOVENDA,  " 
								+ " x1.DT_SAIDA_PEDIDOVENDA,  "
								+ " x1.STATUS_PEDIDOVENDA status_nota, " 
								+ " IMG.IMAGEM_PRODUTO "
								+ " from pedidovenda p "
								+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
								+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " left join ( " 
								+ " select " 
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  "
								+ " nota.STATUS_PEDIDOVENDA, " 
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
								+ " IT_nota.produtoid "
								+ " from PEDIDOVENDA_ITEM IT_nota "
								+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
								+ " group by  "
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
								+ " nota.STATUS_PEDIDOVENDA, "
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
								+ " IT_nota.produtoid "
								+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " and p.TIPOPEDIDOID = 16 and p.pedidovendaid = ' " + pedido + " ' " 
								+ " ORDER BY P.PEDIDOVENDAID  ");
				List<Object[]> lista2 = query2.getResultList();
				
				for (Object[] row2 : lista2) {
					
						
					VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
					
					vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
					vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
					vendasEmGeralItem.setNomeproduto((String) row2[10] );
					vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
					vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
					vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
					vendasEmGeralItem.setNota((String) row2[14] );
					vendasEmGeralItem.setDatanota((Date) row2[15] );
					vendasEmGeralItem.setStatusnota((String) row2[16] );
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

	//pedidos de amostra
		public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
				List<VendasEmGeral> list = new ArrayList<>();
				
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				String dataFormatada = formato.format(data1);
				String dataFormatada2 = formato.format(data2);

				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " v.NOME_CADCFTV nome_vendedor, 'SEVEN' as origem "
								+ " from pedidovenda p "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
								+ " and p.TIPOPEDIDOID = 5  AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " ORDER BY P.PEDIDOVENDAID  ");

				List<Object[]> lista = query.getResultList();
				
				

				for (Object[] row : lista) {
					VendasEmGeral vendasEmGeral = new VendasEmGeral();

					vendasEmGeral.setCodigocliente((BigDecimal) row[0]);
					vendasEmGeral.setNomecliente((String) row[1] );
					vendasEmGeral.setPedido((BigDecimal) row[2] );
					vendasEmGeral.setDatapedido((Date) row[3] );
					vendasEmGeral.setValortotalpedido((BigDecimal) row[4] );
					vendasEmGeral.setPrazo((String) row[5] );
					vendasEmGeral.setTipopedido((String) row[6] );
					vendasEmGeral.setTipooperacaocfop((String) row[7] );
					vendasEmGeral.setStatuspedido((String) row[8] );
					vendasEmGeral.setNomevendedor((String) row[9] );
					vendasEmGeral.setOrigem((String) row[10] );
					
					
					list.add(vendasEmGeral);
				}
				
				//adicionando parte do SIge
				
				SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
				String SdataFormatada = formato2.format(data1);
				String SdataFormatada2 = formato2.format(data2);
				
				javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
					" select s.Cod_cli_for as cod_cliente, "
					+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
					+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
					+" 'AMOSTRA' as tipooperacao,'Fechado'as statuspedido,v.Nome_cadastro nome_vendedor, 'SIGE' as origem "
					
					+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
					+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
					+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
					+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
					+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
					+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
					+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
					+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
					+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('517')  "
					+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
					+" and s.Cod_vend_comp ="+ usuarioconectado()
					+" and s.Cod_cli_for between ' " + cliente1 + " ' and ' " + cliente2 + "' "
					+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
				
				List<Object[]> listasige = querySige.getResultList();
				if(listasige.size()>0){
				for (Object[] rowsige : listasige) {
					VendasEmGeral vendasEmGeral = new VendasEmGeral();
					
					int scliente = (Integer) rowsige[0];
					int spedido = (Integer) rowsige[2];

					vendasEmGeral.setCodigocliente(new BigDecimal(scliente));
					vendasEmGeral.setNomecliente((String) rowsige[1] );
					vendasEmGeral.setPedido(new BigDecimal(spedido) );
					vendasEmGeral.setDatapedido((Date) rowsige[3] );
					vendasEmGeral.setValortotalpedido((BigDecimal) rowsige[4] );
					vendasEmGeral.setPrazo((String) rowsige[5] );
					vendasEmGeral.setTipopedido((String) rowsige[6] );
					vendasEmGeral.setTipooperacaocfop((String) rowsige[7] );
					vendasEmGeral.setStatuspedido((String) rowsige[8] );
					vendasEmGeral.setNomevendedor((String) rowsige[9] );
					vendasEmGeral.setOrigem((String) rowsige[10] );
					
					list.add(vendasEmGeral);			
					}
				}
				//fim da parte do sige

				return list;
			}
		
		//detalhes do pedido de amostra
			public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido, String origem) {
				List<VendasEmGeralItem> list = new ArrayList<>();
				
				if (origem.equals("SEVEN")) {
				javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(
						// "SELECT * FROM("
								" select " 
								+ " CI.CADCFTVID AS CLIENTE, " 
								+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
								+ " p.pedidovendaid pedido, " 
								+ " P.DT_PEDIDOVENDA AS DATAPEDIDO, "
								+ " p.vl_totalprod_pedidovenda, " 
								+ " pg.nome_formapagto as prazo, "
								+ " t.desc_tipo_pedido as tipo_pedido, " 
								+ " CF.tipooperacao_cfop, " 
								+ " p.status_pedidovenda, "
								+ " it.produtoid, " 
								+ " it.ds_produto_pedidovenda_item, " 
								+ " it.qt_pedidovenda_item, "
								+ " IT.VL_UNIT_PEDIDOVENDA_ITEM, " 
								+ " IT.vl_total_pedidovenda_item, "
								+ " x1.NR_NOTA_PEDIDOVENDA,  " 
								+ " x1.DT_SAIDA_PEDIDOVENDA,  "
								+ " x1.STATUS_PEDIDOVENDA status_nota, " 
								+ " IMG.IMAGEM_PRODUTO "
								+ " from pedidovenda p "
								+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAID= P.PEDIDOVENDAID "
								+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " left join ( " 
								+ " select " 
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  "
								+ " nota.STATUS_PEDIDOVENDA, " 
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM,  " 
								+ " IT_nota.produtoid "
								+ " from PEDIDOVENDA_ITEM IT_nota "
								+ " inner join pedidovenda nota on nota.pedidovendaid = it_nota.pedidovendaid " 
								+ " group by  "
								+ " nota.NR_NOTA_PEDIDOVENDA, " 
								+ " nota.DT_SAIDA_PEDIDOVENDA,  " 
								+ " nota.STATUS_PEDIDOVENDA, "
								+ " it_nota.ORIGEM_PEDIDOVENDA_ITEM , " 
								+ " IT_nota.produtoid "
								+ " ) x1 on x1.ORIGEM_PEDIDOVENDA_ITEM = IT.PEDIDOVENDAITEMID  and x1.produtoid = it.produtoid "

								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " 
								+ " AND P.VENDEDOR1ID =  "+ usuarioconectado()
								+ " and p.TIPOPEDIDOID = 5 and p.pedidovendaid = ' " + pedido + " ' " 
								+ " ORDER BY P.PEDIDOVENDAID  ");
				List<Object[]> lista2 = query2.getResultList();
				
				for (Object[] row2 : lista2) {
					
						
					VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
					
					vendasEmGeralItem.setPedido((BigDecimal) row2[2] );
					vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[9] );
					vendasEmGeralItem.setNomeproduto((String) row2[10] );
					vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
					vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
					vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
					vendasEmGeralItem.setNota((String) row2[14] );
					vendasEmGeralItem.setDatanota((Date) row2[15] );
					vendasEmGeralItem.setStatusnota((String) row2[16] );
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				
			}
			if (origem.equals("SIGE")) {
				//adicionando parte do SIge
				javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
					" select s.Cod_cli_for as cod_cliente, "
					+" g.Nome_cadastro ,p.Num_docto pedido ,p.Data_v1 dataPedido , "
					+" cast(s.Valor_liquido as money) as valor_venda ,cd.Desc_cond_pgto,'' tipopedido, "
					+" 'VENDA' as tipooperacao,'Fechado'as statuspedido, v.Nome_cadastro nome_vendedor, 'SIGE' as origem, "
					+" cast(it.Cod_produto as integer)produto, pr.Desc_produto_est, cast(it.Qtde_pri as integer) qtde , it.Valor_unitario, it.Valor_liquido "
					
					+" from tbsaidas s  left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un   "
					+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
					+" inner join tbCadastroGeral v on v.Cod_cadastro = s.Cod_vend_comp "
					+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
					+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
					+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
					+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
					+" inner join tbSaidasItem it on it.Chave_fato = p.Chave_fato "
					+" inner join tbproduto pr on pr.Cod_produto = it.Cod_produto "
					+" inner join tbCondPgto cd on cd.Cod_cond_pgto = p.Cod_cond_pgto "
					+" where s.Cod_tipo_mv in ('520','523','527') and p.Cod_tipo_mv in ('517')  "
					+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
					+" and s.Cod_vend_comp ="+ usuarioconectado()
					+" AND P.Num_docto ="+ pedido);
				
				List<Object[]> listasige = querySige.getResultList();
				
				if(listasige.size()>0){
				for (Object[] rowsige : listasige) {
					VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();
					
					int spedido = (Integer) rowsige[2];
					int sproduto = (Integer) rowsige[11];
					int sqtde = (Integer) rowsige[13];
					
					vendasEmGeralItem.setPedido(new BigDecimal(spedido));
					vendasEmGeralItem.setCodigoproduto(new BigDecimal(sproduto));
					vendasEmGeralItem.setNomeproduto((String) rowsige[12] );
					vendasEmGeralItem.setQuantidadeproduto(new BigDecimal(sqtde));
					vendasEmGeralItem.setValorunitarioproduto((BigDecimal) rowsige[14] );
					vendasEmGeralItem.setValortotalproduto((BigDecimal) rowsige[15] );
					vendasEmGeralItem.setNota(null);
					vendasEmGeralItem.setDatanota(null);
					vendasEmGeralItem.setStatusnota(null);
					vendasEmGeralItem.setImagem(null );
					
					
					list.add(vendasEmGeralItem);
				}
					
				}
			}
				return list;
				
			}
			
	public List<ProdutoVenda> produtos_venda(Date data1,Date data2, String produtos, String cliente){
		List<ProdutoVenda> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql = ""
				+ " select  "
				+ " p.nr_nota_pedidovenda  nota, "
				+ " p.dt_faturamento_pedidovenda  datanota, "
				+ " c.cadcftvid cliente, "
				+ " c.nome_cadcftv nomecliente, "
				//+ " p.vendedor1id vendedor,  "
				//+ " v.nome_cadcftv nomevendedor, "
				+ " it.produtoid produto, "
				+ " pr.nome_produto , "
				+ " it.qt_pedidovenda_item qtde "
				+ " from pedidovenda p  "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid  "
				+ " inner join produto pr on pr.produtoid = it.produtoid  "
				+ " inner join cadcftv c on c.cadcftvid = p.cadcftvid "
				+ " inner join cadcftv v on v.cadcftvid = p.vendedor1id  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('FATURADO')  "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " and p.origem_pedidovenda <> 'SIMETRICA' "
				+ " and p.dt_faturamento_pedidovenda between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " '"
				+ " and (it.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.cadcftvid = "+cliente+" or 1 = "+cliente+" ) "
				+ " and v.cadcftvid ="+ usuarioconectado()	;
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ProdutoVenda v = new ProdutoVenda();
			
			v.setNota((String)row[0]);
			v.setDatanota((Date)row[1]);
			v.setCliente((BigDecimal)row[2]);
			v.setNomecliente((String)row[3]);
			v.setProduto((BigDecimal)row[4]);
			v.setNomeproduto((String)row[5]);
			v.setQtde((BigDecimal)row[6]);
			
			list.add(v);
		}
		
		return list;
	}
	
	
	
	//clientes cadastrados no periodo
	public List<ClientesNovos> clientesnovos(Date data1,Date data2,String vendedor1, String vendedor2) {
		List<ClientesNovos> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " cl.VENDEDORID1 vendedor, " 
						+ " v.NOME_CADCFTV nome_vendedor, "
						+ " c.cadcftvid cliente, " 
						+ " c.NOME_CADCFTV nome_cliente, " 
						+ " c.APELIDO_CADCFTV nome_fantasia, " 
						+ " c.CNPJCPF_CADCFTV cnpj_cpf, " 
						+ " c.DATACREATE_CADCFTV data_cadastro " 
						
						+ " from cadcftv c "
						+ " inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = cl.VENDEDORID1 "
						+ " INNER JOIN cadcftv V ON V.cadcftvID = V2.CADCFTVID "
						
						+ " WHERE  c.ATIVO_CADCFTV = 'SIM' "
						+ " and c.DATACREATE_CADCFTV between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " AND V.CADCFTVID =  "+ usuarioconectado()
						+ " order by cl.VENDEDORID1,c.cadcftvid ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			ClientesNovos clientesNovos = new ClientesNovos();

			clientesNovos.setCodigovendedor((BigDecimal) row[0]);
			clientesNovos.setNomevendedor((String) row[1]);
			clientesNovos.setCodigocliente((BigDecimal) row[2]);
			clientesNovos.setNomecliente((String) row[3]);
			clientesNovos.setNomefantasia((String) row[4]);
			clientesNovos.setCpfcnpj((String) row[5]);
			clientesNovos.setDatacadastro((Date) row[6] );
			
			list.add(clientesNovos);
		}

		return list;
	}

	public List<Cliente> consultacliente(String palavra) {
		List<Cliente> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " V.CADCFTVID cliente, " 
						+ " v.NOME_CADCFTV nome_cliente, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "	
						+ " INNER JOIN VENDEDOR V3 ON V3.CADCFTVID = V2.VENDEDORID1 "
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or CAST(v.cadcftvid AS TEXT) like '%"+palavra+"%' or v.CNPJCPF_CADCFTV like '"+palavra+"%' ) "
						+ " AND V3.CADCFTVID =  "+ usuarioconectado()
						+ " order by v.cadcftvid ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			Cliente cliente = new Cliente();

			cliente.setCodigocliente((BigDecimal) row[0]);
			cliente.setNomecliente((String) row[1]);
			cliente.setCpfcnpj((String) row[2]);
			
			
			list.add(cliente);
		}
		return list;
}
	public List<Vendedor> consultavendedor() {
		List<Vendedor> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " V.CADCFTVID vendedor, " 
						+ " v.NOME_CADCFTV nome_vendedor, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = V.CADCFTVID and V2.GESTORID = G.GESTORID "						
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' "
						+ " order by v.cadcftvid ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			Vendedor vendedor = new Vendedor();

			vendedor.setCodigovendedor((BigDecimal) row[0]);
			vendedor.setNomevendedor((String) row[1]);
			vendedor.setCpfcnpj((String) row[2]);
			
			
			list.add(vendedor);
		}
		return list;
	}
	
	public VendasEmGeralItem consultaitem(BigDecimal produto) {
		VendasEmGeralItem vendasEmGeralItem = new VendasEmGeralItem();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select " 
						+ " '1' pedido, " 
						+ " '01/01/2019' AS DATAPEDIDO, "
						+ " it.produtoid, " 
						+ " '' ds_produto_pedidovenda_item, " 
						+ " 0 qt_pedidovenda_item, "
						+ " 0 VL_UNIT_PEDIDOVENDA_ITEM, " 
						+ " 0 vl_total_pedidovenda_item, "
						+ " '0' NR_NOTA_PEDIDOVENDA,  " 
						+ " '01/01/2019' DT_SAIDA_PEDIDOVENDA,  "
						+ " '0'  status_nota, " 
						+ " IMG.IMAGEM_PRODUTO "  
						+ " from produto it "	
						+ " LEFT JOIN PRODUTO_IMAGEM IMG ON IMG.PRODUTOID = IT.PRODUTOID "
						+ " WHERE it.produtoid = " +produto);

		List<Object[]> lista = query.getResultList();

		for (Object[] row2 : lista) {
			
			vendasEmGeralItem.setPedido((BigDecimal) row2[0] );
			vendasEmGeralItem.setCodigoproduto((BigDecimal) row2[2] );
			vendasEmGeralItem.setNomeproduto((String) row2[3] );
			vendasEmGeralItem.setQuantidadeproduto((BigDecimal) row2[11] );
			vendasEmGeralItem.setValorunitarioproduto((BigDecimal) row2[12] );
			vendasEmGeralItem.setValortotalproduto((BigDecimal) row2[13] );
			vendasEmGeralItem.setNota((String) row2[14] );
			vendasEmGeralItem.setDatanota((Date) row2[15] );
			vendasEmGeralItem.setStatusnota((String) row2[16] );
			//vendasEmGeralItem.setImagem((Blob) row2[17] );
			
		}
		return vendasEmGeralItem;
	}
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2,String ano, String mes, Date data_grafico, Date data_grafico2 ) {
		List<MetaVenda> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data_grafico);
		String dataFormatada2 = formato.format(data_grafico2);

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT TIPO,MES,ANO,VALOR, CASE WHEN NOME_REGIAO is null THEN 'VAZIO' ELSE NOME_REGIAO END AS NOME_REGIAO FROM ( "
				+ "	select  "
				+ "	'META' AS TIPO, "
				+ "	MV.MES_METAVENDEDOR MES, "
				+ "	MV.ANO_METAVENDEDOR ANO, "
				+ "	SUM(MV.VALOR_METAVENDEDOR)VALOR, "
				+ "	RE.NOME_REGIAO "
				+ "	from meta_vendedor mv "
				+ "	INNER JOIN CADCFTV V ON V.CADCFTVID = mv.CADCFTVID "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				
				+ "	WHERE mv.MES_METAVENDEDOR = "+mes//TO_NUMBER(TO_CHAR(SYSDATE,'MM')) "
				+ "	and mv.ANO_METAVENDEDOR = "+ano//TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+ "	and v.ATIVO_CADCFTV = 'SIM' "
				+ " AND V.CADCFTVID =  "+ usuarioconectado()
				+ "	GROUP BY "
				+ "	MV.MES_METAVENDEDOR, "
				+ "	MV.ANO_METAVENDEDOR, "
				+ "	RE.NOME_REGIAO  "
				+ "	UNION ALL  "
				+ "	SELECT "
				+ "	'VENDA' TIPO, "
				+ "	"+mes+" AS MES, "
				+ "	"+ano+" AS ANO, "
				+ "	SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALOR, "
				+ "	RE.NOME_REGIAO "
				+ "	FROM PEDIDOVENDA EN  "
				+ "	INNER JOIN VENDEDOR V ON V.CADCFTVID = EN.VENDEDOR1ID  "
				+ "	INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID  "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				+ "	WHERE CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ "	AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
				+ " AND EN.VENDEDOR1ID =  "+ usuarioconectado()
				//+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = TO_CHAR(CURRENT_DATE,'MM') "
				//+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = TO_CHAR(CURRENT_DATE,'YYYY') "
				+ " and EN.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ "	GROUP BY  "
				+ "	RE.NOME_REGIAO "
				+ "	)X "
				+ "	ORDER BY  "
				+ "	X.ANO,X.MES,X.NOME_REGIAO,X.TIPO ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			MetaVenda metavenda = new MetaVenda();

			metavenda.setTipo((String) row[0]);
			metavenda.setMes((BigDecimal) row[1]);
			metavenda.setAno((BigDecimal) row[2]);
			metavenda.setValor((BigDecimal) row[3]);
			metavenda.setRegiao((String) row[4]);
			
			list.add(metavenda);
		}
		return list;
	}
	
}
