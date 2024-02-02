package br.com.dwbidiretor.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;


import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import br.com.dwbidiretor.classe.Imagem;
import br.com.dwbidiretor.servico.ServicoProduto;


@ManagedBean
@SessionScoped
public class BeanImgPrd implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ServicoProduto servico;

	 public StreamedContent getImage() throws IOException {
	        FacesContext context = FacesContext.getCurrentInstance();

	        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
	            // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
	            return new DefaultStreamedContent();
	        }
	        else {
	            // So, browser is requesting the image. Return a real StreamedContent with the image bytes.
	            String produtoid = context.getExternalContext().getRequestParameterMap().get("imgPRD");
	            Imagem img = servico.imagem(produtoid);
	            return new DefaultStreamedContent(new ByteArrayInputStream(img.getImg()));
	        }
	    }


}
