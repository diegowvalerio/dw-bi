package br.com.dwbidiretor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbidiretor.classe.TabelaPreco;

@FacesConverter(forClass = TabelaPreco.class,value="conversorTabelaPreco")
public class TabelaPrecoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (TabelaPreco) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof TabelaPreco) {
        	TabelaPreco entity= (TabelaPreco) value;
            if (entity != null && entity instanceof TabelaPreco && entity.getId() != null) {
                uiComponent.getAttributes().put( entity.getId().toString(), entity);
                return entity.getId().toString();
            }
        }
        return "";
    }
}