package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;


public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String produtoid;
	private String nomeproduto;
	private String referencia;
	private BigDecimal grupoid;
	private BigDecimal subgrupoid;
	
	private byte[] img;
	
	public byte[] getImg() {
		return img;
	}
	public void setImg(byte[] img) {
		this.img = img;
	}
	public String getProdutoid() {
		return produtoid;
	}
	public void setProdutoid(String produtoid) {
		this.produtoid = produtoid;
	}
	public String getNomeproduto() {
		return nomeproduto;
	}
	public void setNomeproduto(String nomeproduto) {
		this.nomeproduto = nomeproduto;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public BigDecimal getGrupoid() {
		return grupoid;
	}
	public void setGrupoid(BigDecimal grupoid) {
		this.grupoid = grupoid;
	}
	public BigDecimal getSubgrupoid() {
		return subgrupoid;
	}
	public void setSubgrupoid(BigDecimal subgrupoid) {
		this.subgrupoid = subgrupoid;
	}
	
	
	
}
