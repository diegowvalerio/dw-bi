package br.com.dwbidiretor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbidiretor.classe.Almoxarifado;

@FacesConverter(forClass = Almoxarifado.class,value="conversorAlmoxarifado")
public class AlmoxarifadoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Almoxarifado) uiComponent.getAttributes().get(value);
        }
        return null;
    }
 
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Almoxarifado) {
        	Almoxarifado entity= (Almoxarifado) value;
            if (entity != null && entity instanceof Almoxarifado && entity.getAlmoxarifadoid() != null) {
                uiComponent.getAttributes().put( entity.getAlmoxarifadoid().toString(), entity);
                return entity.getAlmoxarifadoid().toString();
            }
        } 
        return "";
    }
}