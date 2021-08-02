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

import br.com.dwbidiretor.classe.ItensTabela;
import br.com.dwbidiretor.classe.MateriaPrimaEstrutura;
import br.com.dwbidiretor.classe.TabelaPreco;
import br.com.dwbidiretor.msg.FacesMessageUtil;
import br.com.dwbidiretor.servico.ServicoItensTabela;
import br.com.dwbidiretor.servico.ServicoMateriaPrimaEstrutura;
import br.com.dwbidiretor.servico.ServicoTabelaPreco;


@Named
@ViewScoped
public class BeanMateriaPrimaEstrutura implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private MateriaPrimaEstrutura materiaPrimaEstrutura = new MateriaPrimaEstrutura();
	@Inject
	private ServicoMateriaPrimaEstrutura servico;
	private List<MateriaPrimaEstrutura> lista = new ArrayList<>();
	
	@Inject
	private ServicoTabelaPreco servicotabelapreco;
	private List<TabelaPreco> listatabelapreco = new ArrayList<>();
	private List<TabelaPreco> listatabelapreco_selecionada = new ArrayList<>();
	
	@Inject
	private ServicoItensTabela servicoitenstabela;
	private List<ItensTabela> listaitenstabela = new ArrayList<>();
	private List<ItensTabela> listatabelamae = new ArrayList<>();
	
	private String produto = null;
	private float custo = 0;
	private String tipo = null;
	private String log = null;
	
	@PostConstruct
	public void init() {
		listatabelapreco = servicotabelapreco.tabelaprecos();
		
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
	
	public void prepara_tabelas_selecionadas(){
		listaitenstabela.clear();
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat resultado = new DecimalFormat("0.00000",otherSymbols);
		
		if(listatabelapreco_selecionada.size()>0) {
			listatabelamae = servicoitenstabela.itenstabela("1");
			log =  "Tabela(s) Selecionada(s)\r\n";
			
			for(TabelaPreco p:listatabelapreco_selecionada) {
				
				log = log +p.getId()+"-"+p.getNometabela()+"\r\n";
				log = log + "Verificando itens ...\r\n";
				ItensTabela t = new ItensTabela();
				for(ItensTabela tt:listatabelamae) {
					t = servicoitenstabela.itenstabela(p.getId().toString(), tt.getProdutoid().toString());
					if(t.getNomeproduto() != null) {
						float vl_n =((100*tt.getValor_tabela().floatValue())/(100-p.getPerc_desconto().floatValue()));
						if(vl_n != t.getValor_tabela().floatValue()) {
							t.setNovovalorvenda(BigDecimal.valueOf(vl_n));
							listaitenstabela.add(t);
							log = log+ ("ID: "+t.getProdutoid()+" valor mãe: "+resultado.format(tt.getValor_tabela())+" valor atual: "+resultado.format(t.getValor_tabela())+" novo valor: "+resultado.format(t.getNovovalorvenda())+"\r\n");
						}
					}					
				}
			}
			
			log = log+ "Total de "+listaitenstabela.size()+" alterações serão realizadas.\r\n";
		}
		renderatualiza();
	}
	
	public void update_tabelas_selecionadas(){
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat resultado = new DecimalFormat("0.00000",otherSymbols);
		
		if (listaitenstabela.size() > 0) {
			try {
				log = log + "Iniciando atualizações...\r\n";
				Connection conexao = ObterConexao();
				conexao.setAutoCommit(false);
				int i = 0;
				for (ItensTabela it : listaitenstabela) {
					
					String query = "";
					query = "update TABELAPRECOPRODUTO set VL_UNIT_TABELAPRECOPRODUTO = "+resultado.format(it.getNovovalorvenda())+" where produtoid = '"+it.getProdutoid()+"' and TABELAPRECOID = "+it.getTabelaprecoid()+" ";
					PreparedStatement pr = conexao.prepareStatement(query);
					pr.executeUpdate();
					pr.closeOnCompletion();
					i += pr.getUpdateCount();
					conexao.commit();
					//System.out.println(query);
					
				}
				
				conexao.setAutoCommit(true);
				conexao.close();
				log = log + i+" Atualizações realizadas com sucesso ! \r\n";
			} catch (Exception e) {
				e.printStackTrace();
				log = log + "=========== ERRO ========== \r\n" + e.getMessage()+ "\r\n =========== FIM ERRO ==========";
			}
		}
	}
	
	public boolean renderatualiza() {
		if(listaitenstabela.size()>0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean hasSelectedProducts() {
        return this.listatabelapreco_selecionada != null && !this.listatabelapreco_selecionada.isEmpty();
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
	
	
	public List<ItensTabela> getListatabelamae() {
		return listatabelamae;
	}

	public void setListatabelamae(List<ItensTabela> listatabelamae) {
		this.listatabelamae = listatabelamae;
	}

	public List<ItensTabela> getListaitenstabela() {
		return listaitenstabela;
	}

	public void setListaitenstabela(List<ItensTabela> listaitenstabela) {
		this.listaitenstabela = listaitenstabela;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
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

	public List<TabelaPreco> getListatabelapreco() {
		return listatabelapreco;
	}

	public void setListatabelapreco(List<TabelaPreco> listatabelapreco) {
		this.listatabelapreco = listatabelapreco;
	}

	public List<TabelaPreco> getListatabelapreco_selecionada() {
		return listatabelapreco_selecionada;
	}

	public void setListatabelapreco_selecionada(List<TabelaPreco> listatabelapreco_selecionada) {
		this.listatabelapreco_selecionada = listatabelapreco_selecionada;
	}
		
	
}
