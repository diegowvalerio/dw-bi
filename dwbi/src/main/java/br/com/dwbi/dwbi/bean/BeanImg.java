package br.com.dwbi.dwbi.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.dwbi.classe.VendasEmGeralItem;
import br.com.dwbi.dwbi.servico.ServicoVendasemGeralItem;

@ManagedBean
@SessionScoped
public class BeanImg implements Serializable {

	private VendasEmGeralItem vendasEmGeralitem = new VendasEmGeralItem();
	@Inject
	private ServicoVendasemGeralItem servicoitem;

	public VendasEmGeralItem getVendasEmGeralitem() {
		return vendasEmGeralitem;
	}

	public void setVendasEmGeralitem(VendasEmGeralItem vendasEmGeralitem) {
		this.vendasEmGeralitem = vendasEmGeralitem;
	}

	public DefaultStreamedContent imagem() {
		System.out.println('1');
		StreamedContent img = null;
		DefaultStreamedContent dsc = null;
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = (HttpSession) request.getSession();

		if (session.getAttribute("item") != null && !session.getAttribute("item").equals("")) {

			vendasEmGeralitem = (VendasEmGeralItem) session.getAttribute("item");
			if (vendasEmGeralitem.getImagem() != null) {
				System.out.println(vendasEmGeralitem.getNomeproduto());
				try {

					//img = new DefaultStreamedContent(new ByteArrayInputStream(vendasEmGeralitem.getImagem().getBytes(1, (int) vendasEmGeralitem.getImagem().length())),	"image/jpeg");
					 InputStream is = vendasEmGeralitem.getImagem().getBinaryStream(); //new ByteArrayInputStream(vendasEmGeralitem.getImagem().getBytes(1, (int) vendasEmGeralitem.getImagem().length()));
					 dsc = new DefaultStreamedContent(is, "image/jpeg");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		session.removeAttribute("item");

		return dsc;
	}

}
