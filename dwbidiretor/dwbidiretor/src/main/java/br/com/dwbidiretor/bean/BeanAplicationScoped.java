package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
						enviaemail();
						
					}
					}
				 catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.scheduleAtFixedRate(tarefa, 0, 1000);
		
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
				System.out.println(resultSet.getString("NOME_CADCFTV"));
			}
			statement.closeOnCompletion();
			conexao.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
        
		
		//System.out.println(ano+"-"+mes+"-"+dia);
		System.out.println("lista:"+lista.size());
		
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
				session.setDebug(true);

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

					System.out.println("Feito!!!");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			  }
			}
		}
	  }	

	public void enviaemail_TESTE() {
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
			String query = "select\r\n"
					+ "\r\n"
					+ "gr.CADCFTVGRUPO AS CADCFTVID,\r\n"
					+ "gr.NOME_CADCFTV AS NOME_CADCFTV,\r\n"
					+ "rtrim(email.EMAIL_EMAILCADCFTV) EMAIL_EMAILCADCFTV\r\n"
					+ "\r\n"
					+ "\r\n"
					+ "from pedidovenda p\r\n"
					+ "INNER JOIN CADCFTV V ON V.CADCFTVID = P.VENDEDOR1ID\r\n"
					+ "INNER JOIN CADCFTV CI ON CI.CADCFTVID = P.CADCFTVID \r\n"
					+ "INNER JOIN CADCFTV gr ON gr.CADCFTVID = CI.CADCFTVID \r\n"
					+ "inner join tipo_pedido t on t.tipopedidoid = p.tipopedidoid\r\n"
					+ "inner join formapagto pg on pg.formapagtoid = p.formapagtoid\r\n"
					+ "INNER JOIN CFOP CF ON CF.CFOPID = P.CFOPID\r\n"
					+ "\r\n"
					+ "left join( \r\n"
					+ "select * from( \r\n"
					+ "select \r\n"
					+ "ROW_NUMBER() OVER(PARTITION BY e.CADCFTVID ORDER BY e.CADCFTVID ASC) AS nun, \r\n"
					+ "e.CADCFTVID, \r\n"
					+ "e.EMAIL_EMAILCADCFTV \r\n"
					+ "from EMAILCADCFTV e \r\n"
					+ "group by \r\n"
					+ "e.CADCFTVID, \r\n"
					+ "e.EMAIL_EMAILCADCFTV)x \r\n"
					+ "where x.nun = 1 \r\n"
					+ ") email on email.CADCFTVID = CI.CADCFTVID \r\n"
					+ "\r\n"
					+ "left join FONECADCFTV fone on fone.CADCFTVID  = CI.CADCFTVID  and fone.TIPO_FONECADCFTV ='COMERCIAL'\r\n"
					+ "left join FONECADCFTV fone2 on fone2.CADCFTVID  = CI.CADCFTVID  and fone2.TIPO_FONECADCFTV ='COMERCIAL2'\r\n"
					+ "            \r\n"
					+ "LEFT join \r\n"
					+ "(\r\n"
					+ "SELECT  max(v.endcadcftvid), V.CADCFTVID,CI.UF_CIDADE, ci.nome_cidade FROM ENDCADCFTV V\r\n"
					+ "INNER JOIN CIDADE CI ON CI.CIDADEID = V.CIDADEID\r\n"
					+ "GROUP BY V.CADCFTVID, CI.UF_CIDADE, ci.nome_cidade\r\n"
					+ ") EN ON EN.CADCFTVID = CI.CADCFTVID \r\n"
					+ "\r\n"
					+ "\r\n"
					+ "where p.status_pedidovenda in ('FATURADO')\r\n"
					+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'YYYY') in ('2021')\r\n"
					+ "and TO_CHAR(P.DT_FATURAMENTO_PEDIDOVENDA,'MM') between '01' and '12'\r\n"
					+ "AND CF.tipooperacao_cfop= 'VENDA'\r\n"
					+ "--and  gr.CADCFTVGRUPO = '18904'\r\n"
					+ "AND email.EMAIL_EMAILCADCFTV IS NOT NULL\r\n"
					+ "and  gr.CADCFTVGRUPO in\r\n"
					+ "('18904',\r\n"
					+ "'847',\r\n"
					+ "'18937',\r\n"
					+ "'879',\r\n"
					+ "'4037',\r\n"
					+ "'10107',\r\n"
					+ "'1637',\r\n"
					+ "'12632',\r\n"
					+ "'15664',\r\n"
					+ "'5506',\r\n"
					+ "'5146',\r\n"
					+ "'15842',\r\n"
					+ "'17560',\r\n"
					+ "'16981',\r\n"
					+ "'17777',\r\n"
					+ "'12502',\r\n"
					+ "'17450',\r\n"
					+ "'18385',\r\n"
					+ "'16307',\r\n"
					+ "'3803',\r\n"
					+ "'16994',\r\n"
					+ "'14818',\r\n"
					+ "'17507',\r\n"
					+ "'3422',\r\n"
					+ "'15267',\r\n"
					+ "'155',\r\n"
					+ "'19369',\r\n"
					+ "'885',\r\n"
					+ "'1767',\r\n"
					+ "'19598',\r\n"
					+ "'17769',\r\n"
					+ "'6360',\r\n"
					+ "'15092',\r\n"
					+ "'19527',\r\n"
					+ "'15898',\r\n"
					+ "'15111',\r\n"
					+ "'19174',\r\n"
					+ "'602',\r\n"
					+ "'23418',\r\n"
					+ "'17794',\r\n"
					+ "'14474',\r\n"
					+ "'11037',\r\n"
					+ "'19528',\r\n"
					+ "'18179',\r\n"
					+ "'16326',\r\n"
					+ "'32086',\r\n"
					+ "'2901',\r\n"
					+ "'6032',\r\n"
					+ "'16434',\r\n"
					+ "'15068',\r\n"
					+ "'5334',\r\n"
					+ "'5725',\r\n"
					+ "'13446',\r\n"
					+ "'6296',\r\n"
					+ "'117',\r\n"
					+ "'2920',\r\n"
					+ "'4355',\r\n"
					+ "'5840',\r\n"
					+ "'14829',\r\n"
					+ "'4192',\r\n"
					+ "'10036')\r\n"
					+ "\r\n"
					+ "group by\r\n"
					+ "gr.CADCFTVGRUPO,\r\n"
					+ "gr.NOME_CADCFTV,\r\n"
					+ "email.EMAIL_EMAILCADCFTV\r\n"
					
					+ "\r\n"
					+ " ";
			
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				NotasClienteEmail notasClienteEmail = new NotasClienteEmail();
				notasClienteEmail.setCodigocliente(resultSet.getBigDecimal("CADCFTVID"));
				notasClienteEmail.setNomecliente(resultSet.getString("NOME_CADCFTV"));
				notasClienteEmail.setEmail(resultSet.getString("EMAIL_EMAILCADCFTV"));
				
				lista.add(notasClienteEmail);
				System.out.println(resultSet.getString("NOME_CADCFTV"));
			}
			statement.closeOnCompletion();
			conexao.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
        
		
		//System.out.println(ano+"-"+mes+"-"+dia);
		System.out.println("lista:"+lista.size());
		
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
						return new PasswordAuthentication("diretoriageral@marchezanmetais.com.br", "march@!metais$geral");
					}
				});

				/** Ativa Debug para sessão */
				session.setDebug(true);

				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress("diretoriageral@marchezanmetais.com.br"));
					// Remetente

					Address[] toUser = InternetAddress // Destinatário(s)
							.parse(nota.getEmail());
							//.parse("informatica@marchezanmetais.com.br ");

					message.setRecipients(Message.RecipientType.TO, toUser);
					message.setSubject("COMUNICADO");// Assunto
					message.setText("Olá "+nota.getNomecliente()+". \r\n"
							+ "Viemos, por meio deste, informar que Jonas Inácio da Silva, que ocupava o cargo de Gerente Nacional de Vendas da Marchezan, a partir de hoje dia 04/10, não faz mais parte do quadro de Colaboradores da empresa. \r\n"
							+ "Continuaremos realizando nosso trabalho e parceria junto a você nosso cliente. \r\n"
							+ "\r\n"
							+ "A nova gestão será realizada pelo profissional Elias Danilo Loureno, que passa a integrar o quadro de Colaboradores da Marchezan a partir de hoje, assumindo o cargo da Gerência Nacional de Vendas, o mesmo possui experiência a mais de 16 anos na área Comercial no segmento de metais Sanitários de grandes empresas e vem para somar junto a nós. \r\n"
							+ "\r\n"
							+ "Acreditamos que sua experiência irá contribuir para que, cada vez mais, a Marchezan possa desenvolver um ótimo trabalho junto a você.\r\n"
							+ "Atenciosamente, \r\n"
							+ "\r\n"
							+ "Diretoria Marchezan. \r\n"
							+ "\r\n");

					/** Método para enviar a mensagem criada */
					Transport.send(message);

					System.out.println("Feito!!!");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
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
