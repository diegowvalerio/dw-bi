package br.com.dwbidiretor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.jsf2leaf.model.LatLong;
import com.jsf2leaf.model.Layer;
import com.jsf2leaf.model.Map;
import com.jsf2leaf.model.Marker;
import com.jsf2leaf.model.Pulse;

import br.com.dwbidiretor.classe.Gestor;
import br.com.dwbidiretor.classe.Mapa;
import br.com.dwbidiretor.classe.Vendedor;
import br.com.dwbidiretor.servico.ServicoGestor;
import br.com.dwbidiretor.servico.ServicoMapa;
import br.com.dwbidiretor.servico.ServicoVendedor;


@Named
@ViewScoped
public class BeanMapa2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map map = new Map();

	@Inject
	private ServicoMapa servico;
	private List<Mapa> listamapa = new ArrayList<>();
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	
	//filtro gestor
	private Gestor gestor = new Gestor();
	@Inject
	private ServicoGestor servicogestor;
	private List<Gestor> listagestor = new ArrayList<>();

	private String vendedorlogado;

	private Date data_grafico = new Date();
	private Date data_grafico2 = new Date();
	
	private String vendedorfiltrado;
	private String vendedorfiltrado2;
	private String gestorfiltrado;
	private String gestorfiltrado2;
	
	
	@PostConstruct
	public void init() {
		
		listavendedor = servicovendedor.consultavendedor();
		vendedorfiltrado = "0";
		vendedorfiltrado2 = "999999";
		
		listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		gestorfiltrado = "0";
		gestorfiltrado2 = "999999";
		
		Layer placesLayer = (new Layer()).setLabel("Marchezan");
		//placesLayer.addMarker(new Marker(new LatLong("-23.010324","-53.193782"),"<b>Marchezan Metais</b>"));
		placesLayer.addMarker(new Marker(new LatLong("-23.010324","-53.193782"),"<b>Marchezan Metais</b>", new Pulse(true, 10, "#FF8C00")));
		
		map.setWidth("100%").setHeight("500px").setCenter(new LatLong("-23.010324","-53.193782")).setZoom(3);
		map.addLayer(placesLayer);
		map.setMiniMap(false);
		
	
	}
	
	public void filtrar2() {
		
		
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
		
		if (gestor == null){
			gestorfiltrado = "0";
			gestorfiltrado2 = "999999";
			
		}else{
			gestorfiltrado = gestor.getGestorid().toString();
			gestorfiltrado2 = gestor.getGestorid().toString();
		}
		
		listamapa = servico.mapa(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		Layer placesLayer = new Layer();
		Layer placesLayer2 = new Layer();
		//placesLayer.addMarker(new Marker(new LatLong("-23.010324","-53.193782"),"<b>Marchezan Metais</b>"));
		
		for (Mapa mapa : listamapa) {
			if(mapa.getTotalperiodo().doubleValue() > 0) {
				placesLayer2.addMarker(new Marker(new LatLong(mapa.getLatitude().toString(),mapa.getLongitude().toString()),"CLIENTE: "+mapa.getCliente()+"-"+mapa.getNomecliente()+", ENDEREÇO: "+mapa.getEndereco()+", BAIRRO: "+mapa.getBairro()+
		        		", "+mapa.getNumero()+", CEP: "+mapa.getCep()+", CIDADE/UF: "+mapa.getCidade()+"-"+mapa.getUf()+", DATA ULTIMA COMPRA: "+mapa.getUltimacompra()+
		        		", VENDEDOR: "+mapa.getVendedor()+"-"+mapa.getNomevendedor(), new Pulse(true, 10, "#FF4040")));
			}else {
				placesLayer.addMarker(new Marker(new LatLong(mapa.getLatitude().toString(),mapa.getLongitude().toString()),"CLIENTE: "+mapa.getCliente()+"-"+mapa.getNomecliente()+", ENDEREÇO: "+mapa.getEndereco()+", BAIRRO: "+mapa.getBairro()+
		        		", "+mapa.getNumero()+", CEP: "+mapa.getCep()+", CIDADE/UF: "+mapa.getCidade()+"-"+mapa.getUf()+", DATA ULTIMA COMPRA: "+mapa.getUltimacompra()+
		        		", VENDEDOR: "+mapa.getVendedor()+"-"+mapa.getNomevendedor() , new Pulse(true, 10, "#1E90FF")));
			}
		}
		
		

		map.setWidth("100%").setHeight("500px").setCenter(new LatLong("-23.010324","-53.193782")).setZoom(3);
		
		if(placesLayer.getMarkers().size()>0) {
			placesLayer.setLabel("Sem Venda");
			map.addLayer(placesLayer);
		}
		if(placesLayer2.getMarkers().size()>0) {
			placesLayer2.setLabel("Com Venda");
			map.addLayer(placesLayer2);
		}
		map.setZoomControl(true);
		map.setZoomEnabled(true);
		map.setMiniMap(false);
		
		listamapa.clear();
		map = new Map();
		
	}
	
	
	public void filtragestor() {
		if (getVendedor() != null){
			List<Gestor> listagestorf = new ArrayList<>();
			gestor = new Gestor();
			listagestorf = servicogestor.consultagestor(getVendedor().getCodigovendedor().toString(),getVendedor().getCodigovendedor().toString());
			gestor = listagestorf.get(0);
		}else{
			//listagestor = new ArrayList<>();
			gestor = new Gestor();
			//listagestor = servicogestor.consultagestor(vendedorfiltrado,vendedorfiltrado2);
		}
	}
	
	public List<Mapa> getListamapa() {
		return listamapa;
	}

	public void setListamapa(List<Mapa> listamapa) {
		this.listamapa = listamapa;
	}

	public List<Vendedor> getListavendedor() {
		return listavendedor;
	}

	public void setListavendedor(List<Vendedor> listavendedor) {
		this.listavendedor = listavendedor;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Gestor getGestor() {
		return gestor;
	}

	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	public List<Gestor> getListagestor() {
		return listagestor;
	}

	public void setListagestor(List<Gestor> listagestor) {
		this.listagestor = listagestor;
	}

	public String getVendedorlogado() {
		return vendedorlogado;
	}

	public void setVendedorlogado(String vendedorlogado) {
		this.vendedorlogado = vendedorlogado;
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

	public String getVendedorfiltrado() {
		return vendedorfiltrado;
	}

	public void setVendedorfiltrado(String vendedorfiltrado) {
		this.vendedorfiltrado = vendedorfiltrado;
	}

	public String getVendedorfiltrado2() {
		return vendedorfiltrado2;
	}

	public void setVendedorfiltrado2(String vendedorfiltrado2) {
		this.vendedorfiltrado2 = vendedorfiltrado2;
	}

	public String getGestorfiltrado() {
		return gestorfiltrado;
	}

	public void setGestorfiltrado(String gestorfiltrado) {
		this.gestorfiltrado = gestorfiltrado;
	}

	public String getGestorfiltrado2() {
		return gestorfiltrado2;
	}

	public void setGestorfiltrado2(String gestorfiltrado2) {
		this.gestorfiltrado2 = gestorfiltrado2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

}
