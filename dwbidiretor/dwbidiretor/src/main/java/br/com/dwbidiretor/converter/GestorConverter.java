package br.com.dwbidiretor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbidiretor.classe.Gestor;

@FacesConverter(forClass = Gestor.class,value="conversorGestor")
public class GestorConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Gestor) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Gestor) {
        	Gestor entity= (Gestor) value;
            if (entity != null && entity instanceof Gestor && entity.getGestorid() != null) {
                uiComponent.getAttributes().put( entity.getGestorid().toString(), entity);
                return entity.getGestorid().toString();
            }
        }
        return "";
    }
}