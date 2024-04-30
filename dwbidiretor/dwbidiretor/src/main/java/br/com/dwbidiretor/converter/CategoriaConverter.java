package br.com.dwbidiretor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbidiretor.classe.Categoria;

@FacesConverter(forClass = Categoria.class,value="conversorCategoria")
public class CategoriaConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Categoria) uiComponent.getAttributes().get(value);
        }
        return null;
    }
 
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Categoria) {
        	Categoria entity= (Categoria) value;
            if (entity != null && entity instanceof Categoria && entity.getCategoriaid() != null) {
                uiComponent.getAttributes().put( entity.getCategoriaid().toString(), entity);
                return entity.getCategoriaid().toString();
            }
        } 
        return "";
    }
}