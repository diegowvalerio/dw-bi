package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
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

import org.primefaces.event.SelectEvent;


import br.com.dwbidiretor.classe.FasePedido;
import br.com.dwbidiretor.classe.PedidoFase;
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
	
	@Inject
	private ServicoPedidoFase servicopedidofase;
	private List<PedidoFase> listapedidofase = new ArrayList<>();
	
	int venda,outros = 1 ;
	int selecionado  = 1;
	int totaldiasfase,mediadias = 0;

	@PostConstruct
	public void init() {
		lista = servico.fasepedido(venda, outros);
		
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
		lista = servico.fasepedido(venda, outros);
		listapedidofase.clear();
	}
	
	public void onRowSelect(SelectEvent event) throws ParseException {
		totaldiasfase = 0;
		fasePedido = (FasePedido) event.getObject();
		if(fasePedido !=null) {
			listapedidofase = servicopedidofase.pedidofase(venda, outros, fasePedido.getRoteiroid());
			for(PedidoFase f:listapedidofase) {
				if(f.getDataentradafase() == null) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
					f.setDataentradafase(sdf.format(f.getDatapedido()));
					
					Date a = f.getDatapedido();
					Date b = new Date();
					long dt = b.getTime() - a.getTime();
					dt = dt / 86400000L;		
			        f.setDiasnafase(new BigDecimal(dt));
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
	
	
	
}
