package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;


public class MixProduto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigoproduto;
	private String nomeproduto;
	
	private BigDecimal qtde2018;
	private BigDecimal qtde2019;
	private BigDecimal qtde2020;
	private BigDecimal qtde2021;
	private BigDecimal qtde2022;
	private BigDecimal qtdetotal;
	private BigDecimal vl2018;
	private BigDecimal vl2019;
	private BigDecimal vl2020;
	private BigDecimal vl2021;
	private BigDecimal vl2022;
	private BigDecimal vltotal;
	
	private BigDecimal qtde2023;
	private BigDecimal vl2023;
		
	public MixProduto() {
		super();
	}

	public BigDecimal getQtde2023() {
		return qtde2023;
	}

	public void setQtde2023(BigDecimal qtde2023) {
		this.qtde2023 = qtde2023;
	}

	public BigDecimal getVl2023() {
		return vl2023;
	}

	public void setVl2023(BigDecimal vl2023) {
		this.vl2023 = vl2023;
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

	public BigDecimal getQtde2018() {
		return qtde2018;
	}

	public void setQtde2018(BigDecimal qtde2018) {
		this.qtde2018 = qtde2018;
	}

	public BigDecimal getQtde2019() {
		return qtde2019;
	}

	public void setQtde2019(BigDecimal qtde2019) {
		this.qtde2019 = qtde2019;
	}

	public BigDecimal getQtde2020() {
		return qtde2020;
	}

	public void setQtde2020(BigDecimal qtde2020) {
		this.qtde2020 = qtde2020;
	}

	public BigDecimal getQtde2021() {
		return qtde2021;
	}

	public void setQtde2021(BigDecimal qtde2021) {
		this.qtde2021 = qtde2021;
	}

	public BigDecimal getQtdetotal() {
		return qtdetotal;
	}

	public void setQtdetotal(BigDecimal qtdetotal) {
		this.qtdetotal = qtdetotal;
	}

	public BigDecimal getVl2018() {
		return vl2018;
	}

	public void setVl2018(BigDecimal vl2018) {
		this.vl2018 = vl2018;
	}

	public BigDecimal getVl2019() {
		return vl2019;
	}

	public void setVl2019(BigDecimal vl2019) {
		this.vl2019 = vl2019;
	}

	public BigDecimal getVl2020() {
		return vl2020;
	}

	public void setVl2020(BigDecimal vl2020) {
		this.vl2020 = vl2020;
	}

	public BigDecimal getVl2021() {
		return vl2021;
	}

	public void setVl2021(BigDecimal vl2021) {
		this.vl2021 = vl2021;
	}

	public BigDecimal getVltotal() {
		return vltotal;
	}

	public void setVltotal(BigDecimal vltotal) {
		this.vltotal = vltotal;
	}

	public BigDecimal getQtde2022() {
		return qtde2022;
	}

	public void setQtde2022(BigDecimal qtde2022) {
		this.qtde2022 = qtde2022;
	}

	public BigDecimal getVl2022() {
		return vl2022;
	}

	public void setVl2022(BigDecimal vl2022) {
		this.vl2022 = vl2022;
	}

}
