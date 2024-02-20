package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.SelectEvent;

import br.com.dwbidiretor.classe.FaseMateriaPrima;
import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.FasePedidoItem;
import br.com.dwbidiretor.classe.PedidoFase;
import br.com.dwbidiretor.classe.painel.Venda_Grupo;
import br.com.dwbidiretor.servico.ServicoFasePedido;
import br.com.dwbidiretor.servico.ServicoPedidoFase;


@Named
@ViewScoped
public class BeanFasePedido implements Serializable {
	private static final long serialVersionUID = 1L;

	private FasePedido fasePedido = new FasePedido();
	@Inject
	private ServicoFasePedido servico;
	private List<FasePedido> lista = new ArrayList<>();
	private List<FasePedidoItem> listaitem = new ArrayList<>();
	private FasePedidoItem fasePedidoItem  = new FasePedidoItem();
	
	@Inject
	private ServicoPedidoFase servicopedidofase;
	private List<PedidoFase> listapedidofase = new ArrayList<>();
	private PedidoFase pedidofase = new PedidoFase();
	
	private List<FaseMateriaPrima> listamateriaprima = new ArrayList<>();
	private FaseMateriaPrima faseMateriaPrima = new FaseMateriaPrima();
	
	int venda =1 ;
	int outros = 1 ;
	int selecionado  = 1;
	int totaldiasfase,mediadias = 0;
	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	private String pedido = "";
	private String lote = "";

	@PostConstruct
	public void init() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		data_grafico =c.getTime();
		
		lista = servico.fasepedido(1, 1,data_grafico,data_grafico2,"0000","0000");
				
	}
	
	public void filtrar(){
		if(selecionado == 1) {
			venda = 1;
			outros = 1;
		}else if(selecionado == 2) {
			venda = 1;
			outros = 0;
		}else if(selecionado == 3) {
			venda = 0;
			outros = 1;
		}
		
		String l= "";
		if(lote.equals("")) {
			l = "0000";
		}else {
			l = lote;
		}
		
		if(pedido.equals("")) {
			lista = servico.fasepedido(venda, outros,data_grafico,data_grafico2,"0000",l);
		}else {
			lista = servico.fasepedido(venda, outros,data_grafico,data_grafico2,pedido,l);
		}
		
		listapedidofase.clear();
	}
	
	public void onRowSelect(SelectEvent event) throws ParseException {		
		totaldiasfase = 0;
		fasePedido = (FasePedido) event.getObject();
		if(fasePedido !=null) {
			String l= "";
			if(lote.equals("")) {
				l = "0000";
			}else {
				l = lote;
			}
			
			if(pedido.equals("")) {
				listapedidofase = servicopedidofase.pedidofase(venda, outros, fasePedido.getRoteiroid(),data_grafico,data_grafico2,"0000",l);
			}else {
				listapedidofase = servicopedidofase.pedidofase(venda, outros, fasePedido.getRoteiroid(),data_grafico,data_grafico2,pedido,l);
			}
			for(PedidoFase f:listapedidofase) {
				if(f.getDataentradafase() == null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					f.setDataentradafase(sdf.format(f.getDatapedido()));
					
					Date a = f.getDatapedido();
					Date b = new Date();
					long dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;		
			        f.setDiasnafase((double) dt);
				}
				totaldiasfase = totaldiasfase + f.getDiasnafase().intValue();
			}
			if(fasePedido.getQtdepedido().intValue() == 0) {
				mediadias = totaldiasfase/1;
			}else {
			   mediadias = totaldiasfase/fasePedido.getQtdepedido().intValue();
			}
		}
		
    }
	
	public void onRowSelect2(SelectEvent event) throws ParseException {
		pedidofase = (PedidoFase) event.getObject();
		if(pedidofase != null) {
			listaitem = servico.fasepedidopedido(pedidofase.getPedidoid().toString());
		}
	}
	
	public void onRowSelect3(SelectEvent event) throws ParseException {
		fasePedidoItem = (FasePedidoItem) event.getObject();
		if(fasePedidoItem != null) {
			listamateriaprima = servicopedidofase.fasemateriaprima(fasePedidoItem.getPedido().toString(), fasePedidoItem.getProduto().toString());
		}
		
	}
	
	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public FasePedidoItem getFasePedidoItem() {
		return fasePedidoItem;
	}

	public void setFasePedidoItem(FasePedidoItem fasePedidoItem) {
		this.fasePedidoItem = fasePedidoItem;
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

	public FasePedido getFasePedido() {
		return fasePedido;
	}

	public void setFasePedido(FasePedido fasePedido) {
		this.fasePedido = fasePedido;
	}

	public List<FasePedido> getLista() {
		return lista;
	}

	public void setLista(List<FasePedido> lista) {
		this.lista = lista;
	}

	public int getVenda() {
		return venda;
	}

	public void setVenda(int venda) {
		this.venda = venda;
	}

	public int getOutros() {
		return outros;
	}

	public void setOutros(int outros) {
		this.outros = outros;
	}

	public int getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(int selecionado) {
		this.selecionado = selecionado;
	}

	public List<PedidoFase> getListapedidofase() {
		return listapedidofase;
	}

	public void setListapedidofase(List<PedidoFase> listapedidofase) {
		this.listapedidofase = listapedidofase;
	}

	public int getTotaldiasfase() {
		return totaldiasfase;
	}

	public void setTotaldiasfase(int totaldiasfase) {
		this.totaldiasfase = totaldiasfase;
	}

	public int getMediadias() {
		return mediadias;
	}

	public void setMediadias(int mediadias) {
		this.mediadias = mediadias;
	}
	
	public PedidoFase getPedidofase() {
		return pedidofase;
	}

	public void setPedidofase(PedidoFase pedidofase) {
		this.pedidofase = pedidofase;
	}

	public List<FasePedidoItem> getListaitem() {
		return listaitem;
	}

	public void setListaitem(List<FasePedidoItem> listaitem) {
		this.listaitem = listaitem;
	}

	public String gettotal() {
		float t = 0;

		for (FasePedido p : getLista()) {
			t = t + p.getVlpedido().floatValue();
		}
		
		return NumberFormat.getCurrencyInstance().format(t);
	}
	
	public String gettotalqtde() {
		float total = 0;

		for (FasePedido p : getLista()) {
			total = total + p.getQtdepedido().intValue();
		}
		return ""+total;
	}

	public String getPedido() {
		return pedido;
	}

	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	public List<FaseMateriaPrima> getListamateriaprima() {
		return listamateriaprima;
	}

	public void setListamateriaprima(List<FaseMateriaPrima> listamateriaprima) {
		this.listamateriaprima = listamateriaprima;
	}

	public FaseMateriaPrima getFaseMateriaPrima() {
		return faseMateriaPrima;
	}

	public void setFaseMateriaPrima(FaseMateriaPrima faseMateriaPrima) {
		this.faseMateriaPrima = faseMateriaPrima;
	}
	
	
	
}
