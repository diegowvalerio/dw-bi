package br.com.dwbidiretor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbidiretor.classe.SigeModulo;

@FacesConverter(forClass = SigeModulo.class,value="conversorSigeModulo")
public class SigeModuloConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (SigeModulo) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof SigeModulo) {
        	SigeModulo entity= (SigeModulo) value;
            if (entity != null && entity instanceof SigeModulo && entity.getIdmodulo() != null) {
                uiComponent.getAttributes().put( entity.getIdmodulo().toString(), entity);
                return entity.getIdmodulo().toString();
            }
        }
        return "";
    }
}