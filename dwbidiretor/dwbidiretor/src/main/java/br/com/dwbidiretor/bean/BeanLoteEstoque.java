package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.LoteEstoque;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.classe.VendasEmGeral;
import br.com.dwbidiretor.classe.VendasEmGeralItem;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoVendasemGeral;
import br.com.dwbidiretor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanLoteEstoque implements Serializable {
	private static final long serialVersionUID = 1L;

	private LoteEstoque loteEstoque = new LoteEstoque();
	private List<LoteEstoque> lista = new ArrayList<>();
	
	private String lote;
	private float qtde;
	private float total_atingido;
	private float perc_atingido_lote;

	@PostConstruct
	public void init() {	

	}
	
	public void filtrar(){
		qtde = 0;
		total_atingido = 0;
		perc_atingido_lote = 0;
		lista.clear();
		try {
			Connection conexao = ObterConexao();

			Statement statement = conexao.createStatement();
			String query ="select  "
					+ " lote.loteid, "
					+ " DT_LOTE, "
					+ " IT.PRODUTOID, "
					+ " IT.DS_PRODUTO_PEDIDOVENDA_ITEM PRODUTO, "
					+ " SUM(IT.QT_PEDIDOVENDA_ITEM) QTDE, "
					+ " AL.SALDOATU_ALMOXARIFADO_PRODUTO SALDO, "
					+ " CASE WHEN round(nvl(AL.SALDOATU_ALMOXARIFADO_PRODUTO,0)/SUM(IT.QT_PEDIDOVENDA_ITEM)*100,2) > 100 THEN 100  "
					+ " ELSE round(nvl(AL.SALDOATU_ALMOXARIFADO_PRODUTO,0)/SUM(IT.QT_PEDIDOVENDA_ITEM)*100,2) END PERC_ATENDIDO,"
					+ " media.media3meses "
					+ " from lote "
					+ " inner join LOTE_ITEM on LOTE_ITEM.LOTEID = lote.LOTEID "
					+ " INNER JOIN PEDIDOVENDA_ITEM IT ON IT.PEDIDOVENDAITEMID = LOTE_ITEM.PEDIDOVENDAITEMID "
					+ " INNER JOIN ALMOXARIFADO_PRODUTO AL ON AL.PRODUTOID = IT.PRODUTOID AND AL.ALMOXARIFADOID = 1 "
					
					+ " left join( "
					+ " select  "
					+ " it.produtoid, "
					+ " trunc(nvl(sum(IT.QT_PEDIDOVENDA_ITEM),0)/3 )media3meses "
					+ " from pedidovenda_item it "
					+ " inner join pedidovenda p on p.pedidovendaid = it.pedidovendaid "
					+ " where p.status_pedidovenda in ('ABERTO','BLOQUEADO','PARCIAL','FECHADO','IMPORTADO') "
					+ " and p.DT_PEDIDOVENDA between ADD_MONTHS(TRUNC(SYSDATE,'MM'),-3) and ADD_MONTHS(TRUNC(LAST_DAY(SYSDATE),'DD'),-1) "
					+ " group by it.produtoid "
					+ " ) media on media.produtoid = it.produtoid "
							
					+ " WHERE lote.LOTEID = "+lote
					+ " GROUP BY "
					+ " lote.loteid, "
					+ " DT_LOTE, "
					+ " IT.PRODUTOID, "
					+ " IT.DS_PRODUTO_PEDIDOVENDA_ITEM, "
					+ " AL.SALDOATU_ALMOXARIFADO_PRODUTO, "
					+ " media.media3meses "
					+ " order by IT.DS_PRODUTO_PEDIDOVENDA_ITEM ";
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				loteEstoque = new LoteEstoque();
				loteEstoque.setLoteid(resultSet.getBigDecimal("LOTEID"));
				loteEstoque.setDatalote(resultSet.getDate("DT_LOTE"));
				loteEstoque.setProdutoid(resultSet.getBigDecimal("PRODUTOID"));
				loteEstoque.setNomeproduto(resultSet.getString("PRODUTO"));
				loteEstoque.setQtde(resultSet.getBigDecimal("QTDE"));
				loteEstoque.setSaldo(resultSet.getBigDecimal("SALDO"));
				loteEstoque.setPerc_atendido(resultSet.getBigDecimal("PERC_ATENDIDO"));
				loteEstoque.setMedia3meses(resultSet.getBigDecimal("media3meses"));
				
				lista.add(loteEstoque);
			}
			statement.closeOnCompletion();
			conexao.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if(lista.size()>0) {
			for(LoteEstoque l:lista) {
				total_atingido = total_atingido + l.getPerc_atendido().floatValue();
				qtde++;
			}
			if(total_atingido > 0) {
				perc_atingido_lote = total_atingido / qtde;
			}
		}
	
	}
	
	private static Connection ObterConexao() {
		Connection conexao = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexao = DriverManager.getConnection("jdbc:oracle:thin:@MSERVER2:1521:AWORKSDB", "SEVEN", "SEVEN");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conexao;
	}

	public LoteEstoque getLoteEstoque() {
		return loteEstoque;
	}

	public void setLoteEstoque(LoteEstoque loteEstoque) {
		this.loteEstoque = loteEstoque;
	}

	public List<LoteEstoque> getLista() {
		return lista;
	}

	public void setLista(List<LoteEstoque> lista) {
		this.lista = lista;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public float getQtde() {
		return qtde;
	}

	public void setQtde(float qtde) {
		this.qtde = qtde;
	}

	public float getTotal_atingido() {
		return total_atingido;
	}

	public void setTotal_atingido(float total_atingido) {
		this.total_atingido = total_atingido;
	}

	public float getPerc_atingido_lote() {
		return perc_atingido_lote;
	}

	public void setPerc_atingido_lote(float perc_atingido_lote) {
		this.perc_atingido_lote = perc_atingido_lote;
	}

	
}
