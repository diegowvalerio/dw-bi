package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.event.ReorderEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

import br.com.dwbidiretor.classe.Etapa;
import br.com.dwbidiretor.classe.Etapa_Item;
import br.com.dwbidiretor.classe.Projeto;
import br.com.dwbidiretor.msg.FacesMessageUtil;
import br.com.dwbidiretor.servico.ServicoProjeto;

@Named
@ViewScoped
public class BeanProjeto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Projeto projeto = new Projeto();
	private Etapa etapa = new Etapa();
	private Etapa_Item item = new Etapa_Item();

	@Inject
	private ServicoProjeto servico;
	private List<Projeto> lista;
	private List<Etapa> listaetapas = new ArrayList<>();
	private List<Etapa_Item> items = new ArrayList<>();

	// timeline
	private TimelineModel timelineProjeto;
	private LocalDateTime min;
	private LocalDateTime max;
	private long zoomMin;
	private long zoomMax;

	private Date data;

	public BeanProjeto() {
		data = new Date();
		this.projeto.setStatus("ABERTO");
	}

	@PostConstruct
	public void carregar() {

		lista = servico.Sconsultar();
		this.projeto = this.getProjeto();
		listaetapas = this.projeto.getEtapas();
		for (Etapa e : listaetapas) {
			items.addAll(e.getItems());
		}
		this.projeto.setDtcadastro(data);
		criatimelineProjeto();

	}

	public String salvar() {
		try {
			projeto.setEtapas(listaetapas);
			servico.Ssalvar(projeto);
			lista = servico.Sconsultar();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "projetos";
	}

	public void excluir() {
		try {
			servico.Sexcluir(projeto.getIdprojeto());
		} catch (Exception e) {
			if (e.getCause().toString().contains("ConstraintViolationException")) {
				FacesMessageUtil.addMensagemError("Registro utilizado em outro local!.");
			} else {
				FacesMessageUtil.addMensagemError(e.getCause().toString());
			}
		}
		lista = servico.Sconsultar();
	}
	
	public String encaminha() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
		session.setAttribute("projetoAux", this.projeto);

		return "edita-projeto";
	}

	// etapa
	public void addEtapa() {
		etapa = new Etapa();
		etapa.setDtcadastro(new Date());
		etapa.setStatus("ABERTO");
		etapa.setProjeto(projeto);

	}

	public void editarsalvarEtapa() {

		try {
			int index = listaetapas.indexOf(etapa);
			if (index > -1) {
				listaetapas.remove(index);
				etapa.setProjeto(projeto);
				listaetapas.add(index, etapa);
			} else {
				etapa.setProjeto(projeto);
				listaetapas.add(etapa);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		etapa = new Etapa();

		atualizaordem();
	}

	public void onRowReorder(ReorderEvent event) {
		atualizaordem();
	}

	public void atualizaordem() {
		for (Etapa p : listaetapas) {
			int index = listaetapas.indexOf(p);
			p.setOrdenacao(index);
		}
	}

	public void remove() {
		if (etapa.getItems().size() > 0) {
			FacesMessageUtil.addMensagemError(
					"Etapa possui items vinculados, não é possivél remover a Etapa, remova os itens primeiro !");
		} else {
			int index = listaetapas.indexOf(etapa);
			if (index > -1) {
				this.listaetapas.remove(index);
			}
		}

		atualizaordem();
	}

	// item

	public void baixonivel(Etapa_Item e) {
		int index = -100;

		for (Etapa x : listaetapas) {
			if(x.equals(e.getEtapa())) {
				
				if (x.getItems().size() != x.getItems().indexOf(e)+1) {
					for(Etapa_Item i : items) {
						if(i.equals(e)) {
							index = items.indexOf(i);	
						}
					}
					if(index != -100) {
						Collections.swap(items, index, index+1);
						atualizalista();
					}
				}else {
					FacesMessageUtil.addMensagemInfo("Não há nível Inferior !");
				}
			}
		}	
		
	}
	

	public void altonivel(Etapa_Item e) {
		int index = -100;

		for (Etapa x : listaetapas) {
			if(x.equals(e.getEtapa())) {
				
				if (x.getItems().indexOf(e)-1 >= 0) {
					for(Etapa_Item i : items) {
						if(i.equals(e)) {
							index = items.indexOf(i);	
						}
					}
					if(index != -100) {
						Collections.swap(items, index, index-1);
						atualizalista();
					}
				}else {
					FacesMessageUtil.addMensagemInfo("Não há nível Superior !");
				}
			}
		}	
		
	}

	public void addEtapaItem(Etapa e) {
		item = new Etapa_Item();
		item.setDtcadastro(new Date());
		item.setStatus("ABERTO");
		item.setEtapa(e);

	}

	public void editarsalvarEtapaItem() {
		try {
			int index = items.indexOf(item);
			if (index > -1) {
				items.remove(index);
				// item.setEtapa(etapa);
				items.add(index, item);
			} else {
				// item.setEtapa(etapa);
				items.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// item = new Etapa_Item();

		atualizalista();

	}

	public void atualizalista() {
		for (Etapa p : listaetapas) {
			List<Etapa_Item> its = new ArrayList<>();
			int ii = 1;
			for (Etapa_Item i : items) {
				if (i.getEtapa().equals(p)) {
					i.setOrdenacao(ii++);
					its.add(i);
				}
			}
			p.setItems(its);
		}

	}

	public void removeItem() {
		int index = items.indexOf(item);
		if (index > -1) {
			this.items.remove(index);
		}
		
		atualizalista();

	}

	// fim etapa

	private void criatimelineProjeto() {
		Date ini = data;
		Date fim = data;
		timelineProjeto = new TimelineModel();
		if (projeto.getDtinicio() != null) {
			ini = projeto.getDtinicio();
		}
		if (projeto.getDtprevista() != null) {
			fim = projeto.getDtprevista();
		}
		timelineProjeto.add(new TimelineEvent("Inicio", ini));
		timelineProjeto.add(new TimelineEvent("Previsto", fim));
		// timelineProjeto.add(new TimelineEvent(projeto.getNome(),ini,fim));

	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<Projeto> getLista() {
		return lista;
	}

	public void setLista(List<Projeto> lista) {
		this.lista = lista;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TimelineModel getTimelineProjeto() {
		return timelineProjeto;
	}

	public void setTimelineProjeto(TimelineModel timelineProjeto) {
		this.timelineProjeto = timelineProjeto;
	}

	public LocalDateTime getMin() {
		return min;
	}

	public void setMin(LocalDateTime min) {
		this.min = min;
	}

	public LocalDateTime getMax() {
		return max;
	}

	public void setMax(LocalDateTime max) {
		this.max = max;
	}

	public long getZoomMin() {
		return zoomMin;
	}

	public void setZoomMin(long zoomMin) {
		this.zoomMin = zoomMin;
	}

	public long getZoomMax() {
		return zoomMax;
	}

	public void setZoomMax(long zoomMax) {
		this.zoomMax = zoomMax;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

	public List<Etapa> getListaetapas() {
		return listaetapas;
	}

	public void setListaetapas(List<Etapa> listaetapas) {
		this.listaetapas = listaetapas;
	}

	public Etapa_Item getItem() {
		return item;
	}

	public void setItem(Etapa_Item item) {
		this.item = item;
	}

	public List<Etapa_Item> getItems() {
		return items;
	}

	public void setItems(List<Etapa_Item> items) {
		this.items = items;
	}

}
