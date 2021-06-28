package br.com.dwbi.dwbi.hibernate;

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
			vendasEmGeralItem.setImagem((Blob) row2[17] );
			
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
				vendasEmGeralItem.setImagem((Blob) row2[17] );
				
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
						vendasEmGeralItem.setImagem((Blob) row2[17] );
						
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
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
					vendasEmGeralItem.setImagem((Blob) row2[17] );
					
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
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or v.cadcftvid like '%"+palavra+"%' or v.CNPJCPF_CADCFTV like '"+palavra+"%' ) "
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
				
				+ "	WHERE mv.MES_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'MM')) "
				+ "	and mv.ANO_METAVENDEDOR = TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+ "	and v.ATIVO_CADCFTV = 'SIM' "
				+ " AND V.CADCFTVID =  "+ usuarioconectado()
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
				+ "	INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID  "
				+ "	LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID "
				+ "	LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID "
				+ "	WHERE CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ "	AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL') "
				+ " AND EN.VENDEDOR1ID =  "+ usuarioconectado()
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
	
}
