package br.com.dwbi.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.*;


public class HCliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigovendedor;
	private String nomevendedor;
	private BigDecimal codigocliente;
	private String nomecliente;
	private String endereco;
	private String numeroendereco;
	private String cidade;
	private String cep;
	private String uf;
	
	private String gestor;
	
	private BigDecimal janeiro2018;
	private BigDecimal fevereiro2018;
	private BigDecimal marco2018;
	private BigDecimal abril2018;
	private BigDecimal maio2018;
	private BigDecimal junho2018;
	private BigDecimal julho2018;
	private BigDecimal agosto2018;
	private BigDecimal setembro2018;
	private BigDecimal outrubo2018;
	private BigDecimal novembro2018;
	private BigDecimal dezembro2018;
	private BigDecimal total2018;
	
	private BigDecimal janeiro2019;
	private BigDecimal fevereiro2019;
	private BigDecimal marco2019;
	private BigDecimal abril2019;
	private BigDecimal maio2019;
	private BigDecimal junho2019;
	private BigDecimal julho2019;
	private BigDecimal agosto2019;
	private BigDecimal setembro2019;
	private BigDecimal outrubo2019;
	private BigDecimal novembro2019;
	private BigDecimal dezembro2019;
	private BigDecimal total2019;
	
	private BigDecimal janeiro2020;
	private BigDecimal fevereiro2020;
	private BigDecimal marco2020;
	private BigDecimal abril2020;
	private BigDecimal maio2020;
	private BigDecimal junho2020;
	private BigDecimal julho2020;
	private BigDecimal agosto2020;
	private BigDecimal setembro2020;
	private BigDecimal outrubo2020;
	private BigDecimal novembro2020;
	private BigDecimal dezembro2020;
	private BigDecimal total2020;
	
	private BigDecimal janeiro2021;
	private BigDecimal fevereiro2021;
	private BigDecimal marco2021;
	private BigDecimal abril2021;
	private BigDecimal maio2021;
	private BigDecimal junho2021;
	private BigDecimal julho2021;
	private BigDecimal agosto2021;
	private BigDecimal setembro2021;
	private BigDecimal outrubo2021;
	private BigDecimal novembro2021;
	private BigDecimal dezembro2021;
	private BigDecimal total2021;
	
	private BigDecimal janeiro2022;
	private BigDecimal fevereiro2022;
	private BigDecimal marco2022;
	private BigDecimal abril2022;
	private BigDecimal maio2022;
	private BigDecimal junho2022;
	private BigDecimal julho2022;
	private BigDecimal agosto2022;
	private BigDecimal setembro2022;
	private BigDecimal outrubo2022;
	private BigDecimal novembro2022;
	private BigDecimal dezembro2022;
	private BigDecimal total2022;
	
	private BigDecimal totalgeral;
	private BigInteger mixqtde;
	private BigDecimal mixqtdemedio;
	private BigDecimal ticketmedio;
	private BigInteger nvendas;
	private Date primeiravenda;
	private Date ultimavenda;
	private Double frequenciamedia;
	private String cnpjcpf;
	private String bairro;
	private String status;
	
	private BigDecimal janeiro2023;
	private BigDecimal fevereiro2023;
	private BigDecimal marco2023;
	private BigDecimal abril2023;
	private BigDecimal maio2023;
	private BigDecimal junho2023;
	private BigDecimal julho2023;
	private BigDecimal agosto2023;
	private BigDecimal setembro2023;
	private BigDecimal outrubo2023;
	private BigDecimal novembro2023;
	private BigDecimal dezembro2023;
	private BigDecimal total2023;
	
	private BigDecimal janeiro2024;
	private BigDecimal fevereiro2024;
	private BigDecimal marco2024;
	private BigDecimal abril2024;
	private BigDecimal maio2024;
	private BigDecimal junho2024;
	private BigDecimal julho2024;
	private BigDecimal agosto2024;
	private BigDecimal setembro2024;
	private BigDecimal outrubo2024;
	private BigDecimal novembro2024;
	private BigDecimal dezembro2024;
	private BigDecimal total2024;
	
	public HCliente() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCnpjcpf() {
		return cnpjcpf;
	}

	public void setCnpjcpf(String cnpjcpf) {
		this.cnpjcpf = cnpjcpf;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public BigInteger getNvendas() {
		return nvendas;
	}

	public void setNvendas(BigInteger nvendas) {
		this.nvendas = nvendas;
	}

	public Date getPrimeiravenda() {
		return primeiravenda;
	}

	public void setPrimeiravenda(Date primeiravenda) {
		this.primeiravenda = primeiravenda;
	}

	public Date getUltimavenda() {
		return ultimavenda;
	}

	public void setUltimavenda(Date ultimavenda) {
		this.ultimavenda = ultimavenda;
	}

	public Double getFrequenciamedia() {
		return frequenciamedia;
	}

	public void setFrequenciamedia(Double frequenciamedia) {
		this.frequenciamedia = frequenciamedia;
	}

	public BigDecimal getTicketmedio() {
		return ticketmedio;
	}

	public void setTicketmedio(BigDecimal ticketmedio) {
		this.ticketmedio = ticketmedio;
	}

	public BigDecimal getMixqtdemedio() {
		return mixqtdemedio;
	}

	public void setMixqtdemedio(BigDecimal mixqtdemedio) {
		this.mixqtdemedio = mixqtdemedio;
	}

	public BigDecimal getTotalgeral() {
		return totalgeral;
	}

	public void setTotalgeral(BigDecimal totalgeral) {
		this.totalgeral = totalgeral;
	}

	public BigInteger getMixqtde() {
		return mixqtde;
	}

	public void setMixqtde(BigInteger mixqtde) {
		this.mixqtde = mixqtde;
	}

	public BigDecimal getCodigovendedor() {
		return codigovendedor;
	}

	public void setCodigovendedor(BigDecimal codigovendedor) {
		this.codigovendedor = codigovendedor;
	}

	public String getNomevendedor() {
		return nomevendedor;
	}

	public void setNomevendedor(String nomevendedor) {
		this.nomevendedor = nomevendedor;
	}

	public BigDecimal getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(BigDecimal codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumeroendereco() {
		return numeroendereco;
	}

	public void setNumeroendereco(String numeroendereco) {
		this.numeroendereco = numeroendereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}

	public BigDecimal getJaneiro2018() {
		return janeiro2018;
	}

	public void setJaneiro2018(BigDecimal janeiro2018) {
		this.janeiro2018 = janeiro2018;
	}

	public BigDecimal getFevereiro2018() {
		return fevereiro2018;
	}

	public void setFevereiro2018(BigDecimal fevereiro2018) {
		this.fevereiro2018 = fevereiro2018;
	}

	public BigDecimal getMarco2018() {
		return marco2018;
	}

	public void setMarco2018(BigDecimal marco2018) {
		this.marco2018 = marco2018;
	}

	public BigDecimal getAbril2018() {
		return abril2018;
	}

	public void setAbril2018(BigDecimal abril2018) {
		this.abril2018 = abril2018;
	}

	public BigDecimal getMaio2018() {
		return maio2018;
	}

	public void setMaio2018(BigDecimal maio2018) {
		this.maio2018 = maio2018;
	}

	public BigDecimal getJunho2018() {
		return junho2018;
	}

	public void setJunho2018(BigDecimal junho2018) {
		this.junho2018 = junho2018;
	}

	public BigDecimal getJulho2018() {
		return julho2018;
	}

	public void setJulho2018(BigDecimal julho2018) {
		this.julho2018 = julho2018;
	}

	public BigDecimal getAgosto2018() {
		return agosto2018;
	}

	public void setAgosto2018(BigDecimal agosto2018) {
		this.agosto2018 = agosto2018;
	}

	public BigDecimal getSetembro2018() {
		return setembro2018;
	}

	public void setSetembro2018(BigDecimal setembro2018) {
		this.setembro2018 = setembro2018;
	}

	public BigDecimal getOutrubo2018() {
		return outrubo2018;
	}

	public void setOutrubo2018(BigDecimal outrubo2018) {
		this.outrubo2018 = outrubo2018;
	}

	public BigDecimal getNovembro2018() {
		return novembro2018;
	}

	public void setNovembro2018(BigDecimal novembro2018) {
		this.novembro2018 = novembro2018;
	}

	public BigDecimal getDezembro2018() {
		return dezembro2018;
	}

	public void setDezembro2018(BigDecimal dezembro2018) {
		this.dezembro2018 = dezembro2018;
	}

	public BigDecimal getTotal2018() {
		return total2018;
	}

	public void setTotal2018(BigDecimal total2018) {
		this.total2018 = total2018;
	}

	public BigDecimal getJaneiro2019() {
		return janeiro2019;
	}

	public void setJaneiro2019(BigDecimal janeiro2019) {
		this.janeiro2019 = janeiro2019;
	}

	public BigDecimal getFevereiro2019() {
		return fevereiro2019;
	}

	public void setFevereiro2019(BigDecimal fevereiro2019) {
		this.fevereiro2019 = fevereiro2019;
	}

	public BigDecimal getMarco2019() {
		return marco2019;
	}

	public void setMarco2019(BigDecimal marco2019) {
		this.marco2019 = marco2019;
	}

	public BigDecimal getAbril2019() {
		return abril2019;
	}

	public void setAbril2019(BigDecimal abril2019) {
		this.abril2019 = abril2019;
	}

	public BigDecimal getMaio2019() {
		return maio2019;
	}

	public void setMaio2019(BigDecimal maio2019) {
		this.maio2019 = maio2019;
	}

	public BigDecimal getJunho2019() {
		return junho2019;
	}

	public void setJunho2019(BigDecimal junho2019) {
		this.junho2019 = junho2019;
	}

	public BigDecimal getJulho2019() {
		return julho2019;
	}

	public void setJulho2019(BigDecimal julho2019) {
		this.julho2019 = julho2019;
	}

	public BigDecimal getAgosto2019() {
		return agosto2019;
	}

	public void setAgosto2019(BigDecimal agosto2019) {
		this.agosto2019 = agosto2019;
	}

	public BigDecimal getSetembro2019() {
		return setembro2019;
	}

	public void setSetembro2019(BigDecimal setembro2019) {
		this.setembro2019 = setembro2019;
	}

	public BigDecimal getOutrubo2019() {
		return outrubo2019;
	}

	public void setOutrubo2019(BigDecimal outrubo2019) {
		this.outrubo2019 = outrubo2019;
	}

	public BigDecimal getNovembro2019() {
		return novembro2019;
	}

	public void setNovembro2019(BigDecimal novembro2019) {
		this.novembro2019 = novembro2019;
	}

	public BigDecimal getDezembro2019() {
		return dezembro2019;
	}

	public void setDezembro2019(BigDecimal dezembro2019) {
		this.dezembro2019 = dezembro2019;
	}

	public BigDecimal getTotal2019() {
		return total2019;
	}

	public void setTotal2019(BigDecimal total2019) {
		this.total2019 = total2019;
	}

	public BigDecimal getJaneiro2020() {
		return janeiro2020;
	}

	public void setJaneiro2020(BigDecimal janeiro2020) {
		this.janeiro2020 = janeiro2020;
	}

	public BigDecimal getFevereiro2020() {
		return fevereiro2020;
	}

	public void setFevereiro2020(BigDecimal fevereiro2020) {
		this.fevereiro2020 = fevereiro2020;
	}

	public BigDecimal getMarco2020() {
		return marco2020;
	}

	public void setMarco2020(BigDecimal marco2020) {
		this.marco2020 = marco2020;
	}

	public BigDecimal getAbril2020() {
		return abril2020;
	}

	public void setAbril2020(BigDecimal abril2020) {
		this.abril2020 = abril2020;
	}

	public BigDecimal getMaio2020() {
		return maio2020;
	}

	public void setMaio2020(BigDecimal maio2020) {
		this.maio2020 = maio2020;
	}

	public BigDecimal getJunho2020() {
		return junho2020;
	}

	public void setJunho2020(BigDecimal junho2020) {
		this.junho2020 = junho2020;
	}

	public BigDecimal getJulho2020() {
		return julho2020;
	}

	public void setJulho2020(BigDecimal julho2020) {
		this.julho2020 = julho2020;
	}

	public BigDecimal getAgosto2020() {
		return agosto2020;
	}

	public void setAgosto2020(BigDecimal agosto2020) {
		this.agosto2020 = agosto2020;
	}

	public BigDecimal getSetembro2020() {
		return setembro2020;
	}

	public void setSetembro2020(BigDecimal setembro2020) {
		this.setembro2020 = setembro2020;
	}

	public BigDecimal getOutrubo2020() {
		return outrubo2020;
	}

	public void setOutrubo2020(BigDecimal outrubo2020) {
		this.outrubo2020 = outrubo2020;
	}

	public BigDecimal getNovembro2020() {
		return novembro2020;
	}

	public void setNovembro2020(BigDecimal novembro2020) {
		this.novembro2020 = novembro2020;
	}

	public BigDecimal getDezembro2020() {
		return dezembro2020;
	}

	public void setDezembro2020(BigDecimal dezembro2020) {
		this.dezembro2020 = dezembro2020;
	}

	public BigDecimal getTotal2020() {
		return total2020;
	}

	public void setTotal2020(BigDecimal total2020) {
		this.total2020 = total2020;
	}

	public BigDecimal getJaneiro2021() {
		return janeiro2021;
	}

	public void setJaneiro2021(BigDecimal janeiro2021) {
		this.janeiro2021 = janeiro2021;
	}

	public BigDecimal getFevereiro2021() {
		return fevereiro2021;
	}

	public void setFevereiro2021(BigDecimal fevereiro2021) {
		this.fevereiro2021 = fevereiro2021;
	}

	public BigDecimal getMarco2021() {
		return marco2021;
	}

	public void setMarco2021(BigDecimal marco2021) {
		this.marco2021 = marco2021;
	}

	public BigDecimal getAbril2021() {
		return abril2021;
	}

	public void setAbril2021(BigDecimal abril2021) {
		this.abril2021 = abril2021;
	}

	public BigDecimal getMaio2021() {
		return maio2021;
	}

	public void setMaio2021(BigDecimal maio2021) {
		this.maio2021 = maio2021;
	}

	public BigDecimal getJunho2021() {
		return junho2021;
	}

	public void setJunho2021(BigDecimal junho2021) {
		this.junho2021 = junho2021;
	}

	public BigDecimal getJulho2021() {
		return julho2021;
	}

	public void setJulho2021(BigDecimal julho2021) {
		this.julho2021 = julho2021;
	}

	public BigDecimal getAgosto2021() {
		return agosto2021;
	}

	public void setAgosto2021(BigDecimal agosto2021) {
		this.agosto2021 = agosto2021;
	}

	public BigDecimal getSetembro2021() {
		return setembro2021;
	}

	public void setSetembro2021(BigDecimal setembro2021) {
		this.setembro2021 = setembro2021;
	}

	public BigDecimal getOutrubo2021() {
		return outrubo2021;
	}

	public void setOutrubo2021(BigDecimal outrubo2021) {
		this.outrubo2021 = outrubo2021;
	}

	public BigDecimal getNovembro2021() {
		return novembro2021;
	}

	public void setNovembro2021(BigDecimal novembro2021) {
		this.novembro2021 = novembro2021;
	}

	public BigDecimal getDezembro2021() {
		return dezembro2021;
	}

	public void setDezembro2021(BigDecimal dezembro2021) {
		this.dezembro2021 = dezembro2021;
	}

	public BigDecimal getTotal2021() {
		return total2021;
	}

	public void setTotal2021(BigDecimal total2021) {
		this.total2021 = total2021;
	}

	public BigDecimal getJaneiro2022() {
		return janeiro2022;
	}

	public void setJaneiro2022(BigDecimal janeiro2022) {
		this.janeiro2022 = janeiro2022;
	}

	public BigDecimal getFevereiro2022() {
		return fevereiro2022;
	}

	public void setFevereiro2022(BigDecimal fevereiro2022) {
		this.fevereiro2022 = fevereiro2022;
	}

	public BigDecimal getMarco2022() {
		return marco2022;
	}

	public void setMarco2022(BigDecimal marco2022) {
		this.marco2022 = marco2022;
	}

	public BigDecimal getAbril2022() {
		return abril2022;
	}

	public void setAbril2022(BigDecimal abril2022) {
		this.abril2022 = abril2022;
	}

	public BigDecimal getMaio2022() {
		return maio2022;
	}

	public void setMaio2022(BigDecimal maio2022) {
		this.maio2022 = maio2022;
	}

	public BigDecimal getJunho2022() {
		return junho2022;
	}

	public void setJunho2022(BigDecimal junho2022) {
		this.junho2022 = junho2022;
	}

	public BigDecimal getJulho2022() {
		return julho2022;
	}

	public void setJulho2022(BigDecimal julho2022) {
		this.julho2022 = julho2022;
	}

	public BigDecimal getAgosto2022() {
		return agosto2022;
	}

	public void setAgosto2022(BigDecimal agosto2022) {
		this.agosto2022 = agosto2022;
	}

	public BigDecimal getSetembro2022() {
		return setembro2022;
	}

	public void setSetembro2022(BigDecimal setembro2022) {
		this.setembro2022 = setembro2022;
	}

	public BigDecimal getOutrubo2022() {
		return outrubo2022;
	}

	public void setOutrubo2022(BigDecimal outrubo2022) {
		this.outrubo2022 = outrubo2022;
	}

	public BigDecimal getNovembro2022() {
		return novembro2022;
	}

	public void setNovembro2022(BigDecimal novembro2022) {
		this.novembro2022 = novembro2022;
	}

	public BigDecimal getDezembro2022() {
		return dezembro2022;
	}

	public void setDezembro2022(BigDecimal dezembro2022) {
		this.dezembro2022 = dezembro2022;
	}

	public BigDecimal getTotal2022() {
		return total2022;
	}

	public void setTotal2022(BigDecimal total2022) {
		this.total2022 = total2022;
	}

	public BigDecimal getJaneiro2023() {
		return janeiro2023;
	}

	public void setJaneiro2023(BigDecimal janeiro2023) {
		this.janeiro2023 = janeiro2023;
	}

	public BigDecimal getFevereiro2023() {
		return fevereiro2023;
	}

	public void setFevereiro2023(BigDecimal fevereiro2023) {
		this.fevereiro2023 = fevereiro2023;
	}

	public BigDecimal getMarco2023() {
		return marco2023;
	}

	public void setMarco2023(BigDecimal marco2023) {
		this.marco2023 = marco2023;
	}

	public BigDecimal getAbril2023() {
		return abril2023;
	}

	public void setAbril2023(BigDecimal abril2023) {
		this.abril2023 = abril2023;
	}

	public BigDecimal getMaio2023() {
		return maio2023;
	}

	public void setMaio2023(BigDecimal maio2023) {
		this.maio2023 = maio2023;
	}

	public BigDecimal getJunho2023() {
		return junho2023;
	}

	public void setJunho2023(BigDecimal junho2023) {
		this.junho2023 = junho2023;
	}

	public BigDecimal getJulho2023() {
		return julho2023;
	}

	public void setJulho2023(BigDecimal julho2023) {
		this.julho2023 = julho2023;
	}

	public BigDecimal getAgosto2023() {
		return agosto2023;
	}

	public void setAgosto2023(BigDecimal agosto2023) {
		this.agosto2023 = agosto2023;
	}

	public BigDecimal getSetembro2023() {
		return setembro2023;
	}

	public void setSetembro2023(BigDecimal setembro2023) {
		this.setembro2023 = setembro2023;
	}

	public BigDecimal getOutrubo2023() {
		return outrubo2023;
	}

	public void setOutrubo2023(BigDecimal outrubo2023) {
		this.outrubo2023 = outrubo2023;
	}

	public BigDecimal getNovembro2023() {
		return novembro2023;
	}

	public void setNovembro2023(BigDecimal novembro2023) {
		this.novembro2023 = novembro2023;
	}

	public BigDecimal getDezembro2023() {
		return dezembro2023;
	}

	public void setDezembro2023(BigDecimal dezembro2023) {
		this.dezembro2023 = dezembro2023;
	}

	public BigDecimal getTotal2023() {
		return total2023;
	}

	public void setTotal2023(BigDecimal total2023) {
		this.total2023 = total2023;
	}

	public BigDecimal getJaneiro2024() {
		return janeiro2024;
	}

	public void setJaneiro2024(BigDecimal janeiro2024) {
		this.janeiro2024 = janeiro2024;
	}

	public BigDecimal getFevereiro2024() {
		return fevereiro2024;
	}

	public void setFevereiro2024(BigDecimal fevereiro2024) {
		this.fevereiro2024 = fevereiro2024;
	}

	public BigDecimal getMarco2024() {
		return marco2024;
	}

	public void setMarco2024(BigDecimal marco2024) {
		this.marco2024 = marco2024;
	}

	public BigDecimal getAbril2024() {
		return abril2024;
	}

	public void setAbril2024(BigDecimal abril2024) {
		this.abril2024 = abril2024;
	}

	public BigDecimal getMaio2024() {
		return maio2024;
	}

	public void setMaio2024(BigDecimal maio2024) {
		this.maio2024 = maio2024;
	}

	public BigDecimal getJunho2024() {
		return junho2024;
	}

	public void setJunho2024(BigDecimal junho2024) {
		this.junho2024 = junho2024;
	}

	public BigDecimal getJulho2024() {
		return julho2024;
	}

	public void setJulho2024(BigDecimal julho2024) {
		this.julho2024 = julho2024;
	}

	public BigDecimal getAgosto2024() {
		return agosto2024;
	}

	public void setAgosto2024(BigDecimal agosto2024) {
		this.agosto2024 = agosto2024;
	}

	public BigDecimal getSetembro2024() {
		return setembro2024;
	}

	public void setSetembro2024(BigDecimal setembro2024) {
		this.setembro2024 = setembro2024;
	}

	public BigDecimal getOutrubo2024() {
		return outrubo2024;
	}

	public void setOutrubo2024(BigDecimal outrubo2024) {
		this.outrubo2024 = outrubo2024;
	}

	public BigDecimal getNovembro2024() {
		return novembro2024;
	}

	public void setNovembro2024(BigDecimal novembro2024) {
		this.novembro2024 = novembro2024;
	}

	public BigDecimal getDezembro2024() {
		return dezembro2024;
	}

	public void setDezembro2024(BigDecimal dezembro2024) {
		this.dezembro2024 = dezembro2024;
	}

	public BigDecimal getTotal2024() {
		return total2024;
	}

	public void setTotal2024(BigDecimal total2024) {
		this.total2024 = total2024;
	}

	
	
}
