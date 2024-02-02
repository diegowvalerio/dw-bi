package br.com.dwbidiretor.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Replace;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import br.com.dwbidiretor.classe.Ligacao;
import br.com.dwbidiretor.classe.LigacaoH;
import br.com.dwbidiretor.msg.FacesMessageUtil;


@Named
@ViewScoped
public class BeanLigacoes implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LigacaoH ligacaoh = new LigacaoH();
	private Ligacao ligacao = new Ligacao();
	private List<LigacaoH> lista = new ArrayList<>();
	private List<Ligacao> ligacoes = new ArrayList<>();
	
	private List<Ligacao> selecionadas = new ArrayList<>();
	
	private String suaurl = "vono3.me";
	private String token = "708517aa-0919-5563-b993-cf29450f1fe7-7077";
	private String key = "cb896b77-c8f9-5ee5-b54a-9222ea1ef9e4-1699";
	private URL url;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	@PostConstruct
	public void init() {
		
		
	}
	
	public void filtrar() throws MalformedURLException{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data1 = dateFormat.format(data_grafico);
        String data2 = dateFormat.format(data_grafico2);
        
        ligacaoh = new LigacaoH();
		lista.clear();
		ligacoes.clear();
		
		url = new URL("https://"+suaurl+"/api/recording/"+token+"/"+key+"?date_ini="+data1+"&date_end="+data2+"&start=0&limit=10000");
		
		try {
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			
			String dados;
			StringBuilder json = new StringBuilder();
			
			while((dados = br.readLine()) != null) {
				json.append(dados);
			}
			
			//Ligacao x = new Gson().fromJson(json.toString(), Ligacao.class);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			
			lista = mapper.readValue(json.toString(),new TypeReference<List<LigacaoH>>(){});
			
			if(lista.size()>0) {
				ligacaoh = lista.get(0);
				ligacoes.clear();
				ligacoes.addAll(lista.get(0).getData());
			}else {
				ligacoes.clear();
			}
			
			if(ligacoes.size()>0) {
				for(Ligacao f:ligacoes) {
					
					try {
						f.setDataoriginal(f.getCalldate());
						
						DateFormat formatUS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = formatUS.parse(f.getCalldate());

						DateFormat formatBR = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						String dataConvertida = formatBR.format(date);
						
						f.setCalldate(dataConvertida);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(ligacoes.size());
	}
	
	public void verificaselecionados() {
		FacesMessageUtil.addMensagemInfo("Selecionada(s):"+selecionadas.size());
	}
	
	public void baixarzipado(String path) {
		try {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ec = ctx.getExternalContext();   
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
		
		File file = new File(path);
		String fileName = file.getName();
		String contentType = ec.getMimeType(fileName);
		int contentLength = (int) file.length();
		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseContentLength(contentLength);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		OutputStream output = response.getOutputStream();
		Files.copy(file.toPath(), output);
        ctx.responseComplete();
        
        file.delete();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void baixar() throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ec = ctx.getExternalContext();   
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
	
		String id = ligacao.getRecord_id().toString();
		//String id= "3109067";
		url = new URL("https://" + suaurl + "/api/recording/" + token + "/" + key + "/?id_record=" + id
				+ "&is_download=1");
		
		// informacoes do arquivo
		InputStream inputStream = url.openStream();
		String folderLocation = System.getenv("temp");
        String fileNamex = ligacao.getCalldate().replace(":", "-").replace("/", "-")+" "+ligacao.getClid()+" "+ligacao.getSource()+" "+ligacao.getDestination()+".mp3";
        File file = new File(folderLocation + File.separator + fileNamex);
		FileUtils.copyToFile(inputStream, file);
		//
		
		//prepara para baixar
		String fileName = file.getName();
		String contentType = ec.getMimeType(fileName);
		int contentLength = (int) file.length();
		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseContentLength(contentLength);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		OutputStream output = response.getOutputStream();
		Files.copy(file.toPath(), output);
        ctx.responseComplete();
        file.delete();
	}

	public void download() throws IOException {
		if(selecionadas.size()>0) { 
			List<String> files = new ArrayList<>();

			for(Ligacao xf:selecionadas) {				
				String id = xf.getRecord_id().toString();
				url = new URL("https://" + suaurl + "/api/recording/" + token + "/" + key + "/?id_record=" + id
						+ "&is_download=1");

				// informacoes do arquivo
				InputStream inputStream = url.openStream();
				String folderLocation = System.getenv("temp");
		        String fileName = xf.getCalldate().replace(":", "-").replace("/", "-")+" "+xf.getClid()+" "+xf.getSource()+" "+xf.getDestination()+".mp3";
		        File file = new File(folderLocation + File.separator + fileName);
		        
				//File file = File.createTempFile(id, ".mp3");
				FileUtils.copyToFile(inputStream, file);
				
				files.add(file.getPath());	
			}
			
			if(files.size()>0) {
				zipFile(files);
			}
			
		}else {
			FacesMessageUtil.addMensagemWarn("Selecione qual ligação deseja baixar !");
		}
	}
	
	public void zipFile(List<String> files) {
		try {
			File z = File.createTempFile("zipado", ".zip");
			String zipFilePath = z.getPath();
			
			FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			
			
			for (String f : files) {
				File inputFile = new File(f);				
				
				FileInputStream fileInputStream = new FileInputStream(inputFile);
				
				// Um ZipEntry é um apontamento para um arquivo ZIP
				ZipEntry zipEntry = new ZipEntry(inputFile.getName());
				zipOutputStream.putNextEntry(zipEntry);
				
				byte[] buf = new byte[1024];
				int bytesRead;

				// Realiza a leitura dos dados do arquivo
				// e escreve no stream de saída que será o Zip
				while ((bytesRead = fileInputStream.read(buf)) > 0) {
					zipOutputStream.write(buf, 0, bytesRead);
				}				

				// Fecha o arquivo ZipEntry que armazena o conteúdo
				// do arquivo
				zipOutputStream.closeEntry();
				fileInputStream.close();
				inputFile.delete();
				//System.out.println("Arquivo de entrada: " + inputFile.getCanonicalPath() + " saida do arquivo ZIP:" + zipFilePath);
			}
			
			zipOutputStream.close();
			fileOutputStream.close();
			
			baixarzipado(zipFilePath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void compactarParaZip(final String arqSaida, final List<String> arqEntradas) throws IOException {
	    int cont;
	    final byte[] dados = new byte[1024];

	    final FileOutputStream destino = new FileOutputStream(new File(arqSaida));
	    final ZipOutputStream saida = new ZipOutputStream(new BufferedOutputStream(destino));

	    for (final String arqEntrada : arqEntradas) {
	        final File file = new File(arqEntrada);
	        final FileInputStream streamDeEntrada = new FileInputStream(file);
	        final BufferedInputStream origem = new BufferedInputStream(streamDeEntrada, 1024);
	        final ZipEntry entry = new ZipEntry(file.getName());
	        saida.putNextEntry(entry);

	        while ((cont = origem.read(dados, 0, 1024)) != -1) {
	            saida.write(dados, 0, cont);
	        }
	        origem.close();
	    }

	    saida.close();
	}
	
	public Date getData_grafico() {
		return data_grafico;
	}

	public void setData_grafico(Date data_grafico) {
		this.data_grafico = data_grafico;
	}

	public Date getData_grafico2() {
		return data_grafico2;
	}

	public void setData_grafico2(Date data_grafico2) {
		this.data_grafico2 = data_grafico2;
	}

	public String getSuaurl() {
		return suaurl;
	}

	public void setSuaurl(String suaurl) {
		this.suaurl = suaurl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public List<LigacaoH> getLista() {
		return lista;
	}

	public void setLista(List<LigacaoH> lista) {
		this.lista = lista;
	}

	public List<Ligacao> getLigacoes() {
		return ligacoes;
	}

	public void setLigacoes(List<Ligacao> ligacoes) {
		this.ligacoes = ligacoes;
	}

	public LigacaoH getLigacaoh() {
		return ligacaoh;
	}

	public void setLigacaoh(LigacaoH ligacaoh) {
		this.ligacaoh = ligacaoh;
	}

	public List<Ligacao> getSelecionadas() {
		return selecionadas;
	}

	public void setSelecionadas(List<Ligacao> selecionadas) {
		this.selecionadas = selecionadas;
	}

	public Ligacao getLigacao() {
		return ligacao;
	}

	public void setLigacao(Ligacao ligacao) {
		this.ligacao = ligacao;
	}


	
}
