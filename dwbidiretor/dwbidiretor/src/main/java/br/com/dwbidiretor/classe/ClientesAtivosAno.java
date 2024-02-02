package br.com.dwbidiretor.classe;

import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class ClientesAtivosAno implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String gestor;
	private BigDecimal codigovendedor;
	private String nomevendedor;
	
	private String ano;
	private BigInteger janeiro;
	private BigInteger fevereiro;
	private BigInteger marco;
	private BigInteger abril;
	private BigInteger maio;
	private BigInteger junho;
	private BigInteger julho;
	private BigInteger agosto;
	private BigInteger setembro;
	private BigInteger outubro;
	private BigInteger novembro;
	private BigInteger dezembro;

	public ClientesAtivosAno() {
		super();
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
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

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public BigInteger getJaneiro() {
		return janeiro;
	}

	public void setJaneiro(BigInteger janeiro) {
		this.janeiro = janeiro;
	}

	public BigInteger getFevereiro() {
		return fevereiro;
	}

	public void setFevereiro(BigInteger fevereiro) {
		this.fevereiro = fevereiro;
	}

	public BigInteger getMarco() {
		return marco;
	}

	public void setMarco(BigInteger marco) {
		this.marco = marco;
	}

	public BigInteger getAbril() {
		return abril;
	}

	public void setAbril(BigInteger abril) {
		this.abril = abril;
	}

	public BigInteger getMaio() {
		return maio;
	}

	public void setMaio(BigInteger maio) {
		this.maio = maio;
	}

	public BigInteger getJunho() {
		return junho;
	}

	public void setJunho(BigInteger junho) {
		this.junho = junho;
	}

	public BigInteger getJulho() {
		return julho;
	}

	public void setJulho(BigInteger julho) {
		this.julho = julho;
	}

	public BigInteger getAgosto() {
		return agosto;
	}

	public void setAgosto(BigInteger agosto) {
		this.agosto = agosto;
	}

	public BigInteger getSetembro() {
		return setembro;
	}

	public void setSetembro(BigInteger setembro) {
		this.setembro = setembro;
	}

	public BigInteger getOutubro() {
		return outubro;
	}

	public void setOutubro(BigInteger outubro) {
		this.outubro = outubro;
	}

	public BigInteger getNovembro() {
		return novembro;
	}

	public void setNovembro(BigInteger novembro) {
		this.novembro = novembro;
	}

	public BigInteger getDezembro() {
		return dezembro;
	}

	public void setDezembro(BigInteger dezembro) {
		this.dezembro = dezembro;
	}

	

	
}
