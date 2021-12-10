package br.com.dwbigestor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbigestor.classe.Venda_Subgrupo;

@FacesConverter(forClass = Venda_Subgrupo.class,value="conversorVendaSubGrupo")
public class Venda_SubGrupoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Venda_Subgrupo) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Venda_Subgrupo) {
        	Venda_Subgrupo entity= (Venda_Subgrupo) value;
            if (entity != null && entity instanceof Venda_Subgrupo && entity.getIdsubgrupo() != null) {
                uiComponent.getAttributes().put( entity.getIdsubgrupo().toString(), entity);
                return entity.getIdsubgrupo().toString();
            }
        }
        return "";
    }
}