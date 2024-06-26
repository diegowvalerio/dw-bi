package br.com.dwbidiretor.hibernate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

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

import br.com.dwbidiretor.classe.PT_Carteira;
import br.com.dwbidiretor.classe.Almoxarifado;
import br.com.dwbidiretor.classe.AnaliseClientePedido;
import br.com.dwbidiretor.classe.CPedido;
import br.com.dwbidiretor.classe.CPedidoFin;
import br.com.dwbidiretor.classe.CPedidoLog;
import br.com.dwbidiretor.classe.Categoria;
import br.com.dwbidiretor.classe.CidadeVenda;
import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.ClienteNovo;
import br.com.dwbidiretor.classe.ClientesAtivos;
import br.com.dwbidiretor.classe.ClientesAtivosAno;
import br.com.dwbidiretor.classe.ClientesNovos;
import br.com.dwbidiretor.classe.CtaCorrente;
import br.com.dwbidiretor.classe.CtaResumo;
import br.com.dwbidiretor.classe.DadosCliente;
import br.com.dwbidiretor.classe.FaseMateriaPrima;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.FasePedidoItem;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.HCliente;
import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.classe.InvestimentoVendedor;
import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.classe.MetaVenda;
import br.com.dwbidiretor.classe.MixProduto;
import br.com.dwbidiretor.classe.Nota;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.classe.NovasVendas_Cliente;
import br.com.dwbidiretor.classe.Orcamentos;
import br.com.dwbidiretor.classe.P1_FaturadoDia;
import br.com.dwbidiretor.classe.P1_MetaFaturado;
import br.com.dwbidiretor.classe.PT_Meta;
import br.com.dwbidiretor.classe.PT_Mix;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.PedidoItem;
import br.com.dwbidiretor.classe.PedidosConferidos;
import br.com.dwbidiretor.classe.PedidosConferidosUsuario;
import br.com.dwbidiretor.classe.Perca;
import br.com.dwbidiretor.classe.PercaDia;
import br.com.dwbidiretor.classe.PercaProduto;
import br.com.dwbidiretor.classe.PrazoPedido;
import br.com.dwbidiretor.classe.Producao;
import br.com.dwbidiretor.classe.ProducaoDia;
import br.com.dwbidiretor.classe.ProducaoProduto;
import br.com.dwbidiretor.classe.Produto;
import br.com.dwbidiretor.classe.ProdutoEstoque;
import br.com.dwbidiretor.classe.ProdutoRanking;
import br.com.dwbidiretor.classe.ReativacaoCliente;
import br.com.dwbidiretor.classe.RetornoAfinacao;
import br.com.dwbidiretor.classe.Rh_Folha;
import br.com.dwbidiretor.classe.Rh_Setor;
import br.com.dwbidiretor.classe.TLOcorrencia;
import br.com.dwbidiretor.classe.TabProduto;
import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.classe.TipoPerca;
import br.com.dwbidiretor.classe.Titulo;
import br.com.dwbidiretor.classe.VendaAnoMes;
import br.com.dwbidiretor.classe.VendaCusto;
import br.com.dwbidiretor.classe.VendaGrupoSubGrupoProdutoQuantidadeValor;
import br.com.dwbidiretor.classe.VendaUF;
import br.com.dwbidiretor.classe.VendaVendedor;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.VendasEndereco;
import br.com.dwbidiretor.classe.VendasFrete;
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
import oracle.sql.BLOB;

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
	
	//banco sige projetos
	@Override
	public E Ssalvar(E e) {
		managerSige.persist(e);
		return e;
	}

	@Override
	public E Salterar(E e) {
		return managerSige.merge(e);
	}

	@Override
	public boolean Sexcluir(Integer id) {
		E e = Sconsultar(id);
		managerSige.remove(e);
		return true;
	}

	@Override
	public E Sconsultar(Integer id) {
		return (E) managerSige.find(classeEntidade, id);
	}
	
	@Override
	public List<E> Sconsultar() {
		return managerSige.createQuery("from " + classeEntidade.getSimpleName()).getResultList();
	}
	
	//fim sige projetos

	
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
				 + "   from vendedor v  "
				 + " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID  "
				 + " inner join cliente vvv on vvv.CADCFTVID = vv.CADCFTVID  "
				 + " inner join REGIAO r on r.REGIAOID = vvv.REGIAOID  "
				 + "   left join(  "
				 + " select   "
				 + " cc.VENDEDORID1 vendedor,  "
				 + " sum(novos.clientes) novos,  "
				 + " sum(mes01.vendas_reativacao) reativados,  "
				 + " sum(ativos.nativos) ativos,  "
				 + " sum(ativosmes.mativos) ativos_mes,  "
				 + "   sum(novos2.clientes) novos2,  "
				 + " sum(mes02.vendas_reativacao) reativados2,  "
				 + " sum(ativos2.nativos) ativos2,  "
				 + " sum(ativosmes2.mativos) ativos_mes2,  "
				 + "   sum(novos3.clientes) novos3,  "
				 + " sum(mes03.vendas_reativacao) reativados3,  "
				 + " sum(ativos3.nativos) ativos3,  "
				 + " sum(ativosmes3.mativos) ativos_mes3  "
				 + "   from cadcftv c   "
				 + " inner join cliente cc on cc.CADCFTVID = c.CADCFTVID  "
				 + "   left join(   "
				 + " select   "
				 + " p.cadcftvid,  "
				 + " count(*) clientes   "
				 + " from(  "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " min(p.DT_PEDIDOVENDA) primeiracompra  "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " group by p.cadcftvid )p  "
				 + " where p.primeiracompra between '01-01-2023' and '31-01-2023'  "
				 + " group by p.cadcftvid  "
				 + " ) novos on novos.cadcftvid = c.cadcftvid   "
				 + "     left join(   "
				 + " select x.CADCFTVID,x.dias  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-12-2022', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-12-2022'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias > 180   "
				 + " )inativo on inativo.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 nativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-12-2022', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-12-2022'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativos on ativos.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 mativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativosmes on ativosmes.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " count(*) vendas_reativacao   "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2023' and '31-01-2023'  "
				 + " group by p.cadcftvid   "
				 + " ) mes01 on mes01.cadcftvid = inativo.cadcftvid   "
				 + " left join(   "
				 + " select   "
				 + " p.cadcftvid,  "
				 + " count(*) clientes   "
				 + " from(  "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " min(p.DT_PEDIDOVENDA) primeiracompra  "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " group by p.cadcftvid )p  "
				 + " where p.primeiracompra between '01-02-2023' and '28-02-2023'  "
				 + " group by p.cadcftvid  "
				 + " ) novos2 on novos2.cadcftvid = c.cadcftvid   "
				 + "   left join(   "
				 + " select x.CADCFTVID,x.dias  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias > 180   "
				 + " )inativo2 on inativo2.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 nativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-01-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-01-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativos2 on ativos2.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 mativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativosmes2 on ativosmes2.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " count(*) vendas_reativacao   "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " and p.DT_PEDIDOVENDA between  '01-02-2023' and '28-02-2023'  "
				 + " group by p.cadcftvid   "
				 + " ) mes02 on mes02.cadcftvid = inativo2.cadcftvid   "
				 + " left join(   "
				 + " select   "
				 + " p.cadcftvid,  "
				 + " count(*) clientes   "
				 + " from(  "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " min(p.DT_PEDIDOVENDA) primeiracompra  "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " group by p.cadcftvid )p  "
				 + " where p.primeiracompra between '01-03-2023' and '31-03-2023'  "
				 + " group by p.cadcftvid  "
				 + " ) novos3 on novos3.cadcftvid = c.cadcftvid   "
				 + "   left join(   "
				 + " select x.CADCFTVID,x.dias  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias > 180   "
				 + " )inativo3 on inativo3.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 nativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('28-02-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '28-02-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativos3 on ativos3.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select x.CADCFTVID ,1 mativos  from(  "
				 + " select   "
				 + " p.CADCFTVID,  "
				 + " extract(days from TO_DATE('31-03-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias  "
				 + " from pedidovenda p   "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " and p.DT_PEDIDOVENDA between  '01-01-2000' and '31-03-2023'  "
				 + " AND CF.TIPOOPERACAO_CFOP = 'VENDA'   "
				 + " group by p.CADCFTVID)x  "
				 + " where x.dias <= 180   "
				 + " )ativosmes3 on ativosmes3.CADCFTVID = c.CADCFTVID   "
				 + "   left join(   "
				 + " select   "
				 + " p.cadcftvid,   "
				 + " count(*) vendas_reativacao   "
				 + " from pedidovenda p    "
				 + " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				 + " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID  "
				 + " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				 + " AND CF.tipooperacao_cfop = 'VENDA'   "
				 + " and p.DT_PEDIDOVENDA between  '01-03-2023' and '31-03-2023'  "
				 + " group by p.cadcftvid   "
				 + " ) mes03 on mes03.cadcftvid = inativo3.cadcftvid   "
				 + " group by cc.VENDEDORID1  "
				 + " )x on x.vendedor = v.CADCFTVID  "
				 + " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3)  "
				 + " and r.NOME_REGIAO = '"+regiao+"' "
				 + " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
				 + " order by v.CADCFTVID)y " ;
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
				+ "  "
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
				
				+ " group by cc.VENDEDORID1 "
				+ " )x on x.vendedor = v.CADCFTVID "
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3) "
				+ " and r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
				+ " order by v.CADCFTVID)y " ;*/
				
				
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
				+ " extract(days from TO_DATE('31-03-2023', 'DD-MM-YYYY') - max(p.DT_PEDIDOVENDA)) dias"
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
				+ " and r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
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
				+ " and r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
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
				//+ " --* "
				+ " group by cc.VENDEDORID1 "
				+ " )x on x.vendedor = v.CADCFTVID "
				+ " where VV.ATIVO_CADCFTV = 'SIM' AND V.CADCFTVID NOT IN (1,2,3) "
				+ " and r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
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
		String sql =
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
				
				+ " where (meta.meta > 0 or meta2.meta > 0) "
				+ " and r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
				+ " )x "
				;

		System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);

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
				+ " where r.NOME_REGIAO = '"+regiao+"' "
				+ " and v.CADCFTVID BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"
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
	
	public List<CtaResumo> ctaresumo(String ano){
		List<CtaResumo> list= new ArrayList<>();
		
		String sql= ""
				+ " select  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta, "
				+ " 'A RECEBER' status, "
				+ " sum(x.jan) as jan, "
				+ " sum(x.fev) as fev, "
				+ " sum(x.mar) as mar, "
				+ " sum(x.abr) as abr, "
				+ " sum(x.mai) as mai, "
				+ " sum(x.jun) as jun, "
				+ " sum(x.jul) as jul, "
				+ " sum(x.ago) as ago, "
				+ " sum(x.set) as set, "
				+ " sum(x.out) as out, "
				+ " sum(x.nov) as nov, "
				+ " sum(x.dez) as dez "
				+ " FROM( "
				+ " select  "
				+ " to_char(c.dt_vencto_ctareceber,'YYYY') ANO, "
				+ " c.planocontaid , "
				+ " pl.nome_planoconta , "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '01' then c.vl_ctareceber else 0 end as JAN, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '02' then c.vl_ctareceber else 0 end as FEV, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '03' then c.vl_ctareceber else 0 end as MAR, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '04' then c.vl_ctareceber else 0 end as ABR, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '05' then c.vl_ctareceber else 0 end as MAI, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '06' then c.vl_ctareceber else 0 end as JUN, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '07' then c.vl_ctareceber else 0 end as JUL, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '08' then c.vl_ctareceber else 0 end as AGO, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '09' then c.vl_ctareceber else 0 end as SET, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '10' then c.vl_ctareceber else 0 end as OUT, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '11' then c.vl_ctareceber else 0 end as NOV, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '12' then c.vl_ctareceber else 0 end as DEZ "
				+ " from CTARECEBER c  "
				+ " inner join planoconta pl on pl.planocontaid = c.planocontaid  "
				+ " where to_char(c.dt_vencto_ctareceber,'YYYY')= '"+ano+"' "
				+ " and c.status_ctareceber = 'ABERTO' "
				+ " and dt_vencto_ctareceber >= CURRENT_DATE "
				+ " )x "
				+ " group by  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta "
				+ " union ALL "
				+ " select  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta, "
				+ " 'A RECEBER VENCIDO' status, "
				+ " sum(x.jan) as jan, "
				+ " sum(x.fev) as fev, "
				+ " sum(x.mar) as mar, "
				+ " sum(x.abr) as abr, "
				+ " sum(x.mai) as mai, "
				+ " sum(x.jun) as jun, "
				+ " sum(x.jul) as jul, "
				+ " sum(x.ago) as ago, "
				+ " sum(x.set) as set, "
				+ " sum(x.out) as out, "
				+ " sum(x.nov) as nov, "
				+ " sum(x.dez) as dez "
				+ " FROM( "
				+ " select  "
				+ " to_char(c.dt_vencto_ctareceber,'YYYY') ANO, "
				+ " c.planocontaid , "
				+ " pl.nome_planoconta , "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '01' then c.vl_ctareceber else 0 end as JAN, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '02' then c.vl_ctareceber else 0 end as FEV, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '03' then c.vl_ctareceber else 0 end as MAR, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '04' then c.vl_ctareceber else 0 end as ABR, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '05' then c.vl_ctareceber else 0 end as MAI, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '06' then c.vl_ctareceber else 0 end as JUN, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '07' then c.vl_ctareceber else 0 end as JUL, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '08' then c.vl_ctareceber else 0 end as AGO, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '09' then c.vl_ctareceber else 0 end as SET, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '10' then c.vl_ctareceber else 0 end as OUT, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '11' then c.vl_ctareceber else 0 end as NOV, "
				+ " case when to_char(c.dt_vencto_ctareceber,'MM') = '12' then c.vl_ctareceber else 0 end as DEZ "
				+ " from CTARECEBER c  "
				+ " inner join planoconta pl on pl.planocontaid = c.planocontaid  "
				+ " where to_char(c.dt_vencto_ctareceber,'YYYY')= '"+ano+"' "
				+ " and c.status_ctareceber = 'ABERTO' "
				+ " and dt_vencto_ctareceber < CURRENT_DATE "
				+ " )x "
				+ " group by  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta "
				+ " union ALL "
				+ " select  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta, "
				+ " 'A PAGAR' status, "
				+ " sum(x.jan) as jan, "
				+ " sum(x.fev) as fev, "
				+ " sum(x.mar) as mar, "
				+ " sum(x.abr) as abr, "
				+ " sum(x.mai) as mai, "
				+ " sum(x.jun) as jun, "
				+ " sum(x.jul) as jul, "
				+ " sum(x.ago) as ago, "
				+ " sum(x.set) as set, "
				+ " sum(x.out) as out, "
				+ " sum(x.nov) as nov, "
				+ " sum(x.dez) as dez "
				+ " FROM( "
				+ " select  "
				+ " to_char(c.dt_vencto_ctapagar,'YYYY') ANO, "
				+ " c.planocontaid , "
				+ " pl.nome_planoconta , "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '01' then c.vl_ctapagar else 0 end as JAN, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '02' then c.vl_ctapagar else 0 end as FEV, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '03' then c.vl_ctapagar else 0 end as MAR, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '04' then c.vl_ctapagar else 0 end as ABR, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '05' then c.vl_ctapagar else 0 end as MAI, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '06' then c.vl_ctapagar else 0 end as JUN, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '07' then c.vl_ctapagar else 0 end as JUL, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '08' then c.vl_ctapagar else 0 end as AGO, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '09' then c.vl_ctapagar else 0 end as SET, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '10' then c.vl_ctapagar else 0 end as OUT, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '11' then c.vl_ctapagar else 0 end as NOV, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '12' then c.vl_ctapagar else 0 end as DEZ "
				+ " from ctapagar c  "
				+ " inner join planoconta pl on pl.planocontaid = c.planocontaid  "
				+ " where to_char(c.dt_vencto_ctapagar,'YYYY')= '"+ano+"' "
				+ " and c.status_ctapagar = 'ABERTO' "
				+ " )x "
				+ " group by  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta "
				+ " union ALL "
				+ " select  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta, "
				+ " 'PREVISAO' status, "
				+ " sum(x.jan) as jan, "
				+ " sum(x.fev) as fev, "
				+ " sum(x.mar) as mar, "
				+ " sum(x.abr) as abr, "
				+ " sum(x.mai) as mai, "
				+ " sum(x.jun) as jun, "
				+ " sum(x.jul) as jul, "
				+ " sum(x.ago) as ago, "
				+ " sum(x.set) as set, "
				+ " sum(x.out) as out, "
				+ " sum(x.nov) as nov, "
				+ " sum(x.dez) as dez "
				+ " FROM( "
				+ " select  "
				+ " to_char(c.dt_vencto_ctapagar,'YYYY') ANO, "
				+ " c.planocontaid , "
				+ " pl.nome_planoconta , "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '01' then c.vl_ctapagar else 0 end as JAN, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '02' then c.vl_ctapagar else 0 end as FEV, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '03' then c.vl_ctapagar else 0 end as MAR, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '04' then c.vl_ctapagar else 0 end as ABR, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '05' then c.vl_ctapagar else 0 end as MAI, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '06' then c.vl_ctapagar else 0 end as JUN, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '07' then c.vl_ctapagar else 0 end as JUL, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '08' then c.vl_ctapagar else 0 end as AGO, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '09' then c.vl_ctapagar else 0 end as SET, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '10' then c.vl_ctapagar else 0 end as OUT, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '11' then c.vl_ctapagar else 0 end as NOV, "
				+ " case when to_char(c.dt_vencto_ctapagar,'MM') = '12' then c.vl_ctapagar else 0 end as DEZ "
				+ " from ctapagar c  "
				+ " inner join planoconta pl on pl.planocontaid = c.planocontaid  "
				+ " where to_char(c.dt_vencto_ctapagar,'YYYY')= '"+ano+"' "
				+ " and c.status_ctapagar = 'PREVISAO' "
				+ " )x "
				+ " group by  "
				+ " X.ano, "
				+ " x.planocontaid, "
				+ " x.nome_planoconta "
				+ " ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			CtaResumo c = new CtaResumo();
			
			c.setAno((String)row[0]);
			c.setPlanocontaid((String)row[1]);
			c.setNomeplanoconta((String)row[2]);
			c.setStatus((String)row[3]);
			c.setJan((BigDecimal)row[4]);
			c.setFev((BigDecimal)row[5]);
			c.setMar((BigDecimal)row[6]);
			c.setAbr((BigDecimal)row[7]);
			c.setMai((BigDecimal)row[8]);
			c.setJun((BigDecimal)row[9]);
			c.setJul((BigDecimal)row[10]);
			c.setAgo((BigDecimal)row[11]);
			c.setSet((BigDecimal)row[12]);
			c.setOut((BigDecimal)row[13]);
			c.setNov((BigDecimal)row[14]);
			c.setDez((BigDecimal)row[15]);
			
			list.add(c);
		}
		
		return list;
	}
	
	
	public List<TLOcorrencia> tlocorrencias(String criador, Date data1, Date data2, String status, String tipo,Date data3, Date data4){
		List<TLOcorrencia> p = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		String dataFormatada3 = formato.format(data3);
		String dataFormatada4 = formato.format(data4);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
				+ " oc.OCORRENCIAID codigo,"
				+ " oc.DT_OCORRENCIA, "
				+ " oc.SOLICITANTE_OCORRENCIA contato, "
				+ " oc.CADCFTVID cliente, "
				+ " cliente.NOME_CADCFTV nomecliente, " 
				+ " cri.nome_usuario criador, "
				+ " oc.STATUS_OCORRENCIA, "
				+ " tipo.DESC_TIPO_OCORRENCIA tipo, "
				+ " origem.DESC_ORIGEM_OCORRENCIA origem, "
				+ " oc.RESUMO_OCORRENCIA, "
				+ " us.NOME_USUARIO responsavelatual, "
				+ " it.DESC_OCORRENCIA_ITEM,"
				+ " mo.nome_modulo_ocorrencia modulo,"
				+ " freq.vendas qtdevendas, "
				+ " freq.primeira primeiravenda, "
				+ " freq.ultima ultimavenda,"
				+ " freq.totalvenda,"
				+ " it.dt_fin_ocorrencia_item dataultima "
				
				+ " from OCORRENCIA oc "
			
				+ " inner join TIPO_OCORRENCIA tipo on tipo.TIPOOCORRENCIAID = oc.TIPOOCORRENCIAID "
				+ " inner join ORIGEM_OCORRENCIA origem on origem.ORIGEMOCORRENCIAID = oc.ORIGEMOCORRENCIAID "
				+ " inner join modulo_ocorrencia mo on mo.moduloocorrenciaid = oc.moduloocorrenciaid  "
				+ " inner join USUARIO us on us.USUARIOID = oc.RESPONSAVELID "
				+ " inner join USUARIO cri on cri.USUARIOID = oc.USUARIOID "
				+ " inner join CADCFTV cliente on cliente.CADCFTVID = oc.CADCFTVID "
				+ " "
				+ " left join( "
				+ " select "
				+ " it.ocorrenciaid, "
				+ " max(it.ocorrenciaitemid) ultima "
				+ " from OCORRENCIA_ITEM it "
				+ " group by it.ocorrenciaid "
				+ " )ult on ult.ocorrenciaid = oc.OCORRENCIAID "
				+ " inner join OCORRENCIA_ITEM it on it.ocorrenciaid = oc.OCORRENCIAID and it.OCORRENCIAITEMID = ult.ultima "
				
				+ " left join(  "
				+ " select  "
				+ " p.CADCFTVID,  "
				+ " count(p.CADCFTVID) vendas,  "
				+ " min(P.DT_PEDIDOVENDA) primeira,  "
				+ " max(P.DT_PEDIDOVENDA) ultima,  "
				+ " sum(p.vl_totalprod_pedidovenda) totalvenda, "
				+ " round(extract(days from max(P.DT_PEDIDOVENDA) - min(P.DT_PEDIDOVENDA) )/count(p.CADCFTVID)) frequencia  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+ " group by p.CADCFTVID  "
				+ " )freq on freq.CADCFTVID = cliente.CADCFTVID "
				
				+ " where oc.DT_OCORRENCIA between '" + dataFormatada +"' and '" + dataFormatada2 +" '"
				+ "	AND (oc.STATUS_OCORRENCIA  = '"+status+"' OR 'TODOS' = '"+status+"' ) "
				+ "	AND (cri.nome_usuario  = '"+criador+"' OR 'TODOS' = '"+criador+"' ) "
				+ "	AND (tipo.DESC_TIPO_OCORRENCIA  = '"+tipo+"' OR 'TODOS' = '"+tipo+"' ) "
				+ " and it.dt_fin_ocorrencia_item between '" + dataFormatada3 +"' and '" + dataFormatada4 +" '"
				);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			TLOcorrencia pr = new TLOcorrencia();
			
			pr.setOcorrenciaid((BigDecimal)row[0]);
			pr.setDataocorrencia((Date)row[1]);
			pr.setContato((String)row[2]);
			pr.setCodigocliente((BigDecimal)row[3]);
			pr.setNomecliente((String)row[4]);
			pr.setCriador((String)row[5]);
			pr.setStatus((String)row[6]);
			pr.setTipo((String)row[7]);
			pr.setOrigem((String)row[8]);
			pr.setResumo((String)row[9]);
			pr.setResponsavelatual((String)row[10]);	
			
			byte[] df = (byte[]) row[11];
			InputStream is = new ByteArrayInputStream(df);						
			try {
				pr.setUltimaresposta(load(is));
			} catch (IOException | BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			pr.setModulo((String)row[12]);
			pr.setQtdevendas((BigInteger)row[13]);
			pr.setPrimeiravenda((Date)row[14]);
			pr.setUltimavenda((Date)row[15]);
			pr.setTotalvenda((BigDecimal)row[16]);
			pr.setDataultima((Date)row[17]);
					
			p.add(pr);
		}
		
		return p;
	}
	
	
	public String load(InputStream is) throws IOException, BadLocationException {
	    RTFEditorKit rtf = new RTFEditorKit();
	    Document doc = rtf.createDefaultDocument();
	    BufferedReader input = new BufferedReader(new InputStreamReader(is));
	    try {
	        rtf.read(input, doc, 0);
	        doc.getText(0, doc.getLength()).trim();
	    } catch (BadLocationException ble) {
	        throw new IOException(ble);
	    }
	    
	    return doc.getText(0, doc.getLength());
	}

	
	public List<PercaProduto> percaproduto(String ano, String mes,String setor, int i,String tipo, int t){
		List<PercaProduto> p = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select * from( "
						+ " select  "
						+ " 'MONTAGEM' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'MONTAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'AFINA플O' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP ,"
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'AFINA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'CROMA플O' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'CROMA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'DEVOLU플O' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'DEVOLU플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'FUNDI플O' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FUNDI플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'USINAGEM' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'USINAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'PINTURA' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP ,"
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'PINTURA%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'FORNECEDOR' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FORNECEDOR%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " 'ALIANZ' setor, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY') ANO, "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') MES, "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP, "
						+ " SUM(o.QT_OUTRASAIDAMP * o.vl_custo_outrasaidamp) valor "
						+ "  "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " inner join produto p on p.produtoid = o.produtoid "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'ALIANZ%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ "  "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OUTRASAIDAMP,'YYYY'), "
						+ " o.PRODUTOID, "
						+ " p.NOME_PRODUTO, "
						+ " o.TIPOOUTRASAIDAMPID, "
						+ " tipo.DESC_TIPOOUTRASAIDAMP "
						+ " )x"
						+ " where (x.setor = '"+setor+"' and 1="+i+" or 2="+i+")"
				);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PercaProduto pr = new PercaProduto();
			
			pr.setSetor((String)row[0]);
			pr.setAno((String)row[1]);
			pr.setMes((String)row[2]);
			pr.setProdutoid((BigDecimal)row[3]);
			pr.setNomeproduto((String)row[4]);
			pr.setQuantidade((BigDecimal)row[5]);
			pr.setTipopercaid((BigDecimal)row[6]);
			pr.setTipoperca((String)row[7]);
			pr.setValor((BigDecimal)row[8]);
			p.add(pr);
		}
		return p;
	}
	
	public List<TipoPerca> tipoperca(){
		List<TipoPerca> p = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select  "
				+ " t.TIPOOUTRASAIDAMPID, "
				+ " t.DESC_TIPOOUTRASAIDAMP "
				+ " from TIPO_OUTRASAIDAMP t "
				+ " where t.DESC_TIPOOUTRASAIDAMP LIKE 'MONTAGEM%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'AFINA플O%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'CROMA플O%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'DEVOLU플O%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'FUNDI플O%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'USINAGEM%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'ALIANZ%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'FORNECEDOR%' "
				+ " OR t.DESC_TIPOOUTRASAIDAMP LIKE 'PINTURA%' "
				+ " ORDER BY t.DESC_TIPOOUTRASAIDAMP "
				);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			TipoPerca pr = new TipoPerca();
			
			pr.setId((BigDecimal)row[0]);
			pr.setTipoperca((String)row[1]);
						
			p.add(pr);
		}
		return p;
	}
	
	public List<PercaDia> percadia(String ano, String mes,String setor, int i,String tipo, int t) {
		
		List<PercaDia> p = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select "
						+ " dia, "
						+ " sum(qtde)qtde,"
						+ " sum(valor)valor "
						+ " from( "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'MONTAGEM' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'MONTAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'AFINA플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'AFINA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'CROMA플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'CROMA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'DEVOLU플O' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'DEVOLU플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'FUNDI플O' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FUNDI플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'USINAGEM' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'USINAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'PINTURA' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'PINTURA%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'ALIANZ' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'ALIANZ%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD')dia, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'FORNECEDOR' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FORNECEDOR%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " group by  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'DD') "
						+ "  "
						+ " )x  "
						+ " where (x.setor = '"+setor+"' and 1="+i+" or 2="+i+")"
						+ " group by x.dia order by x.dia"
				);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			PercaDia pr = new PercaDia();
			
			pr.setDia((String)row[0]);
			pr.setQuantidade((BigDecimal)row[1]);
			pr.setValor((BigDecimal)row[2]);			
			p.add(pr);
		}
		
		return p;
	}
	
	public List<Perca> perca(String ano, String mes,String setor, int i,String tipo, int t) {
		List<Perca> p = new ArrayList<>();
		String sql =
				"select * from ( select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'MONTAGEM' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'MONTAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'AFINA플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'AFINA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'CROMA플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'CROMA플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'DEVOLU플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'DEVOLU플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'FUNDI플O' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FUNDI플O%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') "
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'USINAGEM' setor ,"
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'USINAGEM%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY')"
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'PINTURA' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'PINTURA%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY')"
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'ALIANZ' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'ALIANZ%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY')"
						+ "  "
						+ " union all "
						+ "  "
						+ " select  "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') MES, "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') ANO, "
						+ " SUM(o.QT_OUTRASAIDAMP)QTDE, "
						+ " 'FORNECEDOR' setor, "
						+ " SUM(o.vl_custo_outrasaidamp) valor "
						+ " from OUTRASAIDAMP o "
						+ " inner join TIPO_OUTRASAIDAMP tipo on tipo.TIPOOUTRASAIDAMPID = o.TIPOOUTRASAIDAMPID "
						+ " where tipo.DESC_TIPOOUTRASAIDAMP like'FORNECEDOR%' "
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') = '"+mes+"'"
						+ " and TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY') = '"+ano+"'"
						+ " and (tipo.TIPOOUTRASAIDAMPID = "+tipo+" and 1="+t+" or 2="+t+") "
						+ " GROUP BY "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'MM') , "
						+ " TO_CHAR(DT_OPERACAO_OUTRASAIDAMP,'YYYY')"
						+ " )x where (x.setor = '"+setor+"' and 1="+i+" or 2="+i+")"  ;
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Perca pr = new Perca();
			
			pr.setMes((String)row[0]);
			pr.setAno((String)row[1]);
			pr.setQuantidade((BigDecimal)row[2]);
			pr.setSetor((String)row[3]);
			pr.setValor((BigDecimal)row[4]);
			
			p.add(pr);
		}
		return p;
	}
	
	public List<Producao> producao(String ano, String mes) {
		List<Producao> p = new ArrayList<>();
		String sql =""
				+ " select "
				+ " s.subgrupoprodutoid , "
				+ " s.nome_subgrupoproduto , "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ANO, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') MES, "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'COMPONENTE' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " group by s.subgrupoprodutoid , "
				+ " s.nome_subgrupoproduto , "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') , "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') "
			
				+ " union all  "
				
				+ " select "
				+ " 99999 subgrupoprodutoid , "
				+ " 'MONTAGEM' nome_subgrupoproduto , "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ANO, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') MES, "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'ACABADO' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " group by "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') , "
				+ " TO_CHAR(e.dt_entradaproducao,'MM')"
								
				+ " ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Producao pr = new Producao();
			
			pr.setMes((String)row[3]);
			pr.setAno((String)row[2]);
			pr.setQuantidade((BigDecimal)row[4]);
			pr.setSetor((String)row[1]);
			pr.setSetorid((BigDecimal)row[0]);
			
			p.add(pr);
		}
		return p;
	}
	
	public List<ProducaoDia> producaodia(String ano, String mes, String setor, int i) {
		List<ProducaoDia> p = new ArrayList<>();
		String sql = ""
				+ " select  "
				+ " x.dia, "
				+ " sum(x.qtde) "
				+ " from( "
				+ " select "
				+ " TO_CHAR(e.dt_entradaproducao,'DD') dia, "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'COMPONENTE' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " and (s.subgrupoprodutoid = '"+setor+"' and 1 = "+i+" or 2 = "+i+"  ) "
				+ " group by "
				+ " TO_CHAR(e.dt_entradaproducao,'DD') "
				+ "  "
				+ " union all  "
				+ "  "
				+ " select "
				+ " TO_CHAR(e.dt_entradaproducao,'DD') dia, "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'ACABADO' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " and (99999 = '"+setor+"' and 1 = "+i+" or 2= "+i+") "
				+ " group by "
				+ " TO_CHAR(e.dt_entradaproducao,'DD') "
				+ " )x group by x.dia "
			
				+ "";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);

		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ProducaoDia pr = new ProducaoDia();

			pr.setDia((String) row[0]);
			pr.setQuantidade((BigDecimal) row[1]);

			p.add(pr);
		}

		return p;
	}
	
	public List<ProducaoProduto> producaoproduto(String ano,String mes,String setor,String dia){
		List<ProducaoProduto> list = new ArrayList<>();
		
		String sql = " "
				+ "  select  "
				+ " s.nome_subgrupoproduto setor, "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ano, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') mes, "
				+ " p.produtoid , "
				+ " p.nome_produto , "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde, "
				+ " trunc(sum(ei.qtde_entradaproducao * ei.vl_custounit_entradaproducaoit),2) total_custo  "
				+ " from entradaproducao e  "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid "
				+ " where p.tp_produto = 'COMPONENTE' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " and (s.subgrupoprodutoid = '"+setor+"' or '-2'= "+setor+") "
				+ " and (TO_CHAR(e.dt_entradaproducao,'DD') = '"+dia+"' or '-2' = '"+dia+"' )"
				+ " group by  "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY'), "
				+ " TO_CHAR(e.dt_entradaproducao,'MM'), "
				+ " s.nome_subgrupoproduto , "
				+ " p.produtoid , "
				+ " p.nome_produto "
				+ " union all  "
				+ " select  "
				+ " 'MONTAGEM' setor, "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ano, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') mes, "
				+ " p.produtoid , "
				+ " p.nome_produto , "
				+ " round(sum(ei.qtde_entradaproducao),0) qtde, "
				+ " trunc(sum(ei.qtde_entradaproducao * ei.vl_custounit_entradaproducaoit),2) total_custo  "
				+ " from entradaproducao e  "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid "
				+ " where p.tp_produto = 'ACABADO' "
				+ " and TO_CHAR(e.dt_entradaproducao,'MM') = '"+mes+"'"
				+ " and TO_CHAR(e.dt_entradaproducao,'YYYY') = '"+ano+"'"
				+ " and (99999 = '"+setor+"' or '-2'= "+setor+") "
				+ " and (TO_CHAR(e.dt_entradaproducao,'DD') = '"+dia+"' or '-2' = '"+dia+"' )"
				+ " group by  "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY'), "
				+ " TO_CHAR(e.dt_entradaproducao,'MM'), "
				+ " s.nome_subgrupoproduto , "
				+ " p.produtoid , "
				+ " p.nome_produto ";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);

		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ProducaoProduto pr = new ProducaoProduto();

			pr.setSetor((String)row[0]);
			pr.setAno((String)row[1]);
			pr.setMes((String)row[2]);
			pr.setProdutoid((BigDecimal)row[3]);
			pr.setNomeproduto((String)row[4]);
			pr.setQuantidade((BigDecimal)row[5]);
			pr.setValor((BigDecimal)row[6]);

			list.add(pr);
		}

		
		return list;
	}

	public List<CPedidoFin> cpedidofin(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String status, int bo_vencido ){
		List<CPedidoFin> p = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
				" SELECT "
				+ " idlog, "
				+ " data, "
				+ " descricao, "
				+ " status, "
				+ " pedido, "
				+ " usuario "
				+ " FROM dwbi_pedidolog "
				+ " inner join( "
				+ " select "
				+ " MAX(idlog) id, "
				+ " pedido ped "
				+ " from dwbi_pedidolog "
				+ " group by pedido "
				+ " )x on x.id = idlog "
				+ " where status = 'LIBERADO' ");
		List<Object[]> lista2 = query2.getResultList();
		for (Object[] row2 : lista2) {
			
			javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
					" select  "
							+ " pedido.pedidovendaid pedido, "
							+ " c.NOME_CADCFTV cliente, "
							+ " vv.NOME_CADCFTV vendedor, "
							+ " g.NOME_GESTOR, "
							+ " pedido.STATUS_PEDIDOVENDA statuspedido, "
							+ " nota.NR_NOTA_PEDIDOVENDA nrnota, "
							+ " nota.DT_FATURAMENTO_PEDIDOVENDA datafaturamento, "
							+ " nota.STATUS_PEDIDOVENDA statusnota, "
							+ " receber.ctareceberid, "
							+ " receber.NR_DOCUMENTO_CTARECEBER nrdocumento, "
							+ " receber.NR_PARCELA_CTARECEBER nrparcela, "
							+ " receber.STATUS_CTARECEBER statustitulo, "
							+ " receber.DT_CTARECEB dt_cadastro, "
							+ " receber.ORIGEM_CTARECEBER origem, "
							+ " receber.VL_TITULO_CTARECEBER valortitulo, "
							+ " receber.DT_VENCTO_CTARECEBER dt_vencimento, "
							+ " receber.DT_QUITACAO_CTARECEBER dt_quitacao, "
							+ " case when receber.STATUS_CTARECEBER  = 'ABERTO' AND receber.DT_VENCTO_CTARECEBER < (CURRENT_DATE-1) THEN 1 ELSE 0 END BO_VENCIDO "
							+ " from pedidovenda pedido "
							+ " inner join pedidovenda_item pedidoitem on pedidoitem.pedidovendaid = pedido.pedidovendaid  "
							+ " left join pedidovenda_item notaitem on notaitem.ORIGEM_PEDIDOVENDA_ITEM = pedidoitem.pedidovendaitemid  "
							+ " left join pedidovenda nota on nota.pedidovendaid = notaitem.pedidovendaid "
							+ " left join CTARECEBER receber on receber.VENDAID = nota.pedidovendaid "
							+ " inner join CADCFTV c on c.CADCFTVID = pedido.CADCFTVID "
							//+ " inner join cliente cl on cl.CADCFTVID = c.CADCFTVID "
							+ " inner join vendedor v on v.CADCFTVID = pedido.VENDEDOR1ID "
							+ " inner join CADCFTV vv on vv.CADCFTVID = v.CADCFTVID "
							+ " inner join gestor g on g.GESTORID = v.GESTORID "
							+ " where pedido.PEDIDOVENDAID = "+(String)row2[4]
							+ " and v.cadcftvid BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " '"	
							+ " AND g.gestorid BETWEEN ' " + gestor1 + " ' and ' " + gestor2 + " ' "
							+ " and pedido.cadcftvid between ' "+ cliente1 + " ' and ' " + cliente2 + " ' "
							+ " and nota.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
							+ " AND (receber.STATUS_CTARECEBER  = '"+status+"' OR 'TODOS' = '"+status+"' ) "
							+ " and (receber.STATUS_CTARECEBER  = 'ABERTO' AND receber.DT_VENCTO_CTARECEBER < (CURRENT_DATE-1) and 1 = "+bo_vencido+" or 0 = "+bo_vencido+") "
							+ " group by "
							+ " pedido.pedidovendaid , "
							+ " c.NOME_CADCFTV, "
							+ " vv.NOME_CADCFTV , "
							+ " g.NOME_GESTOR, "
							+ " pedido.STATUS_PEDIDOVENDA, "
							+ " nota.NR_NOTA_PEDIDOVENDA , "
							+ " nota.DT_FATURAMENTO_PEDIDOVENDA, "
							+ " nota.STATUS_PEDIDOVENDA , "
							+ " receber.NR_DOCUMENTO_CTARECEBER , "
							+ " receber.NR_PARCELA_CTARECEBER , "
							+ " receber.STATUS_CTARECEBER , "
							+ " receber.DT_CTARECEB, "
							+ " receber.DT_QUITACAO_CTARECEBER , "
							+ " receber.DT_VENCTO_CTARECEBER , "
							+ " receber.VL_TITULO_CTARECEBER , "
							+ " receber.ORIGEM_CTARECEBER, "
							+ " receber.ctareceberid "
							+ " order by receber.ctareceberid ");		
			
			List<Object[]> lista = query.getResultList();
			for (Object[] row : lista) {
				CPedidoFin ped = new CPedidoFin();
				
				ped.setPedido((BigDecimal)row[0]);
				ped.setNomecliente((String)row[1]);
				ped.setNomevendedor((String)row[2]);
				ped.setNomegestor((String)row[3]);
				ped.setStatuspedido((String)row[4]);
				ped.setNrnota((String)row[5]);
				ped.setDatafaturamento((Date)row[6]);
				ped.setStatusnota((String)row[7]);
				ped.setTituloid((BigDecimal)row[8]);
				ped.setNrdocumento((String)row[9]);
				ped.setNrparcela((String)row[10]);
				ped.setStatustitulo((String)row[11]);
				ped.setDatatitulo((Date)row[12]);
				ped.setOrigem((String)row[13]);
				ped.setValortitulo((BigDecimal)row[14]);
				ped.setDatavencimento((Date)row[15]);
				ped.setDataquitacao((Date)row[16]);
				ped.setBo_vencido((Integer)row[17]);
				
				p.add(ped);
			}
		}
		
		
		return p;
	}
	
	public List<CPedido> cpedido(BigDecimal pedido){
		List<CPedido> p = new ArrayList<>();
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"select "
				+ " p.pedidovendaid, "
				+ " p.CADCFTVID cliente, "
				+ " c.NOME_CADCFTV nomecliente, "
				+ " p.DT_PEDIDOVENDA, "
				+ " p.VL_TOTALPROD_PEDIDOVENDA valor, "
				+ " tp.DESC_TIPO_PEDIDO tipopedido, "
				+ " p.STATUS_PEDIDOVENDA status, "
				+ " c.NOME_CADCFTV nomevendedor, "
				+ " g.NOME_GESTOR "
				+ " "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " inner join CADCFTV v on v.CADCFTVID = p.VENDEDOR1ID "
				+ " inner join vendedor vv on vv.CADCFTVID = v.CADCFTVID "
				+ " inner join gestor g on g.GESTORID = vv.GESTORID "
				+ " "
				+ " where p.pedidovendaid = '"+pedido+"' ");
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			CPedido ped = new CPedido();
			
			ped.setPedido((BigDecimal)row[0]);
			ped.setCodigocliente((BigDecimal)row[1]);
			ped.setNomecliente((String)row[2]);
			ped.setDatapedido((Date)row[3]);
			ped.setValortotalpedido((BigDecimal)row[4]);
			ped.setTipopedido((String)row[5]);
			ped.setStatuspedido((String)row[6]);
			ped.setNomevendedor((String)row[7]);
			ped.setNomegestor((String)row[8]);
			
			javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
			" SELECT "
			+ " idlog, "
			+ " data, "
			+ " descricao, "
			+ " status, "
			+ " pedido, "
			+ " usuario "
			+ " FROM dwbi_pedidolog "
			+ " inner join( "
			+ " select "
			+ " MAX(idlog) id, "
			+ " pedido ped "
			+ " from dwbi_pedidolog "
			+ " group by pedido "
			+ " )x on x.id = idlog "
			+ " where pedido = '"+ped.getPedido().toString()+"'");
			List<Object[]> lista2 = query2.getResultList();
			for (Object[] row2 : lista2) {
				ped.setLiberado((String)row2[3]);
				ped.setUsuario((String)row2[5]);
				ped.setDataliberado((Date)row2[1]);
			}

			p.add(ped);
		}
		return p;
	}
	
	public List<CPedido> cpedidoLista(Date data1, Date data2, String status){
		List<CPedido> p = new ArrayList<>();
		//data para o sige
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String dataFormatada = formato.format(data1);
		
		//ajustar data +1 para filtrar corretamente no sige
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(data2); 
		cal.add(Calendar.DATE, 1);
		data2 = cal.getTime();
		
		String dataFormatada2 = formato.format(data2);
		
		//System.out.println(dataFormatada+"-"+dataFormatada2);
		
		javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
				" SELECT "
				+ " idlog, "
				+ " data, "
				+ " descricao, "
				+ " status, "
				+ " pedido, "
				+ " usuario "
				+ " FROM dwbi_pedidolog "
				+ " inner join( "
				+ " select "
				+ " MAX(idlog) id, "
				+ " pedido ped "
				+ " from dwbi_pedidolog "
				+ " group by pedido "
				+ " )x on x.id = idlog "
				+ " where data between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and status = '"+status+"'");
		
				List<Object[]> lista2 = query2.getResultList();
				for (Object[] row2 : lista2) {
					CPedido ped = new CPedido();
					
					ped.setLiberado((String)row2[3]);
					ped.setUsuario((String)row2[5]);
					ped.setDataliberado((Date)row2[1]);
					ped.setObservacao((String) row2[2]);

					javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
							"select "
							+ " p.pedidovendaid, "
							+ " p.CADCFTVID cliente, "
							+ " c.NOME_CADCFTV nomecliente, "
							+ " p.DT_PEDIDOVENDA, "
							+ " p.VL_TOTALPROD_PEDIDOVENDA valor, "
							+ " tp.DESC_TIPO_PEDIDO tipopedido, "
							+ " p.STATUS_PEDIDOVENDA status, "
							+ " c.NOME_CADCFTV nomevendedor, "
							+ " g.NOME_GESTOR "
							+ " "
							+ " from pedidovenda p "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
							+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
							+ " inner join CADCFTV v on v.CADCFTVID = p.VENDEDOR1ID "
							+ " inner join vendedor vv on vv.CADCFTVID = v.CADCFTVID "
							+ " inner join gestor g on g.GESTORID = vv.GESTORID "
							+ " "
							+ " where p.pedidovendaid = '"+(String)row2[4]+"' ");
					
					List<Object[]> lista = query.getResultList();
					for (Object[] row : lista) {
						
						ped.setPedido((BigDecimal)row[0]);
						ped.setCodigocliente((BigDecimal)row[1]);
						ped.setNomecliente((String)row[2]);
						ped.setDatapedido((Date)row[3]);
						ped.setValortotalpedido((BigDecimal)row[4]);
						ped.setTipopedido((String)row[5]);
						ped.setStatuspedido((String)row[6]);
						ped.setNomevendedor((String)row[7]);
						ped.setNomegestor((String)row[8]);
						
						p.add(ped);
					}			
				}		
		return p;
	}
	
	public List<CPedidoLog> cpedidolog(String pedido){
		List<CPedidoLog> p = new ArrayList<>();
		javax.persistence.Query query2 = (javax.persistence.Query) managerSige.createNativeQuery(
				" SELECT "
				+ " idlog, "
				+ " data, "
				+ " descricao, "
				+ " status, "
				+ " pedido, "
				+ " usuario "
				+ " FROM dwbi_pedidolog "
				+ " where pedido = '"+pedido+"'");
				List<Object[]> lista2 = query2.getResultList();
				for (Object[] row2 : lista2) {
					CPedidoLog ped = new CPedidoLog();
					
					ped.setIdlog((Integer) row2[0]);
					ped.setData((Date) row2[1]);
					ped.setDescricao((String)row2[2]);
					ped.setStatus((String)row2[3]);
					ped.setPedido((String)row2[4]);
					ped.setUsuario((String) row2[5]);
					
					p.add(ped);
				}
			return p;
	}
	
	public List<Produto> produtos(){
		List<Produto> list = new ArrayList<>();
		
		String sql =
				"select "
				+ " to_char(pr.produtoid) produtoid,  "
				+ "pr.REFERENCIA_PRODUTO, "
				+ "pr.NOME_PRODUTO, "
				+ "pr.SUBGRUPOPRODUTOID, "
				+ "sub.GRUPOPRODUTOID "
				//+ " im.imagem_produto  "
				+ "from produto pr "
				+ "inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ "inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " left join produto_imagem im on im.produtoid = pr.produtoid "
				+ "WHERE pr.TP_PRODUTO = 'ACABADO' ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Produto p = new Produto();
			p.setProdutoid((String)row[0]);
			p.setReferencia((String) row[1]);
			p.setNomeproduto((String) row[2]);
			p.setSubgrupoid((BigDecimal)row[3]);
			p.setGrupoid((BigDecimal) row[4]);
			//p.setImg((Blob)row[5]);
			
			list.add(p);
		}
		
		return list;
	}
	
	public List<Produto> produtosgrupo(String grupo){
		List<Produto> list = new ArrayList<>();
		
		String sql =
				"select "
				+ "to_char(pr.produtoid) id, "
				+ "pr.REFERENCIA_PRODUTO, "
				+ "pr.NOME_PRODUTO, "
				+ "pr.SUBGRUPOPRODUTOID, "
				+ "sub.GRUPOPRODUTOID, "
				+ " im.imagem_produto  "
				+ "from produto pr "
				+ "inner join SUBGRUPOPRODUTO sub on sub.SUBGRUPOPRODUTOID = pr.SUBGRUPOPRODUTOID "
				+ "inner join GRUPOPRODUTO gr on gr.GRUPOPRODUTOID = sub.GRUPOPRODUTOID "
				+ " left join produto_imagem im on im.produtoid = pr.produtoid "
				+ " WHERE pr.TP_PRODUTO = 'ACABADO' " //and pr.produtoid ='11179' "
				+ " and gr.GRUPOPRODUTOID = '"+grupo+"' ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Produto p = new Produto();
			p.setProdutoid((String)row[0]);
			p.setReferencia((String) row[1]);
			p.setNomeproduto((String) row[2]);
			p.setSubgrupoid((BigDecimal)row[3]);
			p.setGrupoid((BigDecimal) row[4]);
			p.setImg((byte[])row[5]);
			
			list.add(p);
		}
		
		return list;
	}
	
	
	public Imagem imagem(String produtoid) {
		Imagem img = new Imagem();
		
		String sql =
				"select "
				+ "pr.produtoid, "
				+" im.imagem_produto  "
				+ "from produto pr "
				+ " left join produto_imagem im on im.produtoid = pr.produtoid "
				+ "WHERE pr.TP_PRODUTO = 'ACABADO' and pr.produtoid = '"+produtoid+"' ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			img.setProdutoid((BigDecimal)row[0]);
			img.setImg((byte[])row[1]);
		}
		
		return img;
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
	
	public List<ClientesAtivosAno> clientesativosano(String vendedor1, String vendedor2, String gestor1, String gestor2, String ano){
		List<ClientesAtivosAno> list = new ArrayList<>();
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select  "
				+ "G.NOME_GESTOR, "
				+ "V.CADCFTVID VENDEDOR, "
				+ "V.NOME_CADCFTV NOMEVENDEDOR, "
				+ "'"+ano+"' ANO, "
				+ "COUNT(JANEIRO.MES) JANEIRO, "
				+ "COUNT(FEV.MES) FEVEREIRO, "
				+ "count(mar.mes) MARCO, "
				+ "COUNT(ABRIL.MES) ABRIL, "
				+ "COUNT(MAIO.MES) MAIO, "
				+ "COUNT(JUNHO.MES) JUNHO, "
				+ "COUNT(JULHO.MES) JULHO, "
				+ "COUNT(AGOS.MES) AGOSTO, "
				+ "COUNT(SETEM.MES) SETEMBRO, "
				+ "COUNT(OUTO.MES) OUTUBRO, "
				+ "COUNT(NOV.MES) NOVEMBRO, "
				+ "COUNT(DEZ.MES) DEZEMBRO "
				+ " "
				+ "from CADCFTV c "
				+ "inner join cliente cl on cl.CADCFTVID = c.CADCFTVID "
				+ "INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ "INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = CL.VENDEDORID1 "
				+ "INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'01' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/01/"+ano+"')+1,-6) AND TO_DATE('31/01/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/01/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/01/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")JANEIRO on JANEIRO.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'02' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('28/02/"+ano+"')+1,-6) AND TO_DATE('28/02/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('28/02/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('28/02/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")FEV on FEV.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'03' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/03/"+ano+"')+1,-6) AND TO_DATE('31/03/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/03/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/03/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")mar on mar.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'04' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('30/04/"+ano+"')+1,-6) AND TO_DATE('30/04/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('30/04/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('30/04/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")ABRIL on ABRIL.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'05' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/05/"+ano+"')+1,-6) AND TO_DATE('31/05/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/05/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/05/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")MAIO on MAIO.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'06' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('30/06/"+ano+"')+1,-6) AND TO_DATE('30/06/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('30/06/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('30/06/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")JUNHO on JUNHO.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'07' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/07/"+ano+"')+1,-6) AND TO_DATE('31/07/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/07/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/07/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")JULHO on JULHO.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'08' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/08/"+ano+"')+1,-6) AND TO_DATE('31/08/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/08/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/08/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")AGOS on AGOS.CADCFTVID = c.CADCFTVID  "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'09' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('30/09/"+ano+"')+1,-6) AND TO_DATE('30/09/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('30/09/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('30/09/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")SETEM on SETEM.CADCFTVID = c.CADCFTVID "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'10' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/10/"+ano+"')+1,-6) AND TO_DATE('31/10/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/10/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/10/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")OUTO on OUTO.CADCFTVID = c.CADCFTVID "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'11' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('30/11/"+ano+"')+1,-6) AND TO_DATE('30/11/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('30/11/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('30/11/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")NOV on NOV.CADCFTVID = c.CADCFTVID "
				+ " "
				+ "left join(  "
				+ "select  "
				+ "p.CADCFTVID, "
				+ "'12' MES "
				+ "from pedidovenda p  "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				//+ "AND p.DT_PEDIDOVENDA BETWEEN ADD_MONTHS(TO_DATE('31/12/"+ano+"')+1,-6) AND TO_DATE('31/12/"+ano+"') "
				+ " AND p.DT_PEDIDOVENDA BETWEEN (TO_DATE('31/12/"+ano+"')+ INTERVAL '1 day')- INTERVAL '6 month' AND TO_DATE('31/12/"+ano+"') "
				+ "group by p.CADCFTVID  "
				+ ")DEZ on DEZ.CADCFTVID = c.CADCFTVID "
				+ " "
				+ "WHERE v.cadcftvid BETWEEN ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ "AND v2.gestorid BETWEEN ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " "
				+ "GROUP BY "
				+ "G.NOME_GESTOR,V.CADCFTVID,V.NOME_CADCFTV ");
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ClientesAtivosAno cliente= new ClientesAtivosAno();
			
			cliente.setGestor((String) row[0]);
			cliente.setCodigovendedor((BigDecimal) row[1]);
			cliente.setNomevendedor((String) row[2]);
			cliente.setAno((String) row[3]);
			cliente.setJaneiro((BigInteger) row[4]);
			cliente.setFevereiro((BigInteger) row[5]);
			cliente.setMarco((BigInteger) row[6]);
			cliente.setAbril((BigInteger) row[7]);
			cliente.setMaio((BigInteger) row[8]);
			cliente.setJunho((BigInteger) row[9]);
			cliente.setJulho((BigInteger) row[10]);
			cliente.setAgosto((BigInteger) row[11]);
			cliente.setSetembro((BigInteger) row[12]);
			cliente.setOutubro((BigInteger) row[13]);
			cliente.setNovembro((BigInteger) row[14]);
			cliente.setDezembro((BigInteger) row[15]);
			
			list.add(cliente);
	}
		
		return list;
	}
	
	public List<NovasVendas_Cliente> novasvendas_cliente(String ano){
		List<NovasVendas_Cliente> list = new ArrayList<>();
		
		String sql = ""
				+ "select mes,nmes,sum(novos_venda)novos_venda, sum(novos_cadastro)novos_cadastro from( "
				+ "select '01' mes, 'jan' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '01' mes, 'jan' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '02' mes, 'fev' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '02' mes, 'fev' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '03' mes, 'mar' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '03' mes, 'mar' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '04' mes, 'abr' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '04' mes, 'abr' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '05' mes, 'mai' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '05' mes, 'mai' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '06' mes, 'jun' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '06' mes, 'jun' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '07' mes, 'jul' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '07' mes, 'jul' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '08' mes, 'ago' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '08' mes, 'ago' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '09' mes, 'set' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '09' mes, 'set' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '10' mes, 'out' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '10' mes, 'out' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '11' mes, 'nov' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '11' mes, 'nov' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all "
				+ "select '12' mes, 'dez' nmes, count(p.cadcftvid) novos_venda, 0 novos_cadastro  from( "
				+ "select p.cadcftvid,min(p.DT_PEDIDOVENDA) primeiracompra from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "group by p.cadcftvid )p  where p.primeiracompra between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select '12' mes, 'dez' nmes, 0 novos_venda, count(c.cadcftvid) novos_cadastro "
				+ "from  cadcftv c "
				+ "inner join cliente cl on cl.CADCFTVID = c.cadcftvid  "
				+ "WHERE c.ATIVO_CADCFTV = 'SIM' and c.funcao_principal_cadcftv = 'CLIENTE' "
				+ "and c.DATACREATE_CADCFTV between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ ")x group by mes,nmes";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
				
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			NovasVendas_Cliente cli= new NovasVendas_Cliente();
					
			cli.setAno(ano);
			cli.setMes((String)row[0]);
			cli.setNmes((String)row[1]);
			cli.setNovosvenda((BigDecimal)row[2]);
			cli.setNovoscadastro((BigDecimal)row[3]);
			list.add(cli);
		}	
		
		return list;			
	}
	
	public List<Orcamentos> orcamentos(String ano){
		List<Orcamentos> list = new ArrayList<>();
		
		String sql = ""
				+ "select mes,nmes,sum(cancelados)cancelados, sum(abertos)abertos from( "
				+ "select '01' mes, 'jan' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '01' mes, 'jan' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-01-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '02' mes, 'fev' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '02' mes, 'fev' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-02-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '03' mes, 'mar' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '03' mes, 'mar' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-03-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '04' mes, 'abr' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '04' mes, 'abr' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-04-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '05' mes, 'mai' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '05' mes, 'mai' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-05-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '06' mes, 'jun' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '06' mes, 'jun' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-06-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '07' mes, 'jul' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '07' mes, 'jul' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-07-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '08' mes, 'ago' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '08' mes, 'ago' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-08-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '09' mes, 'set' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '09' mes, 'set' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-09-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '10' mes, 'out' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '10' mes, 'out' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-10-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '11' mes, 'nov' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '11' mes, 'nov' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-11-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ "union all "
				+ "select '12' mes, 'dez' nmes, count(x.pedidovendaid) cancelados, 0 abertos from( "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid = 11 and origem_cancelamento = 'PEDIDO' "
				+ "and p.DT_PEDIDOVENDA between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select p.pedidovendaid from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('CANCELADO')    "
				+ "and p.tipopedidoid in (11,1,6) and origem_cancelamento = 'ORCAMENTO' "
				+ "and p.DT_PEDIDOVENDA between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') )x "
				+ "union all "
				+ "select '12' mes, 'dez' nmes, 0 cancelados, count(x.pedidovendaid) abertos from( "
				+ "select  "
				+ "p.pedidovendaid  "
				+ "from pedidovenda p     "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID     "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ORCAMENTO')    "
				+ "and p.tipopedidoid in (11,1,6) "
				+ "and p.DT_PEDIDOVENDA between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ "union all  "
				+ "select P.pedidovendaid from pedidovenda p       "
				+ "inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID   "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')     "
				+ "and p.tipopedidoid = 11  "
				+ "and p.DT_PEDIDOVENDA between '01-12-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY'))x "
				+ ")x group by mes,nmes";
		
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Orcamentos cli= new Orcamentos();
							
			cli.setAno(ano);
			cli.setMes((String)row[0]);
			cli.setNmes((String)row[1]);
			cli.setCancelados((BigDecimal)row[2]);
			cli.setAbertos((BigDecimal)row[3]);
			list.add(cli);
		}	
				
		return list;	
	}
	public List<VendaUF> vendaufpedido(String ano,String mes){
		List<VendaUF> list = new ArrayList<>();
		String sql = ""
				+ "select  "
				+ "x.tipo,x.uf,x.ano,x.mes, "
				+ "sum(x.dia01) dia01,sum(x.dia02) dia02,sum(x.dia03) dia03,sum(x.dia04) dia04,sum(x.dia05) dia05, "
				+ "sum(x.dia06) dia06,sum(x.dia07) dia07,sum(x.dia08) dia08,sum(x.dia09) dia09,sum(x.dia10) dia10, "
				+ "sum(x.dia11) dia11,sum(x.dia12) dia12,sum(x.dia13) dia13,sum(x.dia14) dia14,sum(x.dia15) dia15, "
				+ "sum(x.dia16) dia16,sum(x.dia17) dia17,sum(x.dia18) dia18,sum(x.dia19) dia19,sum(x.dia20) dia20, "
				+ "sum(x.dia21) dia21,sum(x.dia22) dia22,sum(x.dia23) dia23,sum(x.dia24) dia24,sum(x.dia25) dia25, "
				+ "sum(x.dia26) dia26,sum(x.dia27) dia27,sum(x.dia28) dia28,sum(x.dia29) dia29,sum(x.dia30) dia30,sum(x.dia31) dia31  "
				+ "from( "
				+ "select  'PEDIDO' tipo, en.uf_cidade uf, TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia01, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia02, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '03' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia03, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '04' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia04, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '05' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia05, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '06' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia06, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '07' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia07, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '08' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia08, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '09' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia09, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '10' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia10, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '11' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia11, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '12' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia12, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '13' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia13, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '14' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia14, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '15' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia15, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '16' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia16, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '17' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia17, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '18' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia18, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '19' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia19, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '20' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia20, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '21' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia21, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '22' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia22, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '23' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia23, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '24' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia24, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '25' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia25, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '26' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia26, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '27' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia27, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '28' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia28, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '29' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia29, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '30' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia30, "
				+ "case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '31' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia31 "
				+ "from pedidovenda p   "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "LEFT join  "
				+ "( "
				+ "SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V  "
				+ "inner join(  "
				+ "SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV   "
				+ "group by cadcftvid  "
				+ ")x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID  "
				+ "INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID  "
				+ ") EN ON EN.CADCFTVID = p.CADCFTVID  "
				+ "where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ "and p.DT_PEDIDOVENDA between '01-"+mes+"-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-"+mes+"-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ ")x "
				+ "group by x.tipo,x.uf,x.ano,x.mes";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			VendaUF vuf =  new VendaUF();
			
			vuf.setTipo((String)row[0]);
			vuf.setUf((String)row[1]);
			vuf.setAno((String)row[2]);
			vuf.setMes((String)row[3]);
			vuf.setDia1((BigDecimal)row[4]);
			vuf.setDia2((BigDecimal)row[5]);
			vuf.setDia3((BigDecimal)row[6]);
			vuf.setDia4((BigDecimal)row[7]);
			vuf.setDia5((BigDecimal)row[8]);
			vuf.setDia6((BigDecimal)row[9]);
			vuf.setDia7((BigDecimal)row[10]);
			vuf.setDia8((BigDecimal)row[11]);
			vuf.setDia9((BigDecimal)row[12]);
			vuf.setDia10((BigDecimal)row[13]);
			vuf.setDia11((BigDecimal)row[14]);
			vuf.setDia12((BigDecimal)row[15]);
			vuf.setDia13((BigDecimal)row[16]);
			vuf.setDia14((BigDecimal)row[17]);
			vuf.setDia15((BigDecimal)row[18]);
			vuf.setDia16((BigDecimal)row[19]);
			vuf.setDia17((BigDecimal)row[20]);
			vuf.setDia18((BigDecimal)row[21]);
			vuf.setDia19((BigDecimal)row[22]);
			vuf.setDia20((BigDecimal)row[23]);
			vuf.setDia21((BigDecimal)row[24]);
			vuf.setDia22((BigDecimal)row[25]);
			vuf.setDia23((BigDecimal)row[26]);
			vuf.setDia24((BigDecimal)row[27]);
			vuf.setDia25((BigDecimal)row[28]);
			vuf.setDia26((BigDecimal)row[29]);
			vuf.setDia27((BigDecimal)row[30]);
			vuf.setDia28((BigDecimal)row[31]);
			vuf.setDia29((BigDecimal)row[32]);
			vuf.setDia30((BigDecimal)row[33]);
			vuf.setDia31((BigDecimal)row[34]);
			
		
			list.add(vuf);
		}			
		return list;
	}
	
	
	public List<VendaVendedor> vendavendedorfatura(String ano, String mes){
		List<VendaVendedor> list = new ArrayList<>();
		String sql = ""
				+ " select  "
				+ " x.vendedor,x.nome,x.ano,x.mes, "
				+ " sum(x.dia01) dia01,sum(x.dia02) dia02,sum(x.dia03) dia03,sum(x.dia04) dia04,sum(x.dia05) dia05, "
				+ " sum(x.dia06) dia06,sum(x.dia07) dia07,sum(x.dia08) dia08,sum(x.dia09) dia09,sum(x.dia10) dia10, "
				+ " sum(x.dia11) dia11,sum(x.dia12) dia12,sum(x.dia13) dia13,sum(x.dia14) dia14,sum(x.dia15) dia15, "
				+ " sum(x.dia16) dia16,sum(x.dia17) dia17,sum(x.dia18) dia18,sum(x.dia19) dia19,sum(x.dia20) dia20, "
				+ " sum(x.dia21) dia21,sum(x.dia22) dia22,sum(x.dia23) dia23,sum(x.dia24) dia24,sum(x.dia25) dia25, "
				+ " sum(x.dia26) dia26,sum(x.dia27) dia27,sum(x.dia28) dia28,sum(x.dia29) dia29,sum(x.dia30) dia30,sum(x.dia31) dia31,"
				+ " sum(x.dia01)+sum(x.dia02)+sum(x.dia03)+sum(x.dia04)+sum(x.dia05)+sum(x.dia06)+sum(x.dia07)+sum(x.dia08)+sum(x.dia09)+sum(x.dia10)+sum(x.dia11)+sum(x.dia12)+sum(x.dia13)+sum(x.dia14)+sum(x.dia15)+sum(x.dia16)+sum(x.dia17)+sum(x.dia18)+sum(x.dia19)+sum(x.dia20)+sum(x.dia21)+sum(x.dia22)+sum(x.dia23)+sum(x.dia24)+sum(x.dia25)+sum(x.dia26)+sum(x.dia27)+sum(x.dia28)+sum(x.dia29)+sum(x.dia30)+sum(x.dia31) total "
				+ " from( "
				+ " select v.cadcftvid vendedor, v.nome_cadcftv nome , TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia01, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia02, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '03' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia03, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '04' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia04, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '05' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia05, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '06' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia06, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '07' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia07, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '08' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia08, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '09' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia09, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '10' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia10, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '11' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia11, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '12' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia12, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '13' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia13, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '14' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia14, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '15' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia15, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '16' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia16, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '17' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia17, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '18' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia18, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '19' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia19, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '20' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia20, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '21' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia21, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '22' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia22, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '23' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia23, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '24' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia24, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '25' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia25, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '26' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia26, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '27' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia27, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '28' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia28, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '29' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia29, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '30' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia30, "
				+ " case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '31' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia31 "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join cadcftv v on v.cadcftvid = p.vendedor1id  "
				+ " where p.STATUS_PEDIDOVENDA = 'FATURADO'   "
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_FATURAMENTO_PEDIDOVENDA between '01-"+mes+"-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-"+mes+"-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ " )x "
				+ " group by x.vendedor,x.nome,x.ano,x.mes ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
			for (Object[] row : lista) {
				VendaVendedor vuf =  new VendaVendedor();
				
				vuf.setVendedor((BigDecimal)row[0]);
				vuf.setNome((String)row[1]);
				vuf.setAno((String)row[2]);
				vuf.setMes((String)row[3]);
				vuf.setDia1((BigDecimal)row[4]);
				vuf.setDia2((BigDecimal)row[5]);
				vuf.setDia3((BigDecimal)row[6]);
				vuf.setDia4((BigDecimal)row[7]);
				vuf.setDia5((BigDecimal)row[8]);
				vuf.setDia6((BigDecimal)row[9]);
				vuf.setDia7((BigDecimal)row[10]);
				vuf.setDia8((BigDecimal)row[11]);
				vuf.setDia9((BigDecimal)row[12]);
				vuf.setDia10((BigDecimal)row[13]);
				vuf.setDia11((BigDecimal)row[14]);
				vuf.setDia12((BigDecimal)row[15]);
				vuf.setDia13((BigDecimal)row[16]);
				vuf.setDia14((BigDecimal)row[17]);
				vuf.setDia15((BigDecimal)row[18]);
				vuf.setDia16((BigDecimal)row[19]);
				vuf.setDia17((BigDecimal)row[20]);
				vuf.setDia18((BigDecimal)row[21]);
				vuf.setDia19((BigDecimal)row[22]);
				vuf.setDia20((BigDecimal)row[23]);
				vuf.setDia21((BigDecimal)row[24]);
				vuf.setDia22((BigDecimal)row[25]);
				vuf.setDia23((BigDecimal)row[26]);
				vuf.setDia24((BigDecimal)row[27]);
				vuf.setDia25((BigDecimal)row[28]);
				vuf.setDia26((BigDecimal)row[29]);
				vuf.setDia27((BigDecimal)row[30]);
				vuf.setDia28((BigDecimal)row[31]);
				vuf.setDia29((BigDecimal)row[32]);
				vuf.setDia30((BigDecimal)row[33]);
				vuf.setDia31((BigDecimal)row[34]);	
				vuf.setTotal((BigDecimal)row[35]);	
				
				list.add(vuf);
			}
		
		return list;
		
	}
	
	public List<VendaVendedor> vendavendedor(String ano, String mes){
		List<VendaVendedor> list = new ArrayList<>();
		String sql = ""
				+ " select  "
				+ " x.vendedor,x.nome,x.ano,x.mes, "
				+ " sum(x.dia01) dia01,sum(x.dia02) dia02,sum(x.dia03) dia03,sum(x.dia04) dia04,sum(x.dia05) dia05, "
				+ " sum(x.dia06) dia06,sum(x.dia07) dia07,sum(x.dia08) dia08,sum(x.dia09) dia09,sum(x.dia10) dia10, "
				+ " sum(x.dia11) dia11,sum(x.dia12) dia12,sum(x.dia13) dia13,sum(x.dia14) dia14,sum(x.dia15) dia15, "
				+ " sum(x.dia16) dia16,sum(x.dia17) dia17,sum(x.dia18) dia18,sum(x.dia19) dia19,sum(x.dia20) dia20, "
				+ " sum(x.dia21) dia21,sum(x.dia22) dia22,sum(x.dia23) dia23,sum(x.dia24) dia24,sum(x.dia25) dia25, "
				+ " sum(x.dia26) dia26,sum(x.dia27) dia27,sum(x.dia28) dia28,sum(x.dia29) dia29,sum(x.dia30) dia30,sum(x.dia31) dia31,  "
				+ " sum(x.dia01)+sum(x.dia02)+sum(x.dia03)+sum(x.dia04)+sum(x.dia05)+sum(x.dia06)+sum(x.dia07)+sum(x.dia08)+sum(x.dia09)+sum(x.dia10)+sum(x.dia11)+sum(x.dia12)+sum(x.dia13)+sum(x.dia14)+sum(x.dia15)+sum(x.dia16)+sum(x.dia17)+sum(x.dia18)+sum(x.dia19)+sum(x.dia20)+sum(x.dia21)+sum(x.dia22)+sum(x.dia23)+sum(x.dia24)+sum(x.dia25)+sum(x.dia26)+sum(x.dia27)+sum(x.dia28)+sum(x.dia29)+sum(x.dia30)+sum(x.dia31) total "
				+ " from( "
				+ " select v.cadcftvid vendedor, v.nome_cadcftv nome , TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia01, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia02, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '03' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia03, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '04' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia04, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '05' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia05, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '06' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia06, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '07' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia07, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '08' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia08, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '09' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia09, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '10' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia10, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '11' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia11, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '12' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia12, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '13' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia13, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '14' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia14, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '15' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia15, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '16' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia16, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '17' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia17, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '18' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia18, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '19' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia19, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '20' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia20, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '21' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia21, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '22' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia22, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '23' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia23, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '24' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia24, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '25' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia25, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '26' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia26, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '27' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia27, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '28' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia28, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '29' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia29, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '30' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia30, "
				+ " case when TO_CHAR(p.DT_PEDIDOVENDA,'DD') = '31' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia31 "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " inner join cadcftv v on v.cadcftvid = p.vendedor1id  "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between '01-"+mes+"-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-"+mes+"-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ " )x "
				+ " group by x.vendedor,x.nome,x.ano,x.mes ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
			for (Object[] row : lista) {
				VendaVendedor vuf =  new VendaVendedor();
				
				vuf.setVendedor((BigDecimal)row[0]);
				vuf.setNome((String)row[1]);
				vuf.setAno((String)row[2]);
				vuf.setMes((String)row[3]);
				vuf.setDia1((BigDecimal)row[4]);
				vuf.setDia2((BigDecimal)row[5]);
				vuf.setDia3((BigDecimal)row[6]);
				vuf.setDia4((BigDecimal)row[7]);
				vuf.setDia5((BigDecimal)row[8]);
				vuf.setDia6((BigDecimal)row[9]);
				vuf.setDia7((BigDecimal)row[10]);
				vuf.setDia8((BigDecimal)row[11]);
				vuf.setDia9((BigDecimal)row[12]);
				vuf.setDia10((BigDecimal)row[13]);
				vuf.setDia11((BigDecimal)row[14]);
				vuf.setDia12((BigDecimal)row[15]);
				vuf.setDia13((BigDecimal)row[16]);
				vuf.setDia14((BigDecimal)row[17]);
				vuf.setDia15((BigDecimal)row[18]);
				vuf.setDia16((BigDecimal)row[19]);
				vuf.setDia17((BigDecimal)row[20]);
				vuf.setDia18((BigDecimal)row[21]);
				vuf.setDia19((BigDecimal)row[22]);
				vuf.setDia20((BigDecimal)row[23]);
				vuf.setDia21((BigDecimal)row[24]);
				vuf.setDia22((BigDecimal)row[25]);
				vuf.setDia23((BigDecimal)row[26]);
				vuf.setDia24((BigDecimal)row[27]);
				vuf.setDia25((BigDecimal)row[28]);
				vuf.setDia26((BigDecimal)row[29]);
				vuf.setDia27((BigDecimal)row[30]);
				vuf.setDia28((BigDecimal)row[31]);
				vuf.setDia29((BigDecimal)row[32]);
				vuf.setDia30((BigDecimal)row[33]);
				vuf.setDia31((BigDecimal)row[34]);	
				vuf.setTotal((BigDecimal)row[35]);	
				
				list.add(vuf);
			}
		
		return list;
		
	}
	
	public List<P1_FaturadoDia> p1faturadodia(Date data1, Date data2){
		List<P1_FaturadoDia> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql = ""
				+ " select "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') dia, "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) faturado "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.STATUS_PEDIDOVENDA = 'FATURADO' "
				+ " AND CF.tipooperacao_cfop = 'VENDA' and p.tp_nota_pedidovenda = 'NORMAL' "
				+ " and p.DT_FATURAMENTO_PEDIDOVENDA between '"+ dataFormatada +" ' and '" +dataFormatada2 +"' "
				+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ,TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') ,TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') "
				+ " order by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ,TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') ,TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			P1_FaturadoDia vc =  new P1_FaturadoDia();
			
			vc.setAno((String)row[0]);
			vc.setMes((String)row[1]);
			vc.setDia((String)row[2]);
			vc.setFaturado((BigDecimal)row[3]);
										
			list.add(vc);
		}	
		return list;
	}
	
	public List<P1_FaturadoDia> p1pedidodia(Date data1, Date data2){
		List<P1_FaturadoDia> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql = ""
				+ " select "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'DD') dia, "
				+ " round(sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item),2) pedido "
				+ " from pedidovenda_item  g  "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.DT_PEDIDOVENDA between '"+ dataFormatada +" ' and '" +dataFormatada2 +"' "
				+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ,TO_CHAR(p.DT_PEDIDOVENDA,'MM') ,TO_CHAR(p.DT_PEDIDOVENDA,'DD') "
				+ " order by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ,TO_CHAR(p.DT_PEDIDOVENDA,'MM') ,TO_CHAR(p.DT_PEDIDOVENDA,'DD') ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			P1_FaturadoDia vc =  new P1_FaturadoDia();
			
			vc.setAno((String)row[0]);
			vc.setMes((String)row[1]);
			vc.setDia((String)row[2]);
			vc.setFaturado((BigDecimal)row[3]);
										
			list.add(vc);
		}	
		return list;
	}
	
	public List<Titulo> titulos(String cliente, String status, String nota){
		List<Titulo> list = new ArrayList<>();
		
		String sql = ""
			+ "  select * from( "
			+ " select  "
			+ " c.ctareceberid , "
			+ " f2.sigla_filial , "
			+ " c.nr_documento_ctareceber , "
			+ " c.nr_parcela_ctareceber , "
			+ " c.cadcftvid cliente, "
			+ " c2.nome_cadcftv nome_cliente , "
			+ " en.uf_cidade uf, "
			+ " en.nome_cidade cidade, "
			+ " f.nome_formacob, "
			+ " to_char(c.dt_vencto_ctareceber, 'DD/MM/YYYY') dt_vencimento, "
			+ " c.status_ctareceber status, "
			+ " case  "
			+ " 	when c.status_ctareceber = 'ABERTO' and extract(days from c.dt_vencto_ctareceber - current_date) < 0 then 'VENCIDO' "
			+ " 	when c.status_ctareceber = 'ABERTO' and extract(days from c.dt_vencto_ctareceber - current_date) >= 0 then 'A VENCER' "
			+ " end as status2, "
			+ " c.origem_ctareceber origem, "
			+ " to_char(c.vl_titulo_ctareceber, 'L9G999G990D99') vl_titulo_ctareceber , "
			+ " mail.email_emailcadcftv  emailfinanceiro, "
			+ " outro.emailoutro "
			+ " from ctareceber c  "
			+ " inner join formacobranca f on f.formacobrancaid = c.formacobrancaid  "
			+ " inner join filial f2 on f2.filialid = c.filialid  "
			+ " inner join cadcftv c2 on c2.cadcftvid = c.cadcftvid  "
			+ " LEFT join  "
			+ " (SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade FROM ENDCADCFTV V "
			+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
			+ " GROUP BY V.CADCFTVID, CI.UF_CIDADE, ci.nome_cidade "
			+ " ) EN ON EN.CADCFTVID = c2.CADCFTVID "
			+ " left join vendedor v on v.cadcftvid = c.vendedor1id  "
			+ " left join cadcftv v2 on v2.cadcftvid = v.cadcftvid "
			+ " left join gestor g on g.gestorid = v.gestorid "
			+ " left join emailcadcftv mail on mail.cadcftvid  = c.cadcftvid and mail.tipo_emailcadcftv ='FINANCEIRO' "
			+ " left join pedidovenda p on p.pedidovendaid = c.vendaid  "
			+ " left join( "
			+ " SELECT  "
			+ " V.CADCFTVID,  "
			+ " (array_agg(v.email_emailcadcftv order by v.tipo_emailcadcftv))[1] as emailoutro "
			+ " FROM emailcadcftv  v where v.tipo_emailcadcftv  <> 'FINANCEIRO' "
			+ " group by V.CADCFTVID "
			+ " ) outro on outro.cadcftvid = c.cadcftvid "
			+ " where c.status_ctareceber = 'ABERTO' and c.codigo_barras_ctareceber is not null "
			+ " and c.nosso_nro_ctareceber is not null  "
			+ " )x "
			+ " where (x.cliente = '"+cliente+"' or '1' = '"+cliente+"')  "
			+ " and (x.status2 = '"+status+"' or 'TODOS'= '"+status+"' ) "
			+ " and (x.nr_documento_ctareceber = '"+nota+"' or '1'= '"+nota+"') "
			+ "  ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Titulo vc =  new Titulo();
			
			vc.setId((BigDecimal)row[0]);
			vc.setSigla((String)row[1]);
			vc.setNrdocumento((String)row[2]);
			vc.setNrparcela((String)row[3]);
			vc.setCliente((BigDecimal)row[4]);
			vc.setNomecliente((String)row[5]);
			vc.setUf((String)row[6]);
			vc.setCidade((String)row[7]);
			vc.setFormacobranca((String)row[8]);
			vc.setDtvencimento((String)row[9]);
			vc.setStatus((String)row[10]);
			vc.setStatus2((String)row[11]);
			vc.setOrigem((String)row[12]);
			vc.setValor((String)row[13]);
			vc.setEmail((String)row[14]);
			vc.setEmailoutro((String)row[15]);
										
			list.add(vc);
		}	
		
		return list;
	}
	
	public List<Nota> notas(String cliente, String nota){
		List<Nota> list = new ArrayList<>();
		
		String sql = ""
				+ "  SELECT  "
				+ " f2.sigla_filial , "
				+ " tp.desc_tipo_pedido , "
				+ " p.nr_nota_pedidovenda nota, "
				+ " p.cadcftvid cliente, "
				+ " c.nome_cadcftv nome_cliente, "
				+ " to_char(p.dt_faturamento_pedidovenda , 'DD/MM/YYYY') datanota, "
				+ " p.status_pedidovenda status, "
				+ " to_char(p.vl_total_pedidovenda, 'L9G999G990D99') totalnota, "
				+ " nf.identificacao_nfe chavexml "
				+ " from pedidovenda p "
				+ " inner join cadcftv c on c.cadcftvid = p.cadcftvid  "
				+ " inner join filial f2 on f2.filialid = c.filialid  "
				+ " inner join tipo_pedido tp on tp.tipopedidoid = p.tipopedidoid  "
				+ " inner join pedidovenda_nfe nf on nf.pedidovendaid = p.pedidovendaid  "
				+ " where p.status_pedidovenda in ('FATURADO')  "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'  "
				+ " and (p.cadcftvid = '"+cliente+"' or '1' = '"+cliente+"')  "
				+ " and (p.nr_nota_pedidovenda = '"+nota+"' or '1'='"+nota+"') "
				+ " order by p.dt_faturamento_pedidovenda ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Nota n = new Nota();
			
			n.setSigla((String )row[0]);
			n.setTipopedido((String)row[1]);
			n.setNrnota((String)row[2]);
			n.setCliente((BigDecimal)row[3]);
			n.setNomecliente((String)row[4]);
			n.setDtnota((String)row[5]);
			n.setStatus((String)row[6]);
			n.setValor((String)row[7]);
			n.setChavexml((String)row[8]);
			
			list.add(n);			
		}	
	
		return list;

	}
	
	public List<P1_MetaFaturado> p1metafaturado(Date data1, Date data2){
		List<P1_MetaFaturado> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String mes = sdf.format(data2);
		SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY");
		//String ano = sdf2.format(data2);
		
		
		Date date = new Date(); // sua instancia Date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data2);
		int ano = calendar.get(Calendar.YEAR);
		
		//System.out.println(ano);
		
		
		String sql = ""
				+ " select  "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) faturamento, "
				+ " meta.meta_faturamento, round((sum(p.VL_TOTALPROD_PEDIDOVENDA)/meta.meta_faturamento)*100,2) as perc "
				+ " from pedidovenda p   "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " left join ( "
				+ " select "
				+ " cast(m.ANO_MP as TEXT) ano,  "
				+ " lpad(CAST(m.MES_MP AS TEXT),2,'0')  mes, "
				+ " m.VL_META_PEDIDOS_PNLGESTAO meta_pedidos,  "
				+ " m.VL_META_VENDAS_PNLGESTAO meta_faturamento  "
				+ " from MACROPLANO m  "
				+ " )meta on meta.ano = '"+ano+"' and meta.mes = '"+mes+"' "
				+ " where p.STATUS_PEDIDOVENDA = 'FATURADO'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
				+ " and p.DT_FATURAMENTO_PEDIDOVENDA between '"+ dataFormatada +" ' and '" +dataFormatada2 +"' "
				+ " group by meta_faturamento  "
				+ "";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			P1_MetaFaturado vc =  new P1_MetaFaturado();
	
			vc.setFaturado((BigDecimal)row[0]);
			vc.setMeta((BigDecimal)row[1]);
			vc.setPerc((BigDecimal)row[2]);
			
					
			list.add(vc);
		}		
		return list;
	}
	
	public List<P1_MetaFaturado> p1metapedido(Date data1, Date data2){
		List<P1_MetaFaturado> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		String mes = sdf.format(data2);
		SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY");
		//String ano = sdf2.format(data2);
		
		Date date = new Date(); // sua instancia Date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data2);
		int ano = calendar.get(Calendar.YEAR);
		
		//System.out.println(ano);
		
		String sql = ""
				+ " select  "
				+ " round(sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item),2) pedido, "
				+ " meta.meta_pedidos, round((sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item)/meta.meta_pedidos)*100,2) as perc "
				+ " from pedidovenda_item  g "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " left join ( "
				+ " select "
				+ " cast(m.ANO_MP as TEXT) ano,  "
				+ " lpad(CAST(m.MES_MP AS TEXT),2,'0')  mes, "
				+ " m.VL_META_PEDIDOS_PNLGESTAO meta_pedidos,  "
				+ " m.VL_META_VENDAS_PNLGESTAO meta_faturamento  "
				+ " from MACROPLANO m  "
				+ " )meta on meta.ano = '"+ano+"' and meta.mes = '"+mes+"' "
				+ " where  p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.DT_PEDIDOVENDA between '"+ dataFormatada +" ' and '" +dataFormatada2 +"' "
				+ " group by meta_pedidos  "
				+ "";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			P1_MetaFaturado vc =  new P1_MetaFaturado();
	
			vc.setFaturado((BigDecimal)row[0]);
			vc.setMeta((BigDecimal)row[1]);
			vc.setPerc((BigDecimal)row[2]);
			
					
			list.add(vc);
		}		
		return list;
	}
	
	public List<VendaCusto> vendacustopedido(String ano,String produtos, String vendedor){
		List<VendaCusto> list = new ArrayList<>();
		String sql = ""
				+ " select "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
				+ " round(sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item),2) total, "
				+ " round(SUM(g.QT_PEDIDOVENDA_ITEM * g.VL_CUSTOORIG_PEDIDOVENDA_ITEM),2) custo , "
				+ " round((SUM(g.QT_PEDIDOVENDA_ITEM * g.VL_CUSTOORIG_PEDIDOVENDA_ITEM)/sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item))*100,2) percentual,"
				+ " to_char(sum(g.QT_PEDIDOVENDA_ITEM)) qtde "
				+ " from pedidovenda_item  g "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where  TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') =  '"+ano+"' "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and (g.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.vendedor1id = "+vendedor+" or -1 = "+vendedor+" ) "
				+ " group by TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), TO_CHAR(p.DT_PEDIDOVENDA,'MM') "
				+ "";
				/*+ " select "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') ano, "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM') mes, "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) valor,"
				+ " sum(it.custo) custo,"
				+ " round((sum(it.custo)/sum(p.VL_TOTALPROD_PEDIDOVENDA))*100,2) percentual "
				+ " from pedidovenda p "
				+ " inner join("
				+ " select"
				+ " g.pedidovendaid ,"
				+ " SUM(g.QT_PEDIDOVENDA_ITEM * g.VL_CUSTOORIG_PEDIDOVENDA_ITEM) custo "
				+ " from pedidovenda_item  g group by g.pedidovendaid "
				+ " )it on it.pedidovendaid = p.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.tipooperacao_cfop = 'VENDA' "
				+ " and TO_CHAR(p.DT_PEDIDOVENDA,'YYYY') = '"+ano+"' "
				+ " group by "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'YYYY'), "
				+ " TO_CHAR(p.DT_PEDIDOVENDA,'MM')";*/
						
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			VendaCusto vc =  new VendaCusto();
			
			vc.setAno((String)row[0]);
			vc.setMes((String)row[1]);
			vc.setVenda((BigDecimal)row[2]);
			vc.setCusto((BigDecimal)row[3]);
			vc.setPercentual((BigDecimal)row[4]);
			vc.setQtde((String)row[5]);
			
			list.add(vc);
		}
		return list;
	}
	
	public List<VendaCusto> vendacusto(String ano,String produtos, String vendedor){
		List<VendaCusto> list = new ArrayList<>();
		String sql = ""
				+ " select   "
				+ " x.ano,x.mes,sum(x.valor) venda,sum(x.custo) custo, round((sum(x.custo)/sum(x.valor))*100,2) percentual,to_char(sum(x.qtde)) qtde, pr.qtdeproducao from( "
				+ " select    "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano,   "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes,   "
				+ " round(sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item),2) valor, "
				+ " 0 custo,"
				+ " 0 qtde  "
				+ " from pedidovenda_item  g "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.STATUS_PEDIDOVENDA = 'FATURADO'   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'   "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'  "
				+ " and (g.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.vendedor1id = "+vendedor+" or -1 = "+vendedor+" ) "
				+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"' "
				+ " group by  "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'),   "
				+ " TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM')  "
				+ " union all  "
				+ " SELECT  "
				+ " TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') AS ano,  "
				+ " TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') AS mes,  "
				+ " 0 valor,  "
				+ " round(SUM(g.QT_PEDIDOVENDA_ITEM * g.VL_CUSTOORIG_PEDIDOVENDA_ITEM),2) custo,"
				+ " sum(g.QT_PEDIDOVENDA_ITEM) qtde  "
				+ " from pedidovenda_item  g "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " where p.status_pedidovenda in ('FATURADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'   "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'  "
				+ " and (g.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.vendedor1id = "+vendedor+" or -1 = "+vendedor+" ) "
				+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"' "
				+ " GROUP BY TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY'),TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM')  "
				+ " )x "
				+ " "
				+ "  left join( "
				+ "  select  "
				+ " d.ano,d.mes,round(sum(d.qtde),0) qtdeproducao "
				+ " from( "
				+ " select "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ANO, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') MES, "
				+ " sum(ei.qtde_entradaproducao) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'COMPONENTE' "
				+ " group by "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') , "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') "
				+ " union all  "
				+ " select "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') ANO, "
				+ " TO_CHAR(e.dt_entradaproducao,'MM') MES, "
				+ " sum(ei.qtde_entradaproducao) qtde "
				+ " from entradaproducao e "
				+ " inner join entradaproducao_item ei on ei.entradaproducaoid = e.entradaproducaoid  "
				+ " inner join produto p on p.produtoid = ei.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = p.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join almoxarifado a on a.almoxarifadoid = ei.almoxarifadoid  "
				+ " where p.tp_produto = 'ACABADO' "
				+ " group by "
				+ " TO_CHAR(e.dt_entradaproducao,'YYYY') , "
				+ " TO_CHAR(e.dt_entradaproducao,'MM'))d group by d.ano,d.mes )pr on pr.ano = x.ano and pr.mes = x.mes "
				+ ""
				+ " group by x.ano,x.mes,pr.qtdeproducao ";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			VendaCusto vc =  new VendaCusto();
			
			vc.setAno((String)row[0]);
			vc.setMes((String)row[1]);
			vc.setVenda((BigDecimal)row[2]);
			vc.setCusto((BigDecimal)row[3]);
			vc.setPercentual((BigDecimal)row[4]);
			vc.setQtde((String)row[5]);
			vc.setQtdeproducao((BigDecimal)row[6]);
			list.add(vc);
		}
		return list;
	}
	
	public List<Categoria> categorias(){
		List<Categoria> list = new ArrayList<>();
		
		String sql = ""
				+ " select "
				+ " c.categoriaid , "
				+ " c.nome_categoria "
				+ " from categoria c  "
				+ "  ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Categoria c = new Categoria();
			
			c.setCategoriaid((BigDecimal)row[0]);
			c.setNomecategoria((String)row[1]);
			
			list.add(c);
		}
		
		return list;
	}
	
	public List<ProdutoRanking> produtoranking(Date data1, Date data2, String vendedor, String produtos , String categoriaid){
		List<ProdutoRanking> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		
		String sql = ""
				+ "  select "
				+ " x.produtoid, "
				+ " x.nome_produto, "
				+ " sum(x.valor) valor, "
				+ " sum(x.qtde) qtde,"
				+ " x.NOME_MESA,"
				+ " saldo.saldo, "
				+ " reservado.qtde_reservada  "
				+ " from( "
				+ " SELECT   "
				+ " g.produtoid , "
				+ " pr.nome_produto , "
				+ " 0 valor, "
				+ " sum(g.QT_PEDIDOVENDA_ITEM) qtde,   "
				+ " c.NOME_CELULAPRODUCAO as NOME_MESA "
				+ " from pedidovenda_item  g  "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " inner join cliente cl on cl.cadcftvid = p.cadcftvid "
				+ " inner join categoria ct on ct.categoriaid = cl.categoriaid "
				+ " inner join produto pr on pr.produtoid = g.produtoid  "
				+ " left JOIN CELULA_PRODUCAO c on c.CELULAPRODUCAOID = pr.CELULAPRODUCAOID "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.status_pedidovenda in ('FATURADO')   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'    "
				+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'   "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'   "
				+ " and (g.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.vendedor1id = "+vendedor+" or -1 = "+vendedor+" ) "
				+ " and (ct.categoriaid = "+categoriaid+" or -1 = "+categoriaid+" ) "
				+ " and p.dt_faturamento_pedidovenda between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " GROUP BY  "
				+ " g.produtoid , "
				+ " pr.nome_produto, c.NOME_CELULAPRODUCAO  "
				+ " union all "
				+ " SELECT   "
				+ " g.produtoid , "
				+ " pr.nome_produto , "
				+ " round(sum(g.qt_pedidovenda_item * g.vl_unit_pedidovenda_item),2) valor, "
				+ " 0 qtde,  "
				+ " c.NOME_CELULAPRODUCAO as NOME_MESA "
				+ " from pedidovenda_item  g  "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " inner join cliente cl on cl.cadcftvid = p.cadcftvid "
				+ " inner join categoria ct on ct.categoriaid = cl.categoriaid "
				+ " inner join produto pr on pr.produtoid = g.produtoid  "
				+ " left JOIN CELULA_PRODUCAO c on c.CELULAPRODUCAOID = pr.CELULAPRODUCAOID "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.status_pedidovenda in ('FATURADO')   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'    "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'   "
				+ " and (g.produtoid in ("+produtos+") or 1 in ("+produtos+")) "
				+ " and (p.vendedor1id = "+vendedor+" or -1 = "+vendedor+" ) "
				+ " and (ct.categoriaid = "+categoriaid+" or -1 = "+categoriaid+" ) "
				+ " and p.dt_faturamento_pedidovenda between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " GROUP BY  "
				+ " g.produtoid , "
				+ " pr.nome_produto,c.NOME_CELULAPRODUCAO  "
				+ " )x "
				+ " left join ( "
				+ " select "
				+ " al.produtoid , "
				+ " al.saldoatu_almoxarifado_produto saldo "
				+ " from almoxarifado_produto al "
				+ " where al.almoxarifadoid = 1 "
				+ " )saldo on saldo.produtoid = x.produtoid "
				+ " left join ( "
				+ " select "
				+ " it.produtoid , "
				+ " sum(it.qt_entrega_pedidovenda_item) qtde_reservada "
				+ " from pedidovenda_item it "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL') "
				+ " group by it.produtoid "
				+ " ) reservado on reservado.produtoid = x.produtoid"
				+ " group by "
				+ " x.produtoid, "
				+ " x.nome_produto,x.NOME_MESA, saldo.saldo, reservado.qtde_reservada "
				+ "  ";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ProdutoRanking vc = new ProdutoRanking();
			
			vc.setProduto((BigDecimal)row[0]);
			vc.setNomeproduto((String)row[1]);
			vc.setVenda((BigDecimal)row[2]);
			vc.setQtde((BigDecimal)row[3]);
			vc.setNomemesa((String)row[4]);
			vc.setSaldo_expedicao((BigDecimal)row[5]);
			vc.setQtde_reservada((BigDecimal)row[6]);
			
			list.add(vc);
		}
		return list;
	}
	
	public List<VendaUF> vendauf(String ano,String mes){
		List<VendaUF> list = new ArrayList<>();
		String sql = ""
				+ "select  "
				+ "x.tipo,x.uf,x.ano,x.mes, "
				+ "sum(x.dia01) dia01,sum(x.dia02) dia02,sum(x.dia03) dia03,sum(x.dia04) dia04,sum(x.dia05) dia05, "
				+ "sum(x.dia06) dia06,sum(x.dia07) dia07,sum(x.dia08) dia08,sum(x.dia09) dia09,sum(x.dia10) dia10, "
				+ "sum(x.dia11) dia11,sum(x.dia12) dia12,sum(x.dia13) dia13,sum(x.dia14) dia14,sum(x.dia15) dia15, "
				+ "sum(x.dia16) dia16,sum(x.dia17) dia17,sum(x.dia18) dia18,sum(x.dia19) dia19,sum(x.dia20) dia20, "
				+ "sum(x.dia21) dia21,sum(x.dia22) dia22,sum(x.dia23) dia23,sum(x.dia24) dia24,sum(x.dia25) dia25, "
				+ "sum(x.dia26) dia26,sum(x.dia27) dia27,sum(x.dia28) dia28,sum(x.dia29) dia29,sum(x.dia30) dia30,sum(x.dia31) dia31  "
				+ "from( "
				+ "select  'FATURAMENTO' tipo, en.uf_cidade uf, TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') ano, TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'MM') mes, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '01' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia01, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '02' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia02, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '03' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia03, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '04' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia04, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '05' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia05, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '06' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia06, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '07' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia07, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '08' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia08, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '09' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia09, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '10' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia10, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '11' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia11, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '12' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia12, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '13' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia13, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '14' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia14, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '15' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia15, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '16' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia16, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '17' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia17, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '18' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia18, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '19' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia19, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '20' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia20, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '21' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia21, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '22' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia22, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '23' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia23, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '24' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia24, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '25' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia25, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '26' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia26, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '27' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia27, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '28' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia28, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '29' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia29, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '30' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia30, "
				+ "case when TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '31' then p.VL_TOTALPROD_PEDIDOVENDA else 0 end dia31 "
				+ "from pedidovenda p   "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ "LEFT join  "
				+ "( "
				+ "SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V  "
				+ "inner join(  "
				+ "SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV   "
				+ "group by cadcftvid  "
				+ ")x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID  "
				+ "INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID  "
				+ ") EN ON EN.CADCFTVID = p.CADCFTVID  "
				+ "where p.STATUS_PEDIDOVENDA = 'FATURADO'  "
				+ "AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
				+ "and p.DT_FATURAMENTO_PEDIDOVENDA between '01-"+mes+"-"+ano+"' and (DATE_TRUNC('MONTH', DATE '"+ano+"-"+mes+"-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') "
				+ ")x "
				+ "group by x.tipo,x.uf,x.ano,x.mes";
		
		//System.out.println(sql);
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			VendaUF vuf =  new VendaUF();
			
			vuf.setTipo((String)row[0]);
			vuf.setUf((String)row[1]);
			vuf.setAno((String)row[2]);
			vuf.setMes((String)row[3]);
			vuf.setDia1((BigDecimal)row[4]);
			vuf.setDia2((BigDecimal)row[5]);
			vuf.setDia3((BigDecimal)row[6]);
			vuf.setDia4((BigDecimal)row[7]);
			vuf.setDia5((BigDecimal)row[8]);
			vuf.setDia6((BigDecimal)row[9]);
			vuf.setDia7((BigDecimal)row[10]);
			vuf.setDia8((BigDecimal)row[11]);
			vuf.setDia9((BigDecimal)row[12]);
			vuf.setDia10((BigDecimal)row[13]);
			vuf.setDia11((BigDecimal)row[14]);
			vuf.setDia12((BigDecimal)row[15]);
			vuf.setDia13((BigDecimal)row[16]);
			vuf.setDia14((BigDecimal)row[17]);
			vuf.setDia15((BigDecimal)row[18]);
			vuf.setDia16((BigDecimal)row[19]);
			vuf.setDia17((BigDecimal)row[20]);
			vuf.setDia18((BigDecimal)row[21]);
			vuf.setDia19((BigDecimal)row[22]);
			vuf.setDia20((BigDecimal)row[23]);
			vuf.setDia21((BigDecimal)row[24]);
			vuf.setDia22((BigDecimal)row[25]);
			vuf.setDia23((BigDecimal)row[26]);
			vuf.setDia24((BigDecimal)row[27]);
			vuf.setDia25((BigDecimal)row[28]);
			vuf.setDia26((BigDecimal)row[29]);
			vuf.setDia27((BigDecimal)row[30]);
			vuf.setDia28((BigDecimal)row[31]);
			vuf.setDia29((BigDecimal)row[32]);
			vuf.setDia30((BigDecimal)row[33]);
			vuf.setDia31((BigDecimal)row[34]);
		
			list.add(vuf);
		}			
		return list;
	}
				
	public List<ClientesAtivos> clientesativos2(String ano){
		List<ClientesAtivos> list = new ArrayList<>();
		
		String sql = ""
				+ "select '01' mes, 'jan' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-01-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-01-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-01-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-01-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90   "
				+ "union all "
				+ "select '02' mes, 'fev' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-02-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-02-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-02-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-02-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90   "
				+ "union all "
				+ "select '03' mes, 'mar' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-03-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-03-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-03-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-03-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '04' mes, 'abr' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-04-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-04-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-04-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-04-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '05' mes, 'mai' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-05-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-05-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-05-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-05-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '06' mes, 'jun' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-06-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-06-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-06-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-06-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '07' mes, 'jul' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-07-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-07-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-07-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-07-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '08' mes, 'ago' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-08-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-08-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-08-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-08-01') then null  "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '09' mes, 'set' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-09-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-09-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-09-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-09-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '10' mes, 'out' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-10-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-10-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-10-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-10-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '11' mes, 'nov' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-11-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-11-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-11-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-11-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "union all "
				+ "select '12' mes, 'dez' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-12-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-12-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-12-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-12-01') then null  "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 90  "
				+ "order by mes ";		
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ClientesAtivos cli= new ClientesAtivos();
			
			cli.setAno(ano);
			cli.setMes((String)row[0]);
			cli.setNmes((String)row[1]);
			cli.setQtde((BigInteger)row[2]);
			list.add(cli);
	}	
		return list;
	}
	
	
	public List<ClientesAtivos> clientesativos(String ano){
		List<ClientesAtivos> list = new ArrayList<>();
		
		String sql = ""
				+ "select '01' mes, 'jan' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-01-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-01-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-01-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-01-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-01-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180   "
				+ "union all "
				+ "select '02' mes, 'fev' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-02-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-02-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-02-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-02-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-02-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180   "
				+ "union all "
				+ "select '03' mes, 'mar' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-03-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-03-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-03-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-03-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-03-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '04' mes, 'abr' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-04-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-04-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-04-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-04-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-04-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '05' mes, 'mai' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-05-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-05-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-05-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-05-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-05-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '06' mes, 'jun' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-06-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-06-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-06-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-06-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-06-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '07' mes, 'jul' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-07-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-07-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-07-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-07-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-07-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '08' mes, 'ago' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-08-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-08-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-08-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-08-01') then null  "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-08-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '09' mes, 'set' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-09-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-09-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-09-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-09-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-09-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '10' mes, 'out' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-10-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-10-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-10-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-10-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-10-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '11' mes, 'nov' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-11-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-11-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-11-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-11-01') then null "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-11-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "union all "
				+ "select '12' mes, 'dez' nmes, count(x.CADCFTVID) ativos from(   "
				+ "select p.CADCFTVID,  "
				+ "case when EXTRACT(MONTH FROM CURRENT_DATE) = EXTRACT(MONTH FROM DATE '"+ano+"-12-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-12-01') then extract(days from CURRENT_DATE - max(p.DT_PEDIDOVENDA))  "
				+ "     when EXTRACT(MONTH FROM CURRENT_DATE) < EXTRACT(MONTH FROM DATE '"+ano+"-12-01') and  EXTRACT(YEAR FROM CURRENT_DATE) = EXTRACT(YEAR FROM DATE '"+ano+"-12-01') then null  "
				+ "else extract(days from (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY') - max(p.DT_PEDIDOVENDA)) end dias   "
				+ "from pedidovenda p    "
				+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID    "
				+ "where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')    "
				+ "and p.DT_PEDIDOVENDA between  '01-01-2000' and (DATE_TRUNC('MONTH', DATE '"+ano+"-12-01') + INTERVAL '1 MONTH' - INTERVAL '1 DAY')   "
				+ "AND CF.TIPOOPERACAO_CFOP = 'VENDA'    "
				+ "group by p.CADCFTVID)x where x.dias <= 180  "
				+ "order by mes ";		
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ClientesAtivos cli= new ClientesAtivos();
			
			cli.setAno(ano);
			cli.setMes((String)row[0]);
			cli.setNmes((String)row[1]);
			cli.setQtde((BigInteger)row[2]);
			list.add(cli);
	}	
		return list;
	}
	
	public List<ReativacaoCliente> reativacaocliente(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2){
		List<ReativacaoCliente> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				" select * from( "
						+ " select  "
						+ " row_number() OVER( "
						+ "         PARTITION BY C.CADCFTVID "
						+ "         ORDER BY pnul.dt DESC "
						+ "     ) row_num, "
						+ " C.CADCFTVID CLIENTE, "
						+ " C.NOME_CADCFTV NOMECLIENTE, "
						+ " C.CNPJCPF_CADCFTV cnpjcpf, "
						+ " en.uf_cidade uf, "
						+ " en.nome_cidade cidade, "
						+ " freq.primeira, "
						+ " pnul.dt pnultima, "
						+ " freq.ultima, "
						+ " round(extract(days from freq.ultima - pnul.dt)) dias_pnul_ultima, "
						+ " g.gestorid gestor, "
						+ " g.NOME_GESTOR, "
						+ " V.CADCFTVID VENDEDOR,  "
						+ " V.NOME_CADCFTV NOME_VENDEDOR "
						+ "  "
						+ " FROM CADCFTV C  "
						+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = C.CADCFTVID "
						+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1  "
						+ " inner join vendedor v2 on v2.CADCFTVID = v.CADCFTVID "
						+ " inner join gestor g on g.GESTORID = v2.GESTORID "
						+ " LEFT join  "
						+ " ( "
						+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV FROM ENDCADCFTV V "
						+ " inner join( "
						+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV  "
						+ " group by cadcftvid "
						+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
						+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
						+ " ) EN ON EN.CADCFTVID = c.CADCFTVID "
						+ "  "
						+ " left join(  "
						+ " select  "
						+ " p.CADCFTVID,  "
						+ " count(p.CADCFTVID) vendas,  "
						+ " min(P.DT_PEDIDOVENDA) primeira,  "
						+ " max(P.DT_PEDIDOVENDA) ultima,  "
						+ " round(extract(days from max(P.DT_PEDIDOVENDA) - min(P.DT_PEDIDOVENDA) )/count(p.CADCFTVID)) frequencia "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO')  "
						+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
						+ " group by p.CADCFTVID  "
						+ " )freq on freq.CADCFTVID = c.CADCFTVID  "
						+ "  "
						+ " left join( "
						+ " select  "
						+ " p.CADCFTVID,  "
						+ " P.DT_PEDIDOVENDA dt "
						+ " from pedidovenda p  "
						+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
						+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') AND CF.TIPOOPERACAO_CFOP = 'VENDA'  "
						+ " )pnul on pnul.CADCFTVID = c.CADCFTVID  "
						+ "  "
						+ " WHERE C.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE'  "
						+ " AND C.ATIVO_CADCFTV = 'SIM'  "
						+ " order by c.cadcftvid "
						+ "  "
						+ "  "
						+ " )geral "
						+ " where geral.row_num = 2 "
						+ " and geral.dias_pnul_ultima >= 180 "
						+ " and geral.vendedor between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
						+ " and geral.gestor between  ' " + gestor1 + " ' and ' " + gestor2 + " ' "
						+ " and geral.ultima between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
						+ " order by geral.ultima desc ,geral.dias_pnul_ultima  ");
		
					List<Object[]> lista = query.getResultList();
						for (Object[] row : lista) {
							ReativacaoCliente reativacaoCliente= new ReativacaoCliente();
							
							reativacaoCliente.setClienteid((BigDecimal) row[1]);
							reativacaoCliente.setNomecliente((String) row[2]);
							reativacaoCliente.setCnpjcliente((String) row[3]);
							reativacaoCliente.setUfcliente((String) row[4]);
							reativacaoCliente.setCidadecliente((String) row[5]);
							reativacaoCliente.setPrimeiracompra((Date) row[6]);
							reativacaoCliente.setPnultimacompra((Date) row[7]);
							reativacaoCliente.setUltimacompra((Date) row[8]);
							reativacaoCliente.setQtdediaspnultimacompra((Double) row[9]);
							reativacaoCliente.setGestorid((BigDecimal) row[10]);
							reativacaoCliente.setNomegestor((String) row[11]);
							reativacaoCliente.setVendedorid((BigDecimal) row[12]);
							reativacaoCliente.setNomevendedor((String) row[13]);
							
							list.add(reativacaoCliente);
					}	
				return list;
	}
	
	public List<CidadeVenda> cidadevenda(String vendedor1, String vendedor2, String gestor1, String gestor2,Date data1, Date data2,Integer filtra){
		List<CidadeVenda> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
		"   select "
		+ " c.cidadeid, "
		+ " c.UF_CIDADE, "
		+ " c.NOME_CIDADE, "
		+ " cidade.dt_ultimacompra, "
		+ " cidadem.dt_primeiracompra "
		+ " "
		+ " from cidade c "
		+ " "
		+ " inner join( "
		+ " select "
		+ " e.CIDADEID "
		+ " from ENDCADCFTV e "
		+ " inner join CADCFTV CA on CA.CADCFTVID = e.CADCFTVID and CA.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
		+ " INNER join cliente cli on cli.CADCFTVID = CA.CADCFTVID "
		+ " inner join vendedor v on v.CADCFTVID = cli.VENDEDORID1 "
		+ " where cli.VENDEDORID1 between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
		+ " and v.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
		+ " group by e.CIDADEID "
		+ " ) ct on ct.CIDADEID = c.CIDADEID "
		+ " "
		+ " left join( "
		+ " select "
		+ " max(p.DT_PEDIDOVENDA) dt_ultimacompra, "
		+ " en.CIDADEID "
		+ " from pedidovenda p "
		+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
		+ " INNER JOIN VENDEDOR VE ON VE.CADCFTVID = P.VENDEDOR1ID "
		+ " LEFT join ( "
		+ " SELECT V.CADCFTVID,CI.CIDADEID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV, V.BAIRRO_ENDCADCFTV FROM ENDCADCFTV V "
		+ " inner join( SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
		+ " group by cadcftvid )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
		+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
		+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
		+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
		+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
		+ " and p.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
		+ " and ve.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
		+ " group by en.CIDADEID\r\n"
		+ " )cidade on cidade.CIDADEID = c.CIDADEID "
		+ " "
		+ " left join( "
		+ " select "
		+ " min(p.DT_PEDIDOVENDA) dt_primeiracompra, "
		+ " en.CIDADEID "
		+ " from pedidovenda p "
		+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
		+ " INNER JOIN VENDEDOR VE ON VE.CADCFTVID = P.VENDEDOR1ID "
		+ " LEFT join ( "
		+ " SELECT V.CADCFTVID,CI.CIDADEID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV, V.BAIRRO_ENDCADCFTV FROM ENDCADCFTV V "
		+ " inner join( SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV "
		+ " group by cadcftvid )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
		+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID "
		+ " ) EN ON EN.CADCFTVID = p.CADCFTVID "
		+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
		+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
		+ " and p.VENDEDOR1ID between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
		+ " and ve.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
		+ " group by en.CIDADEID "
		+ " )cidadem on cidadem.CIDADEID = c.CIDADEID "
		+ " "
		+ " where (cidadem.dt_primeiracompra between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' and 1= " + filtra + " ) or (1<>" + filtra + ") "
		+ " "
		+ " order by c.UF_CIDADE, c.NOME_CIDADE, cidade.dt_ultimacompra "		
		);
				
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			CidadeVenda cidade= new CidadeVenda();
			
			cidade.setCodigocidade((BigDecimal) row[0]);
			cidade.setUfcidade((String) row[1]);
			cidade.setNomecidade((String) row[2]);
			cidade.setDataultimacompra((Date) row[3]);
			cidade.setDataprimeiracompra((Date) row[4]);
			
			list.add(cidade);
		}
		
		return list;
	}
	
	
	public List<ProdutoEstoque> produtoestoque(String produto,String almoxarifado,String grupo,String subgrupo, String tipo, int tipop){
		List<ProdutoEstoque> list = new ArrayList<>();
		
		String sql =" "
				+ " select  "
				+ " pr.produtoid , "
				+ " pr.nome_produto, "
				+ " g.nome_grupoproduto , "
				+ " s.nome_subgrupoproduto , "
				+ " pr.tp_produto , "
				+ " NVL(saldo.almoxarifado,'-') almoxarifado, "
				+ " NVL(saldo.estoque,0) estoque,"
				+ " t.nome_tipoproduto "
				+ " from produto pr "
				+ " left join( "
				+ " select "
				+ " ap.produtoid, "
				+ " a.nome_almoxarifado almoxarifado, "
				+ " ap.saldoatu_almoxarifado_produto estoque "
				+ " from almoxarifado_produto ap "
				+ " inner join almoxarifado a on a.almoxarifadoid = ap.almoxarifadoid  "
				+ " where a.status_almoxarifado = 'ATIVO' and a.bo_considera_consulta_estoque = 'SIM' "
				+ " and (a.almoxarifadoid = '"+almoxarifado+"' or '-1' = '"+almoxarifado+"') "
				+ " )saldo on saldo.produtoid = pr.produtoid  "
				+ " inner join subgrupoproduto s on s.subgrupoprodutoid = pr.subgrupoprodutoid  "
				+ " inner join grupoproduto g on g.grupoprodutoid = s.grupoprodutoid  "
				+ " inner join tipoproduto t on t.tipoprodutoid = pr.tipoprodutoid"
				+ " where (pr.tp_produto = '"+tipo+"' or '-1' = '"+tipo+"')"
				+ " and (pr.produtoid = '"+produto+"' or '-1' = '"+produto+"')"
				+ " and (g.grupoprodutoid = '"+grupo+"' or '-1' = '"+grupo+"') "
				+ " and (s.subgrupoprodutoid = '"+subgrupo+"' or '-1' = '"+subgrupo+"')"
				+ " and (pr.tipoprodutoid = "+tipop+" or -1 = "+tipop+") ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			ProdutoEstoque pr = new ProdutoEstoque();
			
			pr.setProdutoid((BigDecimal)row[0]);
			pr.setNomeproduto((String)row[1]);
			pr.setGrupo((String)row[2]);
			pr.setSubgrupo((String)row[3]);
			pr.setTipoproduto((String)row[4]);
			pr.setAlmoxarifado((String)row[5]);
			pr.setEstoque((BigDecimal)row[6]);
			pr.setNome_tipoproduto((String)row[7]);
			
			list.add(pr);
		}
		
		return list;
	}
	
	
	public List<Almoxarifado> almoxarifados(){
		List<Almoxarifado> list = new ArrayList<>();
		
		String sql = " select "
				+ " a.almoxarifadoid , "
				+ " a.nome_almoxarifado nome "
				+ " from almoxarifado a "
				+ " where a.status_almoxarifado = 'ATIVO' and a.bo_considera_consulta_estoque = 'SIM' ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Almoxarifado pr = new Almoxarifado();
			
			pr.setAlmoxarifadoid((BigDecimal)row[0]);
			pr.setNome((String)row[1]);
			
			list.add(pr);
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
				+ " MIXVL.VL2022,"
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
				+ " and cl.VENDEDORID1 between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
				+ " and cl.VENDEDORID1 between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
				+ " and cl.VENDEDORID1 between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and V2.GESTORID between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
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
		//historico cliente
	public List<HCliente> hclientes(String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2, String uf, String regiao,String vendedor3, String status){
		List<HCliente> list = new ArrayList<>();
		
		String sql =
				" select * from ( SELECT "
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
				+ " VENDAS2022.TOTALANO2022,"
				+ ""
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
				+ " vendedorultimo.vendedor uvendedor, "
				+ " vendedorultimo.nomevendedor unomevendedor, "
				+ " "
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
				+ "  INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
				+ " LEFT join (SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV, V.BAIRRO_ENDCADCFTV FROM ENDCADCFTV V "
				+ " inner join( SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV  "
				+ " group by cadcftvid )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID ) EN ON EN.CADCFTVID = c.CADCFTVID "
				+ " left JOIN CADCFTV V3 ON V3.CADCFTVID = CL.VENDEDORID2 "
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
				
				+" LEFT JOIN( "
				+ " select "
				+ " CLIENTE, "
				+ " NOME_CLIENTE, "
				+ " "
				+ " sum(janeiro) JANEIRO2023, "
				+ " sum(fevereiro) FEVEREIRO2023, "
				+ " sum(marco) MARCO2023, "
				+ " sum(abril) ABRIL2023 ,  "
				+ " sum(maio) MAIO2023, "
				+ " sum(junho) JUNHO2023, "
				+ " sum(julho) JULHO2023 , "
				+ " sum(agosto) AGOSTO2023, "
				+ " sum(setembro) SETEMBRO2023, "
				+ " sum(outubro) OUTUBRO2023, "
				+ " sum(novembro) NOVEMBRO2023, "
				+ " sum(dezembro) DEZEMBRO2023, "
				+ " sum(janeiro)+sum(fevereiro)+sum(marco)+sum(abril)+sum(maio)+sum(junho)+sum(julho)+sum(agosto)+sum(setembro)+sum(outubro)+sum(novembro)+sum(dezembro) totalano2023 "
				+ " "
				+ " from( "
				+ " select "
				+ " CI.CADCFTVID AS CLIENTE, "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE, "
				+ " "
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
				+ " "
				+ " from pedidovenda p "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
				+ " INNER JOIN CLIENTE CL ON CL.CADCFTVID = CI.CADCFTVID "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = CL.VENDEDORID1 "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2023') "
				+ " "
				+ " )x "
				+ " group by "
				+ " CLIENTE, "
				+ " NOME_CLIENTE "
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
				+ " min(P.DT_PEDIDOVENDA) primeira, "
				+ " max(P.DT_PEDIDOVENDA) ultima, "
				+ " round(extract(days from max(P.DT_PEDIDOVENDA) - min(P.DT_PEDIDOVENDA) )/count(p.CADCFTVID)) frequencia "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID "
				+ " )freq on freq.CADCFTVID = c.CADCFTVID "
				
				+ " left join( "
				+ " select "
				+ " x.cadcftvid, "
				+ " x.vendedor, "
				+ " max(x.DT_PEDIDOVENDA) ultima, "
				+ " v.nome_cadcftv nomevendedor "
				+ " from (select "
				+ " p.cadcftvid , "
				+ " P.DT_PEDIDOVENDA, "
				+ " case when p.origem_digitacao_pedidovenda = 'SISTEMA' and p.vendedor2id is not null then p.vendedor2id else p.vendedor1id end vendedor "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " )x "
				+ " inner join cadcftv v on v.cadcftvid = x.vendedor "
				+ " group by x.cadcftvid,x.vendedor,v.nome_cadcftv "
				+ " )vendedorultimo on vendedorultimo.CADCFTVID = c.CADCFTVID and vendedorultimo.ultima = freq.ultima "
				
				+ " WHERE C.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
				+ " AND C.ATIVO_CADCFTV = 'SIM' "
				
				+ " and C.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and ( ('0' =  '" + vendedor3 + "') or (v3.cadcftvid = '"+ vendedor3 + "')) "
				+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+ " and (en.UF_CIDADE = '"+uf+"' or 'TODOS' = '"+uf+"' )"
				+ " and (cl.regiaoid = '"+regiao+"' or '0' = '"+regiao+"' )"
				+ " and ( (round(extract(days from CURRENT_DATE - freq.ultima)) <= 90 and '"+status+"' = 'ATIVO') or "
					+ "(round(extract(days from CURRENT_DATE - freq.ultima)) > 90 and round(extract(days from CURRENT_DATE - freq.ultima)) <= 180 and (round(extract(days from CURRENT_DATE - freq.ultima))+extract(days from(date_trunc('month',current_date) + interval '1 month' - interval '1 day')- CURRENT_DATE)) >= 180 and '"+status+"' = 'CRITICO') or "
					+ "(round(extract(days from CURRENT_DATE - freq.ultima)) > 90 and round(extract(days from CURRENT_DATE - freq.ultima)) <= 180 and '"+status+"' = 'SEMI-ATIVO') or "
					+ "(round(extract(days from CURRENT_DATE - freq.ultima)) > 180 and '"+status+"' = 'INATIVO') or "
					+ "(freq.ultima is null and '"+status+"' = 'INATIVO') or "
					+ "( 'TODOS' = '"+status+"')"
					+ " ) "
				+" )x " ;
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
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
			
			hCliente.setVendedorultimo((BigDecimal) row[99]);
			hCliente.setNomevendedorultimo((String) row[100]);
			
			hCliente.setJaneiro2024((BigDecimal) row[101]);
			hCliente.setFevereiro2024((BigDecimal) row[102]);
			hCliente.setMarco2024((BigDecimal) row[103]);
			hCliente.setAbril2024((BigDecimal) row[104]);
			hCliente.setMaio2024((BigDecimal) row[105]);
			hCliente.setJunho2024((BigDecimal) row[106]);
			hCliente.setJulho2024((BigDecimal) row[107]);
			hCliente.setAgosto2024((BigDecimal) row[108]);
			hCliente.setSetembro2024((BigDecimal) row[109]);
			hCliente.setOutrubo2024((BigDecimal) row[110]);
			hCliente.setNovembro2024((BigDecimal) row[111]);
			hCliente.setDezembro2024((BigDecimal) row[112]);
			hCliente.setTotal2024((BigDecimal) row[113]);
			
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
				+ " TO_CHAR(posvenda.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY')  posvenda_entrada, "
				+ " TO_CHAR(analise.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') analise_entrada "
				+ "  "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join CADCFTV cliente on cliente.CADCFTVID = p.CADCFTVID "
				+ " inner join ROTEIRO fase on fase.roteiroid = p.ROTEIROID "
				+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
				+ " left join ROTEIRO_PEDIDO digitacao on digitacao.PEDIDOVENDAID = p.pedidovendaid and digitacao.ROTEIROID = 1 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )conferencia on conferencia.PEDIDOVENDAID = p.pedidovendaid and conferencia.ROTEIROID = 2 "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )analise on analise.PEDIDOVENDAID = p.pedidovendaid and analise.ROTEIROID = 14 "
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
			pedido.setDt_analise((String)row[18]);
			
			list.add(pedido);
		}
		
		return list;
	}
	
	public List<PedidoFase> pedidofase(int venda, int outros, BigDecimal roteiro, Date data1, Date data2,String pedido, String lote){
		List<PedidoFase> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql =
				" select "
				+ " p.pedidovendaid, "
				+ " p.DT_PEDIDOVENDA, "
				+ " p.CADCFTVID cliente, "
				+ " c.NOME_CADCFTV nomecliente, "
				+ " p.VL_TOTALPROD_PEDIDOVENDA valor, "
				+ " tp.DESC_TIPO_PEDIDO tipopedido, "
				+ " p.STATUS_PEDIDOVENDA status, "
				+ " to_char(DT_ROTEIRO_PEDIDO,'DD/MM/YYYY HH24:MI') dataentrada_fase, "
				//+ " to_date(CURRENT_DATE,'DD/MM/YYYY') - to_date(fase.DT_ROTEIRO_PEDIDO,'DD/MM/YYYY') dias_na_fase "
				+ " extract(days from CURRENT_DATE - to_date(to_char(fase.DT_ROTEIRO_PEDIDO, 'DD/MM/YYYY'))) dias_na_fase, "
				+ " cast(lote.loteid as text ) loteid   "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " inner join TIPO_PEDIDO tp on tp.TIPOPEDIDOID = p.TIPOPEDIDOID "
				+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
				+ " left join (select max(DT_ROTEIRO_PEDIDO) as DT_ROTEIRO_PEDIDO,pedidovendaid,ROTEIROID from ROTEIRO_PEDIDO group by pedidovendaid,ROTEIROID )fase on fase.PEDIDOVENDAID = p.pedidovendaid and fase.ROTEIROID = p.ROTEIROID "
				+ " left join( "
				+ " select   "
				+ " p.roteiroid  ,   "
				+ " p.pedidovendaid , "
				+ " l.loteid  "
				+ " from lote l "
				+ " inner join LOTE_ITEM li on li.loteid = l.loteid "
				+ " inner join pedidovenda_item it on it.pedidovendaitemid  = li.pedidovendaitemid  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')  "
				+ " group by p.roteiroid ,  l.loteid ,p.pedidovendaid "
				+ " )lote on lote.roteiroid = p.roteiroid and lote.pedidovendaid = p.pedidovendaid "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and (p.pedidovendaid = "+pedido+" or '0000' = "+pedido+") "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and  (lote.loteid = "+lote+" or '0000' = "+lote+") "
				+ " and p.ROTEIROID = "+roteiro;
				
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);		
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
			pedidofase.setDiasnafase((Double) row[8]);
			pedidofase.setLote((String) row[9]);
			
			list.add(pedidofase);
		}
		return list;
	}
	
	public List<FaseMateriaPrima> fasemateriaprima(String pedido,String produto){
		List<FaseMateriaPrima> list = new ArrayList<>();
		String sql = ""
				+ " select "
				+ " replace(cast(pr.produtoid as text),'.00','') produtoid , "
				+ " pr.nome_produto "
				+ " from pedidovenda_item_estrutura pie "
				+ " inner join pedidovenda_item it on it.pedidovendaitemid  = pie.pedidovendaitemid "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
				+ " inner join produto pr on pr.produtoid = pie.produtoid_filho "
				+ " where p.pedidovendaid = "+pedido+" and it.produtoid = "+produto+" "
				+ " and pr.tipoprodutoid  = 86 "
				+ " group by pr.produtoid ,pr.nome_produto ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
		
		//System.out.println(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FaseMateriaPrima mtp = new FaseMateriaPrima();
			
			mtp.setProduto((String)row[0]);
			mtp.setNomeproduto((String)row[1]);
			
			list.add(mtp);
		}
		return list;
	}
	
	public List<FasePedidoItem> fasepedidoitem_produto(int venda, int outros,Date data1, Date data2, String pedido, String lote, BigDecimal roteiro){
		List<FasePedidoItem> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql = ""
				+ " select "
				+ " it.produtoid codigoproduto, "
				+ " it.ds_produto_pedidovenda_item nomeproduto, "
				+ " sum(it.qt_pedidovenda_item) qtde_pedido, "
				+ " sum(it.qt_entrega_pedidovenda_item) qtde_reservada, "
				+ " (sum(it.qt_pedidovenda_item) - sum(it.qt_entrega_pedidovenda_item)) falta_pedido, "
				+ " AL.SALDOATU_ALMOXARIFADO_PRODUTO saldo_estoque_atual, "
				+ " case when ((sum(it.qt_pedidovenda_item) - sum(it.qt_entrega_pedidovenda_item)) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) < 0 then 0 else ((sum(it.qt_pedidovenda_item) - sum(it.qt_entrega_pedidovenda_item)) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) end falta_pedido_estoque,"
				+ " c.NOME_CELULAPRODUCAO as NOME_MESA "
				+ " from pedidovenda_item it "
				+ " INNER JOIN ALMOXARIFADO_PRODUTO AL ON AL.PRODUTOID = IT.PRODUTOID AND AL.ALMOXARIFADOID = 1 "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " inner join produto pr on pr.produtoid = it.produtoid "
				+ " left JOIN CELULA_PRODUCAO c on c.CELULAPRODUCAOID = pr.CELULAPRODUCAOID "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " left join lote_item lote on lote.pedidovendaitemid = it.pedidovendaitemid "
				+ " where p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL') "
				+ " and (p.pedidovendaid = "+pedido+" or '0000' = "+pedido+") "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and  (lote.loteid in ("+lote+") or '0000' in ("+lote+")) "
				+ " and p.ROTEIROID = "+roteiro+" "
				+ " and (it.qt_pedidovenda_item <> it.qt_entrega_pedidovenda_item) "
				+ " group by it.produtoid,it.ds_produto_pedidovenda_item,AL.SALDOATU_ALMOXARIFADO_PRODUTO,c.NOME_CELULAPRODUCAO "
				+ " order by ((sum(it.qt_pedidovenda_item) - sum(it.qt_entrega_pedidovenda_item)) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) desc "
				+ " ";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedidoItem fasePedido = new FasePedidoItem();
			
			//fasePedido.setPedido((BigDecimal) row[0]);
			fasePedido.setProduto((BigDecimal) row[0]);
			fasePedido.setNomeproduto((String) row[1]);
			fasePedido.setQtdepedido((BigDecimal) row[2]);
			fasePedido.setQtdereservada((BigDecimal) row[3]);
			fasePedido.setFaltapedido((BigDecimal) row[4]);
			fasePedido.setSaldoatual((BigDecimal) row[5]);
			fasePedido.setFaltapedidoestoque((BigDecimal) row[6]);
			//fasePedido.setQtdepedidogeral((BigDecimal) row[8]);
			//fasePedido.setQtdereservadageral((BigDecimal) row[9]);
			//fasePedido.setFaltapedidogeral((BigDecimal) row[10]);
			//fasePedido.setFaltapedidoestoquegeral((BigDecimal) row[11]);
			fasePedido.setMesa((String) row[7]);
			
			list.add(fasePedido);
		}
		
		return list;
	}
	
	public List<FasePedidoItem> fasepedidoitem(String pedido){
		List<FasePedidoItem> list = new ArrayList<>();
		String sql = ""
				+ " select  "
				+ " it.pedidovendaid codigopedido, "
				+ " it.produtoid codigoproduto, "
				+ " it.ds_produto_pedidovenda_item nomeproduto, "
				+ " it.qt_pedidovenda_item qtde_pedido, "
				+ " it.qt_entrega_pedidovenda_item qtde_reservada, "
				+ " (it.qt_pedidovenda_item - it.qt_entrega_pedidovenda_item) falta_pedido, "
				+ " AL.SALDOATU_ALMOXARIFADO_PRODUTO saldo_estoque_atual, "
				+ " case when ((it.qt_pedidovenda_item - it.qt_entrega_pedidovenda_item) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) < 0 then 0 else ((it.qt_pedidovenda_item - it.qt_entrega_pedidovenda_item) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) end falta_pedido_estoque, "
				+ " outros.qtde_pedido_geral, "
				+ " outros.qtde_reservada_geral, "
				+ " (outros.qtde_pedido_geral - outros.qtde_reservada_geral) falta_pedido_geral, "
				+ " case when ((outros.qtde_pedido_geral - outros.qtde_reservada_geral) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) < 0 then 0 else ((outros.qtde_pedido_geral - outros.qtde_reservada_geral) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) end falta_pedido_estoque_geral "
				+ " from pedidovenda_item it "
				+ " INNER JOIN ALMOXARIFADO_PRODUTO AL ON AL.PRODUTOID = IT.PRODUTOID AND AL.ALMOXARIFADOID = 1 "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " left join ( "
				+ " select  "
				+ " it.produtoid , "
				+ " p.roteiroid , "
				+ " sum(it.qt_pedidovenda_item) qtde_pedido_geral , "
				+ " sum(it.qt_entrega_pedidovenda_item) qtde_reservada_geral  "
				+ " from pedidovenda p  "
				+ " inner join pedidovenda_item it on it.pedidovendaid = p.pedidovendaid  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL') "
				+ " group by it.produtoid,p.roteiroid  "
				+ " )outros on outros.produtoid = it.produtoid and outros.roteiroid = p.roteiroid "
				+ " "	
				+ " where it.pedidovendaid = '"+pedido+"' "
				+ " and (it.qt_pedidovenda_item <> it.qt_entrega_pedidovenda_item) "
				+ " order by  ((outros.qtde_pedido_geral - outros.qtde_reservada_geral) - AL.SALDOATU_ALMOXARIFADO_PRODUTO ) desc "
				+ " ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		//System.out.println(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedidoItem fasePedido = new FasePedidoItem();
			
			fasePedido.setPedido((BigDecimal) row[0]);
			fasePedido.setProduto((BigDecimal) row[1]);
			fasePedido.setNomeproduto((String) row[2]);
			fasePedido.setQtdepedido((BigDecimal) row[3]);
			fasePedido.setQtdereservada((BigDecimal) row[4]);
			fasePedido.setFaltapedido((BigDecimal) row[5]);
			fasePedido.setSaldoatual((BigDecimal) row[6]);
			fasePedido.setFaltapedidoestoque((BigDecimal) row[7]);
			fasePedido.setQtdepedidogeral((BigDecimal) row[8]);
			fasePedido.setQtdereservadageral((BigDecimal) row[9]);
			fasePedido.setFaltapedidogeral((BigDecimal) row[10]);
			fasePedido.setFaltapedidoestoquegeral((BigDecimal) row[11]);
				
			List<FaseMateriaPrima> list2 = new ArrayList<>();
			String sql2 = ""
					+ " select "
					+ " replace(cast(pr.produtoid as text),'.00','') produtoid , "
					+ " pr.nome_produto "
					+ " from pedidovenda_item_estrutura pie "
					+ " inner join pedidovenda_item it on it.pedidovendaitemid  = pie.pedidovendaitemid "
					+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
					+ " inner join produto pr on pr.produtoid = pie.produtoid_filho "
					+ " where p.pedidovendaid = "+pedido+" and it.produtoid = "+fasePedido.getProduto().toString()+" "
					+ " and pr.tipoprodutoid  = 86 "
					+ " group by pr.produtoid ,pr.nome_produto ";
			
			javax.persistence.Query query2 = (javax.persistence.Query) manager.createNativeQuery(sql2);	
			List<Object[]> lista2 = query2.getResultList();
			for (Object[] row2 : lista2) {
				FaseMateriaPrima mtp = new FaseMateriaPrima();
				mtp.setProduto((String)row2[0]);
				mtp.setNomeproduto((String)row2[1]);
				list2.add(mtp);
			}
			
			if(list2.size()>0) {
				fasePedido.setImportado("1");
			}
			
			list.add(fasePedido);
		}
		
		return list;
	}
	
	public List<FasePedido> fasepedido(int venda, int outros, Date data1, Date data2, String pedido, String lote){
		List<FasePedido> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " x.ROTEIROID,  "
				+ " x.DESC_ROTEIRO,  "
				+ " x.vl_pedido,  "
				+ " x.qtde , "
				+ " x.ordem "
				+ " from( "
				+ " select "
				+ " r.ROTEIROID,  "
				+ " r.DESC_ROTEIRO,  "
				+ " coalesce(total.vl_pedido,0) vl_pedido,  "
				+ " coalesce(total.qtde,0) qtde , "
				+ " case  "
				+ " when r.ROTEIROID = 1 then 1 "
				+ " when r.ROTEIROID = 2 then 4 "
				+ " when r.ROTEIROID = 3 then 3 "
				+ " when r.ROTEIROID = 4 then 8 "
				+ " when r.ROTEIROID = 5 then 10 "
				+ " when r.ROTEIROID = 6 then 11 "
				+ " when r.ROTEIROID = 7 then 12 "
				+ " when r.ROTEIROID = 8 then 13 "
				+ " when r.ROTEIROID = 9 then 5 "
				+ " when r.ROTEIROID = 10 then 7 "
				+ " when r.ROTEIROID = 11 then 9 "
				+ " when r.ROTEIROID = 12 then 14 "
				+ " when r.ROTEIROID = 13 then 6 "
				+ " when r.ROTEIROID = 14 then 2 else 100 end ordem "
				+ "  "
				+ " from ROTEIRO r  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.ROTEIROID ,  "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_pedido,  "
				+ " count(p.pedidovendaid) qtde  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " left join( "
				+ " select   "
				+ " p.roteiroid  ,   "
				+ " p.pedidovendaid , "
				+ " l.loteid  "
				+ " from lote l "
				+ " inner join LOTE_ITEM li on li.loteid = l.loteid "
				+ " inner join pedidovenda_item it on it.pedidovendaitemid  = li.pedidovendaitemid  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')  "
				+ " group by p.roteiroid ,  l.loteid ,p.pedidovendaid "
				+ " )lote on lote.roteiroid = p.roteiroid and lote.pedidovendaid = p.pedidovendaid "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and (p.pedidovendaid = "+pedido+" or '0000' = "+pedido+") "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and  (lote.loteid = "+lote+" or '0000' = "+lote+") "
				+ " group by p.ROTEIROID "
				+ " ) total on total.ROTEIROID = r.ROTEIROID "
				+ " "
				+ " )x "
				+ " group by x.ROTEIROID, x.DESC_ROTEIRO, x.vl_pedido, x.qtde ,x.ordem  "
				+ "  order by x.ordem ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedido fasePedido = new FasePedido();
			fasePedido.setRoteiroid((BigDecimal) row[0]);
			fasePedido.setNomeroteiro((String) row[1]);
			fasePedido.setVlpedido((BigDecimal) row[2]);
			fasePedido.setQtdepedido((BigInteger) row[3]);
			
			list.add(fasePedido);
		}
		return list;
	}
	
	public List<FasePedido> fasepedido_produtos(int venda, int outros, Date data1, Date data2, String pedido, String lote){
		List<FasePedido> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(
				"  select "
				+ " x.ROTEIROID,  "
				+ " x.DESC_ROTEIRO,  "
				+ " x.vl_pedido,  "
				+ " x.qtde , "
				+ " x.ordem "
				+ " from( "
				+ " select "
				+ " r.ROTEIROID,  "
				+ " r.DESC_ROTEIRO,  "
				+ " coalesce(total.vl_pedido,0) vl_pedido,  "
				+ " coalesce(total.qtde,0) qtde , "
				+ " case  "
				+ " when r.ROTEIROID = 1 then 1 "
				+ " when r.ROTEIROID = 2 then 4 "
				+ " when r.ROTEIROID = 3 then 3 "
				+ " when r.ROTEIROID = 4 then 8 "
				+ " when r.ROTEIROID = 5 then 10 "
				+ " when r.ROTEIROID = 6 then 11 "
				+ " when r.ROTEIROID = 7 then 12 "
				+ " when r.ROTEIROID = 8 then 13 "
				+ " when r.ROTEIROID = 9 then 5 "
				+ " when r.ROTEIROID = 10 then 7 "
				+ " when r.ROTEIROID = 11 then 9 "
				+ " when r.ROTEIROID = 12 then 14 "
				+ " when r.ROTEIROID = 13 then 6 "
				+ " when r.ROTEIROID = 14 then 2 else 100 end ordem "
				+ "  "
				+ " from ROTEIRO r  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " p.ROTEIROID ,  "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_pedido,  "
				+ " count(p.pedidovendaid) qtde  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " left join( "
				+ " select   "
				+ " p.roteiroid  ,   "
				+ " p.pedidovendaid , "
				+ " l.loteid  "
				+ " from lote l "
				+ " inner join LOTE_ITEM li on li.loteid = l.loteid "
				+ " inner join pedidovenda_item it on it.pedidovendaitemid  = li.pedidovendaitemid  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')  "
				+ " group by p.roteiroid ,  l.loteid ,p.pedidovendaid "
				+ " )lote on lote.roteiroid = p.roteiroid and lote.pedidovendaid = p.pedidovendaid "
				+ " "
				+ " inner join ( "
				+ " select "
				+ " it.pedidovendaid "
				+ " from pedidovenda_item it "
				+ " left join lote_item lote on lote.pedidovendaitemid = it.pedidovendaitemid "
				+ " where (it.qt_pedidovenda_item <> it.qt_entrega_pedidovenda_item) "
				+ " group by it.pedidovendaid "
				+ " )item on item.pedidovendaid = p.pedidovendaid "
				+ " "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " and p.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and (p.pedidovendaid = "+pedido+" or '0000' = "+pedido+") "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and  (lote.loteid in ("+lote+") or '0000' in ("+lote+")) "
				+ " group by p.ROTEIROID "
				+ " ) total on total.ROTEIROID = r.ROTEIROID "
				+ " "
				+ " )x "
				+ " group by x.ROTEIROID, x.DESC_ROTEIRO, x.vl_pedido, x.qtde ,x.ordem  "
				+ "  order by x.ordem ");
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedido fasePedido = new FasePedido();
			fasePedido.setRoteiroid((BigDecimal) row[0]);
			fasePedido.setNomeroteiro((String) row[1]);
			fasePedido.setVlpedido((BigDecimal) row[2]);
			fasePedido.setQtdepedido((BigInteger) row[3]);
			
			list.add(fasePedido);
		}
		return list;
	}
	
	public List<FasePedido> fasepedidodatafase(int venda, int outros, Date data1, Date data2, String pedido, String lote,Date fase){
		List<FasePedido> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		
		String dataFormatada2 = formato.format(fase);
		
		//ajustar data +1 para filtrar corretamente
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(fase); 
		cal.add(Calendar.DATE, 1);
		fase = cal.getTime();				
		String dataFormatada3 = formato.format(fase);
		
		String sql = " "
				+"  select "
				+ " x.ROTEIROID,  "
				+ " x.DESC_ROTEIRO,  "
				+ " x.vl_pedido,  "
				+ " x.qtde , "
				+ " x.ordem "
				+ " from( "
				+ " select "
				+ " r.ROTEIROID,  "
				+ " r.DESC_ROTEIRO,  "
				+ " coalesce(total.vl_pedido,0) vl_pedido,  "
				+ " coalesce(total.qtde,0) qtde , "
				+ " case  "
				+ " when r.ROTEIROID = 1 then 1 "
				+ " when r.ROTEIROID = 2 then 4 "
				+ " when r.ROTEIROID = 3 then 3 "
				+ " when r.ROTEIROID = 4 then 8 "
				+ " when r.ROTEIROID = 5 then 10 "
				+ " when r.ROTEIROID = 6 then 11 "
				+ " when r.ROTEIROID = 7 then 12 "
				+ " when r.ROTEIROID = 8 then 13 "
				+ " when r.ROTEIROID = 9 then 5 "
				+ " when r.ROTEIROID = 10 then 7 "
				+ " when r.ROTEIROID = 11 then 9 "
				+ " when r.ROTEIROID = 12 then 14 "
				+ " when r.ROTEIROID = 13 then 6 "
				+ " when r.ROTEIROID = 14 then 2 else 100 end ordem "
				+ "  "
				+ " from ROTEIRO r  "
				+ "  "
				+ " left join(  "
				+ " select  "
				+ " RT.ROTEIROID ,  "
				+ " sum(p.VL_TOTALPROD_PEDIDOVENDA) vl_pedido,  "
				+ " count(p.pedidovendaid) qtde  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " left join( "
				+ " select   "
				+ " p.roteiroid  ,   "
				+ " p.pedidovendaid , "
				+ " l.loteid  "
				+ " from lote l "
				+ " inner join LOTE_ITEM li on li.loteid = l.loteid "
				+ " inner join pedidovenda_item it on it.pedidovendaitemid  = li.pedidovendaitemid  "
				+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO')  "
				+ " group by p.roteiroid ,  l.loteid ,p.pedidovendaid "
				+ " )lote on lote.roteiroid = p.roteiroid and lote.pedidovendaid = p.pedidovendaid "
				+ " inner join ( "
				+ " select   "
				+ " r.pedidovendaid,  "
				+ " r.ROTEIROID  "  
				+ " from ROTEIRO_PEDIDO r "
				+ " inner join (  "
				+ " select   "
				+ " max(roteiropedidoid) roteiropedidoid,  "
				+ " r.pedidovendaid  "
				+ " from ROTEIRO_PEDIDO r inner join pedidovenda p2 on p2.pedidovendaid = r.pedidovendaid where p2.roteiroid not in (1,12) and DT_ROTEIRO_PEDIDO <= '" + dataFormatada3 + " '"
				+ " group by r.pedidovendaid  "
				+ " )rt on rt.roteiropedidoid = r.roteiropedidoid    "
				+ " union all   "
				+ " select   "
				+ " r.pedidovendaid,  "
				+ " r.ROTEIROID  "
				+ " from pedidovenda r  "
				+ " where r.roteiroid = 1 and r.DT_PEDIDOVENDA <=  ' " + dataFormatada2 + " ' "
				+ " union all   "
				+ " select   "
				+ " r.pedidovendaid,  "
				+ " r.ROTEIROID  "
				+ " from pedidovenda r  "
				+ " where r.roteiroid = 12 and r.DT_PEDIDOVENDA <=  ' " + dataFormatada2 + " ' "
				+ " )rt on rt.pedidovendaid = p.pedidovendaid "
				+ " where p.STATUS_PEDIDOVENDA in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO') "
				+ " and p.DT_PEDIDOVENDA <=  ' " + dataFormatada2 + " ' "
				+ " and (p.pedidovendaid = "+pedido+" or '0000' = "+pedido+") "
				+ " AND (CF.tipooperacao_cfop = 'VENDA' and 1="+venda+" or CF.tipooperacao_cfop <> 'VENDA' and 1="+outros+") "
				+ " and  (lote.loteid = "+lote+" or '0000' = "+lote+") "
				+ " group by RT.ROTEIROID "
				+ " ) total on total.ROTEIROID = r.ROTEIROID "
				+ " "
				+ " )x "
				+ " group by x.ROTEIROID, x.DESC_ROTEIRO, x.vl_pedido, x.qtde ,x.ordem  "
				+ "  order by x.ordem ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		//System.out.println(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			FasePedido fasePedido = new FasePedido();
			fasePedido.setRoteiroid((BigDecimal) row[0]);
			fasePedido.setNomeroteiro((String) row[1]);
			fasePedido.setVlpedido((BigDecimal) row[2]);
			fasePedido.setQtdepedido((BigInteger) row[3]);
			
			list.add(fasePedido);
		}
		return list;
	}
	
	public List<CtaCorrente> ctacorrente(){
		List<CtaCorrente> list = new ArrayList<>();
		
		String sql = " "
				+ " select "
				+ " a.nome_agencia , "
				+ " c.nro_ctacorrente conta, "
				+ " c.vl_saldo_ctacorrente saldo, "
				+ " c.vl_saldoconc_ctacorrente saldo_conciliado, "
				+ " c.titular_ctacorrente "
				+ " from ctacorrente c "
				+ " inner join agencia a on a.agenciaid = c.agenciaid "
				+ " where c.ctacorrenteid in (1022,1017,1015,1014,1013,1012,1011) ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			CtaCorrente cta = new CtaCorrente();
				cta.setAgencia((String)row[0]);
				cta.setConta((String)row[1]);
				cta.setSaldo((BigDecimal)row[2]);
				cta.setSaldoconciliado((BigDecimal)row[3]);
				cta.setTitular((String)row[4]);
								
			list.add(cta);
			
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
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
				+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
						+ " AND CF.tipooperacao_cfop = 'VENDA'"
						+ " and p.tp_nota_pedidovenda = 'NORMAL'  "
						+ " group by TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') "
						+ " )faturado_anual on faturado_anual.ano = pl.ano "
						+ "  "
						+ " inner join( "
						+ " select "
						+ " m.ANO_METAVENDEDOR ano, "
						+ " lpad(CAST(m.MES_METAVENDEDOR AS TEXT),2,'0') mes, "
						+ " sum(m.VALOR_METAVENDEDOR)meta_vendedor_mes "
						+ " from META_VENDEDOR m "
						+ " group by  "
						+ " m.ANO_METAVENDEDOR, "
						+ " LPAD(m.MES_METAVENDEDOR,2,'0') "
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
						+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
						+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
						+ "AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.tp_nota_pedidovenda = 'NORMAL' "
						+ "and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'"
						+ "and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"'"
						+ "and TO_CHAR(P.DT_PEDIDOVENDA,'MM') = '"+mes+"'"
						+ ""
						+ "group by"
						+ "p.CADCFTVID,"
						+ "c.NOME_CADCFTV"
						+ ")nota_mes on nota_mes.CADCFTVID = p.CADCFTVID"
						+ ""
						+ "where p.STATUS_PEDIDOVENDA = 'FATURADO'"
						+ "AND CF.tipooperacao_cfop = 'VENDA' "
						+ " and p.tp_nota_pedidovenda = 'NORMAL' "
						+ "and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA'"
						+ "and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"'"
						+ "and TO_CHAR(P.DT_PEDIDOVENDA,'MM') = '"+mes+"'"
						+ "and TO_CHAR(P.DT_PEDIDOVENDA,'DD') = '"+dia+"'"
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
						+ " itens.perc_lucro, "
						+ " g.nome_gestor,"
						+ " V.CADCFTVID "
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
						+ "  case when IT.vl_total_pedidovenda_item = 0 then 1 else IT.vl_total_pedidovenda_item end TOTAL_VENDA,  "
						+ " (IT.vl_total_pedidovenda_item - ( it.VL_CUSTOORIG_PEDIDOVENDA_ITEM  * it.qt_pedidovenda_item )) TOTALLIQUIDO_PEDIDO "
						+ " from pedidovenda_item it "
						+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid)X "
						+ " group by x.pedidovendaid "
						+ " ) itens on itens.pedidovendaid = p.pedidovendaid "
						
						+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
						+ " inner join gestor g on g.gestorid = v2.gestorid "
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
			vendasEmGeral.setNomegestor((String) row[12]);
			vendasEmGeral.setCodigovendedor((BigDecimal) row[13]);
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
			//vendasEmGeralItem.setImagem((Blob) row2[17] );
			
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
				//vendasEmGeralItem.setImagem((Blob) row2[17] );
				
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
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
					list.add(vendasEmGeralItem);
				}
				return list;
				
			}

			public List<VendasEmGeral> trocadefeitodiferente(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
									+ " and p.TIPOPEDIDOID = 20 and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
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
				public List<VendasEmGeralItem> trocadefeitodiferenteitem(BigDecimal pedido) {
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
									+ " and p.TIPOPEDIDOID = 20 and p.pedidovendaid = ' " + pedido + " ' " 
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
							//vendasEmGeralItem.setImagem((Blob) row2[17] );
							
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
			+ " AND CF.tipooperacao_cfop = 'VENDA' "
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " )geral on geral.vlgeralfaturado is not null "
			
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ "and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2020') "
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ "and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2021') "
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
			+ " AND CF.tipooperacao_cfop = 'VENDA'   "
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada3 + " ' " 
			+ " )geral on geral.vlgeralfaturado is not null "
			
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ "and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2020') "
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
			+ " and p.tp_nota_pedidovenda = 'NORMAL' "
			+ "and TO_CHAR(P.DT_PEDIDOVENDA,'YYYY') in ('2021') "
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

		//pedidos de bonifica占쏙옙o
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
						//vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
				}
		
		public List<VendasEmGeral> investimentoemgeral_3(Date data1, Date data2, String vendedor1, String vendedor2, String gestor1, String gestor2,String cliente1, String cliente2) {
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
							//+ " liberado.DT_ROTEIRO_PEDIDO "
							+ " from pedidovenda p "
							+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID "
							+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID "
							+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid "
							+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid "
							+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
							+ " inner join roteiro r on r.roteiroid = p.roteiroid "
							+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = r.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
							/*+ " inner join (select max(rp.ROTEIROPEDIDOID) r,rp.pedidovendaid, min(trunc(rp.DT_ROTEIRO_PEDIDO)) as DT_ROTEIRO_PEDIDO from ROTEIRO_PEDIDO rp "
							+ " inner join pedidovenda p on p.pedidovendaid = rp.pedidovendaid "
							+ " inner join TIPO_PEDIDO_ROTEIRO tr on tr.ROTEIROID = rp.ROTEIROID and tr.TIPOPEDIDOID = p.TIPOPEDIDOID "
							+ " where tr.ORDEM_ROTEIRO >3 group by rp.pedidovendaid "
							+ " ) liberado on liberado.pedidovendaid = p.pedidovendaid "*/
							+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID "
							+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
							+ " AND CF.tipooperacao_cfop <> 'VENDA' "
							+ " and P.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
							+ " and p.TIPOPEDIDOID in (4,3,5,14,13,16) and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
							+ " and v2.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
							+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
							//+ " and tr.ORDEM_ROTEIRO > 3 "
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
				//vendasEmGeral.setDataliberadogestor((Date) row[10] );
				
				
				
				list.add(vendasEmGeral);
			}

			return list;
		}
		
	//pedidos de bonifica占쏙옙o
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
		
	//detalhes do pedido de bonifica占쏙옙o
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
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
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
						//vendasEmGeralItem.setImagem((Blob) row2[17] );
						
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
					//vendasEmGeralItem.setImagem((Blob) row2[17] );
					
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
						//vendasEmGeralItem.setImagem((Blob) row2[17] );
						
						list.add(vendasEmGeralItem);
					}
					return list;
					
				}

	public List<ClienteNovo> clientenovo_venda(Date data1, Date data2,String vendedor1, String vendedor2, String gestor1, String gestor2){
		List<ClienteNovo> list = new ArrayList<>();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);	
		
		String sql = " "
				+ "  select  "
				+ " C.cadcftvid cliente, "
				+ " C.nome_cadcftv nomecliente, "
				+ " c.APELIDO_CADCFTV nome_fantasia, "
				+ " c.CNPJCPF_CADCFTV cnpj_cpf, "
				+ " c.DATACREATE_CADCFTV dtcadastro, "
				+ " V.cadcftvid vendedor, "
				+ " v2.nome_cadcftv nomevendedor, "
				+ " g.nome_gestor gestor, "
				+ " freq.vendas, "
				+ " freq.primeira, "
				+ " freq.ultima, "
				+ " freq.totalvenda "
				+ " from cadcftv c  "
				+ " inner join cliente cl on cl.cadcftvid = c.cadcftvid  "
				+ " INNER JOIN VENDEDOR V ON V.CADCFTVID = cl.VENDEDORID1 "
				+ " inner join cadcftv V2 on V2.cadcftvid = V.cadcftvid  "
				+ " inner join GESTOR G on G.gestorid = V.gestorid  "
				+ " left join( "
				+ " select "
				+ " p.CADCFTVID, "
				+ " count(p.CADCFTVID) vendas, "
				+ " min(P.DT_PEDIDOVENDA) primeira, "
				+ " max(P.DT_PEDIDOVENDA) ultima, "
				+ " SUM(p.VL_TOTALPROD_PEDIDOVENDA) totalvenda "
				+ " from pedidovenda p "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " AND CF.TIPOOPERACAO_CFOP = 'VENDA' "
				+ " group by p.CADCFTVID "
				+ " )freq on freq.CADCFTVID = c.CADCFTVID "
				+ " where c.funcao_principal_cadcftv = 'CLIENTE' "
				+ " and freq.vendas is not null "
				+ " and c.DATACREATE_CADCFTV between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and g.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' " ;
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			ClienteNovo n = new ClienteNovo();
				n.setCodigocliente((BigDecimal)row[0]);
				n.setNomecliente((String)row[1]);
				n.setNomefantasia((String)row[2]);
				n.setCpfcnpj((String)row[3]);
				n.setDatacadastro((Date)row[4]);
				n.setCodigovendedor((BigDecimal)row[5]);
				n.setNomevendedor((String)row[6]);
				n.setGestor((String)row[7]);
				n.setVendas((BigInteger)row[8]);
				n.setPrimeira((Date)row[9]);
				n.setUltima((Date)row[10]);
				n.setTotalvenda((BigDecimal)row[11]);
							
				
			list.add(n);
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
						+ " COALESCE(vendas.vendas,0) VENDAS "
						
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
			clientesNovos.setVendas((BigInteger) row[7] );
			
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

		String sql =
						" SELECT " 
						+ " V.CADCFTVID cliente, " 
						+ " v.NOME_CADCFTV nome_cliente, "
						+ " v.CNPJCPF_CADCFTV cnpj_cpf "  
						
						+ " from cadcftv v "
					
						+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "						
						+ " WHERE v.ATIVO_CADCFTV = 'SIM' AND v.FUNCAO_PRINCIPAL_CADCFTV = 'CLIENTE' "
						+ " and (v.nome_cadcftv like '%"+palavra+"%' or CAST(v.cadcftvid AS TEXT) like '%"+palavra+"%' or v.CNPJCPF_CADCFTV like '"+palavra+"%' ) "
						+ " order by v.cadcftvid ";
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
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
					+ " INNER JOIN CLIENTE V2 ON V2.CADCFTVID = V.CADCFTVID "						
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
	
	public List<TabProduto> tabproduto(String idtabela){
		List<TabProduto> list = new ArrayList<>();
		
		String sql= ""
				+ " select  "
				+ " t.tabelaprecoid , "
				+ " t2.nome_tabelapreco , "
				+ " t2.pc_desc_max_item_tabpreco desc_max, "
				+ " t.produtoid , "
				+ " p.nome_produto , "
				+ " t.vl_unit_tabelaprecoproduto vl_unitario, "
				+ " t.vl_unit_tabelaprecoproduto - ((t.vl_unit_tabelaprecoproduto * t2.pc_desc_max_item_tabpreco)/100) vl_unitario_Liquido, "
				+ " t.bo_promocao_tabelaprecoproduto bo_promocao, "
				+ " t.vl_promocao_tabelaprecoproduto vl_promocao, "
				+ " p.vl_custo_produto vl_custo "
				+ " from tabelaprecoproduto t  "
				+ " inner join tabelapreco t2 on t2.tabelaprecoid = t.tabelaprecoid  "
				+ " inner join produto p on p.produtoid = t.produtoid  "
				+ " where t.tabelaprecoid  = "+idtabela ;
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();

		for (Object[] row : lista) {
			TabProduto p = new TabProduto();
			
			p.setTabelaprecoid((BigDecimal)row[0]);
			p.setNometabela((String)row[1]);
			p.setDesc_max((BigDecimal)row[2]);
			p.setProdutoid((BigDecimal)row[3]);
			p.setNomeproduto((String)row[4]);
			p.setValor_unitario((BigDecimal)row[5]);
			p.setValor_unitario_liquido((BigDecimal)row[6]);
			p.setPromocao((String)row[7]);
			p.setValor_promocao((BigDecimal)row[8]);
			p.setValor_custo((BigDecimal)row[9]);
			
			list.add(p);
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
	
	public List<MetaVenda> metavenda(String vendedor1, String vendedor2, String gestor1, String gestor2,String ano, String mes, Date data_grafico,Date data_grafico2) {
		List<MetaVenda> list = new ArrayList<>();
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data_grafico);
		String dataFormatada2 = formato.format(data_grafico2);

		String sql =
				" SELECT TIPO,MES,ANO,VALOR, CASE WHEN NOME_REGIAO is null THEN 'VAZIO' ELSE NOME_REGIAO END AS NOME_REGIAO FROM ( "
				+ " select 'META' AS TIPO, meta.* from ( "
				+ "	select  "
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
				+ "	RE.NOME_REGIAO)meta  "
				+ "	UNION ALL  "
				+ " select 'VENDA' TIPO, venda.* from( "
				+ "	SELECT "
				+ "	"+mes+" AS MES, "
				+ "	"+ano+" AS ANO, "
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
				//+ "	AND DATE_PART('MONTH',EN.DT_PEDIDOVENDA) = "+mes//TO_CHAR(SYSDATE,'MM') "
				//+ "	AND DATE_PART('YEAR',EN.DT_PEDIDOVENDA) = "+ano//TO_CHAR(SYSDATE,'YYYY') "
				+ " and EN.DT_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ "	GROUP BY  "
				+ "	RE.NOME_REGIAO)venda "
				+ "	)X "
				+ "	ORDER BY  "
				+ "	X.ANO,X.MES,X.NOME_REGIAO,X.TIPO " ;
				
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		//System.out.println(sql);
		
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

		String sql =
				" SELECT EN.VENDEDOR1ID ,VEND.NOME_CADCFTV,SUM(EN.VL_TOTALPROD_PEDIDOVENDA)as VALORVENDA,MV.VALOR_METAVENDEDOR VALORMETA, "
				+" (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 as atingidometa, "
				+" case when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 50 then 'red' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 50 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 70 then 'orange' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 70 and (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 < 100 then 'blue' "
				+" when (SUM(EN.VL_TOTALPROD_PEDIDOVENDA) / MV.VALOR_METAVENDEDOR)*100 >= 100 then 'green' end as cordacoluna "
				+" FROM VENDEDOR V   "
				+" left JOIN PEDIDOVENDA EN ON V.CADCFTVID = EN.VENDEDOR1ID  "
				+" INNER JOIN CADCFTV VEND ON VEND.CADCFTVID = V.CADCFTVID "
				
				+" INNER JOIN CFOP CF ON CF.CFOPID = EN.CFOPID   "
				+" LEFT JOIN CLIENTE CLI ON CLI.CADCFTVID = V.CADCFTVID  "
				+" LEFT JOIN REGIAO RE ON RE.REGIAOID = CLI.REGIAOID  "
				+" left JOIN meta_vendedor mv ON mv.CADCFTVID = VEND.CADCFTVID "
				+" WHERE CF.TIPOOPERACAO_CFOP = 'VENDA'  "
				+" and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and v.gestorid between ' " + gestor1 + " ' and ' " + gestor2 + " ' "
				+" AND EN.status_pedidovenda in ('IMPORTADO','ABERTO','BLOQUEADO','PARCIAL')  "
			
				+" AND TO_CHAR(EN.DT_PEDIDOVENDA,'MM') = '"+mes+"'" //TO_CHAR(SYSDATE,'MM')  "
				+" AND TO_CHAR(EN.DT_PEDIDOVENDA,'YYYY') = '"+ano+"'" //TO_CHAR(SYSDATE,'YYYY')  "
				+" AND mv.MES_METAVENDEDOR = "+mes //TO_NUMBER(TO_CHAR(SYSDATE,'MM'))  "
				+" and mv.ANO_METAVENDEDOR = "+ano //TO_NUMBER(TO_CHAR(SYSDATE,'YYYY')) "
				+" GROUP BY EN.VENDEDOR1ID ,VEND.NOME_CADCFTV, mv.VALOR_METAVENDEDOR ";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);	
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
							+ " and p.tp_nota_pedidovenda = 'NORMAL' "
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
				+" case when t.desc_tipo_pedido = 'NEGOCIA합ES COMERCIAIS' then hist.qtde_negociacoescomerciais - it.QT_PEDIDOVENDA_ITEM else hist.qtde_negociacoescomerciais end as qtde_negociacoescomerciais, "
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
				" TO_CHAR(INICIO.DT_INICIO,'DD/MM/YYYY HH24:MI:SS' ) DT_INICIO, " + 
				" TO_CHAR(FIM.DT_FIM,'DD/MM/YYYY HH24:MI:SS' ) DT_FIM,  " + 
				" u.NOME_USUARIO " + 
				" from pedidovenda p " + 
				" inner join cadcftv g on g.cadcftvid = p.cadcftvid " + 
				" left join V_PEDIDOVENDA_CONFERENCIA it on it.pedidovendaid = p.pedidovendaid " + 
				" INNER JOIN TIPO_PEDIDO TP ON TP.TIPOPEDIDOID = p.TIPOPEDIDOID " + 
				" left JOIN ( " + 
				" SELECT PEDIDOVENDAID ,MIN(DT_INI_ENTGA_PEDIDOVENDA_ITEM) DT_INICIO FROM PEDIDOVENDA_ITEM " + 
				" GROUP BY PEDIDOVENDAID " + 
				" )INICIO ON INICIO.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" left JOIN ( " + 
				" SELECT PEDIDOVENDAID ,MAX(DT_FIM_ENTGA_PEDIDOVENDA_ITEM) DT_FIM FROM PEDIDOVENDA_ITEM " + 
				" GROUP BY PEDIDOVENDAID " + 
				" )FIM ON FIM.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" left JOIN( " + 
				" SELECT PEDIDOVENDAID ,MAX(USUARIOID_DESPACHO ) USUARIO FROM PEDIDOVENDA_ITEM " +  
				" GROUP BY PEDIDOVENDAID " + 
				" )USUARIO ON USUARIO.PEDIDOVENDAID = P.PEDIDOVENDAID " + 
				" INNER JOIN USUARIO U ON U.USUARIOID = USUARIO.USUARIO " +
				" where IT.CONFERIDO = 'SIM' " +
				" and p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "+
				//" and dt.data_i BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " '  "+
				" and fim.dt_fim BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' "
				+ " group by p.DT_PEDIDOVENDA, p.pedidovendaid,g.cadcftvid,g.NOME_CADCFTV,TP.DESC_TIPO_PEDIDO,p.VL_TOTALPROD_PEDIDOVENDA,p.VL_PESOLIQ_PEDIDOVENDA ,p.QT_VOLUME_PEDIDOVENDA,INICIO.DT_INICIO,FIM.DT_FIM,u.NOME_USUARIO " );
				
		
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
	
	public List<PedidosConferidosUsuario> pedidosconferidosusuarios(Date data1, Date data2){
		List<PedidosConferidosUsuario> list = new ArrayList<>();
		
		data2.setDate(data2.getDate()+1);
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		String sql = "select  "
				+ " re.usuario, "
				+ " re.nome_usuario, "
				+ " sum(perc_fechado) total, "
				+ " round(sum(valor_fechado),2) valor_fechado "
				+ " from( "
				+ " SELECT  "
				+ " USUARIOID_DESPACHO USUARIO , "
				+ " u.nome_usuario , "
				+ " sum(qt_despacho) despacho, "
				+ " round((sum(qt_despacho)/nullif(t.qtde_total,0)),1) perc_fechado, "
				+ " sum(tr.qt_despacho * tr.vl_unit_pedidovenda_item) valor_fechado "
				+ " FROM PEDIDOVENDA_ITEM tr "
				+ " inner join pedidovenda p on p.pedidovendaid = tr.pedidovendaid  "
				+ " inner join V_PEDIDOVENDA_CONFERENCIA it on it.pedidovendaid = p.pedidovendaid  "
				+ " INNER JOIN USUARIO U ON U.USUARIOID = USUARIOID_DESPACHO "
				+ " inner join (select pedidovendaid,sum(qt_pedidovenda_item)qtde_total ,sum(qt_despacho) despacho2 from pedidovenda_item group by pedidovendaid )t on t.pedidovendaid  = tr.pedidovendaid  "
				+ " left JOIN (  "
				+ " SELECT PEDIDOVENDAID ,MAX(DT_FIM_ENTGA_PEDIDOVENDA_ITEM) DT_FIM FROM PEDIDOVENDA_ITEM  "
				+ " GROUP BY PEDIDOVENDAID  "
				+ " )FIM ON FIM.PEDIDOVENDAID = P.PEDIDOVENDAID  "
				+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
				+ " and IT.CONFERIDO = 'SIM' "
				+ " and t.despacho2 = t.qtde_total "
				+ " and fim.DT_FIM BETWEEN ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " group by USUARIOID_DESPACHO,t.qtde_total,u.nome_usuario "
				+ " )re group by re.usuario,re.nome_usuario ";
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			PedidosConferidosUsuario f = new PedidosConferidosUsuario();	
				f.setUsuario((BigDecimal)row[0]);
				f.setNomeusuario((String)row[1]);
				f.setPedidos((BigDecimal)row[2]);
				f.setValor((BigDecimal)row[3]);
			list.add(f);
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

	public List<Rh_Setor> rh_setor(String ano, String mes){
		List<Rh_Setor>  list = new ArrayList<>();
		
		String sql  = " "
				+ "  select  "
				+ " cc.desc_centroconsumo setor, "
				+ " x.qtde, "
				+ " g.qtde_geral, "
				+ " round((cast(x.qtde as decimal)/g.qtde_geral)*100,2) perc_qtde_setor, "
				+ " xv.valor_setor, "
				+ " vendas.valor_faturado, "
				+ " round((xv.valor_setor/vendas.valor_faturado)*100,2) perc_valor_setor, "
				+ " round((trunc(xv.valor_setor/x.qtde,2)/vendas.valor_faturado)*100,2) perc_valor_por_pessoa, "
				+ " trunc(xv.valor_setor/x.qtde,2) valor_por_pessoa,"
				+ " ''''||cc.desc_centroconsumo||'''' setor_2 "
				+ " from centroconsumo cc "
				+ " left join( "
				+ " select 1 as g, centroconsumoid, count(x.funcionario) qtde "
				+ " from( "
				+ " select d.cadcftvid funcionario, c2.centroconsumoid  "
				+ " from despesa d "
				+ " inner join despesa_centroconsumo dc on dc.despesaid  = d.despesaid "
				+ " inner join centroconsumo c2 on c2.centroconsumoid = dc.centroconsumoid  "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.1','3.1.2.2','3.1.2.3','3.1.2.4','3.1.2.5','3.1.2.7','3.1.2.8','3.1.2.9','3.1.2.10','3.1.2.11','3.1.2.12','3.1.2.15', "
				+ " '3.2.2.1','3.2.2.2','3.2.2.3','3.2.2.4','3.2.2.5','3.2.2.7','3.2.2.8','3.2.2.10','3.2.2.11','3.2.2.13','3.1.2.17','3.2.1.36','3.2.2.14') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by d.cadcftvid,c2.centroconsumoid)x group by x.centroconsumoid "
				+ " )x on x.centroconsumoid = cc.centroconsumoid  "
				+ " left join( "
				+ " select c2.centroconsumoid ,sum(d.vl_despesa) valor_setor "
				+ " from despesa d "
				+ " inner join despesa_centroconsumo dc on dc.despesaid  = d.despesaid "
				+ " inner join centroconsumo c2 on c2.centroconsumoid = dc.centroconsumoid  "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.1','3.1.2.2','3.1.2.3','3.1.2.4','3.1.2.5','3.1.2.7','3.1.2.8','3.1.2.9','3.1.2.10','3.1.2.11','3.1.2.12','3.1.2.15', "
				+ " '3.2.2.1','3.2.2.2','3.2.2.3','3.2.2.4','3.2.2.5','3.2.2.7','3.2.2.8','3.2.2.10','3.2.2.11','3.2.2.13','3.1.2.17','3.2.1.36','3.2.2.14') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by c2.centroconsumoid "
				+ " )xv on xv.centroconsumoid = cc.centroconsumoid  "
				+ " left join( "
				+ " select 1 as g, count(x.funcionario) qtde_geral "
				+ " from( "
				+ " select d.cadcftvid funcionario  "
				+ " from despesa d "
				+ " inner join despesa_centroconsumo dc on dc.despesaid  = d.despesaid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.1','3.1.2.2','3.1.2.3','3.1.2.4','3.1.2.5','3.1.2.7','3.1.2.8','3.1.2.9','3.1.2.10','3.1.2.11','3.1.2.12','3.1.2.15', "
				+ " '3.2.2.1','3.2.2.2','3.2.2.3','3.2.2.4','3.2.2.5','3.2.2.7','3.2.2.8','3.2.2.10','3.2.2.11','3.2.2.13','3.1.2.17','3.2.1.36','3.2.2.14') "
				+ " and dc.centroconsumoid in (8,7,4,6,12,10,5,11,31,13,9) "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by d.cadcftvid)x "
				+ " )g on g.g = x.g "
				+ " left join( "
				+ " SELECT   "
				+ " sum(g.vl_total_pedidovenda_item) valor_faturado "
				+ " from pedidovenda_item  g  "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.status_pedidovenda in ('FATURADO')   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'    "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'   "
				+ " and to_char(p.dt_faturamento_pedidovenda,'YYYY')= '"+ano+"' "
				+ " and to_char(p.dt_faturamento_pedidovenda,'MM') = '"+mes+"' "
				+ " )vendas on 1=1 "
				+ " where x.qtde is not null "
				+ " and cc.centroconsumoid in (8,7,4,6,12,10,5,11,31,13,9,14) " ;
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		
		for (Object[] row : lista) {
			Rh_Setor rh = new  Rh_Setor();
			
			rh.setSetor((String)row[0]);
			rh.setQtde((BigInteger)row[1]);
			rh.setQtde_geral((BigInteger)row[2]);
			rh.setPerc_qtde_setor((BigDecimal)row[3]);
			rh.setValor_setor((BigDecimal)row[4]);
			rh.setValor_faturado((BigDecimal)row[5]);
			rh.setPerc_valor_setor((BigDecimal)row[6]);
			rh.setPerc_valor_pessoa((BigDecimal)row[7]);
			rh.setValor_pessoa((BigDecimal)row[8]);
			rh.setSetor_2((String)row[9]);
			
			list.add(rh);
			
		}
			
		return list;
	}
	
	
	public List<Rh_Folha> rh_folha(String ano, String mes){
		List<Rh_Folha>  list = new ArrayList<>();
		
		String sql = ""
				+ "  "
				+ " select  "
				+ " vendas.ano, "
				+ " vendas.mes, "
				+ " vendas.valor_faturado, "
				+ " clt.qtde_clt, "
				+ " cltv.valor_clt, "
				+ " trunc((cltv.valor_clt/vendas.valor_faturado)*100,2) perc_clt, "
				+ " trunc(vendas.valor_faturado/clt.qtde_clt,2) valor_faturado_clt, "
				+ " coalesce(pj.qtde_pj,0) qtde_pj, "
				+ " coalesce(pjv.valor_pj,0) valor_pj, "
				+ " coalesce(trunc((pjv.valor_pj/vendas.valor_faturado)*100,2),0) perc_pj, "
				+ " coalesce(trunc(vendas.valor_faturado/pj.qtde_pj,2),0) valor_faturado_pj, "
				+ " coalesce(clt.qtde_clt,0)+coalesce(pj.qtde_pj,0) total_funcionarios, "
				+ " to_char( coalesce(cltv.valor_clt,0) + coalesce(pjv.valor_pj,0) , 'L9G999G990D99') total_valor, "
				+ " round(cast(clt.qtde_clt as decimal)/(clt.qtde_clt+coalesce(pj.qtde_pj,0))*100,2) perc_clt_funcionarios, "
				+ " round(cast(coalesce(pj.qtde_pj,0) as decimal)/(clt.qtde_clt+coalesce(pj.qtde_pj,0) )*100,2) perc_pj_funcionarios, "
				+ " round(cltv.valor_clt/(cltv.valor_clt + coalesce(pjv.valor_pj,0))*100,2) perc_clt_valor, "
				+ " round(coalesce(pjv.valor_pj,0)/(cltv.valor_clt + coalesce(pjv.valor_pj,0))*100,2) perc_pj_valor, "
				+ " coalesce(cltv.valor_clt,0) + coalesce(pjv.valor_pj,0)  total_valor2, "
				+ " round(((coalesce(cltv.valor_clt,0) + coalesce(pjv.valor_pj,0) )/vendas.valor_faturado)*100,2) perc_folha_faturado_total, "
				+ " trunc(vendas.valor_faturado/(coalesce(clt.qtde_clt,0)+coalesce(pj.qtde_pj,0)),2) valor_faturado_total, "
				+ " comissao.valor_comissao, "
				+ " round((comissao.valor_comissao/vendas.valor_faturado)*100,2) perc_comissao_faturado "
				+ " from( "
				+ " SELECT   "
				+ " to_char(p.dt_faturamento_pedidovenda,'YYYY') ano, "
				+ " to_char(p.dt_faturamento_pedidovenda,'MM') mes, "
				+ " sum(g.vl_total_pedidovenda_item) valor_faturado "
				+ " from pedidovenda_item  g  "
				+ " inner join pedidovenda p on p.pedidovendaid = g.pedidovendaid "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID   "
				+ " where p.status_pedidovenda in ('FATURADO')   "
				+ " AND CF.tipooperacao_cfop = 'VENDA'    "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'   "
				+ " and to_char(p.dt_faturamento_pedidovenda,'YYYY')= '"+ano+"' "
				+ " and to_char(p.dt_faturamento_pedidovenda,'MM') = '"+mes+"' "
				+ " group by to_char(p.dt_faturamento_pedidovenda,'YYYY'),to_char(p.dt_faturamento_pedidovenda,'MM') "
				+ " )vendas "
				+ " left join ( "
				+ " select  "
				+ " x.ano, "
				+ " x.mes, "
				+ " count(x.funcionario) qtde_clt "
				+ " from( "
				+ " select "
				+ " to_char(d.data_emissao_despesa,'YYYY') ano, "
				+ " to_char(d.data_emissao_despesa,'MM')mes, "
				+ " d.cadcftvid funcionario "
				+ " from despesa d "
				+ " inner join cadcftv c on c.cadcftvid = d.cadcftvid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.1','3.1.2.2','3.1.2.3','3.1.2.4','3.1.2.5','3.1.2.7','3.1.2.8','3.1.2.9','3.1.2.10','3.1.2.11','3.1.2.12','3.1.2.15','3.2.2.1','3.2.2.2','3.2.2.3','3.2.2.4','3.2.2.5','3.2.2.7','3.2.2.8','3.2.2.10','3.2.2.11','3.2.2.13') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by d.cadcftvid,to_char(d.data_emissao_despesa,'MM'),to_char(d.data_emissao_despesa,'YYYY') "
				+ " )x group by x.ano,x.mes "
				+ " )clt on clt.ano = vendas.ano and clt.mes = vendas.mes "
				+ " left join( "
				+ " select "
				+ " to_char(d.data_emissao_despesa,'YYYY') ano, "
				+ " to_char(d.data_emissao_despesa,'MM')mes, "
				+ " sum(d.vl_despesa) valor_clt "
				+ " from despesa d "
				+ " inner join cadcftv c on c.cadcftvid = d.cadcftvid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.1','3.1.2.2','3.1.2.3','3.1.2.4','3.1.2.5','3.1.2.7','3.1.2.8','3.1.2.9','3.1.2.10','3.1.2.11','3.1.2.12','3.1.2.15','3.2.2.1','3.2.2.2','3.2.2.3','3.2.2.4','3.2.2.5','3.2.2.7','3.2.2.8','3.2.2.10','3.2.2.11','3.2.2.13') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by to_char(d.data_emissao_despesa,'MM'),to_char(d.data_emissao_despesa,'YYYY') "
				+ " )cltv on cltv.ano = vendas.ano and cltv.mes = vendas.mes "
				+ " left join ( "
				+ " select  "
				+ " x.ano, "
				+ " x.mes, "
				+ " count(x.funcionario) qtde_pj "
				+ " from( "
				+ " select "
				+ " to_char(d.data_emissao_despesa,'YYYY') ano, "
				+ " to_char(d.data_emissao_despesa,'MM')mes, "
				+ " d.cadcftvid funcionario "
				+ " from despesa d "
				+ " inner join cadcftv c on c.cadcftvid = d.cadcftvid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where p.planocontaid in ('3.1.2.17','3.2.1.36','3.2.2.14') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') = '"+mes+"' "
				+ " group by d.cadcftvid,to_char(d.data_emissao_despesa,'MM'),to_char(d.data_emissao_despesa,'YYYY') "
				+ " )x group by x.ano,x.mes "
				+ " )pj on pj.ano = vendas.ano and pj.mes = vendas.mes "
				+ " left join( "
				+ " select "
				+ " to_char(d.data_emissao_despesa,'YYYY') ano, "
				+ " to_char(d.data_emissao_despesa,'MM')mes, "
				+ " sum(d.vl_despesa) valor_pj "
				+ " from despesa d "
				+ " inner join cadcftv c on c.cadcftvid = d.cadcftvid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where  p.planocontaid in ('3.1.2.17','3.2.1.36','3.2.2.14') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') =  '"+mes+"' "
				+ " group by to_char(d.data_emissao_despesa,'MM'),to_char(d.data_emissao_despesa,'YYYY') "
				+ " )pjv on pjv.ano = vendas.ano and pjv.mes = vendas.mes "
				+ " left join( "
				+ " select "
				+ " to_char(d.data_emissao_despesa,'YYYY') ano, "
				+ " to_char(d.data_emissao_despesa,'MM')mes, "
				+ " sum(d.vl_despesa) valor_comissao "
				+ " from despesa d "
				+ " inner join cadcftv c on c.cadcftvid = d.cadcftvid "
				+ " inner join planoconta p on p.planocontaid = d.planocontaid "
				+ " where p.planocontaid in ('3.2.1.1') "
				+ " and to_char(d.data_emissao_despesa,'YYYY') = '"+ano+"' "
				+ " and to_char(d.data_emissao_despesa,'MM') =   '"+mes+"' "
				+ " group by to_char(d.data_emissao_despesa,'MM'),to_char(d.data_emissao_despesa,'YYYY')  "
				+ " )comissao on comissao.ano = vendas.ano and comissao.mes = vendas.mes ";
		
		//System.out.println(sql);
		
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
		
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			Rh_Folha rh = new  Rh_Folha();
			
			rh.setAno((String)row[0]);
			rh.setMes((String)row[1]);
			rh.setValor_faturado((BigDecimal)row[2]);
			rh.setQtde_clt((BigInteger)row[3]);
			rh.setValor_clt((BigDecimal)row[4]);
			rh.setPerc_clt((BigDecimal)row[5]);
			rh.setValor_faturado_clt((BigDecimal)row[6]);
			rh.setQtde_pj((BigInteger)row[7]);
			rh.setValor_pj((BigDecimal)row[8]);
			rh.setPerc_pj((BigDecimal)row[9]);
			rh.setValor_faturado_pj((BigDecimal)row[10]);
			rh.setTotal_funcionarios((BigInteger)row[11]);
			rh.setTotal_valor((String)row[12]);
			rh.setPerc_clt_funcionarios((BigDecimal)row[13]);
			rh.setPerc_pj_funcionarios((BigDecimal)row[14]);
			rh.setPerc_clt_valor((BigDecimal)row[15]);
			rh.setPerc_pj_valor((BigDecimal)row[16]);
			rh.setTotal_valor2((BigDecimal)row[17]);
			rh.setPerc_folha_faturado_total((BigDecimal)row[18]);
			rh.setValor_faturado_total((BigDecimal)row[19]);
			rh.setValor_comissao((BigDecimal)row[20]);
			rh.setPerc_comissao_faturado((BigDecimal)row[21]);
			
			list.add(rh);
		}
		
		return list;
	}
	public List<VendasFrete> vendasfrete(Date data1, Date data2, String vendedor1, String vendedor2,String cliente1, String cliente2, String uf){
		List<VendasFrete> list = new ArrayList<>();
	
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(data1);
		String dataFormatada2 = formato.format(data2);
		
		int f = 0;
		if(uf.equals("TD")) {
			f = 2;
		}else {
			f = 1;
		}

		String sql = ""
				+ "  select  "
				+ " CI.CADCFTVID AS CLIENTE,   "
				+ " CI.NOME_CADCFTV AS NOME_CLIENTE,  "
				+ " p.NR_NOTA_PEDIDOVENDA nota,  "
				+ " P.DT_FATURAMENTO_PEDIDOVENDA AS DATAfaturamento,  "
				+ " p.vl_totalprod_pedidovenda,  "
				+ " pg.nome_formapagto as prazo,  "
				+ " t.desc_tipo_pedido as tipo_pedido,  "
				+ " CF.tipooperacao_cfop,  "
				+ " p.status_pedidovenda,  "
				+ " en.nome_cidade,  "
				+ " en.uf_cidade, "
				+ " v.NOME_CADCFTV nome_vendedor, "
				+ " G.NOME_GESTOR, "
				+ " p.transportadorid , "
				+ " tr.nome_cadcftv transportadora, "
				+ " p.vl_custo_frete_pedidovenda , "
				+ " p.nr_conhec_frete_pedidovenda  "
				+ " from pedidovenda p  "
				+ " INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID  "
				+ " INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID  "
				+ " inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid  "
				+ " inner join formapagto pg on pg.formapagtoid = p.formapagtoid  "
				+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID  "
				+ " INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = p.VENDEDOR1ID  "
				+ " INNER JOIN GESTOR G ON G.GESTORID = V2.GESTORID "
				+ " left JOIN CADCFTV tr ON tr.CADCFTVID = P.transportadorid   "
				+ " LEFT join (  "
				+ " SELECT V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade, V.END_ENDCADCFTV, V.CEP_ENDCADCFTV, V.NRO_ENDCADCFTV FROM ENDCADCFTV V  "
				+ " inner join(  "
				+ " SELECT max(ENDCADCFTVID) d , CADCFTVID cod FROM ENDCADCFTV  "
				+ " group by cadcftvid  "
				+ " )x on x.d = v.ENDCADCFTVID and x.cod = v.CADCFTVID  "
				+ " INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID  "
				+ " ) EN ON EN.CADCFTVID = p.CADCFTVID  "
				+ " where p.status_pedidovenda in ('FATURADO')  "
				+ " AND CF.tipooperacao_cfop = 'VENDA'  "
				+ " and p.tp_nota_pedidovenda = 'NORMAL'  "
				+ " and p.DT_FATURAMENTO_PEDIDOVENDA between ' " + dataFormatada + " ' and ' " + dataFormatada2 + " ' " 
				+ " and p.cadcftvid between ' " + cliente1 + " ' and ' " + cliente2 + " ' "
				+ " and v.cadcftvid between ' " + vendedor1 + " ' and ' " + vendedor2 + " ' "
				+ " and (en.uf_cidade = '"+uf+"' and 1="+f+" or 2="+f+" ) "
				+ " order by en.uf_cidade,tr.nome_cadcftv,en.nome_cidade ";
				
		javax.persistence.Query query = (javax.persistence.Query) manager.createNativeQuery(sql);
	
		List<Object[]> lista = query.getResultList();
		for (Object[] row : lista) {
			VendasFrete vendas = new VendasFrete();
			
			vendas.setCodigocliente((BigDecimal) row[0]);
			vendas.setNomecliente((String) row[1] );
			vendas.setPedido((String) row[2]);
			vendas.setData((Date) row[3] );
			vendas.setValortotal((BigDecimal) row[4] );
			vendas.setPrazo((String) row[5]);
			vendas.setTipopedido((String) row[6]);
			vendas.setTipooperacaocfop((String) row[7]);
			vendas.setStatuspedido((String) row[8]);
			vendas.setCidade((String) row[9]);
			vendas.setUf((String) row[10]);
			vendas.setNomevendedor((String) row[11] );
			vendas.setNomegestor((String) row[12]);
			vendas.setTransporte((BigDecimal) row[13]);
			vendas.setNometransporte((String) row[14]);
			vendas.setVlfrete((BigDecimal) row[15] );
			vendas.setNconhecimentofrete((String) row[16]);

			list.add(vendas);
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
					+ "  INNER JOIN PEDIDOVENDA P ON P.CADCFTVID = C.CADCFTVID "
					+ "  inner join cadcftv v on v.cadcftvid = p.vendedor1id "
					+ "  INNER JOIN VENDEDOR V2 ON V2.CADCFTVID = v.cadcftvid "
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
	//busca itens retorno da afina豫o marcio tecco
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
			//faz rela豫o
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

	/// retorno afina豫o
	// consulta rela豫o
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
	//inserir rela豫o nova
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
	//alterar rela豫o
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
	
	//excluir rela�ao
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
