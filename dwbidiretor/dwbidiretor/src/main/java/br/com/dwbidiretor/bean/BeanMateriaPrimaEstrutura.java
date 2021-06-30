package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.msg.FacesMessageUtil;
import br.com.dwbidiretor.servico.ServicoMateriaPrimaEstrutura;


@Named
@ViewScoped
public class BeanMateriaPrimaEstrutura implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private MateriaPrimaEstrutura materiaPrimaEstrutura = new MateriaPrimaEstrutura();
	@Inject
	private ServicoMateriaPrimaEstrutura servico;
	private List<MateriaPrimaEstrutura> lista = new ArrayList<>();
	
	private String produto = null;
	private float custo = 0;
	private String tipo = null;
	
	@PostConstruct
	public void init() {
		
		
	}
	
	public void filtrar() {
		materiaPrimaEstrutura = new MateriaPrimaEstrutura();
		if (!produto.equals(null)) {
			lista = servico.materiaPrimaEstrutura(produto);
		}
		if(lista.size()>0) {
			materiaPrimaEstrutura = lista.get(0);
			for (MateriaPrimaEstrutura ma:lista) {
				//encontrar ml custo antigo
				float ml_a = 0;
				float vl_venda = 0;
				if (custo > 0 && ma.getValor_tabela() != null ) {
					
					if ( tipo.equals("A")) {
						
						ml_a = (((((ma.getCusto_acabado().floatValue() - ma.getCusto_ficha().floatValue()) + (custo*ma.getQtde_estrutura().floatValue())) * 100)/ ma.getValor_tabela().floatValue()) - 100 + 45) * -1;
						vl_venda = (ma.getCusto_acabado().floatValue()*100)/(100-(45+ml_a));
						
					} else if (tipo.equals("N")) {
						
						ml_a = (((ma.getCusto_acabado().floatValue() * 100) / ma.getValor_tabela().floatValue()) - 100+ 45) * -1;
						vl_venda = ((ma.getCusto_acabado().floatValue() - ma.getCusto_ficha().floatValue() + (custo*ma.getQtde_estrutura().floatValue()))*100)/(100-(45+ml_a));
					}
					
					ma.setNovovalorvenda(BigDecimal.valueOf(vl_venda));
				}
			}
		}
	}
	
	public void update_tabela(){
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat resultado = new DecimalFormat("0.00000",otherSymbols); 
		try {
			
			 //"update TABELAPRECOPRODUTO set VL_UNIT_TABELAPRECOPRODUTO = ? where produtoid = ? and TABELAPRECOID = ?";
			Connection conexao = ObterConexao();
			
			conexao.setAutoCommit(false);
			for (MateriaPrimaEstrutura ma:lista) {
				String query = "";
				if(ma.getNovovalorvenda() != null ) {
					query = "update TABELAPRECOPRODUTO set VL_UNIT_TABELAPRECOPRODUTO = "+resultado.format(ma.getNovovalorvenda())+" where produtoid = '"+ma.getProdutoid_acabado()+"' and TABELAPRECOID = "+ma.getTabelaprecoid()+" ";
					PreparedStatement pr = conexao.prepareStatement(query);
					pr.executeUpdate();
					pr.closeOnCompletion();
					conexao.commit();
				}
			}
			conexao.setAutoCommit(true);
			conexao.close();
		}catch (Exception e) { 
			e.printStackTrace();
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
	
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public float getCusto() {
		return custo;
	}

	public void setCusto(float custo) {
		this.custo = custo;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public MateriaPrimaEstrutura getMateriaPrimaEstrutura() {
		return materiaPrimaEstrutura;
	}

	public void setMateriaPrimaEstrutura(MateriaPrimaEstrutura materiaPrimaEstrutura) {
		this.materiaPrimaEstrutura = materiaPrimaEstrutura;
	}

	public List<MateriaPrimaEstrutura> getLista() {
		return lista;
	}

	public void setLista(List<MateriaPrimaEstrutura> lista) {
		this.lista = lista;
	}
		
	
}
