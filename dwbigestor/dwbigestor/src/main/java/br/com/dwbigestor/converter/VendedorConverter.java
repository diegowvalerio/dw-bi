package br.com.dwbigestor.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbigestor.classe.Vendedor;

@FacesConverter(forClass = Vendedor.class,value="conversorVendedor")
public class VendedorConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Vendedor) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Vendedor) {
        	Vendedor entity= (Vendedor) value;
            if (entity != null && entity instanceof Vendedor && entity.getCodigovendedor() != null) {
                uiComponent.getAttributes().put( entity.getCodigovendedor().toString(), entity);
                return entity.getCodigovendedor().toString();
            }
        }
        return "";
    }
}