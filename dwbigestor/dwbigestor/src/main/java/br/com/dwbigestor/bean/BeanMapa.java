package br.com.dwbigestor.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;

import br.com.dwbigestor.classe.Mapa;
import br.com.dwbigestor.classe.Vendedor;
import br.com.dwbigestor.servico.ServicoMapa;
import br.com.dwbigestor.servico.ServicoVendedor;

@Named
@ViewScoped
public class BeanMapa implements Serializable {
	private static final long serialVersionUID = 1L;

	private MapModel simpleModel;
	private Marker marker;
	
	private Mapa mapa = new Mapa();
	@Inject
	private ServicoMapa servico;
	private List<Mapa> listamapa = new ArrayList<>();
	
	@Inject
	private ServicoVendedor servicovendedor;
	private List<Vendedor> listavendedor = new ArrayList<>();
	private Vendedor vendedor = new Vendedor();
	

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
		
		
		gestorfiltrado = "0";
		gestorfiltrado2 = "999999";
		
		simpleModel = new DefaultMapModel();
        //Shared coordinates
        LatLng coord1 = new LatLng(-23.010324, -53.193782);
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "MARCHEZAN METAIS","","https://maps.google.com/mapfiles/ms/micons/red-dot.png"));
	
	}
	
	public void filtrar(){
		simpleModel = new DefaultMapModel();
		
		if (vendedor == null){
			vendedorfiltrado = "0";
			vendedorfiltrado2 = "999999";
			
		}else{
			vendedorfiltrado = vendedor.getCodigovendedor().toString();
			vendedorfiltrado2 = vendedor.getCodigovendedor().toString();
		}
			
		
		listamapa = servico.mapa(data_grafico, data_grafico2,vendedorfiltrado,vendedorfiltrado2, gestorfiltrado, gestorfiltrado2);
		
		LatLng coord = new LatLng(-23.010324, -53.193782);
        //Basic marker
        simpleModel.addOverlay(new Marker(coord, "MARCHEZAN METAIS","","https://maps.google.com/mapfiles/ms/micons/red-dot.png"));
        
		for (Mapa mapa : getListamapa()) {
			
			if(mapa.getTotalperiodo().doubleValue() > 0) {
				//Shared coordinates
		        LatLng coord1 = new LatLng(mapa.getLatitude().doubleValue(), mapa.getLongitude().doubleValue());
		        //Basic marker
		        simpleModel.addOverlay(new Marker(coord1, "CLIENTE: "+mapa.getCliente()+"-"+mapa.getNomecliente()+", ENDEREÇO: "+mapa.getEndereco()+", BAIRRO: "+mapa.getBairro()+
		        		", "+mapa.getNumero()+", CEP: "+mapa.getCep()+", CIDADE/UF: "+mapa.getCidade()+"-"+mapa.getUf()+", DATA ULTIMA COMPRA: "+mapa.getUltimacompra()+
		        		", VENDEDOR: "+mapa.getVendedor()+"-"+mapa.getNomevendedor(),"",
		        		"https://maps.google.com/mapfiles/ms/micons/green-dot.png"));
			}else {
			 			
	        //Shared coordinates
	        LatLng coord1 = new LatLng(mapa.getLatitude().doubleValue(), mapa.getLongitude().doubleValue());
	        //Basic marker
	        simpleModel.addOverlay(new Marker(coord1, "CLIENTE: "+mapa.getCliente()+"-"+mapa.getNomecliente()+", ENDEREÇO: "+mapa.getEndereco()+", BAIRRO: "+mapa.getBairro()+
	        		", "+mapa.getNumero()+", CEP: "+mapa.getCep()+", CIDADE/UF: "+mapa.getCidade()+"-"+mapa.getUf()+", DATA ULTIMA COMPRA: "+mapa.getUltimacompra()+
	        		", VENDEDOR: "+mapa.getVendedor()+"-"+mapa.getNomevendedor(),"",
	        		"https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
			}
		}
		
		
				
	}
	
	 public Mapa getMapa() {
		return mapa;
	}

	public void setMapa(Mapa mapa) {
		this.mapa = mapa;
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

	public MapModel getSimpleModel() {
	        return simpleModel;
	    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
    }
      
    public Marker getMarker() {
        return marker;
    }

}
