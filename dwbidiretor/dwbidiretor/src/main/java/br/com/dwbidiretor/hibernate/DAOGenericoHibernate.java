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
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.classe.VendedorMetaVenda;
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
			+ " NVL((NVL(sum(investimento.bonificacaoexpositor),0) / fat.vlfaturado )*100 ,0) as pcbonificacaoexpositor "

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

			+ " group by  "
			+ " geral.vlgeralfaturado, "
			+ " fat.vlfaturado, "
			+ " investimento.vendedor, "
			+ " investimento.nome_vendedor ");

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
			+ " NVL((NVL(sum(investimento.bonificacaoexpositor),0) / fat.vlfaturado )*100 ,0) as pcbonificacaoexpositor "

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

			+ " group by  "
			+ " geral.vlgeralfaturado, "
			+ " fat.vlfaturado, "
			+ " investimento.vendedor, "
			+ " investimento.nome_vendedor ");

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
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or v.cadcftvid like '%"+palavra+"%') "
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
						+ " where g.status_gestor = 'ATIVO' AND v.CADCFTVID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2) {
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
				+ "	WHERE mv.MES_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'MM')) "
				+ "	and mv.ANO_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
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
				+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = TO_CHAR(SYSDATE,'MM') "
				+ "	AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = TO_CHAR(SYSDATE,'YYYY') "
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
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2, String gestor1, String gestor2) {
		List<VendedorMetaVenda> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"SELECT EN.VENDEDOR1ID ,VEND.NOME_CADCFTV,SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALORVENDA,MV.VALOR_METAVENDEDOR VALORMETA, "
				+" (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 as atingidometa, "
				+"case when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 50 then 'red' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 50 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 70 then 'orange' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 70 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 100 then 'blue' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 100 then 'green' end as cordacoluna "
				+"FROM VENDEDOR V   "
				+"left JOIN PEDIDOVENDA EN ON V.CADCFTVID = EN.VENDEDOR1ID  "
				+"INNER JOIN CADCFTV VEND ON VEND.CADCFTVID = V.CADCFTVID "
				//+"INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				//+"INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV   "
				+"INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID   "
				+"LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID  "
				+"LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID  "
				+"left JOIN meta_vendedor mv ON mv.CADCFTVID = VEND.CADCFTVID "
				+"WHERE CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+"and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and v.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+"AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL')  "
				//+"AND V.GESTORID = G.GESTORID   "
				+"AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = TO_CHAR(SYSDATE,'MM')  "
				+"AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = TO_CHAR(SYSDATE,'YYYY')  "
				+"AND mv.MES_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'MM'))  "
				+"and mv.ANO_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+"GROUP BY EN.VENDEDOR1ID ,VEND.NOME_CADCFTV, mv.VALOR_METAVENDEDOR");
				
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
	+" NVL(geral.VL_BONIFICACAOEXPOSITOR,0) total_BONIFICACAOEXPOSITOR  "
 
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
				+" periodo.tttroca "

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
			
			if(pvenda == null) {pvenda = new BigDecimal(0);}
			if(pamostra == null) {pamostra = new BigDecimal(0);}
			if(pbonificacao == null) {pbonificacao = new BigDecimal(0);}
			if(pexpositor == null) {pexpositor = new BigDecimal(0);}
			if(ptroca == null) {ptroca = new BigDecimal(0);}
			
			dadoscliente.setVlvenda(new BigDecimal(pvenda.doubleValue()+dadoscliente.getVlvenda().doubleValue()) );
			dadoscliente.setVlamostra(new BigDecimal(pamostra.doubleValue()+dadoscliente.getVlamostra().doubleValue()) );
			dadoscliente.setVlbonificacao(new BigDecimal(pbonificacao.doubleValue()+dadoscliente.getVlbonificacao().doubleValue()) );
			dadoscliente.setVlexpositor(new BigDecimal(pexpositor.doubleValue()+dadoscliente.getVlexpositor().doubleValue()) );
			dadoscliente.setVltroca(new BigDecimal(ptroca.doubleValue()+dadoscliente.getVltroca().doubleValue()) );
			
		}
		
		list.add(dadoscliente);
	}

	return list;
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
								+ " r.NOME_REGIAO "

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
								
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = vw.cadcftvid "
								+ " where C.funcao_principal_cadcftv = 'CLIENTE' "
								+ " and vw.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
								+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "

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

					list.add(mapa);
				}

				return list;
			}
	
}
