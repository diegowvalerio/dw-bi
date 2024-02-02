package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class PT_Mix implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String regiao;
	private BigDecimal vendedor;
	private String nomevendedor;
	@Id
	private BigDecimal cliente;
	private String nomecliente;
	
	private String mes;
	private BigDecimal mixmedio; 
	private BigInteger mixmes;
	private Integer pontos;
	private String mes2;
	private BigDecimal mixmedio2; 
	private BigInteger mixmes2;
	private Integer pontos2;
	private String mes3;
	private BigDecimal mixmedio3; 
	private BigInteger mixmes3;
	private Integer pontos3;
	
	private String mes4;
	private BigDecimal mixmedio4; 
	private BigInteger mixmes4;
	private Integer pontos4;
	private String mes5;
	private BigDecimal mixmedio5; 
	private BigInteger mixmes5;
	private Integer pontos5;
	private String mes6;
	private BigDecimal mixmedio6; 
	private BigInteger mixmes6;
	private Integer pontos6;
	
	private String mes7;
	private BigDecimal mixmedio7; 
	private BigInteger mixmes7;
	private Integer pontos7;
	private String mes8;
	private BigDecimal mixmedio8; 
	private BigInteger mixmes8;
	private Integer pontos8;
	private String mes9;
	private BigDecimal mixmedio9; 
	private BigInteger mixmes9;
	private Integer pontos9;
	
	private String mes10;
	private BigDecimal mixmedio10; 
	private BigInteger mixmes10;
	private Integer pontos10;
	private String mes11;
	private BigDecimal mixmedio11; 
	private BigInteger mixmes11;
	private Integer pontos11;
	private String mes12;
	private BigDecimal mixmedio12; 
	private BigInteger mixmes12;
	private Integer pontos12;
	
	private Integer totaltri1;
	private Integer totaltri2;
	private Integer totaltri3;
	private Integer totaltri4;
	private Integer totalanual;
	
	public PT_Mix() {
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

	public BigDecimal getCliente() {
		return cliente;
	}

	public void setCliente(BigDecimal cliente) {
		this.cliente = cliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public BigDecimal getMixmedio() {
		return mixmedio;
	}

	public void setMixmedio(BigDecimal mixmedio) {
		this.mixmedio = mixmedio;
	}

	public BigInteger getMixmes() {
		return mixmes;
	}

	public void setMixmes(BigInteger mixmes) {
		this.mixmes = mixmes;
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

	public BigDecimal getMixmedio2() {
		return mixmedio2;
	}

	public void setMixmedio2(BigDecimal mixmedio2) {
		this.mixmedio2 = mixmedio2;
	}

	public BigInteger getMixmes2() {
		return mixmes2;
	}

	public void setMixmes2(BigInteger mixmes2) {
		this.mixmes2 = mixmes2;
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

	public BigDecimal getMixmedio3() {
		return mixmedio3;
	}

	public void setMixmedio3(BigDecimal mixmedio3) {
		this.mixmedio3 = mixmedio3;
	}

	public BigInteger getMixmes3() {
		return mixmes3;
	}

	public void setMixmes3(BigInteger mixmes3) {
		this.mixmes3 = mixmes3;
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

	public BigDecimal getMixmedio4() {
		return mixmedio4;
	}

	public void setMixmedio4(BigDecimal mixmedio4) {
		this.mixmedio4 = mixmedio4;
	}

	public BigInteger getMixmes4() {
		return mixmes4;
	}

	public void setMixmes4(BigInteger mixmes4) {
		this.mixmes4 = mixmes4;
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

	public BigDecimal getMixmedio5() {
		return mixmedio5;
	}

	public void setMixmedio5(BigDecimal mixmedio5) {
		this.mixmedio5 = mixmedio5;
	}

	public BigInteger getMixmes5() {
		return mixmes5;
	}

	public void setMixmes5(BigInteger mixmes5) {
		this.mixmes5 = mixmes5;
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

	public BigDecimal getMixmedio6() {
		return mixmedio6;
	}

	public void setMixmedio6(BigDecimal mixmedio6) {
		this.mixmedio6 = mixmedio6;
	}

	public BigInteger getMixmes6() {
		return mixmes6;
	}

	public void setMixmes6(BigInteger mixmes6) {
		this.mixmes6 = mixmes6;
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

	public BigDecimal getMixmedio7() {
		return mixmedio7;
	}

	public void setMixmedio7(BigDecimal mixmedio7) {
		this.mixmedio7 = mixmedio7;
	}

	public BigInteger getMixmes7() {
		return mixmes7;
	}

	public void setMixmes7(BigInteger mixmes7) {
		this.mixmes7 = mixmes7;
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

	public BigDecimal getMixmedio8() {
		return mixmedio8;
	}

	public void setMixmedio8(BigDecimal mixmedio8) {
		this.mixmedio8 = mixmedio8;
	}

	public BigInteger getMixmes8() {
		return mixmes8;
	}

	public void setMixmes8(BigInteger mixmes8) {
		this.mixmes8 = mixmes8;
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

	public BigDecimal getMixmedio9() {
		return mixmedio9;
	}

	public void setMixmedio9(BigDecimal mixmedio9) {
		this.mixmedio9 = mixmedio9;
	}

	public BigInteger getMixmes9() {
		return mixmes9;
	}

	public void setMixmes9(BigInteger mixmes9) {
		this.mixmes9 = mixmes9;
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

	public BigDecimal getMixmedio10() {
		return mixmedio10;
	}

	public void setMixmedio10(BigDecimal mixmedio10) {
		this.mixmedio10 = mixmedio10;
	}

	public BigInteger getMixmes10() {
		return mixmes10;
	}

	public void setMixmes10(BigInteger mixmes10) {
		this.mixmes10 = mixmes10;
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

	public BigDecimal getMixmedio11() {
		return mixmedio11;
	}

	public void setMixmedio11(BigDecimal mixmedio11) {
		this.mixmedio11 = mixmedio11;
	}

	public BigInteger getMixmes11() {
		return mixmes11;
	}

	public void setMixmes11(BigInteger mixmes11) {
		this.mixmes11 = mixmes11;
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

	public BigDecimal getMixmedio12() {
		return mixmedio12;
	}

	public void setMixmedio12(BigDecimal mixmedio12) {
		this.mixmedio12 = mixmedio12;
	}

	public BigInteger getMixmes12() {
		return mixmes12;
	}

	public void setMixmes12(BigInteger mixmes12) {
		this.mixmes12 = mixmes12;
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
