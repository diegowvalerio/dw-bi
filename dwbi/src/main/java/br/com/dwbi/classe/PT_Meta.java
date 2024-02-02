package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class PT_Meta implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String regiao;
	@Id
	private BigDecimal vendedor;
	private String nomevendedor;
	
	private String mes;
	private BigDecimal meta; 
	private BigDecimal valor;
	private BigDecimal atingido;
	private Integer pontos;
	private String mes2;
	private BigDecimal meta2; 
	private BigDecimal valor2;
	private BigDecimal atingido2;
	private Integer pontos2;
	private String mes3;
	private BigDecimal meta3; 
	private BigDecimal valor3;
	private BigDecimal atingido3;
	private Integer pontos3;
	
	private String mes4;
	private BigDecimal meta4; 
	private BigDecimal valor4;
	private BigDecimal atingido4;
	private Integer pontos4;
	private String mes5;
	private BigDecimal meta5; 
	private BigDecimal valor5;
	private BigDecimal atingido5;
	private Integer pontos5;
	private String mes6;
	private BigDecimal meta6; 
	private BigDecimal valor6;
	private BigDecimal atingido6;
	private Integer pontos6;
	
	private String mes7;
	private BigDecimal meta7; 
	private BigDecimal valor7;
	private BigDecimal atingido7;
	private Integer pontos7;
	private String mes8;
	private BigDecimal meta8; 
	private BigDecimal valor8;
	private BigDecimal atingido8;
	private Integer pontos8;
	private String mes9;
	private BigDecimal meta9; 
	private BigDecimal valor9;
	private BigDecimal atingido9;
	private Integer pontos9;
	
	private String mes10;
	private BigDecimal meta10; 
	private BigDecimal valor10;
	private BigDecimal atingido10;
	private Integer pontos10;
	private String mes11;
	private BigDecimal meta11; 
	private BigDecimal valor11;
	private BigDecimal atingido11;
	private Integer pontos11;
	private String mes12;
	private BigDecimal meta12; 
	private BigDecimal valor12;
	private BigDecimal atingido12;
	private Integer pontos12;
	
	private Integer totaltri1;
	private Integer totaltri2;
	private Integer totaltri3;
	private Integer totaltri4;
	private Integer totalanual;
	
	public PT_Meta() {
		super();
	}


	public String getRegiao() {
		return regiao;
	}


	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}


	public BigDecimal getVendedor() {
		return vendedor;
	}


	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}


	public String getNomevendedor() {
		return nomevendedor;
	}


	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public BigDecimal getMeta() {
		return meta;
	}


	public void setMeta(BigDecimal meta) {
		this.meta = meta;
	}


	public BigDecimal getValor() {
		return valor;
	}


	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}


	public BigDecimal getAtingido() {
		return atingido;
	}


	public void setAtingido(BigDecimal atingido) {
		this.atingido = atingido;
	}


	public Integer getPontos() {
		return pontos;
	}


	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}


	public String getMes2() {
		return mes2;
	}


	public void setMes2(String mes2) {
		this.mes2 = mes2;
	}


	public BigDecimal getMeta2() {
		return meta2;
	}


	public void setMeta2(BigDecimal meta2) {
		this.meta2 = meta2;
	}


	public BigDecimal getValor2() {
		return valor2;
	}


	public void setValor2(BigDecimal valor2) {
		this.valor2 = valor2;
	}


	public BigDecimal getAtingido2() {
		return atingido2;
	}


	public void setAtingido2(BigDecimal atingido2) {
		this.atingido2 = atingido2;
	}


	public Integer getPontos2() {
		return pontos2;
	}


	public void setPontos2(Integer pontos2) {
		this.pontos2 = pontos2;
	}


	public String getMes3() {
		return mes3;
	}


	public void setMes3(String mes3) {
		this.mes3 = mes3;
	}


	public BigDecimal getMeta3() {
		return meta3;
	}


	public void setMeta3(BigDecimal meta3) {
		this.meta3 = meta3;
	}


	public BigDecimal getValor3() {
		return valor3;
	}


	public void setValor3(BigDecimal valor3) {
		this.valor3 = valor3;
	}


	public BigDecimal getAtingido3() {
		return atingido3;
	}


	public void setAtingido3(BigDecimal atingido3) {
		this.atingido3 = atingido3;
	}


	public Integer getPontos3() {
		return pontos3;
	}


	public void setPontos3(Integer pontos3) {
		this.pontos3 = pontos3;
	}


	public String getMes4() {
		return mes4;
	}


	public void setMes4(String mes4) {
		this.mes4 = mes4;
	}


	public BigDecimal getMeta4() {
		return meta4;
	}


	public void setMeta4(BigDecimal meta4) {
		this.meta4 = meta4;
	}


	public BigDecimal getValor4() {
		return valor4;
	}


	public void setValor4(BigDecimal valor4) {
		this.valor4 = valor4;
	}


	public BigDecimal getAtingido4() {
		return atingido4;
	}


	public void setAtingido4(BigDecimal atingido4) {
		this.atingido4 = atingido4;
	}


	public Integer getPontos4() {
		return pontos4;
	}


	public void setPontos4(Integer pontos4) {
		this.pontos4 = pontos4;
	}


	public String getMes5() {
		return mes5;
	}


	public void setMes5(String mes5) {
		this.mes5 = mes5;
	}


	public BigDecimal getMeta5() {
		return meta5;
	}


	public void setMeta5(BigDecimal meta5) {
		this.meta5 = meta5;
	}


	public BigDecimal getValor5() {
		return valor5;
	}


	public void setValor5(BigDecimal valor5) {
		this.valor5 = valor5;
	}


	public BigDecimal getAtingido5() {
		return atingido5;
	}


	public void setAtingido5(BigDecimal atingido5) {
		this.atingido5 = atingido5;
	}


	public Integer getPontos5() {
		return pontos5;
	}


	public void setPontos5(Integer pontos5) {
		this.pontos5 = pontos5;
	}


	public String getMes6() {
		return mes6;
	}


	public void setMes6(String mes6) {
		this.mes6 = mes6;
	}


	public BigDecimal getMeta6() {
		return meta6;
	}


	public void setMeta6(BigDecimal meta6) {
		this.meta6 = meta6;
	}


	public BigDecimal getValor6() {
		return valor6;
	}


	public void setValor6(BigDecimal valor6) {
		this.valor6 = valor6;
	}


	public BigDecimal getAtingido6() {
		return atingido6;
	}


	public void setAtingido6(BigDecimal atingido6) {
		this.atingido6 = atingido6;
	}


	public Integer getPontos6() {
		return pontos6;
	}


	public void setPontos6(Integer pontos6) {
		this.pontos6 = pontos6;
	}


	public String getMes7() {
		return mes7;
	}


	public void setMes7(String mes7) {
		this.mes7 = mes7;
	}


	public BigDecimal getMeta7() {
		return meta7;
	}


	public void setMeta7(BigDecimal meta7) {
		this.meta7 = meta7;
	}


	public BigDecimal getValor7() {
		return valor7;
	}


	public void setValor7(BigDecimal valor7) {
		this.valor7 = valor7;
	}


	public BigDecimal getAtingido7() {
		return atingido7;
	}


	public void setAtingido7(BigDecimal atingido7) {
		this.atingido7 = atingido7;
	}


	public Integer getPontos7() {
		return pontos7;
	}


	public void setPontos7(Integer pontos7) {
		this.pontos7 = pontos7;
	}


	public String getMes8() {
		return mes8;
	}


	public void setMes8(String mes8) {
		this.mes8 = mes8;
	}


	public BigDecimal getMeta8() {
		return meta8;
	}


	public void setMeta8(BigDecimal meta8) {
		this.meta8 = meta8;
	}


	public BigDecimal getValor8() {
		return valor8;
	}


	public void setValor8(BigDecimal valor8) {
		this.valor8 = valor8;
	}


	public BigDecimal getAtingido8() {
		return atingido8;
	}


	public void setAtingido8(BigDecimal atingido8) {
		this.atingido8 = atingido8;
	}


	public Integer getPontos8() {
		return pontos8;
	}


	public void setPontos8(Integer pontos8) {
		this.pontos8 = pontos8;
	}


	public String getMes9() {
		return mes9;
	}


	public void setMes9(String mes9) {
		this.mes9 = mes9;
	}


	public BigDecimal getMeta9() {
		return meta9;
	}


	public void setMeta9(BigDecimal meta9) {
		this.meta9 = meta9;
	}


	public BigDecimal getValor9() {
		return valor9;
	}


	public void setValor9(BigDecimal valor9) {
		this.valor9 = valor9;
	}


	public BigDecimal getAtingido9() {
		return atingido9;
	}


	public void setAtingido9(BigDecimal atingido9) {
		this.atingido9 = atingido9;
	}


	public Integer getPontos9() {
		return pontos9;
	}


	public void setPontos9(Integer pontos9) {
		this.pontos9 = pontos9;
	}


	public String getMes10() {
		return mes10;
	}


	public void setMes10(String mes10) {
		this.mes10 = mes10;
	}


	public BigDecimal getMeta10() {
		return meta10;
	}


	public void setMeta10(BigDecimal meta10) {
		this.meta10 = meta10;
	}


	public BigDecimal getValor10() {
		return valor10;
	}


	public void setValor10(BigDecimal valor10) {
		this.valor10 = valor10;
	}


	public BigDecimal getAtingido10() {
		return atingido10;
	}


	public void setAtingido10(BigDecimal atingido10) {
		this.atingido10 = atingido10;
	}


	public Integer getPontos10() {
		return pontos10;
	}


	public void setPontos10(Integer pontos10) {
		this.pontos10 = pontos10;
	}


	public String getMes11() {
		return mes11;
	}


	public void setMes11(String mes11) {
		this.mes11 = mes11;
	}


	public BigDecimal getMeta11() {
		return meta11;
	}


	public void setMeta11(BigDecimal meta11) {
		this.meta11 = meta11;
	}


	public BigDecimal getValor11() {
		return valor11;
	}


	public void setValor11(BigDecimal valor11) {
		this.valor11 = valor11;
	}


	public BigDecimal getAtingido11() {
		return atingido11;
	}


	public void setAtingido11(BigDecimal atingido11) {
		this.atingido11 = atingido11;
	}


	public Integer getPontos11() {
		return pontos11;
	}


	public void setPontos11(Integer pontos11) {
		this.pontos11 = pontos11;
	}


	public String getMes12() {
		return mes12;
	}


	public void setMes12(String mes12) {
		this.mes12 = mes12;
	}


	public BigDecimal getMeta12() {
		return meta12;
	}


	public void setMeta12(BigDecimal meta12) {
		this.meta12 = meta12;
	}


	public BigDecimal getValor12() {
		return valor12;
	}


	public void setValor12(BigDecimal valor12) {
		this.valor12 = valor12;
	}


	public BigDecimal getAtingido12() {
		return atingido12;
	}


	public void setAtingido12(BigDecimal atingido12) {
		this.atingido12 = atingido12;
	}


	public Integer getPontos12() {
		return pontos12;
	}


	public void setPontos12(Integer pontos12) {
		this.pontos12 = pontos12;
	}


	public Integer getTotaltri1() {
		return totaltri1;
	}


	public void setTotaltri1(Integer totaltri1) {
		this.totaltri1 = totaltri1;
	}


	public Integer getTotaltri2() {
		return totaltri2;
	}


	public void setTotaltri2(Integer totaltri2) {
		this.totaltri2 = totaltri2;
	}


	public Integer getTotaltri3() {
		return totaltri3;
	}


	public void setTotaltri3(Integer totaltri3) {
		this.totaltri3 = totaltri3;
	}


	public Integer getTotaltri4() {
		return totaltri4;
	}


	public void setTotaltri4(Integer totaltri4) {
		this.totaltri4 = totaltri4;
	}


	public Integer getTotalanual() {
		return totalanual;
	}


	public void setTotalanual(Integer totalanual) {
		this.totalanual = totalanual;
	}

	

}
