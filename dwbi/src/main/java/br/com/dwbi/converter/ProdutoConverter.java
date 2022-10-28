package br.com.dwbi.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.dwbi.classe.Produto;

@FacesConverter(forClass = Produto.class,value="conversorProduto")
public class ProdutoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Produto) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Produto) {
        	Produto entity= (Produto) value;
            if (entity != null && entity instanceof Produto && entity.getProdutoid() != null) {
                uiComponent.getAttributes().put( entity.getProdutoid().toString(), entity);
                return entity.getProdutoid().toString();
            }
        }
        return "";
    }
}