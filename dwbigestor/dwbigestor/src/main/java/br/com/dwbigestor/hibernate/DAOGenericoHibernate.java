package br.com.dwbigestor.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.dwbigestor.classe.HCliente;
import br.com.dwbigestor.classe.MixProduto;
import br.com.dwbigestor.classe.Produto;
import br.com.dwbigestor.classe.Venda_Grupo;
import br.com.dwbigestor.classe.Venda_Subgrupo;
import br.com.dwbigestor.classe.Cliente;
import br.com.dwbigestor.classe.ClientesNovos;
import br.com.dwbigestor.classe.MetaVenda;
import br.com.dwbigestor.classe.VendaAnoMes;
import br.com.dwbigestor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbigestor.classe.VendasEmGeral;
import br.com.dwbigestor.classe.VendasEmGeralItem;
import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.classe.VendedorMetaVenda;
import br.com.dwbigestor.dao.DAOGenerico;

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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID " + " WHERE "
						+ " CF.TIPOOPERACAO_CFOP = 'VENDA' "
						+ " AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+ " AND V.GESTORID = G.GESTORID " + " GROUP BY "
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
			Date data2,String vendedor1, String vendedor2) {
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " inner join subgrupoproduto sub on sub.subgrupoprodutoid = pr.subgrupoprodutoid "
						+ " inner join GRUPOPRODUTO gru on gru.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
						+ " WHERE p.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "+ " AND V2.GESTORID = G.GESTORID "
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
	
	public List<VendasEmGeral> faturamentoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2) {
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " where p.status_pedidovenda in ('FATURADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' "
						+ " AND V2.GESTORID = G.GESTORID "
						+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
						+ " AND CF.tipooperacao_cfop = 'VENDA' " 
						+ " AND V2.GESTORID = G.GESTORID "
						+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
						+ " AND CF.tipooperacao_cfop = 'VENDA' " + " and V2.GESTORID = G.GESTORID "
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
	public List<VendasEmGeral> amostraemgeral(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2) {
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
							+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " AND V2.GESTORID = G.GESTORID "
							+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
							+ " and p.TIPOPEDIDOID = 4 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
							+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
							+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " and V2.GESTORID = G.GESTORID "
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
							+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop = 'VENDA' AND V2.GESTORID = G.GESTORID "
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
							+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
							+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
							+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
		public List<VendasEmGeral> trocadefeitoemgeral(Date data1, Date data2, String vendedor1, String vendedor2, String cliente1, String cliente2) {
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
									+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
									+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
									+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
											+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
											+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
											+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
											+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
											+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
									+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								    + " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
									+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
										+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
										+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
										+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
										+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
										+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
										+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
										+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
										+ " AND CF.tipooperacao_cfop <> 'VENDA' AND V2.GESTORID = G.GESTORID "
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
								+ " v.NOME_CADCFTV nome_vendedor "
								+ " from pedidovenda p "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " AND V2.GESTORID = G.GESTORID "
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.TIPOPEDIDOID = 3 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
		
	//detalhes do pedido de bonificação
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
								+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " and V2.GESTORID = G.GESTORID "
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

	//pedidos de bonificação expositor
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
									+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
									+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
									+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
									+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " AND V2.GESTORID = G.GESTORID "
									+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
									+ " and p.TIPOPEDIDOID = 16 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
			
		//detalhes do pedido de bonificação expositor
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
									+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
									+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
									+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " and V2.GESTORID = G.GESTORID "
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
								+ " v.NOME_CADCFTV nome_vendedor "
								+ " from pedidovenda p "
								+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
								+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
								+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
								+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
								+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
								+ " inner join roteiro r on r.roteiroid = p.roteiroid "
								+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
								+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
								+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " AND V2.GESTORID = G.GESTORID "
								+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
								+ " and p.TIPOPEDIDOID = 5 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
								+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
								+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
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
								+ " AND CF.tipooperacao_cfop <> 'VENDA' " + " and V2.GESTORID = G.GESTORID "
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = cl.VENDEDORID1 "
						+ " INNER JOIN cadcftv V ON V.cadcftvID = V2.CADCFTVID "
						
						+ " WHERE V2.GESTORID = G.GESTORID  "
						+ " and c.ATIVO_CADCFTV = 'SIM' "
						+ " and c.DATACREATE_CADCFTV between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "	
						+ " INNER JOIN VENDEDOR V3 ON V3.CADCFTVID = V2.VENDEDORID1 "
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or v.cadcftvid like '%"+palavra+"%' or v.CNPJCPF_CADCFTV like '"+palavra+"%' ) "
						+ " and V3.GESTORID = G.GESTORID  "
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
			vendasEmGeralItem.setImagem((Blob) row2[17] );
			
		}
		return vendasEmGeralItem;
	}
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2) {
		List<MetaVenda> list = new ArrayList<>();

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
				+ "	INNER JOIN CADCFTV GR ON GR.CADCFTVID = " + usuarioconectado()
				+ "	INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
				+ "	INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = V.CADCFTVID and V2.GESTORID = G.GESTORID "
				+ "	WHERE mv.MES_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'MM')) "
				+ "	and mv.ANO_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+ "	and v.ATIVO_CADCFTV = 'SIM' "
				+ " and v2.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
				+ "	INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				+ "	INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV  "
				+ "	INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID  "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				+ "	WHERE CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ "	AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
				+ "	AND V.GESTORID = G.GESTORID  "
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
	
	public List<VendedorMetaVenda> vendedormetavenda(String vendedor1, String vendedor2) {
		List<VendedorMetaVenda> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"SELECT EN.VENDEDOR1ID ,VEND.NOME_CADCFTV,SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALORVENDA,MV.VALOR_METAVENDEDOR VALORMETA, "
				+" (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 as atingidometa, "
				+"case when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 50 then 'red' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 50 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 70 then 'orange' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 70 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 100 then 'blue' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 100 then 'green' end as cordacoluna "
				+"FROM PEDIDOVENDA EN  "
				+"INNER JOIN VENDEDOR V ON V.CADCFTVID = EN.VENDEDOR1ID  "
				+"INNER JOIN CADCFTV VEND ON VEND.CADCFTVID = V.CADCFTVID "
				+"INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				+"INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV   "
				+"INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID   "
				+"LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID  "
				+"LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID  "
				+"left JOIN meta_vendedor mv ON mv.CADCFTVID = VEND.CADCFTVID "
				+"WHERE CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+"and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+"AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL')  "
				+"AND V.GESTORID = G.GESTORID   "
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
				+ " nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0) + nvl(VENDAS2022.TOTALANO2022,0) totalgeral, "
				+ " mix.mixqtde, "
				+ " mixmedio.qtmixmedio, "
				+ " round((nvl(VENDAS2018.TOTALANO2018,0) + nvl(VENDAS2019.TOTALANO2019,0) + nvl(VENDAS2020.TOTALANO2020,0) + nvl(VENDAS2021.TOTALANO2021,0)+ nvl(VENDAS2022.TOTALANO2022,0))/mixmedio.qtvendas,2) ticketmedio,  "
				+ " freq.vendas, "
				+ " freq.primeira, "
				+ " freq.ultima, "
				+ " freq.frequencia, "
				+ " C.CNPJCPF_CADCFTV cnpjcpf, "
				+ " en.BAIRRO_ENDCADCFTV bairro, "
				+ " case when round(sysdate - freq.ultima) < = 90 then 'ATIVO' "
				+ " when round(sysdate - freq.ultima) > 90 and round(sysdate - freq.ultima) < = 180 then 'SEMI-ATIVO' "
				+ " when round(sysdate - freq.ultima) > 180 then 'INATIVO' "
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
				+ " VENDAS2022.TOTALANO2022 "				
				
				+ " FROM CADCFTV C "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = C.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = CL.VENDEDORID1 "
				//+ " INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
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
				+ " round((max(p.DT_PEDIDOVENDA) - min(p.DT_PEDIDOVENDA)) /count(p.CADCFTVID)) frequencia "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID "
				+ " )freq on freq.CADCFTVID = c.CADCFTVID "
				
				+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  "+ usuarioconectado()
				+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
				
				+ " WHERE C.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
				+ " AND C.ATIVO_CADCFTV = 'SIM' "
				
				+ " and C.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+"  AND V2.GESTORID = G.GESTORID   "
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
			
			list.add(hCliente);
		}
		
		return list;
	}	
	
	public List<MixProduto> mixprodutos(String vendedor1, String vendedor2, String gestor1, String gestor2, String produto1, String produto2, String grupo1, String grupo2, String subgrupo1, String subgrupo2,String cliente1, String cliente2){
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
				+ " MIXVL.VLTOTAL,  "
				+ " MIXQTDE.QTDE2022, "
				+ " MIXVL.VL2022 "
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
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
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
				+ " SUM(MIXQTDE.QTDE2018)+SUM(MIXQTDE.QTDE2019)+ SUM(MIXQTDE.QTDE2020)+SUM(MIXQTDE.QTDE2021)+SUM(MIXQTDE.QTDE2022) QTDETOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2018' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2018, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2019' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2019, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2020' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2020, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2021' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2021, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2022' THEN it.QT_PEDIDOVENDA_ITEM ELSE 0 END QTDE2022  "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA' "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
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
				+ " SUM(MIXVL.VL2018)+SUM(MIXVL.VL2019)+ SUM(MIXVL.VL2020)+SUM(MIXVL.VL2021)+SUM(MIXVL.VL2022) VLTOTAL "
				+ " FROM(SELECT it.produtoid, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2018' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2018, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2019' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2019, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2020' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2020, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2021' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2021, "
				+ " CASE WHEN TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') = '2022' THEN it.VL_TOTAL_PEDIDOVENDA_ITEM ELSE 0 END VL2022  "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = P.VENDEDOR1ID "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ " inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and P.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				//+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and it.produtoid between ' "+ produto1 + " ' and ' " + produto2 + " ' "
				+ " and gr.GRUPOPRODUTOID between ' "+ grupo1 + " ' and ' " + grupo2 + " ' "
				+ " and sub.SUBGRUPOPRODUTOID between ' "+ subgrupo1 + " ' and ' " + subgrupo2 + " ' "
				+ " and p.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
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
			
			
			list.add(mix);
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
	
	public List<Cliente> clientes() {
		List<Cliente> list = new ArrayList<>();

		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
						" SELECT " 
						+ " V.CADCFTVID cliente, " 
						+ " v.NOME_CADCFTV nome_cliente, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
						+ " INNER JOIN CADCFTV GR ON GR.CADCFTVID =  " + usuarioconectado()
						+ " INNER JOIN GESTOR G ON G.CNPJ_GESTOR = GR.CNPJCPF_CADCFTV OR G.CPF_GESTOR = GR.CNPJCPF_CADCFTV "
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "	
						+ " INNER JOIN VENDEDOR V3 ON V3.CADCFTVID = v2.VENDEDORID1 "
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+"  AND V3.GESTORID = G.GESTORID   "
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
	
}
