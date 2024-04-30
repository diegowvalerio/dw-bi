package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.dwbidiretor.classe.Email_produto_estoqueminimo;
import br.com.dwbidiretor.classe.NotasClienteEmail;
import br.com.dwbidiretor.servico.ServicoNotasClienteEmail;


@ManagedBean
@ApplicationScoped
public class BeanAplicationScoped implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Timer timer = new Timer();
	private Date  horaatual = new Date();
	private int i = 0;
	
	
	private NotasClienteEmail notasClienteEmail = new NotasClienteEmail();
	@Inject
	private ServicoNotasClienteEmail servico;
	private List<NotasClienteEmail> lista = new ArrayList<>();
	
	private Email_produto_estoqueminimo email_produtos = new Email_produto_estoqueminimo();
	private List<Email_produto_estoqueminimo> lista_produtos = new ArrayList<>();	

	@PostConstruct
	public void init() { 
		
		//timer = new Timer();
		TimerTask tarefa = new TimerTask() {
			public void run() {
				try {
					i++;
					horaatual = new Date();
					//System.out.println(horaatual.getHours()+":"+horaatual.getMinutes()+":"+horaatual.getSeconds());
					String hora = horaatual.getHours()+":"+horaatual.getMinutes()+":"+horaatual.getSeconds();
					
					if(hora.equals("20:1:0")) { //20:1:0
						//enviaemail();
						//enviaemail_produtos_importados_saldoabaixo_minimo();
						
					}
					}
				 catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.scheduleAtFixedRate(tarefa, 0, 1000);
		
	}
	
	
	public void  registralog(String conteudo, String pagina, String data) {
		
		try {
			Connection conexaoSige = ObterConexaoSige();

			Statement statement = conexaoSige.createStatement();
			String query = "INSERT INTO dwbi_log(usuario,conteudo,tipo,datahora) values('"+usuarioconectado()+"', '"+conteudo+"', '"+pagina+"', '"+data.toString()+"') "
					       +" SELECT idlogin,usuario,senha from dwbi_login where usuario = '"+ usuarioconectado() +"'" ;
			
			statement.executeQuery(query);
			statement.closeOnCompletion();
			conexaoSige.close();
			
			System.out.println("ok");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		System.out.println("ok 2");
		
	}
	
	public String usuarioconectado() {
		String nome;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			nome = ((UserDetails) principal).getUsername();
		} else {
			nome = principal.toString();
		}
		 System.out.println(nome);
		return nome;
	}
	
	public void enviaemail() {
		lista.clear();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String data = df.format(horaatual);
		
		String dia = data.substring(0,2);
		//String dia = "15";
		String mes = data.substring(3,5);
		String ano = data.substring(6,10);
		
		try {
			Connection conexao = ObterConexao();

			Statement statement = conexao.createStatement();
			String query = "SELECT * FROM ( "
			+ " select "
			+ " count(p.pedidovendaid) n_notas_dia, "
			+ " nota_mes.n_notas_mes, "
			+ " p.CADCFTVID, "
			+ " c.NOME_CADCFTV, "
			+ " email.EMAIL_EMAILCADCFTV "
			+ " from pedidovenda p "
			+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
			+ " left join( "
			+ " select * from( "
			+ " select "
			+ " ROW_NUMBER() OVER(PARTITION BY e.CADCFTVID ORDER BY e.CADCFTVID ASC) AS nun, "
			+ " e.CADCFTVID, "
			+ " e.EMAIL_EMAILCADCFTV "
			+ " from EMAILCADCFTV e "
			+ " group by "
			+ " e.CADCFTVID, "
			+ " e.EMAIL_EMAILCADCFTV)x "
			+ " where x.nun = 1 "
			+ " ) email on email.CADCFTVID = p.CADCFTVID "
			+ " left join( "
			+ " select "
			+ " count(p.pedidovendaid) n_notas_mes, "
			+ " p.CADCFTVID, "
			+ " c.NOME_CADCFTV "
			+ " from pedidovenda p "
			+ " inner join CADCFTV c on c.CADCFTVID = p.CADCFTVID "
			+ " INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID "
			+ " where p.STATUS_PEDIDOVENDA = 'FATURADO' "
			+ " AND CF.tipooperacao_cfop = 'VENDA' "
			+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA' "
			+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"' "
			+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') = '"+mes+"' "
		 	
			+ " group by "
			+ " p.CADCFTVID, "
			+ " c.NOME_CADCFTV "
			+ " )nota_mes on nota_mes.CADCFTVID = p.CADCFTVID "
		 	
			+ " where p.STATUS_PEDIDOVENDA = 'FATURADO' "
			+ " AND CF.tipooperacao_cfop = 'VENDA' "
			+ " and p.ORIGEM_PEDIDOVENDA <> 'SIMETRICA' "
			+ " and TO_CHAR(p.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') = '"+ano+"' "
			+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') = '"+mes+"' "
			+ " and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'DD') = '"+dia+"' "
		 	
			+ " group by "
			+ " nota_mes.n_notas_mes, "
			+ " p.CADCFTVID,email.EMAIL_EMAILCADCFTV, "
			+ " c.NOME_CADCFTV)X "
			
			+ " WHERE X.n_notas_dia = X.n_notas_mes and x.EMAIL_EMAILCADCFTV is not null "
			+ " order by x.EMAIL_EMAILCADCFTV desc ";
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				NotasClienteEmail notasClienteEmail = new NotasClienteEmail();
				notasClienteEmail.setCodigocliente(resultSet.getBigDecimal("CADCFTVID"));
				notasClienteEmail.setNomecliente(resultSet.getString("NOME_CADCFTV"));
				notasClienteEmail.setEmail(resultSet.getString("EMAIL_EMAILCADCFTV"));
				
				lista.add(notasClienteEmail);
				//System.out.println(resultSet.getString("NOME_CADCFTV"));
			}
			statement.closeOnCompletion();
			conexao.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
        
		
		//System.out.println(ano+"-"+mes+"-"+dia);
		//System.out.println("lista:"+lista.size());
		
		if (lista.size() > 0) {
			for (NotasClienteEmail nota : lista) {
				
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				Matcher m = p.matcher(nota.getEmail());
				boolean matchFound = m.matches();

				if (matchFound) {
				
				Properties props = new Properties();
				/** Parâmetros de conexão com servidor Gmail */
				/*
				 * props.put("mail.smtp.host", "smtp.gmail.com");
				 * props.put("mail.smtp.socketFactory.port", "465");
				 * props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				 * props.put("mail.smtp.auth", "true"); props.put("mail.smtp.port", "465");
				 */

				props.put("mail.smtp.host", "mail.marchezanmetais.com.br");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");

				Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("pesquisa@marchezanmetais.com.br", "@rv0re24Xcv");
					}
				});

				/** Ativa Debug para sessão */
				//session.setDebug(true);

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("pesquisa@marchezanmetais.com.br"));
					// Remetente

					Address[] toUser = InternetAddress // Destinatário(s)
							.parse(nota.getEmail()+", pesquisa@marchezanmetais.com.br ");
							//.parse("pesquisa@marchezanmetais.com.br ");

					message.setRecipients(Message.RecipientType.TO, toUser);
					message.setSubject("Pesquisa de Satisfação Marchezan Metais");// Assunto
					message.setText("Olá "+nota.getNomecliente()+". \r\n"
							+ "Caro parceiro Marchezan, nosso compromisso é sempre fazer o melhor pra você e seu negócio. \r\n"
							+ "E para continuarmos melhorando nossos produtos e serviços precisamos da sua ajuda. \r\n"
							+ "São apenas 2 minutos respondendo algumas questões para sabermos se estamos no caminho certo e onde precisamos melhorar. \r\n"
							+ "\r\n"
							+ "Contamos com vocês. \r\n"
							+ "\r\n"
							+ "Muito obrigado!!! \r\n"
							+ "\r\n"
							+ "(Sua resposta será confidencial) \n"
							+ "\r\n"
							+ " Para responder clique no link abaixo para acessar a pesquisa. \r\n"
							+ " http://pesquisa.marchezanmetais.com.br ");

					/** Método para enviar a mensagem criada */
					Transport.send(message);

					//System.out.println("Feito!!!");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			  }
			}
		}
	  }	

	public void enviaemail_produtos_importados_saldoabaixo_minimo() {
		lista_produtos.clear();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int day = cal.get(Calendar.DAY_OF_WEEK);
		
		if (day != 1 && day != 7) {
			try {
				Connection conexao = ObterConexao();

				Statement statement = conexao.createStatement();
				String query = " select " + " p.produtoid, " + " p.NOME_PRODUTO, "
						+ " ALM.SALDOATU_ALMOXARIFADO_PRODUTO SALDO_ATUAL, "
						+ " PC.QT_ESTMIN_PRODUTO_QTDCOMPRA SALDO_MINIMO " + " from produto p "
						+ " INNER JOIN PRODUTO_QTDCOMPRA  PC ON PC.PRODUTOID = P.PRODUTOID "
						+ " INNER JOIN ALMOXARIFADO_PRODUTO ALM ON ALM.PRODUTOID = P.PRODUTOID AND ALM.ALMOXARIFADOID = 1 "
						+ " where p.TIPOPRODUTOID = 86 " + " AND p.status_produto = 'ATIVO' "
						+ " and ALM.SALDOATU_ALMOXARIFADO_PRODUTO <= PC.QT_ESTMIN_PRODUTO_QTDCOMPRA ";

				ResultSet resultSet = statement.executeQuery(query);
				while (resultSet.next()) {
					email_produtos = new Email_produto_estoqueminimo();
					email_produtos.setProdutoid(resultSet.getBigDecimal("produtoid"));
					email_produtos.setNomeproduto(resultSet.getString("NOME_PRODUTO"));
					email_produtos.setSaldoatual(resultSet.getBigDecimal("SALDO_ATUAL"));
					email_produtos.setSaldominimo(resultSet.getBigDecimal("SALDO_MINIMO"));

					lista_produtos.add(email_produtos);
					// System.out.println(resultSet.getString("produtoid"));
				}
				statement.closeOnCompletion();
				conexao.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			// System.out.println("lista_produtos:"+lista_produtos.size());

			if (lista_produtos.size() > 0) {
				String tabela = "<h3>Lista de itens com saldo atual menor ou igual ao definido como minimo.</h3> <br></br> <table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#FFF\" border-color:#FFFFFF\" style=\"border-collapse: collapse;border-color:#FFFFFF\">   <tr>   <td bgcolor=\"#0082b8\" style=\"padding: 5px 11px 5px 11px;\"><p style=\"color:#FFFFFF; text-align: center; font-weight: bold;\">Cód. Produto</p></td>   <td bgcolor=\"#0082b8\" style=\"padding: 5px 11px 5px 11px;\"><p style=\"color:#FFFFFF; text-align: center; font-weight: bold;\">Nome Produto</p></td>   <td bgcolor=\"#0082b8\" style=\"padding: 5px 11px 5px 11px;\"><p style=\"color:#FFFFFF; text-align: center; font-weight: bold;\">Saldo Mínimo</p></td>   <td bgcolor=\"#0082b8\" style=\"padding: 5px 11px 5px 11px;\"><p style=\"color:#FFFFFF; text-align: center; font-weight: bold;\">Saldo Atual</p></td> </tr> ";
				for (Email_produto_estoqueminimo produto : lista_produtos) {

					tabela += "<tr bgcolor=\"#FFFFFF\" >   <td style=\"padding: 0px 5px 0px 5px;\"><p style=\"text-align: left;\">"
							+ produto.getProdutoid()
							+ "</p></td>   <td style=\"padding: 0px 5px 0px 5px;\"><p style=\"text-align: left;\">"
							+ produto.getNomeproduto()
							+ "</p></td>   <td style=\"padding: 0px 5px 0px 5px;\"><p style=\"text-align: center;\">"
							+ produto.getSaldominimo()
							+ "</p></td>   <td style=\"padding: 0px 5px 0px 5px;\"><p style=\"text-align: center;\">"
							+ produto.getSaldoatual() + "</p></td> </tr> ";
				}

				tabela += " <tr bgcolor=\"#0082b8\" style=\"height:20px;\"> <th colspan=6> </th></tr>  </table>";

				Properties props = new Properties();
				props.put("mail.smtp.host", "mail.marchezanmetais.com.br");
				props.put("mail.smtp.socketFactory.port", "465");
				props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.port", "465");

				Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("informatica@marchezanmetais.com.br", "Wrv0re24Xcv");
					}
				});

				/** Ativa Debug para sessão */
				// session.setDebug(true);

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("informatica@marchezanmetais.com.br"));
					// Remetente

					Address[] toUser = InternetAddress // Destinatário(s)
							.parse("informatica@marchezanmetais.com.br, producao@marchezanmetais.com.br, comercial.gerencia@marchezanmetais.com.br, pcp@marchezanmetais.com.br, compras@marchezanmetais.com.br, importacao@marchezanmetais.com.br ");

					message.setRecipients(Message.RecipientType.TO, toUser);
					message.setSubject("Aviso de Estoque Minimo - Produtos Importados");// Assunto
					message.setContent(tabela, "text/html; charset=utf-8");

					/** Método para enviar a mensagem criada */
					Transport.send(message);

					// System.out.println("Feito!!!");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
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
	
		
	private static Connection ObterConexaoSige() {
			Connection conexao = null;
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conexao = DriverManager.getConnection("jdbc:sqlserver://SIGE\\SQLEXPRESS:1433;databaseName=SATLBASE;user=DBCLIENTE;password=@rv0re24Xcv");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return conexao;
		}
	/* pegar chamar o timer do scoped */
	public String teste() {
		
		return "";
	}
	
	

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public Date getHoraatual() {
		return horaatual;
	}

	public void setHoraatual(Date horaatual) {
		this.horaatual = horaatual;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	
	public List<NotasClienteEmail> getLista() {
		return lista;
	}

	public void setLista(List<NotasClienteEmail> lista) {
		this.lista = lista;
	}

	public NotasClienteEmail getNotasClienteEmail() {
		return notasClienteEmail;
	}

	public void setNotasClienteEmail(NotasClienteEmail notasClienteEmail) {
		this.notasClienteEmail = notasClienteEmail;
	}

	
}
