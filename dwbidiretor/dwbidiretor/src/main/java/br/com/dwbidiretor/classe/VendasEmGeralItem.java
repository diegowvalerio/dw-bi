package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class VendasEmGeralItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@Id
	private BigDecimal pedido;

	private BigDecimal codigoproduto;
	private String nomeproduto;
	private BigDecimal quantidadeproduto; 
	private BigDecimal valorunitarioproduto;
	private BigDecimal valortotalproduto;
	private String nota;
	private Date datanota;
	private String statusnota;
	
	private Blob imagem;
		

	public VendasEmGeralItem() {
		super();
	}

	

	public BigDecimal getPedido() {
		return pedido;
	}



	public void setPedido(BigDecimal pedido) {
		this.pedido = pedido;
	}



	public BigDecimal getCodigoproduto() {
		return codigoproduto;
	}

	public void setCodigoproduto(BigDecimal codigoproduto) {
		this.codigoproduto = codigoproduto;
	}

	public String getNomeproduto() {
		return nomeproduto;
	}

	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}

	public BigDecimal getQuantidadeproduto() {
		return quantidadeproduto;
	}

	public void setQuantidadeproduto(BigDecimal quantidadeproduto) {
		this.quantidadeproduto = quantidadeproduto;
	}

	public BigDecimal getValorunitarioproduto() {
		return valorunitarioproduto;
	}

	public void setValorunitarioproduto(BigDecimal valorunitarioproduto) {
		this.valorunitarioproduto = valorunitarioproduto;
	}

	public BigDecimal getValortotalproduto() {
		return valortotalproduto;
	}

	public void setValortotalproduto(BigDecimal valortotalproduto) {
		this.valortotalproduto = valortotalproduto;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Date getDatanota() {
		return datanota;
	}

	public void setDatanota(Date datanota) {
		this.datanota = datanota;
	}

	public String getStatusnota() {
		return statusnota;
	}

	public void setStatusnota(String statusnota) {
		this.statusnota = statusnota;
	}



	public Blob getImagem() {
		return imagem;
	}



	public void setImagem(Blob imagem) {
		this.imagem = imagem;
	}
	
	
}
