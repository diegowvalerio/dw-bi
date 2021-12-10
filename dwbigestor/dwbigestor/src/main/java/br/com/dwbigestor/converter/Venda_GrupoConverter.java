package br.com.dwbigestor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbigestor.classe.Venda_Grupo;

@FacesConverter(forClass = Venda_Grupo.class,value="conversorVendaGrupo")
public class Venda_GrupoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Venda_Grupo) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Venda_Grupo) {
        	Venda_Grupo entity= (Venda_Grupo) value;
            if (entity != null && entity instanceof Venda_Grupo && entity.getIdgrupo() != null) {
                uiComponent.getAttributes().put( entity.getIdgrupo().toString(), entity);
                return entity.getIdgrupo().toString();
            }
        }
        return "";
    }
}