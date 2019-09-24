package br.com.dwbidiretor.bean;

import java.io.Serializable;


import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


@Named
@ViewScoped
public class BeanMapa implements Serializable {
	private static final long serialVersionUID = 1L;

	private MapModel simpleModel;

	@PostConstruct
	public void init() {
		
		simpleModel = new DefaultMapModel();
        
        //Shared coordinates
        LatLng coord1 = new LatLng(-7.6377836, -72.6717370);
        LatLng coord2 = new LatLng(36.883707, 30.689216);
        LatLng coord3 = new LatLng(36.879703, 30.706707);
        LatLng coord4 = new LatLng(36.885233, 30.702323);
          
        //Basic marker
        simpleModel.addOverlay(new Marker(coord1, "RONSY COMERCIAL DE FERRAGENS LTDA"));
        simpleModel.addOverlay(new Marker(coord2, "Ataturk Parki"));
        simpleModel.addOverlay(new Marker(coord3, "Karaalioglu Parki"));
        simpleModel.addOverlay(new Marker(coord4, "Kaleici"));
		
	}
	
	public void filtrar(){
		}
	
	 public MapModel getSimpleModel() {
	        return simpleModel;
	    }
}
