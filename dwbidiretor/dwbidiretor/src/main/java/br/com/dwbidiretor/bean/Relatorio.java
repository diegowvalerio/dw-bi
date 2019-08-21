package br.com.dwbidiretor.bean;


import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;


import org.omnifaces.util.Faces;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;


public class Relatorio{
	
	private HttpServletResponse response;
	private FacesContext context;
	private Connection con;
	
	public Relatorio() {
		this.context = FacesContext.getCurrentInstance();
		this.response = (HttpServletResponse) this.context.getExternalContext().getResponse();
	}
	
	/*venda ano mes*/
	public void rel_vendaanomes_lista(String vendedorlogado){
		try{
			String caminho = "";
			caminho = Faces.getRealPath("/pages/reports/vendas/VendasAnoMes");
			String caminhoimagem = Faces.getRealPath("/pages/reports/scve.png");
			JasperCompileManager.compileReportToFile(caminho+".jrxml");
			JasperReport rp = (JasperReport) JRLoader.loadObjectFromFile(caminho+".jasper");
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("VENDEDORLOGADO", vendedorlogado);
			
			params.put("LOGOS", caminhoimagem);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperPrint print = JasperFillManager.fillReport(rp, params,  getConexao());
						
			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			closeConnection();
			
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
		}
		
	}
	
	/*clientes*/
	public void rel_clientes_lista(String situacao,String vendedor,String vendedor1, Date data, Date data1,String nome){
		try{
			String caminho = "";
			caminho = Faces.getRealPath("/pages/reports/clientes/Clientes_Lista");
			String caminhoimagem = Faces.getRealPath("/pages/reports/scve.png");
			JasperCompileManager.compileReportToFile(caminho+".jrxml");
			JasperReport rp = (JasperReport) JRLoader.loadObjectFromFile(caminho+".jasper");
			
			Map<String, Object> params = new HashMap<String, Object>();
			boolean b = false;
			if (situacao.equals("A")){
				b = true;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b);
			}else if (situacao.equals("I")){
				b = false;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b);
			}else{
				boolean b1 = true;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b1);
			}
			
			if(vendedor.equals("")){
				params.put("VENDEDOR", 0);
				params.put("VENDEDOR1", 999999999);
			}else{
				params.put("VENDEDOR", Integer.parseInt(vendedor));
				params.put("VENDEDOR1", Integer.parseInt(vendedor1));
			}
			
			if(nome.equals("")){
				params.put("NOME","%%");
			}else{
				params.put("NOME", "%"+nome+"%");
			}
			
			params.put("DATA", data);
			params.put("DATA1", data1);			
			
			params.put("LOGOS", caminhoimagem);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperPrint print = JasperFillManager.fillReport(rp, params,  getConexao());
						
			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			closeConnection();
			
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
		}
		
	}
	/*produtos*/
	public void rel_produtos_lista(String filtro_situacao,String filtro_descabreviada, String filtro_descricao,String filtro_ean,String filtro_produto,
			String filtro_produto1){
		try{
			String caminho = "";
			caminho = Faces.getRealPath("/pages/reports/produtos/Produtos");
			String caminhoimagem = Faces.getRealPath("/pages/reports/scve.png");
			JasperCompileManager.compileReportToFile(caminho+".jrxml");
			JasperReport rp = (JasperReport) JRLoader.loadObjectFromFile(caminho+".jasper");
			
			Map<String, Object> params = new HashMap<String, Object>();
			boolean b = false;
			if (filtro_situacao.equals("A")){
				b = true;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b);
			}else if (filtro_situacao.equals("I")){
				b = false;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b);
			}else{
				boolean b1 = true;
				params.put("SITUACAO", b);
				params.put("SITUACAO1", b1);
			}
			
			if(filtro_produto.equals("")){
				params.put("PRODUTO", 0);
				params.put("PRODUTO1", 999999999);
			}else{
				params.put("PRODUTO", Integer.parseInt(filtro_produto));
				params.put("PRODUTO1", Integer.parseInt(filtro_produto1));
			}
			
			if(filtro_descricao.equals("")){
				params.put("DESCRICAO","%%");
			}else{
				params.put("DESCRICAO", "%"+filtro_descricao+"%");
			}
			
			if(filtro_descabreviada.equals("")){
				params.put("DESCABREVIADA","%%");
			}else{
				params.put("DESCABREVIADA", "%"+filtro_descabreviada+"%");
			}
			
			if(filtro_ean.equals("")){
				params.put("EAN","%%");
			}else{
				params.put("EAN", "%"+filtro_ean+"%");
			}
						
			params.put("LOGOS", caminhoimagem);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperPrint print = JasperFillManager.fillReport(rp, params,  getConexao());
						
			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			closeConnection();
			
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
		}
		
	}
	
	/*movimentacao*/
	public void rel_movimentacoes_lista(String filtro_tipo,String filtro_vendedor,String filtro_vendedor1,String filtro_cliente,String filtro_cliente1,Date filtro_data, Date filtro_data1){
		try{
			String caminho = "";
			caminho = Faces.getRealPath("/pages/reports/movimentacoes/Movimentacoes");
			String caminhoimagem = Faces.getRealPath("/pages/reports/scve.png");
			JasperCompileManager.compileReportToFile(caminho+".jrxml");
			JasperReport rp = (JasperReport) JRLoader.loadObjectFromFile(caminho+".jasper");
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if (filtro_tipo.equals("T")){
				params.put("TIPO", "%%");
			}else{
				params.put("TIPO", filtro_tipo);
			}
			
			if(filtro_cliente.equals("")){
				params.put("CLIENTE", 0);
				params.put("CLIENTE1", 999999999);
			}else{
				params.put("CLIENTE", Integer.parseInt(filtro_cliente));
				params.put("CLIENTE1", Integer.parseInt(filtro_cliente1));
			}
			
			if(filtro_vendedor.equals("")){
				params.put("VENDEDOR", 0);
				params.put("VENDEDOR1", 999999999);
			}else{
				params.put("VENDEDOR", Integer.parseInt(filtro_vendedor));
				params.put("VENDEDOR1", Integer.parseInt(filtro_vendedor));
			}			
			
			params.put("DATA", filtro_data);
			params.put("DATA1", filtro_data1);
						
			params.put("LOGOS", caminhoimagem);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperPrint print = JasperFillManager.fillReport(rp, params,  getConexao());
						
			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			closeConnection();
			
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
		}
		
	}

	/*comissao*/
	public void rel_comissao_lista(String filtro_tipo,String filtro_vendedor,String filtro_vendedor1,String filtro_cliente,String filtro_cliente1,Date filtro_data, Date filtro_data1){
		try{
			String caminho = "";
			caminho = Faces.getRealPath("/pages/reports/movimentacoes/Comissao");
			String caminhoimagem = Faces.getRealPath("/pages/reports/scve.png");
			JasperCompileManager.compileReportToFile(caminho+".jrxml");
			JasperReport rp = (JasperReport) JRLoader.loadObjectFromFile(caminho+".jasper");
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if (filtro_tipo.equals("T")){
				params.put("TIPO", "%%");
			}else{
				params.put("TIPO", filtro_tipo);
			}
			
			if(filtro_cliente.equals("")){
				params.put("CLIENTE", 0);
				params.put("CLIENTE1", 999999999);
			}else{
				params.put("CLIENTE", Integer.parseInt(filtro_cliente));
				params.put("CLIENTE1", Integer.parseInt(filtro_cliente1));
			}
			
			if(filtro_vendedor.equals("")){
				params.put("VENDEDOR", 0);
				params.put("VENDEDOR1", 999999999);
			}else{
				params.put("VENDEDOR", Integer.parseInt(filtro_vendedor));
				params.put("VENDEDOR1", Integer.parseInt(filtro_vendedor1));
			}			
			
			params.put("DATA", filtro_data);
			params.put("DATA1", filtro_data1);
						
			params.put("LOGOS", caminhoimagem);
			
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperPrint print = JasperFillManager.fillReport(rp, params,  getConexao());
						
			
			JasperExportManager.exportReportToPdfStream(print, baos);
			
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			response.setHeader("Content-disposition","inline; filename=relatorio.pdf");
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();
			closeConnection();
			
		}catch(Exception e){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao gerar o relatorio!"));
		}
		
	}
	/*conexao*/
	private Connection getConexao(){
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@MSERVER2:1521:AWORKSDB", "SEVEN", "SEVEN");
			return con;
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}

	private void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
