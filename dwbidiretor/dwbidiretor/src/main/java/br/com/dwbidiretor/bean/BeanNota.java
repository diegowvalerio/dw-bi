package br.com.dwbidiretor.bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.io.FileUtils;

import br.com.dwbidiretor.classe.Cliente;
import br.com.dwbidiretor.classe.Nota;
import br.com.dwbidiretor.servico.ServicoCliente;
import br.com.dwbidiretor.servico.ServicoNota;

@Named
@ViewScoped
public class BeanNota implements Serializable {
	private static final long serialVersionUID = 1L;

	private Nota notas = new Nota();
	
	@Inject
	private ServicoNota servico;
	private List<Nota> lista = new ArrayList<>();

	//filtro cliente
	private Cliente cliente = new Cliente();
	@Inject
	private ServicoCliente servicocliente;
	private List<Cliente> listacliente = new ArrayList<>();

	private String clientefiltrado;
	private String nota;
	
	private URL url;

	@PostConstruct
	public void init() {
		clientefiltrado = "1";
		nota = null;
		listacliente = servicocliente.clientes();
	}
	
	public void filtrar(){
		
		if(cliente == null && nota.isEmpty()) {
			lista.clear();
		}else {
			if (cliente == null){
				clientefiltrado = "1";
			}else{
				clientefiltrado = cliente.getCodigocliente().toString();
			}
			
			if (nota.isEmpty()) {
				nota = "1";
			}
			lista= servico.notas(clientefiltrado, nota);
		}
		
	}
	
	public void baixar() throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ec = ctx.getExternalContext();   
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
	
		String id = notas.getChavexml().toString();
		//String id= "3109067";
		url = new URL("http://177.72.156.109:8082/api/resources/v1/downloadNF/linkXMLNfe/"+id);
		
		// informacoes do arquivo
		InputStream inputStream = url.openStream();
		String folderLocation = System.getenv("temp");
        String fileNamex = notas.getNrnota()+".xml";
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

	public void baixardanfe() throws IOException {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExternalContext ec = ctx.getExternalContext();   
		HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
	
		String id = notas.getChavexml().toString();
		//String id= "3109067";
		url = new URL("http://177.72.156.109:8082/api/resources/v1/downloadNF/linkDanfeNfe/"+id);
		
		// informacoes do arquivo
		InputStream inputStream = url.openStream();
		String folderLocation = System.getenv("temp");
        String fileNamex = notas.getNrnota()+".pdf";
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
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public ServicoNota getServico() {
		return servico;
	}

	public void setServico(ServicoNota servico) {
		this.servico = servico;
	}

	public List<Nota> getLista() {
		return lista;
	}

	public void setLista(List<Nota> lista) {
		this.lista = lista;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ServicoCliente getServicocliente() {
		return servicocliente;
	}

	public void setServicocliente(ServicoCliente servicocliente) {
		this.servicocliente = servicocliente;
	}

	public List<Cliente> getListacliente() {
		return listacliente;
	}

	public void setListacliente(List<Cliente> listacliente) {
		this.listacliente = listacliente;
	}

	public String getClientefiltrado() {
		return clientefiltrado;
	}

	public void setClientefiltrado(String clientefiltrado) {
		this.clientefiltrado = clientefiltrado;
	}

	public Nota getNotas() {
		return notas;
	}

	public void setNotas(Nota notas) {
		this.notas = notas;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}
	
	
}
