package br.com.dwbidiretor.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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
import org.jfree.data.time.Day;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.VendedorMetaVenda;
import br.com.dwbidiretor.classe.painel.Cliente_Ano;
import br.com.dwbidiretor.classe.painel.Diretor_01;
import br.com.dwbidiretor.classe.painel.Qtde_Ano;
import br.com.dwbidiretor.classe.painel.Qtde_Mes;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.classe.painel.Venda_Subgrupo;
import br.com.dwbidiretor.classe.painel.Vendedor_Ano;
import br.com.dwbidiretor.classe.painel.Vendedor_Mes;
import br.com.dwbidiretor.dao.DAOGenerico;
import br.com.dwbidiretor.fabrica.EntityManagerProducerSige.Corporativo;

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
	
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2){
		List<MixProduto> list = new ArrayList<>();
		
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
				+ " MIXVL.VLTOTAL  "
				+ " from produto pr "
				+ " "
				+ " inner join( "
				+ " SELECT DISTINCT "
				+ " it.produtoid                          "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " )mixpr on mixpr.produtoid = pr.produtoid  "
				+ " "
				+ " "
				+ " LEFT JOIN( "
				+ " SELECT MIXQTDE.produtoid, "
				+ " SUM(MIXQTDE.QTDE2018) QTDE2018, "
				+ " SUM(MIXQTDE.QTDE2019) QTDE2019, "
				+ " SUM(MIXQTDE.QTDE2020) QTDE2020, "
				+ " SUM(MIXQTDE.QTDE2021) QTDE2021, "
				+ " SUM(MIXQTDE.QTDE2018)+SUM(MIXQTDE.QTDE2019)+ SUM(MIXQTDE.QTDE2020)+SUM(MIXQTDE.QTDE2021) QTDETOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2018' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2018, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2019' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2019, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2020' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2020, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2021' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2021 "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " )MIXQTDE GROUP BY MIXQTDE.produtoid "
				+ " )MIXQTDE ON MIXQTDE.produtoid = PR.PRODUTOID "
				+ " "
				+ " LEFT JOIN( "
				+ " SELECT MIXVL.produtoid, "
				+ " SUM(MIXVL.VL2018) VL2018, "
				+ " SUM(MIXVL.VL2019) VL2019, "
				+ " SUM(MIXVL.VL2020) VL2020, "
				+ " SUM(MIXVL.VL2021) VL2021, "
				+ " SUM(MIXVL.VL2018)+SUM(MIXVL.VL2019)+ SUM(MIXVL.VL2020)+SUM(MIXVL.VL2021) VLTOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2018' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2018, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2019' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2019, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2020' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2020, "
				+ " CASE WHEN TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '2021' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2021 "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			
			
			list.add(mix);
		}
				
		return list;
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
				+ " nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0) totalgeral, "
				+ " mix.mixqtde, "
				+ " mixmedio.qtmixmedio, "
				+ " round((nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0))/mixmedio.qtvendas,2) ticketmedio,  "
				+ " freq.vendas, "
				+ " freq.primeira, "
				+ " freq.ultima, "
				+ " freq.frequencia "
				
				+ " FROM CADCFTV C "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = C.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = CL.VENDEDORID1 "
				+ "  INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
				+ " LEFT join (SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V "
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
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2018') "
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
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2019') "
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
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ "  "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2020') "
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
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as janeiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end as fevereiro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '03' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as marco, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '04' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as abril, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '05' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as maio, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '06' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as junho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '07' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as julho, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '08' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as agosto, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '09' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as setembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '10' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as outubro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '11' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as novembro, "
				+ " case when TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')= '12' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as dezembro "
				+ " "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2021') "
				+ " "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
				+ " ) VENDAS2021 ON VENDAS2021.CLIENTE = C.CADCFTVID "
				
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
				+ " where p.status_pedidovenda in ('FATURADO') "
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
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' )x "
				+ " group by x.CADCFTVID,x.pedidovendaid)y "
				+ " group by y.CADCFTVID "
				+ " ) mixmedio on mixmedio.CADCFTVID = c.CADCFTVID "
				
				+ " left join( "
				+ " select "
				+ " p.CADCFTVID, "
				+ " count(p.CADCFTVID) vendas, "
				+ " min(p.DT_FATURAMENTO_PEDIDOVENDA) primeira, "
				+ " max(p.DT_FATURAMENTO_PEDIDOVENDA) ultima, "
				+ " round((max(p.DT_FATURAMENTO_PEDIDOVENDA) - min(p.DT_FATURAMENTO_PEDIDOVENDA)) /count(p.CADCFTVID)) frequencia "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('FATURADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID "
				+ " )freq on freq.CADCFTVID = c.CADCFTVID "
				
				+ " WHERE C.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
				+ " AND C.ATIVO_CADCFTV = 'SIM' "
				
				+ " and C.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			hCliente.setMixqtde((BigDecimal) row[63]);
			hCliente.setMixqtdemedio((BigDecimal) row[64]);
			hCliente.setTicketmedio((BigDecimal) row[65]);
			hCliente.setNvendas((BigDecimal) row[66]);
			hCliente.setPrimeiravenda((Date) row[67]);
			hCliente.setUltimavenda((Date) row[68]);
			hCliente.setFrequenciamedia((BigDecimal) row[69]);
			
			list.add(hCliente);
		}
		
		return list;
	}
	
	public List<PrazoPedido> prazopedido(int venda, int outros,Date data1, Date data2){
		List<PrazoPedido> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select  "
				+ " p.pedidovendaid pedido, "
				+ " p.CADCFTVID codigo_cliente, "
				+ " cliente.NOME_CADCFTV nome_cliente, "
				+ " tp.DESC_TIPO_PEDIDO tipo_pedido , "
				+ " fase.DESC_ROTEIRO fase_atual, "
				+ " p.STATUS_PEDIDOVENDA, "
				+ " p.VL_TOTALPROD_PEDIDOVENDA valor_total , "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'DD/MM/YYYY') digitacao_entrada, "
				+ " TO_CHAR(financeiro.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') financeiro_entrada, "
				+ " TO_CHAR(conferencia.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') conferencia_entrada, "
				+ " TO_CHAR(analisegestor.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') analisegestor_entrada, "
				+ " TO_CHAR(colorselect.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') colorselect_entrada, "
				+ " TO_CHAR(programacao.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') programacao_entrada, "
				+ " TO_CHAR(producao.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY')  producao_entrada, "
				+ " TO_CHAR(producaocolor.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY')  producaocolor_entrada, "
				+ " TO_CHAR(expedicao.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') expedicao_entrada, "
				+ " TO_CHAR(faturamento.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') faturamento_entrada, "
				+ " TO_CHAR(posvenda.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY')  posvenda_entrada "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join CADCFTV cliente on cliente.CADCFTVID = p.CADCFTVID "
				+ " inner join ROTEIRO fase on fase.roteiroid = p.ROTEIROID "
				+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
				+ " left join ROTEIRO_PEDIDO digitacao on digitacao.PEDIDOVENDAID = p.pedidovendaid and digitacao.ROTEIROID = 1 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )conferencia on conferencia.PEDIDOVENDAID = p.pedidovendaid and conferencia.ROTEIROID = 2 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )financeiro on financeiro.PEDIDOVENDAID = p.pedidovendaid and financeiro.ROTEIROID = 3 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )analisegestor on analisegestor.PEDIDOVENDAID = p.pedidovendaid and analisegestor.ROTEIROID = 9 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )colorselect on colorselect.PEDIDOVENDAID = p.pedidovendaid and colorselect.ROTEIROID = 10 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )programacao on programacao.PEDIDOVENDAID = p.pedidovendaid and programacao.ROTEIROID = 4 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )producao on producao.PEDIDOVENDAID = p.pedidovendaid and producao.ROTEIROID = 5 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )producaocolor on producaocolor.PEDIDOVENDAID = p.pedidovendaid and producaocolor.ROTEIROID = 11 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )expedicao on expedicao.PEDIDOVENDAID = p.pedidovendaid and expedicao.ROTEIROID = 6 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )faturamento on faturamento.PEDIDOVENDAID = p.pedidovendaid and faturamento.ROTEIROID = 7 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )posvenda on posvenda.PEDIDOVENDAID = p.pedidovendaid and posvenda.ROTEIROID = 8 "
				+ "  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PrazoPedido pedido= new PrazoPedido();
			pedido.setPedidoid((BigDecimal)row[0]);
			pedido.setCodigocliente((BigDecimal)row[1]);
			pedido.setNomecliente((String)row[2]);
			pedido.setTipopedido((String)row[3]);
			pedido.setFase_atual((String)row[4]);
			pedido.setStatus((String)row[5]);
			pedido.setValor((BigDecimal)row[6]);
			
			pedido.setDt_digitacao((String)row[7]);
			pedido.setDt_financeiro((String)row[8]);
			pedido.setDt_conferencia((String)row[9]);
			pedido.setDt_analisegestor((String)row[10]);
			pedido.setDt_color((String)row[11]);
			pedido.setDt_programacao((String)row[12]);
			pedido.setDt_producao((String)row[13]);
			pedido.setDt_producaocolor((String)row[14]);
			pedido.setDt_expedicao((String)row[15]);
			pedido.setDt_faturamento((String)row[16]);
			pedido.setDt_posvenda((String)row[17]);
			
			list.add(pedido);
		}
		
		return list;
	}
	
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro){
		List<PedidoFase> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " p.pedidovendaid, "
				+ " p.DT_PEDIDOVENDA, "
				+ " p.CADCFTVID cliente, "
				+ " c.NOME_CADCFTV nomecliente, "
				+ " p.VL_TOTALPROD_PEDIDOVENDA valor, "
				+ " tp.DESC_TIPO_PEDIDO tipopedido, "
				+ " p.STATUS_PEDIDOVENDA status, "
				+ " TO_CHAR(fase.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') dataentrada_fase, "
				+ " to_date(SYSDATE,'DD/MM/YYYY') - to_date(fase.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') dias_na_fase "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )fase on fase.PEDIDOVENDAID = p.pedidovendaid and fase.ROTEIROID = p.ROTEIROID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and p.ROTEIROID = "+roteiro);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PedidoFase pedidofase = new PedidoFase();
			pedidofase.setPedidoid((BigDecimal) row[0]);
			pedidofase.setDatapedido((Date) row[1]);
			pedidofase.setCodigocliente((BigDecimal) row[2]);
			pedidofase.setNomecliente((String) row[3]);
			pedidofase.setValor((BigDecimal) row[4]);
			pedidofase.setTipopedido((String) row[5]);
			pedidofase.setStatus((String) row[6]);
			pedidofase.setDataentradafase((String) row[7]);
			pedidofase.setDiasnafase((BigDecimal) row[8]);
			
			list.add(pedidofase);
		}
		return list;
	}
	
	public List<FasePedido> fasepedido(int venda, int outros){
		List<FasePedido> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " r.ROTEIROID, "
				+ " r.DESC_ROTEIRO, "
				+ " nvl(total.vl_pedido,0) vl_pedido, "
				+ " nvl(total.qtde,0) qtde "
				+ " "
				+ " from ROTEIRO r "
				+ " "
				+ " left join( "
				+ " select "
				+ " p.ROTEIROID , "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_pedido, "
				+ " count(p.pedidovendaid) qtde "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " group by p.ROTEIROID) total on total.ROTEIROID = r.ROTEIROID "
				+ "  "
				+ " order by r.ROTEIROID");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedido fasePedido = new FasePedido();
			fasePedido.setRoteiroid((BigDecimal) row[0]);
			fasePedido.setNomeroteiro((String) row[1]);
			fasePedido.setVlpedido((BigDecimal) row[2]);
			fasePedido.setQtdepedido((BigDecimal) row[3]);
			
			list.add(fasePedido);
		}
		return list;
	}
	
	public ItensTabela itenstabela(String idtabela, String produtoid){
		List<ItensTabela> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " p.TABELAPRECOID, "
				+ " p.VL_UNIT_TABELAPRECOPRODUTO, "
				+ " p.PRODUTOID, "
				+ " pr.NOME_PRODUTO "
				+ " "
				+ " from TABELAPRECOPRODUTO p "
				+ " inner join produto pr on pr.produtoid = p.produtoid "
				+ " where p.TABELAPRECOID = "+idtabela+" and pr.produtoid = "+produtoid);
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			ItensTabela itens = new ItensTabela();
			itens.setTabelaprecoid((BigDecimal) row[0]);
			itens.setValor_tabela((BigDecimal) row[1]);
			itens.setProdutoid((BigDecimal) row[2]);
			itens.setNomeproduto((String) row[3]);
			
			list.add(itens);			
		}
		 ItensTabela t = new ItensTabela();
		 if(list.size()>0) {
			 return t = list.get(0); 
		 }else {
			 return t ;
		 }
	}
	
	public List<ItensTabela> itenstabela(String idtabela){
		List<ItensTabela> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " p.TABELAPRECOID, "
				+ " p.VL_UNIT_TABELAPRECOPRODUTO, "
				+ " p.PRODUTOID, "
				+ " pr.NOME_PRODUTO "
				+ " "
				+ " from TABELAPRECOPRODUTO p "
				+ " inner join produto pr on pr.produtoid = p.produtoid "
				+ " where p.TABELAPRECOID = "+idtabela);
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			ItensTabela itens = new ItensTabela();
			itens.setTabelaprecoid((BigDecimal) row[0]);
			itens.setValor_tabela((BigDecimal) row[1]);
			itens.setProdutoid((BigDecimal) row[2]);
			itens.setNomeproduto((String) row[3]);
			
			list.add(itens);			
		}
		 
		return  list;
	}
	
	public List<TabelaPreco> tabelapreco(){
		List<TabelaPreco> list = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " t.TABELAPRECOID, "
				+ " t.NOME_TABELAPRECO, "
				+ " t.PC_DESC_MAX_ITEM_TABPRECO "
				+ " from TABELAPRECO t "
				+ " where t.SITUACA_TABELAPRECO = 'ATIVO' and t.TABELAPRECOID not in (1,8) ");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			TabelaPreco t = new TabelaPreco();
			t.setId((BigDecimal)row[0]);
			t.setNometabela((String) row[1]);
			t.setPerc_desconto((BigDecimal) row[2]);
			
			list.add(t);
		}
		return list;
	}
	
	
	public List<Venda_Subgrupo> venda_subgrupo(String ano, String mes, String idgrupo){
		List<Venda_Subgrupo> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " grp.GRUPOPRODUTOID, "
				+ " sub.SUBGRUPOPRODUTOID, "
				+ " sub.NOME_SUBGRUPOPRODUTO, "
				+ " sum(it.QT_PEDIDOVENDA_ITEM) qtde "
				+ " "
				+ " from pedidovenda p "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
				+ " inner join CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join produto p on p.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = p.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO grp on grp.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ="+ano
				+ " and TO_CHAR(p.DT_PEDIDOVENDA,'MM') = "+mes
				+ " and grp.GRUPOPRODUTOID = "+idgrupo
				+ " group by "
				+ " grp.GRUPOPRODUTOID, "
				+ " sub.SUBGRUPOPRODUTOID, "
				+ " sub.NOME_SUBGRUPOPRODUTO  order by sum(it.QT_PEDIDOVENDA_ITEM)");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Venda_Subgrupo venda = new Venda_Subgrupo();
			venda.setIdgrupo((BigDecimal)row[0]);
			venda.setIdsubgrupo((BigDecimal)row[1]);
			venda.setNomesubgrupo((String) row[2]);
			venda.setValorsubgrupo((BigDecimal) row[3]);
			
			list.add(venda);
		}
		return list;
	}
	
	public List<Venda_Grupo> venda_grupo(String ano, String mes){
		List<Venda_Grupo> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " grp.GRUPOPRODUTOID, "
				+ " grp.NOME_GRUPOPRODUTO, "
				+ " sum(it.QT_PEDIDOVENDA_ITEM) qtde "
				+ " "
				+ " from pedidovenda p "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
				+ " inner join CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join produto p on p.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = p.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO grp on grp.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') = "+ano
				+ " and TO_CHAR(p.DT_PEDIDOVENDA,'MM') = "+mes
				+ " group by "
				+ " grp.GRUPOPRODUTOID, "
				+ " grp.NOME_GRUPOPRODUTO  order by sum(it.QT_PEDIDOVENDA_ITEM) ");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Venda_Grupo venda = new Venda_Grupo();
			venda.setIdgrupo((BigDecimal)row[0]);
			venda.setNomegrupo((String) row[1]);
			venda.setValorgrupo((BigDecimal) row[2]);
			
			list.add(venda);
		}
		return list;
	}
	
	public List<Vendedor_Ano> vendedor_Ano(String uf){
		List<Vendedor_Ano> list = new ArrayList<>();
		
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select  "
				+ " x.ano,COUNT(x.VENDEDOR1ID) vendedores_ativos  "
				+ " from(select   "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " p.VENDEDOR1ID  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " LEFT join "
				+ " ( "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
				+ " group by cadcftvid "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
				+ " where p.STATUS_PEDIDOVENDA in ('FATURADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'), "
				+ " p.VENDEDOR1ID)x group by x.ano "
				+ " order by x.ano ");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Vendedor_Ano venda = new Vendedor_Ano();
			venda.setAno((String) row[0]);
			venda.setVendedores((BigDecimal) row[1]);
			
			list.add(venda);
		}
		return list;
	}
	
	public List<Vendedor_Mes> vendedor_Mes(String uf, String ano){
		List<Vendedor_Mes> list = new ArrayList<>();
		
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " x.ano,x.mes,COUNT(x.VENDEDOR1ID) vendedores_ativos "
				+ " from(select  "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
				+ " p.VENDEDOR1ID  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " LEFT join "
				+ " ( "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
				+ " group by cadcftvid "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
				+ " where p.STATUS_PEDIDOVENDA in ('FATURADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = "+ano
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'), "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM'), "
				+ " p.VENDEDOR1ID)x group by x.ano ,x.mes "
				+ " order by x.ano ,x.mes ");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Vendedor_Mes venda = new Vendedor_Mes();
			venda.setAno((String) row[0]);
			venda.setMes((String) row[1]);
			venda.setVendedores((BigDecimal) row[2]);
			
			list.add(venda);
		}
		return list;
	}
	
	public List<Cliente_Ano> cliente_Ano(String uf){
		List<Cliente_Ano> list = new ArrayList<>();
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select  "
				+ " x.ano,COUNT(x.CADCFTVID) clientes_ativos  "
				+ " from(select   "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " p.CADCFTVID  "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " LEFT join "
				+ " ( "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
				+ " group by cadcftvid "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
				+ " where p.STATUS_PEDIDOVENDA in ('FATURADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'), "
				+ " p.CADCFTVID)x group by x.ano "
				+ " order by x.ano ");
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Cliente_Ano venda = new Cliente_Ano();
			venda.setAno((String) row[0]);
			venda.setClientes((BigDecimal) row[1]);
			
			list.add(venda);
		}
		return list;
	}
	
	public List<Qtde_Ano> qtde_Ano(String uf){
		List<Qtde_Ano> list = new ArrayList<>();
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select   "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " sum(it.QT_PEDIDOVENDA_ITEM) qtde "
				+ " from pedidovenda p "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " LEFT join( "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
				+ " group by cadcftvid "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
				+ " where p.STATUS_PEDIDOVENDA in ('FATURADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.ORIGEM_PEDIDOVENDA  <> 'SIMETRICA' "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') "
				+ " order by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ");
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Qtde_Ano qtde = new Qtde_Ano();
			qtde.setAno((String) row[0]);
			qtde.setQtde((BigDecimal) row[1]);
			
			list.add(qtde);
		}
		return list;
	}
	
	public List<Qtde_Mes> qtde_Mes(String uf, String ano){
		List<Qtde_Mes> list = new ArrayList<>();
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
				+ " sum(it.QT_PEDIDOVENDA_ITEM) qtde "
				+ " from pedidovenda p  "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " LEFT join( "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
				+ " group by cadcftvid "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
				+ " where p.STATUS_PEDIDOVENDA in ('FATURADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.ORIGEM_PEDIDOVENDA  <> 'SIMETRICA' "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = "+ano
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM'), "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') "
				+ " order by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') ");
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Qtde_Mes qtde = new Qtde_Mes();
			qtde.setAno((String) row[0]);
			qtde.setMes((String) row[1]);
			qtde.setQtde((BigDecimal) row[2]);
			
			list.add(qtde);
		}
		return list;
	}
	
	public List<Diretor_01> diretor_01(String ano, String mes){
		List<Diretor_01> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
						+ " pl.ano, "
						+ " pl.VL_METAFATURAMENTO, "
						+ " faturado_anual.vl_anual_faturado, "
						+ " round((faturado_anual.vl_anual_faturado/pl.VL_METAFATURAMENTO)*100,2) perc_meta_faturado_anual, "
						+ " meta_vendedor_mes.mes, "
						+ " meta_vendedor_mes.meta_vendedor_mes, "
						+ " faturado_mes.vl_mes_faturado, "
						+ " round((faturado_mes.vl_mes_faturado/meta_painel.meta_faturamento)*100,2) perc_meta_faturado_mes, "
						+ " faturado_mes.qtde num_docs, "
						+ " round(faturado_mes.vl_mes_faturado /faturado_mes.qtde,2) ticket_medio, "
						+ " clientes.clientes_atendidos, "
						+ " clientes_novos.qtde_clientes_novos, "
						+ " meta_painel.meta_pedidos, "
						+ " meta_painel.meta_faturamento, "
						+ " pedido_mes.vl_mes_pedido, "
						+ " round((pedido_mes.vl_mes_pedido/meta_painel.meta_pedidos)*100,2) perc_meta_pedido_mes, "
						+ " faturado_anual.qtde, "
						+ " round(faturado_anual.vl_anual_faturado /faturado_anual.qtde,2) ticket_medio_anual, "
						+ " pedido_mes.qtde qtde_pedido, "
						+ " round(pedido_mes.vl_mes_pedido /pedido_mes.qtde,2) ticket_medio_pedido, "
						+ " vendedores.vendedores_atendidos "
						+ "  "
						+ " from PLANEJAMENTO_ANUAL pl "
						+ "  "
						+ " inner join(  "
						+ " select  "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
						+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_anual_faturado, "
						+ " count(p.pedidovendaid) qtde "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " where p.STATUS_PEDIDOVENDA = 'FATURADO' "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') "
						+ " )faturado_anual on faturado_anual.ano = pl.ano "
						+ "  "
						+ " inner join( "
						+ " select "
						+ " m.ANO_METAVENDEDOR ano, "
						+ " LPAD(m.MES_METAVENDEDOR,2,0) mes, "
						+ " sum(m.VALOR_METAVENDEDOR)meta_vendedor_mes "
						+ " from META_VENDEDOR m "
						+ " group by  "
						+ " m.ANO_METAVENDEDOR, "
						+ " LPAD(m.MES_METAVENDEDOR,2,0) "
						+ " )meta_vendedor_mes on meta_vendedor_mes.ano = pl.ano "
						+ "  "
						+ " inner join(  "
						+ " select  "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
						+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_mes_faturado, "
						+ " count(p.pedidovendaid) qtde "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " where p.STATUS_PEDIDOVENDA = 'FATURADO' "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') "
						+ " )faturado_mes on faturado_mes.ano = pl.ano and faturado_mes.mes = meta_vendedor_mes.mes  "
						+ "  "
						+ " inner join(  "
						+ " select  "
						+ " x.ano,x.mes,COUNT(x.CADCFTVID) clientes_atendidos "
						+ " from(select  "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
						+ " p.CADCFTVID "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM'), "
						+ " p.CADCFTVID)x group by x.ano,x.mes "
						+ " )clientes on clientes.ano = pl.ano and clientes.mes = meta_vendedor_mes.mes "
						+ "  "
						+ " inner join(  "
						+ " select  "
						+ " x.ano,x.mes,COUNT(x.VENDEDOR1ID) vendedores_atendidos "
						+ " from(select  "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
						+ " p.VENDEDOR1ID "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " where p.STATUS_PEDIDOVENDA in ('FATURADO')  "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM'), "
						+ " p.VENDEDOR1ID)x group by x.ano,x.mes "
						+ " )vendedores on vendedores.ano = pl.ano and vendedores.mes = meta_vendedor_mes.mes "
						+ " "
						+ " left join(  "
						+ " select  "
						+ " TO_CHAR(c.DATACREATE_CADCFTV,'YYYY') ano, "
						+ " TO_CHAR(c.DATACREATE_CADCFTV,'MM') mes, "
						+ " count(c.CADCFTVID) qtde_clientes_novos "
						+ " from cadcftv c "
						+ " inner join cliente cl on cl.CADCFTVID = c.cadcftvid "
						+ " group by "
						+ " TO_CHAR(c.DATACREATE_CADCFTV,'YYYY'), "
						+ " TO_CHAR(c.DATACREATE_CADCFTV,'MM') "
						+ " )clientes_novos on clientes_novos.ano = pl.ano and clientes_novos.mes = meta_vendedor_mes.mes "
						+ "  "
						+ " left join( "
						+ " select "
						+ " m.ANO_MP ano, "
						+ " m.MES_MP mes, "
						+ " m.VL_META_PEDIDOS_PNLGESTAO meta_pedidos, "
						+ " m.VL_META_VENDAS_PNLGESTAO meta_faturamento "
						+ " from MACROPLANO m "
						+ " ) meta_painel on meta_painel.ano = pl.ano and meta_painel.mes = meta_vendedor_mes.mes "
						+ "  "
						+ " left join( "
						+ " select "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
						+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_mes_pedido, "
						+ " count(p.pedidovendaid) qtde "
						+ " from pedidovenda p "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
						+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') "
						+ " )pedido_mes on pedido_mes.ano = pl.ano and pedido_mes.mes = meta_vendedor_mes.mes "
						+ "  "
						+ " where pl.ano = "+ano
						+ " and meta_vendedor_mes.mes ="+mes);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Diretor_01 diretor_01 = new Diretor_01();
			diretor_01.setAno(ano);
			diretor_01.setMes(mes);
			diretor_01.setMeta_anual((BigDecimal) row[1]);
			diretor_01.setAtingido_anual((BigDecimal) row[2]);
			diretor_01.setPerc_atingido_anual((BigDecimal) row[3]);
			diretor_01.setMeta_mensal((BigDecimal) row[5]);
			diretor_01.setAtingido_mensal((BigDecimal) row[6]);
			diretor_01.setPerc_atingido_mensal((BigDecimal) row[7]);
			diretor_01.setNum_docs((BigDecimal) row[8]);
			diretor_01.setTicket_medio((BigDecimal) row[9]);
			diretor_01.setClientes_atendidos((BigDecimal) row[10]);
			diretor_01.setClientes_novos((BigDecimal) row[11]);
			diretor_01.setMeta_mensal_pedidos_p((BigDecimal)row[12]);
			diretor_01.setMeta_mensal_faturamento_p((BigDecimal)row[13]);
			diretor_01.setAtingido_mensal_pedido_p((BigDecimal) row[14]);
			diretor_01.setPerc_atingido_mensal_pedido_p((BigDecimal) row[15]);
			diretor_01.setNum_docs_anual((BigDecimal) row[16]);
			diretor_01.setTicket_medio_anual((BigDecimal) row[17]);
			diretor_01.setNum_docs_pedido((BigDecimal) row[18]);
			diretor_01.setTicket_medio_pedido((BigDecimal) row[19]);
			diretor_01.setVendedores_atendidos((BigDecimal) row[20]);
						
			list.add(diretor_01);
		}
		return list;
	}

	public List<MateriaPrimaEstrutura> materiaPrimaEstrutura(String produtoid){
		List<MateriaPrimaEstrutura> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
						+ " p.produtoid, "
						+ " p.nome_produto, "
						+ " p.VL_CUSTO_PRODUTO, "
						+ "  "
						+ " ficha.PRODUTOID_PAI, "
						+ " pp.NOME_PRODUTO nome_produto_pai, "
						+ " sum(ficha.QTDE_ESTRUTURA) QTDE_ESTRUTURA, "
						+ " sum(ficha.QTDE_ESTRUTURA * p.VL_CUSTO_PRODUTO) custo_ficha, "
						+ " pp.TP_PRODUTO, "
						+ " pp.VL_CUSTO_PRODUTO custo_acabado, "
						+ " tb.VL_UNIT_TABELAPRECOPRODUTO valor_tabela, "
						+ " tb.tabelaprecoid "
						+ " from produto p "
						+ "  "
						+ " left join( "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e.PRODUTOID_FILHO, "
						+ " e.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " where e.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e1.PRODUTOID_FILHO, "
						+ " e1.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " where e1.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e2.PRODUTOID_FILHO, "
						+ " e2.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " where e2.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e3.PRODUTOID_FILHO, "
						+ " e3.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " where e3.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e4.PRODUTOID_FILHO, "
						+ " e4.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " where e4.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e5.PRODUTOID_FILHO, "
						+ " e5.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " where e5.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e6.PRODUTOID_FILHO, "
						+ " e6.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " left join ESTRUTURA e6 on e6.PRODUTOID_FILHO = e5.PRODUTOID_PAI "
						+ " where e6.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e7.PRODUTOID_FILHO, "
						+ " e7.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " left join ESTRUTURA e6 on e6.PRODUTOID_FILHO = e5.PRODUTOID_PAI "
						+ " left join ESTRUTURA e7 on e7.PRODUTOID_FILHO = e6.PRODUTOID_PAI "
						+ " where e7.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e8.PRODUTOID_FILHO, "
						+ " e8.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " left join ESTRUTURA e6 on e6.PRODUTOID_FILHO = e5.PRODUTOID_PAI "
						+ " left join ESTRUTURA e7 on e7.PRODUTOID_FILHO = e6.PRODUTOID_PAI "
						+ " left join ESTRUTURA e8 on e8.PRODUTOID_FILHO = e7.PRODUTOID_PAI "
						+ " where e8.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e9.PRODUTOID_FILHO, "
						+ " e9.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " left join ESTRUTURA e6 on e6.PRODUTOID_FILHO = e5.PRODUTOID_PAI "
						+ " left join ESTRUTURA e7 on e7.PRODUTOID_FILHO = e6.PRODUTOID_PAI "
						+ " left join ESTRUTURA e8 on e8.PRODUTOID_FILHO = e7.PRODUTOID_PAI "
						+ " left join ESTRUTURA e9 on e9.PRODUTOID_FILHO = e8.PRODUTOID_PAI "
						+ " where e9.PRODUTOID_FILHO is not null "
						+ "  "
						+ " union all "
						+ "  "
						+ " select "
						+ " e.PRODUTOID_FILHO x, "
						+ " e10.PRODUTOID_FILHO, "
						+ " e10.PRODUTOID_PAI, "
						+ " e.QTDE_ESTRUTURA "
						+ " from ESTRUTURA e "
						+ " left join ESTRUTURA e1 on e1.PRODUTOID_FILHO = e.PRODUTOID_PAI "
						+ " left join ESTRUTURA e2 on e2.PRODUTOID_FILHO = e1.PRODUTOID_PAI "
						+ " left join ESTRUTURA e3 on e3.PRODUTOID_FILHO = e2.PRODUTOID_PAI "
						+ " left join ESTRUTURA e4 on e4.PRODUTOID_FILHO = e3.PRODUTOID_PAI "
						+ " left join ESTRUTURA e5 on e5.PRODUTOID_FILHO = e4.PRODUTOID_PAI "
						+ " left join ESTRUTURA e6 on e6.PRODUTOID_FILHO = e5.PRODUTOID_PAI "
						+ " left join ESTRUTURA e7 on e7.PRODUTOID_FILHO = e6.PRODUTOID_PAI "
						+ " left join ESTRUTURA e8 on e8.PRODUTOID_FILHO = e7.PRODUTOID_PAI "
						+ " left join ESTRUTURA e9 on e9.PRODUTOID_FILHO = e8.PRODUTOID_PAI "
						+ " left join ESTRUTURA e10 on e10.PRODUTOID_FILHO = e9.PRODUTOID_PAI "
						+ " where e10.PRODUTOID_FILHO is not null "
						+ " )ficha on ficha.x = p.produtoid "
						+ "  "
						+ " left join produto pp on pp.produtoid = ficha.PRODUTOID_PAI "
						+ " left join TABELAPRECOPRODUTO tb on tb.produtoid = pp.produtoid and tb.tabelaprecoid in (1,8) "
						+ " where p.tp_produto = 'MATPRIMA'  "
						+ " and pp.TP_PRODUTO = 'ACABADO' "
						+ " and pp.STATUS_PRODUTO = 'ATIVO' "
						+ " and p.STATUS_PRODUTO = 'ATIVO' "
						+ " and p.produtoid = "+produtoid
						+ "  "
						+ " group by "
						+ " p.produtoid, "
						+ " p.nome_produto, "
						+ " p.VL_CUSTO_PRODUTO, "
						+ " ficha.PRODUTOID_PAI, "
						+ " pp.NOME_PRODUTO, "
						//+ " ficha.QTDE_ESTRUTURA, "
						+ " pp.TP_PRODUTO,pp.VL_CUSTO_PRODUTO,tb.VL_UNIT_TABELAPRECOPRODUTO,tb.tabelaprecoid "
						+ "  "
						+ " order by ficha.PRODUTOID_PAI ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			MateriaPrimaEstrutura materiaPrimaEstrutura = new MateriaPrimaEstrutura();
			
			materiaPrimaEstrutura.setProdutoid((BigDecimal) row[0]);
			materiaPrimaEstrutura.setNomeproduto((String) row[1]);
			materiaPrimaEstrutura.setVl_custo((BigDecimal) row[2]);
			materiaPrimaEstrutura.setProdutoid_acabado((BigDecimal) row[3]);
			materiaPrimaEstrutura.setNomeproduto_acabado((String) row[4]);
			materiaPrimaEstrutura.setQtde_estrutura((BigDecimal) row[5]);
			materiaPrimaEstrutura.setCusto_ficha((BigDecimal) row[6]);
			materiaPrimaEstrutura.setTipoproduto((String) row[7]);
			materiaPrimaEstrutura.setCusto_acabado((BigDecimal) row[8]);
			materiaPrimaEstrutura.setValor_tabela((BigDecimal) row[9]);
			materiaPrimaEstrutura.setTabelaprecoid((BigDecimal) row[10]);
			list.add(materiaPrimaEstrutura);
		}
		return list;
	}
	/* venda ano mes */
	public List<VendaAnoMes> vendaanomes() {
		List<VendaAnoMes> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				// "SELECT * FROM("
				" SELECT " + " TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') AS ANO, " + " TO_CHAR(EN.DT_PEDIDOVENDA,'MM') AS MES, "
						+ " SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VL_TOTAL " + " FROM PEDIDOVENDA EN "
						+ " INNER JOIN VENDEDOR V ON V.CADCFTVID = EN.VENDEDOR1ID "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID " + " WHERE "
						+ " CF.TIPOOPERACAO_CFOP = 'VENDA' "
						+ " AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+ " GROUP BY "
						+ " TO_CHAR(EN.DT_PEDIDOVENDA,'MM'), " + " TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') " + " ORDER BY "
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
			Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
						+ " WHERE p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						
						
						+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
						
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
	
	public List<NotasClienteEmail> notasclienteemails(String ano, String mes, String dia){
		List<NotasClienteEmail> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"SELECT DISTINCT * FROM ("
						+ "select "
						+ "count(p.pedidovendaid) n_notas_dia,"
						+ "nota_mes.n_notas_mes,"
						+ "p.CADCFTVID,"
						+ "c.NOME_CADCFTV,"
						+ "email.EMAIL_EMAILCADCFTV"
						+ ""
						+ "from pedidovenda p "
						+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
						+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID"
						+ ""
						+ "left join("
						+ "select * from("
						+ "select "
						+ "ROW_NUMBER() OVER(PARTITION BY e.CADCFTVID ORDER BY e.CADCFTVID ASC) AS nun,"
						+ "e.CADCFTVID,"
						+ "e.EMAIL_EMAILCADCFTV"
						+ "from EMAILCADCFTV e"
						+ "group by"
						+ "e.CADCFTVID,"
						+ "e.EMAIL_EMAILCADCFTV)x"
						+ "where x.nun = 1"
						+ ")email on email.CADCFTVID = p.CADCFTVID"
						+ ""
						+ "left join("
						+ "select "
						+ "count(p.pedidovendaid) n_notas_mes,"
						+ "p.CADCFTVID,"
						+ "c.NOME_CADCFTV"
						+ ""
						+ "from pedidovenda p "
						+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID"
						+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID"
						+ ""
						+ "where p.STATUS_PEDIDOVENDA = 'FATURADO'"
						+ "AND CF.tipooperacao_cfop = 'VENDA'"
						+ "and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'"
						+ "and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"'"
						+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') = '"+mes+"'"
						+ ""
						+ "group by"
						+ "p.CADCFTVID,"
						+ "c.NOME_CADCFTV"
						+ ")nota_mes on nota_mes.CADCFTVID = p.CADCFTVID"
						+ ""
						+ "where p.STATUS_PEDIDOVENDA = 'FATURADO'"
						+ "AND CF.tipooperacao_cfop = 'VENDA'"
						+ "and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'"
						+ "and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"'"
						+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') = '"+mes+"'"
						+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '"+dia+"'"
						+ ""
						+ ""
						+ "group by"
						+ "nota_mes.n_notas_mes,"
						+ "p.CADCFTVID,email.EMAIL_EMAILCADCFTV,"
						+ "c.NOME_CADCFTV)X"
						+ ""
						+ "WHERE X.n_notas_dia = X.n_notas_mes and x.EMAIL_EMAILCADCFTV is not null"
				);
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			NotasClienteEmail notasClienteEmail = new NotasClienteEmail();

			
			notasClienteEmail.setCodigocliente((BigDecimal) row[2]);
			notasClienteEmail.setNomecliente((String) row[3] );
			notasClienteEmail.setEmail((String) row[4] );
						
			list.add(notasClienteEmail);
		}
		
		return list;
	}
	
	//pedidos de venda
	public List<VendasEmGeral> vendasemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
						+ " v.NOME_CADCFTV nome_vendedor, "
						+ " itens.TOTALLIQUIDO_PEDIDO,"
						+ " itens.perc_lucro "
						+ " from pedidovenda p "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
						+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
						+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
						+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
						//+ " inner join roteiro r on r.roteiroid = p.roteiroid "
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " left join( "
						+ " SELECT  "
						+ " x.pedidovendaid, "
						+ " sum(x.TOTAL_VENDA) TOTAL_VENDA , "
						+ " sum(x.TOTALLIQUIDO_PEDIDO) TOTALLIQUIDO_PEDIDO,"
						+ " round((sum(x.TOTALLIQUIDO_PEDIDO) /sum(x.TOTAL_VENDA))*100,2) as perc_lucro"
						+ " FROM( "
						+ " select "
						+ " p.pedidovendaid, "
						+ " IT.vl_total_pedidovenda_item TOTAL_VENDA, "
						+ " (IT.vl_total_pedidovenda_item - ( it.VL_CUSTOORIG_PEDIDOVENDA_ITEM  * it.qt_pedidovenda_item )) TOTALLIQUIDO_PEDIDO "
						+ " from pedidovenda_item it "
						+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid)X "
						+ " group by x.pedidovendaid "
						+ " ) itens on itens.pedidovendaid = p.pedidovendaid "
						
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
						+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			
			vendasEmGeral.setValortotalliquidopedido((BigDecimal) row[10] );
			vendasEmGeral.setPerc_lucro((BigDecimal) row[11] );
			
			list.add(vendasEmGeral);
		}

		return list;
	}
	//detalhes do pedido de venda
	public List<VendasEmGeralItem> vendasemgeralitem(BigDecimal pedido) {
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
			vendasEmGeralItem.setImagem((Blob) row2[17] );
			
			list.add(vendasEmGeralItem);
		}
		return list;
		
	}
	
	//pedidos de amostra
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
							+ " and p.TIPOPEDIDOID = 4 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
							+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
							+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
		public List<VendasEmGeralItem> amostraemgeralitem(BigDecimal pedido) {
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
				vendasEmGeralItem.setImagem((Blob) row2[17] );
				
				list.add(vendasEmGeralItem);
			}
			return list;
			
		}
		
		//pedidos de amostra
		public List<VendasEmGeral> amostrapagaemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
								+ " AND CF.tipooperacao_cfop = 'VENDA' "
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.TIPOPEDIDOID = 6 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
								+ " AND CF.tipooperacao_cfop <> 'VENDA' "
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

			
	//pedidos de troca defeito
	public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
								+ " and p.TIPOPEDIDOID = 2 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			public List<VendasEmGeralItem> trocadefeitoemgeralitem(BigDecimal pedido) {
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

			//pedidos de troca defeito
			public List<VendasEmGeral> trocanegocioemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
										+ " and p.TIPOPEDIDOID = 13 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
										+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
										+ " AND CF.tipooperacao_cfop <> 'VENDA' "
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
							vendasEmGeralItem.setImagem((Blob) row2[17] );
							
							list.add(vendasEmGeralItem);
						}
						return list;
						
}

//pedidos de investimento vendedor
public List<InvestimentoVendedor> investimentovendedor(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
	List<InvestimentoVendedor> list = new ArrayList<>();
		
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataFormatada3 = formato.format(data2);
	
	//ajuste de data para bater certo
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data2); 
		cal.add(Calendar.DATE, 1);
		data2 = cal.getTime();
								
	
	String dataFormatada = formato.format(data1);
	String dataFormatada2 = formato.format(data2);
	

	javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
			" select investimento.vendedor, "
			+ " investimento.nome_vendedor, "
			+ " geral.vlgeralfaturado, "
			+ " NVL(fat.vlfaturado,0) as vlfaturado, "

			+ " NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocadefeito),0)+NVL(sum(investimento.trocanegocio),0) as  totalinvestido, "

			+ " NVL(((NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocadefeito),0)+NVL(sum(investimento.trocanegocio),0))/fat.vlfaturado)*100 ,0) as pcinvestidovendedor, "


			+ " ((NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocadefeito),0)+NVL(sum(investimento.trocanegocio),0))/geral.vlgeralfaturado)*100 as pcinvestidogeral, "

			+ " NVL(sum(investimento.amostra),0) as vlamostra, "
			+ " (NVL(sum(investimento.amostra),0) / fat.vlfaturado )*100 as pcamostra, "

			+ " NVL(sum(investimento.bonificacao),0) as vlbonificacao, "
			+ " NVL((NVL(sum(investimento.bonificacao),0) / fat.vlfaturado )*100 ,0) as pcbonificacao, "

			+ " NVL(sum(investimento.expositor),0) as vlexpositor, "
			+ " NVL((NVL(sum(investimento.expositor),0) / fat.vlfaturado )*100 ,0) as pcexpositor, "

			+ " NVL(sum(investimento.brinde),0) as vlbrinde, "
			+ " NVL((NVL(sum(investimento.brinde),0) / fat.vlfaturado )*100 ,0) as pcbrinde, "

			+ " NVL(sum(investimento.trocadefeito),0) as vltrocadefeito, "
			+ " NVL((NVL(sum(investimento.trocadefeito),0) / fat.vlfaturado )*100 ,0) as pctrocadefeito, "

			+ " NVL(sum(investimento.trocanegocio),0) as vltrocanegocio, "
			+ " NVL((NVL(sum(investimento.trocanegocio),0) / fat.vlfaturado )*100 ,0) as pctrocanegocio, "
			
			+ " NVL(sum(investimento.bonificacaoexpositor),0) as vlbonificacaoexpositor, "
			+ " NVL((NVL(sum(investimento.bonificacaoexpositor),0) / fat.vlfaturado )*100 ,0) as pcbonificacaoexpositor,"
			
			+ " NVL(fat_2021.vlfaturado,0) as vlfaturado2021, "
			+ " NVL(fat_2020.vlfaturado,0) as vlfaturado2020,"
			+ " investimento2021.investimento2021, "
			+ " investimento2020.investimento2020,"
			+ " nvl(investimento2021.investimento2021,0)/NVL(fat_2021.vlfaturado,1)*100 pcinvest2021, "
			+ " nvl(investimento2020.investimento2020,0)/NVL(fat_2020.vlfaturado,1)*100 pcinvest2020 "

			+ " from( "
			+ " select "
			+ " v.cadcftvid as vendedor, "
			+ " v.NOME_CADCFTV as nome_vendedor , "
			+ " case when p.TIPOPEDIDOID = 4 then p.vl_totalprod_pedidovenda end as amostra,   "
			+ " case when p.TIPOPEDIDOID = 3 then p.vl_totalprod_pedidovenda end as bonificacao,  "
			+ " case when p.TIPOPEDIDOID = 5  then p.vl_totalprod_pedidovenda end as expositor,  "
			+ " case when p.TIPOPEDIDOID = 14 then p.vl_totalprod_pedidovenda end as brinde,  "
			+ " case when p.TIPOPEDIDOID = 13 then p.vl_totalprod_pedidovenda end as trocanegocio,  "
			+ " case when p.TIPOPEDIDOID = 2 then p.vl_totalprod_pedidovenda end as trocadefeito, "
			+ " case when p.TIPOPEDIDOID = 16 then p.vl_totalprod_pedidovenda end as bonificacaoexpositor "

			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ " inner join ( "
			+ " select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ " inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ " where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ " ) liberado on liberado.pedidovendaid = p.pedidovendaid "

			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
			+ " AND CF.tipooperacao_cfop <> 'VENDA'  "
			
			+ " and liberado.DT_ROTEIRO_PEDIDO between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,2,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ " and tr.ORDEM_ROTEIRO > 3 "
			+ " )investimento "

		
			+ " left join ( "
			+ "  select "
			+ " sum(p.vl_totalprod_pedidovenda) as vlfaturado,  "
			+ " V.CADCFTVID as vendedor "
			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('FATURADO')  "
			+ " AND CF.tipooperacao_cfop = 'VENDA'  "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ " group by V.CADCFTVID ) fat on fat.vendedor = investimento.vendedor "
			
			+ " inner join ( "
			+ "  select "
			+ " sum(p.vl_totalprod_pedidovenda) as vlgeralfaturado  "
			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('FATURADO')  "
			+ " AND CF.tipooperacao_cfop = 'VENDA'  "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " ORDER BY P.NR_NOTA_PEDIDOVENDA )geral on geral.vlgeralfaturado is not null "
			
			+ "left join ( "
			+ " select "
			+ "sum(p.vl_totalprod_pedidovenda) as vlfaturado, "
			+ "V.CADCFTVID as vendedor "
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('FATURADO') "
			+ "AND CF.tipooperacao_cfop = 'VENDA' "
			+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2020') "
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "group by V.CADCFTVID ) fat_2020 on fat_2020.vendedor = investimento.vendedor "

			+ "left join ( "
			+ " select "
			+ "sum(p.vl_totalprod_pedidovenda) as vlfaturado, "
			+ "V.CADCFTVID as vendedor "
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('FATURADO') "
			+ "AND CF.tipooperacao_cfop = 'VENDA' "
			+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2021') "
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "group by V.CADCFTVID ) fat_2021 on fat_2021.vendedor = investimento.vendedor "
			
			+ "left join ( "
			+ "select "
			+ "v.cadcftvid as vendedor, "
			+ "v.NOME_CADCFTV as nome_vendedor , "
			+ "sum(p.vl_totalprod_pedidovenda) as investimento2021  "
			
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "inner join ( "
			+ "select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ "inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ ") liberado on liberado.pedidovendaid = p.pedidovendaid "
			+ ""
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
			+ "AND CF.tipooperacao_cfop <> 'VENDA' "
			+ ""
			+ "and TO_CHAR(liberado.DT_ROTEIRO_PEDIDO,'YYYY') in ('2021') "
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,2,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "and tr.ORDEM_ROTEIRO > 3"
			+ "group by  v.cadcftvid,v.NOME_CADCFTV "
			+ ")investimento2021 on investimento2021.vendedor = investimento.vendedor "		
			
			+ " left join ( "
			+ "select "
			+ "v.cadcftvid as vendedor, "
			+ "v.NOME_CADCFTV as nome_vendedor , "
			+ "sum(p.vl_totalprod_pedidovenda) as investimento2020  "

			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "inner join ( "
			+ "select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ "inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ ") liberado on liberado.pedidovendaid = p.pedidovendaid "
			+ ""
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
			+ "AND CF.tipooperacao_cfop <> 'VENDA' "
			+ ""
			+ "and TO_CHAR(liberado.DT_ROTEIRO_PEDIDO,'YYYY') in ('2020') "
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,2,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "and tr.ORDEM_ROTEIRO > 3"
			+ "group by  v.cadcftvid,v.NOME_CADCFTV "
			+ ")investimento2020 on investimento2020.vendedor = investimento.vendedor"	
			

			+ " group by  "
			+ " geral.vlgeralfaturado, "
			+ " fat.vlfaturado, "
			+ " investimento.vendedor, "
			+ " investimento.nome_vendedor,fat_2020.vlfaturado,fat_2021.vlfaturado,investimento2021.investimento2021,investimento2020.investimento2020 ");

	List<Object[]> lista = query.getResultList();
								
	for (Object[] row : lista) {
		InvestimentoVendedor vendasEmGeral = new InvestimentoVendedor();
		
		vendasEmGeral.setVendedor((BigDecimal) row[0]);
		vendasEmGeral.setNomevendedor((String) row[1] );
		vendasEmGeral.setVlgeralfaturado((BigDecimal) row[2] );
		vendasEmGeral.setVlvendedorfaturado((BigDecimal) row[3] );
		vendasEmGeral.setVltotalinvestido((BigDecimal) row[4] );
		vendasEmGeral.setPcinvestidovendedor((BigDecimal) row[5] );
		vendasEmGeral.setPcinvestidogeral((BigDecimal) row[6] );
		vendasEmGeral.setVlamostra((BigDecimal) row[7] );
		vendasEmGeral.setPcamostra((BigDecimal) row[8] );
		vendasEmGeral.setVlbonificacao((BigDecimal) row[9] );
		vendasEmGeral.setPcbonificacao((BigDecimal) row[10] );
		vendasEmGeral.setVlexpositor((BigDecimal) row[11] );
		vendasEmGeral.setPcexpositor((BigDecimal) row[12] );
		vendasEmGeral.setVlbrinde((BigDecimal) row[13] );
		vendasEmGeral.setPcbrinde((BigDecimal) row[14] );
		vendasEmGeral.setVltrocadefeito((BigDecimal) row[15] );
		vendasEmGeral.setPctrocadefeito((BigDecimal) row[16] );
		vendasEmGeral.setVltrocanegocio((BigDecimal) row[17] );
		vendasEmGeral.setPctrocanegocio((BigDecimal) row[18] );
		vendasEmGeral.setVlbonificacaoexpositor((BigDecimal) row[19] );
		vendasEmGeral.setPcbonificacaoexpositor((BigDecimal) row[20] );
		
		vendasEmGeral.setVlvendedorfaturado2021((BigDecimal) row[21] );
		vendasEmGeral.setVlvendedorfaturado2020((BigDecimal) row[22] );
		vendasEmGeral.setInvestimento2021((BigDecimal) row[23] );
		vendasEmGeral.setInvestimento2020((BigDecimal) row[24] );
		vendasEmGeral.setPcinvest2021((BigDecimal) row[25] );
		vendasEmGeral.setPcinvest2020((BigDecimal) row[26] );
		
		list.add(vendasEmGeral);
	}

	return list;
}
//investimento vendedor modelo 2
public List<InvestimentoVendedor> investimentovendedor_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
	List<InvestimentoVendedor> list = new ArrayList<>();
		
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataFormatada3 = formato.format(data2);
	
	//ajuste de data para bater certo
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data2); 
		cal.add(Calendar.DATE, 1);
		data2 = cal.getTime();
								
	
	String dataFormatada = formato.format(data1);
	String dataFormatada2 = formato.format(data2);
	

	javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
			" select investimento.vendedor, "
			+ " investimento.nome_vendedor, "
			+ " geral.vlgeralfaturado, "
			+ " NVL(fat.vlfaturado,0) as vlfaturado, "

			+ " NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocanegocio),0) as  totalinvestido, "

			+ " NVL(((NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocanegocio),0))/fat.vlfaturado)*100 ,0) as pcinvestidovendedor, "


			+ " ((NVL(sum(investimento.amostra),0)+NVL(sum(investimento.bonificacao),0)+NVL(sum(investimento.bonificacaoexpositor),0)+NVL(sum(investimento.expositor),0) + "
			+ " NVL(sum(investimento.brinde),0)+NVL(sum(investimento.trocanegocio),0))/geral.vlgeralfaturado)*100 as pcinvestidogeral, "

			+ " NVL(sum(investimento.amostra),0) as vlamostra, "
			+ " (NVL(sum(investimento.amostra),0) / fat.vlfaturado )*100 as pcamostra, "

			+ " NVL(sum(investimento.bonificacao),0) as vlbonificacao, "
			+ " NVL((NVL(sum(investimento.bonificacao),0) / fat.vlfaturado )*100 ,0) as pcbonificacao, "

			+ " NVL(sum(investimento.expositor),0) as vlexpositor, "
			+ " NVL((NVL(sum(investimento.expositor),0) / fat.vlfaturado )*100 ,0) as pcexpositor, "

			+ " NVL(sum(investimento.brinde),0) as vlbrinde, "
			+ " NVL((NVL(sum(investimento.brinde),0) / fat.vlfaturado )*100 ,0) as pcbrinde, "

			+ " NVL(sum(investimento.trocanegocio),0) as vltrocanegocio, "
			+ " NVL((NVL(sum(investimento.trocanegocio),0) / fat.vlfaturado )*100 ,0) as pctrocanegocio, "
			
			+ " NVL(sum(investimento.bonificacaoexpositor),0) as vlbonificacaoexpositor, "
			+ " NVL((NVL(sum(investimento.bonificacaoexpositor),0) / fat.vlfaturado )*100 ,0) as pcbonificacaoexpositor, "
			
			+ " NVL(fat_2021.vlfaturado,0) as vlfaturado2021, "
			+ " NVL(fat_2020.vlfaturado,0) as vlfaturado2020,"
			+ " investimento2021.investimento2021, "
			+ " investimento2020.investimento2020,"
			+ " nvl(investimento2021.investimento2021,0)/NVL(fat_2021.vlfaturado,1)*100 pcinvest2021, "
			+ " nvl(investimento2020.investimento2020,0)/NVL(fat_2020.vlfaturado,1)*100 pcinvest2020 "	

			+ " from( "
			+ " select "
			+ " v.cadcftvid as vendedor, "
			+ " v.NOME_CADCFTV as nome_vendedor , "
			+ " case when p.TIPOPEDIDOID = 4 then p.vl_totalprod_pedidovenda end as amostra,   "
			+ " case when p.TIPOPEDIDOID = 3 then p.vl_totalprod_pedidovenda end as bonificacao,  "
			+ " case when p.TIPOPEDIDOID = 5  then p.vl_totalprod_pedidovenda end as expositor,  "
			+ " case when p.TIPOPEDIDOID = 14 then p.vl_totalprod_pedidovenda end as brinde,  "
			+ " case when p.TIPOPEDIDOID = 13 then p.vl_totalprod_pedidovenda end as trocanegocio, "
			+ " case when p.TIPOPEDIDOID = 16 then p.vl_totalprod_pedidovenda end as bonificacaoexpositor "

			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ " inner join ( "
			+ " select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ " inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ " where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ " ) liberado on liberado.pedidovendaid = p.pedidovendaid "

			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
			+ " AND CF.tipooperacao_cfop <> 'VENDA'  "
			
			+ " and liberado.DT_ROTEIRO_PEDIDO between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ " and tr.ORDEM_ROTEIRO > 3 "
			+ " )investimento "

		
			+ " left join ( "
			+ "  select "
			+ " sum(p.vl_totalprod_pedidovenda) as vlfaturado,  "
			+ " V.CADCFTVID as vendedor "
			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('FATURADO')  "
			+ " AND CF.tipooperacao_cfop = 'VENDA'  "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ " group by V.CADCFTVID ) fat on fat.vendedor = investimento.vendedor "
			
			+ " inner join ( "
			+ "  select "
			+ " sum(p.vl_totalprod_pedidovenda) as vlgeralfaturado  "
			+ " from pedidovenda p  "
			+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ " where p.status_pedidovenda in ('FATURADO')  "
			+ " AND CF.tipooperacao_cfop = 'VENDA'  "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " ORDER BY P.NR_NOTA_PEDIDOVENDA )geral on geral.vlgeralfaturado is not null "
			
			+ "left join ( "
			+ " select "
			+ "sum(p.vl_totalprod_pedidovenda) as vlfaturado, "
			+ "V.CADCFTVID as vendedor "
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('FATURADO') "
			+ "AND CF.tipooperacao_cfop = 'VENDA' "
			+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2020') "
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "group by V.CADCFTVID ) fat_2020 on fat_2020.vendedor = investimento.vendedor "

			+ "left join ( "
			+ " select "
			+ "sum(p.vl_totalprod_pedidovenda) as vlfaturado, "
			+ "V.CADCFTVID as vendedor "
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('FATURADO') "
			+ "AND CF.tipooperacao_cfop = 'VENDA' "
			+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2021') "
			+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2  + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "group by V.CADCFTVID ) fat_2021 on fat_2021.vendedor = investimento.vendedor "
			
			+ "left join ( "
			+ "select "
			+ "v.cadcftvid as vendedor, "
			+ "v.NOME_CADCFTV as nome_vendedor , "
			+ "sum(p.vl_totalprod_pedidovenda) as investimento2021  "
			
			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "inner join ( "
			+ "select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ "inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ ") liberado on liberado.pedidovendaid = p.pedidovendaid "
			+ ""
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
			+ "AND CF.tipooperacao_cfop <> 'VENDA' "
			+ ""
			+ "and TO_CHAR(liberado.DT_ROTEIRO_PEDIDO,'YYYY') in ('2021') "
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "and tr.ORDEM_ROTEIRO > 3"
			+ "group by  v.cadcftvid,v.NOME_CADCFTV "
			+ ")investimento2021 on investimento2021.vendedor = investimento.vendedor "		
			
			+ " left join ( "
			+ "select "
			+ "v.cadcftvid as vendedor, "
			+ "v.NOME_CADCFTV as nome_vendedor , "
			+ "sum(p.vl_totalprod_pedidovenda) as investimento2020  "

			+ "from pedidovenda p  "
			+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
			+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
			+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
			+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
			+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
			+ "inner join roteiro r on r.roteiroid = p.roteiroid  "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "inner join ( "
			+ "select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
			+ "inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
			+ "inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
			+ "where tr.ORDEM_ROTEIRO > 3 group by rp.pedidovendaid "
			+ ") liberado on liberado.pedidovendaid = p.pedidovendaid "
			+ ""
			+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
			+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
			+ "AND CF.tipooperacao_cfop <> 'VENDA' "
			+ ""
			+ "and TO_CHAR(liberado.DT_ROTEIRO_PEDIDO,'YYYY') in ('2020') "
			+ " and p.TIPOPEDIDOID in (4,3,5,14,13,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " '  "
			+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " '  "
			+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
			+ "and tr.ORDEM_ROTEIRO > 3"
			+ "group by  v.cadcftvid,v.NOME_CADCFTV "
			+ ")investimento2020 on investimento2020.vendedor = investimento.vendedor"				

			+ " group by  "
			+ " geral.vlgeralfaturado, "
			+ " fat.vlfaturado, "
			+ " investimento.vendedor, "
			+ " investimento.nome_vendedor,fat_2020.vlfaturado,fat_2021.vlfaturado,investimento2021.investimento2021,investimento2020.investimento2020 ");

	List<Object[]> lista = query.getResultList();
								
	for (Object[] row : lista) {
		InvestimentoVendedor vendasEmGeral = new InvestimentoVendedor();
		
		vendasEmGeral.setVendedor((BigDecimal) row[0]);
		vendasEmGeral.setNomevendedor((String) row[1] );
		vendasEmGeral.setVlgeralfaturado((BigDecimal) row[2] );
		vendasEmGeral.setVlvendedorfaturado((BigDecimal) row[3] );
		vendasEmGeral.setVltotalinvestido((BigDecimal) row[4] );
		vendasEmGeral.setPcinvestidovendedor((BigDecimal) row[5] );
		vendasEmGeral.setPcinvestidogeral((BigDecimal) row[6] );
		vendasEmGeral.setVlamostra((BigDecimal) row[7] );
		vendasEmGeral.setPcamostra((BigDecimal) row[8] );
		vendasEmGeral.setVlbonificacao((BigDecimal) row[9] );
		vendasEmGeral.setPcbonificacao((BigDecimal) row[10] );
		vendasEmGeral.setVlexpositor((BigDecimal) row[11] );
		vendasEmGeral.setPcexpositor((BigDecimal) row[12] );
		vendasEmGeral.setVlbrinde((BigDecimal) row[13] );
		vendasEmGeral.setPcbrinde((BigDecimal) row[14] );
		vendasEmGeral.setVltrocanegocio((BigDecimal) row[15] );
		vendasEmGeral.setPctrocanegocio((BigDecimal) row[16] );
		vendasEmGeral.setVlbonificacaoexpositor((BigDecimal) row[17] );
		vendasEmGeral.setPcbonificacaoexpositor((BigDecimal) row[18] );
		
		vendasEmGeral.setVlvendedorfaturado2021((BigDecimal) row[19] );
		vendasEmGeral.setVlvendedorfaturado2020((BigDecimal) row[20] );
		vendasEmGeral.setInvestimento2021((BigDecimal) row[21] );
		vendasEmGeral.setInvestimento2020((BigDecimal) row[22] );
		vendasEmGeral.setPcinvest2021((BigDecimal) row[23] );
		vendasEmGeral.setPcinvest2020((BigDecimal) row[24] );
									
		list.add(vendasEmGeral);
	}

	return list;
}
//investimento modelo 2
public List<VendasEmGeral> investimentoemgeral_2(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
	List<VendasEmGeral> list = new ArrayList<>();
	
	//ajuste de data para bater certo
	Calendar cal = Calendar.getInstance(); 
	cal.setTime(data2); 
	cal.add(Calendar.DATE, 1);
	data2 = cal.getTime();
	
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
					+ " v.NOME_CADCFTV nome_vendedor, "
					+ " liberado.DT_ROTEIRO_PEDIDO "
					+ " from pedidovenda p "
					+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
					+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
					+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
					+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
					+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
					+ " inner join roteiro r on r.roteiroid = p.roteiroid "
					+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
					+ " inner join (select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
					+ " inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
					+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
					+ " where tr.ORDEM_ROTEIRO >3 group by rp.pedidovendaid "
					+ " ) liberado on liberado.pedidovendaid = p.pedidovendaid "
					+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
					+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
					+ " AND CF.tipooperacao_cfop <> 'VENDA' "
					+ " and liberado.DT_ROTEIRO_PEDIDO between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
					+ " and p.TIPOPEDIDOID in (4,3,5,14,13,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
					+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
					+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
					+ " and tr.ORDEM_ROTEIRO > 3 "
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
		vendasEmGeral.setDataliberadogestor((Date) row[10] );
		
		
		
		list.add(vendasEmGeral);
	}

	return list;
}

		//pedidos de bonificaï¿½ï¿½o
public List<VendasEmGeral> investimentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
					List<VendasEmGeral> list = new ArrayList<>();
					
					//ajuste de data para bater certo
					Calendar cal = Calendar.getInstance(); 
					cal.setTime(data2); 
					cal.add(Calendar.DATE, 1);
					data2 = cal.getTime();
					
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
									+ " v.NOME_CADCFTV nome_vendedor, "
									+ " liberado.DT_ROTEIRO_PEDIDO "
									+ " from pedidovenda p "
									+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
									+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
									+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
									+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
									+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
									+ " inner join roteiro r on r.roteiroid = p.roteiroid "
									+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
									+ " inner join (select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
									+ " inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
									+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
									+ " where tr.ORDEM_ROTEIRO >3 group by rp.pedidovendaid "
									+ " ) liberado on liberado.pedidovendaid = p.pedidovendaid "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA' "
									+ " and liberado.DT_ROTEIRO_PEDIDO between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
									+ " and p.TIPOPEDIDOID in (4,3,5,14,13,2,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
									+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
									+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
									+ " and tr.ORDEM_ROTEIRO > 3 "
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
						vendasEmGeral.setDataliberadogestor((Date) row[10] );
						
						
						
						list.add(vendasEmGeral);
					}

					return list;
				}

		//detalhes do pedido de investimento
		public List<VendasEmGeralItem> investimentoemgeralitem(BigDecimal pedido) {
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
									+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
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
									+ " and p.pedidovendaid = ' " + pedido + " ' " 
									+ " and tr.ORDEM_ROTEIRO > 3 "
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
						vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
				}
		
	//pedidos de bonificaï¿½ï¿½o
	public List<VendasEmGeral> bonificacaoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
								+ " and p.TIPOPEDIDOID = 3 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
		
	//detalhes do pedido de bonificaï¿½ï¿½o
	public List<VendasEmGeralItem> bonificacaoemgeralitem(BigDecimal pedido) {
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

	public List<VendasEmGeral> bonificacaoexpositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
						+ " and p.TIPOPEDIDOID = 16 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
						vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
				}

	//pedidos de amostra
		public List<VendasEmGeral> expositoremgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
								+ " and p.TIPOPEDIDOID = 5 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			public List<VendasEmGeralItem> expositoremgeralitem(BigDecimal pedido) {
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

			//pedidos de amostra
			public List<VendasEmGeral> brindeemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
									+ " and p.TIPOPEDIDOID = 14 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
									+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
									+ " AND CF.tipooperacao_cfop <> 'VENDA' "
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
						vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
				}

	//clientes cadastrados no periodo
	public List<ClientesNovos> clientesnovos(Date data1,Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = cl.VENDEDORID1 "
						+ " INNER JOIN cadcftv V ON V.cadcftvID = V2.CADCFTVID "
						
						+ " WHERE c.ATIVO_CADCFTV = 'SIM' "
						+ " and c.DATACREATE_CADCFTV between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
	
	//clientes cadastrados no periodo com venda
	public List<ClientesNovos> clientesnovos_efetivado(Date data1,Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
						+ " c.DATACREATE_CADCFTV data_cadastro, " 
						+ " NVL(vendas.vendas,0) VENDAS "
						
						+ " from cadcftv c "
						+ " inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = cl.VENDEDORID1 "
						+ " INNER JOIN cadcftv V ON V.cadcftvID = V2.CADCFTVID "
						
						+ " left join( "
						+ " select "
						+ " p.cadcftvid, "
						+ " count(p.pedidovendaid) vendas "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID " 
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') " 
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " group by p.cadcftvid "
						+ " ) vendas on vendas.cadcftvid = c.cadcftvid "
						
						+ " WHERE c.ATIVO_CADCFTV = 'SIM' "
						+ " and c.DATACREATE_CADCFTV between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
			clientesNovos.setVendas((BigDecimal) row[7] );
			
			list.add(clientesNovos);
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
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = V.CADCFTVID "						
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' "
						+ " order by v.NOME_CADCFTV ");

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
	
public List<Cliente> consultacliente(String palavra) {
		List<Cliente> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " V.CADCFTVID cliente, " 
						+ " v.NOME_CADCFTV nome_cliente, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
						//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "						
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or v.cadcftvid like '%"+palavra+"%' or v.CNPJCPF_CADCFTV like '"+palavra+"%' ) "
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
	
	public List<Gestor> consultagestor(String vendedor1, String vendedor2) {
		List<Gestor> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" select GR.CADCFTVID, G.NOME_GESTOR, g.gestorid from  vendedor v  " 
						+ " inner join gestor g on g.gestorid = v.gestorid  "
						+ " left join CADCFTV GR ON GR.CNPJCPF_CADCFTV = G.CNPJ_GESTOR OR GR.CNPJCPF_CADCFTV = G.CPF_GESTOR  "
						+ " where g.status_gestor = 'ATIVO' and GR.ATIVO_CADCFTV = 'SIM' AND v.CADCFTVID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " group by GR.CADCFTVID, G.NOME_GESTOR, g.gestorid  ");

		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			Gestor gestor = new Gestor();

			gestor.setCodigogestor((BigDecimal) row[0]);
			gestor.setNomegestor((String) row[1]);			
			gestor.setGestorid((BigDecimal) row[2]);
			list.add(gestor);
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
			vendasEmGeralItem.setImagem((Blob) row2[17] );
			
		}
		return vendasEmGeralItem;
	}
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2,String ano, String mes) {
		List<MetaVenda> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT TIPO,MES,ANO,VALOR, CASE WHEN NOME_REGIAO is null THEN 'VAZIO' ELSE NOME_REGIAO END AS NOME_REGIAO FROM ( "
				+ "	select  "
				+ "	'META' AS TIPO, "
				+ "	MV.MES_METAVENDEDOR MES, "
				+ "	MV.ANO_METAVENDEDOR ANO, "
				+ "	SUM(MV.VALOR_METAVENDEDOR)VALOR, "
				+ "	RE.NOME_REGIAO "
				+ "	from CADCFTV V "
				+ "	left JOIN meta_vendedor mv  ON V.CADCFTVID = mv.CADCFTVID "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				//+ "	INNER JOIN CADCFTV GR ON GR.CADCFTVID = " + usuarioconectado()
				//+ "	INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
				+ "	INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = V.CADCFTVID  "
				+ "	WHERE mv.MES_METAVENDEDOR = "+mes//TO_NUMBER(TO_CHAR(SYSDATE,'MM')) "
				+ "	and mv.ANO_METAVENDEDOR = "+ano//TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+ "	and v.ATIVO_CADCFTV = 'SIM' "
				+ " and v2.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ "	GROUP BY "
				+ "	MV.MES_METAVENDEDOR, "
				+ "	MV.ANO_METAVENDEDOR, "
				+ "	RE.NOME_REGIAO  "
				+ "	UNION ALL  "
				+ "	SELECT "
				+ "	'VENDA' TIPO, "
				+ "	TO_NUMBER(TO_CHAR(EN.DT_PEDIDOVENDA,'MM')) AS MES, "
				+ "	TO_NUMBER(TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY')) AS ANO, "
				+ "	SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALOR, "
				+ "	RE.NOME_REGIAO "
				+ "	FROM PEDIDOVENDA EN  "
				+ "	INNER JOIN VENDEDOR V ON V.CADCFTVID = EN.VENDEDOR1ID  "
				//+ "	INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				//+ "	INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV  "
				+ "	INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID  "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				+ "	WHERE CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ "	AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
				+ " and v.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = "+mes//TO_CHAR(SYSDATE,'MM') "
				+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = "+ano//TO_CHAR(SYSDATE,'YYYY') "
				+ "	GROUP BY  "
				+ "	TO_NUMBER(TO_CHAR(EN.DT_PEDIDOVENDA,'MM')), "
				+ "	TO_NUMBER(TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY')), "
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
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano, String mes) {
		List<VendedorMetaVenda> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" SELECT EN.VENDEDOR1ID ,VEND.NOME_CADCFTV,SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALORVENDA,MV.VALOR_METAVENDEDOR VALORMETA, "
				+" (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 as atingidometa, "
				+" case when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 50 then 'red' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 50 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 70 then 'orange' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 70 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 100 then 'blue' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 100 then 'green' end as cordacoluna "
				+" FROM VENDEDOR V   "
				+" left JOIN PEDIDOVENDA EN ON V.CADCFTVID = EN.VENDEDOR1ID  "
				+" INNER JOIN CADCFTV VEND ON VEND.CADCFTVID = V.CADCFTVID "
				//+"INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				//+"INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV   "
				+" INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID   "
				+" LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID  "
				+" LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID  "
				+" left JOIN meta_vendedor mv ON mv.CADCFTVID = VEND.CADCFTVID "
				+" WHERE CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+" and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and v.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+" AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL')  "
				//+"AND V.GESTORID = G.GESTORID   "
				+" AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = "+mes //TO_CHAR(SYSDATE,'MM')  "
				+" AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = "+ano //TO_CHAR(SYSDATE,'YYYY')  "
				+" AND mv.MES_METAVENDEDOR = "+mes //TO_NUMBER(TO_CHAR(SYSDATE,'MM'))  "
				+" and mv.ANO_METAVENDEDOR = "+ano //TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+" GROUP BY EN.VENDEDOR1ID ,VEND.NOME_CADCFTV, mv.VALOR_METAVENDEDOR ");
				
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			VendedorMetaVenda vendametavenda = new VendedorMetaVenda();

			vendametavenda.setCodigovendedor((BigDecimal) row[0]);
			vendametavenda.setNomevendedor((String) row[1]);
			vendametavenda.setValorvenda((BigDecimal) row[2]);
			vendametavenda.setValormeta((BigDecimal) row[3]);
			vendametavenda.setAtingidometa((BigDecimal) row[4]);
			vendametavenda.setCordacoluna((String) row[5]);
			list.add(vendametavenda);
		}
		return list;
		
	}
	
	//FATURAMENTO de venda
		public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
							//+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							//+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							//+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('FATURADO') "
							+ " AND CF.tipooperacao_cfop = 'VENDA' "
							//+ " AND t.desc_tipo_pedido = 'VENDA' "
							+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
							+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
							+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
	
public List<AnaliseClientePedido> analiseclientepedido(Date data1, Date data2, BigDecimal cliente, String cnpj, BigDecimal pedido) {
	List<AnaliseClientePedido> list = new ArrayList<>();
	
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataFormatada = formato.format(data1);
	String dataFormatada2 = formato.format(data2);
	
	SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
	String SdataFormatada = formato2.format(data1);
	String SdataFormatada2 = formato2.format(data2);
	
	javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
			     " select "
				+" c.CADCFTVID CLIENTE, "
				+" c.NOME_CADCFTV, "
				+" PEDIDO.PEDIDOVENDAID as PEDIDOINDIVIDUAL, "
				+" PEDIDO.VLPEDIDO, "
				+" PEDIDO.tipo_pedido, "
				+" PEDIDO.FASE_ATUAL, "
				+" PEDIDO.STATUS, "
				+" PEDIDOS.PEDIDOVENDAID, "
				+" NVL(PEDIDOS.VL_VENDA,0) VL_VENDA, "
				+" NVL(PEDIDOS.VL_AMOSTRA,0) VL_AMOSTRA, "
				+" NVL(PEDIDOS.VL_AMOSTRAPAGA,0) VL_AMOSTRAPAGA, "
				+" NVL(PEDIDOS.VL_BONIFICACAO,0) VL_BONIFICACAO, "
				+" NVL(PEDIDOS.VL_EXPOSITOR,0) VL_EXPOSITOR, "
				+" NVL(PEDIDOS.VL_BRINDE,0) VL_BRINDE, "
				+" NVL(PEDIDOS.VL_TROCA,0) VL_TROCA, "
				+" NVL(PEDIDOS.VL_NEGOCIACOESCOMERCIAIS,0) VL_NEGOCIACOESCOMERCIAIS, "
				+" PEDIDOS.STATUS_PEDIDOVENDA, c.cnpjcpf_cadcftv as cpfcnpj, PEDIDOS.DT, "
				+" NVL(PEDIDOS.VL_BONIFICACAOEXPOSITOR,0) VL_BONIFICACAOEXPOSITOR "
				+" from cadcftv c "
				+" inner join cliente cl on cl.CADCFTVID = c.CADCFTVID "
				+" LEFT JOIN( "
				+" SELECT  "
				+" P.CADCFTVID, "
				+" P.PEDIDOVENDAID, "
				+" P.VL_TOTALPROD_PEDIDOVENDA as VLPEDIDO, "
				+" P.DT_PEDIDOVENDA , "
				+" p.STATUS_PEDIDOVENDA as STATUS, "
				+" t.desc_tipo_pedido as tipo_pedido, "
				+" ro.DESC_ROTEIRO as fase_atual "
				+" FROM PEDIDOVENDA P "
				+" inner join roteiro ro on ro.ROTEIROID = p.roteiroid  "
				+" inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
				+" WHERE p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+" )PEDIDO ON PEDIDO.CADCFTVID = C.CADCFTVID "
				+" AND PEDIDO.PEDIDOVENDAID = " + pedido
				+" LEFT JOIN( "
				+" SELECT "
				+" P.CADCFTVID, "
				+" P.PEDIDOVENDAID, P.DT_PEDIDOVENDA AS DT,"
				+" p.STATUS_PEDIDOVENDA, "
				+" case when CF.tipooperacao_cfop = 'VENDA' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_venda,  "
				+" case when p.tipopedidoid  = 4 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostra,  "
				+" case when p.tipopedidoid  = 6 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostrapaga,  "
				+" case when p.tipopedidoid  = 3 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacao,  "
				+" case when p.tipopedidoid  = 16 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacaoexpositor,  "
				+" case when p.tipopedidoid  = 5 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_expositor,  "
				+" case when p.tipopedidoid  = 14 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_brinde,  "
				+" case when p.tipopedidoid  = 2 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_troca,  "
				+" case when p.tipopedidoid  = 13 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_negociacoescomerciais  "
				+" FROM PEDIDOVENDA P INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+" WHERE p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+" AND P.PEDIDOVENDAID <> " + pedido
				+" )PEDIDOS ON PEDIDOS.CADCFTVID = c.CADCFTVID "
				+" AND PEDIDOS.DT BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+" where c.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
				+" AND C.CADCFTVID = " + cliente
				+" ORDER BY PEDIDOS.PEDIDOVENDAID ");
	List<Object[]> lista = query.getResultList();
	if(lista.size()>0){
	for (Object[] row : lista) {
		
		AnaliseClientePedido analiseClientePedido = new AnaliseClientePedido();
		
		analiseClientePedido.setCodigocliente((BigDecimal) row[0]);
		analiseClientePedido.setNomecliente((String) row[1]);
		
		analiseClientePedido.setPedidoindividual((BigDecimal) row[2] );
		analiseClientePedido.setVlpedido((BigDecimal) row[3] );
		analiseClientePedido.setTipopedido((String) row[4]);
		analiseClientePedido.setFaseatual((String) row[5]);
		analiseClientePedido.setStatus((String) row[6]);
		
		analiseClientePedido.setOrigem("SEVEN");
		analiseClientePedido.setPedido((BigDecimal) row[7] );
		analiseClientePedido.setVlvenda((BigDecimal) row[8] );
		analiseClientePedido.setVlamostra((BigDecimal) row[9] );
		analiseClientePedido.setVlamostrapaga((BigDecimal) row[10] );
		analiseClientePedido.setVlbonificacao((BigDecimal) row[11] );
		analiseClientePedido.setVlexpositor((BigDecimal) row[12] );
		analiseClientePedido.setVlbrinde((BigDecimal) row[13] );
		analiseClientePedido.setVltroca((BigDecimal) row[14] );
		analiseClientePedido.setVlnegociacoescomerciais((BigDecimal) row[15] );
		analiseClientePedido.setStatuspedido((String) row[16]);
		analiseClientePedido.setDatapedido((Date) row[18] );
		analiseClientePedido.setVlbonificacaoexpositor((BigDecimal) row[19] );
		
		list.add(analiseClientePedido);
	}}
	
	javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
			"select s.Cod_cli_for as cod_cliente, g.Nome_cadastro, s.Num_docto as pedido, "
			+" case when p.Cod_tipo_mv = 510 then cast(s.Valor_liquido as money) else 0 end as sige_valor_venda,   "
			+" case when p.Cod_tipo_mv = 516 then cast(s.Valor_liquido as money) else 0 end as sige_valor_amostra,   "
			+" case when p.Cod_tipo_mv = 512 then cast(s.Valor_liquido as money) else 0 end as sige_valor_bonificacao,  " 
			+" case when p.Cod_tipo_mv = 517 then cast(s.Valor_liquido as money) else 0 end as sige_valor_expositor,   "
			+" case when p.Cod_tipo_mv = 515 then cast(s.Valor_liquido as money) else 0 end as sige_valor_troca  , 'Fechado' as Status , p.Data_v1 "
			+" from tbsaidas s left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un  "
			+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
			+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
			+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
			+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
			+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
			+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('510','512','515','516','517')  "
			+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "

			+" and (s.Cod_cli_for = " + cliente + " or g.Cpf_Cgc = '" + cnpj + "') "
			+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ' ");
	List<Object[]> listasige = querySige.getResultList();
	if(listasige.size()>0){
	for (Object[] rowsige : listasige) {
		AnaliseClientePedido analiseClientePedido = new AnaliseClientePedido();
		
		int scliente = (Integer) rowsige[0];
		int spedido = (Integer) rowsige[2];
		
		analiseClientePedido.setCodigocliente(new BigDecimal(scliente));
		analiseClientePedido.setNomecliente((String) rowsige[1]);
		
		analiseClientePedido.setPedidoindividual(new BigDecimal(0));
		analiseClientePedido.setVlpedido(new BigDecimal(0));
		analiseClientePedido.setTipopedido("");
		analiseClientePedido.setFaseatual("");
		analiseClientePedido.setStatus("");
		
		analiseClientePedido.setOrigem("SIGE");
		analiseClientePedido.setPedido(new BigDecimal(spedido));
		analiseClientePedido.setVlvenda((BigDecimal) rowsige[3] );
		analiseClientePedido.setVlamostra((BigDecimal) rowsige[4] );
		analiseClientePedido.setVlamostrapaga(new BigDecimal(0) );
		analiseClientePedido.setVlbonificacao((BigDecimal) rowsige[5] );
		analiseClientePedido.setVlexpositor((BigDecimal) rowsige[6] );
		analiseClientePedido.setVlbrinde(new BigDecimal(0) );
		analiseClientePedido.setVltroca((BigDecimal) rowsige[7] );
		analiseClientePedido.setVlnegociacoescomerciais(new BigDecimal(0) );
		analiseClientePedido.setStatuspedido((String) rowsige[8]);
		analiseClientePedido.setDatapedido((Date) rowsige[9] );
		analiseClientePedido.setVlbonificacaoexpositor(new BigDecimal(0));
		
		list.add(analiseClientePedido);	
	}}
	
	return list;
}
	//pedidoitem seven e sige
	public List<PedidoItem> pedidoitem(BigDecimal pedido) {
		List<PedidoItem> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select " 
				+" t.desc_tipo_pedido as tipo_pedido, "
				+" ro.DESC_ROTEIRO fase_atual, "
				+" p.STATUS_PEDIDOVENDA , "
				+" p.pedidovendaid, "
				+" cli.cadcftvid codcliente, "
				+" cli.NOME_CADCFTV nomecliente, "
				+" cli.cnpjcpf_cadcftv cpfcnpj, "

				+" it.produtoid, "
				+" it.DS_PRODUTO_PEDIDOVENDA_ITEM, "
				+" it.QT_PEDIDOVENDA_ITEM qtde, "
				+" pr.referencia_original_produto ean , "

				+" case when t.desc_tipo_pedido = 'VENDA' then hist.qtde_venda - it.QT_PEDIDOVENDA_ITEM else hist.qtde_venda end as qtde_venda, "
				+" case when t.desc_tipo_pedido = 'AMOSTRA' then hist.qtde_amostra - it.QT_PEDIDOVENDA_ITEM else hist.qtde_amostra end as qtde_amostra, "
				+" case when t.desc_tipo_pedido = 'AMOSTRA PAGAS' then hist.qtde_amostrapaga - it.QT_PEDIDOVENDA_ITEM else hist.qtde_amostrapaga end as qtde_amostrapaga, "
				+" case when t.desc_tipo_pedido = 'BONIFICACAO' then hist.qtde_bonificacao - it.QT_PEDIDOVENDA_ITEM else hist.qtde_bonificacao end as qtde_bonificacao, "
				+" case when t.desc_tipo_pedido = 'EXPOSITOR' then hist.qtde_expositor - it.QT_PEDIDOVENDA_ITEM else hist.qtde_expositor end as qtde_expositor, "
				+" case when t.desc_tipo_pedido = 'MERCADORIA COM DEFEITO' then hist.qtde_troca - it.QT_PEDIDOVENDA_ITEM else hist.qtde_troca end as qtde_troca, "
				+" case when t.desc_tipo_pedido = 'NEGOCIAÇÕES COMERCIAIS' then hist.qtde_negociacoescomerciais - it.QT_PEDIDOVENDA_ITEM else hist.qtde_negociacoescomerciais end as qtde_negociacoescomerciais, "
				+" 0 sige_qtde_venda, "
				+" 0 sige_qtde_amostra, "
				+" 0 sige_qtde_bonificacao, "
				+" 0 sige_qtde_expositor, "
				+" 0 sige_qtde_troca, "
				
				+" ACUMULADO.VL_venda, "
				+" ACUMULADO.VL_amostra, "
				+" ACUMULADO.VL_amostrapaga, "
				+" ACUMULADO.VL_bonificacao, " 
				+" ACUMULADO.VL_expositor, " 
				+" ACUMULADO.VL_troca, "
				+" ACUMULADO.VL_negociacoescomerciais, "
				
				+" p.PC_COMISSAO1_PEDIDOVENDA percentual_comissao, "
				+" lucro.lucropedido, "
				
				+" case when t.desc_tipo_pedido = 'BONIFICACAO PARA EXPOSITOR' then hist.qtde_bonificacaoexpositor - it.QT_PEDIDOVENDA_ITEM else hist.qtde_bonificacaoexpositor end as qtde_bonificacaoexpositor, "
				+" ACUMULADO.VL_bonificacaoexpositor " 

				+" from pedidovenda p "
				+" inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid "
				+" inner join produto pr on pr.produtoid = it.produtoid "
				+" inner join cadcftv cli on cli.cadcftvid = p.CADCFTVID "
				+" inner join roteiro ro on ro.ROTEIROID = p.roteiroid "
				+" inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
				
				+" inner join( "
				+" select "
				+" it.pedidovendaid, "
				+" sum(IT.vl_total_pedidovenda_item) totalitem, "
				+" sum((pr.vl_custo_produto * it.qt_pedidovenda_item )) custo_total, "
				+" sum(IT.vl_total_pedidovenda_item)-sum((pr.vl_custo_produto * it.qt_pedidovenda_item )) bruto, "
				+" TRUNC(((sum(IT.vl_total_pedidovenda_item)-sum((pr.vl_custo_produto * it.qt_pedidovenda_item )))/sum(IT.vl_total_pedidovenda_item))*100,2) lucropedido "
				+" from pedidovenda_item it "
				+" inner join produto pr on pr.produtoid = it.produtoid "
				+" group by it.pedidovendaid "
				+" )lucro on lucro.pedidovendaid = p.pedidovendaid "

				+" left join( "
				+" select resumo.cadcftvid, resumo.produtoid, "
				+" sum(resumo.qtde_venda)qtde_venda, "
				+" sum(resumo.qtde_amostra)qtde_amostra, "
				+" sum(resumo.qtde_amostrapaga)qtde_amostrapaga, "
				+" sum(resumo.qtde_bonificacao)qtde_bonificacao, "
				+" sum(resumo.qtde_bonificacaoexpositor)qtde_bonificacaoexpositor, "
				+" sum(resumo.qtde_expositor)qtde_expositor, "
				+" sum(resumo.qtde_troca)qtde_troca, "
				+" sum(resumo.qtde_negociacoescomerciais)qtde_negociacoescomerciais "
				+" from ( "
				+" select p.cadcftvid, it.produtoid, "
				+" case when p.tipopedidoid  = 1 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_venda, "
				+" case when p.tipopedidoid  = 4 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_amostra, "
				+" case when p.tipopedidoid  = 6 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_amostrapaga, "
				+" case when p.tipopedidoid  = 3 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_bonificacao, "
				+" case when p.tipopedidoid  = 16 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_bonificacaoexpositor, "
				+" case when p.tipopedidoid  = 5 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_expositor, "
				+" case when p.tipopedidoid  = 2 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_troca, "
				+" case when p.tipopedidoid  = 13 then it.QT_PEDIDOVENDA_ITEM else 0 end as qtde_negociacoescomerciais "

				+" from pedidovenda_item it "
				+" inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "

				+" where  p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+" )resumo group by  resumo.cadcftvid, resumo.produtoid) hist  "
				+" on hist.cadcftvid = p.cadcftvid and hist.produtoid = it.produtoid "
				
				+"LEFT JOIN( "
				+" SELECT X.CLIENTE, sum(X.VL_venda)VL_venda, sum(X.VL_amostra)VL_amostra, sum(X.VL_amostrapaga)VL_amostrapaga,  "
				+" sum(X.VL_bonificacao)VL_bonificacao, sum(X.VL_bonificacaoexpositor)VL_bonificacaoexpositor ,sum(X.VL_expositor)VL_expositor, sum(X.VL_troca)VL_troca,  "
				+" sum(X.VL_negociacoescomerciais)VL_negociacoescomerciais FROM(select CI.CADCFTVID AS CLIENTE,  "
				+" case when CF.tipooperacao_cfop = 'VENDA' then p.vl_totalprod_pedidovenda  else 0 end as VL_venda,  "
				+" case when p.tipopedidoid  = 4 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_amostra,  "
				+" case when p.tipopedidoid  = 6 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_amostrapaga, " 
				+" case when p.tipopedidoid  = 3 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_bonificacao,  "
				+" case when p.tipopedidoid  = 16 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_bonificacaoexpositor,  "
				+" case when p.tipopedidoid  = 5 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_expositor,  "
				+" case when p.tipopedidoid  = 2 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_troca,  "
				+" case when p.tipopedidoid  = 13 and CF.tipooperacao_cfop <> 'VENDA' then p.vl_totalprod_pedidovenda else 0 end as VL_negociacoescomerciais  "
				+" from pedidovenda p INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+" where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')) X GROUP BY X.CLIENTE)ACUMULADO "
				+" ON ACUMULADO.CLIENTE = p.cadcftvid "

				+" where p.pedidovendaid= " +pedido);
				
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			PedidoItem pedidoitem = new PedidoItem();
			
			pedidoitem.setTipopedido((String) row[0]);
			pedidoitem.setFaseatual((String) row[1]);
			pedidoitem.setStatuspedido((String) row[2]);
			pedidoitem.setPedido((BigDecimal) row[3]);
			pedidoitem.setCodigocliente((BigDecimal) row[4]);
			pedidoitem.setNomecliente((String) row[5]);
			pedidoitem.setCpfcnpj((String) row[6]);
			
			pedidoitem.setCodigoproduto((BigDecimal) row[7]);
			pedidoitem.setNomeproduto((String) row[8]);
			pedidoitem.setQtdepedido((BigDecimal) row[9]);
			pedidoitem.setEan((String) row[10]);
			
			pedidoitem.setQtdevenda((BigDecimal) row[11]);
			pedidoitem.setQtdeamostra((BigDecimal) row[12]);
			pedidoitem.setQtdeamostrapaga((BigDecimal) row[13]);
			pedidoitem.setQtdebonificacao((BigDecimal) row[14]);
			pedidoitem.setQtdeexpositor((BigDecimal) row[15]);
			pedidoitem.setQtdetroca((BigDecimal) row[16]);
			pedidoitem.setQtdenegociacoescomerciais((BigDecimal) row[17]);
			
			pedidoitem.setVlvenda((BigDecimal) row[23]);
			pedidoitem.setVlamostra((BigDecimal) row[24]);
			pedidoitem.setVlamostrapaga((BigDecimal) row[25]);
			pedidoitem.setVlbonificacao((BigDecimal) row[26]);
			pedidoitem.setVlexpositor((BigDecimal) row[27]);
			pedidoitem.setVltroca((BigDecimal) row[28]);
			pedidoitem.setVlnegociacoescomerciais((BigDecimal) row[29]);
			
			pedidoitem.setPc_comissao((BigDecimal) row[30]);
			pedidoitem.setPc_lucro_visao14((BigDecimal) row[31]);
			
			pedidoitem.setQtdebonificacaoexpositor((BigDecimal) row[32]);
			pedidoitem.setVlbonificacaoexpositor((BigDecimal) row[33]);
			
			//sige
			String cliente =  String.valueOf((BigDecimal) row[4]);
			String produto = String.valueOf((BigDecimal) row[7]);
			String cnpj = String.valueOf((String) row[6]);
			String ean = String.valueOf((String) row[10]);
			
			javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
			" select x.Cod_cadastro,x.Cpf_Cgc,x.Cod_produto,x.Ean, "
			+" isnull(sum(x.sige_qtde_venda),0) sige_qtde_venda, "
			+" isnull(sum(x.sige_qtde_amostra),0) sige_qtde_amostra, "
			+" isnull(sum(x.sige_qtde_bonificacao),0) sige_qtde_bonificacao, "
			+" isnull(sum(x.sige_qtde_expositor),0) sige_qtde_expositor, "
			+" isnull(sum(x.sige_qtde_troca),0) sige_qtde_troca, "
			+" resumo.sige_valor_venda, resumo.sige_valor_amostra, "
			+" resumo.sige_valor_bonificacao,resumo.sige_valor_expositor, resumo.sige_valor_troca from( "
			+" select g.cod_cadastro,g.Cpf_Cgc,s.* from tbCadastroGeral g left join( "
			+" select g.Cod_cadastro c1 ,g.Cpf_Cgc cp,it.Cod_produto,r.ean, "
			+" case when s.Cod_tipo_mv = 510 then it.Qtde_pri else 0 end as sige_qtde_venda, "
			+" case when s.Cod_tipo_mv = 516 then it.Qtde_pri else 0 end as sige_qtde_amostra, "
			+" case when s.Cod_tipo_mv = 512 then it.Qtde_pri else 0 end as sige_qtde_bonificacao, "
			+" case when s.Cod_tipo_mv = 517 then it.Qtde_pri else 0 end as sige_qtde_expositor, "
			+" case when s.Cod_tipo_mv = 515 then it.Qtde_pri else 0 end as sige_qtde_troca "
			+" from tbSaidas s inner join tbSaidasItem it on it.chave_fato = s.chave_fato "
			+" inner join tbproduto p on p.cod_produto = it.cod_produto "
			+" inner join tbprodutoref r on r.cod_produto = p.cod_produto and r.cod_ref = 0 "
			+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
			+" inner join tbsaidas exp on exp.Chave_fato_orig_un = s.Chave_fato "

			+" where  (it.cod_produto = " + produto + " or r.ean = '" + ean + "') "
			
			+" and s.Cod_tipo_mv in ('510','512','515','516','517') "
			+"  ) s on s.c1 = g.Cod_Cadastro where (g.Cod_cadastro = " + cliente + " or g.Cpf_Cgc = '" + cnpj + "')  )x  "
			
			+" inner join(select x.Cod_cliente, "
			+" sum(x.sige_valor_venda) sige_valor_venda,  "
			+" sum(x.sige_valor_amostra) sige_valor_amostra,  "
			+" sum(x.sige_valor_bonificacao) sige_valor_bonificacao,  "
			+" sum(x.sige_valor_expositor) sige_valor_expositor,  "
			+" sum(x.sige_valor_troca) sige_valor_troca from ( "
			+" select s.Cod_cli_for as cod_cliente, "
			+" case when p.Cod_tipo_mv = 510 then cast(s.Valor_liquido as money) else 0 end as sige_valor_venda,  "
			+" case when p.Cod_tipo_mv = 516 then cast(s.Valor_liquido as money) else 0 end as sige_valor_amostra,  "
			+" case when p.Cod_tipo_mv = 512 then cast(s.Valor_liquido as money) else 0 end as sige_valor_bonificacao,  "
			+" case when p.Cod_tipo_mv = 517 then cast(s.Valor_liquido as money) else 0 end as sige_valor_expositor,  "
			+" case when p.Cod_tipo_mv = 515 then cast(s.Valor_liquido as money) else 0 end as sige_valor_troca  "
			+" from tbsaidas s left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un "
			+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv "
			+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2  "
			+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato "
			+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave "
			+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('510','512','515','516','517') "
			+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S')x group by x.cod_cliente)resumo on resumo.cod_cliente = x.Cod_cadastro "

			+" group by x.Cod_cadastro, x.Cpf_Cgc, x.Cod_produto,x.ean,resumo.sige_valor_venda, resumo.sige_valor_expositor, "
			+" resumo.sige_valor_amostra,resumo.sige_valor_bonificacao, resumo.sige_valor_troca ");

			List<Object[]> listasige = querySige.getResultList();
			
			for (Object[] rowsige : listasige) {
				//inserir dados do sige
				BigDecimal venda = (BigDecimal) rowsige[4];
				BigDecimal amostra = (BigDecimal) rowsige[5];
				BigDecimal bonificacao = (BigDecimal) rowsige[6];
				BigDecimal expositor = (BigDecimal) rowsige[7];
				BigDecimal troca = (BigDecimal) rowsige[8];
				
				BigDecimal vl_venda = (BigDecimal) rowsige[9];	
				BigDecimal vl_venda2 = vl_venda.setScale(2, RoundingMode.FLOOR);
				
				BigDecimal vl_amostra = (BigDecimal) rowsige[10];
				BigDecimal vl_amostra2 = vl_amostra.setScale(2, RoundingMode.FLOOR);
				BigDecimal vl_bonificacao = (BigDecimal) rowsige[11];
				BigDecimal vl_bonificacao2 = vl_bonificacao.setScale(2, RoundingMode.FLOOR);
				BigDecimal vl_expositor = (BigDecimal) rowsige[12];
				BigDecimal vl_expositor2 = vl_expositor.setScale(2, RoundingMode.FLOOR);
				BigDecimal vl_troca = (BigDecimal) rowsige[13];
				BigDecimal vl_troca2 = vl_troca.setScale(2, RoundingMode.FLOOR);
				
				pedidoitem.setSige_qtde_venda(venda.intValueExact());
				pedidoitem.setSige_qtde_amostra(amostra.intValueExact());
				pedidoitem.setSige_qtde_bonificacao(bonificacao.intValueExact());
				pedidoitem.setSige_qtde_expositor(expositor.intValueExact());
				pedidoitem.setSige_qtde_troca(troca.intValueExact());
				
				pedidoitem.setSige_vl_venda(vl_venda2.intValue());
				pedidoitem.setSige_vl_amostra(vl_amostra2.intValue());
				pedidoitem.setSige_vl_bonificacao(vl_bonificacao2.intValue());
				pedidoitem.setSige_vl_expositor(vl_expositor2.intValue());
				pedidoitem.setSige_vl_troca(vl_troca2.intValue());
			}
			
			list.add(pedidoitem);
		}
		
		Collections.sort(list, Comparator.comparing(PedidoItem::getSige_vl_venda, Comparator.nullsLast(Integer::compareTo)));
		return list;
		
	}
	
	//pedidos conferidos por periodo
	
	public List<PedidosConferidos> pedidosconferidos(Date data1, Date data2){
		List<PedidosConferidos> list = new ArrayList<>();
		
		data2.setDate(data2.getDate()+1);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select " + 
				" p.DT_PEDIDOVENDA data_pedido, " + 
				" p.pedidovendaid pedido, " + 
				" g.cadcftvid, " + 
				" g.NOME_CADCFTV, " + 
				" TP.DESC_TIPO_PEDIDO TIPO_PEDIDO, " + 
				" p.VL_TOTALPROD_PEDIDOVENDA vlr, " + 
				" p.VL_PESOLIQ_PEDIDOVENDA peso , " + 
				" p.QT_VOLUME_PEDIDOVENDA volumes, " + 
				" INICIO.DT_INICIO, " + 
				" FIM.DT_FIM, " + 
				" u.NOME_USUARIO " + 
				" from pedidovenda p " + 
				" inner join cadcftv g on g.cadcftvid = p.cadcftvid " + 
				" left join V_PEDIDOVENDA_CONFERENCIA it on it.pedidovendaid = p.pedidovendaid " + 
				" INNER JOIN TIPO_PEDIDO TP ON TP.TIPOPEDIDOID = p.TIPOPEDIDOID " + 
				" left JOIN ( " + 
				" SELECT PEDIDOVENDAID ,MIN(TO_CHAR(DT_INI_ENTGA_PEDIDOVENDA_ITEM,'DD/MM/YYYY HH24:MI:SS' )) DT_INICIO FROM PEDIDOVENDA_ITEM " + 
				" GROUP BY PEDIDOVENDAID " + 
				" )INICIO ON INICIO.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" left JOIN ( " + 
				" SELECT PEDIDOVENDAID ,MAX(TO_CHAR(DT_FIM_ENTGA_PEDIDOVENDA_ITEM,'DD/MM/YYYY HH24:MI:SS' )) DT_FIM FROM PEDIDOVENDA_ITEM " + 
				" GROUP BY PEDIDOVENDAID " + 
				" )FIM ON FIM.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" left JOIN( " + 
				" SELECT PEDIDOVENDAID ,MAX(USUARIOID_DESPACHO ) USUARIO FROM PEDIDOVENDA_ITEM " +  
				" GROUP BY PEDIDOVENDAID " + 
				" )USUARIO ON USUARIO.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" INNER JOIN USUARIO U ON U.USUARIOID = USUARIO.USUARIO " +
				" left join( " +
				" select pedidovendaid ,DT_INI_ENTGA_PEDIDOVENDA_ITEM data_i from pedidovenda_item " +
				" )dt on dt.pedidovendaid = p.pedidovendaid and TO_CHAR(dt.data_i,'DD/MM/YYYY HH24:MI:SS' ) = INICIO.DT_INICIO " +
				" left join( " +
				" select pedidovendaid ,DT_FIM_ENTGA_PEDIDOVENDA_ITEM data_f from pedidovenda_item " +
				" )dt2 on dt2.pedidovendaid = p.pedidovendaid and TO_CHAR(dt2.data_f,'DD/MM/YYYY HH24:MI:SS' ) = FIM.DT_FIM " +
				" where IT.CONFERIDO = 'SIM' " +
				" and p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "+
				" and dt.data_i BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " '  "+
				" and dt2.data_f BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " );
				
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			PedidosConferidos pedidosconferido = new PedidosConferidos();	
			
			pedidosconferido.setDatapedido((Date) row[0] );
			pedidosconferido.setPedido((BigDecimal) row[1]);
			pedidosconferido.setCodigocliente((BigDecimal) row[2]);
			pedidosconferido.setNomecliente((String) row[3]);
			pedidosconferido.setTipopedido((String) row[4]);
			pedidosconferido.setVlrpedido((BigDecimal) row[5]);
			pedidosconferido.setPeso((BigDecimal) row[6]);
			pedidosconferido.setVolume((BigDecimal) row[7]);
			
			pedidosconferido.setDatainicio((String) row[8] );
			pedidosconferido.setDatafim((String) row[9] );
			pedidosconferido.setUsuario((String) row[10]);
			
			list.add(pedidosconferido);
		}
		
		return list;
	}

	//dados cliente
public List<DadosCliente> dadoscliente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
	List<DadosCliente> list = new ArrayList<>();
		
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataFormatada = formato.format(data1);
	String dataFormatada2 = formato.format(data2);
	
	SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
	String SdataFormatada = formato2.format(data1);
	String SdataFormatada2 = formato2.format(data2);

	javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
	 " select "			
	+" v.cadcftvid vendedor, "
	+" v.NOME_CADCFTV nome_vendedor, "
	+" c.CADCFTVID CLIENTE,  "
 	+" c.NOME_CADCFTV nome_cliente, "
 	+" c.cnpjcpf_cadcftv as cpfcnpj, "
 	+" en.END_ENDCADCFTV endereco, "
 	+" en.nome_cidade, "
 	+" en.CEP_ENDCADCFTV, "
 	+" en.uf_cidade, "
 
 	+" NVL(PEDIDOS.VL_VENDA,0) VL_VENDA, " 
 	+" NVL(PEDIDOS.VL_AMOSTRA,0) VL_AMOSTRA, " 
 	+" NVL(PEDIDOS.VL_AMOSTRAPAGA,0) VL_AMOSTRAPAGA, " 
 	+" NVL(PEDIDOS.VL_BONIFICACAO,0) VL_BONIFICACAO, " 
 	+" NVL(PEDIDOS.VL_EXPOSITOR,0) VL_EXPOSITOR, " 
 	+" NVL(PEDIDOS.VL_BRINDE,0) VL_BRINDE, " 
 	+" NVL(PEDIDOS.VL_TROCA,0) VL_TROCA, " 
 	+" NVL(PEDIDOS.VL_NEGOCIACOESCOMERCIAIS,0) VL_NEGOCIACOESCOMERCIAIS, "
 
 	+" NVL(geral.VL_VENDA,0) total_VENDA, " 
 	+" NVL(geral.VL_AMOSTRA,0) total_AMOSTRA,  "
 	+" NVL(geral.VL_AMOSTRAPAGA,0) total_AMOSTRAPAGA,  "
 	+" NVL(geral.VL_BONIFICACAO,0) total_BONIFICACAO,  "
 	+" NVL(geral.VL_EXPOSITOR,0) total_EXPOSITOR,  "
 	+" NVL(geral.VL_BRINDE,0) total_BRINDE,  "
 	+" NVL(geral.VL_TROCA,0) total_TROCA,  "
 	+" NVL(geral.VL_NEGOCIACOESCOMERCIAIS,0) total_NEGOCIACOESCOMERCIAIS, en.NRO_ENDCADCFTV numeroendereco, "
 	
	+" NVL(PEDIDOS.VL_BONIFICACAOEXPOSITOR,0) VL_BONIFICACAOEXPOSITOR, " 
	+" NVL(geral.VL_BONIFICACAOEXPOSITOR,0) total_BONIFICACAOEXPOSITOR,"
	
	
	+ " NVL(geral.VL_AMOSTRA  + geral.VL_BONIFICACAO + geral.VL_EXPOSITOR + geral.VL_BRINDE + geral.VL_NEGOCIACOESCOMERCIAIS + geral.VL_BONIFICACAOEXPOSITOR,0) totalinvestimento_geral,"
	+ " NVL(PEDIDOS.VL_AMOSTRA  + PEDIDOS.VL_BONIFICACAO + PEDIDOS.VL_EXPOSITOR + PEDIDOS.VL_BRINDE + PEDIDOS.VL_NEGOCIACOESCOMERCIAIS + PEDIDOS.VL_BONIFICACAOEXPOSITOR,0) totalinvestimento_periodo "
 
 	+" from cadcftv c  "
 	+" inner join cliente cl on cl.CADCFTVID = c.CADCFTVID  "
 	+" inner join cadcftv v on v.cadcftvid = cl.VENDEDORID1 "
 	+" INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = v.cadcftvid "
 	
 	+" LEFT join ( "
 	+" SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V "
 	+" inner join( "
 	+" SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV  "
 	+" group by cadcftvid "
	+" )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
 	+" INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
	+" ) EN ON EN.CADCFTVID = C.CADCFTVID "

 	+" LEFT JOIN(  "
 	+" select  "
 	+" x.CADCFTVID, "
 	+" sum(NVL(x.VL_VENDA,0)) VL_VENDA,  "
 	+" sum(NVL(x.VL_AMOSTRA,0)) VL_AMOSTRA,  "
 	+" sum(NVL(x.VL_AMOSTRAPAGA,0)) VL_AMOSTRAPAGA,  "
 	+" sum(NVL(x.VL_BONIFICACAO,0)) VL_BONIFICACAO,  "
 	+" sum(NVL(x.VL_BONIFICACAOEXPOSITOR,0)) VL_BONIFICACAOEXPOSITOR,  "
 	+" sum(NVL(x.VL_EXPOSITOR,0)) VL_EXPOSITOR,  "
 	+" sum(NVL(x.VL_BRINDE,0)) VL_BRINDE,  "
 	+" sum(NVL(x.VL_TROCA,0)) VL_TROCA,  "
 	+" sum(NVL(x.VL_NEGOCIACOESCOMERCIAIS,0)) VL_NEGOCIACOESCOMERCIAIS from( "
 	+" SELECT  P.CADCFTVID, "
	+" case when CF.tipooperacao_cfop = 'VENDA' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_venda,   "
	+" case when p.tipopedidoid  = 4 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostra,   "
	+" case when p.tipopedidoid  = 6 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostrapaga,   "
	+" case when p.tipopedidoid  = 3 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacao,   "
	+" case when p.tipopedidoid  = 16 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacaoexpositor,   "
	+" case when p.tipopedidoid  = 5 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_expositor,   "
	+" case when p.tipopedidoid  = 14 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_brinde,  " 
	+" case when p.tipopedidoid  = 2 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_troca,  " 
	+" case when p.tipopedidoid  = 13 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_negociacoescomerciais "  
 	+" FROM PEDIDOVENDA P INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
 	+" WHERE p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
 	+" AND p.DT_PEDIDOVENDA BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " '  "
	+" )x group by x.CADCFTVID "
	+" )PEDIDOS ON PEDIDOS.CADCFTVID = c.CADCFTVID  "
 
 	+" LEFT JOIN(  "
 	+" select  "
 	+" x.CADCFTVID, "
 	+" sum(NVL(x.VL_VENDA,0)) VL_VENDA,  "
 	+" sum(NVL(x.VL_AMOSTRA,0)) VL_AMOSTRA,  "
 	+" sum(NVL(x.VL_AMOSTRAPAGA,0)) VL_AMOSTRAPAGA,  "
 	+" sum(NVL(x.VL_BONIFICACAO,0)) VL_BONIFICACAO,  "
 	+" sum(NVL(x.VL_BONIFICACAOEXPOSITOR,0)) VL_BONIFICACAOEXPOSITOR,  "
 	+" sum(NVL(x.VL_EXPOSITOR,0)) VL_EXPOSITOR,  "
 	+" sum(NVL(x.VL_BRINDE,0)) VL_BRINDE,  "
 	+" sum(NVL(x.VL_TROCA,0)) VL_TROCA,  "
 	+" sum(NVL(x.VL_NEGOCIACOESCOMERCIAIS,0)) VL_NEGOCIACOESCOMERCIAIS from( "
 	+" SELECT  P.CADCFTVID, "
	+" case when CF.tipooperacao_cfop = 'VENDA' then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_venda,   "
	+" case when p.tipopedidoid  = 4 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostra,   "
	+" case when p.tipopedidoid  = 6 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_amostrapaga,  " 
	+" case when p.tipopedidoid  = 3 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacao,  " 
	+" case when p.tipopedidoid  = 16 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_bonificacaoexpositor,  " 
	+" case when p.tipopedidoid  = 5 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_expositor,   "
	+" case when p.tipopedidoid  = 14 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_brinde,   "
	+" case when p.tipopedidoid  = 2 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_troca,   "
	+" case when p.tipopedidoid  = 13 and CF.tipooperacao_cfop <> 'VENDA'  then P.VL_TOTALPROD_PEDIDOVENDA else 0 end as VL_negociacoescomerciais   "
 	+" FROM PEDIDOVENDA P INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
 	+" WHERE p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
	+" )x group by x.CADCFTVID  "
	+" )geral ON geral.CADCFTVID = c.CADCFTVID  "
 
 	+" where c.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE'  "
 	+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
	+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
	+ " and c.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
	
 	+" group by v.CADCFTVID, v.NOME_CADCFTV, c.CADCFTVID, c.NOME_CADCFTV, en.END_ENDCADCFTV, en.nome_cidade, "
 	+" en.CEP_ENDCADCFTV, en.uf_cidade, c.cnpjcpf_cadcftv, PEDIDOS.VL_VENDA , PEDIDOS.VL_AMOSTRA,  "
 	+" PEDIDOS.VL_AMOSTRAPAGA,  PEDIDOS.VL_BONIFICACAO,PEDIDOS.VL_BONIFICACAOEXPOSITOR,  PEDIDOS.VL_EXPOSITOR,  PEDIDOS.VL_BRINDE,  "
 	+" PEDIDOS.VL_TROCA,  PEDIDOS.VL_NEGOCIACOESCOMERCIAIS, geral.VL_VENDA , geral.VL_AMOSTRA,  "
 	+" geral.VL_AMOSTRAPAGA,  geral.VL_BONIFICACAO, geral.VL_BONIFICACAOEXPOSITOR,  geral.VL_EXPOSITOR,  geral.VL_BRINDE,  "
 	+" geral.VL_TROCA,  geral.VL_NEGOCIACOESCOMERCIAIS, en.NRO_ENDCADCFTV ");
	List<Object[]> lista = query.getResultList();
		
	for (Object[] row : lista) {
		DadosCliente dadoscliente = new DadosCliente();		
		
		dadoscliente.setCodigovendedor((BigDecimal) row[0]);
		dadoscliente.setNomevendedor((String) row[1]);
		dadoscliente.setCodigocliente((BigDecimal) row[2]);
		dadoscliente.setNomecliente((String) row[3]);
		dadoscliente.setEndereco((String) row[5]);
		dadoscliente.setCidade((String) row[6]);
		dadoscliente.setCep((String) row[7]);
		dadoscliente.setUf((String) row[8]);
		
		dadoscliente.setVlvenda((BigDecimal) row[9] );
		dadoscliente.setVlamostra((BigDecimal) row[10] );
		dadoscliente.setVlamostrapaga((BigDecimal) row[11] );
		dadoscliente.setVlbonificacao((BigDecimal) row[12] );
		dadoscliente.setVlexpositor((BigDecimal) row[13] );
		dadoscliente.setVlbrinde((BigDecimal) row[14] );
		dadoscliente.setVltroca((BigDecimal) row[15] );
		dadoscliente.setVlnegociacoescomerciais((BigDecimal) row[16] );
		
		dadoscliente.setAcvlvenda((BigDecimal) row[17] );
		dadoscliente.setAcvlamostra((BigDecimal) row[18] );
		dadoscliente.setAcvlamostrapaga((BigDecimal) row[19] );
		dadoscliente.setAcvlbonificacao((BigDecimal) row[20] );
		dadoscliente.setAcvlexpositor((BigDecimal) row[21] );
		dadoscliente.setAcvlbrinde((BigDecimal) row[22] );
		dadoscliente.setAcvltroca((BigDecimal) row[23] );
		dadoscliente.setAcvlnegociacoescomerciais((BigDecimal) row[24] );
		
		dadoscliente.setNumeroendereco((String) row[25]);
		
		dadoscliente.setVlbonificacaoexpositor((BigDecimal) row[26] );
		dadoscliente.setAcvlbonificacaoexpositor((BigDecimal) row[27] );
		
		dadoscliente.setTotalinvestimentogeral((BigDecimal) row[28] );
		dadoscliente.setTotalinvestimentoperiodo((BigDecimal) row[29] );
		
		//sige
		String cliente =  String.valueOf((BigDecimal) row[2]);
		String cnpj = String.valueOf((String) row[4]);
		javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
				" select "
				+" g.Cod_Cadastro, "
				+" total.tvenda, "
				+" total.tamostra, "
				+" total.tbonificacao, "
				+" total.texpositor, "
				+" total.ttroca, "

				+" periodo.ttvenda, "
				+" periodo.ttamostra, "
				+" periodo.ttbonificacao, "
				+" periodo.ttexpositor, "
				+" periodo.tttroca, "
				
				+ " total.tamostra + total.tbonificacao + total.texpositor as totalgeral,"
				+ " periodo.ttamostra + periodo.ttbonificacao + periodo.ttexpositor as totalperiodo "

				+" from tbCadastroGeral g "

				+" left join( "
				+" select "
				+" total.cod_cliente, "
				+" isnull(SUM(total.sige_valor_venda),0) tvenda, "
				+" isnull(SUM(total.sige_valor_amostra),0) tamostra, "
				+" isnull(SUM(total.sige_valor_bonificacao),0) tbonificacao, "
				+" isnull(SUM(total.sige_valor_expositor),0) texpositor, "
				+" isnull(SUM(total.sige_valor_troca),0) ttroca "
				+" from( "
				+" select s.Cod_cli_for as cod_cliente, "
				+" case when p.Cod_tipo_mv = 510 then cast(s.Valor_liquido as money) else 0 end as sige_valor_venda,   "
				+" case when p.Cod_tipo_mv = 516 then cast(s.Valor_liquido as money) else 0 end as sige_valor_amostra,  " 
				+" case when p.Cod_tipo_mv = 512 then cast(s.Valor_liquido as money) else 0 end as sige_valor_bonificacao, "  
				+" case when p.Cod_tipo_mv = 517 then cast(s.Valor_liquido as money) else 0 end as sige_valor_expositor,   "
				+" case when p.Cod_tipo_mv = 515 then cast(s.Valor_liquido as money) else 0 end as sige_valor_troca  , 'Fechado' as Status , p.Data_v1 "
				+" from tbsaidas s left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un  "
				+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
				+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
				+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2   "
				+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
				+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave  "
				+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('510','512','515','516','517') " 
				+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' )total group by total.cod_cliente "
				+" )total on total.cod_cliente = g.cod_cadastro "

				+" left join( "
				+" select "
				+" total.cod_cliente, "
				+" isnull(SUM(total.sige_valor_venda),0) ttvenda, "
				+" isnull(SUM(total.sige_valor_amostra),0) ttamostra, "
				+" isnull(SUM(total.sige_valor_bonificacao),0) ttbonificacao, "
				+" isnull(SUM(total.sige_valor_expositor),0) ttexpositor, "
				+" isnull(SUM(total.sige_valor_troca),0) tttroca "
				+" from( "
				+" select s.Cod_cli_for as cod_cliente, "
				+" case when p.Cod_tipo_mv = 510 then cast(s.Valor_liquido as money) else 0 end as sige_valor_venda,   "
				+" case when p.Cod_tipo_mv = 516 then cast(s.Valor_liquido as money) else 0 end as sige_valor_amostra,  " 
				+" case when p.Cod_tipo_mv = 512 then cast(s.Valor_liquido as money) else 0 end as sige_valor_bonificacao, "  
				+" case when p.Cod_tipo_mv = 517 then cast(s.Valor_liquido as money) else 0 end as sige_valor_expositor,   "
				+" case when p.Cod_tipo_mv = 515 then cast(s.Valor_liquido as money) else 0 end as sige_valor_troca  , 'Fechado' as Status , p.Data_v1 "
				+" from tbsaidas s left join tbsaidas p on p.Chave_fato = s.Chave_fato_orig_un  "
				+" inner join tbCadastroGeral g on g.Cod_cadastro = s.Cod_cli_for "
				+" left join tbTipoMvEstoque tp on tp.cod_tipo_mv = p.cod_tipo_mv  "
				+" left join ( select max(s2.chave_fato) chave, s2.Chave_fato_orig_un from tbsaidas s2 "  
				+" group by s2.Chave_fato_orig_un) s22 on s22.Chave_fato_orig_un = s.Chave_fato  "
				+" left join tbsaidas s2 on s2.Chave_fato_orig_un = s.Chave_fato and s2.Chave_fato = s22.chave "  
				+" where s.Cod_tipo_mv in ('520','523','527')and p.Cod_tipo_mv in ('510','512','515','516','517')  " 
				+" and s.STATUS <> 'C'AND s.STATUS_CTB = 'S' and p.Status<>'C' "
				+" and p.Data_v1 between ' " + SdataFormatada + " ' and ' " + SdataFormatada2 + " ')total group by total.cod_cliente "
				+" )periodo on periodo.cod_cliente = g.cod_cadastro "

				+" where (g.Cod_Cadastro = " + cliente + " or g.Cpf_Cgc = '" + cnpj + "') ");
		
		List<Object[]> listasige = querySige.getResultList();
		for (Object[] rowsige : listasige) {
			BigDecimal venda = (BigDecimal) rowsige[1];
			BigDecimal amostra = (BigDecimal) rowsige[2];
			BigDecimal bonificacao = (BigDecimal) rowsige[3];
			BigDecimal expositor = (BigDecimal) rowsige[4];
			BigDecimal troca = (BigDecimal) rowsige[5];
			
			if(venda == null) {venda = new BigDecimal(0);}
			if(amostra == null) {amostra = new BigDecimal(0);}
			if(bonificacao == null) {bonificacao = new BigDecimal(0);}
			if(expositor == null) {expositor = new BigDecimal(0);}
			if(troca == null) {troca = new BigDecimal(0);}
			
			dadoscliente.setAcvlvenda(new BigDecimal(venda.doubleValue() + dadoscliente.getAcvlvenda().doubleValue()));
			dadoscliente.setAcvlamostra(new BigDecimal(amostra.doubleValue() + dadoscliente.getAcvlamostra().doubleValue()));
			dadoscliente.setAcvlbonificacao(new BigDecimal(bonificacao.doubleValue() + dadoscliente.getAcvlbonificacao().doubleValue()));
			dadoscliente.setAcvlexpositor(new BigDecimal(expositor.doubleValue() + dadoscliente.getAcvlexpositor().doubleValue()));
			dadoscliente.setAcvltroca(new BigDecimal(troca.doubleValue() + dadoscliente.getAcvltroca().doubleValue()));
			
			BigDecimal pvenda = (BigDecimal) rowsige[6];
			BigDecimal pamostra = (BigDecimal) rowsige[7];
			BigDecimal pbonificacao = (BigDecimal) rowsige[8];
			BigDecimal pexpositor = (BigDecimal) rowsige[9];
			BigDecimal ptroca = (BigDecimal) rowsige[10];
			
			BigDecimal totalgeral = (BigDecimal) rowsige[11];
			BigDecimal totalperiodo = (BigDecimal) rowsige[12];
			
			if(pvenda == null) {pvenda = new BigDecimal(0);}
			if(pamostra == null) {pamostra = new BigDecimal(0);}
			if(pbonificacao == null) {pbonificacao = new BigDecimal(0);}
			if(pexpositor == null) {pexpositor = new BigDecimal(0);}
			if(ptroca == null) {ptroca = new BigDecimal(0);}
			
			if(totalgeral == null) {totalgeral = new BigDecimal(0);}
			if(totalperiodo == null) {totalperiodo = new BigDecimal(0);}
			
			dadoscliente.setVlvenda(new BigDecimal(pvenda.doubleValue()+dadoscliente.getVlvenda().doubleValue()) );
			dadoscliente.setVlamostra(new BigDecimal(pamostra.doubleValue()+dadoscliente.getVlamostra().doubleValue()) );
			dadoscliente.setVlbonificacao(new BigDecimal(pbonificacao.doubleValue()+dadoscliente.getVlbonificacao().doubleValue()) );
			dadoscliente.setVlexpositor(new BigDecimal(pexpositor.doubleValue()+dadoscliente.getVlexpositor().doubleValue()) );
			dadoscliente.setVltroca(new BigDecimal(ptroca.doubleValue()+dadoscliente.getVltroca().doubleValue()) );
			
			dadoscliente.setTotalinvestimentogeral(new BigDecimal(totalgeral.doubleValue()+dadoscliente.getTotalinvestimentogeral().doubleValue()) );
			dadoscliente.setTotalinvestimentoperiodo(new BigDecimal(totalperiodo.doubleValue()+dadoscliente.getTotalinvestimentoperiodo().doubleValue()) );
			
		}
		
		list.add(dadoscliente);
	}

	return list;
	
}

public List<VendasEndereco> vendasendereco(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
	List<VendasEndereco> list = new ArrayList<>();
	
	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	String dataFormatada = formato.format(data1);
	String dataFormatada2 = formato.format(data2);

	javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
			// "SELECT * FROM("
					" select " 
					+ "  c.CADCFTVID CLIENTE, "
					+ "  c.NOME_CADCFTV nome_cliente, "
					+ "  en.END_ENDCADCFTV endereco, "
					+ "  en.NRO_ENDCADCFTV, "
					+ "  en.nome_cidade, "
					+ "  en.CEP_ENDCADCFTV, "
					+ "  en.uf_cidade, "
					
					+ "  v.cadcftvid vendedor, "
					+ "  v.NOME_CADCFTV nome_vendedor, "
					+ "  G.NOME_GESTOR, "
					
					+ "  P.PEDIDOVENDAID, "
					+ "  P.DT_PEDIDOVENDA, "
					+ "  P.VL_TOTALPROD_PEDIDOVENDA, "
					+ "  re.NOME_REGIAO,"
					+ "  itens.perc_lucro,"
					+ "  qtde.qtdeitens "
					
					+ "  from cadcftv c "
					+ "  inner join cliente cl on cl.CADCFTVID = c.CADCFTVID "
					+ "  inner join REGIAO re on re.REGIAOID = cl.REGIAOID "
					+ "  inner join cadcftv v on v.cadcftvid = cl.VENDEDORID1 "
					+ "  INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = v.cadcftvid "
					+ "  INNER JOIN PEDIDOVENDA P ON P.CADCFTVID = C.CADCFTVID "
					+ "  INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
					+ "  INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
					
					+ "  LEFT join ( "
					+ "  SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V "
					+ "  inner join( "
					+ "  SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
					+ "  group by cadcftvid "
					+ "  )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
					+ "  INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
					+ "  ) EN ON EN.CADCFTVID = C.CADCFTVID "
					
					+ " left join (select it.pedidovendaid, sum(it.QT_INI_PEDIDOVENDA_ITEM) qtdeitens from pedidovenda_item it group by it.pedidovendaid)qtde on qtde.pedidovendaid = p.pedidovendaid"
					
					
					+ "  left join( "
					+ "  SELECT " 
					+ "  x.pedidovendaid, "
					+ "  sum(x.TOTAL_VENDA), "
					+ "  sum(x.TOTALLIQUIDO_PEDIDO), "
					+ "  (sum(x.TOTALLIQUIDO_PEDIDO) /(sum(x.TOTAL_VENDA)))*100 as perc_lucro "
					+ "  FROM( "
					+ "  select "
					+ "  p.pedidovendaid, "
					+ "  case when IT.vl_total_pedidovenda_item = 0 then 1 else IT.vl_total_pedidovenda_item end TOTAL_VENDA, "
					+ "  (IT.vl_total_pedidovenda_item - ( it.VL_CUSTOORIG_PEDIDOVENDA_ITEM  * it.qt_pedidovenda_item )) TOTALLIQUIDO_PEDIDO "
					+ "  from pedidovenda_item it "
					+ "  inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid)X "
					+ "  group by x.pedidovendaid "
					+ "  ) itens on itens.pedidovendaid = p.pedidovendaid "
					
					+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
					+ " AND CF.tipooperacao_cfop = 'VENDA' "
					+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
					+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
					+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
					+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
					+ " ORDER BY P.PEDIDOVENDAID  ");

	List<Object[]> lista = query.getResultList();
	
	

	for (Object[] row : lista) {
		VendasEndereco vendas = new VendasEndereco();

		vendas.setCodigocliente((BigDecimal) row[0]);
		vendas.setNomecliente((String) row[1] );
		vendas.setEndereco((String) row[2]);
		vendas.setNumero((String) row[3]);
		vendas.setCidade((String) row[4]);
		vendas.setCep((String) row[5]);
		vendas.setUf((String) row[6]);
		vendas.setCodigovendedor((BigDecimal) row[7]);
		vendas.setNomevendedor((String) row[8] );
		vendas.setNomegestor((String) row[9]);
		
		vendas.setPedido((BigDecimal) row[10] );
		vendas.setDatapedido((Date) row[11] );
		vendas.setValortotalpedido((BigDecimal) row[12] );
		vendas.setRegiao((String) row[13]);
		vendas.setPerc_lucro((BigDecimal) row[14]);
		vendas.setQtdeitens((BigDecimal) row[15]);
		
				
		list.add(vendas);
	}

	return list;
}
	//busca itens retorno da afinação marcio tecco
	public List<RetornoAfinacao> retornoafinacao(Date data1, Date data2, String cfop){
		List<RetornoAfinacao> list = new ArrayList<>();
	
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
	
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
			" select "
			+ " it.produtoid codigo_usinado, "
			+ " p.NOME_PRODUTO produto_usinado, "
			+ " SUM(it.QT_ITEMENTRADA) qtde "
			+ " from entrada en "
			+ " inner join itementrada it on it.entradaid = en.entradaid "
			+ " inner join produto p on p.produtoid = it.produtoid "
			+ " where en.FORNECEDOR = 16290 "
			+ " and en.DT_ENTRADA between ' " + dataFormatada + " ' and ' " + dataFormatada2 +" ' "
			+ " and en.CFOPID = "+cfop
			+ " GROUP BY p.NOME_PRODUTO,it.produtoid ");
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			
			RetornoAfinacao retorno = new RetornoAfinacao();		
			
			retorno.setProduto_usinado((BigDecimal) row[0]);
			retorno.setNomeproduto_usinado((String) row[1]);
			retorno.setQtde_usinado((BigDecimal) row[2]);
			//faz relação
			javax.persistence.Query querySige = (javax.persistence.Query) managerSige.createNativeQuery(
					" select "
					+ " isnull(codigo_afinacao,0)codigo_afinacao, "
					+ " isnull(nome_afinado,'')nome_afinado, "
					+ " codigo_usinado, "
					+ " nome_usinado, "
					+ " isnull(codigo_cromado,0) codigo_cromado, "
					+ " isnull(nome_cromado,'') nome_cromado, id "
					+ " from dw_tbrelacao "
					+ " where codigo_usinado = ' "+retorno.getProduto_usinado()+" ' ");
			List<Object[]> lista2 = querySige.getResultList();
			for (Object[] row2 : lista2) {
				
				BigDecimal f = new BigDecimal((Double) row2[0]);
				BigDecimal c = new BigDecimal((Double) row2[4]);
				
				retorno.setProduto_afinado(f);
				retorno.setNomeproduto_afinado((String) row2[1]);
				retorno.setProduto_cromado(c);
				retorno.setNomeproduto_cromado((String) row2[5]);
				if(!retorno.getNomeproduto_cromado().equals("")) {
					retorno.setQtde_cromado(retorno.getQtde_usinado());
				}
				retorno.setId((Integer) row2[6]);
			}
			//
			
			//busca valor de servico de affincao da tabela 44
			if (retorno.getProduto_afinado() != null) {
			javax.persistence.Query query3 = (javax.persistence.Query) manager.createNativeQuery(
					" select "
					+ " produtoid, "
					+ " nvl(VL_UNIT_TABELAPRECOPRODUTO,0) vl "
					+ " from TABELAPRECOPRODUTO "
					+ " where TABELAPRECOID = '44' "
					+ " and produtoid = ' "+retorno.getProduto_afinado()+" ' ");
			List<Object[]> lista3 = query3.getResultList();
			 for (Object[] row3 : lista3) {
				retorno.setValor_servicoafinado((BigDecimal) row3[1]);
				
				if(cfop.equals("989")) {
					BigDecimal un = new BigDecimal(retorno.getValor_servicoafinado().doubleValue()/3);
					retorno.setValor_servicoafinado(un);
				}
				BigDecimal total = new BigDecimal(retorno.getQtde_usinado().doubleValue()*retorno.getValor_servicoafinado().doubleValue());
				retorno.setValortotal_servicoafinado(total);
			 }
			}
			//
			
			list.add(retorno);
		}
		
		
	
	return list;
}

	/// retorno afinação
	// consulta relação
	@Override
	public List<RetornoAfinacao> consultar_relacao() {
		List<RetornoAfinacao> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) managerSige.createNativeQuery(
				" select * from dw_tbrelacao ");
		
		List<Object[]> lista = query.getResultList();

		for (Object[] row2 : lista) {
			
			RetornoAfinacao retorno = new RetornoAfinacao();

			if (row2[0] != null) {
				BigDecimal u = new BigDecimal((Double) row2[2]);
				retorno.setProduto_usinado(u);
			}else {
				//BigDecimal u =  new BigDecimal(0);
				//retorno.setProduto_usinado(null);
			}
			
			
			if (row2[0] != null) {
				BigDecimal f = new BigDecimal((Double) row2[0]);
				retorno.setProduto_afinado(f);
			}else {
				//BigDecimal f =  new BigDecimal(0);
				//retorno.setProduto_afinado(f);
			}
			
			if (row2[4] != null) {
				BigDecimal c = new BigDecimal((Double) row2[4]);
				retorno.setProduto_cromado(c);
			}else {
				//BigDecimal c =  new BigDecimal(0);
				//retorno.setProduto_cromado(c);
			}
			
			
			retorno.setNomeproduto_afinado((String) row2[1]);
			retorno.setNomeproduto_usinado((String) row2[3]);
			retorno.setNomeproduto_cromado((String) row2[5]);
			retorno.setId((Integer) row2[6]);
			
			list.add(retorno);

		}
		
		return list;
	}
	//inserir relação nova
	@Override
	public E salvar_relacao(E e) {
		
		javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
				
				" INSERT INTO dw_tbrelacao (codigo_afinacao,nome_afinado,codigo_usinado ,nome_usinado,codigo_cromado ,nome_cromado ) VALUES('"+ ((RetornoAfinacao) e).getProduto_afinado().floatValue() +"', '"+ ((RetornoAfinacao) e).getNomeproduto_afinado() +"', '"+((RetornoAfinacao) e).getProduto_usinado().floatValue() +"', '"+ ((RetornoAfinacao) e).getNomeproduto_usinado() +"', '"+((RetornoAfinacao) e).getProduto_cromado().floatValue() +"', '"+ ((RetornoAfinacao) e).getNomeproduto_cromado() +"')  " 
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println("Erro:"+e2);
		}
		
		
		return e;
	}
	//alterar relação
	@Override
	public E alterar_relacao(E e) {
		
		
		javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
				// "SELECT * FROM("
				" update dw_tbrelacao set codigo_afinacao = '"+ ((RetornoAfinacao) e).getProduto_afinado().floatValue() +"', nome_afinado = '"+ ((RetornoAfinacao) e).getNomeproduto_afinado() +"',codigo_usinado = '"+ ((RetornoAfinacao) e).getProduto_usinado().floatValue() +"', nome_usinado = '"+ ((RetornoAfinacao) e).getNomeproduto_usinado() +"',codigo_cromado = '"+ ((RetornoAfinacao) e).getProduto_cromado().floatValue() +"', nome_cromado = '"+ ((RetornoAfinacao) e).getNomeproduto_cromado() +"' where id = '"+ ((RetornoAfinacao) e).getId() +"'   "
				+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
		try {
			query2.getResultList();
		} catch (Exception e2) {
			System.out.println("Erro:"+e2);
		}
		
		
		return e;
	}
	
	//excluir relaçao
		@Override
		public E excluir_relacao(E e) {

			javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
					// "SELECT * FROM("
					" delete from dw_tbrelacao where id = '"+ ((RetornoAfinacao) e).getId() +"' "
					+" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'");
			try {
				query2.getResultList();
			} catch (Exception e2) {
				System.out.println("Erro:"+e2);
			}
			
			return e;
		}


	//mapa de pedidos
			public List<Mapa> mapa(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2) {
				List<Mapa> list = new ArrayList<>();
				
				SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
				String dataFormatada = formato.format(data1);
				String dataFormatada2 = formato.format(data2);

				javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
								" select " 
								+ " clt.vendedorid1,  "
								+ " vw.apelido_cadcftv as nome_vendedor , "
								+ " v.cadcftvid, "
								+ " c.apelido_cadcftv , "
								+ " CI.NOME_CIDADE, "
								+ " CI.UF_CIDADE, "
								+ " v.tipo_endcadcftv, "
								+ " v.end_endcadcftv, "
								+ " v.bairro_endcadcftv, "
								+ " v.cep_endcadcftv, "
								+ " v.nro_endcadcftv, "
								+ " V.latitude, v.longitude, "
								+ " to_char(x.dt,'dd/mm/yyyy') as ultima_compra, "
								+ " r.NOME_REGIAO,"
								+ " NVL(x1.TOTALPRODUTO,0) TOTAL "

								+ " from CADCFTV c "
								+ " INNER JOIN CLIENTE CLt ON CLt.CADCFTVID = C.CADCFTVID "
								+ " inner join CADCFTV vw on vw.cadcftvid = clt.vendedorid1 "

								+ " LEFT join  "
								+ " ( "
								+ " SELECT  max(v.endcadcftvid) endid, V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade FROM ENDCADCFTV V "
								+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
								+ " GROUP BY V.CADCFTVID, CI.UF_CIDADE, ci.nome_cidade "
								+ " ) EN ON EN.CADCFTVID = C.CADCFTVID "

								+ " inner join ENDCADCFTV V on v.cadcftvid = c.cadcftvid and v.endcadcftvid = en.endid "
								+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "

								+ " inner join regiao r on r.REGIAOID = CLt.REGIAOID "

								+ " left join "
								+ " (select max(dt_pedidovenda) as dt, cadcftvid from pedidovenda "
								+ " where status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') GROUP BY cadcftvid "
								+ " )x on x.cadcftvid = c.cadcftvid "
								
								+ " left join ( "
								+ "	select "
								+ "	cadcftvid, "
								+ "	SUM(VL_TOTALPROD_PEDIDOVENDA) AS TOTALPRODUTO "
								+ "	from pedidovenda p "
								+ "	INNER JOIN CFOP CF ON CF.CFOPID = p.CFOPID "
								+ "	where status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ "	AND CF.tipooperacao_cfop= 'VENDA' "
								+ "and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
								+ "GROUP BY cadcftvid  "
								+ ")x1 on x1.cadcftvid = c.cadcftvid "
								
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = vw.cadcftvid "
								+ " where C.funcao_principal_cadcftv = 'CLIENTE' "
								+ " AND c.ATIVO_CADCFTV = 'SIM' "
								+ " and vw.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
								
								
								+ " and V.LATITUDE <> '-999' "
								+ "and V.LONGITUDE <> '-998' "

								+ " order by  "
								+ " clt.vendedorid1 desc " );

				List<Object[]> lista = query.getResultList();
				
				

				for (Object[] row : lista) {
					Mapa mapa = new Mapa();
					
					mapa.setVendedor((BigDecimal) row[0]);
					mapa.setNomevendedor((String) row[1]);
					mapa.setCliente((BigDecimal) row[2]);
					mapa.setNomecliente((String) row[3]);
					
					mapa.setCidade((String) row[4]);
					mapa.setUf((String) row[5]);
					mapa.setEndereco((String) row[7]);
					mapa.setBairro((String) row[8]);
					mapa.setCep((String) row[9]);
					mapa.setNumero((String) row[10]);
					
					mapa.setLatitude((BigDecimal) row[11]);
					mapa.setLongitude((BigDecimal) row[12]);
					mapa.setUltimacompra((String) row[13]);
					mapa.setTotalperiodo((BigDecimal) row[15]);
					list.add(mapa);
				}

				return list;
			}
	
}
