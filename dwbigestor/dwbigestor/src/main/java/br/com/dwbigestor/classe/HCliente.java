package br.com.dwbigestor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
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
	private BigDecimal mixqtde;
	private BigDecimal mixqtdemedio;
	private BigDecimal ticketmedio;
	private BigDecimal nvendas;
	private Date primeiravenda;
	private Date ultimavenda;
	private BigDecimal frequenciamedia;
	private String cnpjcpf;
	private String bairro;
	private String status;
	
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

	public BigDecimal getNvendas() {
		return nvendas;
	}

	public void setNvendas(BigDecimal nvendas) {
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

	public BigDecimal getFrequenciamedia() {
		return frequenciamedia;
	}

	public void setFrequenciamedia(BigDecimal frequenciamedia) {
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

	public BigDecimal getMixqtde() {
		return mixqtde;
	}

	public void setMixqtde(BigDecimal mixqtde) {
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

	
	
}
