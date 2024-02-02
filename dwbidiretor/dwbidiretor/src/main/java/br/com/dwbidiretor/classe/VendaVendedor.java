package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import javax.persistence.*;

public class VendaVendedor implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal vendedor;
	private String nome;
	private String ano;
	private String mes;
	private BigDecimal dia1;
	private BigDecimal dia2;
	private BigDecimal dia3;
	private BigDecimal dia4;
	private BigDecimal dia5;
	private BigDecimal dia6;
	private BigDecimal dia7;
	private BigDecimal dia8;
	private BigDecimal dia9;
	private BigDecimal dia10;
	private BigDecimal dia11;
	private BigDecimal dia12;
	private BigDecimal dia13;
	private BigDecimal dia14;
	private BigDecimal dia15;
	private BigDecimal dia16;
	private BigDecimal dia17;
	private BigDecimal dia18;
	private BigDecimal dia19;
	private BigDecimal dia20;
	private BigDecimal dia21;
	private BigDecimal dia22;
	private BigDecimal dia23;
	private BigDecimal dia24;
	private BigDecimal dia25;
	private BigDecimal dia26;
	private BigDecimal dia27;
	private BigDecimal dia28;
	private BigDecimal dia29;
	private BigDecimal dia30;
	private BigDecimal dia31;
	
	private BigDecimal total;
	
	
	public VendaVendedor() {
		super();
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getVendedor() {
		return vendedor;
	}

	public void setVendedor(BigDecimal vendedor) {
		this.vendedor = vendedor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getAno() {
		return ano;
	}


	public void setAno(String ano) {
		this.ano = ano;
	}


	public String getMes() {
		return mes;
	}


	public void setMes(String mes) {
		this.mes = mes;
	}


	public BigDecimal getDia1() {
		return dia1;
	}


	public void setDia1(BigDecimal dia1) {
		this.dia1 = dia1;
	}


	public BigDecimal getDia2() {
		return dia2;
	}


	public void setDia2(BigDecimal dia2) {
		this.dia2 = dia2;
	}


	public BigDecimal getDia3() {
		return dia3;
	}


	public void setDia3(BigDecimal dia3) {
		this.dia3 = dia3;
	}


	public BigDecimal getDia4() {
		return dia4;
	}


	public void setDia4(BigDecimal dia4) {
		this.dia4 = dia4;
	}


	public BigDecimal getDia5() {
		return dia5;
	}


	public void setDia5(BigDecimal dia5) {
		this.dia5 = dia5;
	}


	public BigDecimal getDia6() {
		return dia6;
	}


	public void setDia6(BigDecimal dia6) {
		this.dia6 = dia6;
	}


	public BigDecimal getDia7() {
		return dia7;
	}


	public void setDia7(BigDecimal dia7) {
		this.dia7 = dia7;
	}


	public BigDecimal getDia8() {
		return dia8;
	}


	public void setDia8(BigDecimal dia8) {
		this.dia8 = dia8;
	}


	public BigDecimal getDia9() {
		return dia9;
	}


	public void setDia9(BigDecimal dia9) {
		this.dia9 = dia9;
	}


	public BigDecimal getDia10() {
		return dia10;
	}


	public void setDia10(BigDecimal dia10) {
		this.dia10 = dia10;
	}


	public BigDecimal getDia11() {
		return dia11;
	}


	public void setDia11(BigDecimal dia11) {
		this.dia11 = dia11;
	}


	public BigDecimal getDia12() {
		return dia12;
	}


	public void setDia12(BigDecimal dia12) {
		this.dia12 = dia12;
	}


	public BigDecimal getDia13() {
		return dia13;
	}


	public void setDia13(BigDecimal dia13) {
		this.dia13 = dia13;
	}


	public BigDecimal getDia14() {
		return dia14;
	}


	public void setDia14(BigDecimal dia14) {
		this.dia14 = dia14;
	}


	public BigDecimal getDia15() {
		return dia15;
	}


	public void setDia15(BigDecimal dia15) {
		this.dia15 = dia15;
	}


	public BigDecimal getDia16() {
		return dia16;
	}


	public void setDia16(BigDecimal dia16) {
		this.dia16 = dia16;
	}


	public BigDecimal getDia17() {
		return dia17;
	}


	public void setDia17(BigDecimal dia17) {
		this.dia17 = dia17;
	}


	public BigDecimal getDia18() {
		return dia18;
	}


	public void setDia18(BigDecimal dia18) {
		this.dia18 = dia18;
	}


	public BigDecimal getDia19() {
		return dia19;
	}


	public void setDia19(BigDecimal dia19) {
		this.dia19 = dia19;
	}


	public BigDecimal getDia20() {
		return dia20;
	}


	public void setDia20(BigDecimal dia20) {
		this.dia20 = dia20;
	}


	public BigDecimal getDia21() {
		return dia21;
	}


	public void setDia21(BigDecimal dia21) {
		this.dia21 = dia21;
	}


	public BigDecimal getDia22() {
		return dia22;
	}


	public void setDia22(BigDecimal dia22) {
		this.dia22 = dia22;
	}


	public BigDecimal getDia23() {
		return dia23;
	}


	public void setDia23(BigDecimal dia23) {
		this.dia23 = dia23;
	}


	public BigDecimal getDia24() {
		return dia24;
	}


	public void setDia24(BigDecimal dia24) {
		this.dia24 = dia24;
	}


	public BigDecimal getDia25() {
		return dia25;
	}


	public void setDia25(BigDecimal dia25) {
		this.dia25 = dia25;
	}


	public BigDecimal getDia26() {
		return dia26;
	}


	public void setDia26(BigDecimal dia26) {
		this.dia26 = dia26;
	}


	public BigDecimal getDia27() {
		return dia27;
	}


	public void setDia27(BigDecimal dia27) {
		this.dia27 = dia27;
	}


	public BigDecimal getDia28() {
		return dia28;
	}


	public void setDia28(BigDecimal dia28) {
		this.dia28 = dia28;
	}


	public BigDecimal getDia29() {
		return dia29;
	}


	public void setDia29(BigDecimal dia29) {
		this.dia29 = dia29;
	}


	public BigDecimal getDia30() {
		return dia30;
	}


	public void setDia30(BigDecimal dia30) {
		this.dia30 = dia30;
	}


	public BigDecimal getDia31() {
		return dia31;
	}


	public void setDia31(BigDecimal dia31) {
		this.dia31 = dia31;
	}

}
