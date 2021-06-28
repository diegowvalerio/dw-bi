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
							+ " https://docs.google.com/forms/d/e/1FAIpQLSehihD-zigDIGy0Z6D6k1Go7H2BwtJX5AxPdeY4Q6XFCBvsoA/viewform?usp=sf_link ");

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
